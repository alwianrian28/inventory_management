package com.inventory.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.RakController;
import com.inventory.form.input.FormInputRak;
import com.inventory.main.Form;
import com.inventory.main.FormManager;
import com.inventory.model.Gudang;
import com.inventory.model.Rak;
import com.inventory.model.Pagination;
import com.inventory.model.StatusItem;
import com.inventory.model.User;
import com.inventory.tablemodel.TabModRak;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.MessageModal;
import com.inventory.util.ModalBorder;
import com.inventory.util.TabelUtils;
import com.inventory.util.TransparentOptionPane;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.swing.MigLayout;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Option;

/**
 *
 * @author Dearclaudia
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormRak extends Form{
    private static final long serialVersionUID = 1L;


    private final RakController controller = new RakController();
    private final TabModRak tblModel = new TabModRak();
    
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    private JComboBox<StatusItem> cbxStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnRestore;
    
    private int RakID;
    
    private Pagination pagination;
    private JLabel lbItemsPerPage = new JLabel("0 - 0 of 0");
    private JLabel lblPageInfo = new JLabel("1 of 1");
    
    public FormRak() {
        pagination = new Pagination(25);
        
        init();
        cbxStatus.setSelectedIndex(0);
    }
    
    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
        configureRole();
    }

    private void configureRole() {
        User user = FormManager.getLoggedInUser();
        boolean isAdmin = user != null && "Admin".equals(user.getRole());

        btnAdd.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        btnRestore.setVisible(false);
        cbxStatus.setVisible(false);

        if (isAdmin) {
            btnAdd.setVisible(true);
            btnUpdate.setVisible(true);
            btnDelete.setVisible(true);
            cbxStatus.setVisible(true);
        }
    }
    
    private void init() {
        setLayout(new MigLayout("fillx, wrap","[fill]","[][][][fill, grow]"));
        add(setInfo());
        add(createSeparator());
        add(setButton());
        add(setTableData());
    }

    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill","[][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JLabel imageLogo = new JLabel();
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "rak.svg", 0.4f));
        
        JLabel lbTItle = new JLabel("Data Rak");
        lbTItle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumb = new JLabel("MASTER > Rak");
        lbBreadcrumb.putClientProperty(FlatClientProperties.STYLE, "font:14;foreground:rgb(120,120,120)");
        
        panel.add(imageLogo);
        panel.add(lbTItle);
        panel.add(lbBreadcrumb);
        return panel;
    }
    
    private JSeparator createSeparator(){
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap","[][][][]push[][][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        btnAdd = new JButton("Add");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
        btnAdd.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "add_white.svg",0.8f));
        btnAdd.setIconTextGap(5);
        btnAdd.addActionListener((e) -> {
            insertData();
        });
        
        btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "edit.svg",0.4f));
        btnUpdate.setIconTextGap(5);
        btnUpdate.addActionListener((e) -> {
            updateData();
        });
        
        btnDelete = new JButton("Delete");
        btnDelete.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "delete.svg",0.4f));
        btnDelete.setIconTextGap(5);
        btnDelete.addActionListener((e) -> {
            deleteData();
        });
        
        JButton btnExcel = new JButton();
        btnExcel.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "xls.svg",0.4f));
        btnExcel.setIconTextGap(5);
        btnExcel.addActionListener((e) -> {
            exportToExcel();
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
            handleStatusChange(btnRestore, btnAdd, btnUpdate, btnDelete, btnExcel);
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
        
        panel.add(btnAdd,"hmin 30, wmin 90, hidemode 2");
        panel.add(btnUpdate,"hmin 30, wmin 50, hidemode 2");
        panel.add(btnDelete,"hmin 30, wmin 50, hidemode 2");
        panel.add(btnExcel,"hmin 30, wmin 50");
        panel.add(txtSearch,"hmin 30, width 300, gapx 8");
        panel.add(btnRestore,"hmin 30, wmin 50, hidemode 3");
        panel.add(cbxStatus,"hmin 30, wmin 50, hidemode 3");
        
        return panel;
    }

    private void handleStatusChange(JButton btnRestore, JButton btnAdd, JButton btnUpdate, JButton btnDelete, JButton btnExcel) {
        StatusItem selected = (StatusItem) cbxStatus.getSelectedItem();
        if (selected != null) {
            boolean isDeleted = selected.getValue();
            btnRestore.setVisible(isDeleted);
            btnAdd.setEnabled(!isDeleted);
            btnUpdate.setEnabled(!isDeleted);
            btnDelete.setEnabled(!isDeleted);
            btnExcel.setEnabled(!isDeleted);
        }
        pagination.firstPage();
        loadData();
    }
    
    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0","fill","fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10;background:rgb(255,255,255)");
        
        loadData();
        setTableProperties();
        
        JPanel panelSeparator = new JPanel(new MigLayout("debug, insets 5 0 5 0"));
        panelSeparator.putClientProperty(FlatClientProperties.STYLE, "background:rgb(206,206,206)");
        
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
    
    private void updateTable(boolean isSearch) {
        StatusItem selected = (StatusItem) cbxStatus.getSelectedItem();
        boolean isDeleted = selected.getValue();
        String keyword = txtSearch.getText();

        int totalItems = controller.getTotalItems(isDeleted, keyword);
        pagination.setTotalItems(totalItems);

        List<Rak> list;
        if (isSearch) {
            list = controller.searchData(keyword, isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
        } else {
            list = controller.getData(isDeleted, pagination.getItemsPerPage(), pagination.getCurrentPage());
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

    private void insertData() {
        Option option = ModalDialog.createOption();
        option.setBackground(Color.BLACK);
        ModalDialog.showModal(this, 
                new SimpleModalBorder(new FormInputRak(null, this),"Tambah Rak"), option,"form input");
    }

    private void updateData() {
        int row = tblData.getSelectedRow();
        if(row != -1){
            Rak model = tblModel.getData(row);
            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, 
                    new SimpleModalBorder(new FormInputRak(model, this),"Perbarui Rak"), option,"form update");
        }else{
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data yang ingin diperbarui", getOptionAlert());
        }
    }

    private void deleteData() {
        int row = tblData.getSelectedRow();
        if(row != -1){
            Rak model = tblModel.getData(row);
            
            String message = "Apakah Anda yakin ingin menghapus data ini?";
            String title = "Konfirmasi";
            
            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this,
                    new MessageModal(
                            MessageModal.Type.INFO, 
                            message, 
                            title, 
                            SimpleModalBorder.YES_NO_OPTION, 
                            (control, action) -> {
                                if(action == SimpleModalBorder.YES_OPTION){
                                    User user = FormManager.getLoggedInUser();
                                    if (user != null) {
                                        model.setDeleteBy(user.getUserID());
                                    }

                                    boolean success = controller.deleteData(model);

                                    if (success) {
                                        Toast.show(this, Toast.Type.SUCCESS,
                                                "Data berhasil dihapus",
                                                getOptionAlert());
                                        loadData();
                                    } else {
                                        Toast.show(this, Toast.Type.ERROR,
                                                "Gagal menghapus data",
                                                getOptionAlert());
                                    }
                                }
                            }),option);
        }else{
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data yang ingin dihapus", getOptionAlert());
        }
    }
    
    private void restoreData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Rak model = tblModel.getData(row);
            
            String message = "Apakah Anda yakin ingin merestore data ini?";
            String title = "Konfirmasi";

            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, new MessageModal(MessageModal.Type.INFO, message, title, ModalBorder.YES_NO_OPTION, 
                (control, action) -> {
                    if (action == ModalBorder.YES_OPTION) {
                        boolean success = controller.restoreData(model.getRakID(), model.getRakCode(), model.getRakName());

                        if (success) {
                            Toast.show(this, Toast.Type.SUCCESS,
                                    "Data berhasil direstore",
                                    getOptionAlert());
                            loadData();
                        } else {
                            Toast.show(this, Toast.Type.ERROR,
                                    "Gagal merestore data\nPeriksa data yang duplikat",
                                    getOptionAlert());
                        }
                    }
                }),option);
        } else {
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih data yang ingin direstore", getOptionAlert());
        }
    }
    
    private void exportToExcel() {
        List<Rak> data = controller.getData(false, Integer.MAX_VALUE, 1);
        
        File fileToSave = TransparentOptionPane.showSaveFileDialog(
                FormManager.getJFrame(), 
                "Save Excel File", 
                new FileNameExtensionFilter("Excel Files", "xlsx"), 
                new File("Rak.xlsx"));

        if (fileToSave.exists()) {
            int result = TransparentOptionPane.showConfirmDialog(
                    FormManager.getJFrame(),
                    "File sudah ada. Apakah ingin menimpa?",
                    "Konfirmasi Overwrite",
                    JOptionPane.YES_NO_OPTION
            );
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Rak");

            // Header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("No");
            header.createCell(1).setCellValue("Kode Rak");
            header.createCell(2).setCellValue("Nama Rak");
            header.createCell(3).setCellValue("Keterangan");

            // Data
            int rowNum = 1;
            int nomor = 1;
            for (Rak r : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(nomor++);
                row.createCell(1).setCellValue(r.getRakCode());
                row.createCell(2).setCellValue(r.getRakName());
                row.createCell(3).setCellValue(r.getKeterangan());
            }

            // auto size kolom
            for (int i = 0; i <= 3; i++) {
                sheet.autoSizeColumn(i);
            }

            // simpan file
            try (FileOutputStream fileOut = new FileOutputStream(fileToSave)) {
                workbook.write(fileOut);
            }

            Toast.show(this, Toast.Type.SUCCESS, "Data berhasil diexport", getOptionAlert());

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(fileToSave);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.show(this, Toast.Type.ERROR, "Gagal export data " + ex.getMessage(), getOptionAlert());
        }
    }
}
