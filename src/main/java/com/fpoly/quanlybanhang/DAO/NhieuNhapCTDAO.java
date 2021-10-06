/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.DAO;

import com.fpoly.quanlybanhang.Model.PhieuNhapChiTiet;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class NhieuNhapCTDAO {
    
    public void insert_PhieuCT(PhieuNhapChiTiet ct){
        String sql = " insert into PhieuNhapCT values(?,?,?,?,?,?)";
        jdbcHelper.executeUpdate(sql,
                ct.getMaPN(),
                ct.getMaSP(),
                ct.getSoluong(),
                ct.getDonGia(),
                ct.getTongTien(),
                ct.getChuThich()
                );
    }
    
    public void update_PhieuCT(PhieuNhapChiTiet ct){
        String sql = "update PhieuNhapCT set MaPN=?, MaSP=?, SoLuong =?, DonGia=?,TongTien=?,ChuThich=? where MaCTPN=?";
        jdbcHelper.executeUpdate(sql,
                ct.getMaPN(),
                ct.getMaSP(),
                ct.getSoluong(),
                ct.getDonGia(),
                ct.getTongTien(),
                ct.getChuThich(),
                ct.getPhieuNhapCT()
                );
    }
    
    public void delete_PhieuCT(int id){
        String sql = "delete PhieuNhapCT where MaCTPN=?";
        jdbcHelper.executeUpdate(sql, id);
    }
    
    public List<PhieuNhapChiTiet> select(){
        String sql = "select * from PhieuNhapCT";
        return select(sql);
    }
    public List<PhieuNhapChiTiet> select(String id){
        String sql = "select * from PhieuNhapCT where MaPN = ?";
        return select(sql,id);
    }
    
    public PhieuNhapChiTiet findById(int id){
        String sql = "select * from PhieuNhapCT where MaCTPN=?";
        List<PhieuNhapChiTiet> list = select(sql,id);
        return list.size()>0 ? list.get(0):null;
    }
    
    public List<PhieuNhapChiTiet> select(String id, Object...agrs){
        List<PhieuNhapChiTiet> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try{
                rs = jdbcHelper.executeQuery(id, agrs);
                while (rs.next()) {                    
                    PhieuNhapChiTiet ctpn = readFromResultSet(rs);
                    list.add(ctpn);
                }
            }finally{
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }return list;
    }
    
    private PhieuNhapChiTiet readFromResultSet(ResultSet rs) throws SQLException{
        PhieuNhapChiTiet ct = new PhieuNhapChiTiet();
        ct.setPhieuNhapCT(rs.getInt(1));
        ct.setMaPN(rs.getString(2));
        ct.setMaSP(rs.getString(3));
        ct.setSoluong(rs.getInt(4));
        ct.setDonGia(rs.getFloat(5));
        ct.setTongTien(rs.getFloat(6));
        ct.setChuThich(rs.getString(7));
        return ct;
    }
}
