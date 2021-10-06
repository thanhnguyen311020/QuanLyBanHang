/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.Model;

/**
 *
 * @author ADMIN
 */
public class DangNhap {
    private String MaNV;
    private String MatKhau;
    private boolean VaiTro;

    public DangNhap() {
    }

    public DangNhap(String MaNV, String MatKhau, boolean VaiTro) {
        this.MaNV = MaNV;
        this.MatKhau = MatKhau;
        this.VaiTro = VaiTro;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String MatKhau) {
        this.MatKhau = MatKhau;
    }

    public boolean isVaiTro() {
        return VaiTro;
    }

    public void setVaiTro(boolean VaiTro) {
        this.VaiTro = VaiTro;
    }
    
}
