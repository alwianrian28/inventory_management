package com.inventory.service;

import com.inventory.model.Satuan;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface SatuanService {

    boolean insertData(Satuan satuan);
    boolean updateData(Satuan satuan);
    boolean deleteData(Satuan satuan);
    boolean restoreData(int satuanID, String satuanName);

    List<Satuan> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<Satuan> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);

    int getTotalItems(boolean isDelete, String keyword);
}
