package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.BarangController;
import com.inventory.controller.GudangController;
import com.inventory.controller.KategoriController;
import com.inventory.controller.MerkController;
import com.inventory.controller.RakController;
import com.inventory.controller.SatuanController;
import com.inventory.controller.SupplierController;
import com.inventory.form.FormBarang;
import com.inventory.main.FormManager;
import com.inventory.model.Barang;
import com.inventory.model.Kategori;
import com.inventory.model.Merk;
import com.inventory.model.Satuan;
import com.inventory.model.Supplier;
import com.inventory.model.User;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.NumericDocumentFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
public class FormInputBarang extends JPanel{
    private static final long serialVersionUID = 1L;


    private JTextField txtBarangCode, txtBarcode, txtBarangName, txtHargaJual, txtStokMinimum;
    private JComboBox cbxMerk, cbxKategori, cbxSatuan, cbxSupplier;
    private JButton btnSave, btnCancel, btnBrowse;
   
    private JLabel lbPhoto;
    private String filePath;
    
    private final BarangController barangController = new BarangController();
    private final MerkController merkController = new MerkController();
    private final KategoriController kategoriController = new KategoriController();
    private final SatuanController satuanController = new SatuanController();
    private final RakController rakController = new RakController();
    private final GudangController gudangController = new GudangController();
    private final SupplierController supplierController = new SupplierController();
    
    private final Barang model;
    private final FormBarang formBarang;
    private int barangID;
    private final User loggedInUser;
    
    public FormInputBarang(Barang model, FormBarang formBarang) {
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formBarang = formBarang;
        
        init();
        
        if(model != null){
            loadData();
        }else{
            generateBarangCode();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 4, gap 20, width 800","[50][fill, grow][50][fill, grow]"));
        this.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255)");
        
        txtBarangCode = new JTextField();
        txtBarangCode.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan kode barang");
        
        txtBarcode = new JTextField();
        txtBarcode.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan Barcode");
        
        txtBarangName = new JTextField();
        txtBarangName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama barang");
        
        cbxMerk = new JComboBox<>();
        loadMerk();
        
        cbxKategori = new JComboBox<>();
        loadKategori();
        
        cbxSatuan = new JComboBox<>();
        loadSatuan();
        
        cbxSupplier = new JComboBox<>();
        loadSupplier();
        
        txtHargaJual = new JTextField(15);
        txtHargaJual.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan harga Jual");
        ((AbstractDocument) txtHargaJual.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        txtStokMinimum = new JTextField(10);
        txtStokMinimum.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan stok minimum");
        ((AbstractDocument) txtStokMinimum.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        btnSave = new JButton("Save");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
        btnSave.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "save_white.svg",0.8f));
        btnSave.setIconTextGap(5);
        btnSave.addActionListener((e) -> {
            if(model == null){
                insertData();
            }else{
                updateData();
            }
        });
        
        btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "cancel.svg",0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.addActionListener((e) -> {
            if(model == null){
                ModalDialog.closeModal("form input");
            }else{
                ModalDialog.closeModal("form update");
            }
        });
        
        add(createSeparator(),"span, growx, height 2!");
        add(new JLabel("Kode Barang"),"align right");
        add(txtBarangCode);
        add(new JLabel("Merk"),"align right");
        add(cbxMerk);
        add(new JLabel("Barcode"),"align right");
        add(txtBarcode);
        add(new JLabel("Kategori"),"align right");
        add(cbxKategori);
        add(new JLabel("Nama Barang"),"align right");
        add(txtBarangName);
        add(new JLabel("Satuan"),"align right");
        add(cbxSatuan);
        add(new JLabel("Harga Jual"),"align right");
        add(txtHargaJual);
        add(new JLabel("Supplier"),"align right");
        add(cbxSupplier);
        add(new JLabel("Stok Minimum"),"align right, top");
        add(txtStokMinimum,"top");
        add(new JLabel("Photo"),"align right, top");
        add(createPhotoPanel(), "wrap");
        add(createSeparator(),"span, growx, height 2!");
        add(btnSave,"span, split 2, align center, sg btn, hmin 30");
        add(btnCancel,"sg btn, hmin 30");
    }
    
    private void loadMerk() {
        cbxMerk.removeAllItems();
        cbxMerk.addItem(new Merk(0, "Pilih Merk"));
        List<Merk> merk = merkController.getData(false, Integer.MAX_VALUE,1);
        for (Merk m : merk) {
            cbxMerk.addItem(m);
        }
    }
    
    private void loadKategori() {
        cbxKategori.removeAllItems();
        cbxKategori.addItem(new Kategori(0, "Pilih Kategori"));
        List<Kategori> ktg = kategoriController.getData(false, Integer.MAX_VALUE,1);
        for (Kategori k : ktg) {
            cbxKategori.addItem(k);
        }
    }
    
    private void loadSatuan() {
        cbxSatuan.removeAllItems();
        cbxSatuan.addItem(new Satuan(0, "Pilih Satuan"));
        List<Satuan> stn = satuanController.getData(false, Integer.MAX_VALUE,1);
        for (Satuan s : stn) {
            cbxSatuan.addItem(s);
        }
    }
    
    private void loadSupplier() {
        cbxSupplier.removeAllItems();
        cbxSupplier.addItem(new Supplier(0, "Pilih Supplier"));
        List<Supplier> sup = supplierController.getData(false, Integer.MAX_VALUE,1);
        for (Supplier s : sup) {
            cbxSupplier.addItem(s);
        }
    }
    
    private JPanel createPhotoPanel() {
        JPanel panel = new JPanel(new MigLayout("insets 0, gap 5"));
        panel.setPreferredSize(new Dimension(120, 100));
        panel.setOpaque(false);

        lbPhoto = new JLabel();
        lbPhoto.setPreferredSize(new Dimension(100, 100));
        lbPhoto.setOpaque(true);
        lbPhoto.setBackground(Color.WHITE);
        lbPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        lbPhoto.setHorizontalAlignment(JLabel.CENTER);
        lbPhoto.setVerticalAlignment(JLabel.CENTER);
        Icon icon = new FlatSVGIcon(AppResources.ICON_BASE + "Barang.svg", 80, 80);
        lbPhoto.setIcon(icon);
        
        btnBrowse = new JButton();
        btnBrowse.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "browse.svg", 0.4f));
        btnBrowse.addActionListener((e) -> {
            JFileChooser fileChooser = new JFileChooser();
            
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (jpg, jpeg, png)", "jpg", "jpeg", "png");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePath = selectedFile.getAbsolutePath();
                
                setPhoto(filePath);
            }
        });
        
        panel.add(lbPhoto, "w 100!, h 100!");
        panel.add(btnBrowse, "align left, top, gapy 2");

        return panel;
    }
    
    private void setPhoto(String path) {
        if (path != null && !path.isEmpty()) {
            ImageIcon originalIcon = new ImageIcon(path);
            Image originalImage = originalIcon.getImage();

            int labelWidth = 100;
            int labelHeight = 100;

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
            lbPhoto.setIcon(new ImageIcon(scaledImage));
        } else {
            lbPhoto.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "Barang.svg", 80, 80));
        }
    }
    
    private JSeparator createSeparator(){
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private boolean inputValidation(boolean isUpdate){
        boolean valid = false;
        
        Kategori selectedKategori   = (Kategori) cbxKategori.getSelectedItem();
        Merk selectedMerk           = (Merk) cbxMerk.getSelectedItem();
        Satuan selectedSatuan       = (Satuan) cbxSatuan.getSelectedItem();
        Supplier selectedSupplier   = (Supplier) cbxSupplier.getSelectedItem();
        
        if(txtBarangCode.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Kode barang tidak boleh kosong", getOptionAlert());
        }else if(txtBarcode.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Barcode tidak boleh kosong", getOptionAlert());
        }else if(txtBarangName.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Nama barang tidak boleh kosong", getOptionAlert());
        }else if(txtHargaJual.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Harga jual tidak boleh kosong", getOptionAlert());
        }else if(txtStokMinimum.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Stok minimum tidak boleh kosong", getOptionAlert());
        }else if(selectedKategori == null || selectedKategori.getKategoriID() == 0){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih kategori", getOptionAlert());
        }else if(selectedMerk == null || selectedMerk.getMerkID()== 0){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih merk", getOptionAlert());
        }else if(selectedSatuan == null || selectedSatuan.getSatuanID()== 0){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih satuan", getOptionAlert());
        }else if(selectedSupplier == null || selectedSupplier.getSupplierID()== 0){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih supplier", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private void insertData() {
        if(inputValidation(false)) {
            Barang modelBarang = new Barang();
            modelBarang.setBarangCode(txtBarangCode.getText());
            modelBarang.setBarcode(txtBarcode.getText());
            modelBarang.setBarangName(txtBarangName.getText());
            modelBarang.setHargaJual(NumericDocumentFilter.getNumericValue(txtHargaJual));
            modelBarang.setStokMinimum(NumericDocumentFilter.getNumericValueAsInt(txtStokMinimum));

            modelBarang.setKategori((Kategori) cbxKategori.getSelectedItem());
            modelBarang.setMerk((Merk) cbxMerk.getSelectedItem());
            modelBarang.setSatuan((Satuan) cbxSatuan.getSelectedItem());
            modelBarang.setSupplier((Supplier) cbxSupplier.getSelectedItem());

            if (filePath != null && !filePath.isEmpty()) {
                modelBarang.setPhoto(saveFileToProject(new File(filePath)));
            } else {
                modelBarang.setPhoto(null);
            }
            modelBarang.setInsertBy(loggedInUser.getUserID());
            
            boolean success = barangController.insertData(modelBarang);
            
            if (success) {
                    Toast.show(this, Toast.Type.SUCCESS,
                            "Data berhasil ditambahkan", getOptionAlert());
                formBarang.refreshTable();
                resetForm();
                generateBarangCode();
            } else {
                Toast.show(this, Toast.Type.ERROR,
                        "Gagal menambahkan data. Periksa kode/barcode yang duplikat atau data yang belum lengkap.", getOptionAlert());
            }
        }
    }

    private String saveFileToProject(File file){
        String projectDir = System.getProperty("user.dir");
        String folderName = "files";
        File destFolder = new File(projectDir, folderName);

        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }

        String appPrefix = "Barang_";
        String originalFileName = file.getName();

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); 
        String timestamp = String.valueOf(System.currentTimeMillis()); 
        String newFileName = appPrefix + timestamp + extension;

        File destFile = new File(destFolder, newFileName);
        try {
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return folderName + "/" + newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void loadData() {
        btnSave.setText("Update");
        
        barangID = model.getBarangID();
        txtBarangCode.setText(model.getBarangCode());
        txtBarcode.setText(model.getBarcode());
        txtBarangName.setText(model.getBarangName());
        
        
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        DecimalFormat df = new DecimalFormat("#,###", symbols);
        
        double hargaJual = model.getHargaJual();
        txtHargaJual.setText(df.format(hargaJual));
        
        txtStokMinimum.setText(String.valueOf(model.getStokMinimum()));
        
        cbxKategori.setSelectedItem(model.getKategori());
        cbxMerk.setSelectedItem(model.getMerk());
        cbxSatuan.setSelectedItem(model.getSatuan());
        cbxSupplier.setSelectedItem(model.getSupplier());
        
        filePath = model.getPhoto();
        setPhoto(filePath);
    }
    
    private void updateData() {
        if(inputValidation(true)) {
            Barang modelBarang = new Barang();
            modelBarang.setBarangID(barangID);
            modelBarang.setBarangCode(txtBarangCode.getText());
            modelBarang.setBarcode(txtBarcode.getText());
            modelBarang.setBarangName(txtBarangName.getText());
            modelBarang.setHargaJual(NumericDocumentFilter.getNumericValue(txtHargaJual));
            modelBarang.setStokMinimum(NumericDocumentFilter.getNumericValueAsInt(txtStokMinimum));

            modelBarang.setKategori((Kategori) cbxKategori.getSelectedItem());
            modelBarang.setMerk((Merk) cbxMerk.getSelectedItem());
            modelBarang.setSatuan((Satuan) cbxSatuan.getSelectedItem());
            modelBarang.setSupplier((Supplier) cbxSupplier.getSelectedItem());

            if (filePath != null && !filePath.isEmpty()) {
                modelBarang.setPhoto(saveFileToProject(new File(filePath)));
            } else {
                modelBarang.setPhoto(null);
            }
            modelBarang.setUpdateBy(loggedInUser.getUserID());

            boolean success = barangController.updateData(modelBarang);
            
            if (success) {
                    Toast.show(this, Toast.Type.SUCCESS,
                            "Data berhasil diperbarui", getOptionAlert());
                formBarang.refreshTable();
                resetForm();
                ModalDialog.closeModal("form update");
            } else {
                Toast.show(this, Toast.Type.ERROR,
                        "Gagal memperbarui data. Periksa kode/barcode yang duplikat atau data yang belum lengkap.", getOptionAlert());
            }
        }
    }

    private void resetForm() {
        txtBarangCode.setText("");
        txtBarcode.setText("");
        txtBarangName.setText("");
        txtHargaJual.setText("");
        txtStokMinimum.setText("");
        
        cbxKategori.setSelectedIndex(0);
        cbxMerk.setSelectedIndex(0);
        cbxSatuan.setSelectedIndex(0);
        cbxSupplier.setSelectedIndex(0);
        
        Icon icon = new FlatSVGIcon(AppResources.ICON_BASE + "Barang.svg", 80, 80);
        lbPhoto.setIcon(icon);
    }

    private void generateBarangCode() {
        txtBarangCode.setText(barangController.generateBarangCode());
        txtBarangCode.setEditable(false);
    }
    
}
