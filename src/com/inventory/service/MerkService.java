package com.inventory.service;

import com.inventory.model.Merk;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface MerkService {

    boolean insertData(Merk merk);
    boolean updateData(Merk merk);
    boolean deleteData(Merk merk);
    boolean restoreData(int merkID, String merkName);

    List<Merk> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<Merk> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);

    int getTotalItems(boolean isDelete, String keyword);
}
