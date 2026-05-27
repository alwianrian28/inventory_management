package com.inventory.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.ReportController;
import com.inventory.main.Form;
import com.inventory.model.Pagination;
import com.inventory.model.BarangKeluarDetail;
import com.inventory.tablemodel.TabModReportBarangKeluar;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.ExportType;
import com.inventory.util.TabelUtils;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DatePicker;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormReportBarangKeluar extends Form{
    private static final long serialVersionUID = 1L;


    private final ReportController controller = new ReportController();
    private final TabModReportBarangKeluar tblModel = new TabModReportBarangKeluar();
    
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    private JButton btnPreview, btnPDF, btnExcel;
    
    private final Pagination pagination;
    private final JLabel lbItemsPerPage = new JLabel("0 - 0 of 0");
    private final JLabel lblPageInfo = new JLabel("1 of 1");
    
    private DatePicker pickerBarangKeluarDate;
    private JFormattedTextField txtBarangKeluarDate;
    
    private Date startDate;
    private Date endDate;
    
    public FormReportBarangKeluar() {
        pagination = new Pagination(25);
        
        init();
        disableComponent();
    }
    
    @Override
    public void formOpen() {
        super.formOpen(); 
        resetDate();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx, wrap","[fill]","[][][][fill, grow]"));
        add(setInfo());
        add(createSeparator());
        add(setButton());
        add(setTableData());
        
        tblData.setModel(tblModel);
        setTableProperties();
    }

    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill","[][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JLabel imageLogo = new JLabel();
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "barang_keluar.svg", 0.4f));
        
        JLabel lbTitle = new JLabel("Data Laporan Barang Keluar");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumb = new JLabel("LAPORAN > Laporan Barang keluar");
        lbBreadcrumb.putClientProperty(FlatClientProperties.STYLE, "font:14;foreground:rgb(120,120,120)");
        
        panel.add(imageLogo);
        panel.add(lbTitle);
        panel.add(lbBreadcrumb);
        return panel;
    }
    
    private JSeparator createSeparator(){
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap","[][][]push[][][][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        pickerBarangKeluarDate = new DatePicker();
        pickerBarangKeluarDate.setDateSelectionMode(DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        pickerBarangKeluarDate.setUsePanelOption(true);
        pickerBarangKeluarDate.setCloseAfterSelected(true);
        txtBarangKeluarDate = new JFormattedTextField();
        txtBarangKeluarDate.setHorizontalAlignment(JFormattedTextField.CENTER);
        pickerBarangKeluarDate.setEditor(txtBarangKeluarDate);
        
        JButton btnGetData = new JButton("Get Data");
        btnGetData.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "get_data.svg",0.4f));
        btnGetData.setIconTextGap(5);
        btnGetData.addActionListener((e) -> {
            loadData();
        });
        
        JButton btnReset = new JButton("Reset");
        btnReset.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "reset.svg", 0.4f));
        btnReset.setIconTextGap(5);
        btnReset.addActionListener((e) -> {
            resetDate();
        });
        
        btnPreview = new JButton("Preview");
        btnPreview.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);"
                + "disabledBackground:@accentColor;"
                + "disabledText:rgb(255,255,255)");
        btnPreview.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "preview.svg",0.4f));
        btnPreview.setIconTextGap(5);
        btnPreview.addActionListener((e) -> {
            controller.exportBarangKeluar(startDate,endDate,ExportType.PREVIEW);
        });
        
        btnPDF = new JButton("PDF");
        btnPDF.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "pdf.svg",0.4f));
        btnPDF.setIconTextGap(5);
        btnPDF.addActionListener((e) -> {
            controller.exportBarangKeluar(startDate,endDate,ExportType.PDF);
        });
        
        btnExcel = new JButton("Excel");
        btnExcel.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "xls.svg",0.4f));
        btnExcel.setIconTextGap(5);
        btnExcel.addActionListener((e) -> {
            controller.exportBarangKeluar(startDate,endDate,ExportType.EXCEL);
        });
        
        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("raven/modal/demo/icons/search.svg",0.4f));
        txtSearch.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String keyword = txtSearch.getText();
            if (keyword == null) keyword = "";
            
            if (keyword.isEmpty() || keyword.trim().length() >= 2) {
                searchData();
            }
        }
    });

        
        panel.add(txtBarangKeluarDate,"hmin 30, wmin 200");
        panel.add(btnGetData,"hmin 30, wmin 90");
        panel.add(btnReset,"hmin 30, wmin 90");
        panel.add(btnPreview,"hmin 30, wmin 90");
        panel.add(btnPDF,"hmin 30, wmin 50");
        panel.add(btnExcel,"hmin 30, wmin 50");
        panel.add(txtSearch,"hmin 30, width 300, gapx 8");
        
        return panel;
    }

    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0","fill","fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        
        JScrollPane scroll = new JScrollPane(tblData);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scroll);
        panel.add(setPagination(),"dock south");
        return panel;
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
    
    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblData, new int[]{0}, new int[]{50});
        TabelUtils.setHeaderAlignment(tblData, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblData, new int[]{0}, JLabel.CENTER);
        
        tblData.putClientProperty(FlatClientProperties.STYLE, ""
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1");
    }
    
    private boolean hasValidDateSelection() {
        LocalDate[] dates = pickerBarangKeluarDate.getSelectedDateRange();
        return dates != null && dates.length >= 2;
    }

    private void getDateReport(){
        LocalDate[] dates = pickerBarangKeluarDate.getSelectedDateRange();
        if (dates != null && dates.length >= 2) {
            startDate = java.sql.Date.valueOf(dates[0]);
            endDate   = java.sql.Date.valueOf(dates[1]);
        } else {
            startDate = null;
            endDate = null;
        }
    }

    
    private void updateTable(boolean isSearch) {
        if (!hasValidDateSelection()) {
            if (!isSearch) {
                Toast.show(this, Toast.Type.INFO, "Silakan pilih rentang tanggal yang valid", getOptionAlert());
            }
            return;
        }

        getDateReport();

        String keyword = txtSearch.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = null;
        }

        int totalItems = controller.getTotalItemsBarangKeluar(startDate, endDate, keyword);
        pagination.setTotalItems(totalItems);

        List<BarangKeluarDetail> list = controller.getDataBarangKeluar(startDate, endDate, keyword, pagination.getItemsPerPage(), pagination.getCurrentPage());

        if (list.isEmpty()) {
            if (keyword != null) {
                
            } else {
                Toast.show(this, Toast.Type.INFO, "Tidak ditemukan laporan pada periode yang dipilih.", getOptionAlert());
            }
            disableComponent();
        } else {
            enableComponent();
        }

        
        tblModel.setData(list);
        int offset = (pagination.getCurrentPage() - 1) * pagination.getItemsPerPage();
        tblModel.setRowOffset(offset);
        
        int start = (totalItems > 0) ? offset + 1 : 0;
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
    
    private void resetDate(){
        disableComponent();
        pickerBarangKeluarDate.clearSelectedDate();
        txtBarangKeluarDate.setValue(null);
        tblModel.clear();
        pagination.firstPage();
    }
    
    private void disableComponent(){
        btnPreview.setEnabled(false);
        btnPDF.setEnabled(false);
        btnExcel.setEnabled(false);
    }
    
    private void enableComponent(){
        btnPreview.setEnabled(true);
        btnPDF.setEnabled(true);
        btnExcel.setEnabled(true);
    }
}
