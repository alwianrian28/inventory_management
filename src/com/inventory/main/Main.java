package com.inventory.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import raven.modal.demo.utils.DemoPreferences;

public class Main extends JFrame {

    public Main() {
        init();

        Image icon = new ImageIcon(
                getClass().getResource("/com/inventory/assets/App-icon.png")
        ).getImage();

        icon = makeRoundedCorner(icon, 180);

        setIconImage(icon);
    }

    private void init() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getRootPane().putClientProperty(
                FlatClientProperties.FULL_WINDOW_CONTENT, true
        );

        FormManager.install(this);

        setSize(new Dimension(1366, 900));
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        System.setProperty("apple.awt.application.name", "Inventory Management");

        DemoPreferences.init();
        FlatLaf.registerCustomDefaultsSource("com.inventory.themes");

        try {

            Font poppins = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File("resources/fonts/Poppins-Regular.ttf")
            ).deriveFont(Font.PLAIN, 12f);

            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();

            ge.registerFont(poppins);

            UIManager.put("defaultFont", poppins);

        } catch (Exception e) {
            e.printStackTrace();
        }

        DemoPreferences.setupLaf();

        try {

            if (Taskbar.isTaskbarSupported()) {

                Image icon = new ImageIcon(
                        Main.class.getResource("/com/inventory/assets/App-icon.png")
                ).getImage();

                icon = makeRoundedCorner(icon, 220);

                Taskbar.getTaskbar().setIconImage(icon);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    // membuat icon rounded
    private static Image makeRoundedCorner(Image image, int cornerRadius) {

        int width = image.getWidth(null);
        int height = image.getHeight(null);

        BufferedImage output = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = output.createGraphics();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        g2.setClip(new RoundRectangle2D.Float(
                0,
                0,
                width,
                height,
                cornerRadius,
                cornerRadius
        ));

        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }
}