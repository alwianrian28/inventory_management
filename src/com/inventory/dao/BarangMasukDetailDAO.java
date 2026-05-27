package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BarangMasukDetailDAO {

    private final Connection conn;

    private static final String BASE_SELECT =
            "SELECT "
            + " d.barang_masuk_detail_id, d.jumlah, d.harga_beli, d.subtotal, "
            + " bm.barang_masuk_id, bm.no_transaksi, bm.no_nota, bm.tanggal_masuk, "
            + " sg.stok_id, "
            + " b.barang_id, b.barang_name, b.barcode, "
            + " s.satuan_id, s.satuan_name, "
            + " r.rak_id, r.rak_name, "
            + " g.gudang_id, g.gudang_name, "
            + " sup.supplier_id, sup.supplier_name "
            + "FROM barang_masuk_detail d "
            + "JOIN barang_masuk bm ON d.barang_masuk_id = bm.barang_masuk_id "
            + "JOIN supplier sup ON bm.supplier_id = sup.supplier_id "
            + "JOIN stok_gudang sg ON d.stok_id = sg.stok_id "
            + "JOIN barang b ON sg.barang_id = b.barang_id "
            + "JOIN satuan s ON b.satuan_id = s.satuan_id "
            + "JOIN rak r ON d.rak_id = r.rak_id "
            + "JOIN gudang g ON d.gudang_id = g.gudang_id ";

    private static final String SEARCH_CLAUSE =
            "AND ( "
            + " bm.no_transaksi LIKE ? "
            + " OR bm.no_nota LIKE ? "
            + " OR bm.tanggal_masuk LIKE ? "
            + " OR b.barcode LIKE ? "
            + " OR b.barang_name LIKE ? "
            + " OR s.satuan_name LIKE ? "
            + " OR r.rak_name LIKE ? "
            + " OR g.gudang_name LIKE ? "
            + " OR sup.supplier_name LIKE ? "
            + ") ";

    private static final int SEARCH_PARAM_COUNT = 9;

    public BarangMasukDetailDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(BarangMasukDetail d) {
        String sql = "INSERT INTO barang_masuk_detail "
                + "(barang_masuk_id, stok_id, jumlah, harga_beli, subtotal, rak_id, gudang_id) "
                + "VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, d.getBarangMasuk().getBarangMasukID());
            st.setInt(2, d.getStokGudang().getStokID());
            st.setInt(3, d.getJumlah());
            st.setDouble(4, d.getHargaBeli());
            st.setDouble(5, d.getSubtotal());
            st.setInt(6, d.getRak().getRakID());
            st.setInt(7, d.getGudang().getGudangID());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BarangMasukDetail> getDataById(int barangMasukID, int limit, int page) {
        List<BarangMasukDetail> list = new ArrayList<>();

        String sql = BASE_SELECT + "WHERE d.barang_masuk_id = ? LIMIT ? OFFSET ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, barangMasukID);
            st.setInt(2, limit);
            st.setInt(3, (page - 1) * limit);

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

    public List<BarangMasukDetail> searchDataById(
            int barangMasukID,
            String keyword,
            int limit,
            int page
    ) {
        List<BarangMasukDetail> list = new ArrayList<>();

        String sql = BASE_SELECT + "WHERE d.barang_masuk_id = ? " + SEARCH_CLAUSE + "LIMIT ? OFFSET ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            int idx = 1;
            st.setInt(idx++, barangMasukID);

            String search = "%" + keyword + "%";
            for (int j = 0; j < SEARCH_PARAM_COUNT; j++) {
                st.setString(idx++, search);
            }

            st.setInt(idx++, limit);
            st.setInt(idx, (page - 1) * limit);

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

    public int getTotalItemsById(int barangMasukID, String keyword) {
        String sql =
            "SELECT COUNT(*) AS total "
            + "FROM barang_masuk_detail d "
            + "JOIN barang_masuk bm ON d.barang_masuk_id = bm.barang_masuk_id "
            + "JOIN supplier sup ON bm.supplier_id = sup.supplier_id "
            + "JOIN stok_gudang sg ON d.stok_id = sg.stok_id "
            + "JOIN barang b ON sg.barang_id = b.barang_id "
            + "JOIN satuan s ON b.satuan_id = s.satuan_id "
            + "JOIN rak r ON d.rak_id = r.rak_id "
            + "JOIN gudang g ON d.gudang_id = g.gudang_id "
            + "WHERE d.barang_masuk_id = ? "
            + SEARCH_CLAUSE;

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            int idx = 1;
            st.setInt(idx++, barangMasukID);

            String search = "%" + keyword + "%";
            for (int j = 0; j < SEARCH_PARAM_COUNT; j++) {
                st.setString(idx++, search);
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

    private BarangMasukDetail mapResult(ResultSet rs) throws SQLException {
        BarangMasukDetail d = new BarangMasukDetail();
        d.setBarangMasukDetailID(rs.getInt("barang_masuk_detail_id"));
        d.setJumlah(rs.getInt("jumlah"));
        d.setHargaBeli(rs.getDouble("harga_beli"));
        d.setSubtotal(rs.getDouble("subtotal"));

        BarangMasuk bm = new BarangMasuk();
        bm.setBarangMasukID(rs.getInt("barang_masuk_id"));
        bm.setNoTransaksi(rs.getString("no_transaksi"));
        bm.setNoNota(rs.getString("no_nota"));
        bm.setTanggalMasuk(rs.getString("tanggal_masuk"));

        Supplier sup = new Supplier();
        sup.setSupplierID(rs.getInt("supplier_id"));
        sup.setSupplierName(rs.getString("supplier_name"));
        bm.setSupplier(sup);

        Barang b = new Barang();
        b.setBarangID(rs.getInt("barang_id"));
        b.setBarangName(rs.getString("barang_name"));
        b.setBarcode(rs.getString("barcode"));

        Satuan s = new Satuan();
        s.setSatuanID(rs.getInt("satuan_id"));
        s.setSatuanName(rs.getString("satuan_name"));
        b.setSatuan(s);

        StokGudang sg = new StokGudang();
        sg.setStokID(rs.getInt("stok_id"));
        sg.setBarang(b);

        Rak r = new Rak();
        r.setRakID(rs.getInt("rak_id"));
        r.setRakName(rs.getString("rak_name"));

        Gudang g = new Gudang();
        g.setGudangID(rs.getInt("gudang_id"));
        g.setGudangName(rs.getString("gudang_name"));

        d.setBarangMasuk(bm);
        d.setStokGudang(sg);
        d.setRak(r);
        d.setGudang(g);

        return d;
    }
}
