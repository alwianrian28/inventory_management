package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.main.FormManager;
import com.inventory.model.*;
import com.inventory.util.ExportType;
import com.inventory.util.TransparentOptionPane;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Dearclaudia
 */
public class ReportDAO {

    private static final Map<String, JasperReport> REPORT_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Image> IMAGE_CACHE = new ConcurrentHashMap<>();

    private final Connection conn;
    private final User loggedInUser;
    private final String name;
    private final String role;

    public ReportDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
        this.loggedInUser = FormManager.getLoggedInUser();
        this.name = loggedInUser.getName();
        this.role = loggedInUser.getRole();
    }

    private JasperReport loadReport(String jrxmlClasspath) throws JRException {
        JasperReport cached = REPORT_CACHE.get(jrxmlClasspath);
        if (cached != null) {
            return cached;
        }
        InputStream is = getClass().getResourceAsStream(jrxmlClasspath);
        if (is == null) {
            throw new JRException("Report template tidak ditemukan: " + jrxmlClasspath);
        }
        JasperReport report = JasperCompileManager.compileReport(is);
        REPORT_CACHE.put(jrxmlClasspath, report);
        return report;
    }

    private Image loadImage(String classpathImage) {
        Image cached = IMAGE_CACHE.get(classpathImage);
        if (cached != null) {
            return cached;
        }
        try (InputStream is = getClass().getResourceAsStream(classpathImage)) {
            if (is != null) {
                Image img = ImageIO.read(is);
                IMAGE_CACHE.put(classpathImage, img);
                return img;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JRVirtualizer createVirtualizer() {
        return new JRGzipVirtualizer(50);
    }

    private void showViewer(JasperPrint print) {
        JasperViewer viewer = new JasperViewer(print, false);
        viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
        viewer.setVisible(true);
    }

    public void previewBarangMasuk(String noTransaksi) {
        try {
            JasperReport report = loadReport("/com/inventory/jasper/BarangMasuk.jrxml");

            HashMap<String, Object> parameters = new HashMap<>();
            if (noTransaksi != null && !noTransaksi.trim().isEmpty()) {
                parameters.put("no_transaksi", noTransaksi);
                parameters.put("name", name);
                parameters.put("role", role);
            }
            parameters.put("LOGO_IMAGE", loadImage("/com/inventory/assets/Logo4.png"));
            parameters.put("REPORT_VIRTUALIZER", createVirtualizer());

            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);
            showViewer(print);

        } catch (JRException e) {
            e.printStackTrace();
            TransparentOptionPane.showErrorDialog(FormManager.getJFrame(), e);
        }
    }

    public void previewBarangKeluar(String noTransaksi) {
        try {
            JasperReport report = loadReport("/com/inventory/jasper/BarangKeluar.jrxml");

            HashMap<String, Object> parameters = new HashMap<>();
            if (noTransaksi != null && !noTransaksi.trim().isEmpty()) {
                parameters.put("no_transaksi", noTransaksi);
                parameters.put("name", name);
                parameters.put("role", role);
            }
            parameters.put("LOGO_IMAGE", loadImage("/com/inventory/assets/Logo4.png"));
            parameters.put("REPORT_VIRTUALIZER", createVirtualizer());

            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);

            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setExtendedState(JasperViewer.MAXIMIZED_BOTH);
            viewer.setVisible(true);
            viewer.setZoomRatio(1.75f);

        } catch (JRException e) {
            e.printStackTrace();
            TransparentOptionPane.showErrorDialog(FormManager.getJFrame(), e);
        }
    }

    public List<BarangMasukDetail> getDataStok(String keyword, int itemsPerPage, int currentPage) {
        List<BarangMasukDetail> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT bm.no_nota, " +
                "b.barang_code, b.barang_name, b.harga_jual, b.stok_minimum, " +
                "st.harga_beli, st.jumlah, " +
                "m.merk_name, k.kategori_name, s.satuan_name, " +
                "sp.supplier_name, r.rak_name, g.gudang_name " +
                "FROM barang_masuk_detail det " +
                "JOIN barang_masuk bm ON bm.barang_masuk_id = det.barang_masuk_id " +
                "JOIN stok_gudang st ON st.stok_id = det.stok_id " +
                "JOIN barang b ON b.barang_id = st.barang_id " +
                "JOIN merk m ON b.merk_id = m.merk_id " +
                "JOIN kategori k ON b.kategori_id = k.kategori_id " +
                "JOIN satuan s ON b.satuan_id = s.satuan_id " +
                "JOIN supplier sp ON b.supplier_id = sp.supplier_id " +
                "JOIN rak r ON r.rak_id = det.rak_id " +
                "JOIN gudang g ON g.gudang_id = det.gudang_id " +
                "WHERE b.is_delete = 0 "
        );

        if (keyword != null && !keyword.isBlank()) {
            sql.append(
                    "AND ( " +
                    "b.barang_code LIKE ? OR " +
                    "b.barang_name LIKE ? OR " +
                    "m.merk_name LIKE ? OR " +
                    "k.kategori_name LIKE ? OR " +
                    "s.satuan_name LIKE ? OR " +
                    "sp.supplier_name LIKE ? OR " +
                    "r.rak_name LIKE ? OR " +
                    "g.gudang_name LIKE ? OR " +
                    "CAST(st.harga_beli AS CHAR) LIKE ? OR " +
                    "CAST(st.jumlah AS CHAR) LIKE ? " +
                    ") "
            );
        }

        sql.append("ORDER BY b.barang_id ASC LIMIT ? OFFSET ?");

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            int index = 1;

            if (keyword != null && !keyword.isBlank()) {
                for (int i = 0; i < 10; i++) {
                    st.setString(index++, "%" + keyword + "%");
                }
            }

            st.setInt(index++, itemsPerPage);
            st.setInt(index, (currentPage - 1) * itemsPerPage);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {

                    BarangMasukDetail detail = new BarangMasukDetail();

                    BarangMasuk bm = new BarangMasuk();
                    bm.setNoNota(rs.getString("no_nota"));
                    detail.setBarangMasuk(bm);

                    Barang barang = new Barang();
                    barang.setBarangCode(rs.getString("barang_code"));
                    barang.setBarangName(rs.getString("barang_name"));
                    barang.setHargaJual(rs.getDouble("harga_jual"));
                    barang.setStokMinimum(rs.getInt("stok_minimum"));

                    Merk merk = new Merk();
                    merk.setMerkName(rs.getString("merk_name"));
                    barang.setMerk(merk);

                    Kategori kategori = new Kategori();
                    kategori.setKategoriName(rs.getString("kategori_name"));
                    barang.setKategori(kategori);

                    Satuan satuan = new Satuan();
                    satuan.setSatuanName(rs.getString("satuan_name"));
                    barang.setSatuan(satuan);

                    Supplier supplier = new Supplier();
                    supplier.setSupplierName(rs.getString("supplier_name"));
                    barang.setSupplier(supplier);

                    Rak rak = new Rak();
                    rak.setRakName(rs.getString("rak_name"));

                    Gudang gudang = new Gudang();
                    gudang.setGudangName(rs.getString("gudang_name"));

                    StokGudang stok = new StokGudang();
                    stok.setBarang(barang);
                    stok.setRak(rak);
                    stok.setGudang(gudang);
                    stok.setJumlah(rs.getInt("jumlah"));
                    stok.setHargaBeli(rs.getDouble("harga_beli"));

                    detail.setStokGudang(stok);

                    list.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public int getTotalItemsStok(String keyword) {

        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) AS total " +
            "FROM barang_masuk_detail det " +
            "JOIN barang_masuk bm ON bm.barang_masuk_id = det.barang_masuk_id " +
            "INNER JOIN stok_gudang st ON st.stok_id = det.stok_id " +
            "INNER JOIN barang b ON b.barang_id = st.barang_id " +
            "JOIN merk m ON b.merk_id = m.merk_id " +
            "JOIN kategori k ON b.kategori_id = k.kategori_id " +
            "JOIN satuan s ON b.satuan_id = s.satuan_id " +
            "JOIN supplier sp ON b.supplier_id = sp.supplier_id " +
            "INNER JOIN rak r ON r.rak_id = det.rak_id " +
            "INNER JOIN gudang g ON g.gudang_id = det.gudang_id " +
            "WHERE b.is_delete = 0 "
        );

        if (keyword != null && !keyword.isBlank()) {
            sql.append(
                "AND ( " +
                " b.barang_code LIKE ? " +
                " OR b.barang_name LIKE ? " +
                " OR m.merk_name LIKE ? " +
                " OR k.kategori_name LIKE ? " +
                " OR s.satuan_name LIKE ? " +
                " OR sp.supplier_name LIKE ? " +
                " OR r.rak_name LIKE ? " +
                " OR g.gudang_name LIKE ? " +
                " OR CAST(st.harga_beli AS CHAR) LIKE ? " +
                " OR CAST(st.jumlah AS CHAR) LIKE ? " +
                ") "
            );
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (keyword != null && !keyword.isBlank()) {
                for (int i = 0; i < 10; i++) {
                    ps.setString(index++, "%" + keyword + "%");
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public void generateStokReport(ExportType type) {
        try {
            JasperReport report = loadReport("/com/inventory/jasper/ReportStok.jrxml");

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("name", name);
            parameters.put("role", role);
            parameters.put("LOGO_IMAGE", loadImage("/com/inventory/assets/Logo4.png"));
            parameters.put("REPORT_VIRTUALIZER", createVirtualizer());

            if (type == ExportType.EXCEL) {
                parameters.put("REPORT_FORMAT", "XLSX");
            }

            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);

            if (type == ExportType.PREVIEW) {
                showViewer(print);
                return;
            }

            // Tentukan ekstensi dan filter
            String extension = type == ExportType.PDF ? "pdf" : "xlsx";
            String keterangan = type == ExportType.PDF ? "PDF Files" : "Excel Files";

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            // Panggil util untuk Save File Dialog
            File fileToSave = TransparentOptionPane.showSaveFileDialog(
                    FormManager.getJFrame(),
                    "Export Report",
                    new FileNameExtensionFilter(keterangan, extension),
                    new File("Laporan Stok Barang " + saveDate + "." + extension)
            );
            if (fileToSave == null) {
                return; // user cancel
            }

            String savePath = fileToSave.getAbsolutePath();
            if (!savePath.toLowerCase().endsWith("." + extension)) {
                savePath += "." + extension;
            }

            File finalFile = new File(savePath);
            if (finalFile.exists()) {
                int confirm = TransparentOptionPane.showConfirmDialog(
                        FormManager.getJFrame(),
                        "File sudah ada. Apakah Anda ingin menimpanya?",
                        "Konfirmasi Timpa File",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Export sesuai tipe
            if (type == ExportType.PDF) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();
            } else if (type == ExportType.EXCEL) {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);

                exporter.setConfiguration(configuration);
                exporter.exportReport();
            }

            TransparentOptionPane.showMessageDialog(
                    FormManager.getJFrame(),
                    "Laporan berhasil disimpan di : " + savePath,
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (Desktop.isDesktopSupported()) {
                File file = new File(savePath);
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransparentOptionPane.showErrorDialog(FormManager.getJFrame(), e);
        }
    }

    public void previewStok() {
        generateStokReport(ExportType.PREVIEW);
    }

    public void exportStokPDF() {
        generateStokReport(ExportType.PDF);
    }

    public void exportStokExcel() {
        generateStokReport(ExportType.EXCEL);
    }

    public List<BarangMasukDetail> getDataBarangMasuk(
            Date startDate,
            Date endDate,
            String keyword,
            int itemsPerPage,
            int currentPage
    ) {

        List<BarangMasukDetail> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT "
                + " bm.no_transaksi, "
                + " bm.no_nota, "
                + " bm.tanggal_masuk, "
                + " sup.supplier_name, "
                + " b.barang_name, "
                + " det.jumlah, "
                + " det.harga_beli, "
                + " det.subtotal, "
                + " r.rak_name, "
                + " g.gudang_name "
                + "FROM barang_masuk_detail det "
                + "INNER JOIN barang_masuk bm ON bm.barang_masuk_id = det.barang_masuk_id "
                + "INNER JOIN supplier sup ON sup.supplier_id = bm.supplier_id "
                + "INNER JOIN stok_gudang st ON st.stok_id = det.stok_id "
                + "INNER JOIN barang b ON b.barang_id = st.barang_id "
                + "INNER JOIN rak r ON r.rak_id = det.rak_id "
                + "INNER JOIN gudang g ON g.gudang_id = det.gudang_id "
                + "WHERE bm.tanggal_masuk BETWEEN ? AND ? "
        );

        // 🔍 Search
        if (keyword != null && !keyword.isBlank()) {
            sql.append(
                    "AND ( "
                    + " bm.no_transaksi LIKE ? "
                    + " OR bm.no_nota LIKE ? "
                    + " OR sup.supplier_name LIKE ? "
                    + " OR b.barang_name LIKE ? "
                    + " OR r.rak_name LIKE ? "
                    + " OR g.gudang_name LIKE ? "
                    + " OR CAST(det.jumlah AS CHAR) LIKE ? "
                    + " OR CAST(det.harga_beli AS CHAR) LIKE ? "
                    + " OR CAST(det.subtotal AS CHAR) LIKE ? "
                    + ") "
            );
        }

        sql.append("ORDER BY bm.tanggal_masuk DESC ");
        sql.append("LIMIT ? OFFSET ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setDate(index++, new java.sql.Date(startDate.getTime()));
            ps.setDate(index++, new java.sql.Date(endDate.getTime()));

            if (keyword != null && !keyword.isBlank()) {
                for (int i = 0; i < 9; i++) {
                    ps.setString(index++, "%" + keyword + "%");
                }
            }

            ps.setInt(index++, itemsPerPage);
            ps.setInt(index, (currentPage - 1) * itemsPerPage);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    // ================= Barang Masuk =================
                    BarangMasuk barangMasuk = new BarangMasuk();
                    barangMasuk.setNoTransaksi(rs.getString("no_transaksi"));
                    barangMasuk.setNoNota(rs.getString("no_nota"));
                    barangMasuk.setTanggalMasuk(rs.getString("tanggal_masuk"));

                    Supplier supplier = new Supplier();
                    supplier.setSupplierName(rs.getString("supplier_name"));
                    barangMasuk.setSupplier(supplier);

                    // ================= Detail =================
                    BarangMasukDetail detail = new BarangMasukDetail();
                    detail.setBarangMasuk(barangMasuk);
                    detail.setJumlah(rs.getInt("jumlah"));
                    detail.setHargaBeli(rs.getDouble("harga_beli"));
                    detail.setSubtotal(rs.getDouble("subtotal"));

                    // ================= Stok / Barang =================
                    StokGudang stok = new StokGudang();
                    Barang barang = new Barang();
                    barang.setBarangName(rs.getString("barang_name"));
                    stok.setBarang(barang);
                    detail.setStokGudang(stok);

                    // ================= Rak & Gudang =================
                    Rak rak = new Rak();
                    rak.setRakName(rs.getString("rak_name"));
                    detail.setRak(rak);

                    Gudang gudang = new Gudang();
                    gudang.setGudangName(rs.getString("gudang_name"));
                    detail.setGudang(gudang);

                    list.add(detail);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalItemsBarangMasuk(Date startDate, Date endDate, String keyword) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) AS total " +
            "FROM barang_masuk_detail det " +
            "INNER JOIN barang_masuk bm ON bm.barang_masuk_id = det.barang_masuk_id " +
            "INNER JOIN stok_gudang st ON st.stok_id = det.stok_id " +
            "INNER JOIN barang b ON b.barang_id = st.barang_id " +
            "INNER JOIN supplier sp ON sp.supplier_id = bm.supplier_id " +
            "INNER JOIN rak r ON r.rak_id = det.rak_id " +
            "INNER JOIN gudang g ON g.gudang_id = det.gudang_id " +
            "WHERE bm.tanggal_masuk BETWEEN ? AND ? "
        );

        if (keyword != null && !keyword.isBlank()) {
            sql.append(
                "AND (bm.no_transaksi LIKE ? " +
                "OR bm.no_nota LIKE ? " +
                "OR b.barang_name LIKE ? " +
                "OR sp.supplier_name LIKE ? " +
                "OR r.rak_name LIKE ? " +
                "OR g.gudang_name LIKE ? " +
                "OR CAST(det.jumlah AS CHAR) LIKE ? " +
                "OR CAST(det.harga_beli AS CHAR) LIKE ?) "
            );
        }

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            st.setDate(idx++, new java.sql.Date(startDate.getTime()));
            st.setDate(idx++, new java.sql.Date(endDate.getTime()));

            if (keyword != null && !keyword.isBlank()) {
                for (int i = 0; i < 8; i++) {
                    st.setString(idx++, "%" + keyword + "%");
                }
            }

            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt("total") : 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }



    public void generateBarangMasukReport(Date startDate, Date endDate, ExportType type) {
        try {
            JasperReport report = loadReport("/com/inventory/jasper/ReportBarangMasuk.jrxml");

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("name", name);
            parameters.put("role", role);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            parameters.put("LOGO_IMAGE", loadImage("/com/inventory/assets/Logo4.png"));
            parameters.put("REPORT_VIRTUALIZER", createVirtualizer());

            if (type == ExportType.EXCEL) {
                parameters.put("REPORT_FORMAT", "XLSX");
            }

            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);

            if (type == ExportType.PREVIEW) {
                showViewer(print);
                return;
            }

            // Tentukan ekstensi dan filter
            String extension = type == ExportType.PDF ? "pdf" : "xlsx";
            String keterangan = type == ExportType.PDF ? "PDF Files" : "Excel Files";

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            // Panggil util untuk Save File Dialog
            File fileToSave = TransparentOptionPane.showSaveFileDialog(
                    FormManager.getJFrame(),
                    "Export Report",
                    new FileNameExtensionFilter(keterangan, extension),
                    new File("Laporan Barang Masuk " + saveDate + "." + extension)
            );

            if (fileToSave == null) {
                return; // user cancel
            }

            String savePath = fileToSave.getAbsolutePath();
            if (!savePath.toLowerCase().endsWith("." + extension)) {
                savePath += "." + extension;
            }

            File finalFile = new File(savePath);
            if (finalFile.exists()) {
                int confirm = TransparentOptionPane.showConfirmDialog(
                        FormManager.getJFrame(),
                        "File sudah ada. Apakah Anda ingin menimpanya?",
                        "Konfirmasi Timpa File",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Export sesuai tipe
            if (type == ExportType.PDF) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();
            } else if (type == ExportType.EXCEL) {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);

                exporter.setConfiguration(configuration);
                exporter.exportReport();
            }

            TransparentOptionPane.showMessageDialog(
                    FormManager.getJFrame(),
                    "Laporan berhasil disimpan di : " + savePath,
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (Desktop.isDesktopSupported()) {
                File file = new File(savePath);
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransparentOptionPane.showErrorDialog(FormManager.getJFrame(), e);
        }
    }

    public void previewBarangMasuk(Date startDate, Date endDate) {
        generateBarangMasukReport(startDate, endDate, ExportType.PREVIEW);
    }

    public void exportBarangMasukPDF(Date startDate, Date endDate) {
        generateBarangMasukReport(startDate, endDate, ExportType.PDF);
    }

    public void exportBarangMasukExcel(Date startDate, Date endDate) {
        generateBarangMasukReport(startDate, endDate, ExportType.EXCEL);
    }

    public List<BarangKeluarDetail> getDataBarangKeluar(
            Date startDate,
            Date endDate,
            String keyword,
            int itemsPerPage,
            int currentPage
    ) {

        List<BarangKeluarDetail> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT " +
            " bk.no_transaksi, " +
            " bk.tanggal_keluar, " +
            " bk.jenis_keluar, " +
            " bk.tujuan, " +
            " b.barang_name, " +
            " det.jumlah, " +
            " det.harga_jual, " +
            " det.subtotal " +
            "FROM barang_keluar_detail det " +
            "INNER JOIN barang_keluar bk ON bk.barang_keluar_id = det.barang_keluar_id " +
            "INNER JOIN barang b ON b.barang_id = det.barang_id " +
            "WHERE bk.tanggal_keluar BETWEEN ? AND ? "
        );

        // 🔍 Search
        if (keyword != null && !keyword.isBlank()) {
            sql.append(
                "AND ( " +
                " bk.no_transaksi LIKE ? " +
                " OR bk.tanggal_keluar LIKE ? " +
                " OR bk.jenis_keluar LIKE ? " +
                " OR bk.tujuan LIKE ? " +
                " OR b.barang_name LIKE ? " +
                " OR CAST(det.jumlah AS CHAR) LIKE ? " +
                " OR CAST(det.harga_jual AS CHAR) LIKE ? " +
                " OR CAST(det.subtotal AS CHAR) LIKE ? " +
                ") "
            );
        }

        sql.append("ORDER BY bk.tanggal_keluar DESC ");
        sql.append("LIMIT ? OFFSET ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setDate(index++, new java.sql.Date(startDate.getTime()));
            ps.setDate(index++, new java.sql.Date(endDate.getTime()));

            if (keyword != null && !keyword.isBlank()) {
                for (int i = 0; i < 8; i++) {
                    ps.setString(index++, "%" + keyword + "%");
                }
            }

            ps.setInt(index++, itemsPerPage);
            ps.setInt(index, (currentPage - 1) * itemsPerPage);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    // ================= Barang Keluar =================
                    BarangKeluar barangKeluar = new BarangKeluar();
                    barangKeluar.setNoTransaksi(rs.getString("no_transaksi"));
                    barangKeluar.setTanggalKeluar(rs.getString("tanggal_keluar"));
                    barangKeluar.setJenisKeluar(rs.getString("jenis_keluar"));
                    barangKeluar.setTujuan(rs.getString("tujuan"));

                    // ================= Detail =================
                    BarangKeluarDetail detail = new BarangKeluarDetail();
                    detail.setBarangKeluar(barangKeluar);
                    detail.setJumlah(rs.getInt("jumlah"));
                    detail.setHargaJual(rs.getDouble("harga_jual"));
                    detail.setSubtotal(rs.getDouble("subtotal"));

                    // ================= Barang =================
                    Barang barang = new Barang();
                    barang.setBarangName(rs.getString("barang_name"));
                    detail.setBarang(barang);

                    list.add(detail);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public int getTotalItemsBarangKeluar(Date startDate, Date endDate, String keyword) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) AS total " +
            "FROM barang_keluar_detail det " +
            "INNER JOIN barang_keluar bk ON bk.barang_keluar_id = det.barang_keluar_id " +
            "INNER JOIN barang b ON b.barang_id = det.barang_id " +
            "WHERE bk.tanggal_keluar BETWEEN ? AND ? "
        );

        if (keyword != null && !keyword.isBlank()) {
            sql.append(
                "AND (bk.no_transaksi LIKE ? " +
                "OR bk.jenis_keluar LIKE ? " +
                "OR bk.tujuan LIKE ? " +
                "OR b.barang_name LIKE ? " +
                "OR CAST(det.jumlah AS CHAR) LIKE ? " +
                "OR CAST(det.harga_jual AS CHAR) LIKE ? " +
                "OR CAST(det.subtotal AS CHAR) LIKE ?) "
            );
        }

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            st.setDate(idx++, new java.sql.Date(startDate.getTime()));
            st.setDate(idx++, new java.sql.Date(endDate.getTime()));

            if (keyword != null && !keyword.isBlank()) {
                for (int i = 0; i < 7; i++) {
                    st.setString(idx++, "%" + keyword + "%");
                }
            }

            ResultSet rs = st.executeQuery();
            return rs.next() ? rs.getInt("total") : 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }



    public void generateBarangKeluarReport(Date startDate, Date endDate, ExportType type) {
        try {
            JasperReport report = loadReport("/com/inventory/jasper/ReportBarangKeluar.jrxml");

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("name", name);
            parameters.put("role", role);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            parameters.put("LOGO_IMAGE", loadImage("/com/inventory/assets/Logo4.png"));
            parameters.put("REPORT_VIRTUALIZER", createVirtualizer());

            if (type == ExportType.EXCEL) {
                parameters.put("REPORT_FORMAT", "XLSX");
            }

            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);

            if (type == ExportType.PREVIEW) {
                showViewer(print);
                return;
            }

            // Tentukan ekstensi dan filter
            String extension = type == ExportType.PDF ? "pdf" : "xlsx";
            String keterangan = type == ExportType.PDF ? "PDF Files" : "Excel Files";

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            // Panggil util untuk Save File Dialog
            File fileToSave = TransparentOptionPane.showSaveFileDialog(
                    FormManager.getJFrame(),
                    "Export Report",
                    new FileNameExtensionFilter(keterangan, extension),
                    new File("Laporan Barang Keluar " + saveDate + "." + extension)
            );

            if (fileToSave == null) {
                return; // user cancel
            }

            String savePath = fileToSave.getAbsolutePath();
            if (!savePath.toLowerCase().endsWith("." + extension)) {
                savePath += "." + extension;
            }

            File finalFile = new File(savePath);
            if (finalFile.exists()) {
                int confirm = TransparentOptionPane.showConfirmDialog(
                        FormManager.getJFrame(),
                        "File sudah ada. Apakah Anda ingin menimpanya?",
                        "Konfirmasi Timpa File",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Export sesuai tipe
            if (type == ExportType.PDF) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();
            } else if (type == ExportType.EXCEL) {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);

                exporter.setConfiguration(configuration);
                exporter.exportReport();
            }

            TransparentOptionPane.showMessageDialog(
                    FormManager.getJFrame(),
                    "Laporan berhasil disimpan di : " + savePath,
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (Desktop.isDesktopSupported()) {
                File file = new File(savePath);
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransparentOptionPane.showErrorDialog(FormManager.getJFrame(), e);
        }
    }

    public void previewBarangKeluar(Date startDate, Date endDate) {
        generateBarangKeluarReport(startDate, endDate, ExportType.PREVIEW);
    }

    public void exportBarangKeluarPDF(Date startDate, Date endDate) {
        generateBarangKeluarReport(startDate, endDate, ExportType.PDF);
    }

    public void exportBarangKeluarExcel(Date startDate, Date endDate) {
        generateBarangKeluarReport(startDate, endDate, ExportType.EXCEL);
    }

    public List<StokOpnameDetail> getDataOpname(
            Date startDate,
            Date endDate,
            String keyword,
            int itemsPerPage,
            int currentPage
    ) {

        List<StokOpnameDetail> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT " +
            " so.tanggal_opname, " +
            " so.keterangan, " +
            " so.status, " +
            " b.barang_name, " +
            " det.stok_sistem, " +
            " det.stok_fisik, " +
            " det.selisih, " +
            " det.catatan " +
            "FROM stok_opname_detail det " +
            "INNER JOIN stok_opname so ON so.opname_id = det.opname_id " +
            "INNER JOIN barang b ON b.barang_id = det.barang_id " +
            "WHERE so.tanggal_opname BETWEEN ? AND ? "
        );

        // 🔍 Search
        if (keyword != null && !keyword.isBlank()) {
            sql.append(
                "AND ( " +
                " so.tanggal_opname LIKE ? " +
                " OR so.keterangan LIKE ? " +
                " OR so.status LIKE ? " +
                " OR b.barang_name LIKE ? " +
                " OR CAST(det.stok_sistem AS CHAR) LIKE ? " +
                " OR CAST(det.stok_fisik AS CHAR) LIKE ? " +
                " OR CAST(det.selisih AS CHAR) LIKE ? " +
                " OR det.catatan LIKE ? " +
                ") "
            );
        }

        sql.append("ORDER BY so.tanggal_opname DESC ");
        sql.append("LIMIT ? OFFSET ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setDate(index++, new java.sql.Date(startDate.getTime()));
            ps.setDate(index++, new java.sql.Date(endDate.getTime()));

            if (keyword != null && !keyword.isBlank()) {
                for (int i = 0; i < 8; i++) {
                    ps.setString(index++, "%" + keyword + "%");
                }
            }

            ps.setInt(index++, itemsPerPage);
            ps.setInt(index, (currentPage - 1) * itemsPerPage);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    // ================= Stok Opname =================
                    StokOpname opname = new StokOpname();
                    opname.setTanggalOpname(rs.getString("tanggal_opname"));
                    opname.setKeterangan(rs.getString("keterangan"));
                    opname.setStatus(rs.getString("status"));

                    // ================= Detail =================
                    StokOpnameDetail detail = new StokOpnameDetail();
                    detail.setStokOpname(opname);
                    detail.setStokSistem(rs.getInt("stok_sistem"));
                    detail.setStokFisik(rs.getInt("stok_fisik"));
                    detail.setSelisih(rs.getInt("selisih"));
                    detail.setCatatan(rs.getString("catatan"));

                    // ================= Barang =================
                    Barang barang = new Barang();
                    barang.setBarangName(rs.getString("barang_name"));
                    detail.setBarang(barang);

                    list.add(detail);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    public int getTotalItemsStokOpname(Date startDate, Date endDate, String keyword) {

        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) AS total " +
            "FROM stok_opname_detail det " +
            "INNER JOIN stok_opname so ON so.opname_id = det.opname_id " +
            "INNER JOIN barang b ON b.barang_id = det.barang_id " +
            "WHERE so.tanggal_opname BETWEEN ? AND ? "
        );

        if (keyword != null && !keyword.isBlank()) {
            sql.append(
                "AND ( " +
                " so.tanggal_opname LIKE ? " +
                " OR so.keterangan LIKE ? " +
                " OR so.status LIKE ? " +
                " OR b.barang_name LIKE ? " +
                " OR CAST(det.stok_sistem AS CHAR) LIKE ? " +
                " OR CAST(det.stok_fisik AS CHAR) LIKE ? " +
                " OR CAST(det.selisih AS CHAR) LIKE ? " +
                " OR det.catatan LIKE ? " +
                ") "
            );
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            ps.setDate(index++, new java.sql.Date(startDate.getTime()));
            ps.setDate(index++, new java.sql.Date(endDate.getTime()));

            if (keyword != null && !keyword.isBlank()) {
                for (int i = 0; i < 8; i++) {
                    ps.setString(index++, "%" + keyword + "%");
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public void generateOpnameReport(Date startDate, Date endDate, ExportType type) {
        try {
            JasperReport report = loadReport("/com/inventory/jasper/ReportStokOpname.jrxml");

            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("name", name);
            parameters.put("role", role);
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
            parameters.put("LOGO_IMAGE", loadImage("/com/inventory/assets/Logo4.png"));
            parameters.put("REPORT_VIRTUALIZER", createVirtualizer());

            if (type == ExportType.EXCEL) {
                parameters.put("REPORT_FORMAT", "XLSX");
            }

            JasperPrint print = JasperFillManager.fillReport(report, parameters, conn);

            if (type == ExportType.PREVIEW) {
                showViewer(print);
                return;
            }

            // Tentukan ekstensi dan filter
            String extension = type == ExportType.PDF ? "pdf" : "xlsx";
            String keterangan = type == ExportType.PDF ? "PDF Files" : "Excel Files";

            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String saveDate = df.format(date);

            // Panggil util untuk Save File Dialog
            File fileToSave = TransparentOptionPane.showSaveFileDialog(
                    FormManager.getJFrame(),
                    "Export Report",
                    new FileNameExtensionFilter(keterangan, extension),
                    new File("Laporan Stok Opname " + saveDate + "." + extension)
            );

            if (fileToSave == null) {
                return; // user cancel
            }

            String savePath = fileToSave.getAbsolutePath();
            if (!savePath.toLowerCase().endsWith("." + extension)) {
                savePath += "." + extension;
            }

            File finalFile = new File(savePath);
            if (finalFile.exists()) {
                int confirm = TransparentOptionPane.showConfirmDialog(
                        FormManager.getJFrame(),
                        "File sudah ada. Apakah Anda ingin menimpanya?",
                        "Konfirmasi Timpa File",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Export sesuai tipe
            if (type == ExportType.PDF) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));
                exporter.exportReport();
            } else if (type == ExportType.EXCEL) {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(savePath));

                SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                configuration.setRemoveEmptySpaceBetweenRows(true);
                configuration.setOnePagePerSheet(false);

                exporter.setConfiguration(configuration);
                exporter.exportReport();
            }

            TransparentOptionPane.showMessageDialog(
                    FormManager.getJFrame(),
                    "Laporan berhasil disimpan di : " + savePath,
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (Desktop.isDesktopSupported()) {
                File file = new File(savePath);
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransparentOptionPane.showErrorDialog(FormManager.getJFrame(), e);
        }
    }

    public void previewOpname(Date startDate, Date endDate) {
        generateOpnameReport(startDate, endDate, ExportType.PREVIEW);
    }

    public void exportOpnamePDF(Date startDate, Date endDate) {
        generateOpnameReport(startDate, endDate, ExportType.PDF);
    }

    public void exportOpnameExcel(Date startDate, Date endDate) {
        generateOpnameReport(startDate, endDate, ExportType.EXCEL);
    }

}
