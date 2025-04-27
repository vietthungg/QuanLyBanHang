package gui;

import dao.HoaDon_Dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class QuanLyHoaDon_Gui extends JPanel {
    private JTable hoaDonTable;
    private JButton updateButton;
    private JButton searchByIdButton;
    private JButton searchByDateButton;
    private JTextField searchByIdTextField;
    private JTextField searchByDateTextField;
    private DefaultTableModel tableModel;
    private HoaDon_Dao hoaDonDao;

    public QuanLyHoaDon_Gui() {
        setLayout(new BorderLayout());

        hoaDonDao = new HoaDon_Dao();

        // Tạo bảng và thêm cột
        String[] columns = {"OrderID", "CustomerID", "OrderDate", "TotalAmount"};
        tableModel = new DefaultTableModel(columns, 0);
        hoaDonTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(hoaDonTable);

        // Đặt font chữ cho bảng
        hoaDonTable.setFont(new Font("Arial", Font.PLAIN, 16));
        hoaDonTable.setRowHeight(30);

        // Panel chứa các nút hành động và trường tìm kiếm
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

        // Tạo panel tìm kiếm theo mã hóa đơn
        JPanel searchByIdPanel = new JPanel();
        searchByIdPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchByIdTextField = new JTextField(20);
        searchByIdTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchByIdButton = new JButton("Tìm kiếm theo mã hóa đơn");
        searchByIdButton.setFont(new Font("Arial", Font.PLAIN, 16));
        searchByIdPanel.add(new JLabel("Tìm kiếm theo mã hóa đơn:"));
        searchByIdPanel.add(searchByIdTextField);
        searchByIdPanel.add(searchByIdButton);

        // Tạo panel tìm kiếm theo ngày tháng năm
        JPanel searchByDatePanel = new JPanel();
        searchByDatePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchByDateTextField = new JTextField(20);
        searchByDateTextField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchByDateButton = new JButton("Tìm kiếm theo tháng-năm");
        searchByDateButton.setFont(new Font("Arial", Font.PLAIN, 16));
        searchByDatePanel.add(new JLabel("Tìm kiếm theo tháng-năm (yyyy-MM):"));
        searchByDatePanel.add(searchByDateTextField);
        searchByDatePanel.add(searchByDateButton);

        // Panel chứa nút cập nhật
        JPanel buttonPanel = new JPanel();
        updateButton = new JButton("Cập nhật hóa đơn");
        updateButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Chắc chắn nút updateButton đã được khởi tạo
        buttonPanel.add(updateButton);

        // Thêm các phần tử vào actionPanel
        actionPanel.add(searchByIdPanel);
        actionPanel.add(searchByDatePanel);
        actionPanel.add(buttonPanel);

        // Thêm bảng và các nút vào giao diện
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // Tải dữ liệu hóa đơn từ cơ sở dữ liệu và hiển thị trên bảng
        loadHoaDonData();

        // Lắng nghe sự kiện nút tìm kiếm theo mã hóa đơn
        searchByIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchByIdTextField.getText().trim();
                if (!searchText.isEmpty()) {
                    searchHoaDonById(searchText);
                } else {
                    loadHoaDonData();
                }
            }
        });

        // Lắng nghe sự kiện nút tìm kiếm theo ngày tháng năm
        searchByDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchByDateTextField.getText().trim();
                if (!searchText.isEmpty()) {
                    searchHoaDonByDate(searchText);
                } else {
                    loadHoaDonData();
                }
            }
        });

        // Lắng nghe sự kiện nút cập nhật
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = hoaDonTable.getSelectedRow();
                if (selectedRow != -1) {
                    String orderId = hoaDonTable.getValueAt(selectedRow, 0).toString();
                    double totalAmount = Double.parseDouble(JOptionPane.showInputDialog("Nhập tổng tiền mới:"));
                    boolean success = hoaDonDao.updateOrder(orderId, totalAmount);
                    if (success) {
                        hoaDonTable.setValueAt(totalAmount, selectedRow, 3);
                        JOptionPane.showMessageDialog(null, "Cập nhật hóa đơn thành công!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Không thể cập nhật hóa đơn!");
                    }
                }
            }
        });
    }

    // Hàm để tải tất cả hóa đơn từ cơ sở dữ liệu và hiển thị trong bảng
    private void loadHoaDonData() {
        try {
            ResultSet rs = hoaDonDao.getAllOrders();
            tableModel.setRowCount(0); // Xóa tất cả các dòng trước khi hiển thị
            while (rs.next()) {
                String orderId = rs.getString("OrderID");
                String customerId = rs.getString("CustomerID");
                String orderDate = rs.getString("OrderDate");
                double totalAmount = rs.getDouble("TotalAmount");

                Object[] row = {orderId, customerId, orderDate, totalAmount};
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm để tìm kiếm hóa đơn theo mã
    private void searchHoaDonById(String searchText) {
        try {
            ResultSet rs = hoaDonDao.searchOrders(searchText);
            tableModel.setRowCount(0);
            while (rs.next()) {
                String orderId = rs.getString("OrderID");
                String customerId = rs.getString("CustomerID");
                String orderDate = rs.getString("OrderDate");
                double totalAmount = rs.getDouble("TotalAmount");

                Object[] row = {orderId, customerId, orderDate, totalAmount};
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm để tìm kiếm hóa đơn theo ngày tháng năm (yyyy-MM)
    private void searchHoaDonByDate(String searchText) {
        try {
            ResultSet rs = hoaDonDao.searchOrders(searchText);
            tableModel.setRowCount(0);
            while (rs.next()) {
                String orderId = rs.getString("OrderID");
                String customerId = rs.getString("CustomerID");
                String orderDate = rs.getString("OrderDate");
                double totalAmount = rs.getDouble("TotalAmount");

                Object[] row = {orderId, customerId, orderDate, totalAmount};
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
