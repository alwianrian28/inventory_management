package com.inventory.auth;

import com.formdev.flatlaf.FlatClientProperties;
import com.inventory.controller.UserController;
import com.inventory.main.Form;
import com.inventory.main.FormManager;
import com.inventory.model.User;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppConfig;
import com.inventory.util.PasswordFieldUtils;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormRegister extends Form{
    private static final long serialVersionUID = 1L;

    
    private Runnable onLoginClicked;
    
    private JLabel imageLogo;
    private JPanel mainPanel;
    private JPanel panelForm;
    
    private JTextField txtName, txtEmail, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox cbxRole;
    
    private JButton btnRegistrasi, btnLogin;

    private final UserController controller = new UserController();
    
    public FormRegister() {
        init();
        setActionButton();
    }
    
    private void init(){
        setLayout(new MigLayout("fill, insets 20","[center]","[center]"));
        
        mainPanel = new JPanel(new MigLayout("insets 50","[][]","[fill][grow]"));
        mainPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;background:@accentColor");
        
        JPanel panelLogo = new JPanel(new MigLayout("fill", "[center]", "[center]"));
        panelLogo.setOpaque(false);
        
        imageLogo = new JLabel();
        AvatarIcon icon = new AvatarIcon(getClass().getResource("/com/inventory/assets/Logo1.png"), 120, 120, 0);
        imageLogo.setIcon(icon);
        
        JLabel lbTitleLogo = new JLabel(AppConfig.get("app.name"));
        lbTitleLogo.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:rgb(255,255,255);"
                + "font:bold +14 Roboto");
        
        JLabel lbDetail = new JLabel(AppConfig.get("app.description"));
        lbDetail.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:rgb(255,255,255);"
                + "font:14 Roboto");
        
        JPanel group = new JPanel(new MigLayout("wrap", "300, center", "[]10[]5[]"));
        group.setOpaque(false);
        group.add(imageLogo);
        group.add(lbTitleLogo);
        group.add(lbDetail);
        
        panelLogo.add(group,"gapx 0 30");
        
        panelForm = new JPanel(new MigLayout("wrap, insets 20","fill, 350:250"));
        panelForm.putClientProperty(FlatClientProperties.STYLE, "arc:20;background:rgb(255,255,255)");
        panelForm.setOpaque(false);
        
        JLabel lbTitleForm = new JLabel("Sign up",JLabel.CENTER);
        lbTitleForm.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor;font:bold +10");
        
        JLabel lbDescription = new JLabel("Sign up to access the Inventory Management System",JLabel.CENTER);
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");
        
        txtName = new JTextField();
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nama");
        
        txtEmail = new JTextField();
        txtEmail.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Email");
        
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        txtPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true");
        
        cbxRole = new JComboBox();
        initComboItem(cbxRole);
        
        btnRegistrasi = new JButton("Sign up");
        btnRegistrasi.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:rgb(255,255,255);"
                + "background:@accentColor;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "font:bold 16");
        btnRegistrasi.addActionListener((e) -> {
            doRegister();
        });
        
        btnLogin = new JButton("Login");
        btnLogin.setBorderPainted(false);
        btnLogin.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(235, 102, 21)");
        btnLogin.addActionListener((e) -> {
            if (onLoginClicked != null) {
                onLoginClicked.run();
            }
        });
        
        panelForm.add(lbTitleForm);
        panelForm.add(lbDescription);
        panelForm.add(new JLabel("Nama"),"gapy 8");
        panelForm.add(txtName,"hmin 30");
        panelForm.add(new JLabel("Email"),"gapy 8");
        panelForm.add(txtEmail,"hmin 30");
        panelForm.add(new JLabel("Username"),"gapy 8");
        panelForm.add(txtUsername,"hmin 30");
        panelForm.add(new JLabel("Password"),"gapy 8");
        panelForm.add(txtPassword,"hmin 30");
        panelForm.add(new JLabel("Role"),"gapy 8");
        panelForm.add(cbxRole,"hmin 30");
        panelForm.add(btnRegistrasi,"hmin 30, gapy 15");
        panelForm.add(new JSeparator(), "gapy 15 15");
        panelForm.add(new JLabel("Already have an account ?"), "split 2,gapx push n");
        panelForm.add(btnLogin, "gapx n push");
        
        mainPanel.add(panelLogo);
        mainPanel.add(panelForm);
        
        add(mainPanel);
    }
    
    public void setOnLoginClicked(Runnable action) {
        this.onLoginClicked = action;
    }

    private void setActionButton(){
        txtName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    doRegister();
                }
            }
        });
        txtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    doRegister();
                }
            }
        });
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    doRegister();
                }
            }
        });
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    doRegister();
                }
            }
        });
        cbxRole.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    doRegister();
                }
            }
        });
    }
    
    private void initComboItem(JComboBox cbxRole) {
        cbxRole.addItem("Admin");
        cbxRole.setSelectedIndex(0);
        cbxRole.setEnabled(false);
    }
    
    private boolean validasiInput(){
        boolean valid = false;
        String email = txtEmail.getText().trim();
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";
        
        if(txtName.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Please enter the name", getOptionAlert());
        }else if(txtEmail.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Please enter the email", getOptionAlert());
        }else if(!email.matches(emailRegex)){
            Toast.show(this, Toast.Type.INFO, "Invalid email format", getOptionAlert());
        }else if(txtUsername.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Please enter the username", getOptionAlert());
        }else if(PasswordFieldUtils.isBlank(txtPassword)){
            Toast.show(this, Toast.Type.INFO, "Please enter the password", getOptionAlert());
        }else if(cbxRole.getSelectedItem().equals("Select Role")){
            Toast.show(this, Toast.Type.INFO, "Please select a role", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private void doRegister(){
        if(validasiInput()==true){
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
            
            boolean success = controller.insertData(modelUser);
            
            if (success) {
                Toast.show(this, Toast.Type.SUCCESS, "Registration completed successfully!", getOptionAlert());
                onLoginClicked.run();
                resetForm();
                FormManager.logout();
            } else {
                Toast.show(this, Toast.Type.ERROR, "Registration failed!", getOptionAlert());
            }
        }
    }
    
    private void resetForm(){
        txtName.setText("");
        txtEmail.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbxRole.setSelectedIndex(0);
    }
}
