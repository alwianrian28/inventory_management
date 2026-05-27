package com.inventory.controller;

import com.inventory.model.User;
import com.inventory.service.UserService;
import com.inventory.service.impl.UserServiceImpl;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class UserController {

    private final UserService userService = new UserServiceImpl();

    public boolean isUserRegistered() {
        return userService.isUserRegistered();
    }

    public User login(User user) {
        return userService.login(user);
    }

    public boolean insertData(User user) {
        return userService.insertData(user);
    }

    public boolean updateData(User user) {
        return userService.updateData(user);
    }

    public boolean deleteData(User user) {
        return userService.deleteData(user);
    }

    public boolean restoreData(int userID, String username) {
        return userService.restoreData(userID, username);
    }

    public User getUserById(int userID) {
        return userService.getUserById(userID);
    }
    
    public List<User> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return userService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<User> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return userService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return userService.getTotalItems(isDelete, keyword);
    }

    public boolean validateOldPassword(String username, String oldPassword) {
        return userService.validateOldPassword(username, oldPassword);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userService.changePassword(username, oldPassword, newPassword);
    }
}
