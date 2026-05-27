package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.BarangKeluar;
import com.inventory.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluarDAO {

    private final Connection conn;

    public BarangKeluarDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    /* ===================== INSERT ===================== */
    public void insertData(BarangKeluar keluar) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO barang_keluar "
                    + "(tanggal_keluar, no_transaksi, jenis_keluar, tujuan, "
                    + "total_jumlah, total_keluar, user_id, insert_by) "
                    + "VALUES (?,?,?,?,?,?,?,?)";

            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setDate(1, java.sql.Date.valueOf(keluar.getTanggalKeluar()));
            st.setString(2, keluar.getNoTransaksi());
            st.setString(3, keluar.getJenisKeluar());
            st.setString(4, keluar.getTujuan());
            st.setInt(5, keluar.getTotalJumlah());
            st.setDouble(6, keluar.getTotalKeluar());
            st.setInt(7, keluar.getUser().getUserID());
            st.setInt(8, keluar.getInsertBy());

            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    keluar.setBarangKeluarID(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }
    }

    /* ===================== DELETE (SOFT) ===================== */
    public void deleteData(BarangKeluar keluar) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE barang_keluar "
                    + "SET delete_by=?, delete_at=NOW(), is_delete=TRUE "
                    + "WHERE barang_keluar_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, keluar.getDeleteBy());
            st.setInt(2, keluar.getBarangKeluarID());

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }
    }

    /* ===================== RESTORE ===================== */
    public void restoreData(int barangKeluarID) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE barang_keluar "
                    + "SET delete_by=NULL, delete_at=NULL, is_delete=FALSE "
                    + "WHERE barang_keluar_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, barangKeluarID);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }
    }

    /* ===================== GET DATA ===================== */
    public List<BarangKeluar> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<BarangKeluar> list = new ArrayList<>();

        try {
            String sql = "SELECT "
                    + "bk.barang_keluar_id, bk.tanggal_keluar, bk.no_transaksi, "
                    + "bk.jenis_keluar, bk.tujuan, bk.total_jumlah, bk.total_keluar, "
                    + "u.user_id, u.name "
                    + "FROM barang_keluar bk "
                    + "JOIN user u ON bk.user_id = u.user_id "
                    + "WHERE bk.is_delete=? "
                    + "ORDER BY bk.barang_keluar_id ASC "
                    + "LIMIT ? OFFSET ?";

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
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }

        return list;
    }

    /* ===================== SEARCH ===================== */
    public List<BarangKeluar> searchData(String keyword, boolean isDelete,
                                         int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<BarangKeluar> list = new ArrayList<>();

        try {
            String sql = "SELECT "
                    + "bk.barang_keluar_id, bk.tanggal_keluar, bk.no_transaksi, "
                    + "bk.jenis_keluar, bk.tujuan, bk.total_jumlah, bk.total_keluar, "
                    + "u.user_id, u.name "
                    + "FROM barang_keluar bk "
                    + "JOIN user u ON bk.user_id = u.user_id "
                    + "WHERE bk.is_delete=? AND ("
                    + "bk.no_transaksi LIKE ? OR bk.jenis_keluar LIKE ? OR bk.tujuan LIKE ?"
                    + ") "
                    + "LIMIT ? OFFSET ?";

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
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }

        return list;
    }

    /* ===================== TOTAL ===================== */
    public int getTotalItems(boolean isDelete, String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) AS total "
                    + "FROM barang_keluar "
                    + "WHERE is_delete=? AND ("
                    + "no_transaksi LIKE ? OR jenis_keluar LIKE ? OR tujuan LIKE ?"
                    + ")";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");

            rs = st.executeQuery();
            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }

        return 0;
    }

    /* ===================== GENERATE CODE ===================== */
    public String generateBarangKeluarCode() {
        PreparedStatement st = null;
        ResultSet rs = null;
        String result = null;

        try {
            String ym = new SimpleDateFormat("yyyyMM").format(new Date());
            String prefix = "BK-" + ym + "-";

            String sql = "SELECT no_transaksi FROM barang_keluar "
                       + "WHERE no_transaksi LIKE ? "
                       + "ORDER BY no_transaksi DESC LIMIT 1";

            st = conn.prepareStatement(sql);
            st.setString(1, prefix + "%");
            rs = st.executeQuery();

            int nomor = 1;
            if (rs.next()) {
                String lastCode = rs.getString("no_transaksi");
                nomor = Integer.parseInt(lastCode.substring(lastCode.length() - 3)) + 1;
            }

            result = prefix + String.format("%03d", nomor);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (st != null) st.close(); } catch (SQLException ignored) {}
        }

        return result;
    }

    /* ===================== MAPPER ===================== */
    private BarangKeluar mapResult(ResultSet rs) throws SQLException {
        BarangKeluar bk = new BarangKeluar();
        bk.setBarangKeluarID(rs.getInt("barang_keluar_id"));
        bk.setTanggalKeluar(rs.getString("tanggal_keluar"));
        bk.setNoTransaksi(rs.getString("no_transaksi"));
        bk.setJenisKeluar(rs.getString("jenis_keluar"));
        bk.setTujuan(rs.getString("tujuan"));
        bk.setTotalJumlah(rs.getInt("total_jumlah"));
        bk.setTotalKeluar(rs.getDouble("total_keluar"));

        User u = new User();
        u.setUserID(rs.getInt("user_id"));
        u.setName(rs.getString("name"));
        bk.setUser(u);

        return bk;
    }
    
    public void updateStok(int barangKeluarID) {
        String sqlDetail =
                "SELECT barang_id, jumlah " +
                "FROM barang_keluar_detail " +
                "WHERE barang_keluar_id = ?";

        try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
            psDetail.setInt(1, barangKeluarID);

            try (ResultSet rsDetail = psDetail.executeQuery()) {
                while (rsDetail.next()) {
                    int barangID = rsDetail.getInt("barang_id");
                    int qtyKeluar = rsDetail.getInt("jumlah");
                    reduceStokFIFO(barangID, qtyKeluar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void reduceStokFIFO(int barangID, int qtyKeluar) throws SQLException {

        String sqlStock =
                "SELECT stok_id, jumlah " +
                "FROM stok_gudang " +
                "WHERE barang_id = ? AND jumlah > 0 " +
                "ORDER BY stok_id ASC";

        try (PreparedStatement psStock = conn.prepareStatement(
                sqlStock,
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE)) {

            psStock.setInt(1, barangID);

            try (ResultSet rsStock = psStock.executeQuery()) {
                int sisa = qtyKeluar;

                while (rsStock.next() && sisa > 0) {
                    int stok = rsStock.getInt("jumlah");

                    if (stok >= sisa) {
                        rsStock.updateInt("jumlah", stok - sisa);
                        rsStock.updateRow();
                        sisa = 0;
                    } else {
                        rsStock.updateInt("jumlah", 0);
                        rsStock.updateRow();
                        sisa -= stok;
                    }
                }
            }
        }
    }

    public void rollbackStok(int barangKeluarID){
        try {
            String sqlDetail =
                "SELECT barang_id, jumlah " +
                "FROM barang_keluar_detail " +
                "WHERE barang_keluar_id = ?";

            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                psDetail.setInt(1, barangKeluarID);

                try (ResultSet rs = psDetail.executeQuery()) {
                    while (rs.next()) {
                        int barangID = rs.getInt("barang_id");
                        int qty = rs.getInt("jumlah");
                        restoreStokLIFO(barangID, qty);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void restoreStokLIFO(int barangID, int qty) throws SQLException {

        String sql =
            "SELECT stok_id, jumlah " +
            "FROM stok_gudang " +
            "WHERE barang_id = ? " +
            "ORDER BY stok_id ASC";

        try (PreparedStatement ps = conn.prepareStatement(
                sql,
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE)) {

            ps.setInt(1, barangID);

            try (ResultSet rs = ps.executeQuery()) {
                int sisa = qty;

                while (rs.next() && sisa > 0) {
                    int stok = rs.getInt("jumlah");
                    rs.updateInt("jumlah", stok + sisa);
                    rs.updateRow();
                    sisa = 0;
                }
            }
        }
    }


}
