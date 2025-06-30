package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.controlador.OdontologoControlador;
import com.mycompany.centroodontologico.modelo.OdontologoDetalleModelo;
import com.mycompany.centroodontologico.modelo.Odontologo;

import javax.swing.*;
import java.awt.*;
import java.util.Date; // Importamos Date para manejar fechas
import java.util.List;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;

public class OdontologoRegistrarDialog extends JDialog {

    private JTextField txtDni, txtNombre, txtApellido, txtTelefono, txtEmail, txtMatricula;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JRadioButton rbtnMasculino, rbtnFemenino;
    private ButtonGroup generoGroup;
    private JFormattedTextField txtFechaNacimiento; // Campo para la fecha de nacimiento
    private JComboBox<String> cmbEspecialidad, cmbDia, cmbHoraInicio, cmbHoraFin;
    private JButton btnGuardar, btnCancelar;
    private final OdontologoControlador controlador;
    private OdontologoGuardadoListener listener;

    public void setOdontologoGuardadoListener(OdontologoGuardadoListener listener) {
        this.listener = listener;
    }

    public OdontologoRegistrarDialog(JFrame parent, OdontologoControlador controlador) {
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
        gbc.gridx = 1; txtDni = new JTextField(15); panelPrincipal.add(txtDni, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = new JTextField(20); panelPrincipal.add(txtNombre, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1; txtApellido = new JTextField(20); panelPrincipal.add(txtApellido, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; txtTelefono = new JTextField(15); panelPrincipal.add(txtTelefono, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; txtEmail = new JTextField(20); panelPrincipal.add(txtEmail, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 1; txtMatricula = new JTextField(15); panelPrincipal.add(txtMatricula, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1; txtUsuario = new JTextField(15); panelPrincipal.add(txtUsuario, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; txtContrasena = new JPasswordField(15); panelPrincipal.add(txtContrasena, gbc); y++;

        // Campo de fecha de nacimiento
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Fecha de Nacimiento:"), gbc);
        gbc.gridx = 1;
        try {
            // JFormattedTextField para mostrar la fecha
            txtFechaNacimiento = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
            txtFechaNacimiento.setColumns(10);  // Establece el tamaño del campo
            txtFechaNacimiento.setValue(new Date());  // Pone la fecha actual como valor por defecto
        } catch (Exception e) {
            e.printStackTrace();
        }
        panelPrincipal.add(txtFechaNacimiento, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1; cmbEspecialidad = new JComboBox<>(); panelPrincipal.add(cmbEspecialidad, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Día:"), gbc);
        gbc.gridx = 1; cmbDia = new JComboBox<>(new String[] {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"}); panelPrincipal.add(cmbDia, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Hora Inicio:"), gbc);
        gbc.gridx = 1; cmbHoraInicio = new JComboBox<>(new String[] {"08:00:00", "09:00:00", "10:00:00", "11:00:00", "12:00:00"}); panelPrincipal.add(cmbHoraInicio, gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Hora Fin:"), gbc);
        gbc.gridx = 1; cmbHoraFin = new JComboBox<>(new String[] {"12:00:00", "13:00:00", "14:00:00", "15:00:00", "16:00:00"}); panelPrincipal.add(cmbHoraFin, gbc); y++;

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

        cargarEspecialidades();
    }

    private void cargarEspecialidades() {
        List<String> especialidades = Odontologo.obtenerEspecialidadesDisponibles();
        for (String especialidad : especialidades) {
            cmbEspecialidad.addItem(especialidad);
        }
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

        // Convertir la fecha de nacimiento
        Date fechaNacimiento = (Date) txtFechaNacimiento.getValue();
        if (fechaNacimiento != null) {
            nuevo.setFechaNacimiento(fechaNacimiento);
        } else {
            mostrarError("La fecha de nacimiento no es válida.");
            return;
        }

        nuevo.setMatricula(txtMatricula.getText().trim());
        nuevo.setUsuario(txtUsuario.getText().trim());
        nuevo.setContrasenia(new String(txtContrasena.getPassword()).trim());

        String especialidad = (String) cmbEspecialidad.getSelectedItem();
        nuevo.setEspecialidad(especialidad);
        nuevo.setDia((String) cmbDia.getSelectedItem());
        nuevo.setHorario(cmbHoraInicio.getSelectedItem() + " - " + cmbHoraFin.getSelectedItem());

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
