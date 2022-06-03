package org.milaifontanals.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.MaskFormatter;
import org.milaifontanals.Main;
import org.milaifontanals.models.Calendar;
import org.milaifontanals.models.Category;
import org.milaifontanals.models.User;
import org.milaifontanals.models.Event;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.utils.DateValidatorSimpleDateFormat;

/**
 *
 * @author Gerard Casas
 */
public class AddEditEvent extends JDialog {

    private JPanel panel;
    private JComboBox category;
    private JTextField title;
    private JTextArea description;
    private JTextField location;
    private JFormattedTextField color;
    private JFormattedTextField start;
    private JFormattedTextField end;
    private JButton bSave;
    private boolean editing;
    private ArrayList<Category> categories = new ArrayList<>();
    private Event event;
    private User user;
    private SimpleDateFormat formatter;
    private EventsManager parentDialog;
    private Calendar calendar;
    private DateValidatorSimpleDateFormat datevalidator = new DateValidatorSimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AddEditEvent(EventsManager parentDialog) {
        super(parentDialog, true);
        this.parentDialog = parentDialog;

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        Dimension dimemsion = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimemsion.width / 2 - getSize().width / 2, dimemsion.height / 2 - getSize().height / 2);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                close();
            }
        });

        initView();

        add(panel);
        pack();
        setResizable(false);
        setVisible(false);
    }

    public void close() {
        setVisible(false);
        resetInputs();
    }

    public void run(String title, ArrayList<Category> categories, Event event, User user, Calendar calendar, boolean editing) {
        setTitle(title);
        this.editing = editing;
        this.event = event;
        this.user = user;
        this.calendar = calendar;
        resetInputs();
        if (editing) {
            setInputs(event);
        }
        setCategories(categories);
        setVisible(true);
    }

    private void initView() {
        bSave = new JButton("Save");
        bSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Event ev = null;
                try {
                    ev = getNewEvent();

                    if (!datevalidator.isValid(start.getText())) {
                        throw new RuntimeException("Invalid start date");
                    }
                    
                    if (!datevalidator.isValid(end.getText())) {
                        throw new RuntimeException("Invalid end date");
                    }

                    Date calStart = calendar.getStartDate();
                    Date calEnd = calendar.getEndDate();
                    if ((ev.getStart().before(calStart) || ev.getStart().after(calEnd))
                            || (ev.getEnd().after(calEnd) || ev.getEnd().before(calStart))) {
                        throw new RuntimeException("The start date or the end date exceed the dates of the calendar to which they belong");
                    }

                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(AddEditEvent.this, ex.getMessage(),
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    if (editing) {
                        Main.db.updateEvent(ev);
                    } else {
                        Main.db.createEvent(ev, calendar.getId());
                    }
                    parentDialog.refreshEventsTable();
                    close();
                } catch (CalendarOrganizerException ex) {
                    JOptionPane.showMessageDialog(AddEditEvent.this, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lTitle = new JLabel("Title: ");
        JLabel lDesc = new JLabel("Description: ");
        JLabel lCat = new JLabel("Category: ");
        JLabel lLoc = new JLabel("Location: ");
        JLabel lColor = new JLabel("Color: ");
        JLabel lStart = new JLabel("Start: ");
        JLabel lEnd = new JLabel("End: ");

        title = new JTextField();
        category = new JComboBox();
        description = new JTextArea();
        location = new JTextField();
        color = new JFormattedTextField(createFormatter("'#HHHHHH"));
        color.setColumns(7);
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        start = new JFormattedTextField(createFormatter("####-##-## ##:##:##"));
        start.setColumns(20);
        end = new JFormattedTextField(createFormatter("####-##-## ##:##:##"));
        end.setColumns(20);

        Box bTitle = Box.createHorizontalBox();
        bTitle.add(lTitle);
        bTitle.add(Box.createHorizontalStrut(10));
        bTitle.add(title);

        Box bLoc = Box.createHorizontalBox();
        bLoc.add(lLoc);
        bLoc.add(Box.createHorizontalStrut(10));
        bLoc.add(location);

        Box bCat = Box.createHorizontalBox();
        bCat.add(lCat);
        bCat.add(Box.createHorizontalStrut(10));
        bCat.add(category);

        Box bColor = Box.createHorizontalBox();
        bColor.add(lColor);
        bColor.add(Box.createHorizontalStrut(10));
        bColor.add(color);

        Box bStart = Box.createHorizontalBox();
        bStart.add(lStart);
        bStart.add(Box.createHorizontalStrut(10));
        bStart.add(start);

        Box bEnd = Box.createHorizontalBox();
        bEnd.add(lEnd);
        bEnd.add(Box.createHorizontalStrut(10));
        bEnd.add(end);

        JScrollPane jcp = new JScrollPane(description,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jcp.setPreferredSize(new Dimension(200, 200));

        Box bDesc = Box.createHorizontalBox();
        bDesc.add(lDesc);
        bDesc.add(Box.createHorizontalStrut(10));
        bDesc.add(jcp);

        Box bButton = Box.createHorizontalBox();
        bButton.add(bSave);

        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalStrut(10));
        b.add(bTitle);
        b.add(Box.createVerticalStrut(10));
        b.add(bLoc);
        b.add(Box.createVerticalStrut(10));
        b.add(bCat);
        b.add(Box.createVerticalStrut(10));
        b.add(bColor);
        b.add(Box.createVerticalStrut(10));
        b.add(bStart);
        b.add(Box.createVerticalStrut(10));
        b.add(bEnd);
        b.add(Box.createVerticalStrut(10));
        b.add(bDesc);
        b.add(Box.createVerticalStrut(10));
        b.add(bSave);

        panel.add(b);
    }

    private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException ex) {
        }
        return formatter;
    }

    private void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
        category.removeAllItems();
        int pos = -1;
        int i = 0;
        for (Category cat : categories) {
            category.addItem(cat);
            if (editing && cat.getId() == event.getCategory().getId()) {
                pos = i;
            }
            i++;
        }

        category.setSelectedIndex(pos);
    }

    private Event getNewEvent() throws RuntimeException {
        Timestamp start = null;
        Timestamp end = null;

        try {
            start = new Timestamp(formatter.parse(this.start.getText()).getTime());
            end = new Timestamp(formatter.parse(this.end.getText()).getTime());
        } catch (ParseException pe) {
            throw new RuntimeException("Invalid start or end date");
        }

        Event e = null;
        if (editing) {
            e = new Event(event.getId(), ((Category) category.getSelectedItem()),
                    user, title.getText(), description.getText(), location.getText(),
                    event.isPublished(), color.getText(), start, end);
        } else {
            e = new Event(0, ((Category) category.getSelectedItem()),
                    user, title.getText(), description.getText(), location.getText(),
                    false, color.getText(), start, end);
        }

        return e;
    }

    private void resetInputs() {
        title.setText("");
        description.setText("");
        location.setText("");
        color.setText("");
        start.setText("");
        end.setText("");
        category.removeAllItems();
    }

    private void setInputs(Event event) {
        title.setText(event.getTitle());
        description.setText(event.getDescription());
        location.setText(event.getLocation());
        color.setText(event.getColor());
        start.setText(formatter.format(event.getStart()));
        end.setText(formatter.format(event.getEnd()));
    }

}
