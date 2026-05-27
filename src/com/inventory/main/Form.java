package com.inventory.main;

import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Dearclaudia
 */
public class Form extends JPanel {

    private static final long serialVersionUID = 1L;

    private LookAndFeel oldTheme = UIManager.getLookAndFeel();

    public Form() {
        init();
    }
    
    private void init(){
        
    }

    public void formInit(){
        
    }
    
    public void formOpen(){
        
    }
    
    protected final void formCheck(){
        if(oldTheme != UIManager.getLookAndFeel()){
            oldTheme = UIManager.getLookAndFeel();
            SwingUtilities.updateComponentTreeUI(this);
        }
    }
}
