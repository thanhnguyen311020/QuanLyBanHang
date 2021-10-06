/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.Model;

import java.awt.Image;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class NhanVien {
    private String MaNV;
    private String TenNV;
    private String MatKhau;
    private Date NgaySinh;
    private String sdt;
    private String DiaChi;
    private boolean GioiTinh;
    private String hinh;
    private boolean VaiTro;

    public NhanVien() {
    }

    public NhanVien(String MaNV, String TenNV, String MatKhau, Date NgaySinh, String sdt, String DiaChi, boolean GioiTinh, String hinh, boolean VaiTro) {
        this.MaNV = MaNV;
        this.TenNV = TenNV;
        this.MatKhau = MatKhau;
        this.NgaySinh = NgaySinh;
        this.sdt = sdt;
        this.DiaChi = DiaChi;
        this.GioiTinh = GioiTinh;
        this.hinh = hinh;
        this.VaiTro = VaiTro;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public String getTenNV() {
        return TenNV;
    }

    public void setTenNV(String TenNV) {
        this.TenNV = TenNV;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String MatKhau) {
        this.MatKhau = MatKhau;
    }

    public Date getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(Date NgaySinh) {
        this.NgaySinh = NgaySinh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public boolean isGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(boolean GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public boolean isVaiTro() {
        return VaiTro;
    }

    public void setVaiTro(boolean VaiTro) {
        this.VaiTro = VaiTro;
    }
   

  
    
}
