/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.XMSClientLibrary;

/**
 *
 * @author rdmoses
 */
public class XMSRecordOptions {

    public XMSMediaType m_mediaType;

    public String m_terminateDigits;
    public String m_offset;
    public String m_delay;
    public String m_repeat;
    public boolean m_isBargeEnable;
    public boolean m_clearDB;
    public boolean m_isBeepEnabled;
    public String m_timeoutValue;
    public String m_maxTime;
    public XMSAudioCodecOption m_audioMimeCodec;
    public XMSAudioRateOption m_audioMimeRate;
    public String m_audioMimeMode;
    public String m_videoMimeHeight;
    public String m_videoMimeWidth;
    public XMSVideoCodecOption m_videoMimeCodec;
    public String m_videoMimeFramerate;
    public String m_videoMimeMaxbitrate;
    public String m_videoMimeLevel;
    public String m_videoMimeProfile;
    public XMSAudioTypeOption m_audioTypeOption;
    public XMSRecordingVideoTypeOption m_videoTypeOption;

    /**
     * This will Instantiate and Reset all the values to their defaults
     */
    public XMSRecordOptions() {
        Reset();
    }

    /**
     * Resets all the contents back to default Note: the mime params defaults
     * are for mp4 container. The values of these params vary based on the
     * container selected. Please refer RestAPI guide for the correct values.
     */
    public void Reset() {

        m_terminateDigits = "#";
        m_offset = "0s";
        m_delay = "0s";
        m_repeat = "0";
        m_mediaType = XMSMediaType.AUDIO;
        m_isBargeEnable = false;
        m_clearDB = true;
        m_isBeepEnabled = true;
        m_timeoutValue = "";
        m_maxTime = "30s";
        m_audioMimeCodec = XMSAudioCodecOption.AUTOSELECT;
        m_audioMimeRate = XMSAudioRateOption.X_16000;
        m_audioMimeMode = "7";
        m_videoMimeHeight = "480";
        m_videoMimeWidth = "640";
        m_videoMimeCodec = XMSVideoCodecOption.AUTOSELECT;
        m_videoMimeFramerate = "15";
        m_videoMimeMaxbitrate = "768000";
        m_videoMimeLevel = "3.1";
        m_videoMimeProfile = "66";
        m_audioTypeOption = XMSAudioTypeOption.AUDIO_AUTOSELECT;
        m_videoTypeOption = XMSRecordingVideoTypeOption.VIDEO_AUTOSELECT;

    }

    /**
     * Set Max Time.
     *
     * The maximum length of time to record.
     *
     *
     * @param a_maxTime - seconds
     */
    public void SetMaxTime(int a_maxTime) {
        m_maxTime = a_maxTime + "s";
    }

    /**
     * Set Terminating Digits. The digit or digits [0-9,*,#] used to terminate
     * the play_record.
     *
     * @param a_digitValue - Terminating digits
     */
    public void SetTerminateDigits(String a_digitValue) {
        m_terminateDigits = a_digitValue;
    }
// Note I commented these out so that users don't set them as they are 
//  not Applicable on a Record because they are play options
//    /**
//     * Set Offset
//     * @param a_offsetValue - Offset time in seconds.
//     */
//    public void SetOffset(String a_offsetValue){
//        m_offset=a_offsetValue+"s"; // Add 's' at the end of the string for seconds
//    }
//
//    /**
//     * Set Delay
//     * @param a_delayValue - Delay time in seconds
//     */
//    public void SetDelay(String a_delayValue){
//        m_delay=a_delayValue+"s"; // Add 's' at the end of the string for seconds
//    }
//
//    /**
//     * Set Repeat
//     * @param a_repeatValue - How many times do you want to repeat the play
//     */
//    public void SetRepeat(String a_repeatValue){
//        m_repeat=a_repeatValue;
//    }
//

    /**
     * Set Media Type should be enabled on the outbound call
     *
     * @param a_mediaType - AUDIO or VIDEO
     */
    public void SetMediaType(XMSMediaType a_mediaType) {
        m_mediaType = a_mediaType;
    }
//    /**
// * 
// * @param a_isBargeEnable 
// */
//    //TODO do we want to support this as is there something on other technologies.
//   public void EnableBarge(boolean a_isBargeEnable){
//       m_isBargeEnable=a_isBargeEnable;
//   }

    /**
     * Set if there was a specific DTMF that will terminate (ex #) Specifies
     * whether previous input should be considered or ignored for the purpose of
     * barge-in.
     *
     * Valid values are Yes or No.
     *
     * When set to Yes, any previously buffered digits are discarded.
     *
     * When set to No, previously buffered digits will be considered. When set
     * to No, with the barge parameter set to Yes, previously buffered digits
     * will result in the recording phase starting immediately, and the prompt
     * will not be played.
     *
     *
     * @param a_clearDBon
     */
    public void EnableClearDigitBufferOnExecute(boolean a_clearDBon) {
        m_clearDB = a_clearDBon;
    }

    /**
     * Set if there should be a beep at the start of the recording.
     *
     * Specifies whether to play a tone when starting to record.
     *
     * Valid values are Yes or No.
     *
     * @param a_beepon
     */
    public void EnableBeepOnExecute(boolean a_beepon) {
        m_isBeepEnabled = a_beepon;
    }

    /**
     * Set Timeout
     *
     * @param a_timeoutValue - Timeout in seconds.
     *
     * TODO - not clear if this applies to records.
     */
    public void SetTimeout(int a_timeoutValue) {
        m_timeoutValue = a_timeoutValue + "s"; // Append 's' at the end of the string for seconds
    }

    public void setAudioMimeCodec(XMSAudioCodecOption a_audioMimeCodec) {
        m_audioMimeCodec = a_audioMimeCodec;
    }

    public void setAudioMimeRate(XMSAudioRateOption a_audioMimeRate) {
        m_audioMimeRate = a_audioMimeRate;
    }

    public void setAudioMimeMode(String a_audioMimeMode) {
        m_audioMimeMode = a_audioMimeMode;
    }

    public void setVideoMimeHeight(String a_videoMimeHeight) {
        m_videoMimeHeight = a_videoMimeHeight;
    }

    public void setVideoMimeWidth(String a_videoMimeWidth) {
        m_videoMimeWidth = a_videoMimeWidth;
    }

    public void setVideoMimeCodec(XMSVideoCodecOption a_videoMimeCodec) {
        m_videoMimeCodec = a_videoMimeCodec;
    }

    public void setVideoMimeFramerate(String a_videoMimeFramerate) {
        m_videoMimeFramerate = a_videoMimeFramerate;
    }

    public void setVideoMimeMaxbitrate(String a_videoMimeMaxbitrate) {
        m_videoMimeMaxbitrate = a_videoMimeMaxbitrate;
    }

    public void setVideoMimeLevel(String a_videoMimeLevel) {
        m_videoMimeLevel = a_videoMimeLevel;
    }

    public void setVideoMimeProfile(String a_videoMimeProfile) {
        m_videoMimeProfile = a_videoMimeProfile;
    }

    public void setAudioTypeOption(XMSAudioTypeOption a_audioTypeOption) {
        m_audioTypeOption = a_audioTypeOption;
    }

    public void setVideoTypeOption(XMSRecordingVideoTypeOption a_videoTypeOption) {
        m_videoTypeOption = a_videoTypeOption;
    }

    @Override
    public String toString() {
        String RtnStr;

        /**
         * NEED TO DISCUSS THIS SOME MORE
         */
        RtnStr = "RecordOptions: terminateDigits=" + m_terminateDigits
                + " ClearDigitBufferOnExecute=" + m_clearDB
                + " isBeepEnabled=" + m_isBeepEnabled
                + " TimeoutValue=" + m_timeoutValue
                + //                " isBargeEnable"+m_isBargeEnable+
                //                " offset="+m_offset+
                //                " delay"+m_delay+
                //                " repeat"+m_repeat+
                " mediaType=" + m_mediaType
                + "";
        return RtnStr;
    }
}
