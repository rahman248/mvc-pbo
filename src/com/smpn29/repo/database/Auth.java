/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smpn29.repo.database;

import com.smpn29.repo.model.Petugas;
import javax.swing.JOptionPane;

/**
 *
 * @author Rahman S
 */
public class Auth {
    static Petugas mUser;

    public static Petugas getUser() {
        return mUser;
    }

    public static void setUser(Petugas user) {
        Auth.mUser = mUser;
    }

    public static Petugas check() {
        if (mUser == null) {
            JOptionPane.showMessageDialog(null, "Session ended, please reopen the program and login!");

            System.exit(0);
        }

        System.out.println("Login check for user :" + mUser.getUsername() + " as " + mUser.getLevel());

        return mUser;
    }
    
}
