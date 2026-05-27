package com.inventory.dao;

import com.inventory.config.DatabaseConnection;
import com.inventory.model.StokOpname;
import com.inventory.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

/**
 *
 * @author Dearclaudia
 */
public class OpnameDAO{

    private final Connection conn;

    public OpnameDAO() {
        conn = DatabaseConnection.getConnection();
        if (conn == null) {
            throw new IllegalStateException("Database connection is not available.");
        }
    }

    public void insertData(StokOpname model) {
        PreparedStatement st = null;
        
        try {
            String sql = "INSERT INTO stok_opname "
                    + "(tanggal_opname, keterangan, status, user_id, insert_by) "
                    + "VALUES (?,?,?,?,?)";
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            st.setString(1, model.getTanggalOpname());
            st.setString(2, model.getKeterangan());
            st.setString(3, model.getStatus());
            st.setInt(4, model.getUser().getUserID());
            st.setInt(5, model.getInsertBy());
            
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setOpnameID(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateData(StokOpname model) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE stok_opname SET tanggal_opname=?, keterangan=?, status=?, user_id=?, update_by = ?, update_at = NOW() WHERE opname_id=?";
            st = conn.prepareStatement(sql);
            
            st.setString(1, model.getTanggalOpname());
            st.setString(2, model.getKeterangan());
            st.setString(3, model.getStatus());
            st.setInt(4, model.getUser().getUserID());
            st.setInt(5, model.getUpdateBy());
            st.setInt(6, model.getOpnameID());
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(StokOpname model) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE stok_opname SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE opname_id=?";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getOpnameID());
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void restoreData(int opnameID) {
        PreparedStatement st = null;
        
        try {
            String sql = "UPDATE stok_opname SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE opname_id=?";
            st = conn.prepareStatement(sql);
            
            st.setInt(1, opnameID);
            
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<StokOpname> getData(boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<StokOpname> list = new ArrayList<>();
        
        try {
            String sql = "SELECT so.opname_id, so.tanggal_opname, so.keterangan, so.status, us.user_id, us.name\n" +
                    "FROM stok_opname so\n" +
                    "INNER JOIN `user` us ON us.user_id = so.user_id "
                    + "WHERE so.is_delete = ? "
                    + "LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setInt(2, itemsPerPage);
            st.setInt(3, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            
            while (rs.next()) {
                StokOpname modelOpname = new StokOpname();
                modelOpname.setOpnameID(rs.getInt("opname_id"));
                modelOpname.setTanggalOpname(rs.getString("tanggal_opname"));
                modelOpname.setKeterangan(rs.getString("keterangan"));
                modelOpname.setStatus(rs.getString("status"));
                
                User modelUser = new User();
                modelUser.setUserID(rs.getInt("user_id"));
                modelUser.setName(rs.getString("name"));
                modelOpname.setUser(modelUser);
                
                list.add(modelOpname);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public List<StokOpname> searchData(String keyword, boolean isDelete, int itemsPerPage, int currentPage) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<StokOpname> list = new ArrayList<>();
        
        try {
            String sql = "SELECT so.opname_id, so.tanggal_opname, so.keterangan, so.status, us.user_id, us.name\n" +
                    "FROM stok_opname so\n" +
                    "INNER JOIN `user` us ON us.user_id = so.user_id "
                    + "WHERE so.is_delete = ? AND (so.tanggal_opname LIKE ? OR so.keterangan LIKE ? OR so.status LIKE ? OR us.name LIKE ?) "
                    + "LIMIT ? OFFSET ?";
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setString(5, "%" + keyword + "%");
            st.setInt(6, itemsPerPage);
            st.setInt(7, (currentPage - 1) * itemsPerPage);
            rs = st.executeQuery();
            
            while (rs.next()) {
                StokOpname modelOpname = new StokOpname();
                modelOpname.setOpnameID(rs.getInt("opname_id"));
                modelOpname.setTanggalOpname(rs.getString("tanggal_opname"));
                modelOpname.setKeterangan(rs.getString("keterangan"));
                modelOpname.setStatus(rs.getString("status"));
                
                User modelUser = new User();
                modelUser.setUserID(rs.getInt("user_id"));
                modelUser.setName(rs.getString("name"));
                modelOpname.setUser(modelUser);
                
                list.add(modelOpname);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    public void applyStockAdjustment(int opnameID) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT det.barang_id, det.selisih "
                + "FROM stok_opname_detail det "
                + "WHERE det.opname_id = ?";

        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, opnameID);
            rs = st.executeQuery();

            while (rs.next()) {
                int barangID = rs.getInt("barang_id");
                int selisih = rs.getInt("selisih");

                if (selisih != 0) {
                    adjustStock(barangID, selisih);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void adjustStock(int barangID, int selisih){
        try {
            if (selisih == 0) return;

            String sql =
                "SELECT stok_id, jumlah " +
                "FROM stok_gudang " +
                "WHERE barang_id = ? " +
                "ORDER BY stok_id ASC";

            try (PreparedStatement ps = conn.prepareStatement(
                    sql,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_UPDATABLE)) {

                ps.setInt(1, barangID);

                try (ResultSet rs = ps.executeQuery()) {

                    int sisa = selisih;

                    while (rs.next() && sisa != 0) {
                        int jumlah = rs.getInt("jumlah");

                        int newJumlah = jumlah + sisa;

                        if (newJumlah >= 0) {
                            rs.updateInt("jumlah", newJumlah);
                            rs.updateRow();
                            sisa = 0;
                        } else {
                            rs.updateInt("jumlah", 0);
                            rs.updateRow();
                            sisa = newJumlah; // masih minus
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void rollbackStokOpname(int opnameID) {

        String sqlDetail =
            "SELECT barang_id, selisih " +
            "FROM stok_opname_detail " +
            "WHERE opname_id = ?";

        try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {

            psDetail.setInt(1, opnameID);

            try (ResultSet rsDetail = psDetail.executeQuery()) {

                while (rsDetail.next()) {
                    int barangID = rsDetail.getInt("barang_id");
                    int selisih  = rsDetail.getInt("selisih");

                    if (selisih != 0) {
                        rollbackStockByLIFO(barangID, selisih);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rollbackStockByLIFO(int barangID, int selisih) throws SQLException {

        String sql =
            "SELECT stok_id, jumlah " +
            "FROM stok_gudang " +
            "WHERE barang_id = ? " +
            "ORDER BY stok_id DESC"; // 🔥 LIFO

        try (PreparedStatement ps = conn.prepareStatement(
                sql,
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_UPDATABLE)) {

            ps.setInt(1, barangID);

            try (ResultSet rs = ps.executeQuery()) {

                int sisa = selisih;

                while (rs.next() && sisa != 0) {
                    int jumlah = rs.getInt("jumlah");

                    int newJumlah = jumlah - sisa;

                    if (newJumlah >= 0) {
                        rs.updateInt("jumlah", newJumlah);
                        rs.updateRow();
                        sisa = 0;
                    } else {
                        rs.updateInt("jumlah", 0);
                        rs.updateRow();
                        sisa = -newJumlah; // masih sisa rollback
                    }
                }
            }
        }
    }

    
    public int getTotalItems(boolean isDelete, String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) AS total "
                    + "FROM stok_opname so\n"
                    + "INNER JOIN `user` us ON us.user_id = so.user_id "
                    + "WHERE so.is_delete = ? AND (so.tanggal_opname LIKE ? OR so.keterangan LIKE ? OR so.status LIKE ? OR us.name LIKE ?)";

        try {
            st = conn.prepareStatement(sql);
            st.setBoolean(1, isDelete);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setString(5, "%" + keyword + "%");
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
