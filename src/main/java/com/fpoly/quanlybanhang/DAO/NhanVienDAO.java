/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.DAO;

import com.fpoly.quanlybanhang.Model.NhanVien;
import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public class NhanVienDAO {

    public void insert_NhanVien(NhanVien nv) {
        String sql = "insert into NhanVien values(?,?,?,?,?,?,?,?,?)";
        jdbcHelper.executeUpdate(sql,
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getMatKhau(),
                nv.getNgaySinh(),
                nv.getDiaChi(),
                nv.isGioiTinh(),
                nv.getSdt(),
                nv.isVaiTro(),
                nv.getHinh()
        );
    }

    public void update_NhanVien(NhanVien nv) {
        String sql = "update NhanVien set TenNV = ?,MatKhau=? ,NgaySinh=? ,DiaChi=? "
                + " ,GioiTinh = ?,sdt =? ,VaiTro =? ,Hinh=? where MaNV=?";
        jdbcHelper.executeUpdate(sql,
                 nv.getTenNV(),
                nv.getMatKhau(),
                nv.getNgaySinh(),
                nv.getDiaChi(),
                nv.isGioiTinh(),
                nv.getSdt(),
                nv.isVaiTro(),
                nv.getHinh(),
                nv.getMaNV()
        );
    }

    public void delete_NhanVien(String id) {
        String sql = "delete NhanVien where MaNV=?";
        jdbcHelper.executeUpdate(sql, id);
    }

    public List<NhanVien> select() {
        String sql = "select * from NhanVien";
        return select(sql);
    }

    public NhanVien findByID(String id) {
        String sql = "select * from NhanVien where maNV=?";
        List<NhanVien> list = select(sql, id);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<NhanVien> select(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = jdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    NhanVien nv = readFromResultSet(rs);
                    list.add(nv);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        return list;
    }

    private NhanVien readFromResultSet(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaNV(rs.getString("MaNV"));
        nv.setTenNV(rs.getString(2));
        nv.setMatKhau(rs.getString(3));
        nv.setNgaySinh(rs.getDate(4));
        nv.setDiaChi(rs.getString(5));
        nv.setGioiTinh(rs.getBoolean(6));
        nv.setSdt(rs.getString(7));
        nv.setVaiTro(rs.getBoolean(8));
        nv.setHinh(rs.getString(9));
        return nv;



//        nv.setTenNV(rs.getString("TenNV"));
//        nv.setSdt(rs.getString("SDT"));
//        nv.setDiaChi(rs.getString("DiaChi"));
//        
//        nv.setHinh(rs.getString("Anh"));
//        nv.setNgaySinh(rs.getDate("NgaySinh"));
//        
//        nv.setGioiTinh(rs.getBoolean("GioiTinh"));
//        
//        nv.setVaiTro(rs.getBoolean("VaiTro"));
//        
//        
//        nv.setMatKhau(rs.getString("MatKhau"));
        //return nv;
    }

}
