package entity;

public class LoaiSanPham {
	private String maLoaiSanPham;
	private String tenLoaiSanPham;
	public LoaiSanPham(String maLoaiSanPham, String tenLoaiSanPham) {
		super();
		this.maLoaiSanPham = maLoaiSanPham;
		this.tenLoaiSanPham = tenLoaiSanPham;
	}
	public String getMaLoaiSanPham() {
		return maLoaiSanPham;
	}
	public void setMaLoaiSanPham(String maLoaiSanPham) {
		this.maLoaiSanPham = maLoaiSanPham;
	}
	public String getTenLoaiSanPham() {
		return tenLoaiSanPham;
	}
	public void setTenLoaiSanPham(String tenLoaiSanPham) {
		this.tenLoaiSanPham = tenLoaiSanPham;
	}
	
}
