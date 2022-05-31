package org.milaifontanals;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.milaifontanals.gui.UserLogin;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.persistencia.ICalendarOrganizer;

/**
 *
 * @author Gerard Casas
 */
public class Main {

    public static void main(String[] args) {

        ICalendarOrganizer obj = null;

        try {
            obj = (ICalendarOrganizer) Class.forName(getDBPlugin()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(new JFrame(), "Internal Error", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (CalendarOrganizerException ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        UserLogin ul = new UserLogin();
        ul.run(obj);
    }

    private static String getDBPlugin() throws CalendarOrganizerException {
        Properties p = new Properties();
        try {
            p.load(new FileReader("connection.properties"));
        } catch (IOException ex) {
            throw new CalendarOrganizerException("Problem loading connection.properties config file", ex.getCause());
        }

        String layer = p.getProperty("db_layer");
        if (layer == null) {
            throw new CalendarOrganizerException("Need properites: db_layer");
        }

        return layer;
    }
}
