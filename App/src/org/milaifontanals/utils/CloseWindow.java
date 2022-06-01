package org.milaifontanals.utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.persistencia.ICalendarOrganizer;

/**
 *
 * @author Gerard Casas
 */
public class CloseWindow extends WindowAdapter {

    private ICalendarOrganizer db;

    public CloseWindow(ICalendarOrganizer db){
        this.db = db;
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e); 
        try {
            db.close();
        } catch (CalendarOrganizerException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
