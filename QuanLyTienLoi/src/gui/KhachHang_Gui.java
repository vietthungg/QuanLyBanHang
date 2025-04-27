package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class KhachHang_Gui extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField txtMaKH, txtTenKH, txtSDT;
    private JButton addButton, editButton, deleteButton, clearButton;
    private Connection conn;
    private boolean isEditing = false;

    public KhachHang_Gui() {
        connectDB();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(new JLabel("Mã Khách Hàng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMaKH = new JTextField(15);
        txtMaKH.setEditable(false);
        txtMaKH.setBackground(new Color(230, 230, 230));
        inputPanel.add(txtMaKH, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        inputPanel.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTenKH = new JTextField(15);
        inputPanel.add(txtTenKH, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        inputPanel.add(new JLabel("Số Điện Thoại:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSDT = new JTextField(15);
        inputPanel.add(txtSDT, gbc);

        String[] columns = {"Mã KH", "Họ Tên", "Điện Thoại"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        addButton = new JButton("Thêm");
        editButton = new JButton("Sửa");
        deleteButton = new JButton("Xóa");
        clearButton = new JButton("Làm mới");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadDataFromDatabase();
        resetInputFieldsAndPrepareForAdd();

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    populateFieldsFromSelectedRow();
                    prepareForEdit();
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isEditing) return;

                String maKH = txtMaKH.getText();
                String ten = txtTenKH.getText().trim();
                String sdt = txtSDT.getText().trim();

                if (!validateInput(ten, sdt)) {
                    return;
                }
                 if (!validatePhoneNumberUniqueness(sdt, null)) {
                    return;
                }


                try {
                    String sql = "INSERT INTO Customers (CustomerID, FullName, Phone) VALUES (?, ?, ?)";
                    try (PreparedStatement pst = conn.prepareStatement(sql)) {
                        pst.setString(1, maKH);
                        pst.setString(2, ten);
                        pst.setString(3, sdt);
                        int result = pst.executeUpdate();

                        if (result > 0) {
                            tableModel.addRow(new Object[]{maKH, ten, sdt});
                            JOptionPane.showMessageDialog(KhachHang_Gui.this, "Thêm khách hàng thành công!");
                            resetInputFieldsAndPrepareForAdd();
                        } else {
                            JOptionPane.showMessageDialog(KhachHang_Gui.this, "Không thể thêm khách hàng vào CSDL.", "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    handleSQLException(ex, "Lỗi khi thêm khách hàng");
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isEditing) return;

                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                     JOptionPane.showMessageDialog(KhachHang_Gui.this, "Không có hàng nào được chọn để sửa.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                     resetInputFieldsAndPrepareForAdd();
                     return;
                }

                String maKH = txtMaKH.getText();
                String ten = txtTenKH.getText().trim();
                String sdt = txtSDT.getText().trim();
                String originalMaKH = tableModel.getValueAt(selectedRow, 0).toString();

                 if (!maKH.equals(originalMaKH)) {
                     JOptionPane.showMessageDialog(KhachHang_Gui.this, "Mã khách hàng không khớp với hàng đang chọn!", "Lỗi Dữ Liệu", JOptionPane.ERROR_MESSAGE);
                     populateFieldsFromSelectedRow();
                     return;
                 }

                if (!validateInput(ten, sdt)) {
                    return;
                }
                if (!validatePhoneNumberUniqueness(sdt, maKH)) {
                    return;
                }


                try {
                    String sql = "UPDATE Customers SET FullName = ?, Phone = ? WHERE CustomerID = ?";
                    try (PreparedStatement pst = conn.prepareStatement(sql)) {
                        pst.setString(1, ten);
                        pst.setString(2, sdt);
                        pst.setString(3, maKH);

                        int result = pst.executeUpdate();

                        if (result > 0) {
                            tableModel.setValueAt(ten, selectedRow, 1);
                            tableModel.setValueAt(sdt, selectedRow, 2);
                            JOptionPane.showMessageDialog(KhachHang_Gui.this, "Cập nhật khách hàng thành công!");
                            resetInputFieldsAndPrepareForAdd();
                        } else {
                            JOptionPane.showMessageDialog(KhachHang_Gui.this, "Không tìm thấy khách hàng để cập nhật hoặc dữ liệu không thay đổi.", "Lỗi CSDL", JOptionPane.WARNING_MESSAGE);
                             resetInputFieldsAndPrepareForAdd();
                        }
                    }
                } catch (SQLException ex) {
                    handleSQLException(ex, "Lỗi khi cập nhật khách hàng");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String maKHToDelete = tableModel.getValueAt(selectedRow, 0).toString();
                    String tenKHToDelete = tableModel.getValueAt(selectedRow, 1).toString();

                    int confirm = JOptionPane.showConfirmDialog(KhachHang_Gui.this,
                            "Bạn có chắc chắn muốn xóa khách hàng '" + tenKHToDelete + "' (Mã: " + maKHToDelete + ") không?",
                            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            String sql = "DELETE FROM Customers WHERE CustomerID = ?";
                             try (PreparedStatement pst = conn.prepareStatement(sql)) {
                                pst.setString(1, maKHToDelete);
                                int result = pst.executeUpdate();

                                if (result > 0) {
                                    tableModel.removeRow(selectedRow);
                                    JOptionPane.showMessageDialog(KhachHang_Gui.this, "Xóa khách hàng thành công!");
                                     if(maKHToDelete.equals(txtMaKH.getText())) {
                                         resetInputFieldsAndPrepareForAdd();
                                     }
                                } else {
                                    JOptionPane.showMessageDialog(KhachHang_Gui.this, "Không tìm thấy khách hàng trong CSDL để xóa.", "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } catch (SQLException ex) {
                           handleSQLException(ex, "Lỗi khi xóa khách hàng");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(KhachHang_Gui.this, "Vui lòng chọn một khách hàng để xóa!", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetInputFieldsAndPrepareForAdd();
            }
        });
    }

    private void connectDB() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databasename=SalesManagement;encrypt=true;trustServerCertificate=true;integratedSecurity=false;";
            String user = "sa";
            String pass = "123";
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Không thể kết nối CSDL. Chức năng sẽ bị hạn chế.\nLỗi: " + e.getMessage(),
                    "Lỗi Kết Nối CSDL", JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
        }
    }

     private void loadDataFromDatabase() {
        if (conn == null) {
             tableModel.setRowCount(0);
             tableModel.addRow(new Object[]{"N/A", "Lỗi kết nối CSDL", ""});
            return;
        }
        tableModel.setRowCount(0);
        String sql = "SELECT CustomerID, FullName, Phone FROM Customers ORDER BY CustomerID";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("CustomerID"),
                        rs.getString("FullName"),
                        rs.getString("Phone")
                });
            }
        } catch (SQLException e) {
            handleSQLException(e, "Lỗi khi tải dữ liệu khách hàng");
        }
    }

     private String sinhMaKhachHangTuDong() {
        String prefix = "CU";
        int nextNumber = 1;
        if (conn == null) return prefix + "001";

        String sql = "SELECT MAX(CustomerID) AS MaxID FROM Customers WHERE CustomerID LIKE ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
             pst.setString(1, prefix + "%");
             ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String maxId = rs.getString("MaxID");
                if (maxId != null) {
                    try {
                        String numPart = maxId.substring(prefix.length());
                        if(!numPart.isEmpty()){
                            nextNumber = Integer.parseInt(numPart) + 1;
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.err.println("Error parsing CustomerID number: " + maxId + ". Resetting sequence. Error: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "Lỗi khi sinh mã khách hàng");
            return prefix + "ERR";
        }
        return prefix + String.format("%03d", nextNumber);
    }

    private void populateFieldsFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            txtMaKH.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenKH.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtSDT.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
        }
    }

    private void resetInputFieldsAndPrepareForAdd() {
        table.clearSelection();
        txtTenKH.setText("");
        txtSDT.setText("");
        if (conn != null) {
            txtMaKH.setText(sinhMaKhachHangTuDong());
        } else {
             txtMaKH.setText("N/A");
        }
        txtTenKH.requestFocus();
        isEditing = false;
        addButton.setEnabled(true);
        editButton.setEnabled(false);
    }

    private void prepareForEdit() {
         isEditing = true;
         addButton.setEnabled(false);
         editButton.setEnabled(true);
    }

    private boolean validateInput(String ten, String sdt) {
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ Tên không được để trống.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            txtTenKH.requestFocus();
            return false;
        }
        if (sdt.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Số Điện Thoại không được để trống.", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }
        if (!sdt.matches("^0\\d{9,10}$")) {
             JOptionPane.showMessageDialog(this, "Số Điện Thoại không hợp lệ (phải bắt đầu bằng 0, tổng cộng 10-11 chữ số).", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
             txtSDT.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePhoneNumberUniqueness(String sdt, String currentCustomerID) {
         if (conn == null) {
              JOptionPane.showMessageDialog(this, "Không thể kiểm tra Số Điện Thoại do lỗi kết nối CSDL.", "Lỗi Kết Nối", JOptionPane.ERROR_MESSAGE);
             return false;
         }
        String sql = "SELECT CustomerID FROM Customers WHERE Phone = ?";
        if (currentCustomerID != null) {
            sql += " AND CustomerID != ?";
        }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, sdt);
             if (currentCustomerID != null) {
                 pst.setString(2, currentCustomerID);
             }
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                 String existingCustomerID = rs.getString("CustomerID");
                JOptionPane.showMessageDialog(this, "Số Điện Thoại này đã được sử dụng cho khách hàng khác (Mã: " + existingCustomerID + ").", "Trùng Số Điện Thoại", JOptionPane.WARNING_MESSAGE);
                txtSDT.requestFocus();
                return false;
            }
        } catch (SQLException e) {
             handleSQLException(e, "Lỗi khi kiểm tra trùng số điện thoại");
             return false;
        }
        return true;
    }


    private void handleSQLException(SQLException ex, String contextMessage) {
        ex.printStackTrace();
        String userMessage = contextMessage + ":\n";
        if (ex.getMessage().toLowerCase().contains("constraint")) {
            userMessage += "Thao tác vi phạm ràng buộc dữ liệu (ví dụ: xóa khách hàng có đơn hàng, hoặc trùng khóa chính/duy nhất).";
        } else if (ex.getMessage().toLowerCase().contains("connection") || ex.getMessage().toLowerCase().contains("network")) {
             userMessage += "Lỗi kết nối đến cơ sở dữ liệu.";
        } else {
            userMessage += ex.getMessage();
        }
        JOptionPane.showMessageDialog(this, userMessage, "Lỗi SQL", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't set system look and feel.");
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Khách Hàng");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new KhachHang_Gui());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
