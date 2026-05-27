package com.inventory.service.impl;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import com.inventory.service.UserService;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Dearclaudia
 */
public class UserServiceImpl implements UserService{

    private final UserDAO userDAO = new UserDAO();
    
    @Override
    public boolean isUserRegistered() {
        return userDAO.isUserRegistered();
    }

    @Override
    public User login(User user) {
        if (user.getUsername() == null || user.getPassword() == null ||
            user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            return null;
        }
        
        User modelUser = userDAO.findByUsername(user);
        if (modelUser == null) {
            return null;
        }
        
        return modelUser;
    }
    
    @Override
    public boolean insertData(User user) {
        if (user.getName().isEmpty() 
                || user.getEmail().isEmpty() 
                || user.getUsername().isEmpty() 
                || user.getPassword().isEmpty() 
                || user.getRole().isEmpty() ) {
            return false;
        }

        if (userDAO.isUsernameExists(user.getUsername())) {
            return false;
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userDAO.insertData(user);
        
        return true; 
    }

    @Override
    public boolean updateData(User user) {
        if (user.getName().isEmpty() 
                || user.getEmail().isEmpty() 
                || user.getUsername().isEmpty() 
                || user.getRole().isEmpty() ) {
            return false;
        }

        User old = userDAO.getUserById(user.getUserID());
        if (old == null) {
            return false;
        }
        
        if (!old.getUsername().equalsIgnoreCase(user.getUsername())
                && userDAO.isUsernameExists(user.getUsername())) {
            return false;
        }

        userDAO.updateData(user);
        
        return true; 
    }

    @Override
    public boolean deleteData(User user) {
        if (user.getUserID()<= 0) {
            return false;
        }

        userDAO.deleteData(user);
        return true;
    }

    @Override
    public boolean restoreData(int userID, String username) {
        if (userID <= 0) {
            return false;
        }

        if(username == null || username.isEmpty()){
            return false;
        }
        
        if (userDAO.isUsernameExists(username)) {
            return false;
        }
        
        userDAO.restoreData(userID);
        return true;
    }

    @Override
    public User getUserById(int userID) {
        if (userID <= 0) {
            return null;
        }
        
        return userDAO.getUserById(userID);
    }
    
    @Override
    public List<User> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return userDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<User> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return userDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public boolean validateOldPassword(String username, String oldPassword) {
        if(username == null || username.isEmpty()){
            return false;
        }
        
        if(oldPassword == null || oldPassword.isEmpty()){
            return false;
        }
        
        return userDAO.validateOldPassword(username, oldPassword);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userDAO.changePassword(username, oldPassword, newPassword);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return userDAO.getTotalItems(isDelete, keyword);
    }
}
