package com.inventory.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.DashboardController;
import com.inventory.form.input.StockInfoDialog;
import com.inventory.model.StokGudang;
import com.inventory.util.AppConfig;
import com.inventory.util.AppResources;
import com.inventory.util.TabelUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import raven.modal.demo.icons.SVGIconUIColor;

/**
 *
 * @author Dearclaudia
 */
public class MainForm extends JPanel {
    private static final long serialVersionUID = 1L;


    private JPanel mainPanel;
    private final DashboardController controller = new DashboardController();
    private final JButton btnAlert = new JButton();

    public MainForm() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap, insets 0, gap 0", "[fill]", "[][fill, grow][][]"));
        add(createHeader());
        add(createMain());
        add(new JSeparator(), "height 2!");
        add(createFooter());

    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("insets 3 3 3 13", "[]push[][]", "30!"));
        //JPanel panel = new JPanel(new MigLayout("insets 3 3 3 140", "[]push[][]", "30!"));

        JToolBar toolbar = new JToolBar();
        JButton buttonDrawer = new JButton(new FlatSVGIcon("raven/modal/demo/icons/menu.svg", 0.5f));

        buttonDrawer.addActionListener((e) -> {
            if (Drawer.isOpen()) {
                Drawer.showDrawer();
            } else {
                Drawer.toggleMenuOpenMode();
            }
        });

        toolbar.add(buttonDrawer);
        
        String nameUser = FormManager.getLoggedInUser().getName();
        JLabel lbName = new JLabel();
        lbName.setText(nameUser);

        checkStock();
        btnAlert.addActionListener(e -> {
            List<StokGudang> lowStock = controller.getLowStockBarangs();
            showLowStockDetails(lowStock);
        });

        btnAlert.setBorderPainted(false);
        btnAlert.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;"
                + "pressedBackground:shade(rgb(235,235,235),5%)");

        JSeparator sep = new JSeparator(JSeparator.VERTICAL);
        sep.setPreferredSize(new Dimension(5, 25));
        
        panel.add(toolbar);
        panel.add(lbName,"center"); 
        panel.add(sep);
        panel.add(btnAlert);
        return panel;
    }

    public void checkStock() {
        List<StokGudang> lowStock = controller.getLowStockBarangs();

        if (lowStock == null || lowStock.isEmpty()) {
            setStockSafeState();
        } else {
            setStockWarningState(lowStock.size());
        }
    }

    private void setStockSafeState() {
        btnAlert.setIcon(new FlatSVGIcon(
                AppResources.ICON_BASE + "alert_normal.svg", 25, 25
        ));
        btnAlert.setToolTipText("Semua stok aman");
    }

    private void setStockWarningState(int count) {
        btnAlert.setIcon(new FlatSVGIcon(
                AppResources.ICON_BASE + "alert_mines.svg", 25, 25
        ));
        btnAlert.setToolTipText("⚠ " + count + " barang stok rendah");
    }

    private void showLowStockDetails(List<StokGudang> lowStock) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parent == null) {
            return;
        }

        StockInfoDialog dialog
                = new StockInfoDialog(parent, "Informasi Stok Rendah", false);
        dialog.setSize(520, 320);
        dialog.setLayout(new BorderLayout());
        
        Point p = btnAlert.getLocationOnScreen();
        int x = p.x + btnAlert.getWidth() - dialog.getWidth();
        int y = p.y + btnAlert.getHeight();
        dialog.setLocation(x, y);

        String[] columns = {"Nama Barang", "Stok", "Minimum"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        if (lowStock == null || lowStock.isEmpty()) {
            model.addRow(new Object[]{"Semua stok aman", "", ""});
        } else {
            for (StokGudang s : lowStock) {
                model.addRow(new Object[]{
                    s.getBarang().getBarangName(),
                    s.getJumlah(),
                    s.getBarang().getStokMinimum()
                });
            }
        }

        JTable table = new JTable(model);
        TabelUtils.setColumnWidths(table, new int[]{0,1,2}, new int[]{250,100,100});
        TabelUtils.setHeaderAlignment(table, new int[]{1,2}, new int[]{JLabel.CENTER,JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(table, new int[]{1,2}, JLabel.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panel = new JPanel(new MigLayout("fill, insets 10", "[grow]", "[grow]"));
        panel.setBackground(Color.WHITE);
        panel.add(scrollPane, "grow");

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
        
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(new MigLayout("insets 1 n 1 n, al trailing center, gapx 10, height 30!", "[]push[][]", "fill"));

        JLabel lbAppVersion = new JLabel(AppConfig.get("app.name") + " " + AppConfig.get("app.perusahaan") + " " + AppConfig.get("app.version"));
        lbAppVersion.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground");
        lbAppVersion.setIcon(new SVGIconUIColor("raven/modal/demo/icons/git.svg", 1f, "Label.disabledForeground"));
        

        String javaVendor = System.getProperty("java.vendor");
        if (javaVendor.equals("Oracle Corporation")) {
            javaVendor = "";
        }

        String java = javaVendor + " v" + System.getProperty("java.version").trim();
        String st = "Running on: Java %s";
        JLabel lbJava = new JLabel(String.format(st, java));
        lbJava.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground");
        lbJava.setIcon(new SVGIconUIColor("raven/modal/demo/icons/java.svg", 1f, "Label.disabledForeground"));
        

        JLabel lbDate = new JLabel();
        lbDate.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground");
        Timer timer = new Timer(1, (e) -> {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            lbDate.setText(df.format(new Date()));
        });
        
        panel.add(lbAppVersion);
        panel.add(lbJava);
        panel.add(new JSeparator(JSeparator.VERTICAL));
        panel.add(lbDate);

        timer.start();
        return panel;
    }

    private Component createMain() {
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    public void setForm(Form form) {
        mainPanel.removeAll();
        mainPanel.add(form);
        mainPanel.repaint();
        mainPanel.revalidate();

        if (mainPanel.getComponentOrientation().isLeftToRight() != form.getComponentOrientation().isLeftToRight()) {
            applyComponentOrientation(mainPanel.getComponentOrientation());
        }
    }

}
