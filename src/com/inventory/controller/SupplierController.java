package com.inventory.controller;

import com.inventory.model.Supplier;
import com.inventory.service.SupplierService;
import com.inventory.service.impl.SupplierServiceImpl;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class SupplierController {

    private final SupplierService supplierService = new SupplierServiceImpl();

    public boolean insertData(Supplier supplier) {
        return supplierService.insertData(supplier);
    }

    public boolean updateData(Supplier supplier) {
        return supplierService.updateData(supplier);
    }

    public boolean deleteData(Supplier supplier) {
        return supplierService.deleteData(supplier);
    }

    public boolean restoreData(int supplierID, String supplierName) {
        return supplierService.restoreData(supplierID, supplierName);
    }

    public List<Supplier> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return supplierService.getData(isDelete, itemsPerPage, currentPage);
    }

    public List<Supplier> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return supplierService.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    public int getTotalItems(boolean isDelete, String keyword) {
        return supplierService.getTotalItems(isDelete, keyword);
    }
}
