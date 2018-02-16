/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsjoinplay;

import com.dialogic.XMSClientLibrary.*;

/**
 *
 * @author dwolansk
 */
enum AppState {

    INIT,
    WAITCALL,
    PLAY,
    COLLECT,
    OUTDIAL,
    CONNECT
}

public class JoinPlayDemo implements XMSEventCallback {

    String addr = "";
    AppState myState = AppState.INIT;
    XMSCall inCall;
    XMSCall outCall;
    String outAddr;
    String playfile="./verification/greeting.wav";
    public JoinPlayDemo(XMSCall in, XMSCall out){
        inCall = in;
        outCall = out;
        //Set myself as the callback for these objects
        inCall.EnableAllEvents(this);
        outCall.EnableAllEvents(this);
        
    }
    public void SetOutboundAddr(String addr){
        outAddr = addr;
    }
    public void SetPlayFilename(String file){
        playfile = file;
    }
    public void Start(){
        inCall.WaitcallOptions.SetMediaType(XMSMediaType.VIDEO);
        inCall.WaitcallOptions.EnableAutoConnect(true);
        
        inCall.Waitcall();
    }
    
    public void ProcessIncomingEvent(XMSEvent a_event) {
        //Note: could use inCall directly here, but keeping it getting
        // from event to make more portable
        XMSCall myCall = a_event.getCall();
        String reason = a_event.getReason();
        String data = a_event.getData();
        
        switch (a_event.getEventType()) {
            case CALL_CONNECTED:
                /*
                 myCall.PlayCollectOptions.EnableBarge(true);
                 myCall.PlayCollectOptions.EnableClearDigitBufferOnExecute(true);
                 myCall.PlayCollect(playfile);
                */
                myCall.Play(playfile); 
                 break;
            case CALL_MEDIA_STARTED:
               
                break;
            case CALL_PLAYCOLLECT_END:
            case CALL_PLAY_END:
                outCall.MakecallOptions.EnableRtcpFeedback(true);
                outCall.MakecallOptions.SetMediaType(XMSMediaType.VIDEO);
                outCall.Makecall(outAddr);
                /* Digits can be done async or via the collect operation, in this demo we are using ASYNC
                but including this logic as well.
                
                myCall.CollectDigitsOptions.SetMaxDigits(1);
                myCall.CollectDigitsOptions.SetTimeout(20);
                myCall.CollectDigits();
                break;
            case CALL_COLLECTDIGITS_END:
                
                if(reason.contains("max-digits")  ){
                if(data.contentEquals("1")){
                    System.out.println("We have a 1 making outbound call");
                } else {
                    System.out.println("Digit "+data+" was detected");
                }
                }
                */
                break;
            case CALL_DTMF:
                System.out.println("Digit "+data+" was detected");
                myCall.Stop();
                break;
            case CALL_DISCONNECTED:  // The far end hung up will simply wait for the media
                inCall.Dropcall();
                outCall.Dropcall();
                inCall.Waitcall();
                break;
            case CALL_INFO:
                break;
            default:
                System.out.println("Unknown Incoming Event Type - !!"+a_event.getEventType());
        }
    }
    public void ProcessOutgoingEvent(XMSEvent a_event) {
         XMSCall myCall = a_event.getCall();
        String reason = a_event.getReason();
        String data = a_event.getData();
        
        switch (a_event.getEventType()) {
            case CALL_DISCONNECTED:  // The far end hung up will simply wait for the media
                inCall.Dropcall();
                outCall.Dropcall();
                inCall.Waitcall();
                break;
             case CALL_CONNECTED:
                 outCall.Join(inCall);
                 break;
             default:
                System.out.println("Unknown Outgoing Event Type - !!"+a_event.getEventType());
        }
    }
    @Override
    public void ProcessEvent(XMSEvent a_event) {
        //throw new UnsupportedOperationException("Not supported yet.");
        XMSCall myCall = a_event.getCall();
        if(myCall == inCall){
            ProcessIncomingEvent(a_event);
        } else if (myCall == outCall){
            ProcessOutgoingEvent(a_event);
        } else {
            //Really shouldn't get here
            switch (a_event.getEventType()) {
                default:
                    System.out.println("Unknown Event Type!!");
            }

        }
    }

}
