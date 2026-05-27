package com.inventory.util;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Dearclaudia
 */
public class ImageRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, "", isSelected, hasFocus, row, column);

        if (value instanceof ImageIcon) {
            label.setIcon((ImageIcon) value);
            label.setText(null);
            label.setHorizontalAlignment(CENTER);
            label.setVerticalAlignment(CENTER);
        } else {
            label.setIcon(null);
            label.setText("-");
        }

        return label;
    }
}
