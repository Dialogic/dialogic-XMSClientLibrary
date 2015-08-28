/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dialogic.XMSClientLibrary;

import java.util.ArrayList;

/**
 * Logging utility.
 */
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author dslopresti
 * @author dwolanski
 * @author chinck
 */
public abstract class XMSConference extends XMSObject {


    /* Logger information */
    private static Logger m_logger = Logger.getLogger(XMSConference.class.getName());

    public List<XMSCall> m_partylist = new ArrayList<XMSCall>();

    public XMSConferenceOptions ConferenceOptions = new XMSConferenceOptions();
    public XMSPlayOptions PlayOptions = new XMSPlayOptions();
    public XMSAddOptions AddOptions = new XMSAddOptions();
    public XMSRecordOptions RecordOptions = new XMSRecordOptions();
    
    /**
     * CTor for the Object. Default takes no parms
     */
    public XMSConference() {
        m_Name = "XMSConference_" + m_objectcounter;
        PropertyConfigurator.configure("log4j.properties");
        //m_logger.setLevel(Level.ALL);
        m_logger.info("Creating " + m_Name);
        m_partylist.clear();
    }

    /**
     * Add the Call to the conference
     *
     * @param a_call
     * @return
     */
    public XMSReturnCode Add(XMSCall a_call) {
        return XMSReturnCode.NOT_IMPLEMENTED;
    }

    
    /**
     * Remove the Call from the conference
     *
     * @param a_call
     * @return
     */
    public XMSReturnCode Remove(XMSCall a_call) {
        return XMSReturnCode.NOT_IMPLEMENTED;
    }
   /**
      * Returns the list of the parties currently inside the list
      * @return 
      */
     public void RemovePartyfromList(XMSCall call){
         m_logger.info("Removing "+call+" from partylist");
         m_partylist.remove(call);
         if(GetPartyCount()==0 && ConferenceOptions.m_DestroyWhenEmpty){
            m_logger.info("Conference is empty, deleteing it");
            Destroy();
        }
        return;
     }
    /**
     * Returns the number of calls in the conference.
     *
     * @return
     */
    public int GetPartyCount() {
        return m_partylist.size();
    }

    /**
     * Returns the list of the parties currently inside the list
     *
     * @return
     */
    public List<XMSCall> GetPartyList() {
        return m_partylist;
    }

    /**
     * Plays out a file to the parties currently in the conference
     *
     * @param a_playfile
     * @return
     */
    //TODO Get Conference play working.  Will need to add all the enablement for the events as well
    public XMSReturnCode Play(String a_playfile) {
        return XMSReturnCode.NOT_IMPLEMENTED;
    }
     /**
      * Plays out a file to the parties currently in the conference
      * @param a_playfile
      * @param a_Region - The screen region to play the file into
      * @return 
      */
     //TODO Get Conference play working.  Will need to add all the enablement for the events as well
     public XMSReturnCode PlayRegion(String a_playfile,String a_Region){
         return XMSReturnCode.NOT_IMPLEMENTED;
     }
        /**
      * Record the conference
      * @param a_recfile
      * @return 
      */
     //TODO Get Conference play working.  Will need to add all the enablement for the events as well
     public XMSReturnCode Record(String a_recfile){
         return XMSReturnCode.NOT_IMPLEMENTED;
     }
     /** 
      * Force the Destroy the Conference resource from the server
      * By default this is done when the last participant is removed
      * @return 
      */
     public XMSReturnCode Destroy(){
         return XMSReturnCode.NOT_IMPLEMENTED;
     }
     /** 
      * Force the Creation of the Conference resource on the server
      * By default this is done when you add the first participant
      * @return 
      */
     public XMSReturnCode Create(){
         return XMSReturnCode.NOT_IMPLEMENTED;
     }
        /** 
      * Force the Stop of the last active IO function
      * By default this is done when you add the first participant
      * @return 
      */
     public XMSReturnCode Stop(){
         return XMSReturnCode.NOT_IMPLEMENTED;
     }
}
