package org.milaifontanals.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.persistencia.ICalendarOrganizer;
import org.milaifontanals.utils.CloseWindow;

/**
 *
 * @author Gerard Casas
 */
public class Dashboard extends JFrame {

    private ICalendarOrganizer db;
    private JPanel panel;

    public void run() {
        setVisible(true);
    }
    
    public void close() {
        dispose();
        setVisible(false);
    }

    public Dashboard(ICalendarOrganizer db) throws CalendarOrganizerException {
        
        setDb(db);
        
        setTitle("Calendar Organizer Admin Dashboard");
        setJMenuBar(new Menu(this, this.db).getMenuBar());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 720);
        Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimemsion.width / 2 - getSize().width / 2, dimemsion.height / 2 - getSize().height / 2);
        setResizable(true);
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);
        panel.setLayout(null);

        setSearchByEmail();

        addWindowListener(new CloseWindow(db));
    }

    private void setDb(ICalendarOrganizer db) {
        this.db = db;
    }
    
    private void setSearchByEmail() {

    }

}
