/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsechotest;

import com.dialogic.XMSClientLibrary.*;

/**
 *
 * @author dwolansk
 */

public class PlayRecTestCase implements XMSEventCallback {

    
    XMSCall myCall;
    String myPlayfile= new String();
    String myRecfile= new String();
    int myID ;
    public PlayRecTestCase(){
        
    }
    public void Start(XMSCall aCall,int aID){
        myCall = aCall;
        myCall.EnableAllEvents(this);
        myCall.Waitcall();
        myID = aID;
        
        myPlayfile ="verification/video_clip_newscast.wav";
        myRecfile = "recorded/recfile_"+myID+".wav";
    }
    @Override
    public void ProcessEvent(XMSEvent a_event) {
        //throw new UnsupportedOperationException("Not supported yet.");
        XMSCall myCall = a_event.getCall();
        switch (a_event.getEventType()) {
            case CALL_CONNECTED:
                myCall.PlayOptions.SetMediaType(XMSMediaType.AUDIO);
                myCall.Play(myPlayfile);
                break;
            case CALL_RECORD_END:
                myCall.Dropcall();
                myCall.Waitcall();
                break;
            case CALL_PLAY_END:               
                myCall.RecordOptions.SetMediaType(XMSMediaType.AUDIO);
                myCall.RecordOptions.SetTimeout(30);
                myCall.RecordOptions.SetMaxTime(30);
                myCall.Record(myRecfile);
                break;
            case CALL_DISCONNECTED:  // The far end hung up will simply wait for the media
                
                myCall.Waitcall();
                break;
            case CALL_INFO:
                break;
            default:
                System.out.println("Unknown Event Type!!");
        }

    }

}
