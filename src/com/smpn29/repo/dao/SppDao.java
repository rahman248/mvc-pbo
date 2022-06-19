/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.repo.dao;

import com.smpn29.repo.model.Spp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rahman S
 */
public class SppDao extends BaseDao{
    public SppDao() {
        this.table = "spp";
        this.primaryKey = "spp_id";
    }

    public void delete(Spp spp) {
        super.delete(String.valueOf(spp.getSppId()));
    }

    public void update(Spp spp) {
        Map<String, Object> data = new HashMap<>();
        
        data.put("tahun", spp.getTahun());
        data.put("nominal", spp.getNominal());
        
        super.update(data, spp.getSppId());
    }

    public int add(Spp spp) {
         Map<String, Object> data = new HashMap<>();
        
        data.put("tahun", spp.getTahun());
        data.put("nominal", spp.getNominal());
        
        String id = super.add(data);
        
        return Integer.parseInt(id);
    }

    public Spp find(int id) {
        return super.find(String.valueOf(id), Spp.class);
    }

    public ArrayList<Spp> all() {
        return super.all(Spp.class, "spp_id DESC");
    }
}
