package com.inventory.service;

import com.inventory.model.StokOpnameDetail;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface OpnameDetailService {

    boolean insertData(int opnameID);
    boolean deleteData(int opnameID);
    
    List<StokOpnameDetail> getDataById(int opnameID, int itemsPerPage, int currentPage);
    List<StokOpnameDetail> searchDataById(int opnameID, String keyword, int itemsPerPage, int currentPage);
    
    int getTotalItems(int opnameID, String keyword);
}
