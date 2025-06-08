package com.mycompany.centroodontologico.controlador;

import com.mycompany.centroodontologico.vista.MenuAdministracionFrame;
import com.mycompany.centroodontologico.controlador.EspecialidadControlador;
import com.mycompany.centroodontologico.controlador.OdontologoControlador;
import com.mycompany.centroodontologico.controlador.PacienteControlador;

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

    // Método mostrar() que puede ser llamado desde otros controladores
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
            vista.dispose(); // Cierra el menú actual
        }

        if (source == vista.getBtnOdontologo()) {
            new OdontologoControlador();
            vista.dispose();
        }

        if (source == vista.getBtnPacientes()) {
            new PacienteControlador(vista); // ✅ Corregido: no pasamos vista si no hace falta
            vista.dispose();
        }

        if (source == vista.getBtnTurnos()) {
            JOptionPane.showMessageDialog(vista, "Funcionalidad de Turnos aún no implementada.");
        }

        if (source == vista.getBtnPagos()) {
            JOptionPane.showMessageDialog(vista, "Funcionalidad de Pagos aún no implementada.");
        }

        if (source == vista.getBtnCerrarSesion()) {
            vista.dispose();
            // Aquí podrías redirigir al login si lo tienes implementado
        }
    }
}
