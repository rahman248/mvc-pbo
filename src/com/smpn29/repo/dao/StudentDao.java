/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.repo.dao;

import com.smpn29.repo.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rahman S
 */
public class StudentDao extends BaseDao {

    public StudentDao() {
        this.table = "student";
        this.primaryKey = "nisn";
    }

    public void delete(Student student) {
        super.delete(student.getNisn());
    }

    public void update(Student student) {
        Map<String, Object> data = new HashMap();
        data.put("nisn", student.getNisn());
        data.put("nis", student.getNis());
        data.put("nama", student.getNama());
        data.put("kelas_id", student.getKelasId());
        data.put("alamat", student.getAlamat());
        data.put("no_telp", student.getNoTelp());
        data.put("spp_id", student.getSppId());

        super.update(data, student.getNisn());
    }

    public void add(Student student) {
        Map<String, Object> data = new HashMap();

        data.put("nisn", student.getNisn());
        data.put("nis", student.getNis());
        data.put("nama", student.getNama());
        data.put("kelas_id", student.getKelasId());
        data.put("alamat", student.getAlamat());
        data.put("no_telp", student.getNoTelp());
        data.put("spp_id", student.getSppId());

        super.add(data);
    }

    public Student find(String nisn) {
        return super.find(nisn, Student.class);
    }

    public ArrayList<Student> all() {
        return super.all(Student.class);
    }

}
