package com.mycompany.centroodontologico.controlador;

import com.mycompany.centroodontologico.modelo.Odontologo;
import com.mycompany.centroodontologico.modelo.OdontologoDetalleModelo;
import com.mycompany.centroodontologico.vista.OdontologoDetalleDialog;

import javax.swing.*;

public class OdontologoDetalleControlador {
    private final OdontologoDetalleModelo modelo;
    private final OdontologoDetalleDialog vista;
    private final OdontologoControlador padre;

    public OdontologoDetalleControlador(OdontologoDetalleModelo modelo, OdontologoDetalleDialog vista, OdontologoControlador padre) {
        this.modelo = modelo;
        this.vista = vista;
        this.padre = padre;
        inicializarVista();
        inicializarEventos();
    }

    private void inicializarVista() {
        vista.cargarDatos(modelo);
    }

    private void inicializarEventos() {
        vista.btnBorrar.addActionListener(e -> borrarOdontologo());
        vista.btnAtras.addActionListener(e -> vista.dispose());
    }

    private void borrarOdontologo() {
        int confirmacion = JOptionPane.showConfirmDialog(
                vista,
                "¿Estás seguro de que deseas eliminar este odontólogo?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            // CORRECCIÓN AQUÍ: pasar String, no int
            boolean exito = Odontologo.eliminarPorDni(modelo.getDni());
            if (exito) {
                JOptionPane.showMessageDialog(vista, "Odontólogo eliminado correctamente.");
                vista.dispose();
                padre.actualizarTabla(); // Actualiza la tabla principal
            } else {
                JOptionPane.showMessageDialog(vista, "Error al eliminar el odontólogo.");
            }
        }
    }

    public void mostrar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }
}
