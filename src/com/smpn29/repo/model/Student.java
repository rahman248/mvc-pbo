/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.repo.model;

import com.smpn29.repo.dao.KelasDao;
import com.smpn29.repo.dao.PaymentDao;
import com.smpn29.repo.dao.SppDao;
import com.smpn29.repo.dao.StudentDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Rahman S
 */
public class Student implements BaseModel {

    private String nisn;
    private String nis;
    private String nama;
    private int kelasId;
    private String alamat;
    private String noTelp;
    private int sppId;

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getKelasId() {
        return kelasId;
    }

    public void setKelasId(int kelasId) {
        this.kelasId = kelasId;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public int getSppId() {
        return sppId;
    }

    public void setSppId(int sppId) {
        this.sppId = sppId;
    }

    public Kelas getKelas() {
        Kelas kelas = new KelasDao().find(kelasId);

        return kelas;
    }

    public Spp getSpp() {
        Spp spp = new SppDao().find(sppId);


        return spp;
    }

    @Override
    public void save() {
        StudentDao dao = new StudentDao();

        if (dao.find(nisn) == null) {
            dao.add(this);
        } else {
            dao.update(this);
        }
    }

    @Override
    public void delete() {
        if (!this.nisn.equals("")) {
            new StudentDao().delete(this);
        }
    }

    @Override
    public void fillFromResultSet(ResultSet result) throws SQLException {
        setNisn(result.getString("nisn"));
        setNis(result.getString("nis"));
        setNama(result.getString("nama"));
        setKelasId(result.getInt("kelas_id"));
        setAlamat(result.getString("alamat"));
        setNoTelp(result.getString("no_telp"));
        setSppId(result.getInt("spp_id"));
    }

    public ArrayList<Payment> getPayment() {
        return new PaymentDao().where("nisn = " + nisn, "tgl_bayar DESC, payment_id DESC", Payment.class);
    }
}
