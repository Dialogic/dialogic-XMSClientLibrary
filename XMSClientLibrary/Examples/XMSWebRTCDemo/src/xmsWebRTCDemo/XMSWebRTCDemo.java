/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsWebRTCDemo;

import com.dialogic.XMSClientLibrary.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMSWebRTCDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        XMSObjectFactory myFactory = new XMSObjectFactory();
        XMSConnector myConnector = myFactory.CreateConnector();
        XMSCall inbound1pcc = myFactory.CreateCall(myConnector);
        XMSCall outbound3pcc = myFactory.CreateCall(myConnector);
        XMSCall inbound3pcc = myFactory.CreateCall(myConnector);

        Properties prop = new Properties();
        String PlayFile = null;
        boolean isVideo = true;

        try {

            //prop.setProperty("PlayFile","./verification/Dialogic_NetworkFuel" );
            //prop.setProperty("isVideo","true" );
            // prop.store(new FileOutputStream("config.properties"), null);
            //load a properties file
            prop.load(new FileInputStream("config.properties"));

            PlayFile = prop.getProperty("PlayFile");
            if (prop.getProperty("isVideo").contains("false")) {
                isVideo = false;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //Make an outbound call to the same address that you just received a call from
        if (isVideo) {
            inbound1pcc.WaitcallOptions.SetMediaType(XMSMediaType.VIDEO);

        }

        inbound1pcc.Waitcall();

        if (inbound1pcc.getState() == XMSCallState.CONNECTED) {
            outbound3pcc.MakecallOptions.EnableSignaling(false);
            outbound3pcc.MakecallOptions.setSdp("");
            outbound3pcc.MakecallOptions.SetMediaType(XMSMediaType.VIDEO);
            outbound3pcc.MakecallOptions.EnableRtcpFeedback(true);
            outbound3pcc.MakecallOptions.EnableIce(true);
            outbound3pcc.MakecallOptions.EnableEncryption(true);

            outbound3pcc.Makecall("");

            String sdp = outbound3pcc.getLastEvent().getData();
            Pattern pattern = Pattern.compile("sdp=\"(.*?)\"");
            Matcher m = pattern.matcher(sdp);
            String outbound3pccSdp = "";
            if (m.find()) {
                System.out.println("SDP: " + m.group(1));
                outbound3pccSdp = m.group(1);

            }

//            Pattern patternMid = Pattern.compile("m=video(.*?)\"");
//            Matcher mMid = patternMid.matcher(sdp);
//
//            if (mMid.find()) {
//
//                String replaced = mMid.group(1).replaceAll("100", "101");
//                outbound3pccSdp = outbound3pccSdp + "a=mid:video&#xD;&#xA;a=extmap:2 urn:ietf:params:rtp-hdrext:toffset&#xD;&#xA;a=extmap:3 http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time&#xD;&#xA;a=extmap:4 urn:3gpp:video-orientation&#xD;&#xA;a=sendrecv&#xD;&#xA;a=rtcp-mux&#xD;&#xA;a=rtpmap:100 VP8/90000&#xD;&#xA;a=rtcp-fb:100 ccm fir&#xD;&#xA;a=rtcp-fb:100 nack&#xD;&#xA;a=rtcp-fb:100 nack pli&#xD;&#xA;a=rtcp-fb:100 goog-remb&#xD;&#xA;a=rtpmap:116 red/90000&#xD;&#xA;a=rtpmap:117 ulpfec/90000&#xD;&#xA;a=rtpmap:96 rtx/90000&#xD;&#xA;a=fmtp:96 apt=100&#xD;&#xA;a=ssrc-group:FID 1389091461 2222512363&#xD;&#xA;a=ssrc:1389091461 cname:64EuHuCczJv+n5hn&#xD;&#xA;a=ssrc:1389091461 msid:Mdk2xwWJG6YhoIWflFowZ6hzAHmvkgas93GU d8a578a2-1ce0-450e-983a-dae59c245399&#xD;&#xA;a=ssrc:1389091461 mslabel:Mdk2xwWJG6YhoIWflFowZ6hzAHmvkgas93GU&#xD;&#xA;a=ssrc:1389091461 label:d8a578a2-1ce0-450e-983a-dae59c245399&#xD;&#xA;a=ssrc:2222512363 cname:64EuHuCczJv+n5hn&#xD;&#xA;a=ssrc:2222512363 msid:Mdk2xwWJG6YhoIWflFowZ6hzAHmvkgas93GU d8a578a2-1ce0-450e-983a-dae59c245399&#xD;&#xA;a=ssrc:2222512363 mslabel:Mdk2xwWJG6YhoIWflFowZ6hzAHmvkgas93GU&#xD;&#xA;a=ssrc:2222512363 label:d8a578a2-1ce0-450e-983a-dae59c245399&#xD;&#xA;";
//                System.out.println("New SDP: " + outbound3pccSdp);
//            }
            inbound3pcc.MakecallOptions.EnableSignaling(false);
            inbound3pcc.MakecallOptions.setSdp(outbound3pccSdp);
            inbound3pcc.MakecallOptions.SetMediaType(XMSMediaType.VIDEO);
            inbound3pcc.MakecallOptions.EnableRtcpFeedback(true);
            inbound3pcc.MakecallOptions.EnableIce(true);
            inbound3pcc.MakecallOptions.EnableEncryption(true);

            inbound3pcc.Makecall("");

            String inbound3pccSdp = "";
            String sdpInbound3pcc = inbound3pcc.getLastEvent().getData();
            Pattern pattern1 = Pattern.compile("sdp=\"(.*?)\"");
            Matcher m1 = pattern1.matcher(sdpInbound3pcc);
            if (m1.find()) {
                System.out.println("SDP2: " + m1.group(1));
                inbound3pccSdp = m1.group(1);
            }

            outbound3pcc.UpdatecallOptions.EnableSignaling(false);
            outbound3pcc.UpdatecallOptions.setSdp(inbound3pccSdp);
            outbound3pcc.UpdatecallOptions.SetMediaType(XMSMediaType.VIDEO);
            outbound3pcc.UpdatecallOptions.EnableRtcpFeedback(true);
            outbound3pcc.UpdatecallOptions.EnableIce(true);
            outbound3pcc.UpdatecallOptions.EnableEncryption(true);

            outbound3pcc.Updatecall();

            //outbound3pcc.Join(inbound1pcc);
            //Playback the file recorded
            if (isVideo) {
                inbound3pcc.PlayOptions.SetMediaType(XMSMediaType.VIDEO);
            }
            //myCall.Play("image:id=menu&header=IVVR Demo&items=1 Play&items=2 Record&items=3 Playback&items=4 Conf&footer=Dialogic");
            inbound3pcc.Play(PlayFile);
        }
        Sleep(50000);
        //Hangup the call
        inbound3pcc.Dropcall();
        outbound3pcc.Dropcall();
        inbound1pcc.Dropcall();

    }

    public static void Sleep(int time) {
        try {

            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.out.print(ex);
        }

    }
}
