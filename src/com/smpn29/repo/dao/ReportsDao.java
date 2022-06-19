/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.repo.dao;

import com.smpn29.repo.model.Report;
import java.util.ArrayList;

/**
 *
 * @author Rahman S
 */
public class ReportsDao extends BaseDao{
    
     public ReportsDao() {
        this.table = "payment";
        this.primaryKey = "payment_id";
    }

     public ArrayList<Report> reporting() {
        return super.reporting(Report.class, table);
    }
    
}
