package com.inventory.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.DashboardController;
import com.inventory.controller.ImagesController;
import com.inventory.main.Form;
import com.inventory.model.Images;
import com.inventory.util.AppResources;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Dearclaudia
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class FormDashboard extends Form {
    private static final long serialVersionUID = 1L;


    private final ImagesController imagesController = new ImagesController();
    private final DashboardController dashboardController = new DashboardController();

    private final List<BufferedImage> images = new ArrayList<>();
    private int index = 0;

    private FadeImagePanel imagePanel;
    private Timer autoTimer;

    public FormDashboard() {
        loadImages();
        init();
    }

    @Override
    public void formOpen() {
        stopAuto();
        removeAll();
        loadImages();
        init();
        revalidate();
        repaint();
    }

    private void init() {
        setLayout(new MigLayout(
                "fill, wrap, insets 5",
                "[grow,fill]",
                "[]10[]10[grow,fill]"
        ));

        add(panelSlider(), "growx, h 220!");
        add(panelSummary(), "growx");
        add(panelCharts(), "grow");
    }

    private JPanel panelSlider() {
        JPanel wrapper = new JPanel(new MigLayout("fill, insets 0"));
        wrapper.putClientProperty(
                FlatClientProperties.STYLE,
                "arc:15;background:rgb(255,255,255)"
        );

        JLayeredPane layered = new JLayeredPane();
        layered.setLayout(null);
        wrapper.add(layered, "grow");

        imagePanel = new FadeImagePanel();
        if (!images.isEmpty()) {
            imagePanel.setImage(images.get(0));
        }

        JButton prev = createNavButton(
                new FlatSVGIcon(AppResources.ICON_BASE + "before.svg", 0.8f));
        JButton next = createNavButton(
                new FlatSVGIcon(AppResources.ICON_BASE + "next.svg", 0.8f));

        prev.addActionListener(e -> showPrev());
        next.addActionListener(e -> showNext());

        layered.add(imagePanel, Integer.valueOf(0));
        layered.add(prev, Integer.valueOf(1));
        layered.add(next, Integer.valueOf(1));

        wrapper.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension s = wrapper.getSize();
                imagePanel.setBounds(0, 0, s.width, s.height);

                int btnSize = 36;
                int y = (s.height - btnSize) / 2;
                prev.setBounds(12, y, btnSize, btnSize);
                next.setBounds(s.width - btnSize - 12, y, btnSize, btnSize);
            }
        });

        if (images.size() > 1) {
            startAuto();
        }

        return wrapper;
    }

    private JButton createNavButton(FlatSVGIcon icon) {
        JButton btn = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0.6f));
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.putClientProperty(FlatClientProperties.STYLE,
                "background:rgba(0,0,0,50);"
                + "hoverBackground:rgba(0,0,0,80);"
                + "pressedBackground:rgba(0,0,0,100);"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "arc:999");
        return btn;
    }

    private void showNext() {
        if (images.isEmpty()) {
            return;
        }
        index = (index + 1) % images.size();
        imagePanel.fadeTo(images.get(index));
    }

    private void showPrev() {
        if (images.isEmpty()) {
            return;
        }
        index = (index - 1 + images.size()) % images.size();
        imagePanel.fadeTo(images.get(index));
    }

    private void loadImages() {
        images.clear();
        try {
            for (Images img : imagesController.getData(Integer.MAX_VALUE, 1)) {
                if (img.getImagePath() != null) {
                    images.add(ImageIO.read(new File(img.getImagePath())));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAuto() {
        autoTimer = new Timer();
        autoTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> showNext());
            }
        }, 5000, 5000);
    }

    private void stopAuto() {
        if (autoTimer != null) {
            autoTimer.cancel();
            autoTimer = null;
        }
    }

    static class FadeImagePanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private BufferedImage current;
        private BufferedImage next;
        private float alpha = 1f;
        private Timer fade;

        void setImage(BufferedImage img) {
            current = img;
            repaint();
        }

        void fadeTo(BufferedImage img) {
            if (fade != null) {
                fade.cancel();
            }

            next = img;
            alpha = 1f;

            fade = new Timer();
            fade.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    alpha -= 0.05f;
                    if (alpha <= 0) {
                        current = next;
                        next = null;
                        alpha = 1f;
                        fade.cancel();
                    }
                    repaint();
                }
            }, 0, 16);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g2.setClip(new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 15, 15));

            if (current != null) {
                drawCoverImage(g2, current, 1f);
            }

            if (next != null) {
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 1f - alpha));
                drawCoverImage(g2, next, 1f);
            }

            g2.dispose();
        }

        private void drawCoverImage(Graphics2D g2, BufferedImage img, float opacity) {
            int panelW = getWidth();
            int panelH = getHeight();
            int imgW = img.getWidth();
            int imgH = img.getHeight();

            double scale = Math.max(
                    (double) panelW / imgW,
                    (double) panelH / imgH
            );

            int drawW = (int) (imgW * scale);
            int drawH = (int) (imgH * scale);
            int x = (panelW - drawW) / 2;
            int y = (panelH - drawH) / 2;

            g2.drawImage(img, x, y, drawW, drawH, null);
        }
    }

    private JPanel panelSummary() {
        JPanel panel = new JPanel(new MigLayout(
                "wrap 3, insets 0, gap 10",
                "[grow,fill][grow,fill][grow,fill]",
                "[]10[]"
        ));
        panel.setOpaque(false);

        DecimalFormatSymbols s = new DecimalFormatSymbols(new Locale("id", "ID"));
        s.setCurrencySymbol("Rp.");
        s.setGroupingSeparator('.');
        s.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("¤ #,###.00", s);

        panel.add(createCard("Total Barang",
                String.valueOf(dashboardController.getTotalBarangs()),
                "barang.svg"));
        panel.add(createCard("Total Supplier",
                String.valueOf(dashboardController.getTotalSuppliers()),
                "supplier.svg"));
        panel.add(createCard("Barang Masuk (pcs/hari)",
                String.valueOf(dashboardController.getTotalJumlahMasukPerHari()),
                "barang_masuk.svg"));
        panel.add(createCard("Nilai Barang Masuk (Rp/hari)",
                df.format(dashboardController.getTotalMasukPerHari()),
                "barang_masuk.svg"));
        panel.add(createCard("Barang Keluar (pcs/hari)",
                String.valueOf(dashboardController.getTotalJumlahKeluarPerHari()),
                "barang_keluar.svg"));
        panel.add(createCard("Nilai Barang Keluar (Rp/hari)",
                df.format(dashboardController.getTotalKeluarPerHari()),
                "barang_keluar.svg"));

        return panel;
    }

    private JPanel createCard(String title, String value, String iconName) {
        JPanel p = new JPanel(new MigLayout(
                "insets 12 15 12 15, gap 10",
                "[][grow,fill]",
                "[]4[]"
        ));
        p.putClientProperty(FlatClientProperties.STYLE,
                "arc:12;background:rgb(255,255,255)");

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + iconName, 0.55f));
        p.add(iconLabel, "span 1 2, ay center");

        JLabel titleLabel = new JLabel(title);
        titleLabel.putClientProperty(FlatClientProperties.STYLE,
                "font:11;foreground:$Label.disabledForeground");
        p.add(titleLabel, "growx, wrap");

        JLabel valueLabel = new JLabel(value);
        valueLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold +3");
        p.add(valueLabel, "growx");

        return p;
    }

    private JPanel panelCharts() {
        JPanel panel = new JPanel(new MigLayout(
                "insets 0, fill, gap 10",
                "[grow,fill][grow,fill]",
                "[grow,fill]"
        ));
        panel.setOpaque(false);

        panel.add(createBarangMasukLineChart(true), "grow");
        panel.add(createBarangKeluarLineChart(true), "grow");
        return panel;
    }

    private String getNamaBulanIndonesia(int bulan) {
        String[] bulanIndo = {
            "Januari", "Februari", "Maret", "April",
            "Mei", "Juni", "Juli", "Agustus",
            "September", "Oktober", "November", "Desember"
        };
        return bulanIndo[bulan - 1];
    }

    private JPanel createBarangMasukLineChart(boolean monthly) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Double> dataBarangMasuk;
        if (monthly) {
            dataBarangMasuk = dashboardController.getBarangMasukPerBulanTahunIni();
        } else {
            dataBarangMasuk = dashboardController.getBarangMasukPerHariBulanIni();
        }

        for (Map.Entry<String, Double> entry : dataBarangMasuk.entrySet()) {
            String label;
            if (monthly) {
                String key = entry.getKey();
                int bulan;
                if (key.length() > 2) {
                    bulan = Integer.parseInt(key.substring(5, 7));
                } else {
                    bulan = Integer.parseInt(key);
                }
                label = getNamaBulanIndonesia(bulan);
            } else {
                label = entry.getKey();
            }
            dataset.addValue(entry.getValue(), "Barang Masuk", label);
        }

        String xAxis = monthly ? "Bulan" : "Tanggal";

        JFreeChart lineChart = ChartFactory.createLineChart(
                null, xAxis, "Total Barang Masuk",
                dataset, PlotOrientation.VERTICAL,
                false, true, false
        );

        styleChart(lineChart);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(12, 44, 64));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setBaseToolTipGenerator((CategoryDataset ds, int row, int column) -> {
            String lbl = ds.getColumnKey(column).toString();
            Number val = ds.getValue(row, column);
            return (monthly ? "Bulan: " : "Tanggal: ") + lbl + "\nTotal: " + val;
        });
        lineChart.getCategoryPlot().setRenderer(renderer);

        String wrapperTitle = monthly
                ? "Barang Masuk per Bulan (Tahun Ini)"
                : "Barang Masuk Harian (Bulan Ini)";

        return wrapChart(lineChart, wrapperTitle);
    }

    private JPanel createBarangKeluarLineChart(boolean monthly) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Double> dataBarangKeluar;
        if (monthly) {
            dataBarangKeluar = dashboardController.getBarangKeluarPerBulanTahunIni();
        } else {
            dataBarangKeluar = dashboardController.getBarangKeluarPerHariBulanIni();
        }

        for (Map.Entry<String, Double> entry : dataBarangKeluar.entrySet()) {
            String label;
            if (monthly) {
                String key = entry.getKey();
                int bulan;
                if (key.length() > 2) {
                    bulan = Integer.parseInt(key.substring(5, 7));
                } else {
                    bulan = Integer.parseInt(key);
                }
                label = getNamaBulanIndonesia(bulan);
            } else {
                label = entry.getKey();
            }
            dataset.addValue(entry.getValue(), "Barang Keluar", label);
        }

        String xAxis = monthly ? "Bulan" : "Tanggal";

        JFreeChart lineChart = ChartFactory.createLineChart(
                null, xAxis, "Total Barang Keluar",
                dataset, PlotOrientation.VERTICAL,
                false, true, false
        );

        styleChart(lineChart);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
        renderer.setSeriesPaint(0, new Color(12, 44, 64));
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setBaseToolTipGenerator((CategoryDataset ds, int row, int column) -> {
            String lbl = ds.getColumnKey(column).toString();
            Number val = ds.getValue(row, column);
            return (monthly ? "Bulan: " : "Tanggal: ") + lbl + "\nTotal: " + val;
        });
        lineChart.getCategoryPlot().setRenderer(renderer);

        String wrapperTitle = monthly
                ? "Barang Keluar per Bulan (Tahun Ini)"
                : "Barang Keluar Harian (Bulan Ini)";

        return wrapChart(lineChart, wrapperTitle);
    }

    private void styleChart(JFreeChart chart) {
        Color labelColor = Color.BLACK;

        chart.setBackgroundPaint(null);
        if (chart.getLegend() != null) {
            chart.getLegend().setBackgroundPaint(null);
            chart.getLegend().setBorder(0, 0, 0, 0);
            chart.getLegend().setItemPaint(labelColor);
        }

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(null);
        plot.setOutlinePaint(labelColor);
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setDomainGridlinePaint(new Color(220, 220, 220));

        plot.getRangeAxis().setLabelPaint(labelColor);
        plot.getRangeAxis().setTickLabelPaint(labelColor);
        plot.getRangeAxis().setLabelFont(new Font("Poppins", Font.BOLD, 12));
        plot.getRangeAxis().setTickLabelFont(new Font("Poppins", Font.PLAIN, 11));

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelPaint(labelColor);
        domainAxis.setTickLabelPaint(labelColor);
        domainAxis.setLabelFont(new Font("Poppins", Font.BOLD, 12));
        domainAxis.setTickLabelFont(new Font("Poppins", Font.PLAIN, 11));
    }

    private JPanel wrapChart(JFreeChart chart, String title) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(null);
        chartPanel.setPreferredSize(new Dimension(0, 300));
        chartPanel.setMinimumSize(new Dimension(0, 180));

        JPanel wrapper = new JPanel(new MigLayout(
                "fill, insets 15",
                "[grow,fill]",
                "[]10[grow,fill]"
        ));
        wrapper.putClientProperty(FlatClientProperties.STYLE,
                "arc:15;background:rgb(255,255,255)");

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "line_chart.svg", 0.35f));

        JLabel lbTitle = new JLabel(title);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 14");

        JPanel titlePanel = new JPanel(new MigLayout("insets 0", "[]5[]", ""));
        titlePanel.setOpaque(false);
        titlePanel.add(iconLabel);
        titlePanel.add(lbTitle);

        wrapper.add(titlePanel, "center, wrap");
        wrapper.add(chartPanel, "grow");

        return wrapper;
    }
}
