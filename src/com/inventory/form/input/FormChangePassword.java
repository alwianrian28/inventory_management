package com.inventory.form.input;

import com.inventory.model.User;
import com.formdev.flatlaf.FlatClientProperties;
import com.inventory.main.Form;
import com.inventory.main.FormManager;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.UserController;
import com.inventory.util.AppResources;
import com.inventory.util.PasswordFieldUtils;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormChangePassword extends Form{
    private static final long serialVersionUID = 1L;


    private final UserController controller = new UserController();
    
    private final JTextField txtUsername = new JTextField();
    private JPasswordField txtOldPassword, txtNewPassword, txtConfirmPassword;
    
    private final User loggedInUser;
    
    public FormChangePassword() {
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
        imageLogo.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "password.svg", 0.8f));
        
        JLabel lbTItle = new JLabel("Pergantian Password");
        lbTItle.putClientProperty(FlatClientProperties.STYLE, "font:bold 17");
        
        JLabel lbBreadcrumb = new JLabel("OTHER > Manajemen User > Pergantian Password");
        lbBreadcrumb.putClientProperty(FlatClientProperties.STYLE, "font:14;foreground:rgb(120,120,120)");
        
        panel.add(imageLogo);
        panel.add(lbTItle);
        panel.add(lbBreadcrumb);
        return panel;
    }
    
    private void reloadLoggedInUser(){
        User updatedUser = controller.getUserById(loggedInUser.getUserID());
        loggedInUser.setUsername(updatedUser.getUsername());
    }
    
     private void loadData(){
        txtUsername.setEditable(false);
        txtUsername.setText(loggedInUser.getUsername());
        txtOldPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
    }
    
    private JPanel setButton(){
        JPanel panel = new JPanel(new MigLayout("wrap, insets 20, gap 20","[50][400, fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
        
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the username");
        
        txtOldPassword = new JPasswordField();
        txtOldPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the old password");
        txtOldPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true;");
        
        txtNewPassword = new JPasswordField();
        txtNewPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the new password");
        txtNewPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true;");
        
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the confirm password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true;");
        
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
        
        panel.add(new JLabel("Username"),"align right");
        panel.add(txtUsername);
        panel.add(new JLabel("Password Lama"),"align right");
        panel.add(txtOldPassword, "hmin 30");
        panel.add(new JLabel("Password Baru"),"align right");
        panel.add(txtNewPassword, "hmin 30");
        panel.add(new JLabel("Konfirmasi Password Baru"),"align right");
        panel.add(txtConfirmPassword, "hmin 30");
        //panel.add(createSeparator(), "span, growx, height 2!");
        panel.add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        panel.add(btnCancel, "sg btn, hmin 30");
        return panel;
    }
    
    private boolean validationInput() {
        boolean valid = false;
        if (PasswordFieldUtils.isBlank(txtOldPassword)){
            Toast.show(this, Toast.Type.INFO, "Password lama tidak boleh kosong", getOptionAlert());
        }else if (PasswordFieldUtils.isBlank(txtNewPassword)){
            Toast.show(this, Toast.Type.INFO, "Password baru tidak boleh kosong", getOptionAlert());
        }else if (PasswordFieldUtils.isBlank(txtConfirmPassword)){
            Toast.show(this, Toast.Type.INFO, "Konfirmasi password baru tidak boleh kosong", getOptionAlert());
        }else{
            valid = true;
        }
        return valid;
    }
    
    private void updateData(){
        if(validationInput()==true){
            String username = txtUsername.getText();
            String oldPassword = PasswordFieldUtils.getPassword(txtOldPassword);
            String newPassword = PasswordFieldUtils.getPassword(txtNewPassword);
            String confirmPassword = PasswordFieldUtils.getPassword(txtConfirmPassword);

            boolean validOldPassword = controller.validateOldPassword(username, oldPassword);
            if (!validOldPassword) {
                Toast.show(this, Toast.Type.ERROR, "Password lama tidak sesuai", getOptionAlert());
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                Toast.show(this, Toast.Type.ERROR, "Password baru dan konfirmasi password baru tidak sesuai", getOptionAlert());
                return;
            }

            boolean result = controller.changePassword(username, oldPassword, newPassword);

            if (result) {
                Toast.show(this, Toast.Type.SUCCESS, "Password berhasil diperbarui", getOptionAlert());
            } else {
                Toast.show(this, Toast.Type.ERROR, "Gagal mengganti password", getOptionAlert());
            }

            reloadLoggedInUser();
            loadData();
        }
    }
}
