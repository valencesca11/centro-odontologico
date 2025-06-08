package com.mycompany.centroodontologico.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EspecialidadFrame extends JFrame {
    private JTable tablaEspecialidades;
    private JButton btnCrear, btnEliminar, btnAtras;

    public EspecialidadFrame() {
        setTitle("OdontSystem - Especialidad");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new BorderLayout());

        // Modelo de tabla
   String[] columnas = {"ID", "Nombre", "Descripción"};
DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return true; // permitir edición directa
    }
};

        tablaEspecialidades = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tablaEspecialidades);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnCrear = new JButton("Crear Especialidad");
        btnEliminar = new JButton("Eliminar Seleccionada");
        btnAtras = new JButton("Atrás");

        panelBotones.add(btnCrear);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnAtras);

        // Agrega componentes
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        add(panel);
    }

    // Getters para controlador
    public JTable getTablaEspecialidades() {
        return tablaEspecialidades;
    }

    public JButton getBtnCrear() {
        return btnCrear;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnAtras() {
        return btnAtras;
    }
}
