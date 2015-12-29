/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsOutboundPlayCPA;

import com.dialogic.XMSClientLibrary.*;

/**
 *
 * @author dwolansk
 */
enum AppState {

    WAITCALL,
    RECORD,
    MAKECALL,
    PLAY
}

public class MyCallback implements XMSEventCallback {

    String addr = "";
    AppState myState = AppState.WAITCALL;
    boolean isDone = false;

    @Override
    public void ProcessEvent(XMSEvent a_event) {
        //throw new UnsupportedOperationException("Not supported yet.");
        XMSCall myCall = a_event.getCall();
        switch (a_event.getEventType()) {
            case CALL_CONNECTED:
                myCall.Play("./verification/greeting");
                break;
            case CALL_PLAY_END:
                myCall.Dropcall();
                isDone = true;
                break;
            case CALL_DISCONNECTED:  // The far end hung up will simply wait for the media

                break;
            case CALL_INFO:
                break;
            default:
                System.out.println("Unknown Event Type!!");
        }
    }
}
