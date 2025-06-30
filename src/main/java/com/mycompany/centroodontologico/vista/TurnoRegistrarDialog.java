package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.modelo.TurnoModelo;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.IDateEvaluator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.*;

public class TurnoRegistrarDialog extends JDialog {

    private final JTextField txtDniPaciente = new JTextField(10);
    private final JTextField txtPaciente = new JTextField(20);
    private final JTextField txtTelefono = new JTextField(20);
    private final JTextField txtObraSocial = new JTextField(20);
    private final JTextField txtAfiliado = new JTextField(20);
    private final JComboBox<String> comboEspecialidad = new JComboBox<>();
    private final JComboBox<String> comboOdontologo = new JComboBox<>();
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> comboHora = new JComboBox<>();
    private final JButton btnBuscar = new JButton("Buscar");
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnCancelar = new JButton("Cancelar");

    private final TurnoModelo modelo = new TurnoModelo();

    // ATRIBUTO QUE INDICA SI SE GUARDO EL TURNO CORRECTAMENTE
    private boolean turnoGuardado = false;

    public TurnoRegistrarDialog(JFrame parent) {
        super(parent, "Nuevo Turno", true);
        setLayout(new GridBagLayout());
        setSize(500, 450);
        setLocationRelativeTo(parent);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("DNI Paciente:"), gbc);
        gbc.gridx = 1;
        add(txtDniPaciente, gbc);
        gbc.gridx = 2;
        add(btnBuscar, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Paciente:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtPaciente.setEditable(false);
        add(txtPaciente, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 1;
        add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtTelefono.setEditable(false);
        add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Obra Social:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtObraSocial.setEditable(false);
        add(txtObraSocial, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Afiliado N°:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtAfiliado.setEditable(false);
        add(txtAfiliado, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(comboEspecialidad, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Odontólogo:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(comboOdontologo, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        dateChooser.setDateFormatString("yyyy-MM-dd");
        add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Hora (HH:MM):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        comboHora.setModel(new DefaultComboBoxModel<>(new String[]{
                "08:00", "08:30", "09:00", "09:30", "10:00",
                "10:30", "11:00", "11:30", "12:00", "12:30",
                "14:00", "14:30", "15:00", "15:30", "16:00",
                "16:30", "17:00", "17:30"
        }));
        add(comboHora, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, gbc);

        btnBuscar.addActionListener(this::buscarPaciente);
        comboEspecialidad.addActionListener(e -> cargarOdontologos());
        comboOdontologo.addActionListener(e -> filtrarDiasDisponibles());

        cargarEspecialidades();

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardarTurno());
    }

    private void buscarPaciente(ActionEvent e) {
        String dni = txtDniPaciente.getText().trim();
        if (dni.isEmpty()) return;

        String[] datos = modelo.buscarDatosPacientePorDNI(dni);
        if (datos == null) {
            JOptionPane.showMessageDialog(this, "Paciente no encontrado.");
            return;
        }

        txtPaciente.setText(datos[0]);
        txtTelefono.setText(datos[1]);
        txtObraSocial.setText(datos[2]);
        txtAfiliado.setText(datos[3]);
    }

    private void cargarEspecialidades() {
        java.util.List<String> especialidades = modelo.obtenerEspecialidades();
        comboEspecialidad.removeAllItems();
        for (String esp : especialidades) {
            comboEspecialidad.addItem(esp);
        }
    }

    private void cargarOdontologos() {
        String especialidad = (String) comboEspecialidad.getSelectedItem();
        if (especialidad == null) return;

        java.util.List<String> odontologos = modelo.obtenerOdontologosPorEspecialidad(especialidad);
        comboOdontologo.removeAllItems();
        for (String o : odontologos) {
            comboOdontologo.addItem(o);
        }
    }

    private void filtrarDiasDisponibles() {
        String odontologo = (String) comboOdontologo.getSelectedItem();
        if (odontologo == null) return;

        java.util.List<String> dias = modelo.obtenerDiasAtencionPorOdontologo(odontologo);
        if (dias == null || dias.isEmpty()) return;

        var dayChooser = dateChooser.getJCalendar().getDayChooser();

        // Si tu versión permite remover evaluadores previos, hacelo
        // dayChooser.removeAllDateEvaluators();

        dayChooser.addDateEvaluator(new DiaAtencionDateEvaluator(dias));
    }

    private void guardarTurno() {
        String dni = txtDniPaciente.getText().trim();
        String odontologo = (String) comboOdontologo.getSelectedItem();
        java.util.Date selectedDate = dateChooser.getDate();
        Date fecha = selectedDate != null ? new Date(selectedDate.getTime()) : null;
        String hora = (String) comboHora.getSelectedItem();

        if (dni.isEmpty() || odontologo == null || fecha == null || hora == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos antes de guardar.");
            return;
        }

        boolean exito = modelo.insertarTurno(dni, odontologo, fecha, hora);
        if (exito) {
            JOptionPane.showMessageDialog(this, "Turno registrado con éxito.");
            turnoGuardado = true;  // <-- acá indicamos que se guardó
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar el turno.");
        }
    }

    public boolean isTurnoGuardado() {
        return turnoGuardado;
    }

    // Clase interna para limitar días válidos en el calendario
    static class DiaAtencionDateEvaluator implements IDateEvaluator {
        private final Set<Integer> diasPermitidos = new HashSet<>();

        public DiaAtencionDateEvaluator(java.util.List<String> dias) {
            for (String dia : dias) {
                switch (dia.toLowerCase()) {
                    case "lunes" -> diasPermitidos.add(Calendar.MONDAY);
                    case "martes" -> diasPermitidos.add(Calendar.TUESDAY);
                    case "miércoles" -> diasPermitidos.add(Calendar.WEDNESDAY);
                    case "jueves" -> diasPermitidos.add(Calendar.THURSDAY);
                    case "viernes" -> diasPermitidos.add(Calendar.FRIDAY);
                    case "sábado" -> diasPermitidos.add(Calendar.SATURDAY);
                    case "domingo" -> diasPermitidos.add(Calendar.SUNDAY);
                }
            }
        }

        @Override
        public boolean isSpecial(java.util.Date date) {
            return false;
        }

        @Override
        public Color getSpecialForegroundColor() {
            return null;
        }

        @Override
        public Color getSpecialBackroundColor() {
            return null;
        }

        @Override
        public String getSpecialTooltip() {
            return null;
        }

        @Override
        public boolean isInvalid(java.util.Date date) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return !diasPermitidos.contains(cal.get(Calendar.DAY_OF_WEEK));
        }

        @Override
        public Color getInvalidForegroundColor() {
            return Color.RED;
        }

        @Override
        public Color getInvalidBackroundColor() {
            return null;
        }

        @Override
        public String getInvalidTooltip() {
            return "El odontólogo no atiende este día.";
        }
    }
}
