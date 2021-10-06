/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.helper;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class DialogHelper {
    
    public static void alert(Component component,String message){
        JOptionPane.showMessageDialog(component, message,"Hệ Thống Quản Bán Hàng",JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static boolean confirm(Component parent, String message){
        int result = JOptionPane.showConfirmDialog(parent, message,
                "Hệ thống Quản Lý Bán Hàng",JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    
    public static String prompt(Component parent, String message){
        return JOptionPane.showInputDialog(parent,message,
                "Hệ thống Quản Lý Bán Hàng",JOptionPane.INFORMATION_MESSAGE);
    }
}
