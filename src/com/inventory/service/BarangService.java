package com.inventory.service;

import com.inventory.model.Barang;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface BarangService {

    boolean insertData(Barang barang);
    boolean updateData(Barang barang);
    boolean deleteData(Barang barang);
    boolean restoreData(int barangID, String barangCode, String barcode);

    List<Barang> getData(boolean isDelete, int itemsPerPage, int currentPage);
    List<Barang> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage);

    int getTotalItems(boolean isDelete, String keyword);

    Barang getDataById(int barangID);
    Barang getDataByCode(String barangCode);
    Barang getDataByBarcode(String barcode);
    
    List<Barang> getDataBySupplier(String supplierName, boolean isDelete, int itemsPerPage, int currentPage);
    List<Barang> searchDataBySupplier(String supplierName, String keyword, boolean isDelete, int itemsPerPage, int currentPage);
    
    String generateBarangCode();
}
