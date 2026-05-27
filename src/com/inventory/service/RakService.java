package com.inventory.service;

import com.inventory.model.Rak;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface RakService {

    boolean insertData(Rak rak);
    boolean updateData(Rak rak);
    boolean deleteData(Rak rak);
    boolean restoreData(int rakID, String rakCode, String rakName);

    List<Rak> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<Rak> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);

    int getTotalItems(boolean isDelete, String keyword);
    
    String generateRakCode();

}
