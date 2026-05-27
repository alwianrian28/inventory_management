package com.inventory.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.inventory.config.DatabaseConnection;
import com.inventory.model.Barang;
import com.inventory.model.Satuan;
import com.inventory.model.StokOpnameTmp;

/**
 *
 * @author Dearclaudia
 */
public class OpnameTmpDAO{

    private final Connection conn;

    public OpnameTmpDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(StokOpnameTmp model) {
        PreparedStatement st = null;
        
        try {
            String sql = "INSERT INTO stok_opname_tmp "
                    + "(barang_id, stok_sistem, stok_fisik, selisih, catatan) "
                    + "VALUES (?,?,?,?,?)";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, model.getBarang().getBarangID());
            st.setInt(2, model.getStokSistem());
            st.setInt(3, model.getStokFisik());
            st.setInt(4, model.getSelisih());
            st.setString(5, model.getCatatan());
            
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(StokOpnameTmp model) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE stok_opname_tmp SET barang_id=?, stok_sistem = ?, stok_fisik = ?, selisih = ?, catatan = ? WHERE opname_tmp_id=?";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, model.getBarang().getBarangID());
            st.setInt(2, model.getStokSistem());
            st.setInt(3, model.getStokFisik());
            st.setInt(4, model.getSelisih());
            st.setString(5, model.getCatatan());
            st.setInt(6, model.getOpnameTmpID());
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(int opnameTmpID) {
        PreparedStatement st = null;
        
        try {
            String sql = "DELETE FROM stok_opname_tmp WHERE opname_tmp_id=?";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, opnameTmpID);
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllTmp() {
        PreparedStatement st = null;
        
        try {
            String sql = "DELETE FROM stok_opname_tmp";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void resetAI() {
        PreparedStatement st = null;
        
        try {
            String sql = "ALTER TABLE `stok_opname_tmp` AUTO_INCREMENT = 1";
            st = conn.prepareStatement(sql);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<StokOpnameTmp> getData() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<StokOpnameTmp> list = new ArrayList<>();
        
        try {
            String sql = "SELECT sot.opname_tmp_id, b.barang_id, b.barang_code, b.barcode, b.barang_name, "
                    + "s.satuan_name, sot.stok_sistem, sot.stok_fisik, sot.selisih, sot.catatan\n"
                    + "FROM stok_opname_tmp sot\n"
                    + "INNER JOIN barang b ON b.barang_id = sot.barang_id\n"
                    + "INNER JOIN satuan s ON s.satuan_id = b.satuan_id";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            while (rs.next()) {
                StokOpnameTmp modelTmp = new StokOpnameTmp();
                modelTmp.setOpnameTmpID(rs.getInt("opname_tmp_id"));
                modelTmp.setStokSistem(rs.getInt("stok_sistem"));
                modelTmp.setStokFisik(rs.getInt("stok_fisik"));
                modelTmp.setSelisih(rs.getInt("selisih"));
                modelTmp.setCatatan(rs.getString("catatan"));
                
                Barang modelBarang = new Barang();
                modelBarang.setBarangID(rs.getInt("barang_id"));
                modelBarang.setBarangCode(rs.getString("barang_code"));
                modelBarang.setBarcode(rs.getString("barcode"));
                modelBarang.setBarangName(rs.getString("barang_name"));
                modelTmp.setBarang(modelBarang);
                
                Satuan modelSatuan = new Satuan();
                modelSatuan.setSatuanName(rs.getString("satuan_name"));
                modelBarang.setSatuan(modelSatuan);
                
                list.add(modelTmp);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public void loadDetailToTmp(int opnameID) {
        PreparedStatement st = null;
        String sql = "INSERT INTO stok_opname_tmp "
                + "(barang_id, stok_sistem, stok_fisik, selisih, catatan) "
                + "SELECT det.barang_id, det.stok_sistem, det.stok_fisik, det.selisih, det.catatan "
                + "FROM stok_opname_detail det "
                + "WHERE det.opname_id = ?";
        
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, opnameID);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
