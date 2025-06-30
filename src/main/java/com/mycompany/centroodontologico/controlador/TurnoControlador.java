package com.mycompany.centroodontologico.controlador;

import com.mycompany.centroodontologico.vista.TurnoFrame;
import com.mycompany.centroodontologico.vista.TurnoRegistrarDialog;
import com.mycompany.centroodontologico.vista.TurnoModificarDialog;
import com.mycompany.centroodontologico.modelo.TurnoModelo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class TurnoControlador implements ActionListener {

    private TurnoFrame vista;
    private TurnoModelo modelo;
    private MenuAdministracionControlador menuControlador;

    public TurnoControlador(MenuAdministracionControlador menuControlador) {
        this.modelo = new TurnoModelo();
        this.menuControlador = menuControlador;
        this.vista = new TurnoFrame();
        agregarEventos();
        vista.setVisible(true);

        if (this.menuControlador != null && this.menuControlador.getVista() != null) {
            this.menuControlador.getVista().setVisible(false);
        }
    }

    private void agregarEventos() {
        vista.getBtnRegistrar().addActionListener(this);
        vista.getBtnModificar().addActionListener(this);
        vista.getBtnBorrar().addActionListener(this);
        vista.getBtnAtras().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == vista.getBtnRegistrar()) {
            TurnoRegistrarDialog dialog = new TurnoRegistrarDialog(vista);
            dialog.setLocationRelativeTo(vista);
            dialog.setModal(true);
            dialog.setVisible(true);

            if (dialog.isTurnoGuardado()) {
                JOptionPane.showMessageDialog(vista, "Turno registrado correctamente.");
                vista.cargarTurnosEnTabla();
            }

        } else if (source == vista.getBtnModificar()) {
            int filaSeleccionada = vista.getTablaTurnos().getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccioná un turno para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int filaModelo = vista.getTablaTurnos().convertRowIndexToModel(filaSeleccionada);

            String odontologo = (String) vista.getTablaTurnos().getModel().getValueAt(filaModelo, 0);
            String dniPaciente = (String) vista.getTablaTurnos().getModel().getValueAt(filaModelo, 2);
            String fecha = (String) vista.getTablaTurnos().getModel().getValueAt(filaModelo, 4);
            String hora = (String) vista.getTablaTurnos().getModel().getValueAt(filaModelo, 5);

            TurnoModificarDialog dialog = new TurnoModificarDialog(vista, odontologo, dniPaciente, fecha, hora);
            dialog.setLocationRelativeTo(vista);
            dialog.setModal(true);
            dialog.setVisible(true);

            if (dialog.isTurnoModificado()) {
                JOptionPane.showMessageDialog(vista, "Turno modificado correctamente.");
                vista.cargarTurnosEnTabla();
            }

        } else if (source == vista.getBtnBorrar()) {
            int filaSeleccionada = vista.getTablaTurnos().getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccioná un turno para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int filaModelo = vista.getTablaTurnos().convertRowIndexToModel(filaSeleccionada);

            String odontologo = (String) vista.getTablaTurnos().getModel().getValueAt(filaModelo, 0);
            String dniPaciente = (String) vista.getTablaTurnos().getModel().getValueAt(filaModelo, 2);
            String fecha = (String) vista.getTablaTurnos().getModel().getValueAt(filaModelo, 4);
            String hora = (String) vista.getTablaTurnos().getModel().getValueAt(filaModelo, 5);

            int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Seguro que querés eliminar este turno?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean exito = modelo.eliminarTurno(dniPaciente, odontologo, fecha, hora);
                if (exito) {
                    JOptionPane.showMessageDialog(vista, "Turno eliminado con éxito.");
                    vista.cargarTurnosEnTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al eliminar el turno.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (source == vista.getBtnAtras()) {
            vista.dispose(); // Cierra TurnoFrame
            if (menuControlador != null && menuControlador.getVista() != null) {
                menuControlador.getVista().setVisible(true);
            } else {
                new MenuAdministracionControlador();
            }
        }
    }

    public TurnoFrame getVista() {
        return vista;
    }
}
