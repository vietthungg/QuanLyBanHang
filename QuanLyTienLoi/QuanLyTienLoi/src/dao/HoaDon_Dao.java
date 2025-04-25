package dao;

import java.sql.*;
import java.util.List;
import entity.ChiTietHoaDon;

public class HoaDon_Dao {
    private Connection conn;

    public HoaDon_Dao() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databasename=SalesManagement";
            String user = "sa";
            String pass = "123";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean themHoaDon(String maHD, String maKH, Timestamp ngayLap, double tongTien) {
        try {
            String sql = "INSERT INTO Orders (OrderID, CustomerID, OrderDate, TotalAmount) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, maHD);
            pst.setString(2, maKH);
            pst.setTimestamp(3, ngayLap);
            pst.setDouble(4, tongTien);
            return pst.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean themChiTietHoaDon(List<ChiTietHoaDon> dsChiTiet) {
        try {
            String sql = "INSERT INTO OrderDetails (OrderDetailID, OrderID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            for (ChiTietHoaDon cthd : dsChiTiet) {
                pst.setString(1, cthd.getMaChiTietHoaDon());
                pst.setString(2, cthd.getMaHoaDon());
                pst.setString(3, cthd.getMaSanPham());
                pst.setInt(4, cthd.getSoLuong());
                pst.setDouble(5, cthd.getDonGia());
                pst.addBatch();
            }
            int[] results = pst.executeBatch();
            return results.length == dsChiTiet.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
