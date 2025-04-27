package dao;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

public class HoaDonPDFCreator {

    public static void xuatHoaDon(String maKH, String tenKH, DefaultTableModel invoiceModel, double tongTien, int soHoaDon) throws Exception {
        // Nhúng phông DejaVuSans
        BaseFont bf = BaseFont.createFont("Font/DejaVuSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font unicodeFont = new Font(bf, 12);
        Font unicodeBoldFont = new Font(bf, 14, Font.BOLD);

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=SalesManagement", "sa", "123");
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Tìm OrderID lớn nhất và tăng lên 1
            int newSoHoaDon = 1;
            String query = "SELECT MAX(CAST(SUBSTRING(OrderID, 2, LEN(OrderID)) AS INT)) FROM Orders";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next() && rs.getInt(1) > 0) { // Kiểm tra bảng không trống và giá trị hợp lệ
                newSoHoaDon = rs.getInt(1) + 1;
            }
            String orderId = "O" + String.format("%03d", newSoHoaDon);

            // Kiểm tra xem OrderID đã tồn tại chưa
            String checkQuery = "SELECT COUNT(*) FROM Orders WHERE OrderID = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, orderId);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            while (checkRs.getInt(1) > 0) { // Nếu OrderID đã tồn tại, tăng lên
                newSoHoaDon++;
                orderId = "O" + String.format("%03d", newSoHoaDon);
                checkStmt.setString(1, orderId);
                checkRs = checkStmt.executeQuery();
                checkRs.next();
            }

            // Lưu hóa đơn vào bảng Orders
            String insertOrder = "INSERT INTO Orders (OrderID, CustomerID, OrderDate, TotalAmount) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(insertOrder);
            pst.setString(1, orderId);
            pst.setString(2, maKH);
            pst.setString(3, LocalDate.now().toString());
            pst.setDouble(4, tongTien);
            pst.executeUpdate();

            // Lưu chi tiết hóa đơn
            for (int i = 0; i < invoiceModel.getRowCount(); i++) {
                String maSP = invoiceModel.getValueAt(i, 0).toString();
                int soLuong = Integer.parseInt(invoiceModel.getValueAt(i, 4).toString());
                int donGia = Integer.parseInt(invoiceModel.getValueAt(i, 3).toString().replaceAll("[^\\d]", ""));
                String detailId = "OD" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

                String insertDetail = "INSERT INTO OrderDetails (OrderDetailID, OrderID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst2 = conn.prepareStatement(insertDetail);
                pst2.setString(1, detailId);
                pst2.setString(2, orderId);
                pst2.setString(3, maSP);
                pst2.setInt(4, soLuong);
                pst2.setDouble(5, donGia);
                pst2.executeUpdate();
            }

            // Xuất file PDF
            Document doc = new Document();
            String filePath = "hoadon_" + orderId + ".pdf";
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();

            doc.add(new Paragraph("HÓA ĐƠN BÁN HÀNG", unicodeBoldFont));
            doc.add(new Paragraph("Mã hóa đơn: " + orderId, unicodeFont));
            doc.add(new Paragraph("Ngày lập: " + LocalDate.now(), unicodeFont));
            doc.add(new Paragraph("Tên khách hàng: " + tenKH, unicodeFont));
            doc.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            PdfPCell cell;
            cell = new PdfPCell(new Phrase("Tên sản phẩm", unicodeFont));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Số lượng", unicodeFont));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Đơn giá", unicodeFont));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Thành tiền", unicodeFont));
            table.addCell(cell);

            for (int i = 0; i < invoiceModel.getRowCount(); i++) {
                table.addCell(new PdfPCell(new Phrase(invoiceModel.getValueAt(i, 1).toString(), unicodeFont)));
                table.addCell(new PdfPCell(new Phrase(invoiceModel.getValueAt(i, 4).toString(), unicodeFont)));
                table.addCell(new PdfPCell(new Phrase(invoiceModel.getValueAt(i, 3).toString(), unicodeFont)));
                table.addCell(new PdfPCell(new Phrase(invoiceModel.getValueAt(i, 5).toString(), unicodeFont)));
            }

            doc.add(table);
            doc.close();

            Desktop.getDesktop().open(new File(filePath));

            conn.commit(); // Xác nhận giao dịch

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Hoàn tác nếu có lỗi
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}