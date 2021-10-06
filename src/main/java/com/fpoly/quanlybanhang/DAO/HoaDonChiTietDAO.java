/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.DAO;

import com.fpoly.quanlybanhang.Model.HoaDonChiTiet;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class HoaDonChiTietDAO {
    
    public void insert_HoaDonChiTiet(HoaDonChiTiet hdct){
        String sql = "insert into HoaDonChiTiet values(?,?,?,?,?)";
        jdbcHelper.executeUpdate(sql, 
                hdct.getMaHoaDon(),
                hdct.getMaSP(),
              
                hdct.getSoLuong(),
                hdct.getThanhTien(),
                hdct.getGhiChu()
                );
        
    }
    
    public void update_HoaDonChiTiet(HoaDonChiTiet hdct){
        String sql = "update HoaDonChiTiet set MaHoaDon=?, MaSP =?, SoLuong=?, TongTien=?,GhiChu=? where MaHoaDonCT=?";
             jdbcHelper.executeUpdate(sql, 
                hdct.getMaHoaDon(),
                hdct.getMaSP(),
                hdct.getSoLuong(),
                hdct.getThanhTien(),
                hdct.getGhiChu(),
                hdct.getMaHoaDonCT()
                );
     
    }
    
    
    public void delete_HoaDonCT(int id){
        String sql = "delete HoaDonChiTiet where MaHoaDonCT=?";
        jdbcHelper.executeUpdate(sql, id);
        
    }
    
    public List<HoaDonChiTiet> select(){
        String sql = "select * from HoaDonChiTiet";
        return select(sql);
    }
    
    public List<HoaDonChiTiet> select(String id){
        String sql ="select * from HoaDonChiTiet where MaHoaDon = ?";
        return select(sql,id);
    }
    
    public HoaDonChiTiet findByID(int id){
        
        String sql = "Select * from HoaDonChiTiet where MaHoaDonCT=?";
        List<HoaDonChiTiet> list = select(sql,id);
        return list.size()>0 ? list.get(0):null;
    }
    
    
    
    private List<HoaDonChiTiet> select(String sql, Object...args){
        List<HoaDonChiTiet> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try{
                rs = jdbcHelper.executeQuery(sql, args);
                while (rs.next()) {                    
                    HoaDonChiTiet hd = readFromResultSet(rs);
                    list.add(hd);
                }
            }finally{
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }return list;
    }
    
    private HoaDonChiTiet readFromResultSet(ResultSet rs ) throws SQLException{
        HoaDonChiTiet hdct = new HoaDonChiTiet();
        
        hdct.setMaHoaDonCT(rs.getInt(1));
        hdct.setMaHoaDon(rs.getString(2));
        hdct.setMaSP(rs.getString(3));
        hdct.setSoLuong(rs.getInt(4));
        hdct.setThanhTien(rs.getFloat(5));
        hdct.setGhiChu(rs.getString(6));
        return hdct;
    }
    
}
