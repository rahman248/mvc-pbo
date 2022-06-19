package com.smpn29.repo.model;

import com.smpn29.repo.dao.BaseDao;
import com.smpn29.repo.dao.SppDao;
import com.smpn29.repo.dao.StudentDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Rahman S
 */
public class Spp implements BaseModel {
    private int sppId;
    private String tahun;
    private int nominal;

    public int getSppId() {
        return sppId;
    }

    public void setSppId(int sppId) {
        this.sppId = sppId;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public ArrayList<Student> getSiswa() {
        return new StudentDao().where("spp_id=" + this.sppId, Student.class);
    }

    @Override
    public void save() {
        if (this.sppId == 0) {
            int id = new SppDao().add(this);

            this.setSppId(id);
        } else {
            new SppDao().update(this);
        }
    }

    @Override
    public void delete() {
        if (this.sppId != 0) {
            new SppDao().delete(this);
        }
    }

    @Override
    public void fillFromResultSet(ResultSet result) throws SQLException {
        setSppId(result.getInt("spp_id"));
        setTahun(result.getString("tahun"));
        setNominal(result.getInt("nominal"));
    }

    @Override
    public String toString() {
        return this.tahun + " - " + this.nominal;
    }
}
