/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.helper;

import com.fpoly.quanlybanhang.Model.NhanVien;
import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;

/**
 *
 * @author ADMIN
 */
public class ShareHelper {
    
    public static final Image APP_ICON;
    
    static{
        String file ="";
        APP_ICON= new ImageIcon(ShareHelper.class.getResource(file)).getImage();
        
    }
    
    public static boolean saveLogo(File file){
        File dir = new File("Logos");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        File newFile = new File(dir, file.getName());
        try {
             //coppy vao thu muc logos(de neu da ton tai)
            Path source = Paths.get(file.getAbsolutePath());
            Path destination = Paths.get(newFile.getAbsolutePath());
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // doc hinh anh logo chuyen de
    public static ImageIcon readLogos(String fileName){
        File path = new File("logos",fileName);
        return new ImageIcon(path.getAbsolutePath());
    }
    
    // chua thong tin nguoi dung sau khi dang nhap
    public static NhanVien USER = null;
    
    // xoa thong tin gnuoi dung sau khi dang xuat
    public static void logoff(){
        ShareHelper.USER=null;
    }
    
   // kiem tra xem da dang nhap hay cbhua
    public static boolean authenticated(){
        return ShareHelper.USER!=null;
    }
}
