package com.inventory.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.BarangController;
import com.inventory.controller.BarangMasukController;
import com.inventory.controller.BarangMasukDetailController;
import com.inventory.controller.BarangMasukTmpController;
import com.inventory.controller.GudangController;
import com.inventory.controller.RakController;
import com.inventory.controller.ReportController;
import com.inventory.controller.StokGudangController;
import com.inventory.controller.SupplierController;
import com.inventory.form.input.DataBarang;
import com.inventory.form.input.DataBarangMasukDetail;
import com.inventory.main.Form;
import com.inventory.main.FormManager;
import com.inventory.model.Barang;
import com.inventory.model.BarangMasuk;
import com.inventory.model.BarangMasukDetail;
import com.inventory.model.BarangMasukTmp;
import com.inventory.model.Gudang;
import com.inventory.model.Pagination;
import com.inventory.model.Rak;
import com.inventory.model.StatusItem;
import com.inventory.model.StokGudang;
import com.inventory.model.Supplier;
import com.inventory.model.User;
import com.inventory.tablemodel.TabModBarangMasuk;
import com.inventory.tablemodel.TabModBarangMasukTmp;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.ColumnDeleteBarangMasukTmp;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
public class FormBarangMasuk extends Form{
    private static final long serialVersionUID = 1L;


    private JPanel mainPanel, panelView, panelAdd;
    
    private final BarangMasukController controllerMasuk = new BarangMasukController();
    private final BarangMasukTmpController controllerMasukTmp = new BarangMasukTmpController();
    private final BarangMasukDetailController controllerMasukDetail = new BarangMasukDetailController();
    
    private final BarangController controllerBarang = new BarangController();
    private final StokGudangController controllerStok = new StokGudangController();
    private final RakController controlleRak = new RakController();
    private final GudangController controllerGudang = new GudangController();
    private final SupplierController controllerSupplier = new SupplierController();
    private final ReportController controller = new ReportController();
    
    private final TabModBarangMasuk tblModel = new TabModBarangMasuk();
    private final TabModBarangMasukTmp tblModelTmp = new TabModBarangMasukTmp();
    
    private final JTable tblData = new JTable();
    private final JTable tblDataTmp = new JTable();
    
    private JButton btnRestore, btnAdd, btnAddMasuk;
    
    private JTextField txtSearch, txtNoTransaksi, txtBarcode, txtNoNota, txtNamaBarang, txtSatuan, txtJumlah, txtHargaBeli, txtAlamat;
    private JFormattedTextField txtTanggalMasuk;
    private DatePicker pickerTanggalMasuk;
    
    private JComboBox<StatusItem> cbxStatus;
    private JComboBox cbxRak, cbxGudang;
    private JComboBox cbxSupplier = new JComboBox<>();
    
    private JLabel lbSum = new JLabel();
    private JLabel lbTotal = new JLabel();
    
    private int barangMasukTmpID;
    private Barang selectedBarang;
    
    private Pagination pagination;
    private JLabel lbItemsPerPage = new JLabel("0 - 0 of 0");
    private JLabel lblPageInfo = new JLabel("1 of 1");
    
    private final User loggedInUser;
    
    public FormBarangMasuk() {
        this.loggedInUser = FormManager.getLoggedInUser();
        pagination = new Pagination(25);
        
        init();
        setTableProperties();
        configureRole();
        cbxStatus.setSelectedIndex(0);
    }
    
    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
        loadDataTmp();
        resetForm();
        resetFormTmp();
    }
    
    private void configureRole() {
        if(loggedInUser.getRole().equals("Admin")){
            btnRestore.setVisible(false);
            cbxStatus.setVisible(true);
        }else{
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
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "barang_masuk.svg", 0.4f));
        
        JLabel lbTItle = new JLabel("Data Barang Masuk");
        lbTItle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumb = new JLabel("TRANSAKSI > Barang Masuk");
        lbBreadcrumb.putClientProperty(FlatClientProperties.STYLE, "font:14;foreground:rgb(120,120,120)");
        
        panel.add(imageLogo);
        panel.add(lbTItle);
        panel.add(lbBreadcrumb);
        return panel;
    }
    
    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap","[][][][]push[][][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        btnAddMasuk = new JButton("Add");
        btnAddMasuk.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);"
                + "disabledBackground:@accentColor;"
                + "disabledText:rgb(255,255,255)");
        btnAddMasuk.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "add_white.svg",0.8f));
        btnAddMasuk.setIconTextGap(5);
        btnAddMasuk.addActionListener((e) -> {
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "add");
            txtBarcode.requestFocus();
            generateKodeMasuk();
            setDateUser();
            loadDataTmp();
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
            showBarangMasukDetail();
        });
        
        JButton btnPreview = new JButton("Preview");
        btnPreview.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "print.svg",0.4f));
        btnPreview.setIconTextGap(5);
        btnPreview.addActionListener((e) -> {
            previewData();
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
            handleStatusChange(btnAdd, btnDelete, btnDetail, btnPreview);
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
        
        panel.add(btnAddMasuk,"hmin 30, wmin 90");
        panel.add(btnDelete,"hmin 30, wmin 50");
        panel.add(btnDetail,"hmin 30, wmin 50");
        panel.add(btnPreview,"hmin 30, wmin 50");
        panel.add(txtSearch,"hmin 30, width 300, gapx 8");
        panel.add(btnRestore,"hmin 30, wmin 50, hidemode 3");
        panel.add(cbxStatus,"hmin 30, wmin 50, hidemode 3");
        
        return panel;
    }

    private void handleStatusChange(JButton btnAdd, JButton btnDelete, JButton btnDetail, JButton btnPreview) {
        StatusItem selected = (StatusItem) cbxStatus.getSelectedItem();
        if (selected != null) {
            boolean isDeleted = selected.getValue();
            btnRestore.setVisible(isDeleted);
            btnAdd.setEnabled(!isDeleted);
            btnDelete.setEnabled(!isDeleted);
            btnDetail.setEnabled(!isDeleted);
            btnPreview.setEnabled(!isDeleted);
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
        
        TabelUtils.setColumnWidths(tblDataTmp, new int[]{0,9}, new int[]{50,100});
        TabelUtils.setHeaderAlignment(tblDataTmp, new int[]{0,9}, new int[]{JLabel.CENTER,JLabel.CENTER}, JLabel.LEFT);
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

        int totalItems = controllerMasuk.getTotalItems(isDeleted, keyword);
        pagination.setTotalItems(totalItems);

        List<BarangMasuk> list;
        if (isSearch) {
            list = controllerMasuk.searchData(keyword, isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
        } else {
            list = controllerMasuk.getData(isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
        }

        tblModel.setData(list);
        int offset = (pagination.getCurrentPage() - 1) * pagination.getItemsPerPage();
        tblModel.setRowOffset(offset);
        tblData.setModel(tblModel);
        
        int start = offset + 1;
        int end = Math.min(pagination.getCurrentPage() * pagination.getItemsPerPage(), totalItems);

        lbItemsPerPage.setText(totalItems > 0 ? start + " - " + end + " of " + totalItems : "0 - 0 of 0");
        lblPageInfo.setText(pagination.getCurrentPage() + " of " + pagination.getTotalPages());
        
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "view");
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
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "barang_masuk.svg", 0.4f));
        
        JLabel lbTItle = new JLabel("Tambah Barang Masuk");
        lbTItle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumb = new JLabel("TRANSAKSI > Tambah Barang Masuk");      
        lbBreadcrumb.putClientProperty(FlatClientProperties.STYLE, "font:14;foreground:rgb(120,120,120)");
        
        panel.add(imageLogo);
        panel.add(lbTItle);
        panel.add(lbBreadcrumb);
        return panel;
    }
    
    private JPanel setButtonAdd() {
        JPanel panel = new JPanel(new MigLayout("wrap","[][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JButton btnSave = new JButton("Save");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
        btnSave.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "save_white.svg",0.8f));
        btnSave.setIconTextGap(5);
        btnSave.addActionListener((e) -> {
            saveBarangMasuk();
        });
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "cancel.svg",0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.addActionListener((e) -> {
            loadData();
        });
        
        panel.add(btnSave,"hmin 30, wmin 90");
        panel.add(btnCancel,"hmin 30, wmin 50");
        
        return panel;
    }
    
    private JPanel setFormInput() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5", "[150][200][100][fill, grow]", "[100!] [grow]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");

        JPanel panelFaktur = new JPanel(new MigLayout("fill, wrap 4","[50][fill, grow]20[50][fill, grow]"));
        panelFaktur.setBorder(BorderFactory.createLineBorder(new Color(206,206,206)));
        panelFaktur.setOpaque(false);

        txtNoTransaksi = new JTextField();
        txtNoTransaksi.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "No Faktur");
        txtNoTransaksi.setEditable(false);
        
        txtTanggalMasuk = new JFormattedTextField();
        txtTanggalMasuk.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255);foreground:rgb(0,0,0)");
        
        pickerTanggalMasuk = new DatePicker() {
            @Override
            public void showPopup() {
                // kosong untuk disabled pop up
            }
        };
        pickerTanggalMasuk.setCloseAfterSelected(true);
        pickerTanggalMasuk.setDateFormat("yyyy-MM-dd");
        pickerTanggalMasuk.setSelectedDate(LocalDate.now());
        pickerTanggalMasuk.setEditor(txtTanggalMasuk);
        txtTanggalMasuk.setEditable(false);        
        
        txtNoNota = new JTextField();
        txtNoNota.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "No Nota");
        
        JTextField txtPetugas = new JTextField();
        txtPetugas.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Petugas");
        txtPetugas.setText(loggedInUser.getName());
        txtPetugas.setEditable(false);
        
        panelFaktur.add(new JLabel("No Transaksi"));
        panelFaktur.add(txtNoTransaksi,"w 150!");
        panelFaktur.add(new JLabel("Tanggal"));
        panelFaktur.add(txtTanggalMasuk,"w 120!");
        panelFaktur.add(new JLabel("No Nota"));
        panelFaktur.add(txtNoNota,"w 150!");
        panelFaktur.add(new JLabel("Petugas"));
        panelFaktur.add(txtPetugas,"w 120!");
        
        JPanel panelSupplier = new JPanel(new MigLayout("fill, wrap 2","[50][fill, grow]"));
        panelSupplier.setBorder(BorderFactory.createLineBorder(new Color(206,206,206)));
        panelSupplier.setOpaque(false);
        
        txtAlamat = new JTextField();
        txtAlamat.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Address");
        txtAlamat.setEditable(false);
        loadSupplier();
        
        panelSupplier.add(new JLabel("Supplier"));
        panelSupplier.add(cbxSupplier,"w 200!");
        panelSupplier.add(new JLabel("Alamat"));
        panelSupplier.add(txtAlamat,"w 200!");
        
        JPanel panelSum = new JPanel(new MigLayout("fill","center"));
        panelSum.setBorder(BorderFactory.createTitledBorder("Jumlah"));
        panelSum.setOpaque(false);
        
        lbSum = new JLabel();
        lbSum.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +26");
        panelSum.add(lbSum);
        
        JPanel panelTotal = new JPanel(new MigLayout("fill","right"));
        panelTotal.setBorder(BorderFactory.createTitledBorder("Total"));
        panelTotal.setOpaque(false);
        
        lbTotal = new JLabel();
        lbTotal.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +26");
        panelTotal.add(lbTotal);
        
        JPanel panelBarang = new JPanel(new MigLayout(
            "fillx, insets 10, wrap 7",
            "[170:200,grow][200:200,grow][100:200,grow][80:200][100:200,grow][150:200,grow][150:200,grow]",
            "[][][][][fill,grow]"
        ));
        panelBarang.setBorder(BorderFactory.createTitledBorder("Barang"));
        panelBarang.setOpaque(false);

        // === Header ===
        panelBarang.add(new JLabel("Barcode"),         "left");
        panelBarang.add(new JLabel("Barang"),            "left");
        panelBarang.add(new JLabel("Satuan"),            "left");
        panelBarang.add(new JLabel("Jumlah"),          "left");
        panelBarang.add(new JLabel("Harga Beli"),      "left");
        panelBarang.add(new JLabel("Rak"),          "left");
        panelBarang.add(new JLabel("Gudang"),          "left");

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

        
        txtNamaBarang  = new JTextField();
        txtNamaBarang.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nama Barang");
        
        txtSatuan  = new JTextField();
        txtSatuan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Satuan");
        
        txtJumlah = new JTextField(10);
        txtJumlah.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Jumlah");
        ((AbstractDocument) txtJumlah.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        txtHargaBeli = new JTextField(15);
        txtHargaBeli.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Harga Beli");
        ((AbstractDocument) txtHargaBeli.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        cbxRak = new JComboBox<>();
        loadRak();
        
        cbxGudang = new JComboBox<>();
        loadGudang();
        
        JButton btnSearch  = new JButton();
        btnSearch.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "browse.svg",0.4f));
        btnSearch.addActionListener(e -> {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            
            Supplier selectedSupplier   = (Supplier) cbxSupplier.getSelectedItem();
            if (selectedSupplier == null || selectedSupplier.getSupplierID() == 0) {
                Toast.show(this, Toast.Type.INFO, "Please select a supplier", getOptionAlert());
                cbxSupplier.putClientProperty("JComponent.outline", "error");
                return;
            }
            
            DataBarang dialog = new DataBarang(parent, "Data Barang", false, selectedSupplier);
            dialog.setVisible(true);
            setData(dialog);
            cbxSupplier.putClientProperty("JComponent.outline", null);
        });

        // === Barcode panel (Text + Button) ===
        JPanel barcodePanel = new JPanel(new MigLayout("insets 0, fillx", "[grow][30!]"));
        barcodePanel.setOpaque(false);
        barcodePanel.add(txtBarcode, "growx");
        barcodePanel.add(btnSearch, "growy");

        // === Add to main panel ===
        panelBarang.add(barcodePanel,   "growx");
        panelBarang.add(txtNamaBarang,  "growx");
        panelBarang.add(txtSatuan,      "growx");
        panelBarang.add(txtJumlah,      "growx");
        panelBarang.add(txtHargaBeli,   "growx");
        panelBarang.add(cbxRak,         "growx");
        panelBarang.add(cbxGudang,      "growx, wrap");

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
        
        ColumnDeleteBarangMasukTmp.install(tblDataTmp, 9,() -> {
            resetFormTmp();
            loadDataTmp();
        });
        
        JScrollPane scroll = new JScrollPane(tblDataTmp);
        panelBarang.add(scroll,"span 8, grow, gapy 10");
        
        panel.add(panelFaktur, "growx, h 100!");
        panel.add(panelSupplier, "growx, h 100!");
        panel.add(panelSum, "growx, h 100!");
        panel.add(panelTotal, "growx, h 100!, wrap");
        
        panel.add(panelBarang, "span 4, grow, push"); 

        disabledTextfield();
        return panel;
    }

    /* ===================== Code ===================== */
    private void generateKodeMasuk(){
        txtNoTransaksi.setText(controllerMasuk.generateBarangMasukCode());
    }
    
    private void setDateUser(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        txtTanggalMasuk.setText(df.format(new Date()));
    }
    
    private void loadSupplier() {
        cbxSupplier.removeAllItems();
        cbxSupplier.addItem(new Supplier(0, "Pilih Supplier"));
        List<Supplier> sup = controllerSupplier.getData(false, Integer.MAX_VALUE,1);
        for (Supplier s : sup) {
            cbxSupplier.addItem(s);
        }
        
        cbxSupplier.addActionListener(e -> {
            Supplier selected = (Supplier) cbxSupplier.getSelectedItem();
            if (selected != null && selected.getSupplierID() != 0) {
                txtAlamat.setText(selected.getAlamat());
            } else {
                txtAlamat.setText("");
            }
        });
    }
    
    private void loadRak() {
        cbxRak.removeAllItems();
        cbxRak.addItem(new Rak(0, "Pilih Rak"));
        List<Rak> rak = controlleRak.getData(false, Integer.MAX_VALUE,1);
        for (Rak r : rak) {
            cbxRak.addItem(r);
        }
    }
    
    private void loadGudang() {
        cbxGudang.removeAllItems();
        cbxGudang.addItem(new Gudang(0, "Pilih Gudang"));
        List<Gudang> gud = controllerGudang.getData(false, Integer.MAX_VALUE,1);
        for (Gudang g : gud) {
            cbxGudang.addItem(g);
        }
    }

    private void setData(DataBarang dataBarang) {
        if(dataBarang != null){
            dataBarang.setSelectionListener((barang) -> {
                selectedBarang = barang;
                txtBarcode.setText(barang.getBarcode());
                txtNamaBarang.setText(barang.getBarangName());
                txtSatuan.setText(barang.getSatuan().getSatuanName());
                
                txtJumlah.requestFocus();
            });
        }
    }
    
    private void findBarangByBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            return;
        }

        Barang barang = controllerBarang.getDataByBarcode(barcode.trim());

        if (barang != null) {
            selectedBarang = barang;
            txtBarcode.setText(barang.getBarcode());
            txtNamaBarang.setText(barang.getBarangName());
            txtSatuan.setText(barang.getSatuan().getSatuanName());
            txtJumlah.requestFocus();
        } else {
            Toast.show(this, Toast.Type.WARNING, "Barang dengan barcode " + barcode + " tidak ditemukan", getOptionAlert());
            resetFormTmp();
        }
    }
    
    private void loadDataTmp() {
        List<BarangMasukTmp> list = controllerMasukTmp.getData();
        tblModelTmp.setData(list);
        tblDataTmp.setModel(tblModelTmp);
        
        int totalQty = controllerMasukTmp.sumJumlah();
        double totalBarangMasuk = controllerMasukTmp.sumTotalBeli();

        lbSum.setText(String.valueOf(totalQty));

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setCurrencySymbol("Rp.");
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat df = new DecimalFormat("¤ #,###.00", symbols); // ¤ = currency symbol
        lbTotal.setText(df.format(totalBarangMasuk));
        
        if (list != null && !list.isEmpty()) {
            cbxSupplier.setEnabled(false);
        } else {
            cbxSupplier.setEnabled(true);
        }
    }
    
    private boolean inputValidationTmp(){
        boolean valid = false;
        
        Rak selectedRak         = (Rak) cbxRak.getSelectedItem();
        Gudang selectedGudang   = (Gudang) cbxGudang.getSelectedItem();
        
        if(selectedBarang == null){
            Toast.show(this, Toast.Type.INFO, "Barang tidak boleh kosong", getOptionAlert());
        }else if(txtJumlah.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Jumlah tidak boleh kosong", getOptionAlert());
        }else if(txtHargaBeli.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Harga beli tidak boleh kosong", getOptionAlert());
        }else if(selectedRak == null || selectedRak.getRakID() == 0){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih rak", getOptionAlert());
        }else if(selectedGudang == null || selectedGudang.getGudangID()== 0){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih gudang", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private void insertDataTmp(){
        if(inputValidationTmp()){
            int jumlah          = NumericDocumentFilter.getNumericValueAsInt(txtJumlah);
            double hargaBeli    = NumericDocumentFilter.getNumericValue(txtHargaBeli);
            String barcode      = txtBarcode.getText();
            
            boolean exists = controllerMasukTmp.getData()
                .stream()
                .anyMatch(tmp -> tmp.getBarang().getBarcode().equals(barcode));

            if (exists) {
                Toast.show(this, Toast.Type.WARNING, "Barang dengan barcode " + barcode + " sudah ada!", getOptionAlert());
                resetFormTmp();
                return;
            }
            
            BarangMasukTmp modelTmp = new BarangMasukTmp();
            modelTmp.setBarang(selectedBarang);
            modelTmp.setJumlah(jumlah);
            modelTmp.setHargaBeli(hargaBeli);
            modelTmp.setRak((Rak) cbxRak.getSelectedItem());
            modelTmp.setGudang((Gudang) cbxGudang.getSelectedItem());
            
            controllerMasukTmp.insertData(modelTmp);
            loadDataTmp();
            resetFormTmp();
        }
    }

    private void getDataTmp() {
        btnAdd.setText("Update");
        int row = tblDataTmp.getSelectedRow();
        if (row != -1) {
            BarangMasukTmp selected = tblModelTmp.getData(row);

            barangMasukTmpID = selected.getBarangMasukTmpID();
            System.out.println("ID " + selected.getBarangMasukTmpID());
            selectedBarang = selected.getBarang();
            txtBarcode.setText(selected.getBarang().getBarcode());
            txtNamaBarang.setText(selected.getBarang().getBarangName());
            txtSatuan.setText(selected.getBarang().getSatuan().getSatuanName());
            
            txtJumlah.setText(String.valueOf(selected.getJumlah()));
            
            double hargaBeli = selected.getHargaBeli();
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
            DecimalFormat df = new DecimalFormat("#,###", symbols); 
            txtHargaBeli.setText(df.format(hargaBeli));
            
            cbxRak.setSelectedItem(selected.getRak());
            cbxGudang.setSelectedItem(selected.getGudang());
        }
    }

    private void updateDataTmp(){
        if(inputValidationTmp()){
            int jumlah          = NumericDocumentFilter.getNumericValueAsInt(txtJumlah);
            double hargaBeli    = NumericDocumentFilter.getNumericValue(txtHargaBeli);
            
            BarangMasukTmp modelTmp = new BarangMasukTmp();
            modelTmp.setBarang(selectedBarang);
            modelTmp.setJumlah(jumlah);
            modelTmp.setHargaBeli(hargaBeli);
            modelTmp.setRak((Rak) cbxRak.getSelectedItem());
            modelTmp.setGudang((Gudang) cbxGudang.getSelectedItem());
            modelTmp.setBarangMasukTmpID(barangMasukTmpID);
            
            controllerMasukTmp.updateData(modelTmp);
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
        txtNamaBarang.setText("");
        txtSatuan.setText("");
        txtJumlah.setText("");
        txtHargaBeli.setText("");
        cbxRak.setSelectedIndex(0);
        cbxGudang.setSelectedIndex(0);
    }
    
    private void disabledTextfield(){
        txtNamaBarang.setEditable(false);
        txtSatuan.setEditable(false);
    }
    
    private void resetForm() {
        txtNoTransaksi.setText("");
        txtNoNota.setText("");
        lbSum.setText("0");
        lbTotal.setText("0.0");
        cbxSupplier.setSelectedIndex(0);
        txtAlamat.setText("");
    }

    private void deleteData() {
        int row = tblData.getSelectedRow();
        if(row != -1){
            BarangMasuk model = tblModel.getData(row);
            
            String message = "Apakah Anda yakin ingin menghapus data ini ?";
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
                                    BarangMasuk modelBarangMasuk = new BarangMasuk();
                                    modelBarangMasuk.setDeleteBy(loggedInUser.getUserID());
                                    modelBarangMasuk.setBarangMasukID(model.getBarangMasukID());
                                    controllerMasuk.deleteData(modelBarangMasuk);
                                    controllerStok.deleteData(modelBarangMasuk);
                                    
                                    Toast.show(this, Toast.Type.SUCCESS, "Data berhasil dihapus", getOptionAlert());
                                    loadData();
                                    FormManager.getMainForm().checkStock();
                                }
                            }),option);
        }else{
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data yang ingin dihapus", getOptionAlert());
        }
    }
    
    private void restoreData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            BarangMasuk model = tblModel.getData(row);
            
            String message = "Apakah Anda yakin ingin merestore data ini?";
            String title = "Konfirmasi";

            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, new MessageModal(MessageModal.Type.INFO, message, title, ModalBorder.YES_NO_OPTION, 
                (controller, action) -> {
                    if (action == ModalBorder.YES_OPTION) {
                        controllerMasuk.restoreData(model.getBarangMasukID());
                        controllerStok.restoreData(model.getBarangMasukID());
                        Toast.show(this, Toast.Type.SUCCESS, "Data berhasil direstore", getOptionAlert());
                        loadData();
                        FormManager.getMainForm().checkStock();
                    }
                }),option);
        } else {
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data yang ingin direstore", getOptionAlert());
        }
    }

    private void showBarangMasukDetail() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            BarangMasuk model = tblModel.getData(row);
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            DataBarangMasukDetail dialog = new DataBarangMasukDetail(parent, "Data Detail Barang Masuk", false, model);
            dialog.setVisible(true);
        }else{
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data untuk melihat detail barang masuk", getOptionAlert());
        }
    }

    private void previewData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            BarangMasuk model = tblModel.getData(row);
            try {
                controller.previewBarangMasuk(model.getNoTransaksi());
            } catch (Exception e) {
                Toast.show(this, Toast.Type.ERROR, "Gagal prainjau barang masuk", getOptionAlert());
            }
        }else{
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data untuk melihat pratinjau barang masuk", getOptionAlert());
        }
        
    }
    
    private boolean inputValidationBarangMasuk(){
        boolean valid = false;
        
        Supplier selectedSupplier   = (Supplier) cbxSupplier.getSelectedItem();
        
        if(txtNoTransaksi.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "No Transaksi tidak boleh kosong", getOptionAlert());
        }else if(txtNoNota.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "No Nota tidak boleh kosong", getOptionAlert());
        }else if(pickerTanggalMasuk.getSelectedDate() == null){
            Toast.show(this, Toast.Type.INFO, "Tanggal tidak boleh kosong", getOptionAlert());
        }else if(lbSum.getText().trim().isEmpty() || lbSum.getText().equals("0")){
            Toast.show(this, Toast.Type.INFO, "Total tidak boleh kosong", getOptionAlert());
        }else if(lbTotal.getText().trim().isEmpty() || lbTotal.getText().equals("0.0")){
            Toast.show(this, Toast.Type.INFO, "Total tidak boleh kosong", getOptionAlert());
        }else if(selectedSupplier == null || selectedSupplier.getSupplierID() == 0){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih supplier", getOptionAlert());
        }else if(loggedInUser == null || loggedInUser.getUserID() == 0){
            Toast.show(this, Toast.Type.INFO, "User tidak boleh kosong", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private BarangMasuk insertDataBarangMasuk(){
        if(inputValidationBarangMasuk()){
            String noTransaksi  = txtNoTransaksi.getText();
            String noNota       = txtNoNota.getText();
            String tanggal      = pickerTanggalMasuk.getSelectedDate().toString();
            int totalJumlah     = Integer.parseInt(lbSum.getText());
            double totalMasuk   = Double.parseDouble(lbTotal.getText().replaceAll("Rp. ", "").replace(",00", "").replace(".", ""));
                        
            BarangMasuk modelBarangMasuk = new BarangMasuk();
            modelBarangMasuk.setNoTransaksi(noTransaksi);
            modelBarangMasuk.setNoNota(noNota);
            modelBarangMasuk.setTanggalMasuk(tanggal);
            modelBarangMasuk.setTotalJumlah(totalJumlah);
            modelBarangMasuk.setTotalMasuk(totalMasuk);
            modelBarangMasuk.setSupplier((Supplier) cbxSupplier.getSelectedItem());
            modelBarangMasuk.setUser(loggedInUser);
            modelBarangMasuk.setInsertBy(loggedInUser.getUserID());
            
            controllerMasuk.insertData(modelBarangMasuk);
            return modelBarangMasuk;
        }
        return null;
    }
    
    private void saveBarangMasuk() {
        
        if (inputValidationBarangMasuk()) {
            
            BarangMasuk modelBarangMasuk = insertDataBarangMasuk();
            if (modelBarangMasuk == null) return;

            List<BarangMasukTmp> tmpList = controllerMasukTmp.getData();
            if (tmpList.isEmpty()) {
                Toast.show(this, Toast.Type.WARNING, 
                    "Detail barang belum diisi", getOptionAlert());
                return;
            }
            
            for (BarangMasukTmp tmp : tmpList) {
                StokGudang modelStok = new StokGudang();
                modelStok.setBarang(tmp.getBarang());
                modelStok.setJumlah(tmp.getJumlah());
                modelStok.setHargaBeli(tmp.getHargaBeli());
                modelStok.setSubtotal(tmp.getSubtotal());
                modelStok.setRak(tmp.getRak());
                modelStok.setGudang(tmp.getGudang());
                modelStok.setInsertBy(loggedInUser.getUserID());
                controllerStok.insertData(modelStok);
                
                BarangMasukDetail modelDet = new BarangMasukDetail();
                modelDet.setBarangMasuk(modelBarangMasuk);
                modelDet.setStokGudang(modelStok);
                modelDet.setJumlah(tmp.getJumlah());
                modelDet.setHargaBeli(tmp.getHargaBeli());
                modelDet.setSubtotal(tmp.getSubtotal());
                modelDet.setRak(tmp.getRak());
                modelDet.setGudang(tmp.getGudang());
                controllerMasukDetail.insertData(modelDet);
            }
            
            controllerMasukTmp.clearTmp();
            
            Toast.show(this, Toast.Type.SUCCESS, "Data berhasil disimpan", getOptionAlert());
            loadData();
            loadDataTmp();
            resetForm();
            resetFormTmp();
            FormManager.getMainForm().checkStock();
        }
    }
}