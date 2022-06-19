package com.smpn29.repo.dao;

import com.smpn29.repo.model.Kelas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : Rahman S
 * @Project : smpn29jkt
 * @Create : Saturday, 18 June 2022 / 18:06
 * @Package : com.smpn29.repo.dao
 **/
public class KelasDao extends BaseDao {
    public KelasDao() {
        this.table = "kelas";
        this.primaryKey = "kelas_id";
    }

    public ArrayList<Kelas> all() {
        return super.all(Kelas.class);
    }

    public Kelas find(int id) {
        return super.find(String.valueOf(id), Kelas.class);
    }

    public void add(Kelas kelas) {
        Map<String, Object> data = new HashMap();

        data.put("kelas_name", kelas.getKelasName());
        data.put("kelas_code", kelas.getKelasCode());

        super.add(data);
    }

    public void update(Kelas kelas) {
        Map<String, Object> data = new HashMap();

        data.put("nama_kelas", kelas.getKelasName());
        data.put("code_kelas", kelas.getKelasCode());

        super.update(data, kelas.getKelasId());
    }

    public void delete(Kelas kelas) {
        super.delete(String.valueOf(kelas.getKelasId()));
    }
}
