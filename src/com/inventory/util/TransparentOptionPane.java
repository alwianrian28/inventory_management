package com.inventory.util;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Dearclaudia
 */
public class TransparentOptionPane {

    private static JPanel createGlassPane() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150)); // hitam transparan
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    public static int showConfirmDialog(JFrame frame, String message, String title, int optionType) {
        JPanel glass = createGlassPane();
        glass.setOpaque(false);

        if (frame != null) {
            frame.setGlassPane(glass);
            glass.setVisible(true);
        }

        JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, optionType);
        JDialog dialog = optionPane.createDialog(frame, title);

        dialog.setLocationRelativeTo(frame); // center ke frame utama
        dialog.setVisible(true);

        if (frame != null) {
            glass.setVisible(false);
        }

        Object selectedValue = optionPane.getValue();
        if (selectedValue instanceof Integer) {
            return (Integer) selectedValue;
        }
        return JOptionPane.CLOSED_OPTION;
    }

    public static void showMessageDialog(JFrame frame, String message, String title, int messageType) {
        JPanel glass = createGlassPane();
        glass.setOpaque(false);

        if (frame != null) {
            frame.setGlassPane(glass);
            glass.setVisible(true);
        }

        JOptionPane optionPane = new JOptionPane(message, messageType);
        JDialog dialog = optionPane.createDialog(frame, title);

        dialog.setLocationRelativeTo(frame); // center ke frame utama
        dialog.setVisible(true);

        if (frame != null) {
            glass.setVisible(false);
        }
    }

    // 🔹 Tambahan khusus untuk Exception (shortcut ganti null → parent default)
    public static void showErrorDialog(JFrame frame, Exception e) {
        showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static File showSaveFileDialog(JFrame frame, String title, FileNameExtensionFilter filter, File defaultFile) {
        JPanel glass = createGlassPane();
        glass.setOpaque(false);

        if (frame != null) {
            frame.setGlassPane(glass);
            glass.setVisible(true);
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        if (filter != null) chooser.setFileFilter(filter);
        if (defaultFile != null) chooser.setSelectedFile(defaultFile);

        int result = chooser.showSaveDialog(frame);

        if (frame != null) {
            glass.setVisible(false);
        }

        return result == JFileChooser.APPROVE_OPTION ? chooser.getSelectedFile() : null;
    }
}
