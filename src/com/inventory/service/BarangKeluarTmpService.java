package com.inventory.service;

import com.inventory.model.BarangKeluarTmp;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public interface BarangKeluarTmpService {

    boolean insertData(BarangKeluarTmp tmp);
    boolean updateData(BarangKeluarTmp tmp);
    boolean deleteData(int barangKeluarTmpID);
    
    List<BarangKeluarTmp> getData();
    
    int sumJumlah();
    double sumTotalJual();
    
    void clearTmp();
}
