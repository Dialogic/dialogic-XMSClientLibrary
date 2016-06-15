/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsOutboundPlayCPA;

import com.dialogic.XMSClientLibrary.*;

/**
 *
 * @author ssatyana
 */
public class MyCallback implements XMSEventCallback {

    boolean isDone = false;

    @Override
    public void ProcessEvent(XMSEvent a_event) {
        //throw new UnsupportedOperationException("Not supported yet.");
        XMSCall myCall = a_event.getCall();
        switch (a_event.getEventType()) {
            case CALL_CONNECTED:
                //myCall.Play("./verification/greeting");
                break;
            case CALL_PLAY_END:
                myCall.Dropcall();
                isDone = true;
                break;
            case CALL_DISCONNECTED:  // The far end hung up will simply wait for the media
                myCall.Dropcall();
                isDone = true;
                break;
            case CALL_INFO:
                System.out.println("Event after connected: " + myCall.getLastEvent());
                if (myCall.getLastEvent().getReason().equalsIgnoreCase("voice")) {
                    System.out.println("Voice detected: " + myCall.getLastEvent());
                    //myCall.Play("./verification/greeting");
                } else if (myCall.getLastEvent().getReason().equalsIgnoreCase("answer-machine")) {
                    System.out.println("Voicemail: " + myCall.getLastEvent());
                    System.out.println("Reason: " + myCall.getLastEvent().getReason());
                    //myCall.Play("./verification/record_intro");
                }
                break;
            default:
                System.out.println("Unknown Event Type!!");
        }
    }
}
