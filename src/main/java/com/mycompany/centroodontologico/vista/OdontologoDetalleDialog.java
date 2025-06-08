package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.controlador.OdontologoControlador;
import com.mycompany.centroodontologico.modelo.Odontologo;
import com.mycompany.centroodontologico.modelo.OdontologoDetalleModelo;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OdontologoDetalleDialog extends JDialog {

    private final OdontologoControlador controlador;

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtDni = new JTextField();
    private final JTextField txtGenero = new JTextField();
    private final JTextField txtTelefono = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtFechaNacimiento = new JTextField();
    private final JTextField txtMatricula = new JTextField();

    private final JTextArea txtEspecialidades = new JTextArea();
    private final JTable tablaHorarios = new JTable();

    public final JButton btnAtras = new JButton("← Atrás");
    public final JButton btnEditar = new JButton("Editar Odontólogo");
    public final JButton btnBorrar = new JButton("Borrar Odontólogo");

    public OdontologoDetalleDialog(JFrame parent, OdontologoControlador controlador) {
        super(parent, "OdontSystem / Odont / Detalle", true);
        this.controlador = controlador;

        setSize(600, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Datos Odontólogo",
                TitledBorder.CENTER, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridy = 0;

        Dimension campoTam = new Dimension(150, 24);
        JTextField[] campos = {
                txtNombre, txtApellido, txtDni, txtGenero,
                txtTelefono, txtEmail, txtFechaNacimiento, txtMatricula
        };
        for (JTextField campo : campos) {
            campo.setPreferredSize(campoTam);
            campo.setEnabled(false);
        }

        txtEspecialidades.setEnabled(false);
        txtEspecialidades.setLineWrap(true);
        txtEspecialidades.setWrapStyleWord(true);

        String[] labels = {
                "Nombre:", "DNI:",
                "Apellido:", "Género:",
                "Teléfono:", "E-mail:",
                "Fecha de nacimiento:", "Matrícula:"
        };
        JTextField[] fields = {
                txtNombre, txtDni,
                txtApellido, txtGenero,
                txtTelefono, txtEmail,
                txtFechaNacimiento, txtMatricula
        };

        for (int i = 0; i < labels.length; i += 2) {
            gbc.gridx = 0;
            gbc.gridy++;
            panelCentral.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            panelCentral.add(fields[i], gbc);

            if (i + 1 < labels.length) {
                gbc.gridx = 2;
                panelCentral.add(new JLabel(labels[i + 1]), gbc);
                gbc.gridx = 3;
                panelCentral.add(fields[i + 1], gbc);
            }
        }

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 4;
        panelCentral.add(new JLabel("ESPECIALIDADES:"), gbc);
        gbc.gridy++;
        JScrollPane scrollEsp = new JScrollPane(txtEspecialidades);
        scrollEsp.setPreferredSize(new Dimension(550, 60));
        panelCentral.add(scrollEsp, gbc);

        gbc.gridy++;
        panelCentral.add(new JLabel("HORARIOS:"), gbc);
        gbc.gridy++;
        JScrollPane scrollHor = new JScrollPane(tablaHorarios);
        scrollHor.setPreferredSize(new Dimension(550, 100));
        tablaHorarios.setFillsViewportHeight(true);
        panelCentral.add(scrollHor, gbc);

        add(panelCentral, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnBorrar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnAtras);
        add(panelBotones, BorderLayout.SOUTH);

        btnEditar.addActionListener(e -> editarOdontologo());
    }

    private void editarOdontologo() {
        try {
            int dni = Integer.parseInt(txtDni.getText());

            List<String> especialidadesDisponibles = Odontologo.obtenerEspecialidadesDisponibles();
            OdontologoDetalleModelo modelo = Odontologo.obtenerDetalle(dni);

            if (modelo == null) {
                JOptionPane.showMessageDialog(this, "No se pudo cargar la información para editar.");
                return;
            }

            OdontologoEditarDialog editarDialog = new OdontologoEditarDialog(this, modelo, especialidadesDisponibles, controlador);
            editarDialog.setVisible(true);

            // Recargar datos tras edición
            OdontologoDetalleModelo nuevoDetalle = Odontologo.obtenerDetalle(dni);
            if (nuevoDetalle != null) {
                cargarDatos(nuevoDetalle);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "DNI inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarDatos(OdontologoDetalleModelo modelo) {
        txtNombre.setText(modelo.getNombre());
        txtApellido.setText(modelo.getApellido());
        txtDni.setText(String.valueOf(modelo.getDni()));
        txtGenero.setText(modelo.getGenero());
        txtTelefono.setText(modelo.getTelefono());
        txtEmail.setText(modelo.getEmail());
        txtFechaNacimiento.setText(modelo.getFechaNacimiento() != null ? modelo.getFechaNacimiento().toString() : "");
        txtMatricula.setText(modelo.getMatricula());

if (modelo.getEspecialidades() != null && !modelo.getEspecialidades().isEmpty()) {
    txtEspecialidades.setText(String.join("\n", modelo.getEspecialidades()));
} else {
    txtEspecialidades.setText("No registradas.");
}

DefaultTableModel tablaModelo = new DefaultTableModel(new Object[]{"Día", "Horario"}, 0);

if (modelo.getHorarios() != null && !modelo.getHorarios().isEmpty()) {
    for (String horario : modelo.getHorarios()) {
        String[] partes = horario.split(";");
        if (partes.length == 2) {
            tablaModelo.addRow(new Object[]{partes[0], partes[1]});
        } else {
            tablaModelo.addRow(new Object[]{"", ""});
        }
    }
} else {
    tablaModelo.addRow(new Object[]{"No asignado", "No asignado"});
}

tablaHorarios.setModel(tablaModelo);

    }
}
