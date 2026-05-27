package com.inventory.service;
 
import com.inventory.model.StokGudang;
import com.inventory.model.StokOpname;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dearclaudia
 */
public interface DashboardService {
    // Card
    int getTotalBarangs();
    int getTotalSuppliers();
    int getTotalJumlahMasukPerHari();
    double getTotalMasukPerHari();
    int getTotalJumlahKeluarPerHari();
    double getTotalKeluarPerHari();
    
    //Chart
    Map<String, Double> getBarangMasukPerHariBulanIni();
    Map<String, Double> getBarangMasukPerBulanTahunIni();
    
    Map<String, Double> getBarangKeluarPerHariBulanIni();
    Map<String, Double> getBarangKeluarPerBulanTahunIni();
        
    Map<String, Integer> getTop10Barang();
    
    List<StokGudang> getLowStockBarangs();
    List<StokOpname> getPendingStockOpname();
}
