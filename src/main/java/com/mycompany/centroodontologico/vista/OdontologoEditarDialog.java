package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.controlador.OdontologoControlador;
import com.mycompany.centroodontologico.modelo.OdontologoDetalleModelo;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OdontologoEditarDialog extends JDialog {
    private JTextField txtNombre, txtApellido, txtTelefono, txtEmail, txtMatricula;
    private JRadioButton rbtnMasculino, rbtnFemenino;
    private ButtonGroup generoGroup;
    private JFormattedTextField txtFechaNacimiento;
    private JPanel panelEspecialidades;
    private JButton btnGuardar, btnCancelar;

    private JTable tablaHorarios;
    private HorarioTableModel horarioTableModel;
    private JButton btnAgregarHorario, btnEliminarHorario;

    private final OdontologoControlador controlador;
    private final OdontologoDetalleModelo detalle;

    public OdontologoEditarDialog(JDialog parent, OdontologoDetalleModelo detalle, List<String> especialidadesDisponibles, OdontologoControlador controlador) {
        super(parent, "Editar Odontólogo", true);
        this.controlador = controlador;
        this.detalle = detalle;

        setSize(600, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        // Nombre
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; txtNombre = new JTextField(20); txtNombre.setText(detalle.getNombre()); panelPrincipal.add(txtNombre, gbc); y++;

        // Apellido
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1; txtApellido = new JTextField(20); txtApellido.setText(detalle.getApellido()); panelPrincipal.add(txtApellido, gbc); y++;

        // Teléfono
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; txtTelefono = new JTextField(20); txtTelefono.setText(detalle.getTelefono()); panelPrincipal.add(txtTelefono, gbc); y++;

        // Email
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; txtEmail = new JTextField(20); txtEmail.setText(detalle.getEmail()); panelPrincipal.add(txtEmail, gbc); y++;

        // Fecha de nacimiento
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Fecha de nacimiento:"), gbc);
        gbc.gridx = 1;
        txtFechaNacimiento = new JFormattedTextField(java.text.DateFormat.getDateInstance());
        txtFechaNacimiento.setValue(detalle.getFechaNacimiento());
        txtFechaNacimiento.setColumns(20);
        panelPrincipal.add(txtFechaNacimiento, gbc); y++;

        // Género
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
        if ("Masculino".equalsIgnoreCase(detalle.getGenero())) {
            rbtnMasculino.setSelected(true);
        } else if ("Femenino".equalsIgnoreCase(detalle.getGenero())) {
            rbtnFemenino.setSelected(true);
        }
        panelPrincipal.add(generoPanel, gbc); y++;

        // Matrícula
        gbc.gridx = 0; gbc.gridy = y; panelPrincipal.add(new JLabel("Matrícula:"), gbc);
        gbc.gridx = 1; txtMatricula = new JTextField(20); txtMatricula.setText(detalle.getMatricula()); panelPrincipal.add(txtMatricula, gbc); y++;

        // Especialidades
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        panelPrincipal.add(new JLabel("Especialidades:"), gbc); y++;

        gbc.gridx = 0; gbc.gridy = y;
        panelEspecialidades = new JPanel(new GridLayout(0, 1));
        for (String esp : especialidadesDisponibles) {
            JCheckBox check = new JCheckBox(esp);
            if (detalle.getEspecialidades() != null && detalle.getEspecialidades().contains(esp)) {
                check.setSelected(true);
            }
            panelEspecialidades.add(check);
        }
        JScrollPane scrollEspecialidades = new JScrollPane(panelEspecialidades);
        scrollEspecialidades.setPreferredSize(new Dimension(300, 100));
        panelPrincipal.add(scrollEspecialidades, gbc); y++;

        // Horarios
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        panelPrincipal.add(new JLabel("Horarios:"), gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;

        // Convertir List<String> a List<String[]> para tabla
        List<String[]> horariosTabla = new ArrayList<>();
        if (detalle.getHorarios() != null) {
            for (String h : detalle.getHorarios()) {
                // Supongamos que el horario se guarda como "Día - Hora" o "Día;Hora"
                // Ajusta el split según el formato real
                String[] partes = h.split(";", 2);
                if (partes.length < 2) {
                    // Si no tiene separador, ponemos vacío en hora
                    partes = new String[]{partes[0], ""};
                }
                horariosTabla.add(partes);
            }
        }
        horarioTableModel = new HorarioTableModel(horariosTabla);
        tablaHorarios = new JTable(horarioTableModel);
        tablaHorarios.setPreferredScrollableViewportSize(new Dimension(300, 100));
        tablaHorarios.setFillsViewportHeight(true);
        panelPrincipal.add(new JScrollPane(tablaHorarios), gbc); y++;

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        JPanel panelHorariosBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAgregarHorario = new JButton("Agregar Horario");
        btnEliminarHorario = new JButton("Eliminar Horario");

        btnAgregarHorario.addActionListener(e -> horarioTableModel.agregarHorario());
        btnEliminarHorario.addActionListener(e -> {
            int selectedRow = tablaHorarios.getSelectedRow();
            if (selectedRow != -1) {
                horarioTableModel.eliminarHorario(selectedRow);
            }
        });

        panelHorariosBotones.add(btnAgregarHorario);
        panelHorariosBotones.add(btnEliminarHorario);
        panelPrincipal.add(panelHorariosBotones, gbc); y++;

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar = new JButton("Cancelar");
        btnGuardar = new JButton("Guardar Cambios");

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void guardarCambios() {
        detalle.setNombre(txtNombre.getText());
        detalle.setApellido(txtApellido.getText());
        detalle.setTelefono(txtTelefono.getText());
        detalle.setEmail(txtEmail.getText());
        detalle.setFechaNacimiento((java.util.Date) txtFechaNacimiento.getValue());
        detalle.setMatricula(txtMatricula.getText());

        if (rbtnMasculino.isSelected()) {
            detalle.setGenero("Masculino");
        } else if (rbtnFemenino.isSelected()) {
            detalle.setGenero("Femenino");
        }

        List<String> especialidadesSeleccionadas = new ArrayList<>();
        for (Component c : panelEspecialidades.getComponents()) {
            if (c instanceof JCheckBox check && check.isSelected()) {
                especialidadesSeleccionadas.add(check.getText());
            }
        }
        detalle.setEspecialidades(especialidadesSeleccionadas);

        // Convertir List<String[]> a List<String> para guardar en detalle
        List<String[]> horariosTabla = horarioTableModel.getHorarios();
        List<String> horariosParaGuardar = new ArrayList<>();
        for (String[] h : horariosTabla) {
            // Unir día y hora con ';' o el separador que uses
            horariosParaGuardar.add(h[0] + ";" + h[1]);
        }
        detalle.setHorarios(horariosParaGuardar);

        if (controlador.actualizarOdontologo(detalle)) {
            JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al guardar los cambios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class HorarioTableModel extends AbstractTableModel {
        private final List<String[]> horarios;
        private final String[] columnas = {"Día", "Hora"};

        public HorarioTableModel(List<String[]> horarios) {
            this.horarios = horarios;
        }

        @Override
        public int getRowCount() {
            return horarios.size();
        }

        @Override
        public int getColumnCount() {
            return columnas.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnas[column];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return horarios.get(row)[col];
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            horarios.get(row)[col] = value.toString();
            fireTableCellUpdated(row, col);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return true;
        }

        public void agregarHorario() {
            horarios.add(new String[]{"", ""});
            fireTableRowsInserted(horarios.size() - 1, horarios.size() - 1);
        }

        public void eliminarHorario(int row) {
            if (row >= 0 && row < horarios.size()) {
                horarios.remove(row);
                fireTableRowsDeleted(row, row);
            }
        }

        public List<String[]> getHorarios() {
            return horarios;
        }
    }
}
