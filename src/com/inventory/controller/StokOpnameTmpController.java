package com.inventory.controller;

import com.inventory.model.StokOpnameTmp;
import com.inventory.service.impl.OpnameTmpServiceImpl;

import java.util.List;
import com.inventory.service.OpnameTmpService;

/**
 *
 * @author Dearclaudia
 */
public class StokOpnameTmpController {

    private final OpnameTmpService tmpService = new OpnameTmpServiceImpl();

    public boolean insertData(StokOpnameTmp model) {
        return tmpService.insertData(model);
    }

    public boolean updateData(StokOpnameTmp model) {
        return tmpService.updateData(model);
    }

    public boolean deleteData(int opnameTmpID) {
        return tmpService.deleteData(opnameTmpID);
    }

    public boolean clearTmp() {
        return tmpService.clearTmp();
    }

    public List<StokOpnameTmp> getData() {
        return tmpService.getData();
    }

    public boolean loadDetailToTmp(int opnameID) {
        return tmpService.loadDetailToTmp(opnameID);
    }
}
