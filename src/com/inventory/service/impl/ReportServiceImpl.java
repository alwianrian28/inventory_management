package com.inventory.service.impl;

import com.inventory.dao.ReportDAO;
import com.inventory.model.BarangKeluarDetail;
import com.inventory.model.BarangMasukDetail;
import com.inventory.model.StokOpnameDetail;
import com.inventory.service.ReportService;
import com.inventory.util.ExportType;
import java.util.Date;
import java.util.List;

public class ReportServiceImpl implements ReportService {

    private final ReportDAO reportDAO = new ReportDAO();

    @Override
    public void previewBarangMasuk(String noTransaksi) {
        reportDAO.previewBarangMasuk(noTransaksi);
    }

    @Override
    public void previewBarangKeluar(String noTransaksi) {
        reportDAO.previewBarangKeluar(noTransaksi);
    }

    @Override
    public List<BarangMasukDetail> getDataStok(String keyword, int itemsPerPage, int currentPage) {
        return reportDAO.getDataStok(keyword, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItemsStock(String keyword) {
        return reportDAO.getTotalItemsStok(keyword);
    }

    @Override
    public void exportStok(ExportType type) {
        if (type == ExportType.PREVIEW) {
            reportDAO.previewStok();
        } else if (type == ExportType.PDF) {
            reportDAO.exportStokPDF();
        } else {
            reportDAO.exportStokExcel();
        }
    }

    @Override
    public List<BarangMasukDetail> getDataBarangMasuk(Date startDate, Date endDate, String keyword, int itemsPerPage, int currentPage) {
        return reportDAO.getDataBarangMasuk(startDate, endDate, keyword, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItemsBarangMasuk(Date startDate, Date endDate, String keyword) {
        return reportDAO.getTotalItemsBarangMasuk(startDate, endDate, keyword);
    }

    @Override
    public void exportBarangMasuk(Date startDate, Date endDate, ExportType type) {
        if (type == ExportType.PREVIEW) {
            reportDAO.previewBarangMasuk(startDate, endDate);
        } else if (type == ExportType.PDF) {
            reportDAO.exportBarangMasukPDF(startDate, endDate);
        } else {
            reportDAO.exportBarangMasukExcel(startDate, endDate);
        }
    }

    @Override
    public List<BarangKeluarDetail> getDataBarangKeluar(Date startDate, Date endDate, String keyword, int itemsPerPage, int currentPage) {
        return reportDAO.getDataBarangKeluar(startDate, endDate, keyword, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItemsBarangKeluar(Date startDate, Date endDate, String keyword) {
        return reportDAO.getTotalItemsBarangKeluar(startDate, endDate, keyword);
    }

    @Override
    public void exportBarangKeluar(Date startDate, Date endDate, ExportType type) {
        if (type == ExportType.PREVIEW) {
            reportDAO.previewBarangKeluar(startDate, endDate);
        } else if (type == ExportType.PDF) {
            reportDAO.exportBarangKeluarPDF(startDate, endDate);
        } else {
            reportDAO.exportBarangKeluarExcel(startDate, endDate);
        }
    }

    @Override
    public List<StokOpnameDetail> getDataOpname(Date startDate, Date endDate, String keyword, int itemsPerPage, int currentPage) {
        return reportDAO.getDataOpname(startDate, endDate, keyword, itemsPerPage, currentPage);
    }

    @Override
    public int getTotalItemsOpname(Date startDate, Date endDate, String keyword) {
        return reportDAO.getTotalItemsStokOpname(startDate, endDate, keyword);
    }

    @Override
    public void exportOpname(Date startDate, Date endDate, ExportType type) {
        if (type == ExportType.PREVIEW) {
            reportDAO.previewOpname(startDate, endDate);
        } else if (type == ExportType.PDF) {
            reportDAO.exportOpnamePDF(startDate, endDate);
        } else {
            reportDAO.exportOpnameExcel(startDate, endDate);
        }
    }
}
