/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.Model;

import com.fpoly.quanlybanhang.helper.DateHelper;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class PhieuNhap {
    private String MaPN;
    private String MaNV;
    private String NhaPP;
    private float tongtien=0;
    private Date NgayNhap=DateHelper.now();
    private String GhiChu;

    public PhieuNhap() {
    }

    public PhieuNhap(String MaPN, String MaNV, String NhaPP, String GhiChu) {
        this.MaPN = MaPN;
        this.MaNV = MaNV;
        this.NhaPP = NhaPP;
        this.GhiChu = GhiChu;
    }

    public String getMaPN() {
        return MaPN;
    }

    public void setMaPN(String MaPN) {
        this.MaPN = MaPN;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public String getNhaPP() {
        return NhaPP;
    }

    public void setNhaPP(String NhaPP) {
        this.NhaPP = NhaPP;
    }

    public float getTongtien() {
        return tongtien;
    }

    public void setTongtien(float tongtien) {
        this.tongtien = tongtien;
    }

    public Date getNgayNhap() {
        return NgayNhap;
    }

    public void setNgayNhap(Date NgayNhap) {
        this.NgayNhap = NgayNhap;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String GhiChu) {
        this.GhiChu = GhiChu;
    }
    
    
}
