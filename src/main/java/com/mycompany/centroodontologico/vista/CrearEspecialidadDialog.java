package com.mycompany.centroodontologico.vista;

import javax.swing.*;
import java.awt.*;

public class CrearEspecialidadDialog extends JDialog {

    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public CrearEspecialidadDialog(JFrame parent) {
        super(parent, "Especialidades - Crear Esp", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridLayout(4, 1, 10, 5));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        txtNombre = new JTextField();
        txtDescripcion = new JTextArea(3, 20);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcion);

        panelCampos.add(new JLabel("Nombre:"));
        panelCampos.add(txtNombre);
        panelCampos.add(new JLabel("Descripción:"));
        panelCampos.add(scrollDesc);

        JPanel panelBotones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Validación al cerrar sin guardar
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                confirmarSalida();
            }
        });

        btnCancelar.addActionListener(e -> confirmarSalida());
    }

 private void confirmarSalida() {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "¿Estás seguro que quieres salir sin guardar?",
        "Confirmación",
        JOptionPane.YES_NO_OPTION
    );
    if (confirm == JOptionPane.YES_OPTION) {
        dispose(); // solo cerrás el diálogo
    }
}

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextArea getTxtDescripcion() { return txtDescripcion; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
