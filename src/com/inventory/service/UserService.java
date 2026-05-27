package com.inventory.service;

import com.inventory.model.User;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface UserService {

    boolean isUserRegistered();
    User login(User user);
    
    boolean insertData(User model);
    boolean updateData(User model);
    boolean deleteData(User model);
    boolean restoreData(int userID, String username);
    
    User getUserById(int userID);
    List<User> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<User> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);
    
    boolean validateOldPassword(String username, String oldPassword);
    boolean changePassword(String username, String oldPassword, String newPassword);
    
    int getTotalItems(boolean isDelete, String keyword);
}
