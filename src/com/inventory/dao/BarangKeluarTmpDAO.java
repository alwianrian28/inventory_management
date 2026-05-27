package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluarTmpDAO {

    private final Connection conn;

    public BarangKeluarTmpDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    /* ===================== INSERT ===================== */
    public void insertData(BarangKeluarTmp tmp) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO barang_keluar_tmp "
                    + "(barang_id, jumlah, harga_jual, subtotal) "
                    + "VALUES (?,?,?,?)";

            st = conn.prepareStatement(sql);
            st.setInt(1, tmp.getBarang().getBarangID());
            st.setInt(2, tmp.getJumlah());
            st.setDouble(3, tmp.getHargaJual());
            st.setDouble(4, tmp.getSubtotal());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }
    }

    /* ===================== UPDATE ===================== */
    public void updateData(BarangKeluarTmp tmp) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE barang_keluar_tmp SET "
                    + "barang_id=?, jumlah=?, harga_jual=?, subtotal=? "
                    + "WHERE barang_keluar_tmp_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, tmp.getBarang().getBarangID());
            st.setInt(2, tmp.getJumlah());
            st.setDouble(3, tmp.getHargaJual());
            st.setDouble(4, tmp.getSubtotal());
            st.setInt(5, tmp.getBarangKeluarTmpID());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }
    }

    /* ===================== DELETE PER ITEM ===================== */
    public void deleteData(int barangKeluarTmpID) {
        PreparedStatement st = null;

        try {
            String sql = "DELETE FROM barang_keluar_tmp WHERE barang_keluar_tmp_id=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, barangKeluarTmpID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }
    }

    /* ===================== DELETE ALL TMP ===================== */
    public void deleteAllTmp() {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("DELETE FROM barang_keluar_tmp");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }
    }

    /* ===================== RESET AUTO INCREMENT ===================== */
    public void resetAI() {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("ALTER TABLE barang_keluar_tmp AUTO_INCREMENT = 1");
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }
    }

    /* ===================== GET DATA ===================== */
    public List<BarangKeluarTmp> getData() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<BarangKeluarTmp> list = new ArrayList<>();

        try {
            String sql = "SELECT "
                    + "t.barang_keluar_tmp_id, t.jumlah, t.harga_jual, t.subtotal, "
                    + "b.barang_id, b.barcode, b.barang_name, "
                    + "s.satuan_id, s.satuan_name "
                    + "FROM barang_keluar_tmp t "
                    + "JOIN barang b ON t.barang_id = b.barang_id "
                    + "JOIN satuan s ON b.satuan_id = s.satuan_id ";

            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }
        return list;
    }

    /* ===================== SUM JUMLAH ===================== */
    public int sumJumlah() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT COALESCE(SUM(jumlah),0) FROM barang_keluar_tmp"
            );
            rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /* ===================== SUM TOTAL JUAL ===================== */
    public double sumTotalJual() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT COALESCE(SUM(subtotal),0) FROM barang_keluar_tmp"
            );
            rs = st.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /* ===================== MAPPER ===================== */
    private BarangKeluarTmp mapResult(ResultSet rs) throws SQLException {
        BarangKeluarTmp tmp = new BarangKeluarTmp();
        tmp.setBarangKeluarTmpID(rs.getInt("barang_keluar_tmp_id"));
        tmp.setJumlah(rs.getInt("jumlah"));
        tmp.setHargaJual(rs.getDouble("harga_jual"));
        tmp.setSubtotal(rs.getDouble("subtotal"));

        Barang b = new Barang();
        b.setBarangID(rs.getInt("barang_id"));
        b.setBarcode(rs.getString("barcode"));
        b.setBarangName(rs.getString("barang_name"));

        Satuan s = new Satuan();
        s.setSatuanID(rs.getInt("satuan_id"));
        s.setSatuanName(rs.getString("satuan_name"));
        b.setSatuan(s);
        
        tmp.setBarang(b);
        return tmp;
    }
}
