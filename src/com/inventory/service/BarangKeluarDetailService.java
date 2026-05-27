package com.inventory.service;

import com.inventory.model.BarangKeluarDetail;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface BarangKeluarDetailService {

    boolean insertData(BarangKeluarDetail detail);
    
    List<BarangKeluarDetail> getDataById(int barangKeluarID, int itemsPerPage, int currentPage);
    List<BarangKeluarDetail> searchDataById(int barangKeluarID, String keyword, int itemsPerPage, int currentPage);

    int getTotalItems(int barangKeluarID, String keyword);
}
