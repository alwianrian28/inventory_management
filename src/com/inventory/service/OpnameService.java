package com.inventory.service;

import com.inventory.model.StokOpname;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface OpnameService {

    boolean insertData(StokOpname model);
    boolean updateData(StokOpname model);
    boolean deleteData(StokOpname model);
    boolean restoreData(int stokOpnameID);
    
    List<StokOpname> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<StokOpname> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);
    
    boolean applyStockAdjustment(int opnameID);
    boolean adjustStock(int barangID, int selisih);
    boolean rollbackStockOpname(int opnameID);
    
    int getTotalItems(boolean isDelete, String keyword);
}
