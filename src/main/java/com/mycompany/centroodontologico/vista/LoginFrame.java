package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.controlador.LoginControlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciar;

    public LoginFrame() {
        setTitle("OdontSystem - Inicio de sesión");
        setSize(350, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lblUsuario = new JLabel("Usuario:");
        txtUsuario = new JTextField(15);

        JLabel lblContrasena = new JLabel("Contraseña:");
        txtContrasena = new JPasswordField(15);

        btnIniciar = new JButton("Iniciar sesión");

        panel.add(lblUsuario, gbc);
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblContrasena, gbc);
        gbc.gridx = 1;
        panel.add(txtContrasena, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnIniciar, gbc);

        // Acción al presionar el botón o Enter
        Action iniciarSesion = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginControlador().validarLogin(
                    txtUsuario.getText(),
                    new String(txtContrasena.getPassword())
                );
            }
        };

        btnIniciar.addActionListener(iniciarSesion);
        txtContrasena.addActionListener(iniciarSesion); // Enter desde contraseña
        txtUsuario.addActionListener(iniciarSesion);    // Enter desde usuario

        add(panel);
    }
}
