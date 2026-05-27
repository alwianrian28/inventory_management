package com.inventory.service.impl;

import com.inventory.dao.OpnameTmpDAO;
import com.inventory.model.StokOpnameTmp;
import java.util.List;
import com.inventory.service.OpnameTmpService;

/**
 *
 * @author Dearclaudia
 */
public class OpnameTmpServiceImpl implements OpnameTmpService {

    private final OpnameTmpDAO tmpDAO = new OpnameTmpDAO();

    @Override
    public boolean insertData(StokOpnameTmp model) {

        if (model == null) return false;

        if (model.getBarang() == null || model.getBarang().getBarangID() <= 0)
            return false;

        tmpDAO.insertData(model);
        return true;
    }

    @Override
    public boolean updateData(StokOpnameTmp model) {

        if (model == null || model.getOpnameTmpID() <= 0)
            return false;

        tmpDAO.updateData(model);
        return true;
    }

    @Override
    public boolean deleteData(int opnameTmpID) {

        if (opnameTmpID <= 0)
            return false;

        tmpDAO.deleteData(opnameTmpID);
        return true;
    }

    @Override
    public boolean clearTmp() {
        tmpDAO.deleteAllTmp();
        tmpDAO.resetAI();
        return true;
    }

    @Override
    public List<StokOpnameTmp> getData() {
        return tmpDAO.getData();
    }

    @Override
    public boolean loadDetailToTmp(int opnameID) {

        if (opnameID <= 0)
            return false;

        tmpDAO.loadDetailToTmp(opnameID);
        return true;
    }
}
