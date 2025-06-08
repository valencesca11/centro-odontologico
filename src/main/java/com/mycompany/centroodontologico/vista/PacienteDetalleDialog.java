package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.modelo.PacienteModelo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PacienteDetalleDialog extends JDialog {

    private JTextField txtDni, txtNombre, txtApellido, txtFechaNacimiento, txtTelefono, txtGenero, txtEmail, txtObraSocial, txtNumeroAfiliado;
    private JButton btnGuardar, btnEliminar, btnAtras;
    private PacienteModelo pacienteOriginal;
    private boolean datosActualizados = false;

    public PacienteDetalleDialog(Frame parent, PacienteModelo paciente) {
        super(parent, "Detalle Paciente", true);
        this.pacienteOriginal = paciente;
        setLayout(new BorderLayout(10, 10));
        setSize(500, 520);
        setLocationRelativeTo(parent);

        btnAtras = new JButton("Atrás");
        btnAtras.addActionListener(e -> dispose());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(btnAtras);
        add(topPanel, BorderLayout.NORTH);

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        txtDni = agregarCampo("DNI:", String.valueOf(paciente.getDni()), false, panelCampos, gbc, 0);
        txtNombre = agregarCampo("Nombre:", paciente.getNombre(), true, panelCampos, gbc, 1);
        txtApellido = agregarCampo("Apellido:", paciente.getApellido(), true, panelCampos, gbc, 2);
        txtFechaNacimiento = agregarCampo("Fecha Nacimiento (dd/MM/yyyy):", paciente.getFechaNacimientoStr(), true, panelCampos, gbc, 3);
        txtTelefono = agregarCampo("Teléfono:", paciente.getTelefono(), true, panelCampos, gbc, 4);
        txtGenero = agregarCampo("Género:", paciente.getGenero(), true, panelCampos, gbc, 5);
        txtEmail = agregarCampo("Email:", paciente.getEmail(), true, panelCampos, gbc, 6);
        txtObraSocial = agregarCampo("Obra Social:", paciente.getObraSocial(), true, panelCampos, gbc, 7);
        txtNumeroAfiliado = agregarCampo("Número Afiliado:", paciente.getNumeroAfiliado(), true, panelCampos, gbc, 8);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setEnabled(false);
        btnEliminar = new JButton("Eliminar Paciente");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);

        agregarDetectoresDeCambio();
        btnEliminar.addActionListener(e -> confirmarEliminar());
        btnGuardar.addActionListener(e -> guardarCambios());

        SwingUtilities.invokeLater(() -> {
            txtNombre.requestFocusInWindow();
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void mostrar() {
        setVisible(true);
    }

    private JTextField agregarCampo(String label, String valor, boolean editable, JPanel panel, GridBagConstraints gbc, int y) {
        gbc.gridy = y;

        gbc.gridx = 0;
        JLabel lbl = new JLabel(label);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JTextField txt = new JTextField(20);
        txt.setText(valor);
        txt.setEditable(editable);
        panel.add(txt, gbc);

        return txt;
    }

    private void agregarDetectoresDeCambio() {
        DocumentListener listener = new DocumentListenerAdapter(() -> {
            if (camposEditados()) btnGuardar.setEnabled(true);
        });

        txtNombre.getDocument().addDocumentListener(listener);
        txtApellido.getDocument().addDocumentListener(listener);
        txtFechaNacimiento.getDocument().addDocumentListener(listener);
        txtTelefono.getDocument().addDocumentListener(listener);
        txtGenero.getDocument().addDocumentListener(listener);
        txtEmail.getDocument().addDocumentListener(listener);
        txtObraSocial.getDocument().addDocumentListener(listener);
        txtNumeroAfiliado.getDocument().addDocumentListener(listener);
    }

    private boolean camposEditados() {
        return !txtNombre.getText().equals(pacienteOriginal.getNombre())
                || !txtApellido.getText().equals(pacienteOriginal.getApellido())
                || !txtFechaNacimiento.getText().equals(pacienteOriginal.getFechaNacimientoStr())
                || !txtTelefono.getText().equals(pacienteOriginal.getTelefono())
                || !txtGenero.getText().equals(pacienteOriginal.getGenero())
                || !txtEmail.getText().equals(pacienteOriginal.getEmail())
                || !txtObraSocial.getText().equals(pacienteOriginal.getObraSocial())
                || !txtNumeroAfiliado.getText().equals(pacienteOriginal.getNumeroAfiliado());
    }

    private void guardarCambios() {
        pacienteOriginal.setNombre(txtNombre.getText());
        pacienteOriginal.setApellido(txtApellido.getText());
        pacienteOriginal.setFechaNacimientoStr(txtFechaNacimiento.getText());
        pacienteOriginal.setTelefono(txtTelefono.getText());
        pacienteOriginal.setGenero(txtGenero.getText());
        pacienteOriginal.setEmail(txtEmail.getText());
        pacienteOriginal.setObraSocial(txtObraSocial.getText());
        pacienteOriginal.setNumeroAfiliado(txtNumeroAfiliado.getText());

        try {
            pacienteOriginal.guardar(); // Método centralizado en el modelo
            JOptionPane.showMessageDialog(this, "✅ Cambios guardados exitosamente.");
            datosActualizados = true;
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, " Error al guardar cambios:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void confirmarEliminar() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro que deseas eliminar este paciente?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (var conn = com.mycompany.centroodontologico.conexion.conexionBD.conectar()) {
                String deletePaciente = "DELETE FROM Paciente WHERE dni = ?";
                var psPaciente = conn.prepareStatement(deletePaciente);
                psPaciente.setInt(1, pacienteOriginal.getDni());
                psPaciente.executeUpdate();

                String deletePersona = "DELETE FROM Persona WHERE dni = ?";
                var psPersona = conn.prepareStatement(deletePersona);
                psPersona.setInt(1, pacienteOriginal.getDni());
                psPersona.executeUpdate();

                JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente.");
                datosActualizados = true;
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar paciente:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public boolean isDatosActualizados() {
        return datosActualizados;
    }

    private class DocumentListenerAdapter implements DocumentListener {
        private final Runnable onChange;

        public DocumentListenerAdapter(Runnable onChange) {
            this.onChange = onChange;
        }

        public void insertUpdate(DocumentEvent e) { onChange.run(); }
        public void removeUpdate(DocumentEvent e) { onChange.run(); }
        public void changedUpdate(DocumentEvent e) { onChange.run(); }
    }
}
