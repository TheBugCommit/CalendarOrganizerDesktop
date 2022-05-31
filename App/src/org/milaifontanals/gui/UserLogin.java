/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.persistencia.ICalendarOrganizer;

public class UserLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JLabel label;
    private JPanel contentPane;
    private ICalendarOrganizer db;

    public void run(ICalendarOrganizer db) {
        setVisible(true);
        this.db = db;
    }
    
    public void close(){
        dispose();
        setVisible(false);
    }

    public UserLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setForeground(Color.BLACK.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(423, 13, 273, 93);
        contentPane.add(lblNewLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        emailField.setBounds(481, 170, 281, 68);
        contentPane.add(emailField);
        emailField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(481, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Email");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblUsername.setBounds(250, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblPassword.setBounds(250, 286, 193, 52);
        contentPane.add(lblPassword);

        btnNewButton = new JButton("Login");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(545, 392, 162, 73);
        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = passwordField.getText();

                try {
                    if(db.checkAuth(email, password)){
                        close();
                        Dashboard d = new Dashboard();
                        d.run(db);
                    }else{
                        JOptionPane.showMessageDialog(UserLogin.this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (CalendarOrganizerException ex) {
                    System.out.println(ex.getMessage() + ex.getCause());
                }
            }
        });

        contentPane.add(btnNewButton);

        label = new JLabel("");
        label.setBounds(0, 0, 1008, 562);
        contentPane.add(label);
    }
}
