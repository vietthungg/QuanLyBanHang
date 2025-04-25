package dao;

import java.sql.*;
import java.util.*;
import entity.SanPham;

public class SanPhamDao {
    private Connection conn;

    public SanPhamDao() {
        try {
            // Kết nối SQL Server
            String url = "jdbc:sqlserver://localhost:1433;databasename=SalesManagement";
            String user = "sa";
            String pass = "123";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Products");
            while (rs.next()) {
                list.add(new SanPham(
                    rs.getString("ProductID"),
                    rs.getString("ProductName"),
                    rs.getString("CategoryID"),
                    rs.getDouble("Price"),
                    rs.getInt("StockQuantity")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SanPham> searchSanPham(String ma, String ten) {
        List<SanPham> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Products WHERE ProductID LIKE ? AND ProductName LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + ma + "%");
            pst.setString(2, "%" + ten + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new SanPham(
                    rs.getString("ProductID"),
                    rs.getString("ProductName"),
                    rs.getString("CategoryID"),
                    rs.getDouble("Price"),
                    rs.getInt("StockQuantity")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SanPham> searchSanPham(String ma, String ten, String loai) {
        List<SanPham> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Products WHERE ProductID LIKE ? AND ProductName LIKE ?";
            if (!"Tất cả".equalsIgnoreCase(loai)) {
                sql += " AND CategoryID = ?";
            }
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + ma + "%");
            pst.setString(2, "%" + ten + "%");
            if (!"Tất cả".equalsIgnoreCase(loai)) {
                pst.setString(3, loai);
            }
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new SanPham(
                    rs.getString("ProductID"),
                    rs.getString("ProductName"),
                    rs.getString("CategoryID"),
                    rs.getDouble("Price"),
                    rs.getInt("StockQuantity")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
