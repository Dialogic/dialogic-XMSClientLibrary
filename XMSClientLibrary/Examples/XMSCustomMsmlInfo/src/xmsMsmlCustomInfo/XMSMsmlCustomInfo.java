/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsMsmlCustomInfo;

import com.dialogic.XMSClientLibrary.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author ssatyana
 */
public class XMSMsmlCustomInfo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        XMSObjectFactory myFactory = new XMSObjectFactory();
        XMSConnector myConnector = myFactory.CreateConnector();
        XMSCall myCall = myFactory.CreateCall(myConnector);

        Properties prop = new Properties();
        String playFile = null;
        boolean isVideo = true;

        try {
            prop.load(new FileInputStream("config.properties"));

            playFile = prop.getProperty("PlayFile");
            if (prop.getProperty("isVideo").contains("false")) {
                isVideo = false;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (isVideo) {
            myCall.WaitcallOptions.SetMediaType(XMSMediaType.VIDEO);
        }
        myCall.Waitcall();

        if (myCall.getState() == XMSCallState.CONNECTED) {
            if (isVideo) {
                myCall.PlayOptions.SetMediaType(XMSMediaType.VIDEO);
            }

            String msml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                    + "<msml version=\"1.1\" xmlns:ns2=\"http://www.dialogic.com/DialogicTypes\">\n"
                    + "    <dialogstart target=\"conn:1234\" type=\"application/moml+xml\" name=\"Play\">\n"
                    + "        <group topology=\"parallel\">\n"
                    + "            <play>\n"
                    + "                <media>\n"
                    + "                    <audio uri=\"file://verification/greeting.wav\" format=\"audio/wav;codec=L16\" audiosamplerate=\"16000\" audiosamplesize=\"16\"/>\n"
                    + "                </media>\n"
                    + "                <playexit>\n"
                    + "                    <exit namelist=\"play.end play.amt\"/>\n"
                    + "                </playexit>\n"
                    + "            </play>\n"
                    + "            <collect>\n"
                    + "                <pattern digits=\"#\">\n"
                    + "                    <send event=\"TermkeyRecieved\" target=\"source\" namelist=\"dtmf.digits dtmf.len dtmf.end\"/>\n"
                    + "                    <send event=\"terminate\" target=\"play\"/>\n"
                    + "                </pattern>\n"
                    + "            </collect>\n"
                    + "        </group>\n"
                    + "    </dialogstart>\n"
                    + "</msml>";

            myCall.SendInfo(msml);

            System.out.println("LAST EVENT RECEIVED -> " + myCall.getLastEvent());
            System.out.println("MSML RESPONSE -> " + myCall.getLastEvent().getInternalData());
        }
        Sleep(5000);
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
