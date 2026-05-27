package com.inventory.service;

import com.inventory.model.BarangKeluar;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface BarangKeluarService {

    boolean insertData(BarangKeluar masuk);
    boolean deleteData(BarangKeluar masuk);
    boolean restoreData(int barangKeluarID);

    List<BarangKeluar> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<BarangKeluar> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);

    int getTotalItems(boolean isDelete, String keyword);

    String generateBarangKeluarCode();
    
    void updateStok    (int barangKeluarID);
    void rollbackStok  (int barangKeluarID);
}
