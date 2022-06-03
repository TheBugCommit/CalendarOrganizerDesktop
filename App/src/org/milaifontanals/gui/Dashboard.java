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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
import org.milaifontanals.models.Gender;
import org.milaifontanals.models.Nation;
import org.milaifontanals.models.Role;
import org.milaifontanals.utils.DateValidatorSimpleDateFormat;

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
    private Calendar selectedCalendar;

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

    private EventsManager eventsDialog;

    private JPanel userInfo;

    public JTextField tfName, tfEmail, tfBirthDate, tfSurname1, tfSurname2, tfPhone;
    public ButtonGroup bgRole, bgLocked, bgGender;
    public JRadioButton genderF, genderM, genderO, lockedY, lockedN, roleA, roleC;
    public JComboBox jbNation;

    private ArrayList<Calendar> selUserCalendarsOwner;
    private ArrayList<Calendar> selUserCalendarsHelper;
    
    DateValidatorSimpleDateFormat datevalidator = new DateValidatorSimpleDateFormat("yyyy-MM-dd");

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

        eventsDialog = new EventsManager(this);
        emailDialog = new SearchEmailDialog();
        nameSurDialog = new SearchNameSurDialog();

        eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
        pTables = new JPanel(new GridLayout(0, 2));
        pCenterDashboard = new JPanel();
        pCenterDashboard.setLayout(new BoxLayout(pCenterDashboard, BoxLayout.PAGE_AXIS));

        //filters & user info
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
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
                        changeUserInfo();
                        showUserInfo();
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
                User u = (User) dropdown.getSelectedItem();
                selectedUser = u;
                close();
                removeCalendars();
                fetchCalendars();
                changeUserInfo();
                showUserInfo();
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
                    if (ownerCalendars.getSelectionModel() == e.getSource()) {
                        int n = ownerCalendars.getSelectedRow();
                        if (n == -1) {
                            return;
                        }
                        selectedCalendar = selUserCalendarsOwner.get(n);
                        eventsDialog.run(selectedCalendar, selectedUser);
                    } else {
                        int n = helperCalendars.getSelectedRow();
                        if (n == -1) {
                            return;
                        }
                        selectedCalendar = selUserCalendarsHelper.get(n);
                        eventsDialog.run(selectedCalendar, selectedUser);
                    }
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
            model.addRow(new Object[]{cal.getTitle(), cal.getDescription(), cal.getStartDate(), cal.getEndDate(), cal.getId()});
        }
    }

    private void removeCalendars() {
        ownerDefTabMod.setRowCount(0);
        helperDefTabMod.setRowCount(0);
    }

    private void fetchCalendars() {
        try {
            selUserCalendarsHelper = Main.db.getUserHelperCalendars(selectedUser.getId());
            selUserCalendarsOwner = Main.db.getUserOwnerCalendars(selectedUser.getId());
            fillCalendarTable(ownerDefTabMod, selUserCalendarsOwner);
            fillCalendarTable(helperDefTabMod, selUserCalendarsHelper);
        } catch (CalendarOrganizerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel initUserInfo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ArrayList<Nation> nations = null;
        try {
            nations = Main.db.getNations();
        } catch (CalendarOrganizerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return panel;
        }

        JButton editButton = new JButton("Edit User");

        bgRole = new ButtonGroup();
        bgLocked = new ButtonGroup();
        bgGender = new ButtonGroup();

        JLabel lTitle = new JLabel("User Information");
        JLabel lName = new JLabel("Name: ");
        JLabel lEmail = new JLabel("Email: ");
        JLabel lGender = new JLabel("Gender: ");
        JLabel lLocked = new JLabel("Is Loked: ");
        JLabel lRole = new JLabel("Role: ");
        JLabel lNation = new JLabel("Nation: ");
        JLabel lBirthDate = new JLabel("Birth Date: ");
        JLabel lSurname1 = new JLabel("First Surname: ");
        JLabel lSurname2 = new JLabel("Second Surname: ");
        JLabel lPhone = new JLabel("Phone: ");

        tfName = new JTextField();
        tfEmail = new JTextField();
        tfEmail.setEditable(false);
        jbNation = new JComboBox();
        tfBirthDate = new JTextField();
        tfSurname1 = new JTextField();
        tfSurname2 = new JTextField();
        tfPhone = new JTextField();

        for (Nation nation : nations) {
            jbNation.addItem(nation);
        }

        genderF = new JRadioButton(Gender.FEMALE.getName());
        genderF.setActionCommand("Famale");
        genderM = new JRadioButton(Gender.MALE.getName());
        genderM.setActionCommand("Male");
        genderO = new JRadioButton(Gender.OTHER.getName());
        genderO.setActionCommand("Other");
        lockedN = new JRadioButton("No");
        lockedN.setActionCommand("lockedN");
        lockedY = new JRadioButton("Yes");
        lockedY.setActionCommand("lockedY");
        roleA = new JRadioButton(Role.ADMIN.getName());
        roleA.setActionCommand("roleA");
        roleC = new JRadioButton(Role.CUSTOMER.getName());
        roleC.setActionCommand("roleC");

        bgGender.add(genderF);
        bgGender.add(genderM);
        bgGender.add(genderO);
        bgLocked.add(lockedN);
        bgLocked.add(lockedY);
        bgRole.add(roleA);
        bgRole.add(roleC);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User u = null;
                try {
                    u = checkUserData();

                    Object[] options = {"Save", "Cancel"};
                    int n = JOptionPane.showOptionDialog(Dashboard.this,
                            "Sure you want to save the changes?",
                            "Question", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null, options, options[1]);

                    if (n == 1 || n == -1) {
                        changeUserInfo();
                    } else if (n == 0) {
                        try {
                            Main.db.updateUser(u);
                            selectedUser = u;
                            changeUserInfo();
                        } catch (CalendarOrganizerException ex) {
                            JOptionPane.showMessageDialog(Dashboard.this, ex.getMessage(),
                                    "Update Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(Dashboard.this, ex.getMessage(),
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        Box bTitle = Box.createHorizontalBox();
        bTitle.add(lTitle);
        bTitle.add(Box.createHorizontalStrut(20));

        Box bEmail = Box.createHorizontalBox();
        bEmail.add(lEmail);
        bEmail.add(Box.createHorizontalStrut(10));
        bEmail.add(tfEmail);

        Box bName = Box.createHorizontalBox();
        bName.add(lName);
        bName.add(Box.createHorizontalStrut(10));
        bName.add(tfName);

        Box bNation = Box.createHorizontalBox();
        bNation.add(lNation);
        bNation.add(Box.createHorizontalStrut(10));
        bNation.add(jbNation);

        Box bBirthDate = Box.createHorizontalBox();
        bBirthDate.add(lBirthDate);
        bBirthDate.add(Box.createHorizontalStrut(10));
        bBirthDate.add(tfBirthDate);

        Box bSurname1 = Box.createHorizontalBox();
        bSurname1.add(lSurname1);
        bSurname1.add(Box.createHorizontalStrut(10));
        bSurname1.add(tfSurname1);

        Box bSurname2 = Box.createHorizontalBox();
        bSurname2.add(lSurname2);
        bSurname2.add(Box.createHorizontalStrut(10));
        bSurname2.add(tfSurname2);

        Box bPhone = Box.createHorizontalBox();
        bPhone.add(lPhone);
        bPhone.add(Box.createHorizontalStrut(10));
        bPhone.add(tfPhone);

        Box bRole = Box.createHorizontalBox();
        bRole.add(lRole);
        bRole.add(Box.createHorizontalStrut(10));
        bRole.add(roleA);
        bRole.add(roleC);

        Box bGender = Box.createHorizontalBox();
        bGender.add(lGender);
        bGender.add(Box.createHorizontalStrut(10));
        bGender.add(genderF);
        bGender.add(genderM);
        bGender.add(genderO);

        Box bLocked = Box.createHorizontalBox();
        bLocked.add(lLocked);
        bLocked.add(Box.createHorizontalStrut(10));
        bLocked.add(lockedN);
        bLocked.add(lockedY);

        Box bButtons = Box.createHorizontalBox();
        bButtons.add(editButton);

        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalStrut(10));
        b.add(bTitle);
        b.add(Box.createVerticalStrut(10));
        b.add(bEmail);
        b.add(Box.createVerticalStrut(10));
        b.add(bName);
        b.add(Box.createVerticalStrut(10));
        b.add(bNation);
        b.add(Box.createVerticalStrut(10));
        b.add(bSurname1);
        b.add(Box.createVerticalStrut(10));
        b.add(bSurname2);
        b.add(Box.createVerticalStrut(10));
        b.add(bPhone);
        b.add(Box.createVerticalStrut(10));
        b.add(bBirthDate);
        b.add(Box.createVerticalStrut(10));
        b.add(bRole);
        b.add(bLocked);
        b.add(Box.createVerticalStrut(10));
        b.add(bGender);
        b.add(Box.createVerticalStrut(20));
        b.add(bButtons);
        b.add(Box.createVerticalStrut(10));

        panel.add(b);
        panel.setVisible(false);
        return panel;
    }

    private void showUserInfo() {
        userInfo.setVisible(true);
        eastPanel.repaint();
    }

    private void changeUserInfo() {
        tfName.setText(selectedUser.getName());
        tfEmail.setText(selectedUser.getEmail());
        bgGender.setSelected(
                (selectedUser.getGender() == Gender.FEMALE ? genderF
                        : selectedUser.getGender() == Gender.MALE ? genderM : genderO).getModel(), true);
        bgLocked.setSelected((selectedUser.isLocked() ? lockedY : lockedN).getModel(), true);
        bgRole.setSelected((selectedUser.getRole() == Role.ADMIN ? roleA : roleC).getModel(), true);
        tfBirthDate.setText(selectedUser.getBirthDate().toString());
        tfSurname1.setText(selectedUser.getSurname1());
        tfSurname2.setText(selectedUser.getSurname2());
        tfPhone.setText(selectedUser.getPhone());

        int num = jbNation.getItemCount();
        int pos = 0;
        for (int i = 0; i < num; i++) {
            Nation item = (Nation) jbNation.getItemAt(i);
            if (item.equals(selectedUser.getNation())) {
                break;
            }
            pos++;
        }
        jbNation.setSelectedIndex(pos);
    }

    private User checkUserData() throws RuntimeException {
        String lockedC = bgLocked.getSelection().getActionCommand();
        String roleC = bgRole.getSelection().getActionCommand();
        String genderC = bgGender.getSelection().getActionCommand();
        System.out.println(roleC);
        boolean locked = lockedC.equals("lockedY");
        Role role = roleC.equals("roleA") ? Role.ADMIN : Role.CUSTOMER;
        Gender gender = genderC.equals(Gender.FEMALE.getName()) ? Gender.FEMALE
                : genderC.equals(Gender.MALE.getName()) ? Gender.MALE : Gender.OTHER;
        System.out.println(genderC);
        System.out.println(role);
        System.out.println(selectedUser);
        Nation nation = (Nation) jbNation.getSelectedItem();

        
        
        if (!datevalidator.isValid(tfBirthDate.getText())) {
            throw new RuntimeException("Invalid date: must be like 2002-03-02");
        }

        Date birthDate = Date.valueOf(tfBirthDate.getText());

        User u = new User(selectedUser.getId(), tfName.getText(), selectedUser.getEmail(), tfSurname1.getText(),
                tfSurname2.getText(), locked, birthDate, gender, role, nation,
                tfPhone.getText().length() == 0 ? null : tfPhone.getText());
        return u;
    }
}
