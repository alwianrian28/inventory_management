package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.GudangController;
import com.inventory.form.FormGudang;
import com.inventory.main.FormManager;
import com.inventory.model.Gudang;
import com.inventory.model.User;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
public class FormInputGudang extends JPanel{
    private static final long serialVersionUID = 1L;


    private JTextField txtGudangCode, txtGudangName, txtAlamat, txtKeterangan;
    private JButton btnSave, btnCancel;
   
    private final GudangController controller = new GudangController();
    private final Gudang model;
    private final FormGudang formGudang;
    private int GudangID;
    private final User loggedInUser;
    
    public FormInputGudang(Gudang model, FormGudang formGudang) {
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formGudang = formGudang;
        
        init();
        
        if(model != null){
            loadData();
        }else{
            generateGudangCode();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill, grow]"));
        this.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255)");
        
        txtGudangCode = new JTextField();
        txtGudangCode.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan kode gudang");
        
        txtGudangName = new JTextField();
        txtGudangName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama gudang");
        
        txtAlamat = new JTextField();
        txtAlamat.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan alamat");
        
        txtKeterangan = new JTextField();
        txtKeterangan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan keterangan");
        
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
        add(new JLabel("Kode Gudang"),"align right");
        add(txtGudangCode);
        add(new JLabel("Nama Gudang"),"align right");
        add(txtGudangName);
        add(new JLabel("Alamat"),"align right");
        add(txtAlamat);
        add(new JLabel("Keterangan"),"align right");
        add(txtKeterangan);
        add(createSeparator(),"span, growx, height 2!");
        add(btnSave,"span, split 2, align center, sg btn, hmin 30");
        add(btnCancel,"sg btn, hmin 30");
    }
    
    private JSeparator createSeparator(){
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private boolean inputValidation() {

        if (txtGudangCode.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Kode gudang tidak boleh kosong", getOptionAlert());
            return false;
        }
        
        if (txtGudangName.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Nama gudang tidak boleh kosong", getOptionAlert());
            return false;
        }
        
        if (txtAlamat.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Alamat tidak boleh kosong", getOptionAlert());
            return false;
        }

        if (txtKeterangan.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Keterangan tidak boleh kosong", getOptionAlert());
            return false;
        }

        return true;
    }

    
    private void insertData() {

        if (!inputValidation()) return;

        Gudang Gudang = new Gudang();
        Gudang.setGudangCode(txtGudangCode.getText().trim());
        Gudang.setGudangName(txtGudangName.getText().trim());
        Gudang.setAlamat(txtAlamat.getText().trim());
        Gudang.setKeterangan(txtKeterangan.getText().trim());
        Gudang.setInsertBy(loggedInUser.getUserID());

        boolean success = controller.insertData(Gudang);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil ditambahkan", getOptionAlert());
            formGudang.refreshTable();
            resetForm();
            generateGudangCode();
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal menambahkan data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void loadData() {
        btnSave.setText("Update");
        
        GudangID = model.getGudangID();
        txtGudangCode.setText(model.getGudangCode());
        txtGudangName.setText(model.getGudangName());
        txtAlamat.setText(model.getAlamat());
        txtKeterangan.setText(model.getKeterangan());
        
        txtGudangCode.setEditable(false);
    }
    
    private void updateData() {

        if (!inputValidation()) return;

        Gudang Gudang = new Gudang();
        Gudang.setGudangID(GudangID);
        Gudang.setGudangCode(txtGudangCode.getText().trim());
        Gudang.setGudangName(txtGudangName.getText().trim());
        Gudang.setAlamat(txtAlamat.getText().trim());
        Gudang.setKeterangan(txtKeterangan.getText().trim());
        Gudang.setUpdateBy(loggedInUser.getUserID());

        boolean success = controller.updateData(Gudang);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil diperbarui", getOptionAlert());
            formGudang.refreshTable();
            resetForm();
            ModalDialog.closeModal("form update");
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal memperbarui data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void resetForm() {
        txtGudangCode.setText("");
        txtGudangName.setText("");
        txtAlamat.setText("");
        txtKeterangan.setText("");
    }

    private void generateGudangCode() {
        txtGudangCode.setText(controller.generateGudangCode());
        txtGudangCode.setEditable(false);

    }
    
}
