package com.inventory.service.impl;

import com.inventory.dao.SupplierDAO;
import com.inventory.model.Supplier;
import com.inventory.service.SupplierService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class SupplierServiceImpl implements SupplierService {

    private final SupplierDAO supplierDAO = new SupplierDAO();

    @Override
    public boolean insertData(Supplier supplier) {
        if (supplier.getSupplierName() == null || supplier.getSupplierName().isEmpty()) {
            return false;
        }

        if (supplierDAO.isSupplierNameExists(supplier.getSupplierName())) {
            return false;
        }

        supplierDAO.insertData(supplier);
        return true;
    }

    @Override
    public boolean updateData(Supplier supplier) {
        if (supplier.getSupplierID() <= 0) {
            return false;
        }

        if (supplier.getSupplierName() == null || supplier.getSupplierName().isEmpty()) {
            return false;
        }

        Supplier old = supplierDAO.getDataById(supplier.getSupplierID());
        if (old == null) {
            return false;
        }

        if (!old.getSupplierName().equalsIgnoreCase(supplier.getSupplierName())
                && supplierDAO.isSupplierNameExists(supplier.getSupplierName())) {
            return false;
        }

        supplierDAO.updateData(supplier);
        return true;
    }

    @Override
    public boolean deleteData(Supplier supplier) {
        if (supplier.getSupplierID() <= 0) {
            return false;
        }

        supplierDAO.deleteData(supplier);
        return true;
    }

    @Override
    public boolean restoreData(int supplierID, String supplierName) {
        if (supplierID <= 0) {
            return false;
        }
        
        if (supplierDAO.isSupplierNameExists(supplierName)) {
            return false;
        }

        supplierDAO.restoreData(supplierID);
        return true;
    }

    @Override
    public List<Supplier> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        return supplierDAO.getData(isDelete, itemsPerPage, currentPage);
    }

    @Override
    public List<Supplier> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        return supplierDAO.searchData(keyword, isDelete, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(boolean isDelete, String keyword) {
        return supplierDAO.getTotalItems(isDelete, keyword);
    }
}
