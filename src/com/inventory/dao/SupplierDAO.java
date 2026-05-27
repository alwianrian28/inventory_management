package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.Supplier;
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
public class SupplierDAO {

    private final Connection conn;

    public SupplierDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    // ================= INSERT =================
    public void insertData(Supplier model) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO supplier "
                       + "(supplier_name, telepon, email, alamat, insert_by) "
                       + "VALUES (?,?,?,?,?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getSupplierName());
            st.setString(2, model.getTelepon());
            st.setString(3, model.getEmail());
            st.setString(4, model.getAlamat());
            st.setInt(5, model.getInsertBy());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE =================
    public void updateData(Supplier model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE supplier SET "
                       + "supplier_name=?, telepon=?, email=?, alamat=?, "
                       + "update_by=?, update_at=NOW() "
                       + "WHERE supplier_id=?";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getSupplierName());
            st.setString(2, model.getTelepon());
            st.setString(3, model.getEmail());
            st.setString(4, model.getAlamat());
            st.setInt(5, model.getUpdateBy());
            st.setInt(6, model.getSupplierID());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE (SOFT) =================
    public void deleteData(Supplier model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE supplier SET "
                       + "delete_by=?, delete_at=NOW(), is_delete=TRUE "
                       + "WHERE supplier_id=?";
            st = conn.prepareStatement(sql);

            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getSupplierID());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= RESTORE =================
    public void restoreData(int supplierID) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE supplier SET "
                       + "delete_by=NULL, delete_at=NULL, is_delete=FALSE "
                       + "WHERE supplier_id=?";
            st = conn.prepareStatement(sql);

            st.setInt(1, supplierID);

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= GET BY ID =================
    public Supplier getDataById(int supplierID) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Supplier supplier = null;

        try {
            String sql = "SELECT supplier_id, supplier_name, telepon, email, alamat "
                       + "FROM supplier WHERE supplier_id=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, supplierID);
            rs = st.executeQuery();

            if (rs.next()) {
                supplier = new Supplier();
                supplier.setSupplierID(rs.getInt("supplier_id"));
                supplier.setSupplierName(rs.getString("supplier_name"));
                supplier.setTelepon(rs.getString("telepon"));
                supplier.setEmail(rs.getString("email"));
                supplier.setAlamat(rs.getString("alamat"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return supplier;
    }

    // ================= GET DATA (PAGINATION) =================
    public List<Supplier> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Supplier> list = new ArrayList<>();

        try {
            String sql = "SELECT supplier_id, supplier_name, telepon, email, alamat "
                       + "FROM supplier "
                       + "WHERE is_delete=? "
                       + "LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);

            rs = st.executeQuery();

            while (rs.next()) {
                Supplier s = new Supplier();
                s.setSupplierID(rs.getInt("supplier_id"));
                s.setSupplierName(rs.getString("supplier_name"));
                s.setTelepon(rs.getString("telepon"));
                s.setEmail(rs.getString("email"));
                s.setAlamat(rs.getString("alamat"));
                list.add(s);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= SEARCH =================
    public List<Supplier> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Supplier> list = new ArrayList<>();

        try {
            String sql = "SELECT supplier_id, supplier_name, telepon, email, alamat "
                       + "FROM supplier "
                       + "WHERE is_delete=? AND "
                       + "(supplier_name LIKE ? OR telepon LIKE ? OR email LIKE ?) "
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
                Supplier s = new Supplier();
                s.setSupplierID(rs.getInt("supplier_id"));
                s.setSupplierName(rs.getString("supplier_name"));
                s.setTelepon(rs.getString("telepon"));
                s.setEmail(rs.getString("email"));
                s.setAlamat(rs.getString("alamat"));
                list.add(s);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= VALIDATION =================
    public boolean isSupplierNameExists(String supplierName) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) FROM supplier "
                       + "WHERE supplier_name=? AND is_delete=FALSE";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, supplierName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    // ================= TOTAL ITEMS =================
    public int getTotalItems(boolean isDelete, String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) AS total FROM supplier "
                       + "WHERE is_delete=? AND "
                       + "(supplier_name LIKE ? OR telepon LIKE ? OR email LIKE ?)";
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");

            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
