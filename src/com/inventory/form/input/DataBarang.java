package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.BarangController;
import com.inventory.main.FormManager;
import com.inventory.model.Barang;
import com.inventory.model.Pagination;
import com.inventory.model.StatusItem;
import com.inventory.model.Supplier;
import com.inventory.model.User;
import com.inventory.tablemodel.TabModBarang;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.TabelUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
public class DataBarang extends JDialog{

    private JPanel overlay;
    
    private final BarangController controller = new BarangController();
    private final TabModBarang tblModel = new TabModBarang();
    
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    private JComboBox<StatusItem> cbxStatus;
    private JLabel lblImage;
    
    private Pagination pagination;
    private JLabel lbItemsPerPage = new JLabel("0 - 0 of 0");
    private JLabel lblPageInfo = new JLabel("1 of 1");
    
    private Barang selectedBarang;
    private Supplier selectedSupplier;
    private final User loggedInUser;
    
    public DataBarang(Frame parent, String title, boolean modal, Supplier selectedSupplier) {
        super(parent, title, modal);
        setOverlayJDialog(parent);
        
        this.selectedSupplier = selectedSupplier;
        this.loggedInUser = FormManager.getLoggedInUser();
        pagination = new Pagination(25);
        
        init();
        configureRole();
        cbxStatus.setSelectedIndex(0);
        
        setSize(1350, 600);
        setLocationRelativeTo(parent);
    }
    
    private void configureRole() {
        if(loggedInUser.getRole().equals("Admin")){
            cbxStatus.setVisible(true);
        }else{
            cbxStatus.setVisible(false);
        }
    }
    
    /* --------------------- Set Overlay Background Transparant --------------------- */

    private void setOverlayJDialog(Frame parent){
        RootPaneContainer rpc = (RootPaneContainer) parent;

        overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 150)); // transparan
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        overlay.setOpaque(false);

        overlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        rpc.setGlassPane(overlay);
    }
    
    @Override
    public void setVisible(boolean b) {
        Window parent = getOwner();

        if (b) {
            if (parent instanceof RootPaneContainer) {
                ((RootPaneContainer) parent).getGlassPane().setVisible(true);
            }
        }

        super.setVisible(b);

        if (!b) {
            if (parent instanceof RootPaneContainer) {
                ((RootPaneContainer) parent).getGlassPane().setVisible(false);
                parent.repaint();
            }
        }
    }

    @Override
    public void dispose() {
        // pastikan overlay dimatikan walaupun dispose langsung dipanggil
        Window parent = getOwner();
        if (parent instanceof RootPaneContainer) {
            ((RootPaneContainer) parent).getGlassPane().setVisible(false);
            parent.repaint();
        }
        super.dispose();
    }
    
    /* --------------------- Main Code --------------------- */
    
    private void init() {
        setLayout(new MigLayout("fill, wrap, insets 20","[grow,fill][200!]","[]10[fill, grow][]"));
        add(setButton(),"span 2");
        add(setTableData());
        add(setImagePanel(),"grow");
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 0","[][fill,grow][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JButton btnChoose = new JButton("Pilih");
        btnChoose.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);"
                + "disabledBackground:@accentColor;"
                + "disabledText:rgb(255,255,255)");
        btnChoose.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "select.svg",0.4f));
        btnChoose.setIconTextGap(5);
        btnChoose.addActionListener((e) -> {
            getData();
        });
        
        cbxStatus = new JComboBox();
        cbxStatus.addItem(new StatusItem("Aktif", false));
        cbxStatus.addItem(new StatusItem("Deleted", true));
        cbxStatus.addActionListener(e -> {
            handleStatusChange(btnChoose);
        });        
        
        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("raven/modal/demo/icons/search.svg",0.4f));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchData();
            }
        });
        
        panel.add(btnChoose,"hmin 30");
        panel.add(txtSearch,"hmin 30");
        panel.add(cbxStatus,"hmin 30, wmin 50, hidemode 3");
        
        return panel;
    }

    private void handleStatusChange(JButton btnChoose) {
        StatusItem selected = (StatusItem) cbxStatus.getSelectedItem();
        if (selected != null) {
            boolean isDeleted = selected.getValue();
            btnChoose.setEnabled(!isDeleted);
        }
        pagination.firstPage();
        loadData();
    }
    
    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0","fill","fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        
        loadData();
        hideColumnId();
        setTableProperties();
        
        tblData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblData.getSelectedRow();
                if (row != -1) {
                    selectedBarang = tblModel.getData(row);
                    setPhoto(selectedBarang.getPhoto());
                }

                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    e.consume();
                    getData();
                }
            }
        });
        
        tblData.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                
            }

            @Override
            public void focusLost(FocusEvent e) {
                lblImage.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "barang.svg", 150, 150));
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tblData, 
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scrollPane);
        panel.add(setPagination(),"dock south");
        return panel;
    }

    private JPanel setImagePanel() {
        JPanel panel = new JPanel(new MigLayout("fill,insets 5","[center]","[center]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");

        lblImage = new JLabel();
        lblImage.setPreferredSize(new Dimension(200, 200));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setVerticalAlignment(JLabel.CENTER);

        lblImage.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "barang.svg", 150, 150));

        panel.add(lblImage, "growx, top");
        return panel;
    }

    private void hideColumnId(){
        tblData.getColumnModel().getColumn(11).setMinWidth(0);
        tblData.getColumnModel().getColumn(11).setMaxWidth(0);
        tblData.getColumnModel().getColumn(11).setWidth(0);
    }
    
    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblData, new int[]{0,1,2,4,5,6,7,8,9}, new int[]{50,70,110,60,120,60,70,100,80});
        TabelUtils.setHeaderAlignment(tblData, new int[]{0,4,5,6,7,8,9}, new int[]{JLabel.CENTER,JLabel.CENTER,JLabel.CENTER,JLabel.CENTER,JLabel.CENTER,JLabel.CENTER,JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblData, new int[]{0,4,5,6,7,8,9}, JLabel.CENTER);
        
        tblData.putClientProperty(FlatClientProperties.STYLE, ""
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "rowHeight:70");
    }
    
    private JButton createNavButton(String icon, Runnable action) {
        JButton btn = new JButton(new FlatSVGIcon(AppResources.ICON_BASE + icon, 0.4f));
        btn.putClientProperty(FlatClientProperties.STYLE, "arc:10;borderWidth:0");
        btn.addActionListener(e -> action.run());
        return btn;
    }
    
    private JPanel setPagination(){
        JPanel panel = new JPanel(new MigLayout("fillx, insets 5 10 5 10", "[grow][right]", "center"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(206,206,206)));
        
        JButton btnFirstPage = createNavButton("first_page.svg", () -> { pagination.firstPage(); loadData(); });
        JButton btnBefore    = createNavButton("before.svg",     () -> { pagination.previousPage(); loadData(); });
        JButton btnNext      = createNavButton("next.svg",       () -> { pagination.nextPage(); loadData(); });
        JButton btnLastPage  = createNavButton("last_page.svg",  () -> { pagination.lastPage(); loadData(); });
        
        panel.add(lbItemsPerPage);
        panel.add(btnFirstPage,"hmin 20, wmin 20");
        panel.add(btnBefore,"hmin 20, wmin 20");
        panel.add(lblPageInfo);
        panel.add(btnNext,"hmin 20, wmin 20");
        panel.add(btnLastPage,"hmin 20, wmin 20");
        
        return panel;
    }
    
    private void updateTable(boolean isSearch) {
        StatusItem selected = (StatusItem) cbxStatus.getSelectedItem();
        boolean isDeleted = selected.getValue();
        String keyword = txtSearch.getText();
        String supplierName = (selectedSupplier == null) ? null : selectedSupplier.getSupplierName();
        
        int totalItems = controller.getTotalItems(isDeleted, keyword);
        pagination.setTotalItems(totalItems);

        List<Barang> list;
        if (isSearch) {
            list = controller.searchDataBySupplier(supplierName, keyword, isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
        } else {
            list = controller.getDataBySupplier(supplierName, isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
        }

        tblModel.setData(list);
        int offset = (pagination.getCurrentPage() - 1) * pagination.getItemsPerPage();
        tblModel.setRowOffset(offset);
        tblData.setModel(tblModel);

        int start = offset + 1;
        int end = Math.min(pagination.getCurrentPage() * pagination.getItemsPerPage(), totalItems);

        lbItemsPerPage.setText(totalItems > 0 ? start + " - " + end + " of " + totalItems : "0 - 0 of 0");
        lblPageInfo.setText(pagination.getCurrentPage() + " of " + pagination.getTotalPages());
    }

    private void loadData() { 
        updateTable(false); 
    }
    
    private void searchData() { 
        updateTable(true); 
    }
    
    public void refreshTable(){
        loadData();
    }
    
    private void setPhoto(String path) {
        if (path != null && !path.isEmpty()) {
            ImageIcon originalIcon = new ImageIcon(path);
            Image originalImage = originalIcon.getImage();

            int labelWidth = 150;
            int labelHeight = 150;

            int imgWidth = originalIcon.getIconWidth();
            int imgHeight = originalIcon.getIconHeight();

            double aspectRatio = (double) imgWidth / imgHeight;
            int newWidth, newHeight;

            if (imgWidth > imgHeight) {
                newWidth = labelWidth;
                newHeight = (int) (labelWidth / aspectRatio);
            } else {
                newHeight = labelHeight;
                newWidth = (int) (labelHeight * aspectRatio);
            }

            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImage));
        } else {
            lblImage.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "barang.svg", 150, 150));
        }
    }

    private void getData() {
        int row = tblData.getSelectedRow();
        if(row != -1){
            selectedBarang = tblModel.getData(row);
            if (listener != null) listener.onMedicineSelected(selectedBarang);
            dispose();
        }else{
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data yang ingin dipilih", getOptionAlert());
        }
    }
    
    public interface selectionListenerBarang {
        void onMedicineSelected(Barang barang);
    }

    private selectionListenerBarang listener;

    public void setSelectionListener(selectionListenerBarang listener) {
        this.listener = listener;
    }
}
