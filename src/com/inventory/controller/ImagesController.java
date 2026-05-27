package com.inventory.controller;

import com.inventory.model.Images;
import com.inventory.service.ImagesService;
import com.inventory.service.impl.ImagesServiceImpl;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class ImagesController {
    
    private final ImagesService imagesService = new ImagesServiceImpl();
    
    public boolean insertData(Images model) {
        return imagesService.insertData(model);
    }

    public boolean updateData(Images model) {
        return imagesService.updateData(model);
    }

    public boolean deleteData(int id) {
        return imagesService.deleteData(id);
    }
    public List<Images> getData(int itemsPerPage, int currentPage) {
        return imagesService.getData(itemsPerPage, currentPage);
    }

    public List<Images> searchData(String keyword, int itemsPerPage, int currentPage) {
        return imagesService.searchData(keyword, itemsPerPage, currentPage);
    }

    public int getTotalItems(String keyword) {
        return imagesService.getTotalItems(keyword);
    }
}
