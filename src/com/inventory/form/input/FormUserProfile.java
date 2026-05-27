package com.inventory.form.input;

import com.inventory.model.User;
import com.formdev.flatlaf.FlatClientProperties;
import com.inventory.main.Form;
import com.inventory.main.FormManager;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.UserController;
import com.inventory.util.AppResources;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormUserProfile extends Form{
    private static final long serialVersionUID = 1L;


    private final UserController controller = new UserController();
    
    private final JTextField txtName = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtUsername = new JTextField();
    private final JComboBox cbxRole = new JComboBox();
    
    private final User loggedInUser;
    
    public FormUserProfile() {
        this.loggedInUser = FormManager.getLoggedInUser();
        init();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]","[][][fill, grow]"));
        add(setInfo());
        add(createSeparator(), "span, growx, height 2!, gapx 10 10");
        add(setButton());
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }
    
    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill","[][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JLabel imageLogo = new JLabel();
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "user.svg", 0.4f));
        
        JLabel lbTItle = new JLabel("Profile");
        lbTItle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumb = new JLabel("OTHER > Manajemen User > Profile");
        lbBreadcrumb.putClientProperty(FlatClientProperties.STYLE, "font:14;foreground:rgb(120,120,120)");
        
        panel.add(imageLogo);
        panel.add(lbTItle);
        panel.add(lbBreadcrumb);
        return panel;
    }
    
    private void reloadLoggedInUser(){
        User updatedUser = controller.getUserById(loggedInUser.getUserID());
        loggedInUser.setName(updatedUser.getName());
        loggedInUser.setEmail(updatedUser.getEmail());
        loggedInUser.setUsername(updatedUser.getUsername());
        loggedInUser.setRole(updatedUser.getRole());
    }
    
     private void loadData(){
        txtName.setText(loggedInUser.getName());
        txtEmail.setText(loggedInUser.getEmail());
        txtUsername.setText(loggedInUser.getUsername());
        cbxRole.setSelectedItem(loggedInUser.getRole());
    }
    
    private JPanel setButton(){
        JPanel panel = new JPanel(new MigLayout("wrap, insets 20, gap 20","[50][400, fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
        
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama");
        
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan email");
        
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan username");
        
        cbxRole.addItem("Pilih Role");
        cbxRole.addItem("Admin");
        cbxRole.addItem("Staff");
        cbxRole.addItem("Kasir");
        cbxRole.setSelectedItem(loggedInUser.getRole());
                
        JButton btnSave = new JButton("Save");
        btnSave.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);");
        btnSave.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "save_white.svg", 0.8f));
        btnSave.setIconTextGap(5);
        btnSave.addActionListener((e) -> updateData());
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "cancel.svg", 0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.addActionListener((e) -> loadData());
        
        panel.add(new JLabel("Nama"),"align right");
        panel.add(txtName);
        panel.add(new JLabel("Email"),"align right");
        panel.add(txtEmail);
        panel.add(new JLabel("Username"),"align right");
        panel.add(txtUsername);
        panel.add(new JLabel("Role"),"align right");
        panel.add(cbxRole);
        //panel.add(createSeparator(), "span, growx, height 2!");
        panel.add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        panel.add(btnCancel, "sg btn, hmin 30");
        return panel;
    }
    
    private boolean inputValidation(boolean isUpdate){
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
        }else if(cbxRole.getSelectedItem().equals("Pilih Role")){
            Toast.show(this, Toast.Type.INFO, "Silahkan pilih role", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private void updateData(){
        if(inputValidation(true)){
            String nama     = txtName.getText();
            String email    = txtEmail.getText();
            String username = txtUsername.getText();
            String role     = cbxRole.getSelectedItem().toString();
            
            User modelUser = new User();
            modelUser.setUserID(loggedInUser.getUserID());
            modelUser.setName(nama);
            modelUser.setEmail(email);
            modelUser.setUsername(username);
            modelUser.setRole(role);
            
            boolean success = controller.updateData(modelUser);

            if (success) {
                Toast.show(this, Toast.Type.SUCCESS,
                        "Data berhasil diperbarui", getOptionAlert());
                reloadLoggedInUser();
            } else {
                Toast.show(this, Toast.Type.WARNING,
                        "Gagal memperbarui data.\nPeriksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
            }
        }
    }
}
