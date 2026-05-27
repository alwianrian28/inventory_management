package com.inventory.main;

import com.inventory.form.FormDashboard;
import com.inventory.auth.FormAuth;
import com.inventory.model.User;
import com.inventory.util.About;
import java.awt.Color;
import javax.swing.JFrame;
import raven.modal.Drawer;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;
import raven.modal.demo.utils.UndoRedo;
import raven.modal.option.Option;

/**
 *
 * @author Dearclaudia
 */
public class FormManager {
    
    public static final UndoRedo<Form> FORMS = new UndoRedo<>();
    private static MainForm mainForm;
    private static JFrame frame;
    private static FormAuth formAuth;
    private static User loggedInUser;
    
    public static void install(JFrame f){
        frame = f;

        frame.getContentPane().removeAll();
        FormAuth login = getLogin();
        login.formCheck();
        frame.getContentPane().add(login);

        frame.repaint();
        frame.revalidate();
    }

    
    public static void showForm(Form form){
        if(form != FORMS.getCurrent()){
            FORMS.add(form);
            form.formCheck();
            form.formOpen();
            mainForm.setForm(form);
        }
    }
    
    public static void login(User user){
        loggedInUser = user;

        Drawer.installDrawer(frame, new Menu());
        Drawer.setVisible(true);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(getMainForm());

        Drawer.setSelectedItemClass(FormDashboard.class);

        frame.repaint();
        frame.revalidate();
    }

    
    public static User getLoggedInUser(){
        return loggedInUser;
    }
    
    public static void logout(){
        loggedInUser = null;

        Drawer.setVisible(false);

        frame.getContentPane().removeAll();
        FormAuth login = getLogin();
        login.formCheck();
        frame.getContentPane().add(login);

        FORMS.clear();
        mainForm = null;

        frame.repaint();
        frame.revalidate();
    }

    
    public static JFrame getJFrame(){
        return frame;
    }
    
    public static MainForm getMainForm(){
        if(mainForm == null){
            mainForm = new MainForm();
        }
        return mainForm;
    }
    
    private static FormAuth getLogin(){
        if(formAuth == null){
            formAuth = new FormAuth();
        }
        return formAuth;
    }
    
    public static void showAbout() {
        Option option = ModalDialog.createOption();
        option.setBackground(Color.BLACK);
        ModalDialog.showModal(frame, new SimpleModalBorder(new About(),"About"), option);
    }
}
