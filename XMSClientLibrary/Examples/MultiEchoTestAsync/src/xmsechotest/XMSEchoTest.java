/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmsechotest;

import com.dialogic.XMSClientLibrary.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author dwolansk
 */
public class XMSEchoTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        XMSObjectFactory myFactory = new XMSObjectFactory();
        XMSConnector myConnector = myFactory.CreateConnector();
        List<PlayRecTestCase> myTestList=new ArrayList<PlayRecTestCase>();
        int CallCount=4;
        
        for(int x=1;x<=CallCount;x++){
            XMSCall tmpcall = myFactory.CreateCall(myConnector);
            //Enable all events to go back to my event handler
            PlayRecTestCase myTest = new PlayRecTestCase();
            myTest.Start(tmpcall, x);
            myTestList.add(myTest);
        }
        //At this point event handler thread will process all events so this thread just waits to complete

    }

 
}
