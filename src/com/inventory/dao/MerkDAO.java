package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.Merk;
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
public class MerkDAO {

    private final Connection conn;

    public MerkDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(Merk model) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO merk (merk_name, keterangan, insert_by) VALUES (?,?,?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getMerkName());
            st.setString(2, model.getKeterangan());
            st.setInt(3, model.getInsertBy());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateData(Merk model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE merk SET merk_name=?, keterangan=?, update_by=?, update_at=NOW() WHERE merk_id=?";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getMerkName());
            st.setString(2, model.getKeterangan());
            st.setInt(3, model.getUpdateBy());
            st.setInt(4, model.getMerkID());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteData(Merk model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE merk SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE merk_id=?";
            st = conn.prepareStatement(sql);

            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getMerkID());

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void restoreData(int merkID) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE merk SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE merk_id=?";
            st = conn.prepareStatement(sql);

            st.setInt(1, merkID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Merk getDataById(int merkID) {

        PreparedStatement st = null;
        ResultSet rs = null;
        Merk merk = null;

        try {
            String sql = "SELECT merk_id, merk_name, keterangan, is_delete "
                       + "FROM merk "
                       + "WHERE merk_id = ?";

            st = conn.prepareStatement(sql);
            st.setInt(1, merkID);
            rs = st.executeQuery();

            if (rs.next()) {
                merk = new Merk();
                merk.setMerkID(rs.getInt("merk_id"));
                merk.setMerkName(rs.getString("merk_name"));
                merk.setKeterangan(rs.getString("keterangan"));
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

        return merk;
    }

    public List<Merk> getData(boolean isDelete, int itemsPerPage, int currentPage) {

        PreparedStatement st = null;
        ResultSet rs = null;
        List<Merk> list = new ArrayList<>();

        try {
            String sql = "SELECT merk_id, merk_name, keterangan, is_delete "
                       + "FROM merk "
                       + "WHERE is_delete = ? "
                       + "LIMIT ? OFFSET ?";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();

            while (rs.next()) {
                Merk merk = new Merk();
                merk.setMerkID(rs.getInt("merk_id"));
                merk.setMerkName(rs.getString("merk_name"));
                merk.setKeterangan(rs.getString("keterangan"));
                list.add(merk);
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

        return list;
    }

    public List<Merk> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {

        PreparedStatement st = null;
        ResultSet rs = null;
        List<Merk> list = new ArrayList<>();

        try {
            String sql = "SELECT merk_id, merk_name, keterangan, is_delete "
                       + "FROM merk "
                       + "WHERE is_delete = ? AND (merk_name LIKE ? OR keterangan LIKE ?) "
                       + "LIMIT ? OFFSET ?";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setInt(4, itemsPerPage);
            st.setInt(5, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();

            while (rs.next()) {
                Merk merk = new Merk();
                merk.setMerkID(rs.getInt("merk_id"));
                merk.setMerkName(rs.getString("merk_name"));
                merk.setKeterangan(rs.getString("keterangan"));
                list.add(merk);
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

        return list;
    }

    public boolean isMerkNameExists(String merkName) {

        boolean exists = false;

        try {
            String sql = "SELECT COUNT(*) FROM merk WHERE LOWER(merk_name) = LOWER(?) AND is_delete = FALSE";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, merkName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    public int getTotalItems(boolean isDelete, String keyword) {

        PreparedStatement st = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(*) AS total "
                   + "FROM merk "
                   + "WHERE is_delete = ? AND (merk_name LIKE ? OR keterangan LIKE ?)";

        try {
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");

            rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
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

        return 0;
    }
}
