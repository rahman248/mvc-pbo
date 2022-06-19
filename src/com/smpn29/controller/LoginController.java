/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.controller;

import com.smpn29.repo.dao.PetugasDao;
import com.smpn29.repo.database.Auth;
import com.smpn29.repo.model.Petugas;
import com.smpn29.view.ui.LoginView;
import com.smpn29.view.ui.MainView;

import javax.swing.JOptionPane;

/**
 *
 * @author Rahman S
 */
public class LoginController {

    PetugasDao petugasDao = new PetugasDao();

    public void login(LoginView view) {
        String username = view.getTextUsername().getText();
        String password = String.valueOf(view.getTextPassword().getPassword());

        Petugas user = petugasDao.getByUsernameAndPassword(username, password);

        if (user != null) {
            Auth.setUser(user);
            

        } else {
            JOptionPane.showMessageDialog(null, "username atau password salah!");
            Auth.setUser(null);
         
        }
    }

}
