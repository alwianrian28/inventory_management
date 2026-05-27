package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.Barang;
import com.inventory.model.Satuan;
import com.inventory.model.StokOpname;
import com.inventory.model.StokOpnameDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dearclaudia
 */
public class OpnameDetailDAO{

    private final Connection conn;

    public OpnameDetailDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(int opnameID) {
        PreparedStatement st = null;
        try {
            String sql = "INSERT INTO stok_opname_detail " +
                        "(opname_id, barang_id, stok_sistem, stok_fisik, selisih, catatan) " +
                        "SELECT ?, barang_id, stok_sistem, stok_fisik, selisih, catatan " +
                        "FROM stok_opname_tmp ";
            st = conn.prepareStatement(sql);
            st.setInt(1, opnameID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteData(int opnameID) {
        PreparedStatement st = null;
        try {
            String sql = "DELETE FROM stok_opname_detail WHERE opname_id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, opnameID);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<StokOpnameDetail> getDataById(int opnameID, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<StokOpnameDetail> list = new ArrayList<>();
        
        try {
            String sql = "SELECT det.opname_detail_id, so.opname_id, so.tanggal_opname, b.barang_id, b.barang_code, b.barcode, "
                    + "b.barang_name, s.satuan_name, det.stok_sistem, det.stok_fisik, det.selisih, det.catatan\n" +
                    "FROM stok_opname_detail det\n" +
                    "INNER JOIN stok_opname so on so.opname_id = det.opname_id\n" +
                    "INNER JOIN barang b ON b.barang_id = det.barang_id\n" +
                    "INNER JOIN satuan s ON s.satuan_id = b.satuan_id "
                    + "WHERE so.opname_id = ? "
                    + "LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, opnameID);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            
            while (rs.next()) {
                StokOpnameDetail modelDetail = new StokOpnameDetail();
                modelDetail.setOpnameDetailID(rs.getInt("opname_detail_id"));
                modelDetail.setStokSistem(rs.getInt("stok_sistem"));
                modelDetail.setStokFisik(rs.getInt("stok_fisik"));
                modelDetail.setSelisih(rs.getInt("selisih"));
                modelDetail.setCatatan(rs.getString("catatan"));
                
                StokOpname modelOpname = new StokOpname();
                modelOpname.setTanggalOpname(rs.getString("tanggal_opname"));
                modelDetail.setStokOpname(modelOpname);
                
                Barang modelBarang = new Barang();
                modelBarang.setBarangID(rs.getInt("barang_id"));
                modelBarang.setBarangCode(rs.getString("barang_code"));
                modelBarang.setBarcode(rs.getString("barcode"));
                modelBarang.setBarangName(rs.getString("barang_name"));
                modelDetail.setBarang(modelBarang);
                
                Satuan modelSatuan = new Satuan();
                modelSatuan.setSatuanName(rs.getString("satuan_name"));
                modelBarang.setSatuan(modelSatuan);
                
                list.add(modelDetail);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public List<StokOpnameDetail> searchDataById(int opnameID, String keyword, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<StokOpnameDetail> list = new ArrayList<>();
        
        try {
            String sql = "SELECT det.opname_detail_id, so.opname_id, so.tanggal_opname, b.barang_id, b.barang_code, b.barcode, "
                    + "b.barang_name, s.satuan_name, det.stok_sistem, det.stok_fisik, det.selisih, det.catatan\n" +
                    "FROM stok_opname_detail det\n" +
                    "INNER JOIN stok_opname so on so.opname_id = det.opname_id\n" +
                    "INNER JOIN barang b ON b.barang_id = det.barang_id\n" +
                    "INNER JOIN satuan s ON s.satuan_id = b.satuan_id "
                    + "WHERE so.opname_id = ? AND "
                    + "(b.barang_code LIKE ? OR b.barcode LIKE ? OR b.barang_name LIKE ? OR s.satuan_name LIKE ? OR so.tanggal_opname LIKE ? "
                    + "OR det.stok_sistem LIKE ? OR det.stok_fisik LIKE ? OR det.selisih LIKE ? OR det.catatan LIKE ?) "
                    + "LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, opnameID);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setString(5, "%" + keyword + "%");
            st.setString(6, "%" + keyword + "%");
            st.setString(7, "%" + keyword + "%");
            st.setString(8, "%" + keyword + "%");
            st.setString(9, "%" + keyword + "%");
            st.setString(10, "%" + keyword + "%");
            st.setInt(11, itemsPerPage);
            st.setInt(12, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            
            while (rs.next()) {
                StokOpnameDetail modelDetail = new StokOpnameDetail();
                modelDetail.setOpnameDetailID(rs.getInt("opname_detail_id"));
                modelDetail.setStokSistem(rs.getInt("stok_sistem"));
                modelDetail.setStokFisik(rs.getInt("stok_fisik"));
                modelDetail.setSelisih(rs.getInt("selisih"));
                modelDetail.setCatatan(rs.getString("catatan"));
                
                StokOpname modelOpname = new StokOpname();
                modelOpname.setTanggalOpname(rs.getString("tanggal_opname"));
                modelDetail.setStokOpname(modelOpname);
                
                Barang modelBarang = new Barang();
                modelBarang.setBarangID(rs.getInt("barang_id"));
                modelBarang.setBarangCode(rs.getString("barang_code"));
                modelBarang.setBarcode(rs.getString("barcode"));
                modelBarang.setBarangName(rs.getString("barang_name"));
                modelDetail.setBarang(modelBarang);
                
                Satuan modelSatuan = new Satuan();
                modelSatuan.setSatuanName(rs.getString("satuan_name"));
                modelBarang.setSatuan(modelSatuan);
                
                list.add(modelDetail);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public int getTotalItems(int opnameID, String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) AS total "
                    + "FROM stok_opname_detail det\n"
                    + "INNER JOIN stok_opname so on so.opname_id = det.opname_id\n"
                    + "INNER JOIN barang b ON b.barang_id = det.barang_id\n"
                    + "INNER JOIN satuan s ON s.satuan_id = b.satuan_id "
                    + "WHERE so.opname_id = ? AND "
                    + "(b.barang_code LIKE ? OR b.barcode LIKE ? OR b.barang_name LIKE ? OR s.satuan_name LIKE ? OR so.tanggal_opname LIKE ? "
                    + "OR det.stok_sistem LIKE ? OR det.stok_fisik LIKE ? OR det.selisih LIKE ? OR det.catatan LIKE ?) ";

        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, opnameID);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setString(5, "%" + keyword + "%");
            st.setString(6, "%" + keyword + "%");
            st.setString(7, "%" + keyword + "%");
            st.setString(8, "%" + keyword + "%");
            st.setString(9, "%" + keyword + "%");
            st.setString(10, "%" + keyword + "%");
            rs = st.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
