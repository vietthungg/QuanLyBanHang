package gui;

import entity.KhachHang;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ThemKhachHang_Gui extends JDialog {
    private JTextField txtTenKH, txtSDT;
    private JLabel lblMaKH;
    private KhachHang khachHangMoi;
    private Connection conn;

    public ThemKhachHang_Gui(JFrame parent) {
        super(parent, "Thêm Khách Hàng", true);
        setLayout(new GridLayout(6, 2, 10, 10));

        try {
            String url = "jdbc:sqlserver://localhost:1433;databasename=SalesManagement";
            String user = "sa";
            String pass = "123";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(new JLabel("Mã khách hàng:"));
        lblMaKH = new JLabel();
        lblMaKH.setForeground(Color.RED);
        add(lblMaKH);

        add(new JLabel("Tên khách hàng:"));
        txtTenKH = new JTextField();
        add(txtTenKH);

        add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        add(txtSDT);

        JPanel buttonPanel = new JPanel();
        JButton btnThem = new JButton("➕ Thêm");
        JButton btnLamMoi = new JButton("Làm mới");
        JButton btnHuy = new JButton("Hủy");

        buttonPanel.add(btnHuy);
        buttonPanel.add(btnLamMoi);
        buttonPanel.add(btnThem);
        add(new JLabel());
        add(buttonPanel);

        // Tự động sinh mã và hiển thị
        lblMaKH.setText(sinhMaKhachHangTuDong());

        btnHuy.addActionListener(e -> dispose());

        btnLamMoi.addActionListener(e -> {
            txtTenKH.setText("");
            txtSDT.setText("");
        });

        btnThem.addActionListener(e -> {
            String ten = txtTenKH.getText().trim();
            String sdt = txtSDT.getText().trim();
            if (ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin.");
                return;
            }

            try {
                String newID = sinhMaKhachHangTuDong();
                KhachHang kh = new KhachHang(newID, ten, sdt);

                String sql = "INSERT INTO Customers (CustomerID, FullName, Phone) VALUES (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, newID);
                pst.setString(2, ten);
                pst.setString(3, sdt);
                int result = pst.executeUpdate();

                if (result > 0) {
                    khachHangMoi = kh;
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng.");
            }
        });

        setSize(700, 350);
        setLocationRelativeTo(parent);
    }

    public KhachHang getKhachHangMoi() {
        return khachHangMoi;
    }

    private String sinhMaKhachHangTuDong() {
        String prefix = "CU";
        int number = 4;
        try {
            String sql = "SELECT MAX(CustomerID) AS MaxID FROM Customers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String maxId = rs.getString("MaxID");
                if (maxId != null && maxId.startsWith(prefix)) {
                    number = Integer.parseInt(maxId.substring(2)) + 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefix + String.format("%03d", number);
    }
}
