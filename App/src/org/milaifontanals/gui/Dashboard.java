package org.milaifontanals.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.milaifontanals.Main;
import org.milaifontanals.models.User;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.gui.utils.CloseWindow;
import org.milaifontanals.models.Calendar;

/**
 *
 * @author Gerard Casas
 */
public class Dashboard extends JFrame {

    public JPanel panel;
    public JPanel eastPanel;
    private JLabel emailLabel;
    public JTextField emailSearch;
    private User selectedUser;

    private SearchEmailDialog emailDialog;
    private SearchNameSurDialog nameSurDialog;

    private JLabel filterLabel;
    private JComboBox filterDropdown;

    private JPanel pCenterDashboard;
    private JPanel pTables;
    private JTable ownerCalendars;
    private JTable helperCalendars;
    private DefaultTableModel ownerDefTabMod;
    private DefaultTableModel helperDefTabMod;
    private JScrollPane ownerScroll;
    private JScrollPane helperScroll;
    private JLabel ownerLabel;
    private JLabel helperLabel;

    private JPanel userInfo;
    private JLabel lName;
    private JLabel lEmail;
    private JLabel lGender;
    private JLabel lLocked;
    private JLabel lRole;
    private JLabel lNation;
    private JLabel lBirthDate;
    private JLabel lSurname1;
    private JLabel lSurname2;
    private JLabel lPhone;

    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfGender;
    private JTextField tfLocked;
    private JTextField tfRole;
    private JTextField tfNation;
    private JTextField tfBirthDate;
    private JTextField tfSurname1;
    private JTextField tfSurname2;
    private JTextField tfPhone;

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
        setSize(1300, 720);
        Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimemsion.width / 2 - getSize().width / 2, dimemsion.height / 2 - getSize().height / 2);
        setResizable(false);
        panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);

        addWindowListener(new CloseWindow());

        emailDialog = new SearchEmailDialog();
        nameSurDialog = new SearchNameSurDialog();

        eastPanel = new JPanel();
        pTables = new JPanel(new GridLayout(0, 2));
        pCenterDashboard = new JPanel();
        pCenterDashboard.setLayout(new BoxLayout(pCenterDashboard, BoxLayout.PAGE_AXIS));

        //filters & user info
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        filterLabel = new JLabel("Filters: ");
        filterDropdown = initFilterOptions();
        filterPanel.add(filterLabel);
        filterPanel.add(filterDropdown);
        userInfo = initUserInfo();

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);

        eastPanel.add(filterPanel);
        eastPanel.add(separator);
        eastPanel.add(userInfo);

        //Tables
        ownerDefTabMod = new DefaultTableModel();
        helperDefTabMod = new DefaultTableModel();

        ownerCalendars = makeCalendarTable(ownerDefTabMod);
        helperCalendars = makeCalendarTable(helperDefTabMod);

        ownerScroll = new JScrollPane(ownerCalendars,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ownerScroll.setPreferredSize(new Dimension(503, 200));

        helperScroll = new JScrollPane(helperCalendars,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        helperScroll.setPreferredSize(new Dimension(503, 200));

        JPanel pOwner = new JPanel(new BorderLayout());
        JPanel pHelper = new JPanel(new BorderLayout());

        pOwner.add(ownerScroll);
        pHelper.add(helperScroll);

        pOwner.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Owner Calendars", TitledBorder.LEFT,
                TitledBorder.TOP));
        pHelper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Helper Calendars", TitledBorder.LEFT,
                TitledBorder.TOP));

        pTables.add(pOwner);
        pTables.add(pHelper);

        pCenterDashboard.add(pTables);

        panel.add(eastPanel, BorderLayout.WEST);
        panel.add(pCenterDashboard, BorderLayout.CENTER);
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
                        removeCalendars();
                        fetchCalendars();
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
        private JLabel resultLabel;
        private JTextField textField;
        private JButton submitButton;
        private JComboBox dropdown;
        private ArrayList<User> foundUsers;
        private JPanel panel;
        private ManageComboUsers mcu = new ManageComboUsers();

        public SearchNameSurDialog() {
            super(Dashboard.this, true);
            textLabel = new JLabel("Name-Surname: ");
            resultLabel = new JLabel("Matches: ");
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
                        if (foundUsers.size() == 0) {
                            JOptionPane.showMessageDialog(SearchNameSurDialog.this,
                                    "Sorry no user with this name-surname", "Nothing Found",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        JOptionPane.showMessageDialog(SearchNameSurDialog.this,
                                "Matches Found", "Matches",
                                JOptionPane.INFORMATION_MESSAGE);

                        removeAllItemsDropdown();
                        addUsersDropdown(foundUsers);
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

            Box bResult = Box.createHorizontalBox();
            bResult.add(resultLabel);
            bResult.add(Box.createHorizontalStrut(10));
            bResult.add(dropdown);

            Box bButtons = Box.createHorizontalBox();
            bButtons.add(submitButton);

            Box b = Box.createVerticalBox();
            b.add(Box.createVerticalStrut(10));
            b.add(bEmail);
            b.add(Box.createVerticalStrut(10));
            b.add(bResult);
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
            removeAllItemsDropdown();
            textField.setText("");
            setVisible(false);
        }

        public void run() {
            setVisible(true);
        }

        private void addUsersDropdown(ArrayList<User> users) {
            int i = 0;
            dropdown.removeActionListener(mcu);
            for (User user : users) {
                dropdown.addItem(user);
                i++;
            }
            dropdown.setSelectedIndex(-1);
            dropdown.addActionListener(mcu);
        }

        private void removeAllItemsDropdown() {
            dropdown.removeActionListener(mcu);
            dropdown.removeAllItems();
        }

        private class ManageComboUsers implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e.getActionCommand());
                User u = (User) dropdown.getSelectedItem();
                selectedUser = u;
                close();
                removeCalendars();
                fetchCalendars();
            }
        }
    }

    private JTable makeCalendarTable(DefaultTableModel defautltTableModel) {
        JTable table = new JTable(defautltTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                Class clazz = String.class;
                switch (column) {
                    case 2:
                    case 3:
                        clazz = Date.class;
                        break;
                }
                return clazz;
            }

        };

        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    System.out.println(e.getClass().getName());
                    System.out.println("selected");
                }
            }
        });

        defautltTableModel.addColumn("TITLE");
        defautltTableModel.addColumn("DESCRIPTION");
        defautltTableModel.addColumn("START");
        defautltTableModel.addColumn("END");

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);

        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        return table;
    }

    private void fillCalendarTable(DefaultTableModel model, ArrayList<Calendar> calendars) {
        for (Calendar cal : calendars) {
            model.addRow(new Object[]{cal.getTitle(), cal.getDescription(), cal.getStartDate(), cal.getEndDate()});
        }
    }

    private void removeCalendars() {
        ownerDefTabMod.setRowCount(0);
        helperDefTabMod.setRowCount(0);
    }

    private void fetchCalendars() {
        try {
            selectedUser = Main.db.getUserCalendars(selectedUser);
            fillCalendarTable(ownerDefTabMod, selectedUser.getOwnerCalendars());
            fillCalendarTable(helperDefTabMod, selectedUser.getHelCalendars());
            System.out.println(selectedUser.toString());
        } catch (CalendarOrganizerException ex) {
            JOptionPane.showMessageDialog(this,
                    "Something went wrong getting user calendars",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel initUserInfo() {
        JPanel panel = new JPanel();
        JButton editButton = new JButton("Edit User");

        lName = new JLabel("Name: ");
        lEmail = new JLabel("Email: ");
        lGender = new JLabel("Gender: ");
        lLocked = new JLabel("Is Loked: ");
        lRole = new JLabel("Role: ");
        lNation = new JLabel("Nation: ");
        lBirthDate = new JLabel("Birth Date: ");
        lSurname1 = new JLabel("First Surname: ");
        lSurname2 = new JLabel("Second Surname: ");
        lPhone = new JLabel("Phone: ");
        tfName = new JTextField();
        tfEmail = new JTextField();
        tfGender = new JTextField();
        tfLocked = new JTextField();
        tfRole = new JTextField();
        tfNation = new JTextField();
        tfBirthDate = new JTextField();
        tfSurname1 = new JTextField();
        tfSurname2 = new JTextField();
        tfPhone = new JTextField();


        Box bEmail = Box.createHorizontalBox();
        bEmail.add(lEmail);
        bEmail.add(Box.createHorizontalStrut(10));
        bEmail.add(tfEmail);
        
        Box bName = Box.createHorizontalBox();
        bName.add(lName);
        bName.add(Box.createHorizontalStrut(10));
        bName.add(tfName);
        
        Box bGender = Box.createHorizontalBox();
        bName.add(lName);
        bName.add(Box.createHorizontalStrut(10));
        bName.add(tfName);

        Box bButtons = Box.createHorizontalBox();
       // bButtons.add(submitButton);

        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalStrut(10));
        b.add(bEmail);
        b.add(Box.createVerticalStrut(20));
        b.add(bButtons);
        b.add(Box.createVerticalStrut(10));

      //  panel.add(this)
        
        panel.setVisible(false);
        return panel;
    }

    private void showUserInfo() {
        userInfo.setVisible(true);
    }

    private void changeUserInfo() {
        tfName.setText(selectedUser.getName());
        tfEmail.setText(selectedUser.getEmail());
        tfGender.setText(selectedUser.getGender().getName());
        tfLocked.setText(selectedUser.isLocked() ? "Yes" : "No");
        tfRole.setText(selectedUser.getRole().getName());
        tfNation.setText(selectedUser.getNation().getName());
        tfBirthDate.setText(selectedUser.getBirthDate().toString());
        tfSurname1.setText(selectedUser.getSurname1());
        tfSurname2.setText(selectedUser.getSurname2());
        tfPhone.setText(selectedUser.getPhone() == null ? "None" : selectedUser.getPhone());
    }
}
