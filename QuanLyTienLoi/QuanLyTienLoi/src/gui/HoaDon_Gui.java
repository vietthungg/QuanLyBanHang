package gui;

import dao.HoaDonPDFCreator;
import dao.KhachHang_Dao;
import dao.SanPhamDao;
import entity.KhachHang;
import entity.SanPham;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class HoaDon_Gui extends JPanel {
    private static int soHoaDon = 135;

    private JLabel lblMaHoaDon;
    private JLabel lblTongTien;
    private JLabel lblVAT;
    private JLabel lblTenKH;
    private JTextField txtSDTKH;
    private JTextField txtMaSP;
    private JTextField txtTenSP;
    private JComboBox<String> cbSize;

    private DefaultTableModel productModel;
    private DefaultTableModel invoiceModel;

    private SanPhamDao spDAO = new SanPhamDao();
    private KhachHang_Dao khDAO = new KhachHang_Dao();

    public HoaDon_Gui() {
        setLayout(new BorderLayout(10, 10));

        // --- Panel tìm kiếm ---
        JPanel searchPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm sản phẩm"));

        txtMaSP = new JTextField();
        txtTenSP = new JTextField();
        cbSize = new JComboBox<>(new String[]{"Tất cả", "NUOC_UONG", "DO_AN", "DO_DUNG"});
        JButton btnRefresh = new JButton("Làm mới");

        searchPanel.add(new JLabel("Mã sản phẩm"));
        searchPanel.add(txtMaSP);
        searchPanel.add(new JLabel("Tên sản phẩm"));
        searchPanel.add(txtTenSP);
        searchPanel.add(new JLabel("Loại sản phẩm"));
        searchPanel.add(cbSize);
        searchPanel.add(new JLabel(""));
        searchPanel.add(btnRefresh);

        // --- Bảng sản phẩm ---
        String[] productCols = {"Mã SP", "Tên SP", "Loại", "Giá", "Tồn kho"};
        productModel = new DefaultTableModel(productCols, 0);
        JTable productTable = new JTable(productModel);
        JScrollPane productScroll = new JScrollPane(productTable);

        // --- Bảng hóa đơn ---
        String[] invoiceCols = {"Mã SP", "Tên SP", "Loại", "Giá", "Số lượng", "Thành tiền"};
        invoiceModel = new DefaultTableModel(invoiceCols, 0);
        JTable invoiceTable = new JTable(invoiceModel);
        JScrollPane invoiceScroll = new JScrollPane(invoiceTable);

        // --- Thông tin hóa đơn ---
        JPanel invoiceInfo = new JPanel(new GridLayout(3, 3, 10, 10));
        lblMaHoaDon = new JLabel("Mã hóa đơn: HD" + String.format("%06d", soHoaDon));
        lblTenKH = new JLabel();
        txtSDTKH = new JTextField();

        invoiceInfo.add(lblMaHoaDon);
        invoiceInfo.add(new JLabel("Ngày lập: " + LocalDate.now()));
        invoiceInfo.add(new JLabel(""));

        invoiceInfo.add(new JLabel("SĐT Khách Hàng:"));
        invoiceInfo.add(txtSDTKH);
        JButton btnTimKH = new JButton("🔍");
        invoiceInfo.add(btnTimKH);

        invoiceInfo.add(new JLabel("Tên Khách Hàng:"));
        invoiceInfo.add(lblTenKH);
        JButton btnThemKH = new JButton("➕ Thêm KH");
        invoiceInfo.add(btnThemKH);

        // --- Panel tổng cộng ---
        JPanel totalPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        lblTongTien = new JLabel("0 VND");
        lblVAT = new JLabel("0 VND");

        totalPanel.add(new JLabel("Tổng tiền:"));
        totalPanel.add(lblTongTien);
        totalPanel.add(new JLabel("Tổng cộng (VAT 10%):"));
        totalPanel.add(lblVAT);
        totalPanel.add(new JLabel("Tiền nhận:"));
        totalPanel.add(new JTextField());

        // --- Panel hóa đơn chính ---
        JPanel invoicePanel = new JPanel(new BorderLayout(10, 10));
        invoicePanel.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
        invoicePanel.add(invoiceInfo, BorderLayout.NORTH);
        invoicePanel.add(invoiceScroll, BorderLayout.CENTER);
        invoicePanel.add(totalPanel, BorderLayout.SOUTH);

        // --- Nút điều khiển ---
        JButton btnAddToInvoice = new JButton(">>");
        JTextField txtSoLuong = new JTextField("1", 5);
        txtSoLuong.setMaximumSize(new Dimension(50, 25));

        JPanel centerControlPanel = new JPanel();
        centerControlPanel.setLayout(new BoxLayout(centerControlPanel, BoxLayout.Y_AXIS));
        centerControlPanel.add(Box.createVerticalGlue());
        centerControlPanel.add(btnAddToInvoice);
        centerControlPanel.add(Box.createVerticalStrut(10));
        centerControlPanel.add(txtSoLuong);
        centerControlPanel.add(Box.createVerticalGlue());

        // --- Layout trái phải ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.add(productScroll);
        leftPanel.add(Box.createHorizontalStrut(10));
        leftPanel.add(centerControlPanel);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(leftPanel, BorderLayout.WEST);
        centerPanel.add(invoicePanel, BorderLayout.CENTER);

        // --- Các nút chức năng ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnHuyHD = new JButton("Hủy HĐ");
        JButton btnXuatHD = new JButton("Xuất hóa đơn");
        JButton btnThanhToan = new JButton("Thanh toán");

        buttonPanel.add(btnHuyHD);
        buttonPanel.add(btnXuatHD);
        buttonPanel.add(btnThanhToan);

        // --- Add vào Frame ---
        add(searchPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadSanPhamTable();

        // ================== Các sự kiện ===================

        btnRefresh.addActionListener(e -> loadSanPhamTable());

        btnAddToInvoice.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm!");
                return;
            }
            try {
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
                if (soLuong <= 0) throw new NumberFormatException();

                String ma = productModel.getValueAt(selectedRow, 0).toString();
                String ten = productModel.getValueAt(selectedRow, 1).toString();
                String loai = productModel.getValueAt(selectedRow, 2).toString();
                int gia = Integer.parseInt(productModel.getValueAt(selectedRow, 3).toString().replaceAll("[^\\d]", ""));
                int thanhTien = gia * soLuong;

                invoiceModel.addRow(new Object[]{ma, ten, loai, gia + " VND", soLuong, thanhTien + " VND"});
                capNhatTongTien();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Số lượng không hợp lệ!");
            }
        });

        btnTimKH.addActionListener(e -> {
            String sdt = txtSDTKH.getText().trim();
            if (sdt.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập SĐT!");
                return;
            }
            KhachHang kh = khDAO.timKhachHangBangSDT(sdt);
            if (kh != null) {
                lblTenKH.setText(kh.getHoTenKhachHang());
            } else {
                lblTenKH.setText("Không tìm thấy");
            }
        });

        btnThemKH.addActionListener(e -> {
            new ThemKhachHang_Gui((JFrame) SwingUtilities.getWindowAncestor(this)).setVisible(true);
        });

        btnHuyHD.addActionListener(e -> {
            invoiceModel.setRowCount(0);
            capNhatTongTien();
        });

        btnXuatHD.addActionListener(e -> {
            try {
                String sdt = txtSDTKH.getText().trim();
                if (sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại khách hàng!");
                    return;
                }
                KhachHang kh = khDAO.timKhachHangBangSDT(sdt);
                if (kh == null) {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng!");
                    return;
                }
                double tongTien = Double.parseDouble(lblVAT.getText().replaceAll("[^\\d]", ""));
                HoaDonPDFCreator.xuatHoaDon(kh.getMaKhachHang(), kh.getHoTenKhachHang(), invoiceModel, tongTien, soHoaDon);

                // Reset sau khi xuất
                invoiceModel.setRowCount(0);
                lblTongTien.setText("0 VND");
                lblVAT.setText("0 VND");
                soHoaDon++;
                lblMaHoaDon.setText("Mã hóa đơn: HD" + String.format("%06d", soHoaDon));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnThanhToan.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Thanh toán thành công!");
            invoiceModel.setRowCount(0);
            capNhatTongTien();
            soHoaDon++;
            lblMaHoaDon.setText("Mã hóa đơn: HD" + String.format("%06d", soHoaDon));
        });
    }

    private void loadSanPhamTable() {
        productModel.setRowCount(0);
        String loaiSP = cbSize.getSelectedItem().toString();
        List<SanPham> list = spDAO.searchSanPham(txtMaSP.getText(), txtTenSP.getText(), loaiSP);
        for (SanPham sp : list) {
            productModel.addRow(new Object[]{
                    sp.getMaSanPham(), sp.getTenSanPham(), sp.getLoai(), sp.getGia(), sp.getSoLuongTon()
            });
        }
    }

    private void capNhatTongTien() {
        int tong = 0;
        for (int i = 0; i < invoiceModel.getRowCount(); i++) {
            String val = invoiceModel.getValueAt(i, 5).toString().replaceAll("[^\\d]", "");
            tong += Integer.parseInt(val);
        }
        lblTongTien.setText(tong + " VND");
        lblVAT.setText((tong + tong / 10) + " VND");
    }
}
