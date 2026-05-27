package com.inventory.service;

import com.inventory.model.Images;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface ImagesService {

    boolean insertData(Images model);
    boolean updateData(Images model);
    boolean deleteData(int id);
    
    List<Images> getData(int itemsPerPage, int currentPage);
    List<Images> searchData(String keyword, int itemsPerPage, int currentPage);
    
    int getTotalItems(String keyword);
}
