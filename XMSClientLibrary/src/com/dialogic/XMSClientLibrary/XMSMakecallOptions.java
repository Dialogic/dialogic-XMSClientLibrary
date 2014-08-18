/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.XMSClientLibrary;


/**
 *
 * @author dwolansk
 */
public class XMSMakecallOptions {
    boolean m_cpaEnabled;
    XMSMediaType m_mediaType; 
    boolean m_iceEnabled;
    String m_sdp;
    boolean m_signalingEnabled;
    boolean m_encryptionEnabled;
    String m_sourceAddress;
    String m_calledAddress;
    String m_displayName;
    /**
     * This will Instantiate and Reset all the values to their defaults
     */
    public XMSMakecallOptions(){
        Reset();
    }
    /**
     * Resets all the contents back to default
     */
    public void Reset(){
        m_cpaEnabled = false;
        m_mediaType = XMSMediaType.AUDIO;
        m_iceEnabled= false;
        m_signalingEnabled = true;
        m_sdp="";
        m_encryptionEnabled = false;
        m_sourceAddress="";
        m_calledAddress="";
        m_displayName="";
    }
    /**
     * Set if CPA should be enabled on the outbound call
     *  
     * @param a_isEnabled - true or false if it should be enabled
     */
    public void EnableCPA(boolean a_isEnabled){
        m_cpaEnabled=a_isEnabled; 
    }
    
    /**
     * Set if ICE should be enabled on the outbound call
     *  
     * @param a_isEnabled - true or false if it should be enabled
     */
    public void EnableIce(boolean a_isEnabled){
        m_iceEnabled=a_isEnabled; 
    }
    
    /**
     * Set if DTLS should be enabled on the outbound call
     *  
     * @param a_isEnabled - true or false if it should be enabled
     */
    public void EnableEncryption(boolean a_isEnabled){
        m_encryptionEnabled=a_isEnabled; 
    }
    
    /**
     * Set if CPA should be enabled on the outbound call
     *  
     * @param a_isEnabled - true or false if it should be enabled
     */
    public void EnableSignaling(boolean a_isEnabled){
        m_signalingEnabled=a_isEnabled; 
    }
    /**
     * 
     * @param a_sdp  - The SDP to use on the call
     */
    public void setSdp(String a_sdp){
     
        m_sdp = a_sdp;
    }
    /**
     * 
     * @param a_sourceAddress  - Caller address. For SIP, this is the From header
     */
    public void setSourceAddress(String a_sourceAddress){
     
        m_sourceAddress = a_sourceAddress;
    }
     /**
     * 
     * @param a_calledAddress  - Logical destination address. For SIP, this is the To header.  For RTC this will be the local address
     */
    public void setCalledAddress(String a_calledAddress){
     
        m_calledAddress = a_calledAddress;
    }
      /**
     * 
     * @param a_displayName  - Caller's display name
     */
    public void setDisplayName(String a_displayName){
     
        m_displayName = a_displayName;
    }
    /**
     * Set Media Type should be enabled on the outbound call
     * @param a_mediaType - AUDIO or VIDEO
     */
    public void SetMediaType(XMSMediaType a_mediaType){
        m_mediaType=a_mediaType;
    }



    
    @Override
    public String toString()
    {
        String RtnStr;

        /**
         * NEED TO DISCUSS THIS SOME MORE
         */
        RtnStr = "MakecallOptions: cpaEnabled="+m_cpaEnabled+
                " mediaType="+m_mediaType+
                " iceEnabled="+m_iceEnabled+
                " encryptionEnabled="+m_encryptionEnabled+
                " signalingEnabled="+m_signalingEnabled;
        
                if(m_sdp != null)
                    RtnStr+=" sdp="+m_sdp;
                
                if(m_sourceAddress != null)
                    RtnStr+=" sourceAddress="+m_sourceAddress;
                if(m_calledAddress != null)
                    RtnStr+=" calledAddress="+m_calledAddress;
                if(m_displayName != null)
                    RtnStr+=" displayName="+m_displayName;
                
    
        return RtnStr;
    }

}
