package com.inventory.service.impl;

import com.inventory.dao.BarangKeluarTmpDAO;
import com.inventory.model.BarangKeluarTmp;
import com.inventory.service.BarangKeluarTmpService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangKeluarTmpServiceImpl implements BarangKeluarTmpService {

    private final BarangKeluarTmpDAO tmpDAO = new BarangKeluarTmpDAO();

    @Override
    public boolean insertData(BarangKeluarTmp tmp) {

        if (tmp == null) return false;

        if (tmp.getBarang() == null || tmp.getBarang().getBarangID() <= 0)
            return false;

        if (tmp.getJumlah() <= 0)
            return false;

        if (tmp.getHargaJual()< 0)
            return false;

        tmp.setSubtotal(tmp.getJumlah() * tmp.getHargaJual());

        tmpDAO.insertData(tmp);
        return true;
    }

    @Override
    public boolean updateData(BarangKeluarTmp tmp) {

        if (tmp == null || tmp.getBarangKeluarTmpID() <= 0)
            return false;

        if (tmp.getJumlah() <= 0)
            return false;

        if (tmp.getHargaJual()< 0)
            return false;

        tmp.setSubtotal(tmp.getJumlah() * tmp.getHargaJual());

        tmpDAO.updateData(tmp);
        return true;
    }

    @Override
    public boolean deleteData(int barangKeluarTmpID) {

        if (barangKeluarTmpID <= 0)
            return false;

        tmpDAO.deleteData(barangKeluarTmpID);
        return true;
    }

    @Override
    public List<BarangKeluarTmp> getData() {
        return tmpDAO.getData();
    }

    @Override
    public int sumJumlah() {
        return tmpDAO.sumJumlah();
    }

    @Override
    public double sumTotalJual() {
        return tmpDAO.sumTotalJual();
    }

    @Override
    public void clearTmp() {
        tmpDAO.deleteAllTmp();
        tmpDAO.resetAI();
    }
}
