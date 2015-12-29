/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsOutboundPlayCPA;

import com.dialogic.XMSClientLibrary.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author ssatyana
 */
public class XMSOutboundPlayCPA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        XMSObjectFactory myFactory = new XMSObjectFactory();
        XMSConnector myConnector = myFactory.CreateConnector();
        XMSCall myCall = myFactory.CreateCall(myConnector);
        XMSCall incomingCall = myFactory.CreateCall(myConnector);
        MyCallback myCallback = new MyCallback();
        
        incomingCall.EnableAllEvents(myCallback);
        
        Properties prop = new Properties();
        String OutboundAddress = null;
        String PlayFile = null;
        boolean isVideo = true;
        
        try {

            //prop.setProperty("OutboundAddress","rtc:Test" );
            //prop.setProperty("PlayFile","./verification/Dialogic_NetworkFuel" );
            //prop.setProperty("isVideo","true" );
            // prop.store(new FileOutputStream("config.properties"), null);
            //load a properties file
            prop.load(new FileInputStream("config.properties"));
            
            OutboundAddress = prop.getProperty("OutboundAddress");
//            PlayFile = prop.getProperty("PlayFile");
            if (prop.getProperty("isVideo").contains("false")) {
                isVideo = false;
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (isVideo) {
            incomingCall.WaitcallOptions.SetMediaType(XMSMediaType.VIDEO);
        }
        incomingCall.Waitcall();
        
        if (isVideo) {
            myCall.MakecallOptions.SetMediaType(XMSMediaType.VIDEO);
        }
        myCall.MakecallOptions.EnableCPA(true);
        myCall.Makecall(OutboundAddress);
        
        if (myCall.getState() == XMSCallState.CONNECTED) {
            System.out.println("Event after connected: "+myCall.getLastEvent());
            if(myCall.getLastEvent().getReason().equalsIgnoreCase("voice")) {
                System.out.println("Voice detected: "+myCall.getLastEvent());
                myCall.Play("./verification/greeting");
            } else if(myCall.getLastEvent().getReason().equalsIgnoreCase("answer-machine")){
                System.out.println("Voicemail: "+myCall.getLastEvent());
                System.out.println("Reason: "+myCall.getLastEvent().getReason());
                myCall.Play("./verification/record_intro");
            }
        } else if(myCall.getLastEvent().getData().equalsIgnoreCase("1")) {
            System.out.println("DTMF deteched: "+myCall.getLastEvent().getData());
            // transfer
        } else {
            myCall.Play("./verification/verification_intro");
        }
        Sleep(10000);
        //Hangup the call
        myCall.Dropcall();
        
    }
    
    public static void Sleep(int time) {
        try {
            
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.out.print(ex);
        }
        
    }
}
