package com.mycompany.centroodontologico.controlador;

import com.mycompany.centroodontologico.vista.EspecialidadFrame;
import com.mycompany.centroodontologico.vista.CrearEspecialidadDialog;
import com.mycompany.centroodontologico.conexion.conexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class EspecialidadControlador {
    private EspecialidadFrame vista;

    public EspecialidadControlador() {
        mostrar();
    }

    public void mostrar() {
        if (vista == null || !vista.isDisplayable()) {
            vista = new EspecialidadFrame();

            vista.getBtnCrear().addActionListener(e -> abrirFormularioCrear());
            vista.getBtnEliminar().addActionListener(e -> eliminarEspecialidad());
            vista.getBtnAtras().setText("Atrás");
            vista.getBtnAtras().addActionListener(e -> {
                vista.dispose();
                new com.mycompany.centroodontologico.controlador.MenuAdministracionControlador().mostrar();
            });
        }

        cargarEspecialidadesDesdeBD();
        vista.setVisible(true);
    }

    private void cargarEspecialidadesDesdeBD() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTablaEspecialidades().getModel();
        modelo.setRowCount(0);

        try (Connection conn = conexionBD.conectar()) {
            String sql = "SELECT EspecialidadID, Nombre, Descripcion FROM Especialidad";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("EspecialidadID");
                String nombre = rs.getString("Nombre");
                String descripcion = rs.getString("Descripcion");
                modelo.addRow(new Object[]{id, nombre, descripcion});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar especialidades: " + e.getMessage());
        }
    }

    private void abrirFormularioCrear() {
        CrearEspecialidadDialog dialog = new CrearEspecialidadDialog(vista);
        dialog.getBtnGuardar().addActionListener(ev -> {
            String nombre = dialog.getTxtNombre().getText().trim();
            String descripcion = dialog.getTxtDescripcion().getText().trim();

            if (nombre.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nombre y descripción no pueden estar vacíos.");
                return;
            }

            try (Connection conn = conexionBD.conectar()) {
                String verificar = "SELECT COUNT(*) FROM Especialidad WHERE Nombre = ?";
                PreparedStatement ps = conn.prepareStatement(verificar);
                ps.setString(1, nombre);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(dialog, "Ya existe una especialidad con ese nombre.");
                    return;
                }

                String insertar = "INSERT INTO Especialidad (Nombre, Descripcion) VALUES (?, ?)";
                PreparedStatement insert = conn.prepareStatement(insertar);
                insert.setString(1, nombre);
                insert.setString(2, descripcion);
                insert.executeUpdate();

                JOptionPane.showMessageDialog(dialog, "Especialidad creada correctamente.");
                dialog.dispose();
                mostrar(); // recarga la ventana actual sin duplicar

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error al guardar: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void eliminarEspecialidad() {
        int filaSeleccionada = vista.getTablaEspecialidades().getSelectedRow();
        if (filaSeleccionada != -1) {
            DefaultTableModel modelo = (DefaultTableModel) vista.getTablaEspecialidades().getModel();
            Object idObj = modelo.getValueAt(filaSeleccionada, 0);

            if (idObj == null || idObj.toString().isBlank()) {
                JOptionPane.showMessageDialog(vista, "Esta fila no tiene un ID válido. No se puede eliminar de la base de datos.");
                return;
            }

            int especialidadID;
            try {
                especialidadID = Integer.parseInt(idObj.toString());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(vista, "El ID no es un número válido.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Seguro que querés eliminar esta especialidad?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                try (Connection conn = conexionBD.conectar()) {
                    String sql = "DELETE FROM Especialidad WHERE EspecialidadID = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, especialidadID);

                    int filasAfectadas = stmt.executeUpdate();

                    if (filasAfectadas > 0) {
                        modelo.removeRow(filaSeleccionada);
                        JOptionPane.showMessageDialog(vista, "Especialidad eliminada.");
                    } else {
                        JOptionPane.showMessageDialog(vista, "No se encontró la especialidad en la base de datos.");
                    }

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(vista, "Error al eliminar especialidad: " + e.getMessage());
                }
            }

        } else {
            JOptionPane.showMessageDialog(vista, "Seleccioná una fila para eliminar.");
        }
    }
}
