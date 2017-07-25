/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.XMSClientLibrary;

/**
 *
 * @author dwolansk
 */
public class XMSJoincallOptions {

    public boolean m_native;
    public boolean m_transcodeaudio;
    public boolean m_transcodevideo;
    public XMSMediaDirection m_videodirection;
    public XMSMediaDirection m_audiodirection;
    
    /**
     * This will Instantiate and Reset all the values to their defaults.
     */
    public XMSJoincallOptions() {
        Reset();
    }

    /**
     * Resets all the contents back to default.
     *
     * Default is to set the mediatype to audio.
     */
    public void Reset() {
        m_native = false;
        m_transcodeaudio = true;
        m_transcodevideo = true;
        m_audiodirection = XMSMediaDirection.AUTOMATIC;
        m_videodirection = XMSMediaDirection.AUTOMATIC;
    }

    /**
     * Enable native mode
     *
     * @param a_native - boolean option (true=Yes, false=no)
     */
    public void EnableNativeMode(boolean a_native) {
        m_native = a_native;
    }
    
    /**
     * Enable video Transcoding mode
     *
     * @param a_enable - boolean option (true=Yes, false=no)
     */
    public void EnableVideoTranscode(boolean a_enable) {
        m_transcodevideo = a_enable;
    }
     /**
     *  Enable audio Transcoding mode
     *
     * @param a_enable - boolean option (true=Yes, false=no)
     */
    public void EnableAudioTranscode(boolean a_enable) {
        m_transcodeaudio = a_enable;
    }
    /**
     * Direction of audio media relative TO other call
     *
     * @param a_audiodirection - boolean option (true=Yes, false=no)
     */
    public void SetAudioDirection(XMSMediaDirection a_audiodirection){
        m_audiodirection=a_audiodirection;
    }
   /**
     * Direction of video media relative TO other call
     *
     * @param a_videodirection - boolean option (true=Yes, false=no)
     */
    public void SetVideoDirection(XMSMediaDirection a_videodirection){
        m_audiodirection=a_videodirection;
    }
    
    @Override
    public String toString() {
        return "JoincallOptions: m_native=" + m_native
                +"m_transcodeaudio="+m_transcodeaudio
                +"m_transcodevideo"+m_transcodevideo
                +"m_audiodirection"+m_audiodirection.toString()
                +"m_videodirection"+m_videodirection.toString();
    }
}
