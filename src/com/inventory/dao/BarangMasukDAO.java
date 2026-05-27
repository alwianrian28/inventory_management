package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.BarangMasuk;
import com.inventory.model.Supplier;
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
public class BarangMasukDAO {

    private final Connection conn;

    private static final String BASE_SELECT =
            "SELECT bm.barang_masuk_id, bm.no_transaksi, bm.no_nota, bm.tanggal_masuk, "
            + "bm.total_jumlah, bm.total_masuk, "
            + "s.supplier_id, s.supplier_name, "
            + "u.user_id, u.name "
            + "FROM barang_masuk bm "
            + "JOIN supplier s ON bm.supplier_id = s.supplier_id "
            + "JOIN user u ON bm.user_id = u.user_id ";

    public BarangMasukDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(BarangMasuk masuk) {
        String sql = "INSERT INTO barang_masuk "
                + "(no_transaksi, no_nota, tanggal_masuk, total_jumlah, total_masuk, "
                + "supplier_id, user_id, insert_by) "
                + "VALUES (?,?,?,?,?,?,?,?)";

        try (PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, masuk.getNoTransaksi());
            st.setString(2, masuk.getNoNota());
            st.setDate(3, java.sql.Date.valueOf(masuk.getTanggalMasuk()));
            st.setInt(4, masuk.getTotalJumlah());
            st.setDouble(5, masuk.getTotalMasuk());
            st.setInt(6, masuk.getSupplier().getSupplierID());
            st.setInt(7, masuk.getUser().getUserID());
            st.setInt(8, masuk.getInsertBy());

            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    masuk.setBarangMasukID(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(BarangMasuk masuk) {
        String sql = "UPDATE barang_masuk "
                + "SET delete_by=?, delete_at=NOW(), is_delete=TRUE "
                + "WHERE barang_masuk_id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, masuk.getDeleteBy());
            st.setInt(2, masuk.getBarangMasukID());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restoreData(int barangMasukID) {
        String sql = "UPDATE barang_masuk "
                + "SET delete_by=NULL, delete_at=NULL, is_delete=FALSE "
                + "WHERE barang_masuk_id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, barangMasukID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BarangMasuk> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        List<BarangMasuk> list = new ArrayList<>();

        String sql = BASE_SELECT
                + "WHERE bm.is_delete=? "
                + "ORDER BY bm.barang_masuk_id ASC "
                + "LIMIT ? OFFSET ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResult(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BarangMasuk> searchData(String keyword, boolean isDelete,
                                        int itemsPerPage, int currentPage) {
        List<BarangMasuk> list = new ArrayList<>();

        String sql = BASE_SELECT
                + "WHERE bm.is_delete=? AND ("
                + "bm.no_transaksi LIKE ? OR bm.no_nota LIKE ? OR s.supplier_name LIKE ?"
                + ") "
                + "LIMIT ? OFFSET ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";
            st.setBoolean(1, isDelete);
            st.setString(2, search);
            st.setString(3, search);
            st.setString(4, search);
            st.setInt(5, itemsPerPage);
            st.setInt(6, (currentPage - 1) * itemsPerPage);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResult(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        String sql = "SELECT COUNT(*) AS total "
                + "FROM barang_masuk bm "
                + "JOIN supplier s ON bm.supplier_id = s.supplier_id "
                + "WHERE bm.is_delete=? AND "
                + "(bm.no_transaksi LIKE ? OR bm.no_nota LIKE ? OR s.supplier_name LIKE ?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            String search = "%" + keyword + "%";
            st.setBoolean(1, isDelete);
            st.setString(2, search);
            st.setString(3, search);
            st.setString(4, search);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String generateBarangMasukCode() {
        try {
            String ym = new SimpleDateFormat("yyyyMM").format(new Date());
            String prefix = "BM-" + ym + "-";

            String sql = "SELECT no_transaksi FROM barang_masuk "
                       + "WHERE no_transaksi LIKE ? "
                       + "ORDER BY no_transaksi DESC LIMIT 1";

            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, prefix + "%");

                try (ResultSet rs = st.executeQuery()) {
                    int nomor = 1;
                    if (rs.next()) {
                        String lastCode = rs.getString("no_transaksi");
                        nomor = Integer.parseInt(lastCode.substring(lastCode.length() - 3)) + 1;
                    }
                    return prefix + String.format("%03d", nomor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BarangMasuk mapResult(ResultSet rs) throws SQLException {
        BarangMasuk bm = new BarangMasuk();
        bm.setBarangMasukID(rs.getInt("barang_masuk_id"));
        bm.setNoTransaksi(rs.getString("no_transaksi"));
        bm.setNoNota(rs.getString("no_nota"));
        bm.setTanggalMasuk(rs.getString("tanggal_masuk"));
        bm.setTotalJumlah(rs.getInt("total_jumlah"));
        bm.setTotalMasuk(rs.getDouble("total_masuk"));

        Supplier s = new Supplier();
        s.setSupplierID(rs.getInt("supplier_id"));
        s.setSupplierName(rs.getString("supplier_name"));

        User u = new User();
        u.setUserID(rs.getInt("user_id"));
        u.setName(rs.getString("name"));

        bm.setSupplier(s);
        bm.setUser(u);

        return bm;
    }
}
