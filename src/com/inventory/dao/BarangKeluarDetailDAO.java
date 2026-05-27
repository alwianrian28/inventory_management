package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BarangKeluarDetailDAO {

    private final Connection conn;

    public BarangKeluarDetailDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    // ================= INSERT =================
    public void insertData(BarangKeluarDetail d) {
        String sql = "INSERT INTO barang_keluar_detail "
                + "(barang_keluar_id, barang_id, jumlah, harga_jual, subtotal) "
                + "VALUES (?,?,?,?,?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, d.getBarangKeluar().getBarangKeluarID());
            st.setInt(2, d.getBarang().getBarangID());
            st.setInt(3, d.getJumlah());
            st.setDouble(4, d.getHargaJual());
            st.setDouble(5, d.getSubtotal());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= GET DATA =================
    public List<BarangKeluarDetail> getDataById(int barangKeluarID, int limit, int page) {
        List<BarangKeluarDetail> list = new ArrayList<>();

        String sql = baseSelect()
                + " WHERE d.barang_keluar_id = ? "
                + " LIMIT ? OFFSET ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, barangKeluarID);
            st.setInt(2, limit);
            st.setInt(3, (page - 1) * limit);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= SEARCH =================
    public List<BarangKeluarDetail> searchDataById(
            int barangKeluarID,
            String keyword,
            int limit,
            int page
    ) {
        List<BarangKeluarDetail> list = new ArrayList<>();

        String sql = baseSelect()
                + " WHERE d.barang_keluar_id = ? "
                + " AND ( "
                + "   bk.no_transaksi LIKE ? "
                + "   OR bk.tanggal_keluar LIKE ? "
                + "   OR b.barang_name LIKE ? "
                + "   OR b.barcode LIKE ? "
                + " ) "
                + " LIMIT ? OFFSET ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            int i = 1;
            st.setInt(i++, barangKeluarID);

            String search = "%" + keyword + "%";
            for (int j = 0; j < 4; j++) {
                st.setString(i++, search);
            }

            st.setInt(i++, limit);
            st.setInt(i, (page - 1) * limit);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ================= TOTAL =================
    public int getTotalItemsById(int barangKeluarID, String keyword) {
        String sql =
            "SELECT COUNT(*) AS total "
          + "FROM barang_keluar_detail d "
          + "JOIN barang_keluar bk ON d.barang_keluar_id = bk.barang_keluar_id "
          + "JOIN barang b ON d.barang_id = b.barang_id "
          + "WHERE d.barang_keluar_id = ? "
          + "AND ( "
          + " bk.no_transaksi LIKE ? "
          + " OR bk.tanggal_keluar LIKE ? "
          + " OR b.barang_name LIKE ? "
          + " OR b.barcode LIKE ? "
          + " )";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            int i = 1;
            st.setInt(i++, barangKeluarID);

            String search = "%" + keyword + "%";
            for (int j = 0; j < 4; j++) {
                st.setString(i++, search);
            }

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================= SQL BASE =================
    private String baseSelect() {
        return "SELECT "
                + " d.barang_keluar_detail_id, d.jumlah, d.harga_jual, d.subtotal, "
                + " bk.barang_keluar_id, bk.no_transaksi, bk.tanggal_keluar, "
                + " b.barang_id, b.barang_name, b.barcode, "
                + " s.satuan_id, s.satuan_name "
                + "FROM barang_keluar_detail d "
                + "JOIN barang_keluar bk ON d.barang_keluar_id = bk.barang_keluar_id "
                + "JOIN barang b ON d.barang_id = b.barang_id "
                + "JOIN satuan s ON b.satuan_id = s.satuan_id ";
    }

    // ================= MAPPER =================
    private BarangKeluarDetail mapResult(ResultSet rs) throws SQLException {

        BarangKeluarDetail d = new BarangKeluarDetail();
        d.setBarangKeluarDetailID(rs.getInt("barang_keluar_detail_id"));
        d.setJumlah(rs.getInt("jumlah"));
        d.setHargaJual(rs.getDouble("harga_jual"));
        d.setSubtotal(rs.getDouble("subtotal"));

        // Barang Keluar
        BarangKeluar bk = new BarangKeluar();
        bk.setBarangKeluarID(rs.getInt("barang_keluar_id"));
        bk.setNoTransaksi(rs.getString("no_transaksi"));
        bk.setTanggalKeluar(rs.getString("tanggal_keluar"));

        // Barang
        Barang b = new Barang();
        b.setBarangID(rs.getInt("barang_id"));
        b.setBarangName(rs.getString("barang_name"));
        b.setBarcode(rs.getString("barcode"));

        Satuan s = new Satuan();
        s.setSatuanID(rs.getInt("satuan_id"));
        s.setSatuanName(rs.getString("satuan_name"));
        b.setSatuan(s);
        
        d.setBarangKeluar(bk);
        d.setBarang(b);

        return d;
    }
}
