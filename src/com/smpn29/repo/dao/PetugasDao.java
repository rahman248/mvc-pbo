/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.repo.dao;

import com.smpn29.repo.model.Petugas;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rahman S
 */
public class PetugasDao extends BaseDao {

    public PetugasDao() {
        this.table = "petugas";
        this.primaryKey = "petugas_id";
    }
    
    
     private final String selectByUsernameAndPassword = "SELECT * FROM petugas WHERE username = ? AND password = ?";

    public ArrayList<Petugas> all() {
        return super.all(Petugas.class);
    }

    public void add(Petugas petugas) {
        Map<String, Object> data = new HashMap();

        data.put("username", petugas.getUsername());
        data.put("password", petugas.getPassword());
        data.put("name", petugas.getName());
        data.put("level", petugas.getLevel());

        super.add(data);
    }

    public Petugas find(int id) {
        return super.find(String.valueOf(id), Petugas.class);
    }

    public void update(Petugas petugas) {
        Map<String, Object> data = new HashMap<>();

        data.put("username", petugas.getUsername());
        data.put("password", petugas.getPassword());
        data.put("name", petugas.getName());
        data.put("level", petugas.getLevel());

        super.update(data, petugas.getIdPetugas());
    }

    public void delete(Petugas petugas) {
        super.delete("" + petugas.getIdPetugas());
    }

    public Petugas getByUsernameAndPassword(String username, String password) {
        Petugas petugas = null;

        try {
            PreparedStatement statement = connection.prepareStatement(selectByUsernameAndPassword);

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                petugas = new Petugas();

                petugas.fillFromResultSet(result);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PetugasDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return petugas;

    }
    
    
    
}
