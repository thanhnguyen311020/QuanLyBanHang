/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.DAO;

import com.fpoly.quanlybanhang.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class ThongKeDAO {
    
    public List<Object[]> ThongKeDoanhThu(String startDate, String endDate){
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try{
                String sql = "{call sp_ThongkeDoanhThu(?,?)}";
                rs = jdbcHelper.executeQuery(sql, startDate,endDate);
                while (rs.next()) {                    
                    Object[] model = {
                        rs.getInt("MaHoaDonCT"),
                        rs.getString("MaHoaDon"),
                        rs.getString("TenSP"),
                        rs.getInt("SoLuong"),
                        rs.getDate("NgayBan"),
                        rs.getFloat("TongTien")
                    };
                      list.add(model);
                }
            }finally{
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
            
        }return list;
    }
}
