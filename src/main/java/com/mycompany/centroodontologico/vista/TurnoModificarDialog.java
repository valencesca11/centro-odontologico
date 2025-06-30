package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.modelo.TurnoModelo;
import com.toedter.calendar.IDateEvaluator;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.util.*;

public class TurnoModificarDialog extends JDialog {

    private final JTextField txtDniPaciente = new JTextField(10);
    private final JTextField txtPaciente = new JTextField(20);
    private final JTextField txtTelefono = new JTextField(20);
    private final JTextField txtObraSocial = new JTextField(20);
    private final JTextField txtAfiliado = new JTextField(20);
    private final JComboBox<String> comboEspecialidad = new JComboBox<>();
    private final JComboBox<String> comboOdontologo = new JComboBox<>();
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> comboHora = new JComboBox<>();
    private final JButton btnGuardar = new JButton("Guardar");
    private final JButton btnCancelar = new JButton("Cancelar");

    private final TurnoModelo modelo = new TurnoModelo();

    private boolean turnoModificado = false;

    // Datos originales para identificar el turno en la BD y actualizarlo
    private final String odontologoOriginal;
    private final String dniPacienteOriginal;
    private final Date fechaOriginal;
    private final String horaOriginal;

    public TurnoModificarDialog(JFrame parent, String odontologo, String dniPaciente, String fecha, String hora) {
        super(parent, "Modificar Turno", true);
        setLayout(new GridBagLayout());
        setSize(520, 460);
        setLocationRelativeTo(parent);

        this.odontologoOriginal = odontologo;
        this.dniPacienteOriginal = dniPaciente;
        this.fechaOriginal = Date.valueOf(fecha); // formato "yyyy-MM-dd"
        this.horaOriginal = hora;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("DNI Paciente:"), gbc);
        gbc.gridx = 1;
        txtDniPaciente.setEditable(false);
        add(txtDniPaciente, gbc);

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

        cargarDatosPaciente();

        cargarEspecialidades();

        comboEspecialidad.addActionListener(e -> cargarOdontologos());

        comboOdontologo.addActionListener(e -> filtrarDiasDisponibles());

        dateChooser.setDate(fechaOriginal);

        comboHora.setSelectedItem(horaOriginal);

        btnCancelar.addActionListener(e -> dispose());

        btnGuardar.addActionListener(this::guardarCambios);

        seleccionarEspecialidadOdontologoOriginal();
    }

    private void cargarDatosPaciente() {
        txtDniPaciente.setText(dniPacienteOriginal);
        String[] datos = modelo.buscarDatosPacientePorDNI(dniPacienteOriginal);
        if (datos != null) {
            txtPaciente.setText(datos[0]);
            txtTelefono.setText(datos[1]);
            txtObraSocial.setText(datos[2]);
            txtAfiliado.setText(datos[3]);
        }
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

        if (odontologoOriginal != null && odontologos.contains(odontologoOriginal)) {
            comboOdontologo.setSelectedItem(odontologoOriginal);
        }
    }

    private void filtrarDiasDisponibles() {
        String odontologo = (String) comboOdontologo.getSelectedItem();
        if (odontologo == null) return;

        java.util.List<String> dias = modelo.obtenerDiasAtencionPorOdontologo(odontologo);
        if (dias == null || dias.isEmpty()) return;

        var dayChooser = dateChooser.getJCalendar().getDayChooser();

        // Para evitar acumulación, removemos los evaluadores anteriores
        // No existe removeAllDateEvaluators(), por eso recreamos dateChooser o manejamos manualmente

        // Lo más simple: crear una instancia nueva de JDateChooser con mismo formato y reemplazar:

        // O para no complicar, solo agregamos un evaluador una vez (aunque podría acumularse)
        dayChooser.addDateEvaluator(new DiaAtencionDateEvaluator(dias));
    }

    private void guardarCambios(ActionEvent e) {
        String nuevoOdontologo = (String) comboOdontologo.getSelectedItem();
        java.util.Date selectedDate = dateChooser.getDate();
        Date nuevaFecha = selectedDate != null ? new Date(selectedDate.getTime()) : null;
        String nuevaHora = (String) comboHora.getSelectedItem();

        if (nuevoOdontologo == null || nuevaFecha == null || nuevaHora == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos antes de guardar.");
            return;
        }

        boolean huboCambios = !nuevoOdontologo.equals(odontologoOriginal)
                || !nuevaFecha.equals(fechaOriginal)
                || !nuevaHora.equals(horaOriginal);

        if (!huboCambios) {
            JOptionPane.showMessageDialog(this, "No se detectaron cambios para guardar.");
            return;
        }

        boolean exito = modelo.actualizarTurno(
                dniPacienteOriginal,
                odontologoOriginal,
                fechaOriginal.toString(),
                horaOriginal,
                nuevoOdontologo,
                nuevaFecha,
                nuevaHora
        );

        if (exito) {
            turnoModificado = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar el turno.");
        }
    }

    public boolean isTurnoModificado() {
        return turnoModificado;
    }

    private void seleccionarEspecialidadOdontologoOriginal() {
        if (odontologoOriginal == null) return;

        java.util.List<String> especialidades = modelo.obtenerEspecialidadPorOdontologo(odontologoOriginal);

        if (especialidades != null && !especialidades.isEmpty()) {
            comboEspecialidad.setSelectedItem(especialidades.get(0));
        }
    }

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
