package com.inventory.service.impl;

import com.inventory.dao.ImagesDAO;
import com.inventory.model.Images;
import com.inventory.service.ImagesService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class ImagesServiceImpl implements ImagesService{

    private final ImagesDAO imagesDAO = new ImagesDAO();
    
    @Override
    public boolean insertData(Images model) {
        if (model.getImageName()== null || model.getImageName().isEmpty()) {
            return false;
        }
        
        if (model.getImagePath()== null || model.getImagePath().isEmpty()) {
            return false;
        }
        
        imagesDAO.insertData(model);
        return true;
    }

    @Override
    public boolean updateData(Images model) {
        if (model.getImageName()== null || model.getImageName().isEmpty()) {
            return false;
        }
        
        if (model.getImagePath()== null || model.getImagePath().isEmpty()) {
            return false;
        }
        
        imagesDAO.updateData(model);
        return true;
    }

    @Override
    public boolean deleteData(int id) {
        if (id <= 0) {
            return false;
        }
        
        imagesDAO.deleteData(id);
        imagesDAO.resetAutoIncrement();
        return true;
    }

    @Override
    public List<Images> getData(int itemsPerPage, int currentPage) {
        return imagesDAO.getData(itemsPerPage, currentPage);
    }

    @Override
    public List<Images> searchData(String keyword, int itemsPerPage, int currentPage) {
        return imagesDAO.searchData(keyword, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(String keyword) {
        return imagesDAO.getTotalItems(keyword);
    }

}
