/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.DAO;

import com.fpoly.quanlybanhang.Model.LoaiSanPham;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class LoaiSPDAO {
   
    public void insert_LoaiSP(LoaiSanPham lsp){
        String sql = "Insert into LoaiSanPham values(?,?)";
        jdbcHelper.executeUpdate(sql, lsp.getMaLoai(),lsp.getTenLoai());
    }
    
    public void update_LoaiSP(LoaiSanPham lsp){
        String sql = "update LoaiSanPham set  TenLoai=? where MaLoai=?" ;
        jdbcHelper.executeUpdate(sql, lsp.getTenLoai(),lsp.getMaLoai());
    }
    
    public void delete_LoaSP(String id){
        String sql = "delete from LoaiSanPham where MaLoai = ?";
        jdbcHelper.executeUpdate(sql, id);
    }
    
    public List<LoaiSanPham> select(){
        String sql = "select * from LoaiSanPham";
        return select(sql);
    }
    public List<LoaiSanPham> findByIDs(String id){
        String sql = "select * from LoaiSanPham where MaLoai=?";
        return select(sql,id);
    }
    
    public LoaiSanPham findByID(String id){
        String sql = "select * from LoaiSanPham where MaLoai=?";
        List<LoaiSanPham> list = select(sql,id);
        return list.size()>0?list.get(0):null;
    }
    
    private List<LoaiSanPham> select(String sql , Object...args){
        List<LoaiSanPham> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try{
                rs = jdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    LoaiSanPham lsp = readFromResultSet(rs);
                    list.add(lsp);
                }
            }finally{
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }return list;
    }
    
    private LoaiSanPham readFromResultSet(ResultSet rs) throws SQLException{
        LoaiSanPham lsp = new LoaiSanPham();
        lsp.setMaLoai(rs.getString(1));
        lsp.setTenLoai(rs.getString(2));
        return lsp;
    }
}
