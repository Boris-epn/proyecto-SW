package com.mycompany.prototipo1;


import com.mycompany.prototipo1.ConexionSQLServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class ResultadosDAO {

    // Obtiene diagnóstico y pronóstico de la tabla Evolucion
    public static Map<String, String> obtenerEvolucion(int idCita) {
        Map<String, String> evolucion = new HashMap<>();
        String sql = "SELECT diagnostico, pronostico FROM Evolucion WHERE id_cita = ?";
        try (Connection conn = ConexionSQLServer.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                evolucion.put("diagnostico", rs.getString("diagnostico"));
                evolucion.put("pronostico", rs.getString("pronostico"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return evolucion;
    }

    // Obtiene la descripción del tratamiento
    public static String obtenerTratamientos(int idCita) {
        StringBuilder tratamientos = new StringBuilder();
        String sql = "SELECT t.descripcion FROM Tratamiento t " +
                     "INNER JOIN Aplicar a ON t.id_tratamiento = a.id_tratamiento " +
                     "WHERE a.id_cita = ?";
        try (Connection conn = ConexionSQLServer.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tratamientos.append(rs.getString("descripcion")).append("\n");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tratamientos.toString();
    }

    // Obtiene los exámenes. Asume que 'Evaluar' tiene un campo 'resultado'.
    public static DefaultTableModel obtenerExamenes(int idCita) {
        String[] columnas = {"Examen", "Resultado"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        String sql = "SELECT ex.nombre, ev.resultado " +
                     "FROM Examen ex " +
                     "INNER JOIN Evaluar ev ON ex.id_examen = ev.id_examen " +
                     "WHERE ev.id_cita = ?";
        // ... el resto del método se mantiene igual
        return model;
    }

    // Obtiene la receta. Asume que 'Prescribir' tiene 'id_medicamento', 'dosis', etc.
    public static DefaultTableModel obtenerReceta(int idCita) {
        String[] columnas = {"Medicamento", "Presentación", "Dosis", "Frecuencia"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        String sql = "SELECT m.nombre, m.presentacion, p.dosis, p.frecuencia " +
                     "FROM Medicamento m " +
                     "INNER JOIN Prescribir p ON m.id_medicamento = p.id_medicamento " + // Asunción importante
                     "WHERE p.id_cita = ?";
        // ... el resto del método se mantiene igual
        return model;
    }
    
    // Obtiene los signos vitales directamente de la tabla Cita
    public static DefaultTableModel obtenerSignosVitales(int idCita) {
        String[] columnas = {"Presión sistólica", "Presión diastólica", "Peso", "Frecuencia Cardíaca"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        String sql = "SELECT presion_sistolica, presion_diastolica, peso, frecuencia_cardiaca FROM Cita WHERE id_cita = ?";
        try (Connection conn = ConexionSQLServer.conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCita);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("presion_sistolica"), rs.getString("presion_diastolica"),
                    rs.getString("peso"), rs.getString("frecuencia_cardiaca")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return model;
    }
}