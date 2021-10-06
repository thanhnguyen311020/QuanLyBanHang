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
public class HoaDonChiTiet {
    private int MaHoaDonCT;
    private String MaHoaDon;
    private String MaSP;
    private int SoLuong;
    private float ThanhTien;
    private String GhiChu;

    public HoaDonChiTiet() {
    }

    public HoaDonChiTiet(int MaHoaDonCT, String MaHoaDon, String MaSP, int SoLuong, float ThanhTien, String GhiChu) {
        this.MaHoaDonCT = MaHoaDonCT;
        this.MaHoaDon = MaHoaDon;
        this.MaSP = MaSP;
        this.SoLuong = SoLuong;
        this.ThanhTien = ThanhTien;
        this.GhiChu = GhiChu;
    }

    public int getMaHoaDonCT() {
        return MaHoaDonCT;
    }

    public void setMaHoaDonCT(int MaHoaDonCT) {
        this.MaHoaDonCT = MaHoaDonCT;
    }

    public String getMaHoaDon() {
        return MaHoaDon;
    }

    public void setMaHoaDon(String MaHoaDon) {
        this.MaHoaDon = MaHoaDon;
    }

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String MaSP) {
        this.MaSP = MaSP;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public float getThanhTien() {
        return ThanhTien;
    }

    public void setThanhTien(float ThanhTien) {
        this.ThanhTien = ThanhTien;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }
    
    
}
