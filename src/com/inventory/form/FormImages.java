package com.inventory.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.ImagesController;
import com.inventory.dao.ImagesDAO;
import com.inventory.form.input.FormInputImages;
import com.inventory.main.Form;
import com.inventory.model.Images;
import com.inventory.model.Pagination;
import com.inventory.tablemodel.TabModImages;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.ImageRendererBanner;
import com.inventory.util.MessageModal;
import com.inventory.util.TabelUtils;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Option;

/**
 *
 * @author Dearclaudia
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormImages extends Form{
    private static final long serialVersionUID = 1L;


    private final ImagesController controller = new ImagesController();
    private final TabModImages tblModel = new TabModImages();
    
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    
    
    private Pagination pagination;
    private JLabel lbItemsPerPage = new JLabel("0 - 0 of 0");
    private JLabel lblPageInfo = new JLabel("1 of 1");
    
    
    public FormImages() {
        pagination = new Pagination(25);
        
        init();
    }
    
    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
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
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "images.svg", 0.4f));
        
        JLabel lbTItle = new JLabel("Data Images");
        lbTItle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumb = new JLabel("OTHER > Images");
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
        JPanel panel = new JPanel(new MigLayout("wrap","[][][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JButton btnAdd = new JButton("Add");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);"
                + "disabledBackground:@accentColor;"
                + "disabledText:rgb(255,255,255)");
        btnAdd.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "add_white.svg",0.8f));
        btnAdd.setIconTextGap(5);
        btnAdd.addActionListener((e) -> {
            insertData();
        });
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "edit.svg",0.4f));
        btnUpdate.setIconTextGap(5);
        btnUpdate.addActionListener((e) -> {
            updateData();
        });
        
        JButton btnDelete = new JButton("Delete");
        btnDelete.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "delete.svg",0.4f));
        btnDelete.setIconTextGap(5);
        btnDelete.addActionListener((e) -> {
            deleteData();
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
        
        panel.add(btnAdd,"hmin 30, wmin 90");
        panel.add(btnUpdate,"hmin 30, wmin 50");
        panel.add(btnDelete,"hmin 30, wmin 50");
        panel.add(txtSearch,"hmin 30, width 300, gapx 8");
        
        return panel;
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
        tblData.getColumnModel().getColumn(2).setPreferredWidth(360);
        tblData.getColumnModel().getColumn(2).setCellRenderer(new ImageRendererBanner(600));

        TabelUtils.setColumnWidths(tblData, new int[]{0}, new int[]{50});
        TabelUtils.setHeaderAlignment(tblData, new int[]{0,2}, new int[]{JLabel.CENTER,JLabel.CENTER}, JLabel.LEFT);
        TabelUtils.setColumnAlignment(tblData, new int[]{0}, JLabel.CENTER);
        
        tblData.putClientProperty(FlatClientProperties.STYLE, ""
                + "showHorizontalLines:true;"
                + "intercellSpacing:0,1;"
                + "rowHeight:100");
        tblData.setRowMargin(10);
    }
    
    private void updateTable(boolean isSearch) {
        String keyword = txtSearch.getText();

        int totalItems = controller.getTotalItems(keyword);
        pagination.setTotalItems(totalItems);

        List<Images> list;
        if (isSearch) {
            list = controller.searchData(keyword, pagination.getItemsPerPage(), pagination.getCurrentPage());
        } else {
            list = controller.getData(pagination.getItemsPerPage(), pagination.getCurrentPage());
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
                new SimpleModalBorder(new FormInputImages(null, this),"Tambah Images"), option,"form input");
    }

    private void updateData() {
        int row = tblData.getSelectedRow();
        if(row != -1){
            Images model = tblModel.getData(row);
            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, 
                    new SimpleModalBorder(new FormInputImages(model, this),"Perbarui Images"), option,"form update");
        }else{
            Toast.show(this, Toast.Type.INFO, "Please select the data you want to update", getOptionAlert());
        }
    }

    private void deleteData() {
        int row = tblData.getSelectedRow();
        if(row != -1){
            Images model = tblModel.getData(row);
            
            String message = "Are you sure you want to delete this data ?";
            String title = "Confirmation";
            
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
                                    String imagePath = model.getImagePath();
                                    if (imagePath != null && !imagePath.isEmpty()) {
                                        File file = new File(System.getProperty("user.dir"), imagePath);
                                        if (file.exists()) {
                                            file.delete();
                                        }
                                    }
                                    
                                    controller.deleteData(model.getImageID());
                                    Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully deleted", getOptionAlert());
                                    loadData();
                                }
                            }),option);
        }else{
            Toast.show(this, Toast.Type.INFO, "Please select the data you want to delete", getOptionAlert());
        }
    }
}
