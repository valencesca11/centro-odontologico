package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.controlador.OdontologoControlador;
import com.mycompany.centroodontologico.modelo.OdontologoDetalleModelo;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class OdontologoRegistrarDialog extends JDialog {

    private JTextField txtDni, txtNombre, txtApellido, txtTelefono, txtEmail, txtMatricula;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JRadioButton rbtnMasculino, rbtnFemenino;
    private ButtonGroup generoGroup;
    private JFormattedTextField txtFechaNacimiento;
    private JPanel panelEspecialidades;
    private JButton btnGuardar, btnCancelar;
    private final OdontologoControlador controlador;
    private JTable tablaHorarios;
    private OdontologoGuardadoListener listener;

    public void setOdontologoGuardadoListener(OdontologoGuardadoListener listener) {
        this.listener = listener;
    }

    public OdontologoRegistrarDialog(JFrame parent, OdontologoControlador controlador, List<String> especialidadesDisponibles) {
        super(parent, "Registrar Nuevo Odontólogo", true);
        this.controlador = controlador;

        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("DNI:"), gbc);
        gbc.gridx = 1; txtDni = new JTextField(20); panelPrincipal.add(txtDni, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = new JTextField(20); panelPrincipal.add(txtNombre, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1; txtApellido = new JTextField(20); panelPrincipal.add(txtApellido, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; txtTelefono = new JTextField(20); panelPrincipal.add(txtTelefono, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; txtEmail = new JTextField(20); panelPrincipal.add(txtEmail, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Fecha de nacimiento:"), gbc);
        gbc.gridx = 1;
        txtFechaNacimiento = new JFormattedTextField(java.text.DateFormat.getDateInstance());
        txtFechaNacimiento.setValue(new Date());
        txtFechaNacimiento.setColumns(20);
        panelPrincipal.add(txtFechaNacimiento, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1;
        JPanel generoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbtnMasculino = new JRadioButton("Masculino");
        rbtnFemenino = new JRadioButton("Femenino");
        generoGroup = new ButtonGroup();
        generoGroup.add(rbtnMasculino);
        generoGroup.add(rbtnFemenino);
        generoPanel.add(rbtnMasculino);
        generoPanel.add(rbtnFemenino);
        panelPrincipal.add(generoPanel, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 1; txtMatricula = new JTextField(20); panelPrincipal.add(txtMatricula, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1; txtUsuario = new JTextField(20); panelPrincipal.add(txtUsuario, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; txtContrasena = new JPasswordField(20); panelPrincipal.add(txtContrasena, gbc); y++;

        // Botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardarNuevoOdontologo());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(650, 750));
        pack();
        setLocationRelativeTo(parent);
    }

    private void guardarNuevoOdontologo() {
        OdontologoDetalleModelo nuevo = new OdontologoDetalleModelo();

        String dniTexto = txtDni.getText().trim();
        if (dniTexto.isEmpty() || !dniTexto.matches("\\d+")) {
            mostrarError("DNI inválido. Debe contener solo números.");
            return;
        }

        try {
            nuevo.setDni(Integer.parseInt(dniTexto));
        } catch (NumberFormatException e) {
            mostrarError("DNI inválido. Debe ser un número válido.");
            return;
        }

        if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()) {
            mostrarError("Nombre y Apellido son obligatorios.");
            return;
        }

        if (!txtTelefono.getText().trim().matches("\\d+")) {
            mostrarError("Teléfono inválido. Debe contener solo números.");
            return;
        }

        if (!Pattern.matches("^\\S+@\\S+\\.\\S+$", txtEmail.getText().trim())) {
            mostrarError("Email inválido.");
            return;
        }

        nuevo.setNombre(txtNombre.getText().trim());
        nuevo.setApellido(txtApellido.getText().trim());
        nuevo.setTelefono(txtTelefono.getText().trim());
        nuevo.setEmail(txtEmail.getText().trim());
        nuevo.setFechaNacimiento((Date) txtFechaNacimiento.getValue());
        nuevo.setMatricula(txtMatricula.getText().trim());
        nuevo.setUsuario(txtUsuario.getText().trim());
        nuevo.setContrasenia(new String(txtContrasena.getPassword()).trim());

        boolean exito = controlador.crearOdontologo(nuevo);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Odontólogo registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (listener != null) listener.odontologoGuardado();
            dispose();
        } else {
            mostrarError("Error al registrar odontólogo. Verifica los datos.");
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
