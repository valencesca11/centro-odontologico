package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.modelo.PacienteModelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PacienteRegistrarDialog extends JDialog {

    private boolean confirmado = false;
    private PacienteModelo paciente;

    private JTextField txtDni, txtNombre, txtApellido, txtFechaNacimiento, txtObraSocial, txtNroAfiliado, txtTelefono, txtEmail;
    private JRadioButton rbFemenino, rbMasculino;
    private ButtonGroup grupoGenero;
    private JButton btnGuardar, btnCancelar;

    public PacienteRegistrarDialog(JFrame parent) {
        super(parent, "Registrar Paciente", true);
        setSize(600, 650);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtDni = new JTextField();
        txtNombre = new JTextField();
        txtApellido = new JTextField();
        txtFechaNacimiento = new JTextField();
        txtObraSocial = new JTextField();
        txtNroAfiliado = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();

        rbFemenino = new JRadioButton("Femenino");
        rbMasculino = new JRadioButton("Masculino");
        grupoGenero = new ButtonGroup();
        grupoGenero.add(rbFemenino);
        grupoGenero.add(rbMasculino);

        int fila = 0;
        addLabelAndField(panelCampos, "DNI", txtDni, gbc, fila++);
        addLabelAndField(panelCampos, "Nombre", txtNombre, gbc, fila++);
        addLabelAndField(panelCampos, "Apellido", txtApellido, gbc, fila++);
        addLabelAndField(panelCampos, "Fecha de Nacimiento (dd/MM/yyyy)", txtFechaNacimiento, gbc, fila++);
        addLabelAndField(panelCampos, "Obra Social", txtObraSocial, gbc, fila++);
        addLabelAndField(panelCampos, "N° de Afiliado", txtNroAfiliado, gbc, fila++);

        // Campo Género con radio buttons
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panelCampos.add(new JLabel("<html>Género <font color='red'>*</font>:</html>"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel panelGenero = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelGenero.add(rbFemenino);
        panelGenero.add(rbMasculino);
        panelCampos.add(panelGenero, gbc);
        fila++;

        addLabelAndField(panelCampos, "Teléfono", txtTelefono, gbc, fila++);
        addLabelAndField(panelCampos, "Email", txtEmail, gbc, fila++);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelCampos, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(this::guardarPaciente);
        btnCancelar.addActionListener(e -> dispose());

        setMinimumSize(new Dimension(600, 650));
        setPreferredSize(new Dimension(600, 650));
        pack();
    }

    private void addLabelAndField(JPanel panel, String label, JTextField field, GridBagConstraints gbc, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lbl = new JLabel("<html>" + label + " <font color='red'>*</font>:</html>");
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        field.setPreferredSize(new Dimension(250, 25));
        panel.add(field, gbc);
    }

    private void guardarPaciente(ActionEvent e) {
        try {
            // Validaciones básicas
            if (txtDni.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() ||
                txtApellido.getText().trim().isEmpty() || txtFechaNacimiento.getText().trim().isEmpty() ||
                txtObraSocial.getText().trim().isEmpty() || txtNroAfiliado.getText().trim().isEmpty() ||
                txtTelefono.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() ||
                (!rbFemenino.isSelected() && !rbMasculino.isSelected())) {

                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos obligatorios.", "Campos requeridos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int dni = Integer.parseInt(txtDni.getText().trim());
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date parsedDate = sdf.parse(txtFechaNacimiento.getText().trim());
            Date fechaNacimiento = new Date(parsedDate.getTime());

            String obraSocial = txtObraSocial.getText().trim();
            String numeroAfiliado = txtNroAfiliado.getText().trim();
            String genero = rbFemenino.isSelected() ? "Femenino" : "Masculino";
            String telefono = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();

            paciente = new PacienteModelo();
            paciente.setDni(dni);
            paciente.setNombre(nombre);
            paciente.setApellido(apellido);
            paciente.setFechaNacimiento(fechaNacimiento);
            paciente.setObraSocial(obraSocial);
            paciente.setNumeroAfiliado(numeroAfiliado);
            paciente.setGenero(genero);
            paciente.setTelefono(telefono);
            paciente.setEmail(email);

            confirmado = true;
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El DNI debe ser un número válido.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "La fecha debe estar en formato dd/MM/yyyy.", "Error de fecha", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isConfirmed() {
        return confirmado;
    }

    public PacienteModelo getPaciente() {
        return paciente;
    }
}
