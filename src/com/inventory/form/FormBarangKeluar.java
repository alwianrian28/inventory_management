package com.inventory.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.BarangController;
import com.inventory.controller.BarangKeluarController;
import com.inventory.controller.BarangKeluarDetailController;
import com.inventory.controller.BarangKeluarTmpController;
import com.inventory.controller.ReportController;
import com.inventory.form.input.DataBarang;
import com.inventory.form.input.DataBarangKeluarDetail;
import com.inventory.main.Form;
import com.inventory.main.FormManager;
import com.inventory.model.Barang;
import com.inventory.model.BarangKeluar;
import com.inventory.model.BarangKeluarDetail;
import com.inventory.model.BarangKeluarTmp;
import com.inventory.model.Pagination;
import com.inventory.model.StatusItem;
import com.inventory.model.User;
import com.inventory.tablemodel.TabModBarangKeluar;
import com.inventory.tablemodel.TabModBarangKeluarTmp;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.ColumnDeleteBarangKeluarTmp;
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
public class FormBarangKeluar extends Form{
    private static final long serialVersionUID = 1L;


    private JPanel mainPanel, panelView, panelAdd;
    
    private final BarangKeluarController controllerKeluar = new BarangKeluarController();
    private final BarangKeluarTmpController controllerKeluarTmp = new BarangKeluarTmpController();
    private final BarangKeluarDetailController controllerKeluarDetail = new BarangKeluarDetailController();
    
    private final BarangController controllerBarang = new BarangController();
    private final ReportController controller = new ReportController();
    
    private final TabModBarangKeluar tblModel = new TabModBarangKeluar();
    private final TabModBarangKeluarTmp tblModelTmp = new TabModBarangKeluarTmp();
    
    private final JTable tblData = new JTable();
    private final JTable tblDataTmp = new JTable();
    
    private JButton btnRestore, btnAdd, btnAddKeluar;
    
    private JTextField txtSearch, txtNoTransaksi, txtBarcode, txtNamaBarang, txtSatuan, txtJumlah, txtHargaJual, txtTujuan;
    private JFormattedTextField txtTanggalKeluar;
    private DatePicker pickerTanggalKeluar;
    
    private JComboBox<StatusItem> cbxStatus;
    private JComboBox cbxJenisKeluar;
    
    private JLabel lbSum = new JLabel();
    private JLabel lbTotal = new JLabel();
    
    private int barangKeluarTmpID;
    private int stok;
    private Barang selectedBarang;
    
    private Pagination pagination;
    private JLabel lbItemsPerPage = new JLabel("0 - 0 of 0");
    private JLabel lblPageInfo = new JLabel("1 of 1");
    
    private final User loggedInUser;
    
    public FormBarangKeluar() {
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
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "barang_keluar.svg", 0.4f));
        
        JLabel lbTItle = new JLabel("Data Barang Keluar");
        lbTItle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumb = new JLabel("TRANSAKSI > Barang Keluar");
        lbBreadcrumb.putClientProperty(FlatClientProperties.STYLE, "font:14;foreground:rgb(120,120,120)");
        
        panel.add(imageLogo);
        panel.add(lbTItle);
        panel.add(lbBreadcrumb);
        return panel;
    }
    
    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap","[][][][]push[][][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        btnAddKeluar = new JButton("Add");
        btnAddKeluar.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);"
                + "disabledBackground:@accentColor;"
                + "disabledText:rgb(255,255,255)");
        btnAddKeluar.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "add_white.svg",0.8f));
        btnAddKeluar.setIconTextGap(5);
        btnAddKeluar.addActionListener((e) -> {
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "add");
            txtBarcode.requestFocus();
            generateKodeKeluar();
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
            showBarangKeluarDetail();
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
        
        panel.add(btnAddKeluar,"hmin 30, wmin 90");
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
        
        TabelUtils.setColumnWidths(tblDataTmp, new int[]{0,7}, new int[]{50,100});
        TabelUtils.setHeaderAlignment(tblDataTmp, new int[]{0,7}, new int[]{JLabel.CENTER,JLabel.CENTER}, JLabel.LEFT);
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

        int totalItems = controllerKeluar.getTotalItems(isDeleted, keyword);
        pagination.setTotalItems(totalItems);

        List<BarangKeluar> list;
        if (isSearch) {
            list = controllerKeluar.searchData(keyword, isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
        } else {
            list = controllerKeluar.getData(isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
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
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "barang_keluar.svg", 0.4f));
        
        JLabel lbTItle = new JLabel("Tambah Barang Keluar");
        lbTItle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumb = new JLabel("TRANSAKSI > Tambah Barang Keluar");      
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
            saveBarangKeluar();
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
        
        txtTanggalKeluar = new JFormattedTextField();
        txtTanggalKeluar.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255);foreground:rgb(0,0,0)");
        
        pickerTanggalKeluar = new DatePicker() {
            @Override
            public void showPopup() {
                // kosong untuk disabled pop up
            }
        };
        pickerTanggalKeluar.setCloseAfterSelected(true);
        pickerTanggalKeluar.setDateFormat("yyyy-MM-dd");
        pickerTanggalKeluar.setSelectedDate(LocalDate.now());
        pickerTanggalKeluar.setEditor(txtTanggalKeluar);
        txtTanggalKeluar.setEditable(false);        
        
        JTextField txtPetugas = new JTextField();
        txtPetugas.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Petugas");
        txtPetugas.setText(loggedInUser.getName());
        txtPetugas.setEditable(false);
        
        panelFaktur.add(new JLabel("No Transaksi"));
        panelFaktur.add(txtNoTransaksi,"w 150!");
        panelFaktur.add(new JLabel("Tanggal"));
        panelFaktur.add(txtTanggalKeluar,"w 120!");
        panelFaktur.add(new JLabel("Petugas"));
        panelFaktur.add(txtPetugas,"w 150!");
        
        JPanel panelSupplier = new JPanel(new MigLayout("fill, wrap 2","[50][fill, grow]"));
        panelSupplier.setBorder(BorderFactory.createLineBorder(new Color(206,206,206)));
        panelSupplier.setOpaque(false);
        
        cbxJenisKeluar = new JComboBox();
        cbxJenisKeluar.addItem("Pilih Jenis Keluar");
        cbxJenisKeluar.addItem("PENJUALAN");
        cbxJenisKeluar.addItem("INTERNAL");
        cbxJenisKeluar.addItem("RETUR_SUPPLIER");
        cbxJenisKeluar.addItem("PINJAM");
        cbxJenisKeluar.addItem("RUSAK_HILANG");
        cbxJenisKeluar.addItem("TRANSFER_GUDANG");
        
        txtTujuan = new JTextField();
        txtTujuan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tujuan");
        
        panelSupplier.add(new JLabel("Jenis Keluar"));
        panelSupplier.add(cbxJenisKeluar,"w 200!");
        panelSupplier.add(new JLabel("Tujuan"));
        panelSupplier.add(txtTujuan,"w 200!");
        
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
            "fillx, insets 10, wrap 5",
            "[170:200,grow][200:200,grow][100:200,grow][80:200][150:200,grow]",
            "[][][][][fill,grow]"
        ));
        panelBarang.setBorder(BorderFactory.createTitledBorder("Barang"));
        panelBarang.setOpaque(false);

        // === Header ===
        panelBarang.add(new JLabel("Barcode"),         "left");
        panelBarang.add(new JLabel("Barang"),            "left");
        panelBarang.add(new JLabel("Satuan"),            "left");
        panelBarang.add(new JLabel("Jumlah"),          "left");
        panelBarang.add(new JLabel("Harga Jual"),      "left");
        
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
        
        txtHargaJual = new JTextField(15);
        txtHargaJual.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Harga Jual");
        ((AbstractDocument) txtHargaJual.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        JButton btnSearch  = new JButton();
        btnSearch.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "browse.svg",0.4f));
        btnSearch.addActionListener(e -> {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            
            DataBarang dialog = new DataBarang(parent, "Data Barang", false, null);
            dialog.setVisible(true);
            setData(dialog);
            cbxJenisKeluar.putClientProperty("JComponent.outline", null);
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
        panelBarang.add(txtHargaJual,      "growx");
        panelBarang.add(txtJumlah,   "growx, wrap");
        
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
        
        ColumnDeleteBarangKeluarTmp.install(tblDataTmp, 7,() -> {
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
    private void generateKodeKeluar(){
        txtNoTransaksi.setText(controllerKeluar.generateBarangKeluarCode());
    }
    
    private void setDateUser(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        txtTanggalKeluar.setText(df.format(new Date()));
    }
    
    private void setData(DataBarang dataBarang) {
        if(dataBarang != null){
            dataBarang.setSelectionListener((barang) -> {
                selectedBarang = barang;
                txtBarcode.setText(barang.getBarcode());
                txtNamaBarang.setText(barang.getBarangName());
                txtSatuan.setText(barang.getSatuan().getSatuanName());
                
                double hargaJual = barang.getHargaJual();
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
                DecimalFormat df = new DecimalFormat("#,###", symbols); 
                txtHargaJual.setText(df.format(hargaJual));
                
                txtJumlah.requestFocus();
                
                stok = barang.getTotalStok();
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
            txtNamaBarang.setText(barang.getBarangName());
            txtSatuan.setText(barang.getSatuan().getSatuanName());
            
            double hargaJual = barang.getHargaJual();
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
            DecimalFormat df = new DecimalFormat("#,###", symbols); 
            txtHargaJual.setText(df.format(hargaJual));
            
            txtJumlah.requestFocus();
        } else {
            Toast.show(this, Toast.Type.WARNING, "Barang dengan barcode " + barcode + " tidak ditemukan", getOptionAlert());
            resetFormTmp();
        }
    }
    
    private void loadDataTmp() {
        List<BarangKeluarTmp> list = controllerKeluarTmp.getData();
        tblModelTmp.setData(list);
        tblDataTmp.setModel(tblModelTmp);
        
        int totalQty = controllerKeluarTmp.sumJumlah();
        double totalBarangKeluar = controllerKeluarTmp.sumTotalJual();

        lbSum.setText(String.valueOf(totalQty));

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setCurrencySymbol("Rp.");
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat df = new DecimalFormat("¤ #,###.00", symbols); // ¤ = currency symbol
        lbTotal.setText(df.format(totalBarangKeluar));
    }
    
    private boolean inputValidationTmp(){
        boolean valid = false;
        
        if(selectedBarang == null){
            Toast.show(this, Toast.Type.INFO, "Barang tidak boleh kosong", getOptionAlert());
        }else if(txtJumlah.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Jumlah tidak boleh kosong", getOptionAlert());
        }else if(txtHargaJual.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Harga jual tidak boleh kosong", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private void insertDataTmp(){
        if(inputValidationTmp()){
            int jumlah          = NumericDocumentFilter.getNumericValueAsInt(txtJumlah);
            double hargaJual    = NumericDocumentFilter.getNumericValue(txtHargaJual);
            String barcode      = txtBarcode.getText();
            
            boolean exists = controllerKeluarTmp.getData()
                .stream()
                .anyMatch(tmp -> tmp.getBarang().getBarcode().equals(barcode));

            if (exists) {
                Toast.show(this, Toast.Type.WARNING, "Barang dengan barcode " + barcode + " sudah ada!", getOptionAlert());
                resetFormTmp();
                return;
            }
            
            if (jumlah > stok) {
                Toast.show(this,Toast.Type.ERROR,"Stok tidak mencukupi. Stok tersedia: " + stok,getOptionAlert());
                return;
            }
            
            BarangKeluarTmp modelTmp = new BarangKeluarTmp();
            modelTmp.setBarang(selectedBarang);
            modelTmp.setJumlah(jumlah);
            modelTmp.setHargaJual(hargaJual);
            
            controllerKeluarTmp.insertData(modelTmp);
            loadDataTmp();
            resetFormTmp();
        }
    }

    private void getDataTmp() {
        btnAdd.setText("Update");
        int row = tblDataTmp.getSelectedRow();
        if (row != -1) {
            BarangKeluarTmp selected = tblModelTmp.getData(row);

            barangKeluarTmpID = selected.getBarangKeluarTmpID();
            System.out.println("ID " + selected.getBarangKeluarTmpID());
            selectedBarang = selected.getBarang();
            txtBarcode.setText(selected.getBarang().getBarcode());
            txtNamaBarang.setText(selected.getBarang().getBarangName());
            txtSatuan.setText(selected.getBarang().getSatuan().getSatuanName());
            
            txtJumlah.setText(String.valueOf(selected.getJumlah()));
            
            double hargaJual = selected.getHargaJual();
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
            DecimalFormat df = new DecimalFormat("#,###", symbols); 
            txtHargaJual.setText(df.format(hargaJual));
        }
    }

    private void updateDataTmp(){
        if(inputValidationTmp()){
            int jumlah          = NumericDocumentFilter.getNumericValueAsInt(txtJumlah);
            double hargaJual    = NumericDocumentFilter.getNumericValue(txtHargaJual);
            
            if (jumlah > stok) {
                Toast.show(this,Toast.Type.ERROR,"Stok tidak mencukupi. Stok tersedia: " + stok,getOptionAlert());
                return;
            }
            
            BarangKeluarTmp modelTmp = new BarangKeluarTmp();
            modelTmp.setBarang(selectedBarang);
            modelTmp.setJumlah(jumlah);
            modelTmp.setHargaJual(hargaJual);
            modelTmp.setBarangKeluarTmpID(barangKeluarTmpID);
            
            controllerKeluarTmp.updateData(modelTmp);
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
        txtHargaJual.setText("");
    }
    
    private void disabledTextfield(){
        txtNamaBarang.setEditable(false);
        txtSatuan.setEditable(false);
        txtHargaJual.setEditable(false);
    }
    
    private void resetForm() {
        txtNoTransaksi.setText("");
        txtTujuan.setText("");
        lbSum.setText("0");
        lbTotal.setText("0.0");
        cbxJenisKeluar.setSelectedIndex(0);
        txtTujuan.setText("");
    }

    private void deleteData() {
        int row = tblData.getSelectedRow();
        if(row != -1){
            BarangKeluar model = tblModel.getData(row);
            
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
                                    BarangKeluar modelBarangKeluar = new BarangKeluar();
                                    modelBarangKeluar.setDeleteBy(loggedInUser.getUserID());
                                    modelBarangKeluar.setBarangKeluarID(model.getBarangKeluarID());
                                    controllerKeluar.deleteData(modelBarangKeluar);
                                    controllerKeluar.rollbackStok(modelBarangKeluar.getBarangKeluarID());
                                    
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
            BarangKeluar model = tblModel.getData(row);
            
            String message = "Apakah Anda yakin ingin merestore data ini?";
            String title = "Konfirmasi";

            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, new MessageModal(MessageModal.Type.INFO, message, title, ModalBorder.YES_NO_OPTION, 
                (controller, action) -> {
                    if (action == ModalBorder.YES_OPTION) {
                        controllerKeluar.restoreData(model.getBarangKeluarID());
                        controllerKeluar.updateStok(model.getBarangKeluarID());
                        Toast.show(this, Toast.Type.SUCCESS, "Data berhasil direstore", getOptionAlert());
                        loadData();
                        FormManager.getMainForm().checkStock();
                    }
                }),option);
        } else {
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data yang ingin direstore", getOptionAlert());
        }
    }

    private void showBarangKeluarDetail() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            BarangKeluar model = tblModel.getData(row);
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            DataBarangKeluarDetail dialog = new DataBarangKeluarDetail(parent, "Data Detail Barang Keluar", false, model);
            dialog.setVisible(true);
        }else{
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data untuk melihat detail barang masuk", getOptionAlert());
        }
    }

    private void previewData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            BarangKeluar model = tblModel.getData(row);
            try {
                controller.previewBarangKeluar(model.getNoTransaksi());
            } catch (Exception e) {
                Toast.show(this, Toast.Type.ERROR, "Gagal prainjau barang masuk", getOptionAlert());
            }
        }else{
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data untuk melihat pratinjau barang masuk", getOptionAlert());
        }
        
    }
    
    private boolean inputValidationBarangKeluar(){
        boolean valid = false;
        
        if(txtNoTransaksi.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "No Transaksi tidak boleh kosong", getOptionAlert());
        }else if(cbxJenisKeluar.getSelectedIndex() == 0){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih jenis keluar", getOptionAlert());
        }else if(txtTujuan.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Tujuan tidak boleh kosong", getOptionAlert());
        }else if(pickerTanggalKeluar.getSelectedDate() == null){
            Toast.show(this, Toast.Type.INFO, "Tanggal tidak boleh kosong", getOptionAlert());
        }else if(lbSum.getText().trim().isEmpty() || lbSum.getText().equals("0")){
            Toast.show(this, Toast.Type.INFO, "Total tidak boleh kosong", getOptionAlert());
        }else if(lbTotal.getText().trim().isEmpty() || lbTotal.getText().equals("0.0")){
            Toast.show(this, Toast.Type.INFO, "Total tidak boleh kosong", getOptionAlert());
        }else if(loggedInUser == null || loggedInUser.getUserID() == 0){
            Toast.show(this, Toast.Type.INFO, "User tidak boleh kosong", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private BarangKeluar insertDataBarangKeluar(){
        if(inputValidationBarangKeluar()){
            String noTransaksi  = txtNoTransaksi.getText();
            String tanggal      = pickerTanggalKeluar.getSelectedDate().toString();
            String jenisKeluar  = cbxJenisKeluar.getSelectedItem().toString();
            String tujuan       = txtTujuan.getText().trim();
            int totalJumlah     = Integer.parseInt(lbSum.getText());
            double totalKeluar   = Double.parseDouble(lbTotal.getText().replaceAll("Rp. ", "").replace(",00", "").replace(".", ""));
                        
            BarangKeluar modelBarangKeluar = new BarangKeluar();
            modelBarangKeluar.setNoTransaksi(noTransaksi);
            modelBarangKeluar.setTanggalKeluar(tanggal);
            modelBarangKeluar.setJenisKeluar(jenisKeluar);
            modelBarangKeluar.setTujuan(tujuan);
            modelBarangKeluar.setTotalJumlah(totalJumlah);
            modelBarangKeluar.setTotalKeluar(totalKeluar);
            modelBarangKeluar.setUser(loggedInUser);
            modelBarangKeluar.setInsertBy(loggedInUser.getUserID());
            
            controllerKeluar.insertData(modelBarangKeluar);
            return modelBarangKeluar;
        }
        return null;
    }
    
    private void saveBarangKeluar() {
        
        if (inputValidationBarangKeluar()) {
            
            BarangKeluar modelBarangKeluar = insertDataBarangKeluar();
            if (modelBarangKeluar == null) return;

            List<BarangKeluarTmp> tmpList = controllerKeluarTmp.getData();
            if (tmpList.isEmpty()) {
                Toast.show(this, Toast.Type.WARNING, 
                    "Detail barang belum diisi", getOptionAlert());
                return;
            }
            
            for (BarangKeluarTmp tmp : tmpList) {
                BarangKeluarDetail modelDet = new BarangKeluarDetail();
                modelDet.setBarangKeluar(modelBarangKeluar);
                modelDet.setBarang(tmp.getBarang());
                modelDet.setJumlah(tmp.getJumlah());
                modelDet.setHargaJual(tmp.getHargaJual());
                modelDet.setSubtotal(tmp.getSubtotal());
                controllerKeluarDetail.insertData(modelDet);
            }
            
            controllerKeluar.updateStok(modelBarangKeluar.getBarangKeluarID());
            controllerKeluarTmp.clearTmp();
            
            Toast.show(this, Toast.Type.SUCCESS, "Data berhasil disimpan", getOptionAlert());
            loadData();
            loadDataTmp();
            resetForm();
            FormManager.getMainForm().checkStock();
        }
    }
}