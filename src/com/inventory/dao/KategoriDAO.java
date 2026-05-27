package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.Kategori;
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
public class KategoriDAO{

    private final Connection conn;

    public KategoriDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(Kategori model) {
        PreparedStatement st = null;
        
        try {
            String sql = "INSERT INTO kategori(kategori_name, keterangan, insert_by) VALUES (?,?,?)";
            st = conn.prepareStatement(sql);
            
            st.setString(1, model.getKategoriName());
            st.setString(2, model.getKeterangan());
            st.setInt(3, model.getInsertBy());
            
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(Kategori model) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE kategori SET kategori_name=?, keterangan=?, update_by = ?, update_at = NOW() WHERE kategori_id=?";
            st = conn.prepareStatement(sql);
            
            st.setString(1, model.getKategoriName());
            st.setString(2, model.getKeterangan());
            st.setInt(3, model.getUpdateBy());
            st.setInt(4, model.getKategoriID());
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(Kategori model) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE kategori SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE kategori_id=?";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getKategoriID());
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restoreData(int kategoriID) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE kategori SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE kategori_id=?";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, kategoriID);
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Kategori getDataById(int kategoriID) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Kategori kategori = null;

        try {
            String sql = "SELECT kategori_id, kategori_name, keterangan, is_delete "
                       + "FROM kategori "
                       + "WHERE kategori_id = ?";

            st = conn.prepareStatement(sql);
            st.setInt(1, kategoriID);
            rs = st.executeQuery();

            if (rs.next()) {
                kategori = new Kategori();
                kategori.setKategoriID(rs.getInt("kategori_id"));
                kategori.setKategoriName(rs.getString("kategori_name"));
                kategori.setKeterangan(rs.getString("keterangan"));
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

        return kategori;
    }

    
    public List<Kategori> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Kategori> list = new ArrayList<>();
        
        try {
            String sql = "SELECT kategori_id, kategori_name, keterangan, is_delete "
                    + "FROM kategori "
                    + "WHERE is_delete = ? "
                    + "LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            
            while (rs.next()) {
                Kategori modelkategori = new Kategori();
                modelkategori.setKategoriID(rs.getInt("kategori_id"));
                modelkategori.setKategoriName(rs.getString("kategori_name"));
                modelkategori.setKeterangan(rs.getString("keterangan"));
                
                list.add(modelkategori);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public List<Kategori> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Kategori> list = new ArrayList<>();
        
        try {
            String sql = "SELECT kategori_id, kategori_name, keterangan, is_delete "
                    + "FROM kategori "
                    + "WHERE is_delete = ? AND (kategori_name LIKE ? OR keterangan LIKE ?) LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setInt(4, itemsPerPage);
            st.setInt(5, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            
            while (rs.next()) {
                Kategori modelkategori = new Kategori();
                modelkategori.setKategoriID(rs.getInt("kategori_id"));
                modelkategori.setKategoriName(rs.getString("kategori_name"));
                modelkategori.setKeterangan(rs.getString("keterangan"));
                
                list.add(modelkategori);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public boolean validatekategoriName(String kategoriName) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = false;
        
        try {
            String sql = "SELECT kategori_name FROM kategori WHERE LOWER(kategori_name) = LOWER(?) AND is_delete = FALSE";
            st = conn.prepareStatement(sql);
            st.setString(1, kategoriName);
            rs = st.executeQuery();
            
            valid = !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return valid;
    }
    
    public boolean iskategoriNameExists(String kategoriName) {
        boolean exists = false;
        try {
            String sql = "SELECT COUNT(*) FROM kategori WHERE kategori_name = ? AND is_delete = FALSE"; 
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, kategoriName);
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
        String sql = "SELECT COUNT(*) AS total "
                + "FROM kategori "
                + "WHERE is_delete = ? AND (kategori_name LIKE ? OR keterangan LIKE ?)";

        try {
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }    
}
