package com.inventory.controller;

import com.inventory.model.BarangMasukTmp;
import com.inventory.service.BarangMasukTmpService;
import com.inventory.service.impl.BarangMasukTmpServiceImpl;

import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangMasukTmpController {

    private final BarangMasukTmpService barangMasukTmpService = new BarangMasukTmpServiceImpl();

    public boolean insertData(BarangMasukTmp tmp) {
        return barangMasukTmpService.insertData(tmp);
    }

    public boolean updateData(BarangMasukTmp tmp) {
        return barangMasukTmpService.updateData(tmp);
    }

    public boolean deleteData(int barangMasukTmpID) {
        return barangMasukTmpService.deleteData(barangMasukTmpID);
    }

    public List<BarangMasukTmp> getData() {
        return barangMasukTmpService.getData();
    }

    public int sumJumlah() {
        return barangMasukTmpService.sumJumlah();
    }

    public double sumTotalBeli() {
        return barangMasukTmpService.sumTotalBeli();
    }

    public void clearTmp() {
        barangMasukTmpService.clearTmp();
    }
}
