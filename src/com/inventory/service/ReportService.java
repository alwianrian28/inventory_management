package com.inventory.service;

import com.inventory.model.BarangKeluarDetail;
import com.inventory.model.BarangMasukDetail;
import com.inventory.model.StokOpnameDetail;
import com.inventory.util.ExportType;
import java.util.Date;
import java.util.List;

public interface ReportService {
    void previewBarangMasuk(String noTransaksi);
    void previewBarangKeluar(String noTransaksi);
    
    List<BarangMasukDetail> getDataStok(String keyword, int itemsPerPage, int currentPage);
    int getTotalItemsStock(String keyword);
    void exportStok(ExportType type);
    
    List<BarangMasukDetail> getDataBarangMasuk(Date startDate, Date endDate, String keyword, int itemsPerPage, int currentPage);
    int getTotalItemsBarangMasuk(Date startDate, Date endDate, String keyword);
    void exportBarangMasuk(Date startDate, Date endDate, ExportType type);
    
    List<BarangKeluarDetail> getDataBarangKeluar(Date startDate, Date endDate, String keyword, int itemsPerPage, int currentPage);
    int getTotalItemsBarangKeluar(Date startDate, Date endDate, String keyword);
    void exportBarangKeluar(Date startDate, Date endDate, ExportType type);
    
    List<StokOpnameDetail> getDataOpname(Date startDate, Date endDate, String keyword, int itemsPerPage, int currentPage);
    int getTotalItemsOpname(Date startDate, Date endDate, String keyword);
    void exportOpname(Date startDate, Date endDate, ExportType type);
}
