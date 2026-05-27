package com.inventory.config;

import com.inventory.util.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Dearclaudia
 */
public class DatabaseConnection {
    
    private static Connection conn;
    
    public static Connection getConnection(){
        if(conn == null){
            try {
                String url = "jdbc:mysql://" + AppConfig.get("db.host") + ":" + AppConfig.get("db.port") + "/" +AppConfig.get("db.name");
                String user = AppConfig.get("db.user");
                String pass = AppConfig.get("db.password");

                conn = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(null, 
                        "Gagal terhubung ke database.\nPastikan MySQL Server aktif dan konfigurasi koneksi sudah benar.");
                System.exit(0);
            }
        }
        
        return conn;
    }
}
