package com.inventory.form.input;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;

/**
 *
 * @author Dearclaudia
 */
public class StockInfoDialog extends JDialog {

    private JPanel overlay;

    public StockInfoDialog(Frame parent, String title, boolean modal) {
        super(parent, title, modal);
        setOverlay(parent);
    }
    
    private void setOverlay(Frame parent){
        RootPaneContainer rpc = (RootPaneContainer) parent;

        overlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 150));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        overlay.setOpaque(false);

        overlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        rpc.setGlassPane(overlay);
    }
    
    @Override
    public void setVisible(boolean b) {
        Window parent = getOwner();

        if (b) {
            if (parent instanceof RootPaneContainer) {
                ((RootPaneContainer) parent).getGlassPane().setVisible(true);
            }
        }

        super.setVisible(b);

        if (!b) {
            if (parent instanceof RootPaneContainer) {
                ((RootPaneContainer) parent).getGlassPane().setVisible(false);
                parent.repaint();
            }
        }
    }

    @Override
    public void dispose() {
        Window parent = getOwner();
        if (parent instanceof RootPaneContainer) {
            ((RootPaneContainer) parent).getGlassPane().setVisible(false);
            parent.repaint();
        }
        super.dispose();
    }
}
