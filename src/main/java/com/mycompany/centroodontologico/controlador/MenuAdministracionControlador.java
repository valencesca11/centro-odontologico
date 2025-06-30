package com.mycompany.centroodontologico.controlador;

import com.mycompany.centroodontologico.vista.MenuAdministracionFrame;
import com.mycompany.centroodontologico.controlador.EspecialidadControlador;
import com.mycompany.centroodontologico.controlador.OdontologoControlador;
import com.mycompany.centroodontologico.controlador.PacienteControlador;
import com.mycompany.centroodontologico.controlador.TurnoControlador;
import com.mycompany.centroodontologico.controlador.PagoControlador; // ⬅️ Importación agregada

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAdministracionControlador implements ActionListener {

    private MenuAdministracionFrame vista;

    public MenuAdministracionControlador() {
        vista = new MenuAdministracionFrame();
        vista.agregarEventos(this);
        vista.setVisible(true);
    }

    public MenuAdministracionFrame getVista() {
        return vista;
    }

    public void mostrar() {
        if (vista == null || !vista.isDisplayable()) {
            vista = new MenuAdministracionFrame();
            vista.agregarEventos(this);
        }
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == vista.getBtnEspecialidad()) {
            new EspecialidadControlador();
            vista.dispose();
        }

        else if (source == vista.getBtnOdontologo()) {
            new OdontologoControlador();
            vista.dispose();
        }

        else if (source == vista.getBtnPacientes()) {
            new PacienteControlador(vista);
            vista.dispose();
        }

        else if (source == vista.getBtnTurnos()) {
            new TurnoControlador(this);
            vista.setVisible(false);
        }

        else if (source == vista.getBtnPagos()) {
            new PagoControlador(); // ✅ Llama al controlador que abre la interfaz de pagos
        }

        else if (source == vista.getBtnCerrarSesion()) {
            vista.dispose();
        }
    }
}
