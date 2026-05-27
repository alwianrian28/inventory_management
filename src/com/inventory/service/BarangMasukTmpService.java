package com.inventory.service;

import com.inventory.model.BarangMasukTmp;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface BarangMasukTmpService {

    boolean insertData(BarangMasukTmp tmp);
    boolean updateData(BarangMasukTmp tmp);
    boolean deleteData(int barangMasukTmpID);
    
    List<BarangMasukTmp> getData();
    
    int sumJumlah();
    double sumTotalBeli();
    
    void clearTmp();
}
