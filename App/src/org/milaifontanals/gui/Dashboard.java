package org.milaifontanals.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.milaifontanals.Main;
import org.milaifontanals.models.User;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.utils.CloseWindow;

/**
 *
 * @author Gerard Casas
 */
public class Dashboard extends JFrame {

    private JPanel panel;

    public void run() {
        setVisible(true);
    }
    
    public void close() {
        setVisible(false);
    }

    public Dashboard() throws CalendarOrganizerException {
                
        setTitle("Calendar Organizer Admin Dashboard");
        setJMenuBar(new Menu(this).getMenuBar());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 720);
        Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimemsion.width / 2 - getSize().width / 2, dimemsion.height / 2 - getSize().height / 2);
        setResizable(true);
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);
        panel.setLayout(null);


        addWindowListener(new CloseWindow());
    }
    
    private void setSearchByEmail() {
        try {
            User u  = Main.db.searchUserByEmail("albert@gmail.com");
            /*u = db.getUserCalendars(u);
            System.out.println(u.toString());
            for(Calendar c : u.getHelCalendars()){
                System.out.println(c.toString() + "Helper");
            }
            for(Calendar c : u.getOwnerCalendars()){
                System.out.println(c.toString() + "Owner");
            }*/
            
            ArrayList<User> u2 = Main.db.searchUserByNameSurname("cas");
            for(User ut : u2){
                System.out.println(ut);
            }
        } catch (CalendarOrganizerException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
