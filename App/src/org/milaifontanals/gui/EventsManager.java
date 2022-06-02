package org.milaifontanals.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.milaifontanals.Main;
import org.milaifontanals.models.Calendar;
import org.milaifontanals.models.Event;
import org.milaifontanals.models.User;
import org.milaifontanals.persistencia.CalendarOrganizerException;

/**
 *
 * @author Gerard Casas
 */
public class EventsManager extends JDialog {

    public JTable eventsTable;
    public DefaultTableModel dtmEvents;
    public JPanel eventsPanel;
    private ArrayList<Event> events;
    private JFrame frame;
    private Calendar calendar;
    private User user;
    private JButton bDeleteEvent, bEditEvent, bAddEvent;
    private ManageButtons buttonsManager;

    public EventsManager(JFrame frame) {
        super(frame);
        this.frame = frame;

        eventsPanel = new JPanel(new BorderLayout());
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1000, 720);
        Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimemsion.width / 2 - getSize().width / 2, dimemsion.height / 2 - getSize().height / 2);
        setResizable(false);

        dtmEvents = new DefaultTableModel();
        eventsTable = makeEventsTable(dtmEvents);
        JScrollPane eventsScroll = new JScrollPane(eventsTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        eventsScroll.setPreferredSize(new Dimension(900, 300));
        eventsPanel.add(eventsScroll, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                removeAllEvents();
                setVisible(false);
            }
        });

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsManager = new ManageButtons();
        bDeleteEvent = initButtonDelete(buttonsManager);
        bAddEvent = initButtonAdd(buttonsManager);
        bEditEvent = initButtonEdit(buttonsManager);

        panelButtons.add(bDeleteEvent);
        panelButtons.add(bAddEvent);
        panelButtons.add(bEditEvent);

        eventsPanel.add(panelButtons, BorderLayout.SOUTH);

        setContentPane(eventsPanel);
        setVisible(false);
    }

    public void run(Calendar calendar, User user) {
        setTitle("Calendar " + calendar.getTitle());
        setUser(user);
        setCalendar(calendar);
        fetchAllEvents();
        setVisible(true);
    }

    public void close() {
        setVisible(false);
        removeAllEvents();
    }

    private JTable makeEventsTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                Class clazz = String.class;
                switch (column) {
                    case 7:
                    case 8:
                        clazz = Timestamp.class;
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
                    System.out.println("fila seleccionada");
                }
            }
        });

        model.addColumn("PUBLISHED");
        model.addColumn("USER-EMAIL");
        model.addColumn("TITLE");
        model.addColumn("DESCRIPTION");
        model.addColumn("COLOR");
        model.addColumn("CATEGORY");
        model.addColumn("START");
        model.addColumn("END");

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);

        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        return table;
    }

    private void removeAllEvents() {
        dtmEvents.setRowCount(0);
    }

    private void fetchAllEvents() {
        try {
            events = Main.db.getCalendarEvents(calendar.getId());
            fillEventsTable(events);
        } catch (CalendarOrganizerException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillEventsTable(ArrayList<Event> events) {
        for (Event event : events) {
            dtmEvents.addRow(new Object[]{event.isPublished() ? "Yes" : "No", event.getUser().getEmail(),
                event.getTitle(), event.getDescription(), event.getColor(),
                event.getCategory().getName(), event.getStart(), event.getEnd()});
        }
    }

    private void setCalendar(Calendar calendar) {
        this.calendar = (Calendar) calendar.clone();
    }

    private void setUser(User user) {
        this.user = (User) user.clone();
    }

    private JButton initButtonDelete(ManageButtons buttonManager) {
        JButton button = new JButton("Delete");
        button.setActionCommand("Delete");
        button.addActionListener(buttonManager);
        return button;
    }

    private JButton initButtonAdd(ManageButtons buttonManager) {
        JButton button = new JButton("Add");
        button.setActionCommand("Add");
        button.addActionListener(buttonManager);
        return button;
    }

    private JButton initButtonEdit(ManageButtons buttonManager) {
        JButton button = new JButton("Edit");
        button.setActionCommand("Edit");
        button.addActionListener(buttonManager);
        return button;
    }

    private class ManageButtons implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            int n = eventsTable.getSelectedRow();
            switch (command) {
                case "Delete":
                    if (n == -1) {
                        JOptionPane.showMessageDialog(EventsManager.this,
                                "Upps! You don't select any table row",
                                "WARNING: Deleting", JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            //System.out.println(Main.db.eventIsPublished(events.get(n).getId()));
                            if (Main.db.eventIsPublished(events.get(n).getId())) {
                                JOptionPane.showMessageDialog(EventsManager.this,
                                        "This event it's published, you can't delete it",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                        } catch (CalendarOrganizerException ex) {
                            JOptionPane.showMessageDialog(EventsManager.this, ex.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        Object[] options = {"Delete", "Cancel"};
                        int op = JOptionPane.showOptionDialog(EventsManager.this,
                                "Sure you want to delete event?",
                                "Question", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null, options, options[1]);

                        if (op == 0) {
                            try {
                                Main.db.deleteEvent(events.get(n).getId());
                                events.remove(n);
                                dtmEvents.removeRow(n);
                            } catch (CalendarOrganizerException ex) {
                                JOptionPane.showMessageDialog(EventsManager.this, ex.getMessage(),
                                        "Update Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    break;
                case "Edit":

                    break;
                case "Add":

                    break;
            }
        }
    }
}