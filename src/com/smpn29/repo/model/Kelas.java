package com.smpn29.repo.model;

import com.smpn29.repo.dao.KelasDao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author : Rahman S
 * @Project : smpn29jkt
 * @Create : Saturday, 18 June 2022 / 18:04
 * @Package : com.smpn29.repo.model
 **/
public class Kelas implements BaseModel {

    private int kelasId;
    private String kelasName;
    private String kelasCode;

    public int getKelasId() {
        return kelasId;
    }

    public void setKelasId(int kelasId) {
        this.kelasId = kelasId;
    }

    public String getKelasName() {
        return kelasName;
    }

    public void setKelasName(String kelasName) {
        this.kelasName = kelasName;
    }

    public String getKelasCode() {
        return kelasCode;
    }

    public void setKelasCode(String kelasCode) {
        this.kelasCode = kelasCode;
    }

    /**
     *
     */
    @Override
    public void save() {
        if (this.kelasId == 0) {
            new KelasDao().add(this);
        } else {
            new KelasDao().update(this);
        }
    }

    /**
     *
     */
    @Override
    public void delete() {
        if (this.kelasId != 0) {
            new KelasDao().delete(this.kelasId + "");
        }
    }

    /**
     * @param result
     * @throws SQLException
     */
    @Override
    public void fillFromResultSet(ResultSet result) throws SQLException {
        setKelasId(result.getInt("kelas_id"));
        setKelasName(result.getString("kelas_name"));
        setKelasCode(result.getString("kelas_code"));

    }
}
