/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.DAO;

import com.fpoly.quanlybanhang.Model.PhieuNhap;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class PhieuNhapDAO {
    
    public void insert_PhieuNhap(PhieuNhap pn){
        String sql = "insert into PhieuNhap values(?,?,?,?,?,?)";
        jdbcHelper.executeUpdate(sql, 
                pn.getMaPN(),
                pn.getMaNV(),
                pn.getNhaPP(),
                pn.getTongtien(),
                pn.getNgayNhap(),
                pn.getGhiChu()
                );
    }
    
    public void update_PhieuNhap(PhieuNhap pn){
        String sql = "update PhieuNhap set MaNV = ?, MaNhaPhanPhoi=?, TongTien=?, NgayNhap=?, NghiChu=? where MaPN=?";
        jdbcHelper.executeUpdate(sql, 
                
                pn.getMaNV(),
                pn.getNhaPP(),
                pn.getTongtien(),
                pn.getNgayNhap(),
                pn.getGhiChu(),
                pn.getMaPN()
                );
    }
    
    public void delete_PhieuNhap(String id){
        String sql = "delete PhieuNhap where MaPN=?";
    }
    
    public List<PhieuNhap> select(){
        String sql = "Select * from PhieuNhap";
        return select(sql);
    }
    
    public PhieuNhap findByID(String id){
        String sql = "select * from PhieuNhap where MaPN = ?";
        List<PhieuNhap> list = select(sql,id);
        return list.size()>0?list.get(0):null;
    }
    
    private List<PhieuNhap> select(String sql, Object...args){
        List<PhieuNhap> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try{
                rs=jdbcHelper.executeQuery(sql, args);
                while (rs.next()) {                    
                    PhieuNhap pn = readFormResultSet(rs);
                    list.add(pn);
                }
            }finally{
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }return list;
    }

    private PhieuNhap readFormResultSet(ResultSet rs) throws SQLException {
        PhieuNhap pn = new PhieuNhap();
        pn.setMaPN(rs.getString(1));
        pn.setMaNV(rs.getString(2));
        pn.setNhaPP(rs.getString(3));
        pn.setTongtien(rs.getFloat(4));
        pn.setNgayNhap(rs.getDate(5));
        pn.setGhiChu(rs.getString(6));
        return pn;
    }
    
    
    
    
}
