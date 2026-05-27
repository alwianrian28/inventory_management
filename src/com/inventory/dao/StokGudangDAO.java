package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StokGudangDAO {

    private final Connection conn;

    public StokGudangDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(StokGudang model) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO stok_gudang "
                    + "(barang_id, jumlah, harga_beli, subtotal, rak_id, gudang_id, insert_by) "
                    + "VALUES (?,?,?,?,?,?,?)";

            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, model.getBarang().getBarangID());
            st.setInt(2, model.getJumlah());
            st.setDouble(3, model.getHargaBeli());
            st.setDouble(4, model.getSubtotal());
            st.setInt(5, model.getRak().getRakID());
            st.setInt(6, model.getGudang().getGudangID());
            st.setInt(7, model.getInsertBy());

            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setStokID(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void deleteData(StokGudang model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE stok_gudang SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE stok_id=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getStokID());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    public void deleteData(BarangMasuk model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE stok_gudang s "
                       + "INNER JOIN barang_masuk_detail det ON det.stok_id = s.stok_id "
                       + "SET s.delete_by = ?, s.delete_at = NOW(), s.is_delete = TRUE "
                       + "WHERE det.barang_masuk_id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getBarangMasukID());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void restoreData(int barangMasukID) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE stok_gudang s "
                       + "INNER JOIN barang_masuk_detail det ON det.stok_id = s.stok_id "
                       + "SET s.delete_by = NULL, s.delete_at = NULL, s.is_delete = FALSE "
                       + "WHERE det.barang_masuk_id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, barangMasukID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<StokGudang> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<StokGudang> list = new ArrayList<>();

        try {
            String sql = "SELECT  sg.stok_id, sg.jumlah, sg.harga_beli, sg.subtotal, sg.is_delete,  " +
                    "     b.barang_id, b.barang_name,  " +
                    "     r.rak_id, r.rak_name,  " +
                    "     g.gudang_id, g.gudang_name  " +
                    "    FROM stok_gudang sg  " +
                    "    JOIN barang b ON sg.barang_id = b.barang_id  " +
                    "    JOIN rak r ON sg.rak_id = r.rak_id  " +
                    "    JOIN gudang g ON sg.gudang_id = g.gudang_id  " +
                    "    WHERE sg.is_delete=?  " +
                    "    LIMIT ? OFFSET ?;";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (st != null) st.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }

    public List<StokGudang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<StokGudang> list = new ArrayList<>();

        try {
            String sql ="SELECT sg.stok_id, sg.jumlah, sg.harga_beli, sg.subtotal, sg.is_delete,  " +
                    "     b.barang_id, b.barang_name,  " +
                    "     r.rak_id, r.rak_name,  " +
                    "     g.gudang_id, g.gudang_name  " +
                    "    FROM stok_gudang sg  " +
                    "    JOIN barang b ON sg.barang_id = b.barang_id  " +
                    "    JOIN rak r ON sg.rak_id = r.rak_id  " +
                    "    JOIN gudang g ON sg.gudang_id = g.gudang_id  " +
                    "    WHERE sg.is_delete=?  " +
                    "    AND (b.barang_name LIKE ? OR r.rak_name LIKE ? OR g.gudang_name LIKE ?)  " +
                    "    LIMIT ? OFFSET ?;";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setInt(5, itemsPerPage);
            st.setInt(6, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();

            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (st != null) st.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql =
                    "SELECT COUNT(*) AS total " +
                    "FROM stok_gudang sg " +
                    "JOIN barang b ON sg.barang_id = b.barang_id " +
                    "WHERE sg.is_delete=? " +
                    "AND (b.barang_code LIKE ? OR b.barang_name LIKE ?)";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            rs = st.executeQuery();

            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private StokGudang mapResult(ResultSet rs) throws SQLException {
        StokGudang sg = new StokGudang();
        sg.setStokID(rs.getInt("stok_id"));
        sg.setJumlah(rs.getInt("jumlah"));
        sg.setHargaBeli(rs.getDouble("harga_beli"));
        sg.setSubtotal(rs.getDouble("subtotal"));
        
        Barang b = new Barang();
        b.setBarangID(rs.getInt("barang_id"));
        b.setBarangName(rs.getString("barang_name"));

        Rak r = new Rak();
        r.setRakID(rs.getInt("rak_id"));
        r.setRakName(rs.getString("rak_name"));

        Gudang g = new Gudang();
        g.setGudangID(rs.getInt("gudang_id"));
        g.setGudangName(rs.getString("gudang_name"));

        sg.setBarang(b);
        sg.setRak(r);
        sg.setGudang(g);

        return sg;
    }
}
