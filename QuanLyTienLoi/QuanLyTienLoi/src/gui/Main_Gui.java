package gui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main_Gui extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Main_Gui() {


        JMenuBar menuBar = new JMenuBar();

        JMenu banHangMenu = new JMenu("Bán Hàng");
        JMenu nhanVienMenu = new JMenu("Quản Lý Nhân Viên");
        JMenu hoaDonMenu = new JMenu("Quản Lý Hóa Đơn");
        JMenu sanPhamMenu = new JMenu("Quản Lý Sản Phẩm");
        JMenu thongKeMenu = new JMenu("Quản Lý Thống Kê");

        JMenuItem khachHangItem = new JMenuItem("Quản Lý Khách Hàng");
        JMenuItem nhanVienItem = new JMenuItem("Quản Lý Nhân Viên");
        JMenuItem sanPhamItem = new JMenuItem("Quản Lý Sản Phẩm");
        JMenuItem hoaDonItem = new JMenuItem("Quản Lý Hóa Đơn");
        JMenuItem thoatItem = new JMenuItem("Thoát");
        JMenuItem thongKeOption1 = new JMenuItem("Doanh Thu");
        JMenuItem thongKeOption2 = new JMenuItem("Sản Phẩm Bán Chạy");

        banHangMenu.add(khachHangItem);
        banHangMenu.addSeparator();
        banHangMenu.add(thoatItem);

        nhanVienMenu.add(nhanVienItem);
        hoaDonMenu.add(hoaDonItem);
        sanPhamMenu.add(sanPhamItem);

        thongKeMenu.add(thongKeOption1);
        thongKeMenu.addSeparator();
        thongKeMenu.add(thongKeOption2);

        menuBar.add(banHangMenu);
        menuBar.add(nhanVienMenu);
        menuBar.add(hoaDonMenu);
        menuBar.add(sanPhamMenu);
        menuBar.add(thongKeMenu);

        setJMenuBar(menuBar);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel khachHangPanel = new KhachHang_Gui();
        JPanel nhanVienPanel = new NhanVien_Gui();
        JPanel sanPhamPanel = new SanPham_Gui();
        JPanel hoaDonPanel = new HoaDon_Gui();
        JPanel thongKeDoanhThuPanel = new ThongKeDoanhThu_Gui();
        JPanel thongKeSanPhamPanel = new ThongKeSanPham_Gui();

        mainPanel.add(khachHangPanel, "KhachHang");
        mainPanel.add(nhanVienPanel, "NhanVien");
        mainPanel.add(sanPhamPanel, "SanPham");
        mainPanel.add(hoaDonPanel, "HoaDon");
        mainPanel.add(thongKeDoanhThuPanel, "ThongKeDoanhThu");
        mainPanel.add(thongKeSanPhamPanel, "ThongKeSanPham");

        add(mainPanel);

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
                cardLayout.show(mainPanel, "HoaDon");
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
        cardLayout.show(mainPanel, "KhachHang");
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