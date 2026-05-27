package com.inventory.util;

import com.inventory.model.Images;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Dearclaudia
 */
public class ImageRendererBanner extends DefaultTableCellRenderer {

    private final int targetWidth;
    private final int targetHeight;

    public ImageRendererBanner(int targetWidth) {
        this.targetWidth = targetWidth;
        this.targetHeight = (int) (targetWidth * 360.0 / 1600.0); // sesuai banner
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, "", isSelected, hasFocus, row, column);

        label.setIcon(null);

        if (value instanceof Image) {
            Image img = (Image) value;

            Image scaled = img.getScaledInstance(
                    targetWidth,
                    targetHeight,
                    Image.SCALE_SMOOTH
            );

            label.setIcon(new ImageIcon(scaled));
        }

        return label;
    }


    // ===================== helper =====================

    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(
            img.getWidth(null),
            img.getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = bimage.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return bimage;
    }


    private ImageIcon scaleCover(Image img, int targetW, int targetH) {

        BufferedImage src = toBufferedImage(img);

        int srcW = src.getWidth();
        int srcH = src.getHeight();

        double scale = Math.max(
            (double) targetW / srcW,
            (double) targetH / srcH
        );

        int scaledW = (int) Math.round(srcW * scale);
        int scaledH = (int) Math.round(srcH * scale);

        BufferedImage scaled = new BufferedImage(
            scaledW, scaledH, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR
        );
        g2.drawImage(src, 0, 0, scaledW, scaledH, null);
        g2.dispose();

        // 🔒 AMAN: pastikan tidak keluar raster
        int x = Math.max(0, (scaledW - targetW) / 2);
        int y = Math.max(0, (scaledH - targetH) / 2);

        int cropW = Math.min(targetW, scaledW);
        int cropH = Math.min(targetH, scaledH);

        BufferedImage cropped = scaled.getSubimage(x, y, cropW, cropH);

        return new ImageIcon(cropped);
    }

}
