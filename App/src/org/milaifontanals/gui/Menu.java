package org.milaifontanals.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import static javax.swing.KeyStroke.getKeyStroke;
import org.milaifontanals.persistencia.CalendarOrganizerException;
import org.milaifontanals.utils.OpenBrowser;
import org.milaifontanals.utils.ReadProperties;

/**
 *
 * @author Gerard Casas
 */
public class Menu {
    
    private JMenuBar menu;
    
    public Menu(JFrame frame) throws CalendarOrganizerException {
        
        setMenuBar(new JMenuBar());
        
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        
        JMenu generic = new JMenu("Generic");
        generic.setMnemonic(KeyEvent.VK_G);

        JMenuItem exit = new JMenuItem("Exit", 'E');
        exit.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        exit.setAccelerator(getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));

        HashMap<String,String> urls = getWebUrls();
        
        JMenuItem helpWeb = new JMenuItem("Help Web");
        helpWeb.setAccelerator(getKeyStroke(KeyEvent.VK_F2, KeyEvent.ALT_DOWN_MASK));
        helpWeb.addActionListener(new OpenBrowser(urls.get("web_help_url"), frame));

        JMenuItem website = new JMenuItem("Website");
        website.setAccelerator(getKeyStroke(KeyEvent.VK_F1, KeyEvent.ALT_DOWN_MASK));
        website.addActionListener(new OpenBrowser(urls.get("web_url"), frame));
        
        JMenuItem about = new JMenuItem("About", 'A');
        about.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(frame, 
                    "Name: Gerard Casas Serarols\n"
                    + "Tel: 611451460\n"
                    + "Email: gcasas@milaifontanals.org",
                    "About me", JOptionPane.PLAIN_MESSAGE);
        });
        
        generic.add(website);
        generic.add(exit);
        help.add(helpWeb);
        help.add(about);

        menu.add(generic);
        menu.add(help);
    }

    public JMenuBar getMenuBar() {
        return menu;
    }

    public void setMenuBar(JMenuBar menu) {
        this.menu = menu;
    }
    
    private HashMap<String,String> getWebUrls() throws CalendarOrganizerException {
        HashMap<String,String> props = new ReadProperties("env.properties", 
                new ArrayList<>(){{
                    add("web_url");
                    add("web_help_url");
                }})
                .getPropertiesReaded();
        
        if (props.get("web_url") == null || props.get("web_help_url") == null) {
            throw new CalendarOrganizerException("Need properites: web_url, web_help_url on file env.properties");
        }

        return props;
    }
}
