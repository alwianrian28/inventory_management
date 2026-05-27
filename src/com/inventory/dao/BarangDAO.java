package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangDAO {

    private final Connection conn;

    public BarangDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(Barang model) {
        String sql = "INSERT INTO barang "
                + "(barang_code, barcode, barang_name, merk_id, kategori_id, satuan_id, "
                + "harga_jual, stok_minimum, supplier_id, photo, insert_by) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, model.getBarangCode());
            st.setString(2, model.getBarcode());
            st.setString(3, model.getBarangName());
            st.setInt(4, model.getMerk().getMerkID());
            st.setInt(5, model.getKategori().getKategoriID());
            st.setInt(6, model.getSatuan().getSatuanID());
            st.setDouble(7, model.getHargaJual());
            st.setInt(8, model.getStokMinimum());
            st.setInt(9, model.getSupplier().getSupplierID());
            st.setString(10, model.getPhoto());
            st.setInt(11, model.getInsertBy());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(Barang model) {
        String sql = "UPDATE barang SET "
                + "barang_code=?, barcode=?, barang_name=?, "
                + "merk_id=?, kategori_id=?, satuan_id=?, "
                + "harga_jual=?, stok_minimum=?, "
                + "supplier_id=?, photo=?, "
                + "update_by=?, update_at=NOW() "
                + "WHERE barang_id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, model.getBarangCode());
            st.setString(2, model.getBarcode());
            st.setString(3, model.getBarangName());
            st.setInt(4, model.getMerk().getMerkID());
            st.setInt(5, model.getKategori().getKategoriID());
            st.setInt(6, model.getSatuan().getSatuanID());
            st.setDouble(7, model.getHargaJual());
            st.setInt(8, model.getStokMinimum());
            st.setInt(9, model.getSupplier().getSupplierID());
            st.setString(10, model.getPhoto());
            st.setInt(11, model.getUpdateBy());
            st.setInt(12, model.getBarangID());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(Barang model) {
        String sql = "UPDATE barang SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE barang_id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getBarangID());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restoreData(int barangID) {
        String sql = "UPDATE barang SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE barang_id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, barangID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Barang getDataById(int barangID) {
        String sql = baseSelectSingle()
                + "WHERE b.barang_id = ? AND b.is_delete = FALSE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, barangID);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResult(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Barang getDataByCode(String barangCode) {
        String sql = baseSelectWithStock()
                + "WHERE b.barang_code = ? AND b.is_delete = FALSE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, barangCode);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResult(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Barang getDataByBarcode(String barcode) {
        String sql = baseSelectWithStock()
                + "WHERE b.barcode = ? AND b.is_delete = FALSE";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, barcode);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return mapResult(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Barang> getDataBySupplier(
            String supplierName,
            boolean isDelete,
            int itemsPerPage,
            int currentPage
    ) {
        List<Barang> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(baseSelectWithStock());
        sql.append("WHERE b.is_delete = ? ");

        if (supplierName != null) {
            sql.append("AND sp.supplier_name = ? ");
        }

        sql.append("GROUP BY b.barang_id ORDER BY b.barang_name ASC LIMIT ? OFFSET ?");

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            st.setBoolean(idx++, isDelete);

            if (supplierName != null) {
                st.setString(idx++, supplierName);
            }

            st.setInt(idx++, itemsPerPage);
            st.setInt(idx, (currentPage - 1) * itemsPerPage);

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

    public List<Barang> searchDataBySupplier(
            String supplierName,
            String keyword,
            boolean isDelete,
            int itemsPerPage,
            int currentPage
    ) {
        List<Barang> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(baseSelectWithStock());
        sql.append("WHERE b.is_delete = ? ");

        if (supplierName != null) {
            sql.append("AND sp.supplier_name = ? ");
        }

        sql.append(
            "AND ( " +
            " b.barang_code LIKE ? OR " +
            " b.barang_name LIKE ? OR " +
            " b.barcode LIKE ? OR " +
            " m.merk_name LIKE ? OR " +
            " k.kategori_name LIKE ? OR " +
            " s.satuan_name LIKE ? " +
            ") " +
            "GROUP BY b.barang_id " +
            "ORDER BY b.barang_name ASC " +
            "LIMIT ? OFFSET ?"
        );

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            st.setBoolean(idx++, isDelete);

            if (supplierName != null) {
                st.setString(idx++, supplierName);
            }

            String search = "%" + keyword + "%";
            for (int i = 0; i < 6; i++) {
                st.setString(idx++, search);
            }

            st.setInt(idx++, itemsPerPage);
            st.setInt(idx, (currentPage - 1) * itemsPerPage);

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

    public List<Barang> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        List<Barang> list = new ArrayList<>();

        String sql = baseSelectWithStock()
            + "WHERE b.is_delete = ? "
            + "GROUP BY b.barang_id "
            + "ORDER BY b.barang_code ASC "
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

    public List<Barang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        List<Barang> list = new ArrayList<>();

        String sql = baseSelectWithStock()
                + "WHERE b.is_delete=? "
                + "AND ( "
                + " b.barang_code LIKE ? OR b.barang_name LIKE ? OR b.barcode LIKE ? OR "
                + " m.merk_name LIKE ? OR k.kategori_name LIKE ? OR s.satuan_name LIKE ? OR "
                + " b.harga_jual LIKE ? OR b.stok_minimum LIKE ? OR sp.supplier_name LIKE ? "
                + ") "
                + "LIMIT ? OFFSET ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            int idx = 1;
            st.setBoolean(idx++, isDelete);

            String search = "%" + keyword + "%";
            for (int i = 0; i < 9; i++) {
                st.setString(idx++, search);
            }

            st.setInt(idx++, itemsPerPage);
            st.setInt(idx, (currentPage - 1) * itemsPerPage);

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
        String sql = "SELECT COUNT(*) AS total FROM barang WHERE is_delete=? "
                + "AND (barang_name LIKE ? OR barang_code LIKE ? OR barcode LIKE ?)";

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

    public boolean isBarangCodeExists(String barangCode) {
        return isExists("SELECT COUNT(*) FROM barang WHERE barang_code=? AND is_delete=FALSE", barangCode);
    }

    public boolean isBarcodeExists(String barcode) {
        return isExists("SELECT COUNT(*) FROM barang WHERE barcode=? AND is_delete=FALSE", barcode);
    }

    private boolean isExists(String sql, String value) {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generateBarangCode() {
        String sql = "SELECT barang_code FROM barang WHERE is_delete=FALSE ORDER BY barang_code DESC LIMIT 1";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            int nomor = 1;
            if (rs.next()) {
                String lastCode = rs.getString("barang_code");
                if (lastCode != null && lastCode.startsWith("BRG-")) {
                    nomor = Integer.parseInt(lastCode.substring(4)) + 1;
                }
            }
            return "BRG-" + String.format("%04d", nomor);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String baseSelectSingle() {
        return
            "SELECT " +
            " b.barang_id, b.barang_code, b.barcode, b.barang_name, " +
            " b.harga_jual, b.stok_minimum, b.photo, " +
            " 0 AS total_stok, " +
            " m.merk_id, m.merk_name, " +
            " k.kategori_id, k.kategori_name, " +
            " s.satuan_id, s.satuan_name, " +
            " sp.supplier_id, sp.supplier_name " +
            "FROM barang b " +
            "JOIN merk m ON b.merk_id = m.merk_id " +
            "JOIN kategori k ON b.kategori_id = k.kategori_id " +
            "JOIN satuan s ON b.satuan_id = s.satuan_id " +
            "JOIN supplier sp ON b.supplier_id = sp.supplier_id ";
    }

    private String baseSelectWithStock() {
        return
            "SELECT " +
            " b.barang_id, b.barang_code, b.barcode, b.barang_name, " +
            " b.harga_jual, b.stok_minimum, b.photo, " +
            " IFNULL(SUM(st.jumlah), 0) AS total_stok, " +
            " m.merk_id, m.merk_name, " +
            " k.kategori_id, k.kategori_name, " +
            " s.satuan_id, s.satuan_name, " +
            " sp.supplier_id, sp.supplier_name " +
            "FROM barang b " +
            "JOIN merk m ON b.merk_id = m.merk_id " +
            "JOIN kategori k ON b.kategori_id = k.kategori_id " +
            "JOIN satuan s ON b.satuan_id = s.satuan_id " +
            "JOIN supplier sp ON b.supplier_id = sp.supplier_id " +
            "LEFT JOIN stok_gudang st " +
            "  ON st.barang_id = b.barang_id AND st.is_delete = 0 ";
    }

    private Barang mapResult(ResultSet rs) throws SQLException {
        Barang b = new Barang();
        b.setBarangID(rs.getInt("barang_id"));
        b.setBarangCode(rs.getString("barang_code"));
        b.setBarcode(rs.getString("barcode"));
        b.setBarangName(rs.getString("barang_name"));
        b.setHargaJual(rs.getDouble("harga_jual"));
        b.setStokMinimum(rs.getInt("stok_minimum"));
        b.setTotalStok(rs.getInt("total_stok"));
        b.setPhoto(rs.getString("photo"));

        Merk m = new Merk();
        m.setMerkID(rs.getInt("merk_id"));
        m.setMerkName(rs.getString("merk_name"));

        Kategori k = new Kategori();
        k.setKategoriID(rs.getInt("kategori_id"));
        k.setKategoriName(rs.getString("kategori_name"));

        Satuan s = new Satuan();
        s.setSatuanID(rs.getInt("satuan_id"));
        s.setSatuanName(rs.getString("satuan_name"));

        Supplier sp = new Supplier();
        sp.setSupplierID(rs.getInt("supplier_id"));
        sp.setSupplierName(rs.getString("supplier_name"));

        b.setMerk(m);
        b.setKategori(k);
        b.setSatuan(s);
        b.setSupplier(sp);

        return b;
    }

}
