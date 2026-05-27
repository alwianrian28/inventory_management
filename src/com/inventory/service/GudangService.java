package com.inventory.service;

import com.inventory.model.Gudang;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface GudangService {

    boolean insertData(Gudang gudang);
    boolean updateData(Gudang gudang);
    boolean deleteData(Gudang gudang);
    boolean restoreData(int gudangID, String gudangCode, String gudangName);

    List<Gudang> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<Gudang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);

    int getTotalItems(boolean isDelete, String keyword);

    String generateGudangCode();
}
