/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.helper;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author ADMIN
 */
public class ValidateHelper {
   
    public static void chekEmpyDemo(JTextField field, StringBuilder sb, String error) {
        if (field.getText().equals("")) {
            sb.append(error).append("\n");
            field.setBackground(Color.pink);
            field.requestFocus();
        }else{
            field.setBackground(Color.white);
        }
    }
    
    public static void checkEmail(JTextField field,JLabel lbl , StringBuilder sb) {

        Pattern pattern = Pattern.compile("\\w+@\\w+\\.\\w+");

        Matcher matcher = pattern.matcher(field.getText());// so giá trị nhập vào vs mẫu 

        if (!matcher.find()) {// kiểm tra so khớp nếu trùng trả True
            sb.append("Email Sai Định Dạng");
            field.setBackground(Color.pink);
            lbl.setVisible(true);
        } else {
            field.setBackground(Color.white);
            lbl.setVisible(false);
        }

    }
    public static void checkDate(JTextField field,JLabel lbl, StringBuilder sb){
        SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date_format.parse(field.getText());
            lbl.setVisible(false);
            field.setBackground(Color.white);
        } catch (Exception e) {
            sb.append("Sai định dạng hoặc dữ liệu").append("\n");
            lbl.setVisible(true);
            field.setBackground(Color.pink);
            
        }
    }
}
