package com.inventory.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.BarangController;
import com.inventory.controller.StokOpnameController;
import com.inventory.controller.StokOpnameDetailController;
import com.inventory.controller.StokOpnameTmpController;
import com.inventory.form.input.DataBarang;
import com.inventory.form.input.DataOpnameDetail;
import com.inventory.main.Form;
import com.inventory.main.FormManager;
import com.inventory.model.Barang;
import com.inventory.model.Pagination;
import com.inventory.model.StokOpname;
import com.inventory.model.StokOpnameTmp;
import com.inventory.model.StatusItem;
import com.inventory.model.User;
import com.inventory.tablemodel.TabModOpname;
import com.inventory.tablemodel.TabModOpnameTmp;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.ColumnDeleteOpnameTmp;
import com.inventory.util.MessageModal;
import com.inventory.util.ModalBorder;
import com.inventory.util.NumericDocumentFilter;
import com.inventory.util.TabelUtils;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DatePicker;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.option.Option;

/**
 *
 * @author Dearclaudia
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormStokOpname extends Form{
    private static final long serialVersionUID = 1L;


    private JPanel mainPanel, panelView, panelAdd;
    
    private final StokOpnameController controllerOpname = new StokOpnameController();
    private final StokOpnameTmpController controllerOpnameTmp = new StokOpnameTmpController();
    private final StokOpnameDetailController controllerOpnameDetail = new StokOpnameDetailController();
    
    private final BarangController controllerBarang = new BarangController();
    
    private final TabModOpname tblModel = new TabModOpname();
    private final TabModOpnameTmp tblModelTmp = new TabModOpnameTmp();
    
    private final JTable tblData = new JTable();
    private final JTable tblDataTmp = new JTable();
    
    private JButton btnRestore, btnAdd, btnAddStokOpname, btnCancelView;
    private JButton btnSave = new JButton("Save");
    private JButton btnCancelAdd = new JButton("Cancel");
    
    private JTextField txtSearch, txtKeterangan, txtBarcode, txtBarangName, txtSatuan;
    private JTextField txtStokSistem, txtStokFisik, txtSelisih, txtCatatan;
    private JFormattedTextField txtTanggalOpname;
    private DatePicker pickerTanggalOpname;
    
    private JComboBox<StatusItem> cbxStatus;
    private JComboBox cbxStatusOpname;
    
    JLabel lbTItle = new JLabel();
    JLabel lbBreadcrumb = new JLabel();
    
    private final Pagination pagination;
    private final JLabel lbItemsPerPage = new JLabel("0 - 0 of 0");
    private final JLabel lblPageInfo = new JLabel("1 of 1");
    
    private Barang selectedBarang;
    private int opnameTmpID;
    private int opnameID;
    private final User loggedInUser;
    
    public FormStokOpname() {
        this.loggedInUser = FormManager.getLoggedInUser();
        pagination = new Pagination(25);
        
        init();
        setTableProperties();
        configureRole();
        cbxStatus.setSelectedIndex(0);
        setActionButton();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, "view");
        loadData();
        loadDataTmp();
        resetForm();
        resetFormTmp();
    }
    
    private void configureRole() {
        if ("Admin".equals(loggedInUser.getRole())) {
            btnRestore.setVisible(false);
            cbxStatus.setVisible(true);
        } else {
            btnRestore.setVisible(false);
            cbxStatus.setVisible(false);
        }
    }
    
    private void init() {
        setLayout(new MigLayout("fill, insets 0","fill","fill"));
        mainPanel = new JPanel(new CardLayout());
        
        //View
        panelView = new JPanel(new MigLayout("fill", "fill", "fill"));
        panelView.add(setInfo(), "wrap");
        JPanel separatorView = new JPanel(new MigLayout());
        separatorView.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");
        panelView.add(separatorView,"growx, h 1px!, wrap");
        panelView.add(setButton(), "wrap");
        panelView.add(setTableData(), "grow, push");

        // Add
        panelAdd = new JPanel(new MigLayout("fill", "fill", "fill"));
        panelAdd.add(setInfoAdd(), "wrap");
        JPanel separatorAdd = new JPanel(new MigLayout());
        separatorAdd.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");
        panelAdd.add(separatorAdd,"growx, h 1px!, wrap");
        panelAdd.add(setButtonAdd(), "wrap");
        panelAdd.add(setFormInput(), "grow, push");
        
        // tambahkan card name biar bisa dipanggil
        mainPanel.add(panelView, "view");
        mainPanel.add(panelAdd, "add");

        add(mainPanel, "grow, push");
    }

    /* ===================== VIEW ===================== */
    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill","[][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JLabel imageLogo = new JLabel();
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "stock_opname.svg", 0.4f));
        
        JLabel lbTItleView = new JLabel("Data Stok Opname");
        lbTItleView.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumbView = new JLabel("TRANSAKSI > Stok Opname");
        lbBreadcrumbView.putClientProperty(FlatClientProperties.STYLE, "font:14;foreground:rgb(120,120,120)");
        
        panel.add(imageLogo);
        panel.add(lbTItleView);
        panel.add(lbBreadcrumbView);
        return panel;
    }
    
    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap","[][][][]push[][][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        btnAddStokOpname = new JButton("Add");
        btnAddStokOpname.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);"
                + "disabledBackground:@accentColor;"
                + "disabledText:rgb(255,255,255)");
        btnAddStokOpname.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "add_white.svg",0.8f));
        btnAddStokOpname.setIconTextGap(5);
        btnAddStokOpname.addActionListener((e) -> {
            if(btnAddStokOpname.getText().equals("Add")){
                ((CardLayout) mainPanel.getLayout()).show(mainPanel, "add");
                txtBarcode.requestFocus();
                setDateUser();
            }else{
                loadDetailToTmp();
            }
        });
        
        JButton btnDelete = new JButton("Delete");
        btnDelete.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "delete.svg",0.4f));
        btnDelete.setIconTextGap(5);
        btnDelete.addActionListener((e) -> {
            deleteData();
        });
        
        JButton btnDetail = new JButton("Detail");
        btnDetail.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "detail.svg",0.4f));
        btnDetail.setIconTextGap(5);
        btnDetail.addActionListener((e) -> {
            showStokOpnameDetail();
        });
        
        btnCancelView = new JButton("Cancel");
        btnCancelView.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "cancel.svg",0.4f));
        btnCancelView.setIconTextGap(5);
        btnCancelView.addActionListener((e) -> {
            setVisibleButton();
        });
        
        btnRestore = new JButton("Restore");
        btnRestore.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "restore.svg",0.4f));
        btnRestore.setIconTextGap(5);
        btnRestore.addActionListener((e) -> {
            restoreData();
        });
        
        cbxStatus = new JComboBox();
        cbxStatus.addItem(new StatusItem("Aktif", false));
        cbxStatus.addItem(new StatusItem("Deleted", true));
        cbxStatus.addActionListener(e -> {
            handleStatusChange(btnAdd, btnDelete, btnDetail);
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
        
        panel.add(btnAddStokOpname,"hmin 30, wmin 90");
        panel.add(btnDelete,"hmin 30, wmin 50");
        panel.add(btnDetail,"hmin 30, wmin 50");
        panel.add(btnCancelView,"hmin 30, wmin 50");
        panel.add(txtSearch,"hmin 30, width 300, gapx 8");
        panel.add(btnRestore,"hmin 30, wmin 50, hidemode 3");
        panel.add(cbxStatus,"hmin 30, wmin 50, hidemode 3");
        
        return panel;
    }

    private void handleStatusChange(JButton btnAdd, JButton btnDelete, JButton btnDetail) {
        StatusItem selected = (StatusItem) cbxStatus.getSelectedItem();
        if (selected != null) {
            boolean isDeleted = selected.getValue();
            btnRestore.setVisible(isDeleted);
            btnAdd.setEnabled(!isDeleted);
            btnDelete.setEnabled(!isDeleted);
            btnDetail.setEnabled(!isDeleted);
        }
        pagination.firstPage();
        loadData();
    }
    
    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0","fill","fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        
        loadData();
        loadDataTmp();
        
        JScrollPane scroll = new JScrollPane(tblData);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scroll);
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
        
        TabelUtils.setColumnWidths(tblDataTmp, new int[]{0,8}, new int[]{50,100});
        TabelUtils.setHeaderAlignment(tblDataTmp, new int[]{0,8}, new int[]{JLabel.CENTER,JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblDataTmp, new int[]{0}, JLabel.CENTER);
        
        tblDataTmp.putClientProperty(FlatClientProperties.STYLE, ""
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;");

    }

    private JPanel setPagination(){
        JPanel panel = new JPanel(new MigLayout("fillx, insets 5 10 5 10", "[grow][right]", "center"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(206,206,206)));
        
        lbItemsPerPage.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(100,100,100)");
        lblPageInfo.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(100,100,100)");
        
        JButton btnFirstPage = new JButton();
        btnFirstPage.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "first_page.svg",0.4f));
        btnFirstPage.putClientProperty(FlatClientProperties.STYLE, "arc:10;borderWidth:0;foreground:rgb(206,206,206)");
        btnFirstPage.addActionListener((e) -> {
            pagination.firstPage();
            loadData();
        });
        
        JButton btnBefore = new JButton();
        btnBefore.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "before.svg",0.4f));
        btnBefore.putClientProperty(FlatClientProperties.STYLE, "arc:10;borderWidth:0");
        btnBefore.addActionListener((e) -> {
            pagination.previousPage();
            loadData();
        });
        
        JButton btnNext = new JButton();
        btnNext.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "next.svg",0.4f));
        btnNext.putClientProperty(FlatClientProperties.STYLE, "arc:10;borderWidth:0");
        btnNext.addActionListener((e) -> {
            pagination.nextPage();
            loadData();
        });
        
        JButton btnLastPage = new JButton();
        btnLastPage.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "last_page.svg",0.4f));
        btnLastPage.putClientProperty(FlatClientProperties.STYLE, "arc:10;borderWidth:0");
        btnLastPage.addActionListener((e) -> {
            pagination.lastPage();
            loadData();
        });
        
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

        int totalItems = controllerOpname.getTotalItems(isDeleted, keyword);
        pagination.setTotalItems(totalItems);

        List<StokOpname> list;
        if (isSearch) {
            list = controllerOpname.searchData(keyword, isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
        } else {
            list = controllerOpname.getData(isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
        }

        tblModel.setData(list);
        int offset = (pagination.getCurrentPage() - 1) * pagination.getItemsPerPage();
        tblModel.setRowOffset(offset);
        tblData.setModel(tblModel);
        tblData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tblData.getSelectedRow();
                if (row != -1) {
                    btnAddStokOpname.setText("Continue");
                    btnCancelView.setVisible(true);
                }
            }
        });
        
        int start = offset + 1;
        int end = Math.min(pagination.getCurrentPage() * pagination.getItemsPerPage(), totalItems);

        lbItemsPerPage.setText(totalItems > 0 ? start + " - " + end + " of " + totalItems : "0 - 0 of 0");
        lblPageInfo.setText(pagination.getCurrentPage() + " of " + pagination.getTotalPages());
        
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "view");
        setVisibleButton();
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

    /* ===================== ADD ===================== */
    private JPanel setInfoAdd() {
        JPanel panel = new JPanel(new MigLayout("fill","[][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JLabel imageLogo = new JLabel();
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "stock_opname.svg", 0.4f));
        
        lbTItle.setText("Tambah Stok Opname");
        lbBreadcrumb.setText("TRANSAKSI > Tambah Stok Opname");
        
        lbTItle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        lbBreadcrumb.putClientProperty(FlatClientProperties.STYLE, "font:14;foreground:rgb(120,120,120)");
        
        panel.add(imageLogo);
        panel.add(lbTItle);
        panel.add(lbBreadcrumb);
        return panel;
    }
    
    private JPanel setButtonAdd() {
        JPanel panel = new JPanel(new MigLayout("wrap","[][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
        btnSave.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "save_white.svg",0.8f));
        btnSave.setIconTextGap(5);
        btnSave.addActionListener((e) -> {
            if(btnSave.getText().equals("Save")){
                insertDataStokOpname();
            }else{
                updateDataStokOpname();
            }
        });
        
        btnCancelAdd.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "cancel.svg",0.4f));
        btnCancelAdd.setIconTextGap(5);
        btnCancelAdd.addActionListener((e) -> {
            loadData();
            setVisibleButton();
        });
        
        panel.add(btnSave,"hmin 30, wmin 90");
        panel.add(btnCancelAdd,"hmin 30, wmin 50");
        
        return panel;
    }
    
    private JPanel setFormInput() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5", "fill", "[fill] [grow]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");

        JPanel panelUser = new JPanel(new MigLayout("fill, insets 10","[50][150][50][150][50][fill, grow][50][fill, grow]"));
        panelUser.setBorder(BorderFactory.createLineBorder(new Color(206,206,206)));
        panelUser.setOpaque(false);

        txtTanggalOpname = new JFormattedTextField();
        txtTanggalOpname.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255);foreground:rgb(0,0,0)");
        
        pickerTanggalOpname = new DatePicker() {
            @Override
            public void showPopup() {
                // kosong untuk disabled pop up
            }
        };
        pickerTanggalOpname.setCloseAfterSelected(true);
        pickerTanggalOpname.setDateFormat("yyyy-MM-dd");
        pickerTanggalOpname.setSelectedDate(LocalDate.now());
        pickerTanggalOpname.setEditor(txtTanggalOpname);
                
        JTextField txtPetugas = new JTextField();
        txtPetugas.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Petugas");
        txtPetugas.setText(loggedInUser.getName());
        txtPetugas.setEditable(false);
        
        txtKeterangan = new JTextField();
        txtKeterangan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Keterangan");
        
        cbxStatusOpname = new JComboBox();
        cbxStatusOpname.addItem("Pilih Status");
        cbxStatusOpname.addItem("Draft");
        cbxStatusOpname.addItem("Final");
        
        panelUser.add(new JLabel("Tanggal"));
        panelUser.add(txtTanggalOpname,"grow");
        panelUser.add(new JLabel("Petugas"));
        panelUser.add(txtPetugas,"grow");
        panelUser.add(new JLabel("Keterangan"));
        panelUser.add(txtKeterangan);
        panelUser.add(new JLabel("Status"));
        panelUser.add(cbxStatusOpname);
        
        JPanel panelBarang = new JPanel(new MigLayout(
            "fillx, insets 10, wrap 7",
            "[170:200,grow][200:200,grow][100:200,grow][80:200,grow][100:200,grow]",
            "[][][][][fill,grow][]"
        ));
        panelBarang.setBorder(BorderFactory.createTitledBorder("Barang"));
        panelBarang.setOpaque(false);

        // === Header ===
        panelBarang.add(new JLabel("Barcode"),    "left");
        panelBarang.add(new JLabel("Barang"),       "left");
        panelBarang.add(new JLabel("Satuan"),     "left");
        panelBarang.add(new JLabel("Stok Sistem"),"left");
        panelBarang.add(new JLabel("Stok Fisik"), "left");
        panelBarang.add(new JLabel("Selisih"),    "left");
        panelBarang.add(new JLabel("Catatan"),    "left");
        
        // === Input Fields ===
        txtBarcode       = new JTextField();
        txtBarcode.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Barcode");
        txtBarcode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String barcode = txtBarcode.getText().trim();
                if (!barcode.isEmpty()) {
                    findBarangByBarcode(barcode);
                }
            }
        });

        
        txtBarangName  = new JTextField();
        txtBarangName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nama Barang");
        
        txtSatuan  = new JTextField();
        txtSatuan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Satuan");
        
        txtStokSistem = new JTextField(10);
        txtStokSistem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Stok Sistem");
        ((AbstractDocument) txtStokSistem.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        txtStokFisik = new JTextField(10);
        txtStokFisik.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Stok Fisik");
        ((AbstractDocument) txtStokFisik.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        txtSelisih = new JTextField(10);
        txtSelisih.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selisih");
        ((AbstractDocument) txtSelisih.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        txtCatatan  = new JTextField();
        txtCatatan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Catatan");
        
        txtStokFisik.addActionListener((e) -> {
            int stokSistem     = NumericDocumentFilter.getNumericValueAsInt(txtStokSistem);
            int stokFisik   = NumericDocumentFilter.getNumericValueAsInt(txtStokFisik);
            int selisih      = stokFisik - stokSistem;
            txtSelisih.setText(String.valueOf(selisih));
            txtCatatan.requestFocus();
        });
        
        JButton btnSearch  = new JButton();
        btnSearch.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "browse.svg",0.4f));
        btnSearch.addActionListener(e -> {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            
            DataBarang dialog = new DataBarang(parent, "Data Barang", false,null);
            dialog.setVisible(true);
            setData(dialog);
        });

        // === Barcode panel (Text + Button) ===
        JPanel barcodePanel = new JPanel(new MigLayout("insets 0, fillx", "[grow][30!]"));
        barcodePanel.setOpaque(false);
        barcodePanel.add(txtBarcode, "growx");
        barcodePanel.add(btnSearch, "growy");

        // === Add to main panel ===
        panelBarang.add(barcodePanel,     "growx");
        panelBarang.add(txtBarangName,  "growx");
        panelBarang.add(txtSatuan,          "growx");
        panelBarang.add(txtStokSistem,   "growx");
        panelBarang.add(txtStokFisik, "growx");
        panelBarang.add(txtSelisih,   "growx");
        panelBarang.add(txtCatatan,          "growx");
        
        btnAdd = new JButton("Add");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);"
                + "disabledBackground:@accentColor;"
                + "disabledText:rgb(255,255,255)");
        btnAdd.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "add_white.svg",0.8f));
        btnAdd.setIconTextGap(5);
        btnAdd.addActionListener((e) -> {
            if(btnAdd.getText().equals("Add")){
                insertDataTmp();
            }else{
                updateDataTmp();
            }
        });
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "cancel.svg",0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.addActionListener((e) -> {
            resetFormTmp();
        });
        
        JPanel separator = new JPanel(new MigLayout());
        separator.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");
        panelBarang.add(separator, "span 7, growx, h 1px!, gapy 10, wrap");
        
        panelBarang.add(btnAdd,    "split 2, hmin 30, wmin 100, gapy 10");
        panelBarang.add(btnCancel, "hmin 30, wmin 100, wrap");
        
        tblDataTmp.setModel(tblModelTmp);
        tblDataTmp.setFocusable(false);
        tblDataTmp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tblDataTmp.getSelectedRow();
                if (row != -1) {
                    getDataTmp();
                }
            }
        });
        
        ColumnDeleteOpnameTmp.install(tblDataTmp, 8,() -> {
            resetFormTmp();
            loadDataTmp();
        });
        
        JScrollPane scroll = new JScrollPane(tblDataTmp);
        panelBarang.add(scroll,"span 7, grow, gapy 10");
        
        panel.add(panelUser, "wrap");
        panel.add(panelBarang, "span 4, grow, push"); 

        disabledTextfield();
        return panel;
    }

    /* ===================== Code ===================== */
    private void setDateUser(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        txtTanggalOpname.setText(df.format(new Date()));
        txtTanggalOpname.setEditable(false);
    }
    
    private void setData(DataBarang dataBarang) {
        if(dataBarang != null){
            dataBarang.setSelectionListener((inventory) -> {
                selectedBarang = inventory;
                txtBarcode.setText(inventory.getBarcode());
                txtBarangName.setText(inventory.getBarangName());
                txtSatuan.setText(inventory.getSatuan().getSatuanName());
                txtStokSistem.setText(String.valueOf(inventory.getTotalStok()));
                txtStokFisik.requestFocus();
            });
        }
    }
    
    private void findBarangByBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            return;
        }

        Barang barang = (Barang) controllerBarang.getDataByBarcode(barcode.trim());

        if (barang != null) {
            selectedBarang = barang;
            txtBarcode.setText(barang.getBarcode());
            txtBarangName.setText(barang.getBarangName());
            txtSatuan.setText(barang.getSatuan().getSatuanName());
            txtStokSistem.setText(String.valueOf(barang.getTotalStok()));
            txtStokFisik.requestFocus();
        } else {
            Toast.show(this, Toast.Type.WARNING, "Barang dengan barcode " + barcode + " tidak ditemukan", getOptionAlert());
            resetFormTmp();
        }
    }
    
    private void loadDataTmp() {
        List<StokOpnameTmp> list = controllerOpnameTmp.getData();
        tblModelTmp.setData(list);
        tblDataTmp.setModel(tblModelTmp);
    }
    
    private boolean inputValidationTmp(){
        boolean valid = false;
        
        if(selectedBarang == null){
            Toast.show(this, Toast.Type.INFO, "Barang tidak boleh kosong", getOptionAlert());
        }else if(txtStokSistem.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Stok sistem tidak boleh kosong", getOptionAlert());
        }else if(txtStokFisik.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Stok fisik tidak boleh kosong", getOptionAlert());
        }else if(txtSelisih.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Selisih tidak boleh kosong", getOptionAlert());
        }else if(txtCatatan.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Catatan tidak boleh kosong", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private void setActionButton(){
        txtCatatan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if(btnAdd.getText().equals("Add")){
                        insertDataTmp();
                    }else{
                        updateDataTmp();
                    }
                }
            }
        });
    }
    
    private void insertDataTmp(){
        if(inputValidationTmp()){
            int stokSistem     = NumericDocumentFilter.getNumericValueAsInt(txtStokSistem);
            int stokFisik   = NumericDocumentFilter.getNumericValueAsInt(txtStokFisik);
            int selisih      = NumericDocumentFilter.getNumericValueAsInt(txtSelisih);
            String catatan         = txtCatatan.getText();
            String barcode      = txtBarcode.getText();
            
            boolean exists = controllerOpnameTmp.getData()
                .stream()
                .anyMatch(tmp -> tmp.getBarang().getBarcode().equals(barcode));

            if (exists) {
                Toast.show(this, Toast.Type.WARNING, "Data dengan barcode " + barcode + " sudah ada!", getOptionAlert());
                resetFormTmp();
                return;
            }
            
            StokOpnameTmp modelTmp = new StokOpnameTmp();
            modelTmp.setBarang(selectedBarang);
            modelTmp.setStokSistem(stokSistem);
            modelTmp.setStokFisik(stokFisik);
            modelTmp.setSelisih(selisih);
            modelTmp.setCatatan(catatan);
            
            controllerOpnameTmp.insertData(modelTmp);
            loadDataTmp();
            resetFormTmp();
        }
    }

    private void getDataTmp() {
        btnAdd.setText("Update");
        int row = tblDataTmp.getSelectedRow();
        if (row != -1) {
            StokOpnameTmp selected = tblModelTmp.getData(row);

            opnameTmpID = selected.getOpnameTmpID();
            selectedBarang = selected.getBarang();
            txtBarcode.setText(selected.getBarang().getBarcode());
            txtBarangName.setText(selected.getBarang().getBarangName());
            txtSatuan.setText(selected.getBarang().getSatuan().getSatuanName());
            
            txtStokSistem.setText(String.valueOf(selected.getStokSistem()));
            txtStokFisik.setText(String.valueOf(selected.getStokFisik()));
            txtSelisih.setText(String.valueOf(selected.getSelisih()));
            
            txtCatatan.setText(selected.getCatatan());
        }
    }

    private void updateDataTmp(){
        if(inputValidationTmp()){
            int stokSistem     = NumericDocumentFilter.getNumericValueAsInt(txtStokSistem);
            int stokFisik   = NumericDocumentFilter.getNumericValueAsInt(txtStokFisik);
            int selisih      = NumericDocumentFilter.getNumericValueAsInt(txtSelisih);
            String catatan         = txtCatatan.getText();
            
            StokOpnameTmp modelTmp = new StokOpnameTmp();
            modelTmp.setBarang(selectedBarang);
            modelTmp.setStokSistem(stokSistem);
            modelTmp.setStokFisik(stokFisik);
            modelTmp.setSelisih(selisih);
            modelTmp.setCatatan(catatan);
            modelTmp.setOpnameTmpID(opnameTmpID);
            
            controllerOpnameTmp.updateData(modelTmp);
            loadDataTmp();
            resetFormTmp();
            btnAdd.setText("Add");
        }
    }
    
    private void resetFormTmp() {
        btnAdd.setText("Add");
        txtBarcode.requestFocus();
        
        selectedBarang = null;
        txtBarcode.setText("");
        txtBarangName.setText("");
        txtSatuan.setText("");
        txtStokSistem.setText("");
        txtStokFisik.setText("");
        txtSelisih.setText("");
        txtCatatan.setText("");
    }
    
    private void disabledTextfield(){
        txtBarangName.setEditable(false);
        txtSatuan.setEditable(false);
        txtStokSistem.setEditable(false);
        txtSelisih.setEditable(false);
    }
    
    private boolean inputValidationStokOpname(){
        boolean valid = false;
        
        if(pickerTanggalOpname.getSelectedDate() == null){
            Toast.show(this, Toast.Type.INFO, "Tanggal Opname tidak boleh kosong", getOptionAlert());
        }else if(txtKeterangan.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Keterangan tidak boleh kosong", getOptionAlert());
        }else if(cbxStatusOpname.getSelectedIndex() == 0){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih status opname", getOptionAlert());
        }else if(loggedInUser == null || loggedInUser.getUserID() == 0){
            Toast.show(this, Toast.Type.INFO, "User tidak boleh kosong", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private void insertDataStokOpname(){
        if(inputValidationStokOpname()){
            String tanggalOpname  = pickerTanggalOpname.getSelectedDate().toString();
            String keterangan      = txtKeterangan.getText();
            String statusOpname     = cbxStatusOpname.getSelectedItem().toString();
                        
            StokOpname modelStokOpname = new StokOpname();
            modelStokOpname.setTanggalOpname(tanggalOpname);
            modelStokOpname.setKeterangan(keterangan);
            modelStokOpname.setStatus(statusOpname);
            modelStokOpname.setUser(loggedInUser);
            modelStokOpname.setInsertBy(loggedInUser.getUserID());
            
            controllerOpname.insertData(modelStokOpname);
            controllerOpnameDetail.insertData(modelStokOpname.getOpnameID());
            
            controllerOpname.applyStockAdjustment(modelStokOpname.getOpnameID());
            
            controllerOpnameTmp.clearTmp();
            
            Toast.show(this, Toast.Type.SUCCESS, "Data berhasil disimpan", getOptionAlert());
            loadData();
            resetForm();
            FormManager.getMainForm().checkStock();
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "view");
        }
    }
    
    private void resetForm() {
        pickerTanggalOpname.clearSelectedDate();
        txtKeterangan.setText("");
        cbxStatusOpname.setSelectedIndex(0);
    }

    private void deleteData() {
        int row = tblData.getSelectedRow();
        if(row != -1){
            StokOpname model = tblModel.getData(row);
            
            String message = "Apakah Anda yakin ingin menghapus data ini?";
            String title = "Konfirmasi";
            
            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this,
                    new MessageModal(
                            MessageModal.Type.INFO, 
                            message, 
                            title, 
                            ModalBorder.YES_NO_OPTION, 
                            (controller, action) -> {
                                if(action == ModalBorder.YES_OPTION){
                                    StokOpname modelStokOpname = new StokOpname();
                                    modelStokOpname.setDeleteBy(loggedInUser.getUserID());
                                    modelStokOpname.setOpnameID(model.getOpnameID());
                                    controllerOpname.deleteData(modelStokOpname);
                                    controllerOpname.rollbackStockOpname(model.getOpnameID());
                                    
                                    Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully deleted", getOptionAlert());
                                    loadData();
                                    FormManager.getMainForm().checkStock();
                                }
                            }),option);
        }else{
            Toast.show(this, Toast.Type.INFO, "Please select the data you want to delete", getOptionAlert());
        }
    }
    
    private void restoreData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            StokOpname model = tblModel.getData(row);
            
            String message = "Apakah Anda yakin ingin merestore data ini?";
            String title = "Konfirmasi";

            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, new MessageModal(MessageModal.Type.INFO, message, title, ModalBorder.YES_NO_OPTION, 
                (controller, action) -> {
                    if (action == ModalBorder.YES_OPTION) {
                        controllerOpname.restoreData(model.getOpnameID());
                        controllerOpname.applyStockAdjustment(model.getOpnameID());
                        
                        Toast.show(this, Toast.Type.SUCCESS, "Data berhasil direstore", getOptionAlert());
                        loadData();
                        FormManager.getMainForm().checkStock();
                    }
                }),option);
        } else {
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data yang ingin direstore", getOptionAlert());
        }
    }

    private void showStokOpnameDetail() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            StokOpname model = tblModel.getData(row);
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            DataOpnameDetail dialog = new DataOpnameDetail(parent, "Data Detail Stok Opname", false, model);
            dialog.setVisible(true);
            setVisibleButton();
        }else{
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data untuk melihat detail stok opname", getOptionAlert());
        }
    }

    private void loadDetailToTmp() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "add");
            btnCancelAdd.setVisible(false);
            
            StokOpname model = tblModel.getData(row);
            
            opnameID = model.getOpnameID();
            pickerTanggalOpname.setSelectedDate(LocalDate.parse(model.getTanggalOpname()));
            txtKeterangan.setText(model.getKeterangan());
            cbxStatusOpname.setSelectedItem(model.getStatus());
            
            controllerOpname.rollbackStockOpname(opnameID);
            controllerOpnameTmp.loadDetailToTmp(opnameID);
            controllerOpnameDetail.deleteData(opnameID);
            
            loadDataTmp();
            btnSave.setText("Update");
            lbTItle.setText("Perbarui Stok Opname");
            lbBreadcrumb.setText("TRANSAKSI > Perbarui Stok Opname");
        }
    }

    private void updateDataStokOpname() {
        if(inputValidationStokOpname()){
            String tanggalOpname  = pickerTanggalOpname.getSelectedDate().toString();
            String keterangan      = txtKeterangan.getText();
            String statusOpname     = cbxStatusOpname.getSelectedItem().toString();
                        
            StokOpname modelStokOpname = new StokOpname();
            modelStokOpname.setTanggalOpname(tanggalOpname);
            modelStokOpname.setKeterangan(keterangan);
            modelStokOpname.setStatus(statusOpname);
            modelStokOpname.setUser(loggedInUser);
            modelStokOpname.setUpdateBy(loggedInUser.getUserID());
            modelStokOpname.setOpnameID(opnameID);
            
            controllerOpname.updateData(modelStokOpname);
            controllerOpnameDetail.insertData(opnameID);
            
            controllerOpname.applyStockAdjustment(opnameID);
            
            controllerOpnameTmp.clearTmp();
            
            Toast.show(this, Toast.Type.SUCCESS, "Data berhasil diperbarui", getOptionAlert());
            loadData();
            resetForm();
            setVisibleButton();
            FormManager.getMainForm().checkStock();
            lbTItle.setText("Tambah Stok Opname");
            lbBreadcrumb.setText("TRANSAKSI > Tambah Stok Opname");
        }
    }

    private void setVisibleButton() {
        btnAddStokOpname.setText("Add");
        btnSave.setText("Save");
        btnCancelView.setVisible(false);
        btnCancelAdd.setVisible(true);
    }
}
