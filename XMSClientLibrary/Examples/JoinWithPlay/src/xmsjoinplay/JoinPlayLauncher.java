/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsjoinplay;

import com.dialogic.XMSClientLibrary.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dwolansk
 */
public class JoinPlayLauncher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        XMSObjectFactory myFactory = new XMSObjectFactory();
        XMSConnector myConnector = myFactory.CreateConnector();
        XMSCall myInCall = myFactory.CreateCall(myConnector);
        XMSCall myOutCall = myFactory.CreateCall(myConnector);
        JoinPlayDemo myDemo = new JoinPlayDemo(myInCall,myOutCall);

        myDemo.SetOutboundAddr("sip:test@192.168.1.71:5066");
        myDemo.SetPlayFilename("./verification/greeting.wav");
        
        myDemo.Start();
    }

}
