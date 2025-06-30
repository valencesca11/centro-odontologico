package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.modelo.TurnoModelo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TurnoFrame extends JFrame {

    private JComboBox<String> comboOdontologo;
    private JTextField txtDniPaciente;
    private JTable tablaTurnos;
    private JButton btnRegistrar, btnModificar, btnBorrar, btnAtras;

    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public TurnoFrame() {
        setTitle("OdontSystem / Turnos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panelPrincipal, BorderLayout.CENTER);

        // Panel botón atrás
        JPanel panelAtras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAtras = new JButton("< Atrás");
        panelAtras.add(btnAtras);
        panelPrincipal.add(panelAtras);

        // Panel título
        JPanel panelTitulo = new JPanel();
        JLabel lblTitulo = new JLabel("GESTIÓN DE TURNOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo);
        panelPrincipal.add(panelTitulo);

        // Panel de filtros
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtrosPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        filtrosPanel.add(new JLabel("Odontólogo:"));
        comboOdontologo = new JComboBox<>();
        cargarOdontologosEnCombo();
        filtrosPanel.add(comboOdontologo);

        filtrosPanel.add(new JLabel("DNI Paciente:"));
        txtDniPaciente = new JTextField(12);
        filtrosPanel.add(txtDniPaciente);

        panelPrincipal.add(filtrosPanel);

        // Tabla de turnos
        String[] columnas = {"Odontólogo", "Paciente", "DNI", "Obra Social", "Fecha", "Horario", "Abonado"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 6) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTurnos = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        tablaTurnos.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(tablaTurnos);
        scrollPane.setPreferredSize(new Dimension(850, 250));
        panelPrincipal.add(scrollPane);

        // Panel de botones inferiores
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBorrar = new JButton("Borrar Turno");
        btnModificar = new JButton("Modificar Turno");
        btnRegistrar = new JButton("Registrar Turno");

        bottomPanel.add(btnBorrar);
        bottomPanel.add(btnModificar);
        bottomPanel.add(btnRegistrar);

        panelPrincipal.add(bottomPanel);

        // Filtro dinámico
        comboOdontologo.addActionListener(e -> filtrarPorDniYPaciente());
        txtDniPaciente.addActionListener(e -> filtrarPorDniYPaciente());
        txtDniPaciente.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrarPorDniYPaciente();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrarPorDniYPaciente();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrarPorDniYPaciente();
            }
        });

        cargarTurnosEnTabla();
    }

    private void cargarOdontologosEnCombo() {
        TurnoModelo modelo = new TurnoModelo();
        List<String> odontologos = modelo.obtenerNombresOdontologos();

        comboOdontologo.removeAllItems();
        comboOdontologo.addItem("Todos");
        for (String nombre : odontologos) {
            comboOdontologo.addItem(nombre);
        }
    }

    public void cargarTurnosEnTabla() {
        TurnoModelo modelo = new TurnoModelo();
        List<Object[]> datos = modelo.obtenerTurnosParaTabla();

        tableModel.setRowCount(0);
        for (Object[] fila : datos) {
            tableModel.addRow(fila);
        }
    }

    private void filtrarPorDniYPaciente() {
        String odontologoSeleccionado = (String) comboOdontologo.getSelectedItem();
        String dniIngresado = txtDniPaciente.getText().trim();

        List<RowFilter<Object, Object>> filtros = new ArrayList<>();

        if (odontologoSeleccionado != null && !odontologoSeleccionado.equalsIgnoreCase("Todos")) {
            filtros.add(RowFilter.regexFilter("(?i)" + odontologoSeleccionado, 0));
        }

        if (!dniIngresado.isEmpty()) {
            filtros.add(RowFilter.regexFilter("^" + dniIngresado, 2));
        }

        if (filtros.isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.andFilter(filtros));
        }
    }

    // GETTERS
    public JButton getBtnRegistrar() {
        return btnRegistrar;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public JButton getBtnBorrar() {
        return btnBorrar;
    }

    public JButton getBtnAtras() {
        return btnAtras;
    }

    public JTable getTablaTurnos() {
        return tablaTurnos;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TurnoFrame().setVisible(true));
    }
}
