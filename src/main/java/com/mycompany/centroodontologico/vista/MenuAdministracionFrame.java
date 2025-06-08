package com.mycompany.centroodontologico.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuAdministracionFrame extends JFrame {

    private JButton btnTurnos;
    private JButton btnOdontologo;
    private JButton btnPacientes;
    private JButton btnPagos;
    private JButton btnEspecialidad;
    private JButton btnCerrarSesion;

    public MenuAdministracionFrame() {
        setTitle("OdontSystem / Menú / Administración");
        setSize(300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // ✅ Cambiado a DISPOSE para permitir reapertura
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnTurnos = crearBoton("TURNOS");
        btnOdontologo = crearBoton("ODONTÓLOGO");
        btnPacientes = crearBoton("PACIENTES");
        btnPagos = crearBoton("PAGOS");
        btnEspecialidad = crearBoton("ESPECIALIDAD");

        btnCerrarSesion = new JButton("CERRAR SESIÓN");
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(200, 35));
        btnCerrarSesion.setBackground(new Color(255, 102, 102));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setFont(new Font("Arial", Font.BOLD, 12));

        int espacio = 10;
        panel.add(btnTurnos);
        panel.add(Box.createVerticalStrut(espacio));
        panel.add(btnOdontologo);
        panel.add(Box.createVerticalStrut(espacio));
        panel.add(btnPacientes);
        panel.add(Box.createVerticalStrut(espacio));
        panel.add(btnPagos);
        panel.add(Box.createVerticalStrut(espacio));
        panel.add(btnEspecialidad);
        panel.add(Box.createVerticalStrut(espacio * 3));
        panel.add(btnCerrarSesion);

        add(panel);
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 35));
        boton.setFocusPainted(false);
        return boton;
    }

    // Métodos públicos para controladores
    public void agregarEventos(ActionListener listener) {
        btnTurnos.addActionListener(listener);
        btnOdontologo.addActionListener(listener);
        btnPacientes.addActionListener(listener);
        btnPagos.addActionListener(listener);
        btnEspecialidad.addActionListener(listener);
        btnCerrarSesion.addActionListener(listener);
    }

    public JButton getBtnTurnos() { return btnTurnos; }
    public JButton getBtnOdontologo() { return btnOdontologo; }
    public JButton getBtnPacientes() { return btnPacientes; }
    public JButton getBtnPagos() { return btnPagos; }
    public JButton getBtnEspecialidad() { return btnEspecialidad; }
    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
} 
