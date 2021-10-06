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
public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private String DiaChi;

    public KhachHang() {
    }

    public KhachHang(String maKH, String tenKH, String sdt, String DiaChi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.DiaChi = DiaChi;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
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
    
    @Override
    public String toString(){
        return this.maKH+"-"+this.tenKH;
    }
}
