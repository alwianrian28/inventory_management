package com.inventory.service;

import com.inventory.model.BarangMasukDetail;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface BarangMasukDetailService {

    boolean insertData(BarangMasukDetail detail);
    
    List<BarangMasukDetail> getDataById(int barangMasukID, int itemsPerPage, int currentPage);
    List<BarangMasukDetail> searchDataById(int barangMasukID, String keyword, int itemsPerPage, int currentPage);

    int getTotalItems(int barangMasukID, String keyword);
}
