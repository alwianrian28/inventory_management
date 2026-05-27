package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.Satuan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SatuanDAO {

    private final Connection conn;

    public SatuanDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(Satuan model) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO satuan(satuan_name, keterangan, insert_by) VALUES (?,?,?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getSatuanName());
            st.setString(2, model.getKeterangan());
            st.setInt(3, model.getInsertBy());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(Satuan model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE satuan SET satuan_name=?, keterangan=?, update_by=?, update_at=NOW() WHERE satuan_id=?";

            st = conn.prepareStatement(sql);
            st.setString(1, model.getSatuanName());
            st.setString(2, model.getKeterangan());
            st.setInt(3, model.getUpdateBy());
            st.setInt(4, model.getSatuanID());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(Satuan model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE satuan  SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE satuan_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getSatuanID());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restoreData(int satuanID) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE satuan SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE satuan_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, satuanID);

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Satuan getDataById(int satuanID) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Satuan satuan = null;

        try {
            String sql = "SELECT satuan_id, satuan_name, keterangan FROM satuan WHERE satuan_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, satuanID);
            rs = st.executeQuery();

            if (rs.next()) {
                satuan = new Satuan();
                satuan.setSatuanID(rs.getInt("satuan_id"));
                satuan.setSatuanName(rs.getString("satuan_name"));
                satuan.setKeterangan(rs.getString("keterangan"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return satuan;
    }

    public List<Satuan> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Satuan> list = new ArrayList<>();

        try {
            String sql = "SELECT satuan_id, satuan_name, keterangan FROM satuan WHERE is_delete=? LIMIT ? OFFSET ?";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();

            while (rs.next()) {
                Satuan s = new Satuan();
                s.setSatuanID(rs.getInt("satuan_id"));
                s.setSatuanName(rs.getString("satuan_name"));
                s.setKeterangan(rs.getString("keterangan"));
                list.add(s);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Satuan> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Satuan> list = new ArrayList<>();

        try {
            String sql = "SELECT satuan_id, satuan_name, keterangan FROM satuan WHERE is_delete=?  AND (satuan_name LIKE ? OR keterangan LIKE ?) LIMIT ? OFFSET ?";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setInt(4, itemsPerPage);
            st.setInt(5, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();

            while (rs.next()) {
                Satuan s = new Satuan();
                s.setSatuanID(rs.getInt("satuan_id"));
                s.setSatuanName(rs.getString("satuan_name"));
                s.setKeterangan(rs.getString("keterangan"));
                list.add(s);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean validateSatuanName(String satuanName) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = false;

        try {
            String sql = " SELECT satuan_name FROM satuan  WHERE LOWER(satuan_name)=LOWER(?) AND is_delete=FALSE ";

            st = conn.prepareStatement(sql);
            st.setString(1, satuanName);
            rs = st.executeQuery();

            valid = !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valid;
    }

    public boolean isSatuanNameExists(String satuanName) {
        boolean exists = false;

        try {
            String sql = "SELECT COUNT(*) FROM satuan WHERE satuan_name=? AND is_delete=FALSE ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, satuanName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) AS total FROM satuan WHERE is_delete=? AND (satuan_name LIKE ? OR keterangan LIKE ?)";

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
}
