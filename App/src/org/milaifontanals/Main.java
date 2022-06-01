package org.milaifontanals;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.milaifontanals.gui.GUI;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.persistencia.ICalendarOrganizer;
import org.milaifontanals.utils.ReadProperties;

/**
 *
 * @author Gerard Casas
 */
public class Main {
    public static ICalendarOrganizer db = null;
    
    public static void main(String[] args) {
        
        try {
            HashMap<String, String> props = checkProperties();
            db = (ICalendarOrganizer) Class.forName(props.get("db_layer")).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Internal Error", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (CalendarOrganizerException ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        GUI gui = new GUI();
    }
    
    private static HashMap<String, String> checkProperties() throws CalendarOrganizerException {
        ArrayList<String> neededProps = new ArrayList<>() {
            {
                add("db_layer");
                add("db_url");
                add("db_username");
                add("db_password");
                add("db_layer");
                add("web_url");
                add("web_help_url");
            }
        };
        
        HashMap<String, String> props = null;
        try{
            props = new ReadProperties("env.properties", neededProps).getPropertiesReaded();
        }catch(Exception ex){
            throw new CalendarOrganizerException(ex.getMessage(), ex.getCause());
        }

        for (String prop : neededProps) {
            if (props.get(prop) == null) {
                throw new CalendarOrganizerException("Need property: " + prop);
            }
        }

        return props;
    }
}
