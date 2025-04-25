package dao;

import entity.KhachHang;
import java.sql.*;

public class KhachHang_Dao {
    private Connection conn;

    public KhachHang_Dao() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databasename=SalesManagement";
            String user = "sa";
            String pass = "123";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public KhachHang timKhachHangBangSDT(String sdt) {
        try {
            String sql = "SELECT * FROM Customers WHERE Phone = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, sdt);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new KhachHang(
                        rs.getString("CustomerID"),
                        rs.getString("FullName"),
                        rs.getString("Phone")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String layMaKHTheoSDT(String sdt) {
        try {
            String sql = "SELECT CustomerID FROM Customers WHERE Phone = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, sdt);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("CustomerID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
