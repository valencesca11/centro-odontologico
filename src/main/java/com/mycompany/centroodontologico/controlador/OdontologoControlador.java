package com.mycompany.centroodontologico.controlador;

import com.mycompany.centroodontologico.vista.OdontologoFrame;
import com.mycompany.centroodontologico.vista.OdontologoRegistrarDialog;
import com.mycompany.centroodontologico.vista.OdontologoDetalleDialog; // Se agregó la importación
import com.mycompany.centroodontologico.modelo.OdontologoDetalleModelo;
import com.mycompany.centroodontologico.modelo.Odontologo;
import com.mycompany.centroodontologico.conexion.conexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List; // Importación correcta
import java.util.regex.Pattern;

public class OdontologoControlador {
    private OdontologoFrame vista;

    public OdontologoControlador() {
        vista = new OdontologoFrame(this, new ArrayList<>()); // Pasamos una lista vacía como segundo parámetro
        cargarOdontologos(null, null);
        agregarEventos();
        vista.setVisible(true);
    }

    private void agregarEventos() {
        vista.getBtnAtras().addActionListener(e -> {
            vista.dispose();
            new MenuAdministracionControlador();
        });

        vista.getCbEspecialidad().addActionListener(e -> filtrar());

        vista.getTxtBuscar().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrar();
            }
        });

        vista.getBtnInfo().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(vista,
                        "Podés buscar por nombre, apellido o DNI del odontólogo.");
            }
        });

        vista.getBtnDetalles().addActionListener(e -> mostrarDetallesOdontologoSeleccionado());

        vista.getBtnRegistrar().addActionListener(e -> {
            OdontologoRegistrarDialog registrarDialog = new OdontologoRegistrarDialog(vista, this);
            registrarDialog.setOdontologoGuardadoListener(this::actualizarTabla);
            registrarDialog.setVisible(true);
        });
    }

    private void mostrarDetallesOdontologoSeleccionado() {
        int filaSeleccionada = vista.getTabla().getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona un odontólogo para ver sus detalles.");
            return;
        }

        String dni = vista.getTabla().getValueAt(filaSeleccionada, 3).toString();
        OdontologoDetalleModelo modelo = obtenerDetallePorDni(dni);

        if (modelo != null) {
            OdontologoDetalleDialog dialogo = new OdontologoDetalleDialog(vista, this);
            OdontologoDetalleControlador controlador = new OdontologoDetalleControlador(modelo, dialogo, this);
            controlador.mostrar();
        } else {
            JOptionPane.showMessageDialog(vista,
                    "No se encontraron detalles para el odontólogo con DNI: " + dni);
        }
    }

    private void filtrar() {
        String especialidad = (String) vista.getCbEspecialidad().getSelectedItem();
        String filtro = vista.getTxtBuscar().getText().trim();
        cargarOdontologos(especialidad, filtro);
    }

    public void cargarOdontologos(String especialidad, String filtro) {
        DefaultTableModel modelo = vista.getModeloTabla();
        modelo.setRowCount(0);

        String sql = """
            SELECT e.Nombre AS Especialidad, p.Nombre, p.Apellido, o.DNI, h.Dia,
                   CONCAT(DATE_FORMAT(h.HoraInicio, '%H:%i'), ' a ', DATE_FORMAT(h.HoraFin, '%H:%i')) AS Horario
            FROM Odontologo o
            JOIN Persona p ON o.DNI = p.DNI
            JOIN HorarioOdontologo h ON o.DNI = h.OdontologoDNI
            JOIN Especialidad e ON e.OdontologoDNI = o.DNI
            WHERE (? IS NULL OR e.Nombre = ?)
              AND (? IS NULL OR p.Nombre LIKE ? OR p.Apellido LIKE ? OR o.DNI LIKE ?)
        """;  // Query corregido para usar el filtro de manera más flexible.

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            boolean filtroEspecialidad = especialidad != null && !especialidad.isEmpty();
            boolean filtroTexto = filtro != null && !filtro.isEmpty();

            ps.setString(1, filtroEspecialidad ? especialidad : null);
            ps.setString(2, filtroEspecialidad ? especialidad : null);
            ps.setString(3, filtroTexto ? filtro : null);
            ps.setString(4, "%" + filtro + "%");
            ps.setString(5, "%" + filtro + "%");
            ps.setString(6, "%" + filtro + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelo.addRow(new Object[] {
                        rs.getString("Especialidad"),
                        rs.getString("Nombre"),
                        rs.getString("Apellido"),
                        rs.getInt("DNI"),
                        rs.getString("Dia"),
                        rs.getString("Horario")
                    });
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista,
                    "Error cargando odontólogos: " + e.getMessage());
        }
    }

    private OdontologoDetalleModelo obtenerDetallePorDni(String dni) {
        try {
            int dniInt = Integer.parseInt(dni.trim());  // Conversión segura de String a int
            return Odontologo.obtenerDetalle(dniInt);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "DNI inválido.");
            return null;
        }
    }

    public void actualizarTabla() {
        vista.getCbEspecialidad().setSelectedItem(null);
        vista.getTxtBuscar().setText("");
        cargarOdontologos(null, null);
    }

    public boolean crearOdontologo(OdontologoDetalleModelo odontologo) {
        return Odontologo.insertarOdontologo(odontologo);
    }

    public boolean actualizarOdontologo(OdontologoDetalleModelo modelo) {
        if (!validarFormatoEmail(modelo.getEmail())) {
            JOptionPane.showMessageDialog(vista, "El formato del correo electrónico no es válido.");
            return false;
        }

        if (emailExisteParaOtro(modelo.getDni(), modelo.getEmail())) {
            JOptionPane.showMessageDialog(vista, "El correo electrónico ya está registrado para otro odontólogo.");
            return false;
        }

        return Odontologo.actualizarOdontologo(modelo);
    }

    private boolean validarFormatoEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }

    private boolean emailExisteParaOtro(int dniActual, String email) {
        String query = "SELECT 1 FROM Persona WHERE Email = ? AND DNI != ?";
        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setInt(2, dniActual);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
}
