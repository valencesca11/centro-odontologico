package com.mycompany.centroodontologico.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.mycompany.centroodontologico.modelo.PacienteModelo;
import java.util.List;

public class PacienteGestionFrame extends JFrame {

    private JTextField txtBuscarDni;
    private JTable tablaPacientes;
    private JButton btnRegistrar;
    private JButton btnVerDetalles;
    private JButton btnAtras;
    private DefaultTableModel modeloTabla;

    public PacienteGestionFrame() {
        setTitle("Gestión de Pacientes");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        agregarEventosInternos();
        cargarPacientesEnTabla();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Panel superior (búsqueda)
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBuscar = new JLabel("Buscar por DNI:");
        txtBuscarDni = new JTextField(15);

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscarDni);
        add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de pacientes
        String[] columnas = {"DNI", "Nombre", "Apellido", "Fecha de nacimiento", "Obra Social", "Número de afil.", "Género", "Teléfono", "Email"};
        modeloTabla = new DefaultTableModel(null, columnas);
        tablaPacientes = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaPacientes);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior (botones)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnRegistrar = new JButton("Registrar Paciente");
        btnVerDetalles = new JButton("Ver Detalles");
        btnAtras = new JButton("Atrás");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnAtras);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarEventosInternos() {
        // Filtro automático al escribir en txtBuscarDni
        txtBuscarDni.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filtrarPorDni();
            }

            public void removeUpdate(DocumentEvent e) {
                filtrarPorDni();
            }

            public void changedUpdate(DocumentEvent e) {
                filtrarPorDni();
            }
        });

        btnVerDetalles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fila = tablaPacientes.getSelectedRow();
                if (fila != -1) {
                    PacienteModelo paciente = obtenerPacienteDesdeFila(fila);
                    PacienteDetalleDialog dialog = new PacienteDetalleDialog(PacienteGestionFrame.this, paciente);
                    dialog.mostrar();

                    if (dialog.isDatosActualizados()) {
                        System.out.println("Datos del paciente actualizados, recargando tabla...");
                        cargarPacientesEnTabla();
                    }
                } else {
                    JOptionPane.showMessageDialog(PacienteGestionFrame.this, "Seleccioná un paciente para ver los detalles.");
                }
            }
        });
    }

    private void filtrarPorDni() {
        String texto = txtBuscarDni.getText().trim();
        modeloTabla.setRowCount(0);
        List<PacienteModelo> pacientes = PacienteModelo.obtenerTodos();

        for (PacienteModelo p : pacientes) {
            if (texto.isEmpty() || String.valueOf(p.getDni()).startsWith(texto)) {
                modeloTabla.addRow(new Object[]{
                        p.getDni(),
                        p.getNombre(),
                        p.getApellido(),
                        p.getFechaNacimientoStr(),
                        p.getObraSocial(),
                        p.getNumeroAfiliado(),
                        p.getGenero(),
                        p.getTelefono(),
                        p.getEmail()
                });
            }
        }
    }

    private PacienteModelo obtenerPacienteDesdeFila(int fila) {
        PacienteModelo paciente = new PacienteModelo();
        paciente.setDni(Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString()));
        paciente.setNombre(modeloTabla.getValueAt(fila, 1).toString());
        paciente.setApellido(modeloTabla.getValueAt(fila, 2).toString());
        paciente.setFechaNacimientoStr(modeloTabla.getValueAt(fila, 3).toString());
        paciente.setObraSocial(modeloTabla.getValueAt(fila, 4).toString());
        paciente.setNumeroAfiliado(modeloTabla.getValueAt(fila, 5).toString());
        paciente.setGenero(modeloTabla.getValueAt(fila, 6).toString());
        paciente.setTelefono(modeloTabla.getValueAt(fila, 7).toString());
        paciente.setEmail(modeloTabla.getValueAt(fila, 8).toString());
        return paciente;
    }

    public void cargarPacientesEnTabla() {
        modeloTabla.setRowCount(0);
        List<PacienteModelo> pacientes = PacienteModelo.obtenerTodos();

        for (PacienteModelo p : pacientes) {
            modeloTabla.addRow(new Object[]{
                    p.getDni(),
                    p.getNombre(),
                    p.getApellido(),
                    p.getFechaNacimientoStr(),
                    p.getObraSocial(),
                    p.getNumeroAfiliado(),
                    p.getGenero(),
                    p.getTelefono(),
                    p.getEmail()
            });
        }

        System.out.println("Tabla de pacientes recargada. Total: " + pacientes.size());
    }

    public String getDniBuscado() {
        return txtBuscarDni.getText();
    }

    public JTable getTablaPacientes() {
        return tablaPacientes;
    }

    public void agregarEventos(ActionListener listener) {
        btnRegistrar.addActionListener(listener);
        btnAtras.addActionListener(listener);
    }

    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnVerDetalles() {
        return btnVerDetalles;
    }

    public JButton getBtnAtras() {
        return btnAtras;
    }
}
