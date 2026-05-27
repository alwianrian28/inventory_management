package com.inventory.controller;

import com.inventory.model.StokGudang;
import com.inventory.model.StokOpname;
import com.inventory.service.DashboardService;
import com.inventory.service.impl.DashboardServiceImpl;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dearclaudia
 */
public class DashboardController {
    
    private final DashboardService dashboardService = new DashboardServiceImpl();
    
    public int getTotalBarangs() {
        return dashboardService.getTotalBarangs();
    }

    public int getTotalSuppliers() {
        return dashboardService.getTotalSuppliers();
    }

    public int getTotalJumlahMasukPerHari() {
        return dashboardService.getTotalJumlahMasukPerHari();
    }

    public double getTotalMasukPerHari() {
        return dashboardService.getTotalMasukPerHari();
    }

    public int getTotalJumlahKeluarPerHari() {
        return dashboardService.getTotalJumlahKeluarPerHari();
    }

    public double getTotalKeluarPerHari() {
        return dashboardService.getTotalKeluarPerHari();
    }

    public Map<String, Double> getBarangMasukPerHariBulanIni() {
        return dashboardService.getBarangMasukPerHariBulanIni();
    }

    public Map<String, Double> getBarangMasukPerBulanTahunIni() {
        return dashboardService.getBarangMasukPerBulanTahunIni();
    }
    
    public Map<String, Double> getBarangKeluarPerHariBulanIni() {
        return dashboardService.getBarangKeluarPerHariBulanIni();
    }

    public Map<String, Double> getBarangKeluarPerBulanTahunIni() {
        return dashboardService.getBarangKeluarPerBulanTahunIni();
    }

    public Map<String, Integer> getTop10Barang() {
        return dashboardService.getTop10Barang();
    }

    public List<StokGudang> getLowStockBarangs() {
        return dashboardService.getLowStockBarangs();
    }

    public List<StokOpname> getPendingStockOpname() {
        return dashboardService.getPendingStockOpname();
    }
}
