package com.inventory.auth;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import com.inventory.main.Form;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;
import raven.extras.SlidePane;
import raven.extras.SlidePaneTransition;

/**
 *
 * @author Dearclaudia
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormAuth extends Form {
    private static final long serialVersionUID = 1L;


    private SlidePane slidePane;

    private final FormLogin formLogin = new FormLogin();
    private final FormRegister formRegister = new FormRegister();

    public FormAuth() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill","[center]","[center]"));
        
        slidePane = new SlidePane((container, component) -> minSize(container, component));
        slidePane.addSlide(formLogin);
        add(slidePane);
        
        formLogin.setOnRegisterClicked(() -> {
            slidePane.addSlide(formRegister, SlidePaneTransition.Type.FORWARD,1000);
        });
        
        formRegister.setOnLoginClicked(() -> {
            slidePane.addSlide(formLogin, SlidePaneTransition.Type.BACK,1000);
        });
    }

    private Dimension minSize(Container container, Component component) {
        Container parent = container.getParent();
        Dimension comSize = component.getPreferredSize();
        Dimension parentSize = parent.getSize();
        Insets parentInsets = FlatUIUtils.addInsets(parent.getInsets(), getMiglayoutDefaultInsets());
        int width = Math.min(comSize.width, parentSize.width - (parentInsets.left + parentInsets.right));
        int height = Math.min(comSize.height, parentSize.height - (parentInsets.top + parentInsets.bottom));
        return new Dimension(width, height);
    }

    private Insets getMiglayoutDefaultInsets() {
        int top = (int) PlatformDefaults.getPanelInsets(0).getValue();
        int left = (int) PlatformDefaults.getPanelInsets(1).getValue();
        int bottom = (int) PlatformDefaults.getPanelInsets(2).getValue();
        int right = (int) PlatformDefaults.getPanelInsets(3).getValue();
        return UIScale.scale(new Insets(top, left, bottom, right));
    }
}

