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
public class SanPham {
    private String MaSP;
    private String TenSP;
    private int SoLuong;
    private float GiaNhap;
    private float GiaBan;
    private String Anh;
    private String NghiChu;
    private String MaLoai;
    private LoaiSanPham lsp;

    public SanPham() {
    }

    public SanPham(String MaSP, String TenSP, int SoLuong, float GiaNhap, float GiaBan, String Anh, String NghiChu, String MaLoai, LoaiSanPham lsp) {
        this.MaSP = MaSP;
        this.TenSP = TenSP;
        this.SoLuong = SoLuong;
        this.GiaNhap = GiaNhap;
        this.GiaBan = GiaBan;
        this.Anh = Anh;
        this.NghiChu = NghiChu;
        this.MaLoai = MaLoai;
        this.lsp = lsp;
    }

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String MaSP) {
        this.MaSP = MaSP;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String TenSP) {
        this.TenSP = TenSP;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public float getGiaNhap() {
        return GiaNhap;
    }

    public void setGiaNhap(float GiaNhap) {
        this.GiaNhap = GiaNhap;
    }

    public float getGiaBan() {
        return GiaBan;
    }

    public void setGiaBan(float GiaBan) {
        this.GiaBan = GiaBan;
    }

    public String getAnh() {
        return Anh;
    }

    public void setAnh(String Anh) {
        this.Anh = Anh;
    }

    public String getNghiChu() {
        return NghiChu;
    }

    public void setNghiChu(String NghiChu) {
        this.NghiChu = NghiChu;
    }

    public String getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(String MaLoai) {
        this.MaLoai = MaLoai;
    }

    public LoaiSanPham getLsp() {
        return lsp;
    }

    public void setLsp(LoaiSanPham lsp) {
        this.lsp = lsp;
    }
   
    
    
    @Override
    public String toString(){
        return this.TenSP;
    }
    
    public boolean equals(Object obj){
        SanPham sp = (SanPham)obj;
        return sp.getMaSP().equals(this.getMaSP());
    }
}
