package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.MerkController;
import com.inventory.form.FormMerk;
import com.inventory.main.FormManager;
import com.inventory.model.Merk;
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
public class FormInputMerk extends JPanel{
    private static final long serialVersionUID = 1L;


    private JTextField txtMerkName, txtKeterangan;
    private JButton btnSave, btnCancel;
   
    private final MerkController controller = new MerkController();
    private final Merk model;
    private final FormMerk formMerk;
    private int MerkID;
    private final User loggedInUser;
    
    public FormInputMerk(Merk model, FormMerk formMerk) {
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formMerk = formMerk;
        
        init();
        
        if(model != null){
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill, grow]"));
        this.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255)");
        
        txtMerkName = new JTextField();
        txtMerkName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama merk");
        
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
        add(new JLabel("Nama Merk"),"align right");
        add(txtMerkName);
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

        if (txtMerkName.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Nama merk tidak boleh kosong", getOptionAlert());
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

        Merk Merk = new Merk();
        Merk.setMerkName(txtMerkName.getText().trim());
        Merk.setKeterangan(txtKeterangan.getText().trim());
        Merk.setInsertBy(loggedInUser.getUserID());

        boolean success = controller.insertData(Merk);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil ditambahkan", getOptionAlert());
            formMerk.refreshTable();
            resetForm();
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal menambahkan data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void loadData() {
        btnSave.setText("Update");
        
        MerkID = model.getMerkID();
        txtMerkName.setText(model.getMerkName());
        txtKeterangan.setText(model.getKeterangan());
    }
    
    private void updateData() {

        if (!inputValidation()) return;

        Merk Merk = new Merk();
        Merk.setMerkID(MerkID);
        Merk.setMerkName(txtMerkName.getText().trim());
        Merk.setKeterangan(txtKeterangan.getText().trim());
        Merk.setUpdateBy(loggedInUser.getUserID());

        boolean success = controller.updateData(Merk);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil diperbarui", getOptionAlert());
            formMerk.refreshTable();
            resetForm();
            ModalDialog.closeModal("form update");
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal memperbarui data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void resetForm() {
        txtMerkName.setText("");
        txtKeterangan.setText("");
    }
    
}
