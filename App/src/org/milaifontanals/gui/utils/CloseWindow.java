package org.milaifontanals.gui.utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.milaifontanals.Main;
import org.milaifontanals.persistencia.CalendarOrganizerException;

/**
 *
 * @author Gerard Casas
 */
public class CloseWindow extends WindowAdapter {
    
    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e); 
        try {
            Main.db.close();
        } catch (CalendarOrganizerException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
