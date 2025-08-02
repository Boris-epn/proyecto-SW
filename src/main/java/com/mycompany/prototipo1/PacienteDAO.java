package com.mycompany.prototipo1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;

public class PacienteDAO {

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
}