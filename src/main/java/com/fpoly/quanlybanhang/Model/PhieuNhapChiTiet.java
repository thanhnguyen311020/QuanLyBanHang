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
public class PhieuNhapChiTiet {
    private int  PhieuNhapCT;
    private String MaPN;
    private String MaSP;
    private int Soluong;
    private float DonGia;
    private float TongTien;
    private String ChuThich;

    public PhieuNhapChiTiet() {
    }

    public PhieuNhapChiTiet(int PhieuNhapCT, String MaPN, String MaSP, int Soluong, float DonGia, float TongTien, String ChuThich) {
        this.PhieuNhapCT = PhieuNhapCT;
        this.MaPN = MaPN;
        this.MaSP = MaSP;
        this.Soluong = Soluong;
        this.DonGia = DonGia;
        this.TongTien = TongTien;
        this.ChuThich = ChuThich;
    }

    public int getPhieuNhapCT() {
        return PhieuNhapCT;
    }

    public void setPhieuNhapCT(int PhieuNhapCT) {
        this.PhieuNhapCT = PhieuNhapCT;
    }

    public String getMaPN() {
        return MaPN;
    }

    public void setMaPN(String MaPN) {
        this.MaPN = MaPN;
    }

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String MaSP) {
        this.MaSP = MaSP;
    }

    public int getSoluong() {
        return Soluong;
    }

    public void setSoluong(int Soluong) {
        this.Soluong = Soluong;
    }

    public float getDonGia() {
        return DonGia;
    }

    public void setDonGia(float DonGia) {
        this.DonGia = DonGia;
    }

    public float getTongTien() {
        return TongTien;
    }

    public void setTongTien(float TongTien) {
        this.TongTien = TongTien;
    }

    public String getChuThich() {
        return ChuThich;
    }

    public void setChuThich(String ChuThich) {
        this.ChuThich = ChuThich;
    }
    
    
}
