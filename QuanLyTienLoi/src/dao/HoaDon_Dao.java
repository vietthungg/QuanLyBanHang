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

    // Thêm hóa đơn
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

    // Lấy tất cả hóa đơn
    public ResultSet getAllOrders() {
        try {
            String sql = "SELECT OrderID, CustomerID, OrderDate, TotalAmount FROM Orders";
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật hóa đơn
    public boolean updateOrder(String orderId, double totalAmount) {
        try {
            String sql = "UPDATE Orders SET TotalAmount = ? WHERE OrderID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, totalAmount);
            pst.setString(2, orderId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa hóa đơn
//    public boolean deleteOrder(String orderId) {
//        try {
//            String sql = "DELETE FROM Orders WHERE OrderID = ?";
//            PreparedStatement pst = conn.prepareStatement(sql);
//            pst.setString(1, orderId);
//            return pst.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }


    public ResultSet searchOrders(String searchText) {
        try {
            // Tìm kiếm theo OrderID hoặc OrderDate (theo định dạng yyyy-MM)
            String sql = "SELECT OrderID, CustomerID, OrderDate, TotalAmount FROM Orders " +
                         "WHERE OrderID LIKE ? OR FORMAT(OrderDate, 'yyyy-MM') LIKE ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + searchText + "%");
            pst.setString(2, "%" + searchText + "%");
            return pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
