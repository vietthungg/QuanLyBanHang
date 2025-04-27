package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KhachHang_Gui extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;

    public KhachHang_Gui() {
        // Tạo bảng
        String[] columns = {"Mã KH", "Họ Tên", "Địa Chỉ", "Điện Thoại"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Dữ liệu mẫu
        tableModel.addRow(new Object[]{"KH001", "Nguyễn Văn A", "Hà Nội", "0123456789"});
        tableModel.addRow(new Object[]{"KH002", "Trần Thị B", "TP.HCM", "0987654321"});

        // Tạo panel cho các nút
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Xử lý sự kiện nút
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Chức năng Thêm Khách Hàng");
                // Thêm code để mở form nhập dữ liệu khách hàng
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    JOptionPane.showMessageDialog(null, "Chức năng Sửa Khách Hàng: " + tableModel.getValueAt(selectedRow, 1));
                    // Thêm code để mở form sửa dữ liệu
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một khách hàng!");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một khách hàng!");
                }
            }
        });

        // Bố cục giao diện
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}