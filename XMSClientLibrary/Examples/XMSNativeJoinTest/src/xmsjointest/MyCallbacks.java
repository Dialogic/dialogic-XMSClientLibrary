/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsjointest;

import com.dialogic.XMSClientLibrary.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ssatyana
 */
public class MyCallbacks implements XMSEventCallback {

    String addr = "";
    boolean isDone = false;
    static List<XMSCall> joinList = new ArrayList();
    static int counter = 0;
    static boolean isRecord = false;

    @Override
    public void ProcessEvent(XMSEvent a_event) {
        XMSCall myCall = a_event.getCall();
        switch (a_event.getEventType()) {
            case CALL_CONNECTED:
                System.out.println("connected");
                if (joinList.size() == 1) {
                    joinList.add(myCall);
                    System.out.println("size: " + joinList.size());
                    XMSCall myCall1 = (XMSCall) joinList.get(0);
                    XMSCall myCall2 = (XMSCall) joinList.get(1);
                    myCall1.JoincallOptions.EnableNativeMode(Boolean.TRUE);
                    myCall1.Join(myCall2);
//                    Currently not working for MSML
//                    myCall1.RecordOptions.SetMaxTime(200);
//                    myCall2.RecordOptions.SetMaxTime(200);
//                    myCall1.Record("record1.wav");
//                    myCall2.Record("record2.wav");
                } else {
                    System.out.println("size: " + joinList.size());
                    joinList.add(myCall);
                }
                break;
            case CALL_RECORD_END:
                myCall.Dropcall();
                break;
            case CALL_PLAY_END:
                break;
            case CALL_DISCONNECTED:  // The far end hung up will simply wait for the media

                break;
            case CALL_INFO:
                XMSCall myCall1 = (XMSCall) joinList.get(0);
                XMSCall myCall2 = (XMSCall) joinList.get(1);
                Pattern dialogPattern = Pattern.compile("dialog:(.*)");
                Matcher dialogMatcher = dialogPattern.matcher(myCall.getLastEvent().getInternalData());
                String dialogType = null;
                if (dialogMatcher.find()) {
                    dialogType = dialogMatcher.group(1);
                }
                if (dialogType != null && dialogType.equalsIgnoreCase("Record")) {
                    if (counter == 1) {
                        Sleep(10000);
                        myCall1.Dropcall();
                        myCall2.Dropcall();
                    } else {
                        counter++;
                    }
                } else {
//                    if (!isRecord) {
//                        System.out.println("list: " + joinList);
//                        myCall1.RecordOptions.SetMaxTime(200);
//                        myCall2.RecordOptions.SetMaxTime(200);
//                        myCall1.Record("record1.wav");
//                        myCall2.Record("record2.wav");
//                        isRecord = true;
//                    }
                }
                break;
            default:
                System.out.println("Unknown Event Type!!");
        }

    }

    public static void Sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.out.print(ex);
        }

    }

}
