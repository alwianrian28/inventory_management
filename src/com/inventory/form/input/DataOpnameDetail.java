package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.StokOpnameDetailController;
import com.inventory.main.FormManager;
import com.inventory.model.Pagination;
import com.inventory.model.StokOpname;
import com.inventory.model.StokOpnameDetail;
import com.inventory.model.User;
import com.inventory.tablemodel.TabModOpnameDetail;
import com.inventory.util.AppResources;
import com.inventory.util.TabelUtils;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Dearclaudia
 */
public class DataOpnameDetail extends JDialog{

    private JPanel overlay;
    
    private final StokOpnameDetailController controller = new StokOpnameDetailController();
    private final TabModOpnameDetail tblModel = new TabModOpnameDetail();
    
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    
    private Pagination pagination;
    private JLabel lbItemsPerPage = new JLabel("0 - 0 of 0");
    private JLabel lblPageInfo = new JLabel("1 of 1");
    
    private StokOpname stokOpname;
    private final User loggedInUser;
    
    public DataOpnameDetail(Frame parent, String title, boolean modal, StokOpname stokOpname) {
        super(parent, title, modal);
        setOverlayJDialog(parent);
        
        this.stokOpname = stokOpname;
        this.loggedInUser = FormManager.getLoggedInUser();
        pagination = new Pagination(25);
        
        init();
        setSize(1350, 600);
        setLocationRelativeTo(parent);
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
        setLayout(new MigLayout("fill, wrap, width 1200, insets 20","[grow,fill]","[]10[fill, grow][]"));
        add(setButton(),"span 2");
        add(setTableData());
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 0","[fill,grow]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("raven/modal/demo/icons/search.svg",0.4f));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchData();
            }
        });
        
        panel.add(txtSearch,"hmin 30");
        
        return panel;
    }

    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0","fill","fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        
        loadData();
        setTableProperties();
        
        JScrollPane scrollPane = new JScrollPane(tblData, 
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scrollPane);
        panel.add(setPagination(),"dock south");
        return panel;
    }

    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblData, new int[]{0}, new int[]{50});
        TabelUtils.setHeaderAlignment(tblData, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblData, new int[]{0}, JLabel.CENTER);
        
        tblData.putClientProperty(FlatClientProperties.STYLE, ""
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;");
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
        String keyword = txtSearch.getText();

        int totalItems = controller.getTotalItems(stokOpname.getOpnameID(),keyword);
        pagination.setTotalItems(totalItems);

        List<StokOpnameDetail> list;
        if (isSearch) {
            list = controller.searchDataById(stokOpname.getOpnameID(), keyword, pagination.getItemsPerPage(), pagination.getCurrentPage());
        } else {
            list = controller.getDataById(stokOpname.getOpnameID(), pagination.getItemsPerPage(), pagination.getCurrentPage());
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
}
