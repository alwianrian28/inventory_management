package com.inventory.service.impl;

import com.inventory.dao.DashboardDAO;
import com.inventory.model.StokGudang;
import com.inventory.model.StokOpname;
import com.inventory.service.DashboardService;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dearclaudia
 */
public class DashboardServiceImpl implements DashboardService{

    private final DashboardDAO dashboardDAO = new DashboardDAO();
    
    @Override
    public int getTotalBarangs() {
        return dashboardDAO.getTotalBarangs();
    }

    @Override
    public int getTotalSuppliers() {
        return dashboardDAO.getTotalSuppliers();
    }

    @Override
    public int getTotalJumlahMasukPerHari() {
        return dashboardDAO.getTotalJumlahMasukPerHari();
    }

    @Override
    public double getTotalMasukPerHari() {
        return dashboardDAO.getTotalMasukPerHari();
    }

    @Override
    public int getTotalJumlahKeluarPerHari() {
        return dashboardDAO.getTotalJumlahKeluarPerHari();
    }

    @Override
    public double getTotalKeluarPerHari() {
        return dashboardDAO.getTotalKeluarPerHari();
    }

    @Override
    public Map<String, Double> getBarangMasukPerHariBulanIni() {
        return dashboardDAO.getBarangMasukPerHariBulanIni();
    }

    @Override
    public Map<String, Double> getBarangMasukPerBulanTahunIni() {
        return dashboardDAO.getBarangMasukPerBulanTahunIni();
    }
    
    @Override
    public Map<String, Double> getBarangKeluarPerHariBulanIni() {
        return dashboardDAO.getBarangKeluarPerHariBulanIni();
    }

    @Override
    public Map<String, Double> getBarangKeluarPerBulanTahunIni() {
        return dashboardDAO.getBarangKeluarPerBulanTahunIni();
    }

    @Override
    public Map<String, Integer> getTop10Barang() {
        return dashboardDAO.getTop10Barang();
    }

    @Override
    public List<StokGudang> getLowStockBarangs() {
        return dashboardDAO.getLowStockBarangs();
    }

    @Override
    public List<StokOpname> getPendingStockOpname() {
        return dashboardDAO.getPendingStockOpname();
    }

}
