package org.milaifontanals.gui;

import javax.swing.JOptionPane;
import org.milaifontanals.persistencia.CalendarOrganizerException;

/**
 *
 * @author Gerard Casas
 */
public class GUI {

    public static Dashboard dashboardFrame = null;
    public static UserLogin userLoginFrame = null;

    public GUI() {
        userLoginFrame = new UserLogin();
        try {
            dashboardFrame = new Dashboard();
        } catch (CalendarOrganizerException ex) {
            JOptionPane.showMessageDialog(dashboardFrame,
                    ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        userLoginFrame.run();
    }
}
