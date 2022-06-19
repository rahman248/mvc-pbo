/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.smpn29;

import com.smpn29.view.Login.LoginView;
import com.smpn29.view.Splash.Splash;
import java.sql.Connection;
import java.util.Locale;

/**
 *
 * @author Rahman S
 */
public class App {
    Connection con;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Locale.setDefault(new Locale("id", "ID"));
        Splash mScreen = new Splash();
      
        mScreen.setVisible(true);
        try {
            for (int row = 0; row <= 100; row++) {
                Thread.sleep(100);
                mScreen.numberProgress.setText(Integer.toString(row) + "%");
                mScreen.loadingProgress.setValue(row);
                if (row == 100) {

                    mScreen.setVisible(false);
                    new LoginView().setVisible(true);

                }
            }
        } catch (Exception e) {
        }
    }
    
}
