package com.mycompany.centroodontologico.vista;

import com.mycompany.centroodontologico.controlador.OdontologoControlador;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OdontologoFrame extends JFrame {
    private JComboBox<String> cbEspecialidad;
    private JTextField txtBuscar;
    private JLabel lblInfo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar, btnDetalles, btnAtras;

    private OdontologoControlador controlador;
    private List<String> especialidadesDisponibles;

    public OdontologoFrame(OdontologoControlador controlador, List<String> especialidadesDisponibles) {
        this.controlador = controlador;
        this.especialidadesDisponibles = especialidadesDisponibles;

        setTitle("OdontSystem / Odontólogos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));

        panelSuperior.add(new JLabel("Especialidad:"));
        cbEspecialidad = new JComboBox<>();
        cbEspecialidad.setPreferredSize(new Dimension(120, 25));
        panelSuperior.add(cbEspecialidad);

        panelSuperior.add(Box.createHorizontalStrut(30));

        panelSuperior.add(new JLabel("Filtrar por:"));
        txtBuscar = new JTextField();
        txtBuscar.setPreferredSize(new Dimension(150, 25));
        panelSuperior.add(txtBuscar);

        lblInfo = new JLabel("\u2139"); // ícono info
        lblInfo.setFont(new Font("Dialog", Font.PLAIN, 20));
        lblInfo.setForeground(Color.BLUE);
        lblInfo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelSuperior.add(lblInfo);

        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{
            "Especialidad", "Nombre", "Apellido", "DNI", "Día", "Horario"
        }, 0);

        tabla = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        btnRegistrar = new JButton("Registrar Odontólogo");
        btnDetalles = new JButton("Ver Detalles Odontólogo");
        btnAtras = new JButton("\u2190 Atrás");
        // 🔧 Botón temporal para probar si actualiza la tabla sin filtros:
JButton btnActualizar = new JButton("Actualizar tabla");
btnActualizar.addActionListener(e -> controlador.actualizarTabla());

        panelInferior.add(btnRegistrar);
        panelInferior.add(btnDetalles);
        panelInferior.add(btnAtras);
        panelInferior.add(btnActualizar);

        add(panelInferior, BorderLayout.SOUTH);

        // Cargar especialidades
        cbEspecialidad.addItem(""); // Opción vacía
        for (String esp : especialidadesDisponibles) {
            cbEspecialidad.addItem(esp);
        }
    }

    public JComboBox<String> getCbEspecialidad() { return cbEspecialidad; }
    public JTextField getTxtBuscar() { return txtBuscar; }
    public JLabel getBtnInfo() { return lblInfo; }
    public JTable getTabla() { return tabla; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    public JButton getBtnRegistrar() { return btnRegistrar; }
    public JButton getBtnDetalles() { return btnDetalles; }
    public JButton getBtnAtras() { return btnAtras; }
}
