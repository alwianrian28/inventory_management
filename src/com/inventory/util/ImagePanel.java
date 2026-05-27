package com.inventory.util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Dearclaudia
 */
public class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;


    private Image image;
    private int imgW, imgH;

    public ImagePanel(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            image = icon.getImage();
            imgW = icon.getIconWidth();
            imgH = icon.getIconHeight();
        }
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) return;

        int panelW = getWidth();
        int panelH = getHeight();

        double imgRatio = (double) imgW / imgH;
        double panelRatio = (double) panelW / panelH;

        int drawW, drawH;

        // 🔥 JIKA GAMBAR KECIL → CONTAIN
        if (imgW <= panelW && imgH <= panelH) {
            drawW = imgW;
            drawH = imgH;
        }
        // 🔥 JIKA GAMBAR BESAR → COVER
        else {
            if (imgRatio > panelRatio) {
                drawH = panelH;
                drawW = (int) (panelH * imgRatio);
            } else {
                drawW = panelW;
                drawH = (int) (panelW / imgRatio);
            }
        }

        int x = (panelW - drawW) / 2;
        int y = (panelH - drawH) / 2;

        g.drawImage(image, x, y, drawW, drawH, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1, 360);
    }
}

