package com.smpn29.repo.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Rahman S
 */
public class DbConnections {

    static Connection con;

    public static Connection createConnection() {

        if (con == null) {

            try {
                Class.forName("org.mariadb.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mariadb://localhost:3307/smpnjkt", "root", "KopikoP1!@#$");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }

        return con;
    }

}