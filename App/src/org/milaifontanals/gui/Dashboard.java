package org.milaifontanals.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import org.milaifontanals.persistencia.ICalendarOrganizer;

/**
 *
 * @author g3cas
 */
public class Dashboard extends JFrame{

    private ICalendarOrganizer db;
    private JPanel contentPane;
    
    public void run(ICalendarOrganizer db) {
        setVisible(true);
        this.db = db;
    }

    public Dashboard() {
        setTitle("Calendar Organizer Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        JButton btnNewButton = new JButton("Logout");
        btnNewButton.setForeground(new Color(0, 0, 0));
        btnNewButton.setBackground(UIManager.getColor("Button.disabledForeground"));
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 39));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(btnNewButton, "Are you sure?");
                // JOptionPane.setRootFrame(null);
                if (a == JOptionPane.YES_OPTION) {
                    dispose();
                    UserLogin obj = new UserLogin();
                    obj.run(db);
                }
            }
        });
        btnNewButton.setBounds(247, 118, 491, 114);
        contentPane.add(btnNewButton);
    }
}
