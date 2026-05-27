package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.RakController;
import com.inventory.form.FormRak;
import com.inventory.main.FormManager;
import com.inventory.model.Rak;
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
public class FormInputRak extends JPanel{
    private static final long serialVersionUID = 1L;


    private JTextField txtRakCode, txtRakName, txtKeterangan;
    private JButton btnSave, btnCancel;
   
    private final RakController controller = new RakController();
    private final Rak model;
    private final FormRak formRak;
    private int RakID;
    private final User loggedInUser;
    
    public FormInputRak(Rak model, FormRak formRak) {
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formRak = formRak;
        
        init();
        
        if(model != null){
            loadData();
        }else{
            generateRakCode();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill, grow]"));
        this.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255)");
        
        txtRakCode = new JTextField();
        txtRakCode.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan kode rak");
        
        txtRakName = new JTextField();
        txtRakName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama rak");
        
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
        add(new JLabel("Kode Rak"),"align right");
        add(txtRakCode);
        add(new JLabel("Nama Rak"),"align right");
        add(txtRakName);
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

        if (txtRakCode.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Kode rak tidak boleh kosong", getOptionAlert());
            return false;
        }
        
        if (txtRakName.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Nama rak tidak boleh kosong", getOptionAlert());
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

        Rak Rak = new Rak();
        Rak.setRakCode(txtRakCode.getText().trim());
        Rak.setRakName(txtRakName.getText().trim());
        Rak.setKeterangan(txtKeterangan.getText().trim());
        Rak.setInsertBy(loggedInUser.getUserID());

        boolean success = controller.insertData(Rak);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil ditambahkan", getOptionAlert());
            formRak.refreshTable();
            resetForm();
            generateRakCode();
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal menambahkan data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void loadData() {
        btnSave.setText("Update");
        
        RakID = model.getRakID();
        txtRakCode.setText(model.getRakCode());
        txtRakName.setText(model.getRakName());
        txtKeterangan.setText(model.getKeterangan());
        
        txtRakCode.setEditable(false);
    }
    
    private void updateData() {

        if (!inputValidation()) return;

        Rak Rak = new Rak();
        Rak.setRakID(RakID);
        Rak.setRakCode(txtRakCode.getText().trim());
        Rak.setRakName(txtRakName.getText().trim());
        Rak.setKeterangan(txtKeterangan.getText().trim());
        Rak.setUpdateBy(loggedInUser.getUserID());

        boolean success = controller.updateData(Rak);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil diperbarui", getOptionAlert());
            formRak.refreshTable();
            resetForm();
            ModalDialog.closeModal("form update");
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal memperbarui data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void resetForm() {
        txtRakCode.setText("");
        txtRakName.setText("");
        txtKeterangan.setText("");
    }

    private void generateRakCode() {
        txtRakCode.setText(controller.generateRakCode());
        txtRakCode.setEditable(false);

    }
    
}
