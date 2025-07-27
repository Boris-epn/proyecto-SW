package vista;

import com.mycompany.prototipo1.CitaDAO;
import java.awt.Image;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import vista.resultados;
import vista.evolucion;
import vista.editar_paciente;
import com.mycompany.prototipo1.AntecedentesDAO;
import javax.swing.table.DefaultTableModel;
import com.mycompany.prototipo1.CitaDAO;
import com.mycompany.prototipo1.ConsultaPrevia;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.table.TableColumnModel;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author USUARIO
 */
public class informacion_doctor extends javax.swing.JFrame {
    private String cedulaDoctor;
    private String cedulaPaciente;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(informacion_doctor.class.getName());

    /**
     * Creates new form informaciion_doctor
     */
    public informacion_doctor(String cedulaDoctor) {
        initComponents();
        this.setLocationRelativeTo(this);
        this.cedulaDoctor = cedulaDoctor;
        Map<String, String> datosPaciente = CitaDAO.obtenerPacienteConCitaMasProxima(cedulaDoctor);
        if (datosPaciente != null && !datosPaciente.isEmpty()) {
            this.cedulaPaciente=datosPaciente.getOrDefault("cedula", "");
            System.out.println("la cedula del paciente es");
            System.out.println(this.cedulaPaciente);
        }
        cargarDatosDelPaciente();
        cargarEvolucion();
        
    }
    
    private void cargarEvolucion() {
    if (this.cedulaPaciente == null || this.cedulaPaciente.isEmpty()) {
        // Limpiar la tabla si no hay paciente seleccionado
        DefaultTableModel model = (DefaultTableModel) jTEvolucion.getModel();
        model.setRowCount(0);
        model.addRow(new Object[]{"Seleccione un paciente primero", "", "", "", "", ""});
        return;
    }

    // Obtener los registros de evolución para el paciente
    List<Map<String, String>> evoluciones = CitaDAO.obtenerEvolucion(this.cedulaPaciente);

    // Obtener el modelo de la tabla
    DefaultTableModel model = (DefaultTableModel) jTEvolucion.getModel();
    model.setRowCount(0); // Limpiar datos existentes

    if (evoluciones.isEmpty()) {
        // Agregar una fila indicando que no hay datos
        model.addRow(new Object[]{"No hay registros", "", "", "", "", ""});
    } else {
        // Llenar la tabla con los datos
        for (Map<String, String> evolucion : evoluciones) {
            model.addRow(new Object[]{
                evolucion.getOrDefault("fecha", "N/A"),
                evolucion.getOrDefault("hora", "N/A"),
                evolucion.getOrDefault("doctor", "N/A"),
                evolucion.getOrDefault("especialidad", "N/A"),
                evolucion.getOrDefault("diagnostico", "N/A"),
                evolucion.getOrDefault("pronostico", "N/A")
            });
        }
    }

    // Configurar el ancho de las columnas
    TableColumnModel columnModel = jTEvolucion.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(80);  // Fecha
    columnModel.getColumn(1).setPreferredWidth(60);  // Hora
    columnModel.getColumn(2).setPreferredWidth(150); // Doctor
    columnModel.getColumn(3).setPreferredWidth(100); // Especialidad
    columnModel.getColumn(4).setPreferredWidth(250); // Diagnóstico
    columnModel.getColumn(5).setPreferredWidth(250); // Pronóstico
    
    // Hacer que la tabla sea seleccionable pero no editable
    jTEvolucion.setDefaultEditor(Object.class, null);
}
    
    private void cargarDatosDelPaciente() {
    Map<String, String> datos = CitaDAO.obtenerPacienteConCitaMasProxima(cedulaDoctor);
    
    if (datos != null && !datos.isEmpty()) {
        this.cedulaPaciente = datos.get("cedula");

        // --- Panel de Información del paciente ---
        jTFNombres.setText(datos.getOrDefault("nombres", ""));
        jTFApellidos.setText(datos.getOrDefault("apellidos", ""));
        this.jTFCedula.setText(datos.getOrDefault("cedula", ""));
        this.jTFFechaNacimiento.setText(datos.getOrDefault("fecha_nacimiento", ""));
        this.jTFSexo.setText(datos.getOrDefault("sexo", ""));
        this.jTFCorreo.setText(datos.getOrDefault("correo", ""));
        this.jtfalergias.setText(datos.getOrDefault("alergias",""));
        // ... (resto de campos de información del paciente) ...

        // --- Panel de Evolución ---
        // Asumiendo que tus campos de texto se llaman así:
        

        // --- Carga de datos secundarios ---
        cargarAntecedentes();
        cargarConsultasPrevias();
        cargarEvolucion();

    } else {
        // ... (código para limpiar los campos) ...
        limpiarCamposPaciente(); // Llama al método de limpieza
        cargarEvolucion();
    }
}

    // Los métodos cargarAntecedentes(), cargarConsultasPrevias(), limpiar... 
    // y el resto de la clase se mantienen como estaban, pero ahora son llamados
    // desde el nuevo método centralizado 'cargarDatosDelPaciente()'.
    // ...
    private void cargarPacienteConCitaProxima() {
        Map<String, String> datosPaciente = CitaDAO.obtenerPacienteConCitaMasProxima(cedulaDoctor);
        
        if (datosPaciente != null && !datosPaciente.isEmpty()) {
            jTFNombres.setText(datosPaciente.getOrDefault("nombres", ""));
            jTFApellidos.setText(datosPaciente.getOrDefault("apellidos", ""));
            jTFCedula.setText(datosPaciente.getOrDefault("cedula", ""));
            jTFFechaNacimiento.setText(datosPaciente.getOrDefault("fecha_nacimiento", ""));
            jTFSexo.setText(datosPaciente.getOrDefault("sexo", ""));
            jTFCorreo.setText(datosPaciente.getOrDefault("correo", ""));
            jTFAlergias.setText(datosPaciente.getOrDefault("alergias", ""));
            cargarAntecedentes();
        } else {
            limpiarCamposPaciente();
            JOptionPane.showMessageDialog(this, 
                "No tiene citas programadas para hoy o días futuros", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    
    private void limpiarCamposPaciente() {
        jTFNombres.setText("");
        jTFApellidos.setText("");
        jTFCedula.setText("");
        jTFFechaNacimiento.setText("");
        jTFSexo.setText("");
        jTFCorreo.setText("");
        jTFAlergias.setText("");
    }
    private void cargarAntecedentes() {
    if (this.cedulaPaciente == null || this.cedulaPaciente.isEmpty()) {
        limpiarTablaAntecedentes();
        return;
    }

    // Llamamos al nuevo DAO
    Map<String, String> antecedentes = AntecedentesDAO.obtenerAntecedentesPorPaciente(this.cedulaPaciente);

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

    if (antecedentes != null && !antecedentes.isEmpty()) {
        // Aseguramos que la tabla tenga al menos una fila para escribir
        if (model.getRowCount() == 0) {
            model.addRow(new Object[model.getColumnCount()]);
        }
        // Llenamos la primera fila (fila 0) con los datos
        model.setValueAt(antecedentes.get("familiares"), 0, 0);
        model.setValueAt(antecedentes.get("patologicos"), 0, 1);
        model.setValueAt(antecedentes.get("fisiologicos"), 0, 2);
    } else {
        // Si no hay antecedentes, limpiamos la tabla
        limpiarTablaAntecedentes();
    }
}
// En el archivo informacion_doctor.java

private void cargarConsultasPrevias() {
    if (this.cedulaPaciente == null || this.cedulaPaciente.isEmpty()) return;

    jPanelConsultasContainer.removeAll();

    List<ConsultaPrevia> consultas = CitaDAO.obtenerConsultasPrevias(this.cedulaPaciente);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    if (consultas.isEmpty()) {
        jPanelConsultasContainer.add(new JLabel("No se encontraron consultas previas."));
    } else {
        for (ConsultaPrevia consulta : consultas) {
            JPanel consultaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));

            JTextField fechaField = new JTextField(sdf.format(consulta.getFecha()), 10);
            fechaField.setEditable(false);
            
            // CORRECCIÓN: Se vuelve a añadir el campo para la especialidad.
            JTextField espField = new JTextField(consulta.getEspecialidad(), 15);
            espField.setEditable(false);

            JTextField docField = new JTextField(consulta.getNombreDoctor(), 20);
            docField.setEditable(false);

            JButton btnResultados = new JButton("Resultados");
            
            btnResultados.putClientProperty("id_cita", consulta.getIdCita());

            btnResultados.addActionListener(e -> {
                int idCitaSeleccionada = (int) ((JButton) e.getSource()).getClientProperty("id_cita");
                resultados ventanaResultados = new resultados(idCitaSeleccionada);
                ventanaResultados.setVisible(true);
            });

            consultaPanel.add(new JLabel("Fecha:"));
            consultaPanel.add(fechaField);
            // CORRECCIÓN: Se vuelve a añadir la etiqueta y el campo de especialidad.
            consultaPanel.add(new JLabel("Especialidad:"));
            consultaPanel.add(espField);
            consultaPanel.add(new JLabel("Doctor:"));
            consultaPanel.add(docField);
            consultaPanel.add(btnResultados);

            jPanelConsultasContainer.add(consultaPanel);
        }
    }

    jPanelConsultasContainer.revalidate();
    jPanelConsultasContainer.repaint();
}
private void limpiarTablaAntecedentes() {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    // Limpia todas las filas
    model.setRowCount(0);
    // Opcional: añade una fila vacía para que no se vea desolada
    model.addRow(new Object[]{"", "", ""});
}
    private informacion_doctor() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField24 = new javax.swing.JTextField();
        jtpinformacionpaciente = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTFNombres = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTFApellidos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTFCedula = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTFFechaNacimiento = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTFCorreo = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jTFAlergias = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jbeditar = new javax.swing.JButton();
        jTFSexo = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTEvolucion = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanelConsultasContainer = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jtfnombres = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jtfapellidos = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jtfcedula = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jftffechadenacimiento = new javax.swing.JFormattedTextField();
        jLabel39 = new javax.swing.JLabel();
        jcbsexo = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        jtfcorreo = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jtfalergias = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jbguardar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jcbbusqueda = new javax.swing.JComboBox<>();
        jtfbusqueda = new javax.swing.JTextField();
        jbbuscar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Nombres");

        jTFNombres.setEditable(false);
        jTFNombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFNombresActionPerformed(evt);
            }
        });

        jLabel2.setText("Apellidos");

        jTFApellidos.setEditable(false);
        jTFApellidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFApellidosActionPerformed(evt);
            }
        });

        jLabel3.setText("Cédula");

        jTFCedula.setEditable(false);
        jTFCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCedulaActionPerformed(evt);
            }
        });

        jLabel4.setText("Fecha de nacimiento");

        jTFFechaNacimiento.setEditable(false);
        jTFFechaNacimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFFechaNacimientoActionPerformed(evt);
            }
        });

        jLabel5.setText("Sexo");

        jLabel6.setText("Correo");

        jTFCorreo.setEditable(false);
        jTFCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCorreoActionPerformed(evt);
            }
        });

        jLabel26.setText("Alergias");

        jTFAlergias.setEditable(false);

        jbeditar.setText("Editar");
        jbeditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbeditarActionPerformed(evt);
            }
        });

        jTFSexo.setEditable(false);
        jTFSexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFSexoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(370, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jbeditar))
                            .addComponent(jTFNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTFApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jTFFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTFCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTFSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTFCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTFAlergias, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel4))
                .addGap(213, 213, 213))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTFNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTFCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTFFechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTFSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTFCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jTFAlergias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(120, 120, 120)
                .addComponent(jbeditar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel42)
                .addContainerGap(321, Short.MAX_VALUE))
        );

        jtpinformacionpaciente.addTab("Información del paciente", jPanel1);

        jPanel6.setAutoscrolls(true);

        jButton2.setText("Agregar evolución");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTEvolucion.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jTEvolucion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Fecha", "Hora", "Doctor", "Especialidad", "Diagnóstico", "Pronóstico"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTEvolucion.setShowHorizontalLines(true);
        jTEvolucion.setShowVerticalLines(true);
        jScrollPane3.setViewportView(jTEvolucion);
        if (jTEvolucion.getColumnModel().getColumnCount() > 0) {
            jTEvolucion.getColumnModel().getColumn(0).setPreferredWidth(80);
            jTEvolucion.getColumnModel().getColumn(1).setPreferredWidth(60);
            jTEvolucion.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTEvolucion.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTEvolucion.getColumnModel().getColumn(4).setPreferredWidth(60);
            jTEvolucion.getColumnModel().getColumn(5).setPreferredWidth(60);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(255, 255, 255)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 878, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(jButton2)
                .addContainerGap(256, Short.MAX_VALUE))
        );

        jtpinformacionpaciente.addTab("Evolución", jPanel6);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Antecedentes"));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Abuelo paterno: cáncer renal", "Hipertensión arterial", "10 horas de sueño"},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                " Familiares", " Patológicos", "Fisiológicos"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Consultas previas"));

        jPanelConsultasContainer.setLayout(new javax.swing.BoxLayout(jPanelConsultasContainer, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(jPanelConsultasContainer);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );

        jtpinformacionpaciente.addTab("Historia clínica", jPanel4);

        jLabel35.setText("Nombres");

        jtfnombres.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfnombresFocusLost(evt);
            }
        });

        jLabel36.setText("Apellidos");

        jtfapellidos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfapellidosFocusLost(evt);
            }
        });

        jLabel37.setText("cédula");

        jtfcedula.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfcedulaFocusLost(evt);
            }
        });

        jLabel38.setText("Fecha de nacimiento");

        jftffechadenacimiento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        jftffechadenacimiento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jftffechadenacimientoFocusLost(evt);
            }
        });

        jLabel39.setText("Sexo");

        jcbsexo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mujer", "Hombre" }));

        jLabel40.setText("Correo");

        jtfcorreo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfcorreoFocusLost(evt);
            }
        });

        jLabel41.setText("Alergias");

        jtfalergias.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jtfalergiasFocusLost(evt);
            }
        });

        jButton5.setText("Cancelar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jbguardar.setText("Guardar");
        jbguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbguardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbguardar)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfcedula, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jtfapellidos)
                                .addComponent(jcbsexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jftffechadenacimiento)
                                .addComponent(jtfcorreo)
                                .addComponent(jtfalergias, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGap(39, 39, 39)
                                    .addComponent(jButton5))
                                .addComponent(jtfnombres, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(112, 112, 112))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jtfcedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfnombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfapellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jftffechadenacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(jcbsexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jtfcorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jtfalergias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jbguardar))
                .addGap(192, 192, 192))
        );

        jtpinformacionpaciente.addTab("Agregar paciente", jPanel5);

        jcbbusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cédula", "Nombre" }));

        jbbuscar.setText("Buscar");
        jbbuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbbuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(219, 219, 219)
                .addComponent(jcbbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbbuscar)
                    .addComponent(jtfbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(150, 150, 150))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfbusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81)
                .addComponent(jbbuscar)
                .addContainerGap(552, Short.MAX_VALUE))
        );

        jtpinformacionpaciente.addTab("Buscar paciente", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtpinformacionpaciente, javax.swing.GroupLayout.PREFERRED_SIZE, 890, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtpinformacionpaciente)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbguardarActionPerformed
 
        this.jtfnombres.setText("");
        this.jtfapellidos.setText("");
        this.jtfcedula.setText("");
        this.jftffechadenacimiento.setText("");
        this.jtfcorreo.setText("");
        this.jtfalergias.setText("");
    }//GEN-LAST:event_jbguardarActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.jtfnombres.setText("");
        this.jtfapellidos.setText("");
        this.jtfcedula.setText("");
        this.jftffechadenacimiento.setText("");
        this.jtfcorreo.setText("");
        this.jtfalergias.setText("");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTFCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCorreoActionPerformed

    private void jTFFechaNacimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFFechaNacimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFFechaNacimientoActionPerformed

    private void jTFCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCedulaActionPerformed

    private void jTFApellidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFApellidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFApellidosActionPerformed

    private void jTFNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFNombresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFNombresActionPerformed

    private void jtfcedulaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfcedulaFocusLost
       String cedula = this.jtfcedula.getText().trim();

    if (cedula == null || !cedula.matches("\\d{10}")) {
        JOptionPane.showMessageDialog(this,
            "Cédula de identidad no válida",
            "Error de validación",
            JOptionPane.ERROR_MESSAGE);
        this.jtfcedula.requestFocus();
        this.jtfcedula.setText("");
        return;
    }
    int provincia = Integer.parseInt(cedula.substring(0, 2));
    int tercerDigito = cedula.charAt(2) - '0';
    if (provincia < 1 || provincia > 24 || tercerDigito >= 6) {
        JOptionPane.showMessageDialog(this,
            "Cédula de identidad no válida",
            "Error de validación",
            JOptionPane.ERROR_MESSAGE);
        this.jtfcedula.requestFocus();
        this.jtfcedula.setText("");
        return;
    }
    int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
    int suma = 0;
    for (int i = 0; i < 9; i++) {
        int digito = cedula.charAt(i) - '0';
        int producto = digito * coeficientes[i];
        suma += (producto > 9) ? producto - 9 : producto;
    }
    int digitoVerificador = (10 - (suma % 10)) % 10;

    if (digitoVerificador != (cedula.charAt(9) - '0')) {
        JOptionPane.showMessageDialog(this,
            "Cédula de identidad no válida",
            "Error de validación",
            JOptionPane.ERROR_MESSAGE);
        this.jtfcedula.requestFocus();
        this.jtfcedula.setText("");
    }
    }//GEN-LAST:event_jtfcedulaFocusLost

    private void jtfnombresFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfnombresFocusLost
       String nombres = this.jtfnombres.getText().trim();
    if (!nombres.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+") || nombres == "") {
        JOptionPane.showMessageDialog(this,
            "Nombre inválido. No debe contener números ni caracteres especiales.",
            "Error de validación", JOptionPane.ERROR_MESSAGE);
        this.jtfnombres.requestFocus();
        this.jtfnombres.setText("");
    }  
    }//GEN-LAST:event_jtfnombresFocusLost

    private void jtfapellidosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfapellidosFocusLost
          String apellidos = this.jtfapellidos.getText().trim();
    if (!apellidos.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+") || apellidos == "") {
        JOptionPane.showMessageDialog(this,
            "Nombre inválido. No debe contener números ni caracteres especiales.",
            "Error de validación", JOptionPane.ERROR_MESSAGE);
        this.jtfapellidos.requestFocus();
        this.jtfapellidos.setText("");
    } 
    }//GEN-LAST:event_jtfapellidosFocusLost

    private void jftffechadenacimientoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jftffechadenacimientoFocusLost
       String fecha = this.jftffechadenacimiento.getText().trim();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setLenient(false);
    try {
        Date date = sdf.parse(fecha);
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this,
            "Fecha de nacimiento inválida. Use el formato YYYY-MM-DD.",
            "Error de validación", JOptionPane.ERROR_MESSAGE);
        this.jftffechadenacimiento.requestFocus();
        this.jftffechadenacimiento.setText("");
    }
    }//GEN-LAST:event_jftffechadenacimientoFocusLost

    private void jtfcorreoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfcorreoFocusLost
       String email = this.jtfcorreo.getText().trim();
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
        JOptionPane.showMessageDialog(this,
            "Correo inválido. Por favor ingrese un formato correo@dominio.com.",
            "Error de validación",
            JOptionPane.ERROR_MESSAGE);
        this.jtfcorreo.requestFocus();
        this.jtfcorreo.setText("");
    }
    }//GEN-LAST:event_jtfcorreoFocusLost

    private void jtfalergiasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jtfalergiasFocusLost
         String alergias = this.jtfalergias.getText().trim();
    if (alergias.matches(".*\\d.*")) {
        JOptionPane.showMessageDialog(this,
            "Alergias inválidas. No debe contener números.",
            "Error de validación",
            JOptionPane.ERROR_MESSAGE);
        this.jtfalergias.requestFocus();
        this.jtfalergias.setText("");
    }
    }//GEN-LAST:event_jtfalergiasFocusLost

    private void jbbuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbbuscarActionPerformed
     String opcion = this.jcbbusqueda.getSelectedItem().toString();

switch (opcion) {
    case "Cédula":
        String ced = this.jtfbusqueda.getText().trim();
        if (!ced.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                "Cédula inválida. Debe contener 10 dígitos numéricos.",
                "Error de validación", JOptionPane.ERROR_MESSAGE);
            break;
        }
        int prov = Integer.parseInt(ced.substring(0, 2));
        int tercer = ced.charAt(2) - '0';
        if (prov < 1 || prov > 24 || tercer >= 6) {
            JOptionPane.showMessageDialog(this,
                "Cédula inválida. Provincia o tercer dígito no válidos.",
                "Error de validación", JOptionPane.ERROR_MESSAGE);
            break;
        }
        int[] coef = {2,1,2,1,2,1,2,1,2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int d = ced.charAt(i) - '0';
            int prod = d * coef[i];
            suma += (prod > 9) ? prod - 9 : prod;
        }
        int dv = (10 - (suma % 10)) % 10;
        if (dv != (ced.charAt(9) - '0')) {
            JOptionPane.showMessageDialog(this,
                "Cédula inválida. Dígito verificador incorrecto.",
                "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
        break;

    case "Nombre":
        String nom = this.jtfbusqueda.getText().trim();
        if (!nom.matches("[A-Za-zÁÉÍÓÚáéíóúÑñ ]+")) {
            JOptionPane.showMessageDialog(this,
                "Nombre inválido. No debe contener números ni caracteres especiales.",
                "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
        break;
        
    }//GEN-LAST:event_jbbuscarActionPerformed

    }
    private void jbeditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbeditarActionPerformed
     vista.editar_paciente editarpaciente = new vista.editar_paciente();
     editarpaciente.setVisible(true);
    }//GEN-LAST:event_jbeditarActionPerformed

    private void jTFSexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFSexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFSexoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        evolucion vistaEvolucion = new evolucion();
        vistaEvolucion.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    
    
    /**
     * @param args the command line arguments
     */

    /**
     * @param args the command line arguments
     */

    /**
     * @param args the command line arguments
     */

    /**
     * @param args the command line arguments
     */
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new informacion_doctor().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelConsultasContainer;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTEvolucion;
    private javax.swing.JTextField jTFAlergias;
    private javax.swing.JTextField jTFApellidos;
    private javax.swing.JTextField jTFCedula;
    private javax.swing.JTextField jTFCorreo;
    private javax.swing.JTextField jTFFechaNacimiento;
    private javax.swing.JTextField jTFNombres;
    private javax.swing.JTextField jTFSexo;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JButton jbbuscar;
    private javax.swing.JButton jbeditar;
    private javax.swing.JButton jbguardar;
    private javax.swing.JComboBox<String> jcbbusqueda;
    private javax.swing.JComboBox<String> jcbsexo;
    private javax.swing.JFormattedTextField jftffechadenacimiento;
    private javax.swing.JTextField jtfalergias;
    private javax.swing.JTextField jtfapellidos;
    private javax.swing.JTextField jtfbusqueda;
    private javax.swing.JTextField jtfcedula;
    private javax.swing.JTextField jtfcorreo;
    private javax.swing.JTextField jtfnombres;
    private javax.swing.JTabbedPane jtpinformacionpaciente;
    // End of variables declaration//GEN-END:variables
}
