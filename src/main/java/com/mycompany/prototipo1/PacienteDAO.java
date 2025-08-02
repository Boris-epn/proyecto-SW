package com.mycompany.prototipo1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PacienteDAO {
    private int cedula;

    public PacienteDAO(int cedula) {
        this.cedula = cedula;
    }
    

    /**
     * Busca pacientes en la base de datos por cédula o nombre completo.
     * @param criterio El campo por el cual buscar ("Cédula" o "Nombre").
     * @param valor El texto a buscar.
     * @return Un DefaultTableModel con los resultados para mostrar en una JTable.
     */
    public static DefaultTableModel buscarPacientes(String criterio, String valor) {
        // Definimos las columnas que tendrá nuestra tabla de resultados
        String[] columnas = {"Cédula", "Nombres", "Apellidos", "Fecha de Nacimiento", "Sexo"};
        DefaultTableModel model = new DefaultTableModel(null, columnas);
        
        String sql = "";
        
        // Construimos la consulta SQL dinámicamente dependiendo del criterio
        if ("Cédula".equals(criterio)) {
            sql = "SELECT cedula, nombres, apellidos, fecha_nacimiento, sexo FROM Paciente WHERE cedula LIKE ?";
        } else if ("Nombre".equals(criterio)) {
            // Usamos CONCAT para buscar en nombre y apellido juntos
            sql = "SELECT cedula, nombres, apellidos, fecha_nacimiento, sexo FROM Paciente WHERE CONCAT(nombres, ' ', apellidos) LIKE ?";
        } else {
            return model; // Si el criterio no es válido, retorna un modelo vacío
        }

        try (Connection conn = ConexionSQLServer.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Usamos '%' para que la búsqueda sea flexible (contenga el texto)
            pstmt.setString(1, "%" + valor + "%");
            
            ResultSet rs = pstmt.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // Llenamos el modelo con las filas encontradas
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("cedula");
                fila[1] = rs.getString("nombres");
                fila[2] = rs.getString("apellidos");
                if (rs.getDate("fecha_nacimiento") != null) {
                    fila[3] = sdf.format(rs.getDate("fecha_nacimiento"));
                } else {
                    fila[3] = "";
                }
                fila[4] = rs.getString("sexo");
                model.addRow(fila);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return model;
    }
    public static Map<String, String> obtenerInformacionPacientePorCedula(String cedulaPaciente) {
    Map<String, String> datosPaciente = new HashMap<>();
    String sql = "SELECT p.cedula, p.nombres, p.apellidos, p.estado_civil, p.sangre, p.telefono, " +
                 "p.fecha_nacimiento, p.sexo, p.edad, p.correo, p.alergias " +
                 "FROM Paciente p " +
                 "WHERE p.cedula = ?";
    
    try (Connection conn = ConexionSQLServer.conectar();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, cedulaPaciente);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fechaNacimiento = "";
            if (rs.getDate("fecha_nacimiento") != null) {
                fechaNacimiento = sdf.format(rs.getDate("fecha_nacimiento"));
            }
            
            datosPaciente.put("cedula", rs.getString("cedula"));
            datosPaciente.put("nombres", rs.getString("nombres"));
            datosPaciente.put("apellidos", rs.getString("apellidos"));
            datosPaciente.put("fecha_nacimiento", fechaNacimiento);
            datosPaciente.put("sexo", rs.getString("sexo"));
            datosPaciente.put("correo", rs.getString("correo"));
            datosPaciente.put("alergias", rs.getString("alergias"));
            datosPaciente.put("edad", rs.getString("edad"));
            datosPaciente.put("telefono", rs.getString("telefono"));
            datosPaciente.put("estado_civil", rs.getString("estado_civil"));
            datosPaciente.put("sangre", rs.getString("sangre"));
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, 
            "Error en la consulta SQL: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(null, 
            "Error: Driver JDBC no encontrado", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    System.out.println("*******************");
    System.out.println("Datos del paciente encontrados: " + datosPaciente);
    return datosPaciente;
}
}