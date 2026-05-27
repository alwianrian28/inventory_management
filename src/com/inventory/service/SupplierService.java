package com.inventory.service;

import com.inventory.model.Supplier;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface SupplierService {

    boolean insertData(Supplier supplier);
    boolean updateData(Supplier supplier);
    boolean deleteData(Supplier supplier);
    boolean restoreData(int supplierID, String supplierName);

    List<Supplier> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<Supplier> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);

    int getTotalItems(boolean isDelete, String keyword);
}
