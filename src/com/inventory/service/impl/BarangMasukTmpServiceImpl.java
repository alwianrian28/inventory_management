package com.inventory.service.impl;

import com.inventory.dao.BarangMasukTmpDAO;
import com.inventory.model.BarangMasukTmp;
import com.inventory.service.BarangMasukTmpService;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class BarangMasukTmpServiceImpl implements BarangMasukTmpService {

    private final BarangMasukTmpDAO tmpDAO = new BarangMasukTmpDAO();

    @Override
    public boolean insertData(BarangMasukTmp tmp) {

        if (tmp == null) return false;

        if (tmp.getBarang() == null || tmp.getBarang().getBarangID() <= 0)
            return false;

        if (tmp.getJumlah() <= 0)
            return false;

        if (tmp.getHargaBeli() < 0)
            return false;

        if (tmp.getRak() == null || tmp.getRak().getRakID() <= 0)
            return false;

        if (tmp.getGudang() == null || tmp.getGudang().getGudangID() <= 0)
            return false;

        tmp.setSubtotal(tmp.getJumlah() * tmp.getHargaBeli());

        tmpDAO.insertData(tmp);
        return true;
    }

    @Override
    public boolean updateData(BarangMasukTmp tmp) {

        if (tmp == null || tmp.getBarangMasukTmpID() <= 0)
            return false;

        if (tmp.getJumlah() <= 0)
            return false;

        if (tmp.getHargaBeli() < 0)
            return false;

        if (tmp.getRak() == null || tmp.getRak().getRakID() <= 0)
            return false;

        if (tmp.getGudang() == null || tmp.getGudang().getGudangID() <= 0)
            return false;

        tmp.setSubtotal(tmp.getJumlah() * tmp.getHargaBeli());

        tmpDAO.updateData(tmp);
        return true;
    }

    @Override
    public boolean deleteData(int barangMasukTmpID) {

        if (barangMasukTmpID <= 0)
            return false;

        tmpDAO.deleteData(barangMasukTmpID);
        return true;
    }

    @Override
    public List<BarangMasukTmp> getData() {
        return tmpDAO.getData();
    }

    @Override
    public int sumJumlah() {
        return tmpDAO.sumJumlah();
    }

    @Override
    public double sumTotalBeli() {
        return tmpDAO.sumTotalBeli();
    }

    @Override
    public void clearTmp() {
        tmpDAO.deleteAllTmp();
        tmpDAO.resetAI();
    }
}
