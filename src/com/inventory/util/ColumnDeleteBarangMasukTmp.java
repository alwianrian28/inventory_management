package com.inventory.util;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.BarangMasukTmpController;
import com.inventory.model.BarangMasukTmp;
import com.inventory.tablemodel.TabModBarangMasukTmp;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.table.TableColumn;
import net.miginfocom.swing.MigLayout;

public class ColumnDeleteBarangMasukTmp {

    // Renderer tetap sama
    public static class ButtonRenderer extends JPanel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;

        private final JButton deleteButton;

        public ButtonRenderer() {
            setOpaque(false);
            setLayout(new MigLayout("fill, insets 0", "[center]", "[center]"));
            
            deleteButton = new JButton(new FlatSVGIcon(AppResources.ICON_BASE + "delete_white.svg", 0.3f));
            deleteButton.setBackground(Color.RED);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBorderPainted(false);
            deleteButton.setFocusPainted(false);

            add(deleteButton, "w 20!, h 20!");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    // Editor
    public static class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private final JPanel panel;
        private final JButton deleteButton;
        private JTable table;
        private final DeleteCallback callback;

        public ButtonEditor(DeleteCallback callback) {
            this.callback = callback;
            panel = new JPanel(new MigLayout("fill, insets 0", "[center]", "[center]"));

            deleteButton = new JButton(new FlatSVGIcon(AppResources.ICON_BASE + "delete_white.svg", 0.3f));
            deleteButton.setBackground(new Color(158, 0, 0));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBorderPainted(false);
            deleteButton.setFocusPainted(false);
            deleteButton.addActionListener(this);

            panel.add(deleteButton, "w 20!, h 20!");
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.table = table;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row >= 0) { 
                TabModBarangMasukTmp model = (TabModBarangMasukTmp) table.getModel();
                BarangMasukTmp modelTmp = model.getData(row);
                
                BarangMasukTmpController controller = new BarangMasukTmpController();
                controller.deleteData(modelTmp.getBarangMasukTmpID());
                
                model.removeRow(row);
                
                if (callback != null) {
                    callback.onDeleteSuccess();
                }
            }
            fireEditingStopped();
        }
    }

    public static void install(JTable table, int columnIndex, DeleteCallback callback) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(callback));
    }

    public interface DeleteCallback {
        void onDeleteSuccess();
    }
}
