package org.milaifontanals.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.milaifontanals.persistencia.CalendarOrganizerException;

/**
 *
 * @author Gerard Casas
 */
public class GUI {
    public static Dashboard dashboardFrame = null;
    public static UserLogin userLoginFrame = null;
    
    public GUI(){
        userLoginFrame = new UserLogin();
        try {
            dashboardFrame = new Dashboard();
        } catch (CalendarOrganizerException ex) {
            //TODO SHOW POPUP
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        userLoginFrame.run();
    }
}
