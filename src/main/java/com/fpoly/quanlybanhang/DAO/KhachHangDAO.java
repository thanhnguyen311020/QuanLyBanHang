/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.DAO;

import com.fpoly.quanlybanhang.Model.KhachHang;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class KhachHangDAO {
    
    public void insert_KhachHang(KhachHang kh){
        String sql = "insert into KhachHang values(?,?,?,?)";
        jdbcHelper.executeUpdate(sql, 
                kh.getMaKH(),
                kh.getTenKH(),
                kh.getSdt(),
                kh.getDiaChi()
                );
    }
    
    public void update_KhachHang(KhachHang kh){
        String sql = "update KhachHang set TenKhach=?, sdt=?, DiaChi=? where maKhach=?";
        jdbcHelper.executeUpdate(sql,
                kh.getTenKH(),
                kh.getSdt(),
                kh.getDiaChi(),
                kh.getMaKH()
                );
    }
    
    public void delete_KhachHang(String id){
        String sql = "delete KhachHang where MaKhach=?";
        jdbcHelper.executeUpdate(sql, id);
    }
    
    public List<KhachHang> select(){
        String sql = "select * from KhachHang";
        return select(sql);
    }
    
    public KhachHang findById(String id){
        String sql = "select * from KhachHang where MaKhach=?";
        List<KhachHang> list = select(sql,id);
        return list.size()>0 ? list.get(0):null;
    }
    
    private List<KhachHang> select(String sql, Object...args){
        List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try{
                rs = jdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    KhachHang kh = readFromResultSet(rs);
                    list.add(kh);
                }
            }finally{
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }return list;
    }
    
    private KhachHang readFromResultSet(ResultSet rs) throws SQLException{
        KhachHang kh = new KhachHang();
        kh.setMaKH(rs.getString(1));
        kh.setTenKH(rs.getString(2));
        kh.setSdt(rs.getString(3));
        kh.setDiaChi(rs.getString(4));
        
        return kh;
    }
}
