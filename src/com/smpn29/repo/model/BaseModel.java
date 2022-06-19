/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.repo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Rahman S
 */
public interface BaseModel {

    public void save();

    public void delete();

    public void fillFromResultSet(ResultSet result) throws SQLException;
}

