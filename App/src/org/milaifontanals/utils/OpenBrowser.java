package org.milaifontanals.utils;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Gerard Casas
 */
public class OpenBrowser implements ActionListener {

    private String url;
    private JFrame frame;

    public OpenBrowser(String url, JFrame frame) {
        this.url = url;
        this.frame = frame == null ? new JFrame() : frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openBrowser(url);
    }

    private void openBrowser(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException x) {
                JOptionPane.showMessageDialog(frame, "Error opening browser", "Error", JOptionPane.ERROR);
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException x) {
                JOptionPane.showMessageDialog(frame, "Error opening browser", "Error", JOptionPane.ERROR);
            }
        }
    }
}
