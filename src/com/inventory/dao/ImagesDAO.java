package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.Images;
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
public class ImagesDAO{

    private final Connection conn;

    public ImagesDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(Images model) {
        PreparedStatement st = null;
        
        try {
            String sql = "INSERT INTO images(image_name, image_path) VALUES (?,?)";
            st = conn.prepareStatement(sql);
            
            st.setString(1, model.getImageName());
            st.setString(2, model.getImagePath());
            
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(Images model) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE images SET image_name=?, image_path=? WHERE id=?";
            st = conn.prepareStatement(sql);
            
            st.setString(1, model.getImageName());
            st.setString(2, model.getImagePath());
            st.setInt(3, model.getImageID());
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(int id) {
        PreparedStatement st = null;
        
        try {
            String sql = "DELETE FROM images WHERE id=?";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, id);
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetAutoIncrement() {
        PreparedStatement st = null;
        
        try {
            String sql = "ALTER TABLE `images` AUTO_INCREMENT = 1";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Images> getData(int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Images> list = new ArrayList<>();
        
        try {
            String sql = "SELECT id, image_name, image_path "
                    + "FROM images "
                    + "LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, itemsPerPage);
            st.setInt(2, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            
            while (rs.next()) {
                Images modelimages = new Images();
                modelimages.setImageID(rs.getInt("id"));
                modelimages.setImageName(rs.getString("image_name"));
                modelimages.setImagePath(rs.getString("image_path"));
                
                list.add(modelimages);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public List<Images> searchData(String keyword, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Images> list = new ArrayList<>();
        
        try {
            String sql = "SELECT id, image_name, image_path "
                    + "FROM images "
                    + "WHERE image_name LIKE ? "
                    + "LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            
            while (rs.next()) {
                Images modelimages = new Images();
                modelimages.setImageID(rs.getInt("id"));
                modelimages.setImageName(rs.getString("image_name"));
                modelimages.setImagePath(rs.getString("image_path"));
                
                list.add(modelimages);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public int getTotalItems(String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) AS total "
                + "FROM images "
                + "WHERE image_name LIKE ?";

        try {
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
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
