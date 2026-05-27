package com.inventory.controller;

import com.inventory.model.BarangKeluarTmp;
import com.inventory.service.BarangKeluarTmpService;
import com.inventory.service.impl.BarangKeluarTmpServiceImpl;

import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluarTmpController {

    private final BarangKeluarTmpService barangKeluarTmpService = new BarangKeluarTmpServiceImpl();

    public boolean insertData(BarangKeluarTmp tmp) {
        return barangKeluarTmpService.insertData(tmp);
    }

    public boolean updateData(BarangKeluarTmp tmp) {
        return barangKeluarTmpService.updateData(tmp);
    }

    public boolean deleteData(int barangKeluarTmpID) {
        return barangKeluarTmpService.deleteData(barangKeluarTmpID);
    }

    public List<BarangKeluarTmp> getData() {
        return barangKeluarTmpService.getData();
    }

    public int sumJumlah() {
        return barangKeluarTmpService.sumJumlah();
    }

    public double sumTotalJual() {
        return barangKeluarTmpService.sumTotalJual();
    }

    public void clearTmp() {
        barangKeluarTmpService.clearTmp();
    }
}
