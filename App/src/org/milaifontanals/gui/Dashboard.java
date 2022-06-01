package org.milaifontanals.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import org.milaifontanals.Main;
import org.milaifontanals.models.User;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.gui.utils.CloseWindow;

/**
 *
 * @author Gerard Casas
 */
public class Dashboard extends JFrame {

    public JPanel panel;
    public JPanel filterPanel;
    private JLabel emailLabel;
    public JTextField emailSearch;
    private User selectedUser;

    private SearchEmailDialog emailDialog;
    private SearchNameSurDialog nameSurDialog;

    private JLabel filterLabel;
    private JComboBox filterDropdown;

    private final static String[] options = new String[]{"Search by Email", "Search by Name-Surname"};

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
        panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);

        addWindowListener(new CloseWindow());

        emailDialog = new SearchEmailDialog();
        nameSurDialog = new SearchNameSurDialog();

        filterPanel = new JPanel();

        filterLabel = new JLabel("Filters");
        filterDropdown = initFilterOptions();
        filterPanel.add(filterLabel);
        filterPanel.add(filterDropdown);

        panel.add(filterPanel, BorderLayout.EAST);
    }

    private JComboBox initFilterOptions() {
        filterDropdown = new JComboBox();

        for (String option : options) {
            filterDropdown.addItem(option);
        }
        filterDropdown.setSelectedIndex(-1);
        filterDropdown.addActionListener(new ManageCombo());

        return filterDropdown;
    }

    private void setSearchByEmail() {
        try {
            User u = Main.db.searchUserByEmail("albert@gmail.com");
            /*u = db.getUserCalendars(u);
            System.out.println(u.toString());
            for(Calendar c : u.getHelCalendars()){
                System.out.println(c.toString() + "Helper");
            }
            for(Calendar c : u.getOwnerCalendars()){
                System.out.println(c.toString() + "Owner");
            }*/

            ArrayList<User> u2 = Main.db.searchUserByNameSurname("cas");
            for (User ut : u2) {
                System.out.println(ut);
            }
        } catch (CalendarOrganizerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private class ManageCombo implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (filterDropdown.getSelectedItem() == "Search by Email") {
                emailDialog.run();
            } else if (filterDropdown.getSelectedItem() == "Search by Name-Surname") {
                nameSurDialog.run();
            }
        }
    }

    private class SearchEmailDialog extends JDialog {

        private JLabel emailLabel;
        private JTextField emailField;
        private JButton submitButton;
        private JPanel panel;

        public SearchEmailDialog() {
            super(Dashboard.this, true);
            emailLabel = new JLabel("Email: ");
            emailField = new JTextField();
            submitButton = new JButton("Search");
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String email = emailField.getText();
                    if (!checkEmail(email)) {
                        JOptionPane.showMessageDialog(SearchEmailDialog.this,
                                "Invalid Email Format", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    try {
                        selectedUser = Main.db.searchUserByEmail(email);
                        if (selectedUser == null) {
                            JOptionPane.showMessageDialog(SearchEmailDialog.this,
                                    "Sorry no user with this email", "Nothing Found",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        close();
                        //TODO cridar funcio per omplir taules
                    } catch (CalendarOrganizerException ex) {
                        JOptionPane.showMessageDialog(SearchEmailDialog.this,
                                "Something went wrong searching user",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            panel = new JPanel();

            emailField.setPreferredSize(new Dimension(200, 24));

            Box bEmail = Box.createHorizontalBox();
            bEmail.add(emailLabel);
            bEmail.add(Box.createHorizontalStrut(10));
            bEmail.add(emailField);

            Box bButtons = Box.createHorizontalBox();
            bButtons.add(submitButton);

            Box b = Box.createVerticalBox();
            b.add(Box.createVerticalStrut(10));
            b.add(bEmail);
            b.add(Box.createVerticalStrut(20));
            b.add(bButtons);
            b.add(Box.createVerticalStrut(10));

            panel.add(b);
            panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

            add(panel);
            setTitle("Search by Email");
            pack();
            setResizable(false);
            setLocationRelativeTo(Dashboard.this);
            setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    close();
                }
            });
        }

        public void close() {
            emailField.setText("");
            setVisible(false);
        }

        public void run() {
            setVisible(true);
        }

        private boolean checkEmail(String email) {
            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }

    private class SearchNameSurDialog extends JDialog {

        private JLabel textLabel;
        private JTextField textField;
        private JButton submitButton;
        private JComboBox dropdown;
        private ArrayList<User> foundUsers;
        private JPanel panel;

        public SearchNameSurDialog() {
            super(Dashboard.this, true);
            textLabel = new JLabel("Name-Surname: ");
            textField = new JTextField();
            submitButton = new JButton("Search");
            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String text = textField.getText();
                    if (text.length() == 0) {
                        return;
                    }

                    try {
                        foundUsers = Main.db.searchUserByNameSurname(text);
                        if (foundUsers == null) {
                            JOptionPane.showMessageDialog(SearchNameSurDialog.this,
                                    "Sorry no user with this name-surname", "Nothing Found",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        removeAllItemsDropdown();
                        addUsersDropdown(foundUsers);
                        //TODO cridar funcio per omplir taules
                    } catch (CalendarOrganizerException ex) {
                        JOptionPane.showMessageDialog(SearchNameSurDialog.this,
                                "Something went wrong searching user",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            dropdown = new JComboBox();
            panel = new JPanel();

            textField.setPreferredSize(new Dimension(200, 24));

            Box bEmail = Box.createHorizontalBox();
            bEmail.add(textLabel);
            bEmail.add(Box.createHorizontalStrut(10));
            bEmail.add(textField);

            Box bButtons = Box.createHorizontalBox();
            bButtons.add(submitButton);

            Box b = Box.createVerticalBox();
            b.add(Box.createVerticalStrut(10));
            b.add(bEmail);
            b.add(Box.createVerticalStrut(20));
            b.add(bButtons);
            b.add(Box.createVerticalStrut(10));

            panel.add(b);
            panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

            add(panel);
            setTitle("Search by Name-Surname");
            pack();
            setResizable(false);
            setLocationRelativeTo(Dashboard.this);
            setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    close();
                }
            });
        }

        public void close() {
            textField.setText("");
            setVisible(false);
        }

        public void run() {
            setVisible(true);
        }

        private void addUsersDropdown(ArrayList<User> users) {
            if (users == null) {
                return;
            }
            for (User user : users) {
                dropdown.addItem(user);
            }
            dropdown.setSelectedIndex(-1);
            dropdown.addActionListener(new ManageComboUsers());
            panel.add(dropdown);
            repaint();
        }

        private void removeAllItemsDropdown() {
            dropdown.removeAllItems();
        }
        
        private class ManageComboUsers implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                User u = (User) dropdown.getSelectedItem();
                if(u != null)
                System.out.println();
            }
        }
    }
}
