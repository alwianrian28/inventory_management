package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.UserController;
import com.inventory.form.FormUser;
import com.inventory.main.FormManager;
import com.inventory.model.User;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.PasswordFieldUtils;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
public class FormInputUser extends JPanel{
    private static final long serialVersionUID = 1L;


    private JTextField txtName, txtEmail, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox cbxRole;
    private JButton btnSave, btnCancel;
   
    private JLabel lbPassword;
    
    private final UserController controller = new UserController();
    private final User model;
    private final FormUser formUser;
    private int userID;
    private final User loggedInUser;
    
    public FormInputUser(User model, FormUser formUser) {
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formUser = formUser;
        
        init();
        
        if(model != null){
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill, grow]"));
        this.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255)");
        
        txtName = new JTextField();
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama");
        
        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan email");
        
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan username");
        
        lbPassword = new JLabel("Password");
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true");
        
        cbxRole = new JComboBox();
        cbxRole.addItem("Pilih Role");
        cbxRole.addItem("Admin");
        cbxRole.addItem("Staff");
        cbxRole.addItem("Gudang");
        
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
        add(new JLabel("Nama"),"align right");
        add(txtName);
        add(new JLabel("Email"),"align right");
        add(txtEmail);
        add(new JLabel("Username"),"align right");
        add(txtUsername);
        add(lbPassword,"align right, hidemode 3");
        add(txtPassword,", hidemode 3");
        add(new JLabel("Role"),"align right");
        add(cbxRole);
        add(createSeparator(),"span, growx, height 2!");
        add(btnSave,"span, split 2, align center, sg btn, hmin 30");
        add(btnCancel,"sg btn, hmin 30");
    }
    
    private JSeparator createSeparator(){
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private boolean inputValidation(boolean validatePassword, boolean isUpdate){
        boolean valid = false;
        String email = txtEmail.getText().trim();
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";
        
        if(txtName.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Nama tidak boleh kosong", getOptionAlert());
        }else if(txtEmail.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Email tidak boleh kosong", getOptionAlert());
        }else if(!email.matches(emailRegex)){
            Toast.show(this, Toast.Type.INFO, "Format email tidak valid", getOptionAlert());
        }else if(txtUsername.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Username tidak boleh kosong", getOptionAlert());
        }else if(validatePassword && PasswordFieldUtils.isBlank(txtPassword)){
            Toast.show(this, Toast.Type.INFO, "Password tidak boleh kosong", getOptionAlert());
        }else if(cbxRole.getSelectedItem().equals("Pilih Role")){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih role", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private void insertData(){
        if(inputValidation(true, false)){
            String name     = txtName.getText();
            String email    = txtEmail.getText();
            String username = txtUsername.getText();
            String password = PasswordFieldUtils.getPassword(txtPassword);
            String role     = cbxRole.getSelectedItem().toString();
            
            User modelUser = new User();
            modelUser.setName(name);
            modelUser.setEmail(email);
            modelUser.setUsername(username);
            modelUser.setPassword(password);
            modelUser.setRole(role);
            modelUser.setInsertBy(loggedInUser.getUserID());
            
            boolean success = controller.insertData(modelUser);

            if (success) {
                Toast.show(this, Toast.Type.SUCCESS,
                        "Data berhasil ditambahkan", getOptionAlert());
                formUser.refreshTable();
                resetForm();
            } else {
                Toast.show(this, Toast.Type.WARNING,
                        "Gagal menambahkan data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
            }
        }
    }

    private void loadData() {
        btnSave.setText("Update");
        
        lbPassword.setVisible(false);
        txtPassword.setVisible(false);
        
        userID = model.getUserID();
        txtName.setText(model.getName());
        txtEmail.setText(model.getEmail());
        txtUsername.setText(model.getUsername());
        cbxRole.setSelectedItem(model.getRole());
    }
    
    private void updateData() {
        if(inputValidation(false,true)){
            String name     = txtName.getText();
            String email    = txtEmail.getText();
            String username = txtUsername.getText();
            String password = PasswordFieldUtils.getPassword(txtPassword);
            String role     = cbxRole.getSelectedItem().toString();
            
            User modelUser = new User();
            modelUser.setUserID(userID);
            modelUser.setName(name);
            modelUser.setEmail(email);
            modelUser.setUsername(username);
            modelUser.setPassword(password);
            modelUser.setRole(role);
            modelUser.setUpdateBy(loggedInUser.getUserID());
            
            boolean success = controller.updateData(modelUser);

            if (success) {
                Toast.show(this, Toast.Type.SUCCESS,
                        "Data berhasil diperbarui", getOptionAlert());
                formUser.refreshTable();
                resetForm();
                ModalDialog.closeModal("form update");
            } else {
                Toast.show(this, Toast.Type.WARNING,
                        "Gagal memperbarui data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
            }
        }
    }

    private void resetForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbxRole.setSelectedIndex(0);
    }
    
}
