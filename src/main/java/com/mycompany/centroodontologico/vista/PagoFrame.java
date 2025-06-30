package com.mycompany.centroodontologico.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PagoFrame extends JFrame {

    private JTextField txtDni;
    private JButton btnBuscar, btnPagar, btnCerrar;
    private JTable tablaPagos;
    private DefaultTableModel tableModel;

    public PagoFrame() {
        setTitle("Gestión de Pagos");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.add(new JLabel("Buscar DNI Paciente:"));
        txtDni = new JTextField(10);
        panelBusqueda.add(txtDni);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"DNI", "Paciente", "Fecha", "Hora", "Obra Social", "Médico"};
        tableModel = new DefaultTableModel(columnas, 0);
        tablaPagos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablaPagos);
        add(scrollPane, BorderLayout.CENTER);

        // Botones inferiores
        JPanel panelBotones = new JPanel();
        btnPagar = new JButton("Realizar Pago");
        btnCerrar = new JButton("Cerrar");
        panelBotones.add(btnPagar);
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Getters
    public JTextField getTxtDni() { return txtDni; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnPagar() { return btnPagar; }
    public JButton getBtnCerrar() { return btnCerrar; }
    public JTable getTablaPagos() { return tablaPagos; }
    public DefaultTableModel getTableModel() { return tableModel; }
}
