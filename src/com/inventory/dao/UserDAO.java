package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Dearclaudia
 */
public class UserDAO{

    private final Connection conn;

    public UserDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }
    
    public boolean isUsernameExists(String username) {
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT COUNT(*) FROM user WHERE username = ? AND is_delete = FALSE"; 
            st = conn.prepareStatement(sql);
            st.setString(1, username);
            rs = st.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void insertData(User model) {
        PreparedStatement st = null;
        
        try {
            String sql = "INSERT INTO user(name, email, username, password, role, insert_by) VALUES (?,?,?,?,?,?)";
            st = conn.prepareStatement(sql);
            
            st.setString(1, model.getName());
            st.setString(2, model.getEmail());
            st.setString(3, model.getUsername());
            st.setString(4, model.getPassword());
            st.setString(5, model.getRole());
            st.setInt(6, model.getInsertBy());
            
            st.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isUserRegistered() {
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT COUNT(*) FROM user WHERE is_delete = FALSE"; 
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            if (rs.next()) {
                int total = rs.getInt(1);
                return total > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User findByUsername(User user) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT user_id, name, email, username, password, role FROM user WHERE username=? AND is_delete = 0";
        User modelUser = null;
        
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, user.getUsername());
            rs = st.executeQuery();
            
            if(rs.next()){
                String hashedPassword = rs.getString("password");
                
                if(BCrypt.checkpw(user.getPassword(), hashedPassword)){
                    modelUser = new User();
                    modelUser.setUserID(rs.getInt("user_id"));
                    modelUser.setName(rs.getString("name"));
                    modelUser.setEmail(rs.getString("email"));
                    modelUser.setUsername(rs.getString("username"));
                    modelUser.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return modelUser;
    }

    
    public void updateData(User model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE user SET name=?, email=?, username=?, role=?, update_by = ?, update_at = NOW()WHERE user_id=?";
            
            st = conn.prepareStatement(sql);
            st.setString    (1, model.getName());
            st.setString    (2, model.getEmail());
            st.setString    (3, model.getUsername());
            st.setString    (4, model.getRole());
            st.setInt       (5, model.getUpdateBy());
            st.setInt       (6, model.getUserID());
            
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void deleteData(User model) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE user SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE user_id=?";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getUserID());
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void restoreData(int userID) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE user SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE user_id=?";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, userID);
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public User getUserById(int UserID) {
        PreparedStatement st = null;
        ResultSet rs = null;
        User user = null;
        String sql ="SELECT user_id, name, email, username, role FROM user WHERE user_id = ? AND is_delete = 0";
        
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, UserID);
            rs = st.executeQuery();
            while(rs.next()){
                user = new User();
                user.setUserID      (rs.getInt("user_id"));
                user.setName        (rs.getString("name"));
                user.setEmail       (rs.getString("email"));
                user.setUsername    (rs.getString("username"));
                user.setRole        (rs.getString("role"));
                
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    
    public List<User> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<User> list = new ArrayList();
        String sql ="SELECT user_id, name, email, username, role "
                + "FROM user "
                + "WHERE is_delete = ? "
                + "LIMIT ? OFFSET ?";
        
        try {
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            while(rs.next()){
                User model = new User();
                model.setUserID      (rs.getInt("user_id"));
                model.setName        (rs.getString("name"));
                model.setEmail       (rs.getString("email"));
                model.setUsername    (rs.getString("username"));
                model.setRole        (rs.getString("role"));
                
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    
    public List<User> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<User> list = new ArrayList();
        String sql ="SELECT user_id, name, email, username, role FROM user "
                + "WHERE is_delete = ? "
                + "AND (user_id LIKE ? "
                + "OR name LIKE ? "
                + "OR email LIKE ? "
                + "OR username LIKE ? "
                + "OR role LIKE ?) "
                + "LIMIT ? OFFSET ?";
        
        try {
            st = conn.prepareStatement(sql);
            
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setString(5, "%" + keyword + "%");
            st.setString(6, "%" + keyword + "%");
            st.setInt(7, itemsPerPage);
            st.setInt(8, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            while(rs.next()){
                User model = new User();
                model.setUserID      (rs.getInt("user_id"));
                model.setName        (rs.getString("name"));
                model.setEmail       (rs.getString("email"));
                model.setUsername    (rs.getString("username"));
                model.setRole        (rs.getString("role"));
                
                list.add(model);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean validateUsername(String username) {
        PreparedStatement st = null;
        ResultSet rs = null;
        boolean valid = false;
        
        try {
            String sql = "SELECT username FROM user WHERE username LIKE BINARY ? AND is_delete = 0";
            st = conn.prepareStatement(sql);
            st.setString(1, username);
            rs = st.executeQuery();
            
            valid = !rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return valid;
    }
        
    public boolean validateOldPassword(String username, String oldPassword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT password FROM user WHERE username = ? AND is_delete = 0";

        try {
            st = conn.prepareStatement(sql);
            st.setString(1, username);
            rs = st.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(oldPassword, hashedPassword);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        PreparedStatement st = null;
        PreparedStatement stUpdate = null;
        ResultSet rs = null;
        String sql = "SELECT password FROM user WHERE username = ? AND is_delete = 0";

        try {
            st = conn.prepareStatement(sql);
            st.setString(1, username);
            rs = st.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                if (BCrypt.checkpw(oldPassword, hashedPassword)) {
                    String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                    String sqlUpdate = "UPDATE user SET password = ? WHERE username = ?";
                    stUpdate = conn.prepareStatement(sqlUpdate);
                    stUpdate.setString(1, hashedNewPassword);
                    stUpdate.setString(2, username);
                    int result = stUpdate.executeUpdate();
                    return result > 0;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getTotalItems(boolean isDelete, String keyword) {
        String baseSql = "SELECT COUNT(*) AS total FROM user WHERE is_delete = ? ";
        String searchSql = "AND (user_id LIKE ? OR name LIKE ? OR email LIKE ? OR username LIKE ? OR role LIKE ?)";

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            if (keyword != null && !keyword.isEmpty()) {
                st = conn.prepareStatement(baseSql + searchSql);
                int index = 1;
                st.setBoolean(index++, isDelete);
                for (int i = 0; i < 5; i++) {
                    st.setString(index++, "%" + keyword + "%");
                }
            } else {
                st = conn.prepareStatement(baseSql);
                st.setBoolean(1, isDelete);
            }

            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
}
