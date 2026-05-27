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
public class BarangMasukTmpDAO {

    private final Connection conn;

    public BarangMasukTmpDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(BarangMasukTmp tmp) {
        String sql = "INSERT INTO barang_masuk_tmp "
                + "(barang_id, jumlah, harga_beli, subtotal, rak_id, gudang_id) "
                + "VALUES (?,?,?,?,?,?)";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, tmp.getBarang().getBarangID());
            st.setInt(2, tmp.getJumlah());
            st.setDouble(3, tmp.getHargaBeli());
            st.setDouble(4, tmp.getSubtotal());
            st.setInt(5, tmp.getRak().getRakID());
            st.setInt(6, tmp.getGudang().getGudangID());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(BarangMasukTmp tmp) {
        String sql = "UPDATE barang_masuk_tmp SET "
                + "barang_id=?, jumlah=?, harga_beli=?, subtotal=?, rak_id=?, gudang_id=? "
                + "WHERE barang_masuk_tmp_id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, tmp.getBarang().getBarangID());
            st.setInt(2, tmp.getJumlah());
            st.setDouble(3, tmp.getHargaBeli());
            st.setDouble(4, tmp.getSubtotal());
            st.setInt(5, tmp.getRak().getRakID());
            st.setInt(6, tmp.getGudang().getGudangID());
            st.setInt(7, tmp.getBarangMasukTmpID());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(int barangMasukTmpID) {
        String sql = "DELETE FROM barang_masuk_tmp WHERE barang_masuk_tmp_id=?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, barangMasukTmpID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllTmp() {
        try (PreparedStatement st = conn.prepareStatement("DELETE FROM barang_masuk_tmp")) {
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetAI() {
        try (PreparedStatement st = conn.prepareStatement("ALTER TABLE barang_masuk_tmp AUTO_INCREMENT = 1")) {
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BarangMasukTmp> getData() {
        List<BarangMasukTmp> list = new ArrayList<>();

        String sql = "SELECT "
                + "t.barang_masuk_tmp_id, t.jumlah, t.harga_beli, t.subtotal, "
                + "b.barang_id, b.barcode, b.barang_name, "
                + "s.satuan_id, s.satuan_name, "
                + "r.rak_id, r.rak_name, "
                + "g.gudang_id, g.gudang_name "
                + "FROM barang_masuk_tmp t "
                + "JOIN barang b ON t.barang_id=b.barang_id "
                + "JOIN satuan s ON b.satuan_id=s.satuan_id "
                + "JOIN rak r ON t.rak_id=r.rak_id "
                + "JOIN gudang g ON t.gudang_id=g.gudang_id";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(mapResult(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int sumJumlah() {
        try (PreparedStatement st = conn.prepareStatement("SELECT COALESCE(SUM(jumlah),0) FROM barang_masuk_tmp");
             ResultSet rs = st.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double sumTotalBeli() {
        try (PreparedStatement st = conn.prepareStatement("SELECT COALESCE(SUM(subtotal),0) FROM barang_masuk_tmp");
             ResultSet rs = st.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private BarangMasukTmp mapResult(ResultSet rs) throws SQLException {
        BarangMasukTmp tmp = new BarangMasukTmp();
        tmp.setBarangMasukTmpID(rs.getInt("barang_masuk_tmp_id"));
        tmp.setJumlah(rs.getInt("jumlah"));
        tmp.setHargaBeli(rs.getDouble("harga_beli"));
        tmp.setSubtotal(rs.getDouble("subtotal"));

        Barang b = new Barang();
        b.setBarangID(rs.getInt("barang_id"));
        b.setBarcode(rs.getString("barcode"));
        b.setBarangName(rs.getString("barang_name"));

        Satuan s = new Satuan();
        s.setSatuanID(rs.getInt("satuan_id"));
        s.setSatuanName(rs.getString("satuan_name"));
        b.setSatuan(s);

        Rak r = new Rak();
        r.setRakID(rs.getInt("rak_id"));
        r.setRakName(rs.getString("rak_name"));

        Gudang g = new Gudang();
        g.setGudangID(rs.getInt("gudang_id"));
        g.setGudangName(rs.getString("gudang_name"));

        tmp.setBarang(b);
        tmp.setRak(r);
        tmp.setGudang(g);

        return tmp;
    }
}
