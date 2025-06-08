package com.mycompany.centroodontologico.controlador;

import com.mycompany.centroodontologico.modelo.Paciente;
import com.mycompany.centroodontologico.modelo.PacienteModelo;
import com.mycompany.centroodontologico.vista.MenuAdministracionFrame;
import com.mycompany.centroodontologico.vista.PacienteDetalleDialog;
import com.mycompany.centroodontologico.vista.PacienteGestionFrame;
import com.mycompany.centroodontologico.vista.PacienteRegistrarDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PacienteControlador implements ActionListener {

    private PacienteGestionFrame pacienteVista;
    private MenuAdministracionFrame menuPrincipal;

    public PacienteControlador(MenuAdministracionFrame menuPrincipal) {
        this.menuPrincipal = menuPrincipal;
        abrirVistaPacientes();
    }

    private void abrirVistaPacientes() {
        pacienteVista = new PacienteGestionFrame();
        pacienteVista.setVisible(true);
        pacienteVista.agregarEventos(this);
        menuPrincipal.setVisible(false);
        llenarTablaPacientes();
    }

    private void llenarTablaPacientes() {
        Paciente pacienteDAO = new Paciente();
        List<PacienteModelo> pacientes = pacienteDAO.obtenerPacientes();

        DefaultTableModel modelo = (DefaultTableModel) pacienteVista.getTablaPacientes().getModel();
        modelo.setRowCount(0);

        for (PacienteModelo p : pacientes) {
            modelo.addRow(new Object[]{
                p.getDni(),
                p.getNombre(),
                p.getApellido(),
                p.getFechaNacimiento(),
                p.getObraSocial(),
                p.getNumeroAfiliado(),
                p.getGenero(),
                p.getTelefono(),
                p.getEmail()
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();

        if (fuente == pacienteVista.getBtnAtras()) {
            pacienteVista.dispose();
            menuPrincipal.setVisible(true);
        }

        if (fuente == pacienteVista.getBtnRegistrar()) {
            PacienteRegistrarDialog dialog = new PacienteRegistrarDialog(pacienteVista);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                PacienteModelo nuevo = dialog.getPaciente();
                Paciente pacienteDAO = new Paciente();
                pacienteDAO.registrarPaciente(nuevo);
                llenarTablaPacientes();
            }
        }

        if (fuente == pacienteVista.getBtnVerDetalles()) {
            int filaSeleccionada = pacienteVista.getTablaPacientes().getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(pacienteVista, "Seleccioná un paciente para ver detalles.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            DefaultTableModel modelo = (DefaultTableModel) pacienteVista.getTablaPacientes().getModel();
            String dni = modelo.getValueAt(filaSeleccionada, 0).toString();

            // En lugar de obtener sólo el paciente con los datos de la tabla, ahora obtenemos todos los datos
            PacienteModelo paciente = PacienteModelo.obtenerPorDni(dni); // Aquí obtenemos todos los datos del paciente

            if (paciente != null) {
                // Pasa el paciente completo al detalle del paciente
                PacienteDetalleDialog dialog = new PacienteDetalleDialog(pacienteVista, paciente);
                dialog.setVisible(true);

                if (dialog.isDatosActualizados()) {
                    llenarTablaPacientes(); // Actualiza la tabla si se guardaron cambios
                }
            } else {
                JOptionPane.showMessageDialog(pacienteVista, "No se pudo encontrar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
