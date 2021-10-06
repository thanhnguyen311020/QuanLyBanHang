/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpoly.quanlybanhang.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
public class DateHelper {
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    public static Date toDae(String date, String...pattern){
        try {
            if (pattern.length>0) {
                DATE_FORMAT.applyPattern(pattern[0]);
            }
            if (date == null) {
                return DateHelper.now();
            }
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String toString(Date date, String...pattern){
        if (pattern.length>0) {
            DATE_FORMAT.applyPattern(pattern[0]);
        }
        if (date== null) {
            date = DateHelper.now();
        }
        return DATE_FORMAT.format(date);
    }
    
    public static Date now(){
        return new Date();
    }
}
