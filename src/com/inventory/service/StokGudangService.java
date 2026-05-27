package com.inventory.service;

import com.inventory.model.BarangMasuk;
import com.inventory.model.StokGudang;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface StokGudangService {

    boolean insertData(StokGudang stok);
    boolean deleteData(StokGudang stok);
    boolean deleteData(BarangMasuk masuk);
    boolean restoreData(int barangMasukID);

    List<StokGudang> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<StokGudang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);

    int getTotalItems(boolean isDelete, String keyword);
}
