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
    }

    /**
     * Enable native mode
     *
     * @param a_native - boolean option (true=Yes, false=no)
     */
    public void EnableNativeMode(boolean a_native) {
        m_native = a_native;
    }

    @Override
    public String toString() {
        return "JoincallOptions: m_native=" + m_native;
    }
}
