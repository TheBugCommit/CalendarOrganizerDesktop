package org.milaifontanals.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.milaifontanals.Main;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.gui.utils.CloseWindow;

public class UserLogin extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private JPanel contentPane;

    public void run() {
        setVisible(true);
    }

    public void close() {
        emailField.setText("");
        passwordField.setText("");
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

        loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 26));
        loginBtn.setBounds(545, 392, 162, 73);
        loginBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = passwordField.getText();

                try {
                    if (Main.db.checkAuth(email, password)) {
                        GUI.userLoginFrame.close();
                        GUI.dashboardFrame.run();
                    } else {
                        JOptionPane.showMessageDialog(UserLogin.this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (CalendarOrganizerException ex) {
                    JOptionPane.showMessageDialog(UserLogin.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addWindowListener(new CloseWindow());

        contentPane.add(loginBtn);
    }
}
