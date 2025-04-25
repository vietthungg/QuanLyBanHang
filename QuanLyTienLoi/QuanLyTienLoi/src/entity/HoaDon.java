package entity;

import java.sql.Timestamp;

public class HoaDon {
	private String maDonHang;
	private String maKhachHang;
	private String productName;
    private java.sql.Timestamp ngayLapHoaDon;
    private double tongSoTien;
	public HoaDon(String maDonHang, String maKhachHang, String productName, Timestamp ngayLapHoaDon,
			double tongSoTien) {
		super();
		this.maDonHang = maDonHang;
		this.maKhachHang = maKhachHang;
		this.productName = productName;
		this.ngayLapHoaDon = ngayLapHoaDon;
		this.tongSoTien = tongSoTien;
	}
	public String getMaDonHang() {
		return maDonHang;
	}
	public void setMaDonHang(String maDonHang) {
		this.maDonHang = maDonHang;
	}
	public String getMaKhachHang() {
		return maKhachHang;
	}
	public void setMaKhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public java.sql.Timestamp getNgayLapHoaDon() {
		return ngayLapHoaDon;
	}
	public void setNgayLapHoaDon(java.sql.Timestamp ngayLapHoaDon) {
		this.ngayLapHoaDon = ngayLapHoaDon;
	}
	public double getTongSoTien() {
		return tongSoTien;
	}
	public void setTongSoTien(double tongSoTien) {
		this.tongSoTien = tongSoTien;
	}
    
    
}
