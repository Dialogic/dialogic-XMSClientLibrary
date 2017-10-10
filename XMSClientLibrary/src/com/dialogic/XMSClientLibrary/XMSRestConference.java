/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.XMSClientLibrary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Observable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.xmlbeans.XmlNMTOKEN;

import com.dialogic.xms.*;
import com.dialogic.xms.EventDataDocument.*;

/**
 *
 * @author dwolansk
 */
public class XMSRestConference extends XMSConference {

    private static Logger m_logger = Logger.getLogger(XMSRestConference.class.getName());
    /* Logger information */

    private XMSRestPendingTransactionInfo m_pendingtransactionInfo = new XMSRestPendingTransactionInfo();

    private XMSRestConnector m_restconnector;

    public XMSRestConference() {
        m_type = "REST";
        m_Name = "XMSRestConference:" + m_objectcounter++;

        PropertyConfigurator.configure("log4j.properties");
        //m_logger.setLevel(Level.ALL);
        m_logger.info("Creating " + m_Name);

    }

    /**
     * CTor for the REST call object. THis will both create and tie the object
     * into the XMSConnector
     *
     * @param a_connector
     */
    /**
     * CTor for the REST call object. THis will both create and tie the object
     * into the XMSConnector
     *
     * @param a_connector
     */
    public XMSRestConference(XMSConnector a_connector) {
        m_callIdentifier = null;
        m_Name = "XMSConference:" + m_objectcounter++;
        PropertyConfigurator.configure("log4j.properties");
        //m_logger.setLevel(Level.ALL);
        m_logger.info("Creating " + m_Name);

        m_type = a_connector.getType();
        m_restconnector = (XMSRestConnector) a_connector;
        m_connector = a_connector;
        Initialize();
        m_logger.info("Adding Myself as an Observer");
        //    this.addObserver(this);

    }

    public XMSReturnCode Create() {
        FunctionLogger logger = new FunctionLogger("Create", this, m_logger);
        logger.args("ConferenceOptions=" + ConferenceOptions);

        String XMLPAYLOAD;
        SendCommandResponse RC;

        XMLPAYLOAD = buildCreateConferencePayload();

        /*
         XMLPAYLOAD = "<web_service version=\"1.0\"> "+
         " <conference "+
         " layout=\"0\""+
         " >"+
         " </conference>"+
         " </web_service>";
         * 
         */
        RC = m_connector.SendCommand(this, RESTOPERATION.POST, "conferences", XMLPAYLOAD);

        if (RC.get_scr_status_code() == 201) {
            logger.info("Conference Create Success, ID: " + RC.get_scr_identifier());
            SetCallIdentifier(RC.get_scr_identifier());
        } else {

            logger.info("Conference Create Failed, Status Code: " + RC.get_scr_status_code());
            return XMSReturnCode.FAILURE;

        }

        return XMSReturnCode.SUCCESS;
    }

    @Override
    public XMSReturnCode Add(XMSCall a_call) {
        FunctionLogger logger = new FunctionLogger("Add", this, m_logger);
        logger.args("Call=" + a_call + "  ConferenceOptions=" + ConferenceOptions);
        if (this.getCallIdentifier() == null) {
            logger.info("CallIdentifier is null, making conference");
            if (Create() == XMSReturnCode.FAILURE) {
                return XMSReturnCode.FAILURE;
            }

        }
        logger.info("CallIdentifier " + this.getCallIdentifier() + " found");
        if (a_call instanceof XMSRestCall) {
            if (((XMSRestCall) a_call).AddToConference(this) == XMSReturnCode.FAILURE) {
                return XMSReturnCode.FAILURE;
            }
            m_partylist.add(a_call);
            //a_call.addObserver(this);
        } else {
            return XMSReturnCode.FAILURE;
        }
        return XMSReturnCode.SUCCESS;
    }

    @Override
    public XMSReturnCode updateConf() {
        FunctionLogger logger = new FunctionLogger("Stop", this, m_logger);

        String XMLPAYLOAD;
        SendCommandResponse RC;

        String l_urlext;
        l_urlext = "conferences/" + m_callIdentifier;

        XMLPAYLOAD = buildUpdateConfPayload();

        //logger.info("Sending message ---->  " + XMLPAYLOAD);
        RC = m_connector.SendCommand(this, RESTOPERATION.PUT, l_urlext, XMLPAYLOAD);

        if (RC.get_scr_status_code() == 200) {

            return XMSReturnCode.SUCCESS;

        } else {

            logger.info("Stop Failed, Status Code: " + RC.get_scr_status_code());
            //setState(XMSCallState.NULL);
            return XMSReturnCode.FAILURE;

        }
    }

    @Override
    public XMSReturnCode Remove(XMSCall a_call) {
        FunctionLogger logger = new FunctionLogger("Remove", this, m_logger);
        logger.args("Call=" + a_call);
        //TODO- Add checkt to make sure that a_call is valid and is still in the caller list.
        if (a_call instanceof XMSRestCall) {
            m_partylist.remove(a_call);
            if (((XMSRestCall) a_call).RemoveFromConference(this) == XMSReturnCode.FAILURE) {
                if (GetPartyCount() == 0 && ConferenceOptions.m_DestroyWhenEmpty) {
                    Destroy();
                }
                return XMSReturnCode.FAILURE;

            }

        } else {
            return XMSReturnCode.FAILURE;
        }

        if (GetPartyCount() == 0 && ConferenceOptions.m_DestroyWhenEmpty) {
            Destroy();
        }
        return XMSReturnCode.SUCCESS;

    } // end remove

    public XMSReturnCode Destroy() {
        FunctionLogger logger = new FunctionLogger("Destroy", this, m_logger);

        if (this.getCallIdentifier() == null) {
            logger.info("Call identifier is null");
            return XMSReturnCode.SUCCESS;
        }

        for (XMSCall call : m_partylist) {

            ((XMSRestCall) call).RemoveFromConference(this);
        }

        SendCommandResponse RC;

        String l_urlext;
        l_urlext = "conferences/" + m_callIdentifier;
        RC = m_connector.SendCommand(this, RESTOPERATION.DELETE, l_urlext, null);

        if (RC.get_scr_status_code() == 204) {
            logger.info("Conference Delete Success, ID: " + RC.get_scr_identifier());
            this.m_connector.RemoveCallFromActiveCallList(m_callIdentifier);
            m_callIdentifier = null;
            m_partylist.clear();
        } else {

            logger.info("Conference Delete Failed, Status Code: " + RC.get_scr_status_code());

            return XMSReturnCode.FAILURE;

        }

        return XMSReturnCode.SUCCESS;
    }

    /**
     * Play a file
     *
     * @param a_filelist - A list of Files to be played
     * @return
     */
    @Override
    public XMSReturnCode PlayRegion(String a_file, String a_Region) {
        FunctionLogger logger = new FunctionLogger("PlayRegion", this, m_logger);
        logger.args("Region " + a_Region + " Playfile " + a_file + " " + PlayOptions);
        String XMLPAYLOAD;
        SendCommandResponse RC;

        String l_urlext;
        l_urlext = "conferences/" + m_callIdentifier;

        ArrayList<String> l_playlist = new ArrayList<String>();
        l_playlist.add(a_file);

        XMLPAYLOAD = buildPlayPayload(l_playlist, a_Region);

        //logger.info("Sending message ---->  " + XMLPAYLOAD);
        RC = m_connector.SendCommand(this, RESTOPERATION.PUT, l_urlext, XMLPAYLOAD);

        if (RC.get_scr_status_code() == 200) {
            m_pendingtransactionInfo.setDescription("Play file(s)=" + l_playlist);
            m_pendingtransactionInfo.setTransactionId(RC.get_scr_transaction_id());
            m_pendingtransactionInfo.setResponseData(RC);

            //setState(XMSCallState.PLAY);
            try {
                BlockIfNeeded(XMSEventType.CALL_PLAY_END);
            } catch (InterruptedException ex) {
                logger.error("Exception:" + ex);
            }

            return XMSReturnCode.SUCCESS;

        } else {

            logger.info("Play Failed, Status Code: " + RC.get_scr_status_code());

            //setState(XMSCallState.NULL);
            return XMSReturnCode.FAILURE;

        }

    } // end play

    /**
     * Play a file
     *
     * @param a_filelist - A list of Files to be played
     * @return
     */
    @Override
    public XMSReturnCode Play(String a_file) {
        FunctionLogger logger = new FunctionLogger("PlayList", this, m_logger);
        logger.args("Playfile " + a_file + " " + PlayOptions);
        String XMLPAYLOAD;
        SendCommandResponse RC;

        String l_urlext;
        l_urlext = "conferences/" + m_callIdentifier;

        ArrayList<String> l_playlist = new ArrayList<String>();
        l_playlist.add(a_file);

        XMLPAYLOAD = buildPlayPayload(l_playlist, "0");

        //logger.info("Sending message ---->  " + XMLPAYLOAD);
        RC = m_connector.SendCommand(this, RESTOPERATION.PUT, l_urlext, XMLPAYLOAD);

        if (RC.get_scr_status_code() == 200) {
            m_pendingtransactionInfo.setDescription("Play file(s)=" + l_playlist);
            m_pendingtransactionInfo.setTransactionId(RC.get_scr_transaction_id());
            m_pendingtransactionInfo.setResponseData(RC);

            //setState(XMSCallState.PLAY);
            try {
                BlockIfNeeded(XMSEventType.CALL_PLAY_END);
            } catch (InterruptedException ex) {
                logger.error("Exception:" + ex);
            }

            return XMSReturnCode.SUCCESS;

        } else {

            logger.info("Play Failed, Status Code: " + RC.get_scr_status_code());

            //setState(XMSCallState.NULL);
            return XMSReturnCode.FAILURE;

        }

    } // end play

    /**
     * Record a file
     *
     * @param a_file - File to record
     * @return
     */
    @Override
    public XMSReturnCode Record(String a_file) {
        FunctionLogger logger = new FunctionLogger("Record", this, m_logger);
        logger.args("Record " + a_file + " " + RecordOptions);
        String XMLPAYLOAD;
        SendCommandResponse RC;

        String l_urlext;
        l_urlext = "conferences/" + m_callIdentifier;

        XMLPAYLOAD = buildRecordPayload(a_file);

        //logger.info("Sending message ---->  " + XMLPAYLOAD);
        RC = m_connector.SendCommand(this, RESTOPERATION.PUT, l_urlext, XMLPAYLOAD);

        if (RC.get_scr_status_code() == 200) {
            m_pendingtransactionInfo.setDescription("Record file=" + a_file);
            m_pendingtransactionInfo.setTransactionId(RC.get_scr_transaction_id());
            m_pendingtransactionInfo.setResponseData(RC);

            //setState(XMSCallState.PLAY);
            try {
                BlockIfNeeded(XMSEventType.CALL_RECORD_END);
            } catch (InterruptedException ex) {
                logger.error("Exception:" + ex);
            }

            return XMSReturnCode.SUCCESS;

        } else {

            logger.info("Record Failed, Status Code: " + RC.get_scr_status_code());

            //setState(XMSCallState.NULL);
            return XMSReturnCode.FAILURE;

        }

    } // end record

    @Override
    public XMSReturnCode MultiRecord(String a_file) {
        FunctionLogger logger = new FunctionLogger("Record", this, m_logger);
        logger.args("Record " + a_file + " " + RecordOptions);
        String XMLPAYLOAD;
        SendCommandResponse RC;

        String l_urlext;
        l_urlext = "conferences/" + m_callIdentifier;

        XMLPAYLOAD = buildMultiRecordPayload(a_file);

        //logger.info("Sending message ---->  " + XMLPAYLOAD);
        RC = m_connector.SendCommand(this, RESTOPERATION.PUT, l_urlext, XMLPAYLOAD);

        if (RC.get_scr_status_code() == 200) {
            m_pendingtransactionInfo.setDescription("Record file=" + a_file);
            m_pendingtransactionInfo.setTransactionId(RC.get_scr_transaction_id());
            m_pendingtransactionInfo.setResponseData(RC);

            //setState(XMSCallState.PLAY);
            try {
                BlockIfNeeded(XMSEventType.CALL_RECORD_END);
            } catch (InterruptedException ex) {
                logger.error("Exception:" + ex);
            }

            return XMSReturnCode.SUCCESS;

        } else {

            logger.info("Record Failed, Status Code: " + RC.get_scr_status_code());

            //setState(XMSCallState.NULL);
            return XMSReturnCode.FAILURE;

        }

    }

    /**
     * Force the Stop of the last active IO function
     *
     * @return
     */
    @Override
    public XMSReturnCode Stop() {
        FunctionLogger logger = new FunctionLogger("Stop", this, m_logger);

        String XMLPAYLOAD;
        SendCommandResponse RC;

        String l_urlext;
        l_urlext = "conferences/" + m_callIdentifier;

        XMLPAYLOAD = buildStopPayload(m_pendingtransactionInfo.getTransactionId());

        //logger.info("Sending message ---->  " + XMLPAYLOAD);
        RC = m_connector.SendCommand(this, RESTOPERATION.PUT, l_urlext, XMLPAYLOAD);

        if (RC.get_scr_status_code() == 200) {

            return XMSReturnCode.SUCCESS;

        } else {

            logger.info("Stop Failed, Status Code: " + RC.get_scr_status_code());
            //setState(XMSCallState.NULL);
            return XMSReturnCode.FAILURE;

        }

    }// end Stop

    /**
     * This is the Notify handler that will be called by EventThread when new
     * events are created.
     *
     * @param obj
     * @param arg
     */
    @Override
    public void update(Observable obj, Object arg) {
        FunctionLogger logger = new FunctionLogger("update", this, m_logger);
        logger.args("obj=" + obj + " arg=" + arg);
        XMSEvent l_callbackevt = new XMSEvent();
        if (arg instanceof XMSRestEvent) {
            XMSRestEvent l_evt = (XMSRestEvent) arg;
            //TODO This Method is getting a little combersome, may want to extend out to private OnXXXXEvent functions to segment the readability.
            //TODO Should we support the Ringing event?
            if (l_evt.eventType.contentEquals("end_play")) {
                logger.info("Processing play end event");
                m_pendingtransactionInfo.Reset();
                //setState(XMSCallState.CONNECTED);
                EventData[] l_datalist = l_evt.event.getEventDataArray();
                for (EventDataDocument.EventData ed : l_datalist) {
                    if (ed.getName().contentEquals(EventDataName.REASON.toString())) {
                        l_callbackevt.setReason(ed.getValue());
                    }
                }
                l_callbackevt.CreateEvent(XMSEventType.CALL_PLAY_END, this, "", "", l_evt.toString());
                UnblockIfNeeded(l_callbackevt);
                //end end_play

            } else if (l_evt.eventType.contentEquals("media_started")) {
                logger.info("Processing media_started event");

                EventData[] l_datalist = l_evt.event.getEventDataArray(); // 27-Jul-2012 dsl
                for (EventDataDocument.EventData ed : l_datalist) {                         // 27-Jul-2012 dsl
                    if (ed.getName().contentEquals(EventDataName.REASON.toString())) {                           // 27-Jul-2012 dsl
                        l_callbackevt.setReason(ed.getValue());                          // 27-Jul-2012 dsl
                    }
                }
                l_callbackevt.CreateEvent(XMSEventType.CALL_MEDIA_STARTED, this, "", l_callbackevt.getReason(), l_evt.toString()); // 30-Jul-2012 dsl
                PostCallEvent(l_callbackevt);
                //end stream state
            } else {
                logger.info("Unprocessed event type: " + l_evt.eventType);
            }
        }
    }

    // Building Methods
    /**
     * Build the payload string to make a new conference
     *
     *
     * @return Payload string
     *
     */
    private String buildCreateConferencePayload() {

        FunctionLogger logger = new FunctionLogger("buildCreateConferencePayload", this, m_logger);
        String l_rqStr = "";

        WebServiceDocument l_WMSdoc;
        WebServiceDocument.WebService l_WMS;
        XmlNMTOKEN l_ver;

        // Create a new Web Service Doc Instance
        l_WMSdoc = WebServiceDocument.Factory.newInstance();
        l_WMS = l_WMSdoc.addNewWebService();

        // Create a new XMLToken Instance
        l_ver = XmlNMTOKEN.Factory.newInstance();
        l_ver.setStringValue("1.0");
        l_WMS.xsetVersion(l_ver);

        // Create a conf instance
        ConferenceDocument.Conference l_conf;

        l_conf = l_WMS.addNewConference();

        l_conf.setMaxParties(ConferenceOptions.m_MaxParties);

        switch (ConferenceOptions.m_layoutSizeOption) {
            case AUTOMATIC:
                l_conf.setLayoutSize(LayoutSizeOption.AUTOMATIC);
                break;
            case QCIF:
                l_conf.setLayoutSize(LayoutSizeOption.QCIF);
                break;
            case CIF:
                l_conf.setLayoutSize(LayoutSizeOption.CIF);
                break;
            case VGA:
                l_conf.setLayoutSize(LayoutSizeOption.VGA);
                break;
            case X_720_P:
                l_conf.setLayoutSize(LayoutSizeOption.X_720_P);
                break;
            default:
                System.out.println("Unknown layout size option");
                break;
        }
        
        l_conf.setLayout(Integer.toString(ConferenceOptions.m_Layout.getValue()));

        if (ConferenceOptions.m_mixingMode.equals(XMSConfMixingMode.MCU)) {
            l_conf.setMixingMode(ConfMixingMode.MCU);
        } else if (ConferenceOptions.m_mixingMode.equals(XMSConfMixingMode.SFU)) {
            l_conf.setMixingMode(ConfMixingMode.SFU);
        }

        //l_conf.setActiveTalkerInterval(ConferenceOptions.m_active_talker_interval);
        if (!ConferenceOptions.m_active_talker_region.isEmpty()) {
            l_conf.setActiveTalkerRegion(ConferenceOptions.m_active_talker_region);
        }

        if (ConferenceOptions.m_CaptionEnabled) {

            l_conf.setCaption(BooleanType.YES);
        } else {
            l_conf.setCaption(BooleanType.NO);

        } // end if
        //TODO get setCaptionDuration working
        l_conf.setCaptionDuration("" + ConferenceOptions.m_CaptionDuration);

        if (ConferenceOptions.m_BeepEnabled) {

            l_conf.setBeep(BooleanType.YES);
        } else {
            l_conf.setBeep(BooleanType.NO);

        } // end if
        if (ConferenceOptions.m_DigitClampingEnabled) {

            l_conf.setClampDtmf(BooleanType.YES);
        } else {
            l_conf.setClampDtmf(BooleanType.NO);

        } // end if

        if (ConferenceOptions.m_AGCEnabled) {

            l_conf.setAutoGainControl(BooleanType.YES);
        } else {
            l_conf.setAutoGainControl(BooleanType.NO);

        } // end if

        if (ConferenceOptions.m_ECEnabled) {

            l_conf.setEchoCancellation(BooleanType.YES);
        } else {
            l_conf.setEchoCancellation(BooleanType.NO);

        } // end if

        // Set Media getType parm
        switch (ConferenceOptions.m_MediaType) {
            case AUDIO:
                l_conf.setType(MediaType.AUDIO);
                //l_conf.setMedia(MediaType.AUDIO);
                break;
            case VIDEO:
                l_conf.setType(MediaType.AUDIOVIDEO);
                //  l_conf.setMedia(MediaType.AUDIOVIDEO);
                break;
            case UNKNOWN:
                l_conf.setType(MediaType.UNKNOWN);
            //l_conf.setMedia(MediaType.UNKNOWN);
        } // end switch

        //logger.debug("RAW REST generated...." + l_WMS.toString());
        ByteArrayOutputStream l_newDialog = new ByteArrayOutputStream();

        try {
            l_WMSdoc.save(l_newDialog);
            l_rqStr = l_WMSdoc.toString();

        } catch (IOException ex) {
            logger.error(ex);
        }

        //logger.debug ("Returning Payload:\n " + l_rqStr);
        return l_rqStr;  // Return the requested string...

    } // end buildMakecallPayload

    private String buildUpdateConfPayload() {

        FunctionLogger logger = new FunctionLogger("buildCreateConferencePayload", this, m_logger);
        String l_rqStr = "";

        WebServiceDocument l_WMSdoc;
        WebServiceDocument.WebService l_WMS;
        XmlNMTOKEN l_ver;

        // Create a new Web Service Doc Instance
        l_WMSdoc = WebServiceDocument.Factory.newInstance();
        l_WMS = l_WMSdoc.addNewWebService();

        // Create a new XMLToken Instance
        l_ver = XmlNMTOKEN.Factory.newInstance();
        l_ver.setStringValue("1.0");
        l_WMS.xsetVersion(l_ver);

        // Create a conf instance
        ConferenceDocument.Conference l_conf;

        l_conf = l_WMS.addNewConference();
        l_conf.setPrimaryVideoSource("vas");
        //logger.debug("RAW REST generated...." + l_WMS.toString());
        ByteArrayOutputStream l_newDialog = new ByteArrayOutputStream();

        try {
            l_WMSdoc.save(l_newDialog);
            l_rqStr = l_WMSdoc.toString();

        } catch (IOException ex) {
            logger.error(ex);
        }

        //logger.debug ("Returning Payload:\n " + l_rqStr);
        return l_rqStr;  // Return the requested string...

    }

    /**
     * CLASS TYPE : private METHOD : buildPlayPayload
     *
     * DESCRIPTION : Build the payload string to play a file(s). Supports
     * multiple files
     *
     * PARAMETERS : Array List <String> = playlist
     *
     *
     * RETURN : Payload string
     *
     * Author(s) : r.moses Created : 21-May-2012 Updated : 31-May-2012
     *
     *
     * HISTORY :
     * ***********************************************************************
     */
    private String buildPlayPayload(ArrayList<String> a_playlist, String a_Region) {
        FunctionLogger logger = new FunctionLogger("buildPlayPayload", this, m_logger);
        String l_rqStr = "";
        String uriString = "";

        WebServiceDocument l_WMSdoc;
        WebServiceDocument.WebService l_WMS;

        XmlNMTOKEN l_ver;

        ConferenceDocument.Conference l_conf;
        ConfActionDocument.ConfAction l_confAction;
        //CallActionDocument.CallAction l_callAction; // Call Action instance

        PlayDocument.Play l_play;
        PlaySourceDocument.PlaySource l_playSource;

        // Create a new Web Service Doc Instance
        l_WMSdoc = WebServiceDocument.Factory.newInstance();
        l_WMS = l_WMSdoc.addNewWebService();

        // Create a new XMLToken Instance
        l_ver = XmlNMTOKEN.Factory.newInstance();
        l_ver.setStringValue("1.0");
        l_WMS.xsetVersion(l_ver);

        // add a new call
        l_conf = l_WMS.addNewConference();

        // Add a new Call Action to the call
        l_confAction = l_conf.addNewConfAction();

        // Add a new Play to the callAction
        l_play = l_confAction.addNewPlay();

        // Add the play properties
        // Hard code these for now..
        l_play.setOffset(PlayOptions.m_offset); // Hard code this for now..
        l_play.setDelay(PlayOptions.m_delay); // Hard code this for now..
        l_play.setRepeat(PlayOptions.m_repeat); // Hard code this for now..

        l_play.setTerminateDigits(PlayOptions.m_terminateDigits); // Hard code this for now..
        //l_play.setTransactionId(getCallIdentifier()+"_"+m_transactionId++); // Hard code this for now..
        l_play.setRegion(a_Region);
        // Setup your play list and put it in playsource
        for (int i = 0; i < a_playlist.size(); i++) {
//                logger.debug("playList entry["+i+"] - " + uriString);

            // Add a new playsource to the play
            l_playSource = l_play.addNewPlaySource();

            uriString = (a_playlist.get(i));
            String l_uristring = uriString;

            if (this.PlayOptions.m_mediaType == XMSMediaType.IMAGE) {
                if (!l_uristring.startsWith("image:")) {
                    l_uristring = "image:" + l_uristring;
                }
                l_playSource.setLocation(l_uristring);
            } else {
                if (uriString.toLowerCase().endsWith(".wav") || uriString.toLowerCase().endsWith(".vid")) {
                    l_uristring = uriString.substring(0, uriString.length() - 4);
                }
                // TO DO: May need to append the MediaDefaultDirectory

                l_playSource.setLocation("file://" + l_uristring);
                //              logger.debug("Added [" + uriString + "]");
            }
        }

        //logger.debug("RAW REST generated...." + l_WMS.toString());
        ByteArrayOutputStream l_newDialog = new ByteArrayOutputStream();

        try {
            l_WMSdoc.save(l_newDialog);
            l_rqStr = l_WMSdoc.toString();

        } catch (IOException ex) {
            logger.error(ex);
        }

        //logger.debug ("Returning Payload:\n " + l_rqStr);
        return l_rqStr;  // Return the requested string...
    } // end buildPlayPayload

    private String buildRecordPayload(String a_file) {
        FunctionLogger logger = new FunctionLogger("buildRecordPayload", this, m_logger);
        String l_rqStr = "";
        String uriString = "";

        WebServiceDocument l_WMSdoc;
        WebServiceDocument.WebService l_WMS;

        XmlNMTOKEN l_ver;

        ConferenceDocument.Conference l_conf;
        ConfActionDocument.ConfAction l_confAction;
        //CallActionDocument.CallAction l_callAction; // Call Action instance

        RecordDocument.Record l_rec;

        // Create a new Web Service Doc Instance
        l_WMSdoc = WebServiceDocument.Factory.newInstance();
        l_WMS = l_WMSdoc.addNewWebService();

        // Create a new XMLToken Instance
        l_ver = XmlNMTOKEN.Factory.newInstance();
        l_ver.setStringValue("1.0");
        l_WMS.xsetVersion(l_ver);

        // add a new call
        l_conf = l_WMS.addNewConference();

        // Add a new Call Action to the call
        l_confAction = l_conf.addNewConfAction();

        // Add a new Record to the callAction
        l_rec = l_confAction.addNewRecord();

        l_rec.setTerminateDigits(RecordOptions.m_terminateDigits);
        if (RecordOptions.m_maxTime != null && !RecordOptions.m_maxTime.isEmpty()) {
            l_rec.setMaxTime(RecordOptions.m_timeoutValue);
        }

        RecordingAudioMimeParamsDocument.RecordingAudioMimeParams audiomime = l_rec.addNewRecordingAudioMimeParams();
//        audiomime.setCodec(AudioCodecOption.OPUS);
//        //audiomime.setRate(AudioRateOption.X_8000);

        switch (RecordOptions.m_audioMimeCodec) {
            case AMR:
                audiomime.setCodec(AudioCodecOption.AMR);
                break;
            case L_8:
                audiomime.setCodec(AudioCodecOption.L_8);
                break;
            case L_16:
                audiomime.setCodec(AudioCodecOption.L_16);
                break;
            case MULAW:
                audiomime.setCodec(AudioCodecOption.MULAW);
                break;
            case ALAW:
                audiomime.setCodec(AudioCodecOption.ALAW);
                break;
            case AMR_WB:
                audiomime.setCodec(AudioCodecOption.AMR_WB);
                break;
            case OPUS:
                audiomime.setCodec(AudioCodecOption.OPUS);
                break;
            case NATIVE:
                audiomime.setCodec(AudioCodecOption.NATIVE);
                break;
            default:
                System.out.println("Unknown audio mime codec");
                break;
        }

        switch (RecordOptions.m_audioMimeRate) {
            case X_8000:
                audiomime.setRate(AudioRateOption.X_8000);
                break;
            case X_11025:
                audiomime.setRate(AudioRateOption.X_11025);
                break;
            case X_16000:
                audiomime.setRate(AudioRateOption.X_16000);
                break;
            default:
                System.out.println("Unknown audio mime rate");
                break;
        }

        if (RecordOptions.m_audioMimeMode != null && !RecordOptions.m_audioMimeMode.isEmpty()) {
            audiomime.setMode(RecordOptions.m_audioMimeMode);
        }

        l_rec.setRecordingAudioUri("file://" + a_file);
//        l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_WEBM);
//
        RecordingVideoMimeParamsDocument.RecordingVideoMimeParams videomime = l_rec.addNewRecordingVideoMimeParams();

        if (RecordOptions.m_videoMimeHeight != null && !RecordOptions.m_videoMimeHeight.isEmpty()) {
            videomime.setHeight(RecordOptions.m_videoMimeHeight);
        }

        if (RecordOptions.m_videoMimeWidth != null && !RecordOptions.m_videoMimeWidth.isEmpty()) {
            videomime.setHeight(RecordOptions.m_videoMimeWidth);
        }

        if (RecordOptions.m_videoMimeFramerate != null && !RecordOptions.m_videoMimeFramerate.isEmpty()) {
            videomime.setHeight(RecordOptions.m_videoMimeFramerate);
        }

        if (RecordOptions.m_videoMimeMaxbitrate != null && !RecordOptions.m_videoMimeMaxbitrate.isEmpty()) {
            videomime.setHeight(RecordOptions.m_videoMimeMaxbitrate);
        }

        if (RecordOptions.m_videoMimeLevel != null && !RecordOptions.m_videoMimeLevel.isEmpty()) {
            videomime.setHeight(RecordOptions.m_videoMimeLevel);
        }

        if (RecordOptions.m_videoMimeProfile != null && !RecordOptions.m_videoMimeProfile.isEmpty()) {
            videomime.setHeight(RecordOptions.m_videoMimeProfile);
        }

        switch (RecordOptions.m_videoMimeCodec) {
            case H_264:
                videomime.setCodec(VideoCodecOption.H_264);
                break;
            case H_263:
                videomime.setCodec(VideoCodecOption.H_263);
                break;
            case H_263_1998:
                videomime.setCodec(VideoCodecOption.H_263_1998);
                break;
            case MP_4_V_ES:
                videomime.setCodec(VideoCodecOption.MP_4_V_ES);
                break;
            case JPEG:
                videomime.setCodec(VideoCodecOption.JPEG);
                break;
            case VP_8:
                videomime.setCodec(VideoCodecOption.VP_8);
                break;
            case VP_9:
                videomime.setCodec(VideoCodecOption.VP_9);
                break;
            case NATIVE:
                videomime.setCodec(VideoCodecOption.NATIVE);
                break;
            default:
                System.out.println("Unknown video mime codec");
                break;
        }

        switch (RecordOptions.m_audioTypeOption) {
            case AUDIO_X_WAV:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_X_WAV);
                break;
            case AUDIO_BASIC:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_BASIC);
                break;
            case AUDIO_X_ALAW_BASIC:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_X_ALAW_BASIC);
                break;
            case AUDIO_L_8:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_L_8);
                break;
            case AUDIO_L_16:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_L_16);
                break;
            case AUDIO_X_AUD:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_X_AUD);
                break;
            case AUDIO_AMR:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_AMR);
                break;
            case AUDIO_AMR_WB:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_AMR_WB);
                break;
            case AUDIO_3_GPP:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_3_GPP);
                break;
            case AUDIO_MP_4:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_MP_4);
                break;
            case AUDIO_MKV:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_MKV);
                break;
            case AUDIO_WEBM:
                l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_WEBM);
                break;
            case TEXT_URI_LIST:
                l_rec.setRecordingAudioType(AudioTypeOption.TEXT_URI_LIST);
                break;
            default:
                System.out.println("Unknown audio type option");
                break;
        }

        if (RecordOptions.m_mediaType.equals(XMSMediaType.VIDEO)) {
            l_rec.setRecordingVideoUri("file://" + a_file);

            switch (RecordOptions.m_videoTypeOption) {
                case VIDEO_X_VID:
                    l_rec.setRecordingVideoType(RecordingVideoTypeOption.VIDEO_X_VID);
                    break;
                case VIDEO_3_GPP:
                    l_rec.setRecordingVideoType(RecordingVideoTypeOption.VIDEO_3_GPP);
                    break;
                case IMAGE_JPEG:
                    l_rec.setRecordingVideoType(RecordingVideoTypeOption.IMAGE_JPEG);
                    break;
                case VIDEO_MP_4:
                    l_rec.setRecordingVideoType(RecordingVideoTypeOption.VIDEO_MP_4);
                    break;
                case VIDEO_MKV:
                    l_rec.setRecordingVideoType(RecordingVideoTypeOption.VIDEO_MKV);
                    break;
                case VIDEO_WEBM:
                    l_rec.setRecordingVideoType(RecordingVideoTypeOption.VIDEO_WEBM);
                    break;
                default:
                    System.out.println("Unknown video type option");
                    break;

            }
        }

//        videomime.setHeight("480");
//        videomime.setWidth("640");
//        videomime.setCodec(VideoCodecOption.VP_8);
//        videomime.setFramerate("15");
//        videomime.setMaxbitrate("768000");
//        videomime.setLevel("3.1");
//        videomime.setProfile("66");
//        l_rec.setRecordingVideoMimeParams(videomime);
//
//        l_rec.setRecordingVideoUri("file://" + a_file);
//        l_rec.setRecordingVideoType(RecordingVideoTypeOption.VIDEO_WEBM);

//        uriString = a_file;
//        String l_uristring = uriString;
//
//        if (uriString.toLowerCase().endsWith(".wav") || uriString.toLowerCase().endsWith(".vid")) {
//            l_uristring = uriString.substring(0, uriString.length() - 4);
//        }
//        // TO DO: May need to append the MediaDefaultDirectory
//
//        l_rec.setRecordingUri("file://" + l_uristring);
  //              logger.debug("Added [" + uriString + "]");

        //logger.debug("RAW REST generated...." + l_WMS.toString());
        ByteArrayOutputStream l_newDialog = new ByteArrayOutputStream();

        try {
            l_WMSdoc.save(l_newDialog);
            l_rqStr = l_WMSdoc.toString();

        } catch (IOException ex) {
            logger.error(ex);
        }

        //logger.debug ("Returning Payload:\n " + l_rqStr);
        return l_rqStr;  // Return the requested string...
    } // end buildPlayPayload

    /**
     * Currently supports audio only
     * @param a_file
     * @return
     */
    private String buildMultiRecordPayload(String a_file) {
        FunctionLogger logger = new FunctionLogger("buildRecordPayload", this, m_logger);
        String l_rqStr = "";
        String uriString = "";

        WebServiceDocument l_WMSdoc;
        WebServiceDocument.WebService l_WMS;

        XmlNMTOKEN l_ver;

        ConferenceDocument.Conference l_conf;
        ConfActionDocument.ConfAction l_confAction;
        //CallActionDocument.CallAction l_callAction; // Call Action instance

        // Create a new Web Service Doc Instance
        l_WMSdoc = WebServiceDocument.Factory.newInstance();
        l_WMS = l_WMSdoc.addNewWebService();

        // Create a new XMLToken Instance
        l_ver = XmlNMTOKEN.Factory.newInstance();
        l_ver.setStringValue("1.0");
        l_WMS.xsetVersion(l_ver);

        // add a new call
        l_conf = l_WMS.addNewConference();

        // Add a new Call Action to the call
        l_confAction = l_conf.addNewConfAction();

        String first = null;
        String second = null;
        if (m_partylist.size() == 2) {
            for (XMSCall party : m_partylist) {
                if (first == null) {
                    first = party.getCallIdentifier();
                } else {
                    second = party.getCallIdentifier();
                }
            }
        }

        MultiRecordDocument.MultiRecord l_rec = l_confAction.addNewMultiRecord();

        l_rec.setTerminateDigits(RecordOptions.m_terminateDigits); // Hard code this for now..
        l_rec.setMaxTime("infinite");

        RecordTrackDocument.RecordTrack recordTrack1 = l_rec.addNewRecordTrack();
        if (first != null) {
            recordTrack1.setId(first);
            recordTrack1.setMedia(RecordTrackMedia.AUDIO);
        }

        RecordTrackDocument.RecordTrack recordTrack2 = l_rec.addNewRecordTrack();
        if (second != null) {
            recordTrack2.setId(second);
            recordTrack2.setMedia(RecordTrackMedia.AUDIO);
        }

        l_rec.setRecordingAudioUri("file://" + a_file);
        l_rec.setRecordingAudioType(AudioTypeOption.AUDIO_X_WAV);

        //logger.debug("RAW REST generated...." + l_WMS.toString());
        ByteArrayOutputStream l_newDialog = new ByteArrayOutputStream();

        try {
            l_WMSdoc.save(l_newDialog);
            l_rqStr = l_WMSdoc.toString();

        } catch (IOException ex) {
            logger.error(ex);
        }

        //logger.debug ("Returning Payload:\n " + l_rqStr);
        return l_rqStr;  // Return the requested string...
    }

    /**
     * CLASS TYPE : private METHOD : buildStopPayload
     *
     * DESCRIPTION : Builds Stop Payload
     *
     *
     *
     * RETURN : Payload string
     *
     * Author(s) : Dan Wolanski Created : 6/8/2012 Updated : 6/8/2012
     *
     *
     * HISTORY :
     * ***********************************************************************
     */
    private String buildStopPayload(String a_transactionID) {
        FunctionLogger logger = new FunctionLogger("buildStopPayload", this, m_logger);

        String l_rqStr = "";

        WebServiceDocument l_WMSdoc;
        WebServiceDocument.WebService l_WMS;

        XmlNMTOKEN l_ver;

        ConferenceDocument.Conference l_conf;
        ConfActionDocument.ConfAction l_confAction;
        //CallActionDocument.CallAction l_callAction; // Call Action instance

        StopDocument.Stop l_stop;

        // Create a new Web Service Doc Instance
        l_WMSdoc = WebServiceDocument.Factory.newInstance();
        l_WMS = l_WMSdoc.addNewWebService();

        // Create a new XMLToken Instance
        l_ver = XmlNMTOKEN.Factory.newInstance();
        l_ver.setStringValue("1.0");
        l_WMS.xsetVersion(l_ver);

        // add a new call
        l_conf = l_WMS.addNewConference();

        // Add a new Call Action to the call
        l_confAction = l_conf.addNewConfAction();

        // Add a new Play to the callAction
        l_stop = l_confAction.addNewStop();
        l_stop.setTransactionId(a_transactionID);

        // logger.debug("RAW REST generated...." + l_WMS.toString());
        ByteArrayOutputStream l_newDialog = new ByteArrayOutputStream();

        try {
            l_WMSdoc.save(l_newDialog);
            l_rqStr = l_WMSdoc.toString();

        } catch (IOException ex) {
            logger.error(ex);
        }

        //  logger.debug ("Returning Payload:\n " + l_rqStr);
        return l_rqStr;  // Return the requested string...

    } // end buildStopPayload
}
