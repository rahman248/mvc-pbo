/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.repo.model;

import com.smpn29.repo.dao.PetugasDao;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Rahman S
 */
public class Petugas implements BaseModel {

    private int idPetugas;
    private String username;
    private String password;
    private String name;
    private String level;

    public int getIdPetugas() {
        return idPetugas;
    }

    public void setIdPetugas(int idPetugas) {
        this.idPetugas = idPetugas;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

   

    @Override
    public void save() {
        if (this.idPetugas == 0) {
            new PetugasDao().add(this);
        } else {
            new PetugasDao().update(this);
        }
    }

    @Override
    public void delete() {
        if (this.idPetugas != 0) {
            new PetugasDao().delete(this);
        }
    }

    @Override
    public void fillFromResultSet(ResultSet result) throws SQLException {
        setIdPetugas(result.getInt("petugas_id"));
        setUsername(result.getString("username"));
        setPassword(result.getString("password"));
        setName(result.getString("name"));
        setLevel(result.getString("level"));
    }

}
