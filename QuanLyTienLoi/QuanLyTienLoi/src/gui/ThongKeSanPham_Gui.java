package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThongKeSanPham_Gui extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;

    public ThongKeSanPham_Gui() {
        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Số Lượng Bán", "Doanh Thu"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        tableModel.addRow(new Object[]{"SP001", "Áo thun", "100", "15000000"});
        tableModel.addRow(new Object[]{"SP002", "Quần jeans", "50", "17500000"});

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
                JOptionPane.showMessageDialog(null, "Chức năng Thêm Thống Kê Sản Phẩm");
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    JOptionPane.showMessageDialog(null, "Chức năng Sửa Thống Kê Sản Phẩm: " + tableModel.getValueAt(selectedRow, 1));
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một sản phẩm!");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(null, "Xóa thống kê này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn một thống kê!");
                }
            }
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}