package com.inventory.util;

import com.inventory.main.Main;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.LoggingFacade;
import com.inventory.config.DatabaseConnection;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.DefaultCaret;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Dearclaudia
 */
public class About extends JPanel{
    private static final long serialVersionUID = 1L;


    private JLabel imageLogo;
    
    public About() {
        init();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 0 20,width 500", "[fill,330::]", ""));
        
        imageLogo = new JLabel();
        imageLogo.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/com/inventory/assets/Logo1.png"))
                .getImage()
                .getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        
        JTextPane title = createText(AppConfig.get("app.name"));
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +5");
        
        JTextPane namaPT = createText(AppConfig.get("app.perusahaan"));
        namaPT.putClientProperty(FlatClientProperties.STYLE, "font:bold +5");

        JTextPane description = createText("");
        description.setContentType("text/html");
        description.setText(getDescriptionText());
        description.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    showUrl(e.getURL());
                } catch (URISyntaxException ex) {
                    Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        add(imageLogo);
        add(title);
        add(namaPT);
        add(description);
        add(createSystemInformation());
        add(createLibraryInformation());
    }

    private JTextPane createText(String text) {
        JTextPane textPane = new JTextPane();
        textPane.setOpaque(false);
        textPane.setBorder(BorderFactory.createEmptyBorder());
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setCaret(new DefaultCaret() {
            @Override
            public void paint(Graphics g) {
            }
        });
        return textPane;
    }

    private String getDescriptionText() {
        String text = AppConfig.get("app.detail") +
                "<br>For source code, message to me <a href=\"https://wa.me/6287893036298\">Dearclaudia.</a><br><br>" +
                "<b>Created by Dearclaudia</b>";
        return text;
    }


    private String getSystemInformationText() {
        String text = "<b>App Version: </b>%s<br/>" +
                "<b>Java: </b>%s<br/>" +
                "<b>MySQL: </b>%s<br/>" +
                "<b>System: </b>%s<br/>"; 

        return text;
    }

     private String getLibraryInformationText() {
        String text = "<div style=\"padding-left: 10px;\">" +
                    "  <b>• MySQL Connector</b><br>\n" +
                    "  <b>• JBcrypt</b><br>\n" +
                    "  <b>• JasperReport</b><br>\n" +
                    "  <b>• JFreeChart</b><br>\n" +
                    "  <b>• DatePicker</b><br>\n" +
                    "  <b>• FlatLaf</b><br>\n" +
                    "  <b>• MigLayout</b><br> </div>";
        return text;
    }
    
    private JComponent createSystemInformation() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("System Information"));
        panel.setOpaque(false);
        
        JTextPane textPane = createText("");
        textPane.setContentType("text/html");
        String version = AppConfig.get("app.version");
        String java = System.getProperty("java.vendor") + " - v" + System.getProperty("java.version");
        String system = System.getProperty("os.name") + " " + System.getProperty("os.arch") + " - v" + System.getProperty("os.version");
        String mysqlVersion = getMySQLVersion();
        
        String text = String.format(getSystemInformationText(),
                version,
                java,
                mysqlVersion,
                system);
        textPane.setText(text);
        panel.add(textPane);
        return panel;
    }
    
    private String getMySQLVersion() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                return rs.getString(1); // Mengambil versi MySQL
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Not Connected";
    }
    
    private JComponent createLibraryInformation() {
        JPanel panel = new JPanel(new MigLayout("wrap"));
        panel.setBorder(new TitledBorder("Libraries Used"));
        panel.setOpaque(false);
        
        JTextPane textPane = createText("");
        textPane.setContentType("text/html");
        String text = getLibraryInformationText();
        textPane.setText(text);

        textPane.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                try {
                    showUrl(e.getURL());
                } catch (URISyntaxException ex) {
                    Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        panel.add(textPane);
        return panel;
    }

    private void showUrl(URL url) throws URISyntaxException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(url.toURI());
                } catch (IOException | URISyntaxException e) {
                    LoggingFacade.INSTANCE.logSevere("Error browse url", e);
                }
            }
        }
    }
}
