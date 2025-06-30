package com.mycompany.centroodontologico.controlador;

import com.mycompany.centroodontologico.modelo.TurnoModelo;
import com.mycompany.centroodontologico.vista.PagoFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PagoControlador implements ActionListener {

    private PagoFrame vista;
    private TurnoModelo modelo;

    public PagoControlador() {
        this.vista = new PagoFrame();
        this.modelo = new TurnoModelo();
        agregarEventos();
        vista.setVisible(true);
    }

    private void agregarEventos() {
        vista.getBtnBuscar().addActionListener(this);
        vista.getBtnPagar().addActionListener(this);
        vista.getBtnCerrar().addActionListener(e -> vista.dispose());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnBuscar()) {
            buscarTurnos();
        }

        if (e.getSource() == vista.getBtnPagar()) {
            realizarPago();
        }
    }

    private void buscarTurnos() {
        String dni = vista.getTxtDni().getText().trim();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingresá un DNI para buscar.");
            return;
        }

        List<Object[]> turnos = modelo.obtenerTurnosPendientesPago(dni);
        DefaultTableModel tm = vista.getTableModel();
        tm.setRowCount(0);
        for (Object[] fila : turnos) {
            tm.addRow(fila);
        }

        if (turnos.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No se encontraron turnos pendientes para ese DNI.");
        }
    }

    private void realizarPago() {
        int fila = vista.getTablaPagos().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccioná un turno para pagar.");
            return;
        }

        String dni = vista.getTablaPagos().getValueAt(fila, 0).toString();
        String fecha = vista.getTablaPagos().getValueAt(fila, 2).toString();
        String hora = vista.getTablaPagos().getValueAt(fila, 3).toString();
        String obraSocial = vista.getTablaPagos().getValueAt(fila, 4).toString();
        String odontologo = vista.getTablaPagos().getValueAt(fila, 5).toString();

        double valorConsulta = 30000.0;
        double descuento = (!obraSocial.equalsIgnoreCase("Particular")) ? 0.20 : 0.0;
        double montoFinal = valorConsulta * (1 - descuento);

        int confirmar = JOptionPane.showConfirmDialog(
            vista,
            String.format("""
                DNI: %s
                Obra social: %s
                Valor base de la consulta: $%.2f
                Descuento aplicado: %.0f%%
                Monto final a pagar: $%.2f

                ¿Deseás confirmar el pago?
            """, dni, obraSocial, valorConsulta, descuento * 100, montoFinal),
            "Confirmar Pago",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmar == JOptionPane.YES_OPTION) {
            boolean exito = modelo.registrarPagoTurno(dni, fecha, hora, odontologo);
            if (exito) {
                JOptionPane.showMessageDialog(vista,
                    "<html>✅ El pago fue registrado exitosamente.<br></html>",
                    "Pago confirmado", JOptionPane.INFORMATION_MESSAGE);
                buscarTurnos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al registrar el pago.");
            }
        }
    }
}
