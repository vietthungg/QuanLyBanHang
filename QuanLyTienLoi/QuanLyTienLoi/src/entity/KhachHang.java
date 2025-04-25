package entity;

public class KhachHang {
	private String maKhachHang;
	private String hoTenKhachHang;
	private String sdt;
	public KhachHang(String maKhachHang, String hoTenKhachHang, String sdt) {
		super();
		this.maKhachHang = maKhachHang;
		this.hoTenKhachHang = hoTenKhachHang;
		this.sdt = sdt;
	}
	public String getMaKhachHang() {
		return maKhachHang;
	}
	public void setMaKhachHang(String maKhachHang) {
		this.maKhachHang = maKhachHang;
	}
	public String getHoTenKhachHang() {
		return hoTenKhachHang;
	}
	public void setHoTenKhachHang(String hoTenKhachHang) {
		this.hoTenKhachHang = hoTenKhachHang;
	}
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	
	
}
