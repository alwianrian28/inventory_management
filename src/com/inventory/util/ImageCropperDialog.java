package com.inventory.util;

import static com.inventory.util.AlertUtils.getOptionAlert;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import raven.modal.Toast;

public class ImageCropperDialog extends JDialog {

    private BufferedImage originalImage;
    private BufferedImage croppedImageResult;
    private JLabel imageDisplayLabel;
    private JPanel cropOverlayPanel;
    private JSlider zoomSlider;
    private JButton btnDone, btnCancel;
    private JLayeredPane layeredPane;

    private double zoomFactor = 1.0;
    private Point imageOffset = new Point(0, 0);
    private Point lastMousePoint;

    private final int CROP_WIDTH_FIXED = 1600;
    private final int CROP_HEIGHT_FIXED = 360;

    public ImageCropperDialog(Frame parent, String imagePath) {
        super(parent, "Sesuaikan Gambar Banner", true);
        try {
            originalImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            Toast.show(this, Toast.Type.ERROR, "Tidak dapat memuat gambar", getOptionAlert());
            dispose();
            return;
        }
        initUI();
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(CROP_WIDTH_FIXED + 100, CROP_HEIGHT_FIXED + 100));

        imageDisplayLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (originalImage != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    AffineTransform transform = new AffineTransform();
                    transform.translate(imageOffset.x, imageOffset.y);
                    transform.scale(zoomFactor, zoomFactor);

                    g2d.drawImage(originalImage, transform, this);
                }
            }
        };
        imageDisplayLabel.setBounds(0, 0,
                layeredPane.getPreferredSize().width, layeredPane.getPreferredSize().height);
        imageDisplayLabel.setOpaque(false);
        imageDisplayLabel.setBackground(Color.DARK_GRAY);

        // Panning
        imageDisplayLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePoint = e.getPoint();
            }
        });
        imageDisplayLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastMousePoint != null) {
                    int dx = e.getX() - lastMousePoint.x;
                    int dy = e.getY() - lastMousePoint.y;
                    imageOffset.translate(dx, dy);
                    lastMousePoint = e.getPoint();
                    imageDisplayLabel.repaint();
                }
            }
        });

        layeredPane.add(imageDisplayLabel, JLayeredPane.CENTER_ALIGNMENT);

        cropOverlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int x = (getWidth() - CROP_WIDTH_FIXED) / 2;
                int y = (getHeight() - CROP_HEIGHT_FIXED) / 2;

                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                //g2d.clearRect(x, y, CROP_WIDTH_FIXED, CROP_HEIGHT_FIXED);

                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(x, y, CROP_WIDTH_FIXED, CROP_HEIGHT_FIXED);
            }
        };
        cropOverlayPanel.setOpaque(false);
        cropOverlayPanel.setBounds(0, 0,
                layeredPane.getPreferredSize().width, layeredPane.getPreferredSize().height);
        layeredPane.add(cropOverlayPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        zoomSlider = new JSlider(JSlider.HORIZONTAL, 10, 300, 100);
        zoomSlider.setMajorTickSpacing(50);
        zoomSlider.setMinorTickSpacing(10);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);
        zoomSlider.addChangeListener((ChangeEvent e) -> {
            double oldZoom = zoomFactor;
            double newZoom = zoomSlider.getValue() / 100.0;

            if (originalImage != null && oldZoom > 0) {
                // Titik tengah panel
                int panelCenterX = imageDisplayLabel.getWidth() / 2;
                int panelCenterY = imageDisplayLabel.getHeight() / 2;

                // Posisi relatif pada gambar sebelum zoom
                double relX = (panelCenterX - imageOffset.x) / oldZoom;
                double relY = (panelCenterY - imageOffset.y) / oldZoom;

                // Update zoom
                zoomFactor = newZoom;

                // Hitung offset baru supaya relX/relY tetap di tengah
                imageOffset.x = (int) (panelCenterX - relX * zoomFactor);
                imageOffset.y = (int) (panelCenterY - relY * zoomFactor);
            } else {
                zoomFactor = newZoom;
            }

            imageDisplayLabel.repaint();
        });


        JPanel zoomPanel = new JPanel(new BorderLayout(5, 0));
        zoomPanel.add(new JLabel("Zoom: "), BorderLayout.WEST);
        zoomPanel.add(zoomSlider, BorderLayout.CENTER);
        controlPanel.add(zoomPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDone = new JButton("Selesai");
        btnDone.addActionListener(e -> {
            performCrop();
            if (croppedImageResult != null) {
                dispose();
            }
        });

        btnCancel = new JButton("Batal");
        btnCancel.addActionListener(e -> {
            croppedImageResult = null;
            dispose();
        });
        buttonPanel.add(btnDone);
        buttonPanel.add(btnCancel);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(controlPanel, BorderLayout.SOUTH);
        centerImageOnLoad();
    }

    private void centerImageOnLoad() {
        if (originalImage == null) return;
        double scaleX = (double) CROP_WIDTH_FIXED / originalImage.getWidth();
        double scaleY = (double) CROP_HEIGHT_FIXED / originalImage.getHeight();
        zoomFactor = Math.max(scaleX, scaleY);

        zoomSlider.setValue((int) (zoomFactor * 100));
        int scaledW = (int) (originalImage.getWidth() * zoomFactor);
        int scaledH = (int) (originalImage.getHeight() * zoomFactor);
        imageOffset.x = (layeredPane.getWidth() - scaledW) / 2;
        imageOffset.y = (layeredPane.getHeight() - scaledH) / 2;
        imageDisplayLabel.repaint();
    }

    private void performCrop() {
        if (originalImage == null) return;

        int cropX = (imageDisplayLabel.getWidth() - CROP_WIDTH_FIXED) / 2;
        int cropY = (imageDisplayLabel.getHeight() - CROP_HEIGHT_FIXED) / 2;
        double invZoom = 1.0 / zoomFactor;

        int startX = (int) ((cropX - imageOffset.x) * invZoom);
        int startY = (int) ((cropY - imageOffset.y) * invZoom);
        int w = (int) (CROP_WIDTH_FIXED * invZoom);
        int h = (int) (CROP_HEIGHT_FIXED * invZoom);

        try {
            croppedImageResult = originalImage.getSubimage(
                    Math.max(0, startX), Math.max(0, startY),
                    Math.min(w, originalImage.getWidth() - startX),
                    Math.min(h, originalImage.getHeight() - startY));

            int imageType = originalImage.getColorModel().hasAlpha() 
                    ? BufferedImage.TYPE_INT_ARGB 
                    : BufferedImage.TYPE_INT_RGB;

            BufferedImage finalImg = new BufferedImage(croppedImageResult.getWidth(),
                                                    croppedImageResult.getHeight(),
                                                    imageType);
            Graphics2D g2d = finalImg.createGraphics();
            g2d.drawImage(croppedImageResult, 0, 0, null);
            g2d.dispose();
            croppedImageResult = finalImg;


        } catch (RasterFormatException e) {
            Toast.show(this, Toast.Type.ERROR, "Error Cropping", getOptionAlert());
        }
    }

    public BufferedImage getCroppedImageResult() {
        return croppedImageResult;
    }

    @Override
    public void setVisible(boolean b) {
        if (b) SwingUtilities.invokeLater(this::centerImageOnLoad);
        super.setVisible(b);
    }
}
