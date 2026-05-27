package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.Gudang;
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
public class GudangDAO {

    private final Connection conn;

    public GudangDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(Gudang model) {
        PreparedStatement st = null;

        try {
            String sql = "INSERT INTO gudang(gudang_code, gudang_name, alamat, keterangan, insert_by) "
                       + "VALUES (?,?,?,?,?)";
            st = conn.prepareStatement(sql);

            st.setString(1, model.getGudangCode());
            st.setString(2, model.getGudangName());
            st.setString(3, model.getAlamat());
            st.setString(4, model.getKeterangan());
            st.setInt(5, model.getInsertBy());

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

    public void updateData(Gudang model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE gudang SET gudang_name=?, alamat=?, keterangan=?, "
                       + "update_by=?, update_at=NOW() WHERE gudang_id=?";

            st = conn.prepareStatement(sql);
            st.setString(1, model.getGudangName());
            st.setString(2, model.getAlamat());
            st.setString(3, model.getKeterangan());
            st.setInt(4, model.getUpdateBy());
            st.setInt(5, model.getGudangID());

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

    public void deleteData(Gudang model) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE gudang SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE gudang_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getGudangID());

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

    public void restoreData(int gudangID) {
        PreparedStatement st = null;

        try {
            String sql = "UPDATE gudang SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE gudang_id=?";

            st = conn.prepareStatement(sql);
            st.setInt(1, gudangID);

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

    public Gudang getDataById(int gudangID) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Gudang gudang = null;

        try {
            String sql = "SELECT gudang_id, gudang_code, gudang_name, alamat, keterangan "
                       + "FROM gudang WHERE gudang_id=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, gudangID);
            rs = st.executeQuery();

            if (rs.next()) {
                gudang = new Gudang();
                gudang.setGudangID(rs.getInt("gudang_id"));
                gudang.setGudangCode(rs.getString("gudang_code"));
                gudang.setGudangName(rs.getString("gudang_name"));
                gudang.setAlamat(rs.getString("alamat"));
                gudang.setKeterangan(rs.getString("keterangan"));
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

        return gudang;
    }

    public List<Gudang> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Gudang> list = new ArrayList<>();

        try {
            String sql = "SELECT gudang_id, gudang_code, gudang_name, alamat, keterangan "
                       + "FROM gudang WHERE is_delete=? LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();

            while (rs.next()) {
                Gudang g = new Gudang();
                g.setGudangID(rs.getInt("gudang_id"));
                g.setGudangCode(rs.getString("gudang_code"));
                g.setGudangName(rs.getString("gudang_name"));
                g.setAlamat(rs.getString("alamat"));
                g.setKeterangan(rs.getString("keterangan"));
                list.add(g);
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

    public List<Gudang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Gudang> list = new ArrayList<>();

        try {
            String sql = "SELECT gudang_id, gudang_code, gudang_name, alamat, keterangan FROM gudang "
                       + "WHERE is_delete=? AND (gudang_code LIKE ? OR gudang_name LIKE ? OR alamat LIKE ? OR keterangan LIKE ?) "
                       + "LIMIT ? OFFSET ?";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setString(5, "%" + keyword + "%");
            st.setInt(6, itemsPerPage);
            st.setInt(7, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();

            while (rs.next()) {
                Gudang g = new Gudang();
                g.setGudangID(rs.getInt("gudang_id"));
                g.setGudangCode(rs.getString("gudang_code"));
                g.setGudangName(rs.getString("gudang_name"));
                g.setAlamat(rs.getString("alamat"));
                g.setKeterangan(rs.getString("keterangan"));
                list.add(g);
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

    public int getTotalItems(boolean isDelete, String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) AS total FROM gudang "
                       + "WHERE is_delete=? AND (gudang_name LIKE ? OR alamat LIKE ? OR keterangan LIKE ?)";

            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            rs = st.executeQuery();

            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean isGudangNameExists(String gudangName) {
        boolean exists = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM gudang WHERE gudang_name = ? AND is_delete = FALSE";
            ps = conn.prepareStatement(sql);
            ps.setString(1, gudangName);
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

    
    public String generateGudangCode() {
        PreparedStatement st = null;
        ResultSet rs = null;
        String urutan;

        try {
            String sql = "SELECT gudang_code FROM gudang WHERE is_delete=FALSE ORDER BY gudang_code DESC LIMIT 1";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            int nomor;
            if (rs.next()) {
                String lastKode = rs.getString("gudang_code"); // G-09
                nomor = Integer.parseInt(lastKode.substring(2)) + 1;
            } else {
                nomor = 1;
            }

            urutan = "G-" + String.format("%02d", nomor);
        } catch (SQLException e) {
            e.printStackTrace();
            urutan = null;
        }

        return urutan;
    }

    public boolean isGudangCodeExists(String gudangCode) {
        boolean exists = false;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT COUNT(*) FROM gudang WHERE gudang_code=? AND is_delete=FALSE";
            ps = conn.prepareStatement(sql);
            ps.setString(1, gudangCode);
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
