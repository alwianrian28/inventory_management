package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.SupplierController;
import com.inventory.form.FormSupplier;
import com.inventory.main.FormManager;
import com.inventory.model.Supplier;
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
public class FormInputSupplier extends JPanel{
    private static final long serialVersionUID = 1L;


    private JTextField txtSupplierName, txtTelepon, txtEmail, txtAlamat;
    private JButton btnSave, btnCancel;
   
    private final SupplierController controller = new SupplierController();
    private final Supplier model;
    private final FormSupplier formSupplier;
    private int SupplierID;
    private final User loggedInUser;
    
    public FormInputSupplier(Supplier model, FormSupplier formSupplier) {
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formSupplier = formSupplier;
        
        init();
        
        if(model != null){
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill, grow]"));
        this.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255)");
        
        txtSupplierName = new JTextField();
        txtSupplierName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama supplier");
        
        txtTelepon = new JTextField();
        txtTelepon.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nomor telepon");
        
        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan email");
        
        txtAlamat = new JTextField();
        txtAlamat.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan alamat");
        
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
        add(new JLabel("Nama Supplier"),"align right");
        add(txtSupplierName);
        add(new JLabel("Telepon"),"align right");
        add(txtTelepon);
        add(new JLabel("Email"),"align right");
        add(txtEmail);
        add(new JLabel("Alamat"),"align right");
        add(txtAlamat);
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

        if (txtSupplierName.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Nama supplier tidak boleh kosong", getOptionAlert());
            return false;
        }

        if (txtTelepon.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Nomor telepon tidak boleh kosong", getOptionAlert());
            return false;
        } else if (!txtTelepon.getText().trim().matches("\\d{10,15}")) {
            Toast.show(this, Toast.Type.INFO,
                    "Nomor telepon harus number (10–15 digits)", getOptionAlert());
            return false;
        }

        
        String email = txtEmail.getText().trim();
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";
        if (txtEmail.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Email tidak boleh kosong", getOptionAlert());
            return false;
        } else if (!email.matches(emailRegex)){
            Toast.show(this, Toast.Type.INFO,
                    "Invalid format email", getOptionAlert());
            return false;
        }
        
        if (txtAlamat.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Alamat tidak boleh kosong", getOptionAlert());
            return false;
        }

        return true;
    }

    
    private void insertData() {

        if (!inputValidation()) return;

        Supplier Supplier = new Supplier();
        Supplier.setSupplierName(txtSupplierName.getText().trim());
        Supplier.setTelepon(txtTelepon.getText().trim());
        Supplier.setEmail(txtEmail.getText().trim());
        Supplier.setAlamat(txtAlamat.getText().trim());
        Supplier.setInsertBy(loggedInUser.getUserID());

        boolean success = controller.insertData(Supplier);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil ditambahkan", getOptionAlert());
            formSupplier.refreshTable();
            resetForm();
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal menambahkan data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void loadData() {
        btnSave.setText("Update");
        
        SupplierID = model.getSupplierID();
        txtSupplierName.setText(model.getSupplierName());
        txtTelepon.setText(model.getTelepon());
        txtEmail.setText(model.getEmail());
        txtAlamat.setText(model.getAlamat());
    }
    
    private void updateData() {

        if (!inputValidation()) return;

        Supplier Supplier = new Supplier();
        Supplier.setSupplierID(SupplierID);
        Supplier.setSupplierName(txtSupplierName.getText().trim());
        Supplier.setTelepon(txtTelepon.getText().trim());
        Supplier.setEmail(txtEmail.getText().trim());
        Supplier.setAlamat(txtAlamat.getText().trim());
        Supplier.setUpdateBy(loggedInUser.getUserID());

        boolean success = controller.updateData(Supplier);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil diperbarui", getOptionAlert());
            formSupplier.refreshTable();
            resetForm();
            ModalDialog.closeModal("form update");
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal memperbarui data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void resetForm() {
        txtSupplierName.setText("");
        txtTelepon.setText("");
        txtEmail.setText("");
        txtAlamat.setText("");
    }
    
}
