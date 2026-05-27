package com.inventory.auth;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.UserController;
import com.inventory.main.Form;
import com.inventory.main.FormManager;
import com.inventory.model.User;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppConfig;
import com.inventory.util.AppResources;
import com.inventory.util.PasswordFieldUtils;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
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
 * @author Alwian
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormLogin extends Form{
    private static final long serialVersionUID = 1L;

    
    private Runnable onRegisterClicked;
    
    private JLabel imageLogo;
    private JPanel mainPanel;
    private JPanel panelForm;
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    private final UserController controller = new UserController();
    
    public FormLogin() {
        init();
        setActionButton();
    }
    
    private void init(){
        setLayout(new MigLayout("fillx, insets 20","[center]","[center]"));
        
        mainPanel = new JPanel(new MigLayout("insets 50","[][]","[fill][grow]")){
//            @Override
//            protected void paintComponent(Graphics g) {
//                Graphics2D g2 = (Graphics2D) g.create();
//                int w = getWidth();
//                int h = getHeight();
//
//                Color c1 = new Color(12, 178, 145);
//                Color c2 = new Color(8, 54, 78);
//
//                GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2);
//                g2.setPaint(gp);
//                g2.fillRoundRect(0, 0, w, h, 20, 20);
//
//                g2.dispose();
//            }
        };
        mainPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;background:@accentColor");
        mainPanel.setOpaque(false);
        
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
        
        panelLogo.add(group,"gapx 30");
        
        panelForm = new JPanel(new MigLayout("wrap, insets 20","fill, 200:250"));
        panelForm.putClientProperty(FlatClientProperties.STYLE, "arc:20;background:rgb(255,255,255)");
        panelForm.setOpaque(false);
        
        JLabel lbTitleForm = new JLabel("Login",JLabel.CENTER);
        lbTitleForm.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor;font:bold +10");
        
        JLabel lbDescription = new JLabel("Please sign in to access your dashboard",JLabel.CENTER);
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");
        
        JLabel lbUsername = new JLabel("Username");
        lbUsername.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");
        
        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon(AppResources.ICON_BASE + "username.svg", 20, 20));
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JLabel lbPassword = new JLabel("Password");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");
        
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon(AppResources.ICON_BASE + "password.svg", 20, 20));
        txtPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "showRevealButton:true;"
                + "showCapsLock:true");
        
        btnLogin = new JButton("Login");
        btnLogin.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:rgb(255,255,255);"
                + "background:@accentColor;"
                + "arc:10;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "font:bold 16");
        btnLogin.addActionListener((e) -> {
            doLogin();
        });
        
        btnRegister = new JButton("Sign Up");
        btnRegister.setBorderPainted(false);
        btnRegister.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(235, 102, 21)");
        btnRegister.addActionListener((e) -> {
            if (controller.isUserRegistered()) {
                Toast.show(this, Toast.Type.INFO, 
                    "Application registration is restricted to admin only", 
                    getOptionAlert());
            } else {
                if (onRegisterClicked != null) {
                    onRegisterClicked.run();
                }
            }
        });
        
        panelForm.add(lbTitleForm);
        panelForm.add(lbDescription);
        panelForm.add(lbUsername,"gapy 8");
        panelForm.add(txtUsername,"hmin 30");
        panelForm.add(lbPassword,"gapy 8");
        panelForm.add(txtPassword,"hmin 30");
        panelForm.add(btnLogin,"hmin 30, gapy 15");
        panelForm.add(new JSeparator(), "gapy 15 15");
        panelForm.add(new JLabel("Don't have an account ?"), "split 2,gapx push n");
        panelForm.add(btnRegister, "gapx n push");
        
        mainPanel.add(panelForm);
        mainPanel.add(panelLogo);
        
        add(mainPanel);
    }
    
    public void setOnRegisterClicked(Runnable action) {
        this.onRegisterClicked = action;
    }
    
    private void setActionButton(){
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    doLogin();
                }
            }
        });
        
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    doLogin();
                }
            }
        });
    }
    
    private boolean validasiInput(){
        boolean valid = false;
        
        if(txtUsername.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Please enter the username", getOptionAlert());
            txtUsername.putClientProperty("JComponent.outline", "error");
        }else if(PasswordFieldUtils.isBlank(txtPassword)){
            Toast.show(this, Toast.Type.INFO, "Please enter the password", getOptionAlert());
            txtPassword.putClientProperty("JComponent.outline", "error");
        }else{
            valid = true;
            txtUsername.putClientProperty("JComponent.outline", null);
            txtPassword.putClientProperty("JComponent.outline", null);
        }
        
        return valid;
    }
    
    private void doLogin(){
        if(validasiInput() == true){
            String username = txtUsername.getText();
            String password = PasswordFieldUtils.getPassword(txtPassword);
            
            User modelUser = new User();
            modelUser.setUsername(username);
            modelUser.setPassword(password);
            
            User user = controller.login(modelUser);
            if(user != null){
                FormManager.login(user);
                resetForm();
            }else{
                Toast.show(this, Toast.Type.ERROR, "Incorrect username or password. Please try again", getOptionAlert());
            }
        }
    }
    
    private void resetForm() {
        txtUsername.setText("");
        txtPassword.setText("");
    }
}
