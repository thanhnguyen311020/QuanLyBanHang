/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.DAO;

import com.fpoly.quanlybanhang.Model.NhanVien;
import com.fpoly.quanlybanhang.Model.SanPham;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class SanPhamDAO {

    public void insert_SanPham(SanPham sp) {
        String sql = "insert into SanPham values(?,?,?,?,?,?,?,?)";
        jdbcHelper.executeUpdate(sql,
                sp.getMaSP(),
                sp.getTenSP(),
                sp.getSoLuong(),
                sp.getGiaNhap(),
                sp.getGiaBan(),
                sp.getAnh(),
                sp.getNghiChu(),
                sp.getMaLoai()
        );
    }

    public void update_SanPham(SanPham sp) {
        String sql = "update SanPham set TenSP=? ,SoLuong=?,GiaNhap=?,GiaBan=?, "
                + " Anh=? , GhiChu=? ,LoaiSP=? where MaSP=?";
        jdbcHelper.executeUpdate(sql,
                sp.getTenSP(),
                sp.getSoLuong(),
                sp.getGiaNhap(),
                sp.getGiaBan(),
                sp.getAnh(),
                sp.getNghiChu(),
                sp.getMaLoai(),
                sp.getMaSP()
        );

    }
    
    public void delete_SanPham(String id){
        String sql = "Delete SanPham where MaSP=?";
        jdbcHelper.executeUpdate(sql, id);
    }
    
    public List<SanPham> select(){
       String sql = "select * from SanPham";
       return select(sql);
    }
    
    public List<SanPham> selectID(String id){
        String sql = "select * from SanPham where LoaiSP=?";
        return select(sql,id);
    }
    
    public SanPham findByID(String id){
        String sql ="select * from SanPham where MaSP=?";
        List<SanPham> list = select(sql,id);
        return list.size()>0?list.get(0):null;
    }
    
    
    private List<SanPham> select(String sql,Object...args){
        List<SanPham> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try{
                rs= jdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    SanPham sp = readFromResultSet(rs);
                    list.add(sp);
                }
            }finally{
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }return list;
    }
    
    private SanPham readFromResultSet(ResultSet rs) throws SQLException{
       SanPham sp = new SanPham();
       sp.setMaSP(rs.getString(1));
       sp.setTenSP(rs.getString(2));
       sp.setSoLuong(rs.getInt(3));
       sp.setGiaNhap(rs.getFloat(4));
       sp.setGiaBan(rs.getFloat(5));
       sp.setAnh(rs.getString(6));
       sp.setNghiChu(rs.getString(7));
       sp.setMaLoai(rs.getString(8));
       
       return sp;
    }
}
