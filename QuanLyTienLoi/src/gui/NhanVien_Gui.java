package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NhanVien_Gui extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;

    public NhanVien_Gui() {
        String[] columns = {"Mã NV", "Họ Tên", "Chức Vụ", "Số Điện Thoại"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        tableModel.addRow(new Object[]{"NV001", "Lê Văn C", "Nhân viên bán hàng", "0912345678"});
        tableModel.addRow(new Object[]{"NV002", "Phạm Thị D", "Quản lý", "0978765432"});

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Thêm");
        JButton editButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Chức năng Thêm Nhân Viên");
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    JOptionPane.showMessageDialog(null, "Chức năng Sửa Nhân Viên: " + tableModel.getValueAt(selectedRow, 1));
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhân viên!");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            private JOptionPane JMenuPane;

			@Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JMenuPane.showConfirmDialog(null, "Xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhân viên!");
                }
            }
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}