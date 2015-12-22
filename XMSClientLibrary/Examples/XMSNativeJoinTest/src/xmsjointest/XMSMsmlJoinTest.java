/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsjointest;

import com.dialogic.XMSClientLibrary.*;

/**
 *
 * @author ssatyana
 */
public class XMSMsmlJoinTest {

    static int counter = 0;
    static XMSCall myCall1;
    static XMSCall myCall2;

    public static void main(String[] args) {

        XMSObjectFactory myFactory = new XMSObjectFactory();
        XMSConnector myConnector = myFactory.CreateConnector();
        myCall1 = myFactory.CreateCall(myConnector);
        myCall2 = myFactory.CreateCall(myConnector);
        MyCallbacks myCallback1 = new MyCallbacks();
        MyCallbacks myCallback2 = new MyCallbacks();
        myCall1.EnableAllEvents(myCallback1);
        myCall2.EnableAllEvents(myCallback2);

        myCall1.WaitcallOptions.SetMediaType(XMSMediaType.AUDIO);
        myCall1.Waitcall();

        myCall2.WaitcallOptions.SetMediaType(XMSMediaType.AUDIO);
        myCall2.Waitcall();

//        myCall1.JoinMsml(myCall2);
//
//        myCall1.Record("record1.wav");
//        myCall2.Record("record2.wav");
    }
}
