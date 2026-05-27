package com.inventory.controller;

import com.inventory.model.BarangKeluarDetail;
import com.inventory.model.BarangMasukDetail;
import com.inventory.model.StokOpnameDetail;
import com.inventory.service.ReportService;
import com.inventory.service.impl.ReportServiceImpl;
import com.inventory.util.ExportType;
import java.util.Date;
import java.util.List;

public class ReportController {

    private final ReportService reportService = new ReportServiceImpl();

    public void previewBarangMasuk(String noTransaksi){
        reportService.previewBarangMasuk(noTransaksi);
    }
    
    public void previewBarangKeluar(String noTransaksi){
        reportService.previewBarangKeluar(noTransaksi);
    }
    
    public List<BarangMasukDetail> getDataStok(String keyword, int itemsPerPage, int currentPage) {
        return reportService.getDataStok(keyword, itemsPerPage, currentPage);
    }

    public int getTotalItemsStok(String keyword) {
        return reportService.getTotalItemsStock(keyword);
    }

    public void exportStok(ExportType type) {
        reportService.exportStok(type);
    }

    public List<BarangMasukDetail> getDataBarangMasuk(
            Date startDate,
            Date endDate,
            String keyword,
            int itemsPerPage,
            int currentPage) {
        return reportService.getDataBarangMasuk(
                startDate, endDate, keyword, itemsPerPage, currentPage);
    }

    public int getTotalItemsBarangMasuk(Date startDate, Date endDate, String keyword) {
        return reportService.getTotalItemsBarangMasuk(startDate, endDate, keyword);
    }

    public void exportBarangMasuk(Date startDate, Date endDate, ExportType type) {
        reportService.exportBarangMasuk(startDate, endDate, type);
    }

    public List<BarangKeluarDetail> getDataBarangKeluar(
            Date startDate,
            Date endDate,
            String keyword,
            int itemsPerPage,
            int currentPage) {
        return reportService.getDataBarangKeluar(
                startDate, endDate, keyword, itemsPerPage, currentPage);
    }

    public int getTotalItemsBarangKeluar(Date startDate, Date endDate, String keyword) {
        return reportService.getTotalItemsBarangKeluar(startDate, endDate, keyword);
    }

    public void exportBarangKeluar(Date startDate, Date endDate, ExportType type) {
        reportService.exportBarangKeluar(startDate, endDate, type);
    }

    public List<StokOpnameDetail> getDataOpname(
            Date startDate,
            Date endDate,
            String keyword,
            int itemsPerPage,
            int currentPage) {
        return reportService.getDataOpname(
                startDate, endDate, keyword, itemsPerPage, currentPage);
    }

    public int getTotalItemsOpname(Date startDate, Date endDate, String keyword) {
        return reportService.getTotalItemsOpname(startDate, endDate, keyword);
    }

    public void exportOpname(Date startDate, Date endDate, ExportType type) {
        reportService.exportOpname(startDate, endDate, type);
    }
}
