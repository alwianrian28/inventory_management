package com.inventory.service;

import com.inventory.model.BarangMasuk;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface BarangMasukService {

    boolean insertData(BarangMasuk masuk);
    boolean deleteData(BarangMasuk masuk);
    boolean restoreData(int barangMasukID);

    List<BarangMasuk> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<BarangMasuk> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);

    int getTotalItems(boolean isDelete, String keyword);

    String generateBarangMasukCode();
}
