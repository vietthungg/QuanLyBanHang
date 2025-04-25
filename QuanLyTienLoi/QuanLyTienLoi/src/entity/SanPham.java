package entity;

public class SanPham {
	private String maSanPham;
	private String tenSanPham;
	private String loai;
	private double gia;
	private int soLuongTon;
	public SanPham(String maSanPham, String tenSanPham, String loai, double gia, int soLuongTon) {
		super();
		this.maSanPham = maSanPham;
		this.tenSanPham = tenSanPham;
		this.loai = loai;
		this.gia = gia;
		this.soLuongTon = soLuongTon;
	}
	public String getMaSanPham() {
		return maSanPham;
	}
	public void setMaSanPham(String maSanPham) {
		this.maSanPham = maSanPham;
	}
	public String getTenSanPham() {
		return tenSanPham;
	}
	public void setTenSanPham(String tenSanPham) {
		this.tenSanPham = tenSanPham;
	}
	public String getLoai() {
		return loai;
	}
	public void setLoai(String loai) {
		this.loai = loai;
	}
	public double getGia() {
		return gia;
	}
	public void setGia(double gia) {
		this.gia = gia;
	}
	public int getSoLuongTon() {
		return soLuongTon;
	}
	public void setSoLuongTon(int soLuongTon) {
		this.soLuongTon = soLuongTon;
	}
	
	
	
}
