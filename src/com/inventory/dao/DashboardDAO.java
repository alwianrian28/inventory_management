package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.Barang;
import com.inventory.model.StokGudang;
import com.inventory.model.StokOpname;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dearclaudia
 */
public class DashboardDAO{

    private final Connection conn;

    public DashboardDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public int getTotalBarangs() {
        int total = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) AS total FROM barang WHERE is_delete = FALSE";
        try{
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            if(rs.next()){
                total = rs.getInt("total");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return total;
    }

    public int getTotalSuppliers() {
        int total = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) AS total FROM supplier WHERE is_delete = FALSE";
        try{
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            if(rs.next()){
                total = rs.getInt("total");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return total;
    }

    public int getTotalJumlahMasukPerHari() {
        int total = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT is_delete, IFNULL(SUM(total_jumlah),0) AS total "
               + "FROM barang_masuk "
               + "WHERE DATE(tanggal_masuk) = CURDATE() AND is_delete = FALSE";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    public double getTotalMasukPerHari() {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT is_delete, IFNULL(SUM(total_masuk),0) AS total "
               + "FROM barang_masuk "
               + "WHERE DATE(tanggal_masuk) = CURDATE() AND is_delete = FALSE";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getTotalJumlahKeluarPerHari() {
        int total = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT is_delete, IFNULL(SUM(total_jumlah),0) AS total "
               + "FROM barang_keluar "
               + "WHERE DATE(tanggal_keluar) = CURDATE() AND is_delete = FALSE";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    public double getTotalKeluarPerHari() {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT is_delete, IFNULL(SUM(total_keluar),0) AS total "
               + "FROM barang_keluar "
               + "WHERE DATE(tanggal_keluar) = CURDATE() AND is_delete = FALSE";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public Map<String, Double> getBarangMasukPerHariBulanIni() {
        PreparedStatement st = null;
        ResultSet rs = null;
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT is_delete, DATE(tanggal_masuk) AS tgl, SUM(total_masuk) AS total " +
                     "FROM barang_masuk " +
                     "WHERE is_delete = FALSE AND MONTH(tanggal_masuk) = MONTH(CURDATE()) " +
                     "AND YEAR(tanggal_masuk) = YEAR(CURDATE()) " +
                     "GROUP BY DATE(tanggal_masuk) " +
                     "ORDER BY DATE(tanggal_masuk)";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            while (rs.next()) {
                result.put(rs.getString("tgl"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Double> getBarangMasukPerBulanTahunIni() {
        PreparedStatement st = null;
        ResultSet rs = null;
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT is_delete, MONTH(tanggal_masuk) AS bln, SUM(total_masuk) AS total " +
                     "FROM barang_masuk " +
                     "WHERE is_delete = FALSE AND YEAR(tanggal_masuk) = YEAR(CURDATE()) " +
                     "GROUP BY MONTH(tanggal_masuk) " +
                     "ORDER BY MONTH(tanggal_masuk)";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            while (rs.next()) {
                int month = rs.getInt("bln");
                result.put(String.valueOf(month), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public Map<String, Double> getBarangKeluarPerHariBulanIni() {
        PreparedStatement st = null;
        ResultSet rs = null;
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT is_delete, DATE(tanggal_keluar) AS tgl, SUM(total_keluar) AS total " +
                     "FROM barang_keluar " +
                     "WHERE is_delete = FALSE AND MONTH(tanggal_keluar) = MONTH(CURDATE()) " +
                     "AND YEAR(tanggal_keluar) = YEAR(CURDATE()) " +
                     "GROUP BY DATE(tanggal_keluar) " +
                     "ORDER BY DATE(tanggal_keluar)";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            while (rs.next()) {
                result.put(rs.getString("tgl"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Double> getBarangKeluarPerBulanTahunIni() {
        PreparedStatement st = null;
        ResultSet rs = null;
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT is_delete, MONTH(tanggal_keluar) AS bln, SUM(total_keluar) AS total " +
                     "FROM barang_keluar " +
                     "WHERE is_delete = FALSE AND YEAR(tanggal_keluar) = YEAR(CURDATE()) " +
                     "GROUP BY MONTH(tanggal_keluar) " +
                     "ORDER BY MONTH(tanggal_keluar)";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            while (rs.next()) {
                int month = rs.getInt("bln");
                result.put(String.valueOf(month), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public Map<String, Integer> getTop10Barang() {
        PreparedStatement st = null;
        ResultSet rs = null;
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT b.barang_name, SUM(sd.jumlah) AS qty " +
                     "FROM barang_keluar_detail sd " +
                     "JOIN barang b ON b.barang_id = sd.barang_id " +
                     "GROUP BY b.barang_id, b.barang_name " +
                     "ORDER BY qty DESC " +
                     "LIMIT 10";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            while (rs.next()) {
                result.put(rs.getString("barang_name"), rs.getInt("qty"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<StokGudang> getLowStockBarangs() {
        List<StokGudang> list = new ArrayList<>();

        String sql = "SELECT b.barang_code,\n" +
                    "       b.barang_name,\n" +
                    "       COALESCE(SUM(s.jumlah), 0) AS total_jumlah,\n" +
                    "       b.stok_minimum\n" +
                    "FROM barang b\n" +
                    "LEFT JOIN stok_gudang s ON b.barang_id = s.barang_id\n" +
                    "AND s.is_delete = 0\n" +
                    "WHERE b.is_delete = 0\n" +
                    "GROUP BY b.barang_id,\n" +
                    "         b.barang_code,\n" +
                    "         b.barang_name,\n" +
                    "         b.stok_minimum\n" +
                    "HAVING COALESCE(SUM(s.jumlah), 0) <= b.stok_minimum\n" +
                    "ORDER BY total_jumlah ASC";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Barang b = new Barang();
                b.setBarangCode(rs.getString("barang_code"));
                b.setBarangName(rs.getString("barang_name"));
                b.setStokMinimum(rs.getInt("stok_minimum"));

                StokGudang sto = new StokGudang();
                sto.setJumlah(rs.getInt("total_jumlah"));
                sto.setBarang(b);

                list.add(sto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }



    public List<StokOpname> getPendingStockOpname() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<StokOpname> list = new ArrayList<>();
        String sql = "SELECT tanggal_opname, keterangan, status, is_delete " +
                     "FROM stok_opname " +
                     "WHERE is_delete = FALSE AND status IN ('draft') " +
                     "ORDER BY tanggal_opname DESC";
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                StokOpname opname = new StokOpname();
                opname.setTanggalOpname(rs.getString("tanggal_opname"));
                opname.setKeterangan(rs.getString("keterangan"));
                opname.setStatus(rs.getString("status"));
                list.add(opname);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
