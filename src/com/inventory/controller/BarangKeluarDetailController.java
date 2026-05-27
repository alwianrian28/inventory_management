package com.inventory.controller;

import com.inventory.model.BarangKeluarDetail;
import com.inventory.service.BarangKeluarDetailService;
import com.inventory.service.impl.BarangKeluarDetailServiceImpl;

import java.util.List;

public class BarangKeluarDetailController {

    private final BarangKeluarDetailService barangKeluarDetailService =
            new BarangKeluarDetailServiceImpl();

    public boolean insertData(BarangKeluarDetail detail) {
        return barangKeluarDetailService.insertData(detail);
    }

    public List<BarangKeluarDetail> getDataById(int barangKeluarID,
                                               int itemsPerPage,
                                               int currentPage) {
        return barangKeluarDetailService.getDataById(
                barangKeluarID, itemsPerPage, currentPage
        );
    }

    public List<BarangKeluarDetail> searchDataById(int barangKeluarID,
                                                  String keyword,
                                                  int itemsPerPage,
                                                  int currentPage) {
        return barangKeluarDetailService.searchDataById(
                barangKeluarID, keyword, itemsPerPage, currentPage
        );
    }

    public int getTotalItems(int barangKeluarID, String keyword) {
        return barangKeluarDetailService.getTotalItems(barangKeluarID, keyword);
    }
}
