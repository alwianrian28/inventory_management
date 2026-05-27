package com.inventory.service.impl;

import com.inventory.dao.OpnameDetailDAO;
import com.inventory.model.StokOpnameDetail;
import java.util.List;
import com.inventory.service.OpnameDetailService;

/**
 *
 * @author Dearclaudia
 */
public class OpnameDetailServiceImpl implements OpnameDetailService {

    private final OpnameDetailDAO detailDAO = new OpnameDetailDAO();

    @Override
    public boolean insertData(int opnameID) {

        if (opnameID <= 0)
            return false;

        detailDAO.insertData(opnameID);
        return true;
    }

    @Override
    public boolean deleteData(int opnameID) {

        if (opnameID <= 0)
            return false;

        detailDAO.deleteData(opnameID);
        return true;
    }

    @Override
    public List<StokOpnameDetail> getDataById(int opnameID, int itemsPerPage, int currentPage) {

        if (opnameID <= 0)
            return List.of();

        return detailDAO.getDataById(opnameID, itemsPerPage, currentPage);
    }

    @Override
    public List<StokOpnameDetail> searchDataById(int opnameID, String keyword, int itemsPerPage, int currentPage) {

        if (opnameID <= 0)
            return List.of();

        return detailDAO.searchDataById(opnameID, keyword, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItems(int opnameID, String keyword) {

        if (opnameID <= 0)
            return 0;

        return detailDAO.getTotalItems(opnameID, keyword);
    }
}
