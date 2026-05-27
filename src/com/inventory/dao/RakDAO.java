package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.Rak;
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
public class RakDAO {

    private final Connection conn;

    public RakDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(Rak model) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO rak(rak_code, rak_name, keterangan, insert_by) VALUES (?,?,?,?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getRakCode());
            st.setString(2, model.getRakName());
            st.setString(3, model.getKeterangan());
            st.setInt(4, model.getInsertBy());

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


    public void updateData(Rak model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE rak SET rak_name=?, keterangan=?, update_by=?, update_at=NOW() WHERE rak_id=?";

            st = conn.prepareStatement(sql);
            st.setString(1, model.getRakName());
            st.setString(2, model.getKeterangan());
            st.setInt(3, model.getUpdateBy());
            st.setInt(4, model.getRakID());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(Rak model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE rak SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE rak_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getRakID());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restoreData(int rakID) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE rak SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE rak_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, rakID);

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Rak getDataById(int rakID) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Rak rak = null;

        try {
            String sql = "SELECT rak_id, rak_code, rak_name, keterangan FROM rak WHERE rak_id=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, rakID);
            rs = st.executeQuery();

            if (rs.next()) {
                rak = new Rak();
                rak.setRakID(rs.getInt("rak_id"));
                rak.setRakCode(rs.getString("rak_code"));
                rak.setRakName(rs.getString("rak_name"));
                rak.setKeterangan(rs.getString("keterangan"));
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

        return rak;
    }


    public List<Rak> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Rak> list = new ArrayList<>();

        try {
            String sql = "SELECT rak_id, rak_code, rak_name, keterangan FROM rak WHERE is_delete=? LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();

            while (rs.next()) {
                Rak r = new Rak();
                r.setRakID(rs.getInt("rak_id"));
                r.setRakCode(rs.getString("rak_code"));
                r.setRakName(rs.getString("rak_name"));
                r.setKeterangan(rs.getString("keterangan"));
                list.add(r);
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


    public List<Rak> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Rak> list = new ArrayList<>();

        try {
            String sql = "SELECT rak_id, rak_code, rak_name, keterangan FROM rak "
                       + "WHERE is_delete=? AND (rak_code LIKE ? OR rak_name LIKE ? OR keterangan LIKE ?) "
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
                Rak r = new Rak();
                r.setRakID(rs.getInt("rak_id"));
                r.setRakCode(rs.getString("rak_code"));
                r.setRakName(rs.getString("rak_name"));
                r.setKeterangan(rs.getString("keterangan"));
                list.add(r);
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


    public boolean validateRakName(String rakName) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid;

        try {
            String sql = "SELECT rak_name FROM rak WHERE LOWER(rak_name)=LOWER(?) AND is_delete=FALSE";
            st = conn.prepareStatement(sql);
            st.setString(1, rakName);
            rs = st.executeQuery();

            valid = !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            valid = false;
        }

        return valid;
    }

    public boolean isRakNameExists(String rakName) {
        boolean exists = false;

        try {
            String sql = "SELECT COUNT(*) FROM rak WHERE rak_name=? AND is_delete=FALSE";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, rakName);
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
            String sql = "SELECT COUNT(*) AS total FROM rak "
                       + "WHERE is_delete=? AND (rak_name LIKE ? OR keterangan LIKE ?)";

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
    
    public String generateRakCode() {
        PreparedStatement st = null;
        ResultSet rs = null;
        String urutan = null;

        try {
            String sql = "SELECT rak_code FROM rak WHERE is_delete = FALSE ORDER BY rak_code DESC LIMIT 1";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            int nomor;
            if (rs.next()) {
                String lastKode = rs.getString("rak_code"); // contoh: R-09
                nomor = Integer.parseInt(lastKode.substring(2)) + 1;
            } else {
                nomor = 1;
            }

            urutan = "R-" + String.format("%02d", nomor);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return urutan;
    }

    public boolean isRakCodeExists(String rakCode) {
        boolean exists = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM rak WHERE rak_code = ? AND is_delete = FALSE";
            ps = conn.prepareStatement(sql);
            ps.setString(1, rakCode);
            rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exists;
    }

}
