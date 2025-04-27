package gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main_Gui extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Main_Gui() {

        JMenuBar menuBar = new JMenuBar();

//        JMenu banHangMenu = new JMenu("quản lý hoá đơn");
//        JMenu nhanVienMenu = new JMenu("Quản Lý Nhân Viên");
        JMenu hoaDonMenu = new JMenu("Bán Hàng");
        JMenu sanPhamMenu = new JMenu("Quản Lý Sản Phẩm");
        JMenu thongKeMenu = new JMenu("Quản Lý Thống Kê");

        JMenuItem khachHangItem = new JMenuItem("Quản lý hoá đơn");
        JMenuItem nhanVienItem = new JMenuItem("Quản Lý Nhân Viên");
        JMenuItem sanPhamItem = new JMenuItem("quản lý sản phẩm");
        JMenuItem hoaDonItem = new JMenuItem("Lập hoá đơn");
        JMenuItem thoatItem = new JMenuItem("Thoát");
        JMenuItem thongKeOption1 = new JMenuItem("Doanh Thu");
        JMenuItem thongKeOption2 = new JMenuItem("Sản Phẩm Bán Chạy");

//        banHangMenu.add(khachHangItem);
//        banHangMenu.addSeparator();
//        banHangMenu.add(thoatItem);

//        nhanVienMenu.add(nhanVienItem);
        hoaDonMenu.add(hoaDonItem);
        sanPhamMenu.add(sanPhamItem);

        thongKeMenu.add(thongKeOption1);
        thongKeMenu.addSeparator();
        thongKeMenu.add(thongKeOption2);

//        menuBar.add(banHangMenu);
//        menuBar.add(nhanVienMenu);
        menuBar.add(hoaDonMenu);
        menuBar.add(sanPhamMenu);
        menuBar.add(thongKeMenu);

        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Các panel của bạn
        JPanel khachHangPanel = new KhachHang_Gui();
        JPanel nhanVienPanel = new NhanVien_Gui();
        JPanel sanPhamPanel = new SanPham_Gui();
        JPanel hoaDonPanel = new HoaDon_Gui();  // Giữ lại panel HoaDon (Bán Hàng)
        JPanel thongKeDoanhThuPanel = new ThongKeDoanhThu_Gui();
        JPanel thongKeSanPhamPanel = new ThongKeSanPham_Gui();
        JPanel quanLyHoaDonPanel = new QuanLyHoaDon_Gui();  // Thêm QuanLyHoaDon_Gui vào

        // Thêm các panel vào mainPanel với tên khác nhau
        mainPanel.add(khachHangPanel, "KhachHang");
        mainPanel.add(nhanVienPanel, "NhanVien");
        mainPanel.add(sanPhamPanel, "SanPham");
        mainPanel.add(hoaDonPanel, "HoaDon");  // Panel Bán Hàng (HoaDon) vẫn giữ nguyên
        mainPanel.add(thongKeDoanhThuPanel, "ThongKeDoanhThu");
        mainPanel.add(thongKeSanPhamPanel, "ThongKeSanPham");
        mainPanel.add(quanLyHoaDonPanel, "QuanLyHoaDon");  // Chỉ thêm QuanLyHoaDon vào

        add(mainPanel);

        // Hành động khi chọn các mục menu
        khachHangItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "KhachHang");
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        nhanVienItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "NhanVien");
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        sanPhamItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "SanPham");
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        hoaDonItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "HoaDon");  // Lập hóa đơn, vẫn giữ nguyên
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        // Hành động cho "Quản lý hoá đơn"
        JMenuItem quanLyHoaDonItem = new JMenuItem("Quản Lý Hóa Đơn");
        hoaDonMenu.add(quanLyHoaDonItem);
        quanLyHoaDonItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "QuanLyHoaDon");  // Chuyển tới giao diện quản lý hóa đơn
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        thoatItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        thongKeOption1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "ThongKeDoanhThu");
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        thongKeOption2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "ThongKeSanPham");
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });
        
        // Hiển thị KhachHang panel mặc định
        cardLayout.show(mainPanel, "HoaDon");
        setTitle("Quản Lý Bán Hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        
        
    }

    public static void main(String[] args) {
        Main_Gui frame = new Main_Gui();
        frame.setVisible(true);
    }
}