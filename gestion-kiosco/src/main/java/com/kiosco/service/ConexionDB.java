package com.kiosco.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/kiosco_db";
    private static final String USER = "root";
    private static final String PASS = "Roldan12";

    public static Connection getConexion() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Error de coneccion: " + e.getMessage());
        }
        return con;
    }

}
