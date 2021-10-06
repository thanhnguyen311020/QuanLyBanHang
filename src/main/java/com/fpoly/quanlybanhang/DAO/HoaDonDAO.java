/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.DAO;

import com.fpoly.quanlybanhang.Model.DonHang;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class HoaDonDAO {
    
    public void insert_HoaDon(DonHang hd){
        String sql = "insert into HoaDon values(?,?,?,?,?,?)";
        jdbcHelper.executeUpdate(sql,
                hd.getMaHD(),
                hd.getMaNV(),
                hd.getNgayLap(),
                hd.getMaKH(),
                hd.getThanhTien(),
                hd.getGhiChu()
                );
        
    }
    
    public void update_HoaDon(DonHang hd){
        String sql = "update HoaDon set MaNV=?, NgayBan=?, MaKhach=?, TongTien=?, GhiChu=? where MaHoaDon=?";
        jdbcHelper.executeUpdate(sql,
                
                hd.getMaNV(),
                hd.getNgayLap(),
                hd.getMaKH(),
                hd.getThanhTien(),
                hd.getGhiChu(),
                hd.getMaHD()
                );
    }
    
    public void delete_HoaDon(String id){
        String sql = "delete HoaDon where MaHoaDon=?";
        jdbcHelper.executeUpdate(sql,id
                );
    }
    
    public List<DonHang> selectByDate(Date date){
        String sql = "select * from HoaDon where NgayBan =?";        
        return select(sql,date);
    }
    
    public List<DonHang> select(){
        String sql = "select * from HoaDon";
        return select(sql);
    }
    
    public DonHang findById(String id){
        String sql = "select * from HoaDon where MaHoaDon=?";
        List<DonHang> list = select(sql,id);
        return list.size()>0?list.get(0):null;
    }
    
    public List<DonHang> select(String sql,Object...args){
        List<DonHang> list = new ArrayList<>();
        try {
            ResultSet rs= null;
            try{
                rs=jdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    DonHang hd = readFromResultSet(rs);
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
    
    private DonHang readFromResultSet(ResultSet rs) throws SQLException{
        DonHang hd = new DonHang();
        hd.setMaHD(rs.getString(1));
        hd.setMaNV(rs.getString(2));
        hd.setNgayLap(rs.getDate(3));
        hd.setMaKH(rs.getString(4));
        hd.setThanhTien(rs.getFloat(5));
        hd.setGhiChu(rs.getString(6));
        return hd;
    }
}
