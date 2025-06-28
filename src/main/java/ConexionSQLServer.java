/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author USUARIO
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQLServer {
    public static void main(String[] args) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            String url = "jdbc:sqlserver://DESKTOP-LIQ0V6G\\SQLEXPRESS;"
                       + "databaseName=polisalud;"
                       + "encrypt=true;"
                       + "trustServerCertificate=true;"
                       + "loginTimeout=30";
            
            String user = "login1";
            String password = "P@ssw0rd";

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a SQL Server.");
            conn.close();
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Controlador JDBC no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            e.printStackTrace(); // Detalles adicionales del error
        }
    }
}



