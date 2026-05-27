package com.inventory.controller;

import com.inventory.model.BarangMasukDetail;
import com.inventory.service.BarangMasukDetailService;
import com.inventory.service.impl.BarangMasukDetailServiceImpl;

import java.util.List;

public class BarangMasukDetailController {

    private final BarangMasukDetailService barangMasukDetailService =
            new BarangMasukDetailServiceImpl();

    public boolean insertData(BarangMasukDetail detail) {
        return barangMasukDetailService.insertData(detail);
    }

    public List<BarangMasukDetail> getDataById(int barangMasukID,
                                               int itemsPerPage,
                                               int currentPage) {
        return barangMasukDetailService.getDataById(
                barangMasukID, itemsPerPage, currentPage
        );
    }

    public List<BarangMasukDetail> searchDataById(int barangMasukID,
                                                  String keyword,
                                                  int itemsPerPage,
                                                  int currentPage) {
        return barangMasukDetailService.searchDataById(
                barangMasukID, keyword, itemsPerPage, currentPage
        );
    }

    public int getTotalItems(int barangMasukID, String keyword) {
        return barangMasukDetailService.getTotalItems(barangMasukID, keyword);
    }
}
