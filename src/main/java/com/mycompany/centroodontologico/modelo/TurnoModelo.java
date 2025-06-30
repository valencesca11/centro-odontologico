package com.mycompany.centroodontologico.modelo;

import com.mycompany.centroodontologico.conexion.conexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurnoModelo {

    // Método que ya tenías para obtener los turnos
    public List<Object[]> obtenerTurnosParaTabla() {
        List<Object[]> lista = new ArrayList<>();

        String sql = """
            SELECT 
                CONCAT(persO.Nombre, ' ', persO.Apellido) AS Odontologo,
                CONCAT(persP.Nombre, ' ', persP.Apellido) AS Paciente,
                persP.DNI,
                pac.ObraSocial,
                t.FechaProgramada,
                t.HoraProgramada,
                IF(t.PagoID IS NULL, false, true) AS Abonado
            FROM Turno t
            JOIN Paciente pac ON t.PacienteID = pac.PacienteID
            JOIN Persona persP ON pac.DNI = persP.DNI
            JOIN Odontologo o ON t.OdontologoDNI = o.DNI
            JOIN Persona persO ON o.DNI = persO.DNI
            ORDER BY t.FechaProgramada, t.HoraProgramada;
        """;

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getString("Odontologo");
                fila[1] = rs.getString("Paciente");
                fila[2] = rs.getString("DNI");
                fila[3] = rs.getString("ObraSocial");
                fila[4] = rs.getDate("FechaProgramada").toString();
                fila[5] = rs.getTime("HoraProgramada").toString();
                fila[6] = rs.getBoolean("Abonado");
                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener turnos: " + e.getMessage());
        }

        return lista;
    }

    public List<String> obtenerNombresOdontologos() {
        List<String> odontologos = new ArrayList<>();

        String sql = """
            SELECT CONCAT(pers.Nombre, ' ', pers.Apellido) AS NombreCompleto
            FROM Odontologo o
            JOIN Persona pers ON o.DNI = pers.DNI
            ORDER BY pers.Apellido, pers.Nombre
        """;

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                odontologos.add(rs.getString("NombreCompleto"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener odontólogos: " + e.getMessage());
        }

        return odontologos;
    }

    public String[] buscarDatosPacientePorDNI(String dni) {
        String sql = """
            SELECT CONCAT(p.Nombre, ' ', p.Apellido) AS NombreCompleto, p.Telefono, pac.ObraSocial, pac.NumeroAfiliado
            FROM Paciente pac
            JOIN Persona p ON pac.DNI = p.DNI
            WHERE p.DNI = ?
        """;

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new String[]{
                    rs.getString("NombreCompleto"),
                    rs.getString("Telefono"),
                    rs.getString("ObraSocial"),
                    rs.getString("NumeroAfiliado")
                };
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar paciente: " + e.getMessage());
        }
        return null;
    }

    public List<String> obtenerEspecialidades() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT Nombre FROM Especialidad ORDER BY Nombre";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("Nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener especialidades: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerOdontologosPorEspecialidad(String especialidad) {
        List<String> lista = new ArrayList<>();
        String sql = """
            SELECT CONCAT(p.Nombre, ' ', p.Apellido) AS NombreCompleto
            FROM Odontologo o
            JOIN Persona p ON o.DNI = p.DNI
            JOIN Especialidad e ON o.DNI = e.OdontologoDNI
            WHERE e.Nombre = ?
            ORDER BY p.Apellido
        """;

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, especialidad);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString("NombreCompleto"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener odontólogos por especialidad: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerDiasAtencionPorOdontologo(String nombreCompleto) {
        List<String> dias = new ArrayList<>();

        String sql = """
            SELECT ho.Dia
            FROM HorarioOdontologo ho
            JOIN Odontologo o ON ho.OdontologoDNI = o.DNI
            JOIN Persona p ON o.DNI = p.DNI
            WHERE CONCAT(p.Nombre, ' ', p.Apellido) = ?
        """;

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreCompleto);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dias.add(rs.getString("Dia"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener días de atención: " + e.getMessage());
        }
        return dias;
    }

    public boolean insertarTurno(String dniPaciente, String odontologoNombreCompleto, Date fecha, String hora) {
        String sqlPaciente = "SELECT PacienteID FROM Paciente WHERE DNI = ?";
        String sqlOdontologo = "SELECT o.DNI FROM Odontologo o JOIN Persona p ON o.DNI = p.DNI WHERE CONCAT(p.Nombre, ' ', p.Apellido) = ?";
        String sqlInsert = "INSERT INTO Turno (PacienteID, OdontologoDNI, FechaProgramada, HoraProgramada) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexionBD.conectar()) {
            conn.setAutoCommit(false);

            int pacienteID = -1;
            try (PreparedStatement psPaciente = conn.prepareStatement(sqlPaciente)) {
                psPaciente.setString(1, dniPaciente);
                try (ResultSet rs = psPaciente.executeQuery()) {
                    if (rs.next()) {
                        pacienteID = rs.getInt("PacienteID");
                    } else {
                        conn.rollback();
                        System.err.println("Paciente no encontrado con DNI: " + dniPaciente);
                        return false;
                    }
                }
            }

            String odontologoDNI = null;
            try (PreparedStatement psOdontologo = conn.prepareStatement(sqlOdontologo)) {
                psOdontologo.setString(1, odontologoNombreCompleto);
                try (ResultSet rs = psOdontologo.executeQuery()) {
                    if (rs.next()) {
                        odontologoDNI = rs.getString("DNI");
                    } else {
                        conn.rollback();
                        System.err.println("Odontólogo no encontrado: " + odontologoNombreCompleto);
                        return false;
                    }
                }
            }

            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, pacienteID);
                psInsert.setString(2, odontologoDNI);
                psInsert.setDate(3, fecha);
                psInsert.setString(4, hora);
                int filas = psInsert.executeUpdate();
                if (filas != 1) {
                    conn.rollback();
                    System.err.println("No se pudo insertar el turno.");
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar turno: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarTurno(String dniPaciente, String odontologoNombreCompleto, String fecha, String hora) {
        String sqlOdontologo = "SELECT o.DNI FROM Odontologo o JOIN Persona p ON o.DNI = p.DNI WHERE CONCAT(p.Nombre, ' ', p.Apellido) = ?";
        String sqlDelete = "DELETE FROM Turno WHERE PacienteID = (SELECT PacienteID FROM Paciente WHERE DNI = ?) AND OdontologoDNI = ? AND FechaProgramada = ? AND HoraProgramada = ?";

        try (Connection conn = conexionBD.conectar()) {
            conn.setAutoCommit(false);

            String odontologoDNI = null;
            try (PreparedStatement psOdontologo = conn.prepareStatement(sqlOdontologo)) {
                psOdontologo.setString(1, odontologoNombreCompleto);
                try (ResultSet rs = psOdontologo.executeQuery()) {
                    if (rs.next()) {
                        odontologoDNI = rs.getString("DNI");
                    } else {
                        System.err.println("Odontólogo no encontrado para eliminación: " + odontologoNombreCompleto);
                        return false;
                    }
                }
            }

            try (PreparedStatement psDelete = conn.prepareStatement(sqlDelete)) {
                psDelete.setString(1, dniPaciente);
                psDelete.setString(2, odontologoDNI);
                psDelete.setDate(3, java.sql.Date.valueOf(fecha));
                psDelete.setString(4, hora);

                int filas = psDelete.executeUpdate();
                if (filas != 1) {
                    conn.rollback();
                    System.err.println("No se pudo eliminar el turno.");
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar turno: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarTurno(String dniPaciente, String odontologoAntiguo, String fechaAntigua, String horaAntigua,
                                  String nuevoOdontologo, Date nuevaFecha, String nuevaHora) {
        String sqlOdontologo = "SELECT o.DNI FROM Odontologo o JOIN Persona p ON o.DNI = p.DNI WHERE CONCAT(p.Nombre, ' ', p.Apellido) = ?";
        String sqlUpdate = """
            UPDATE Turno
            SET OdontologoDNI = ?, FechaProgramada = ?, HoraProgramada = ?
            WHERE PacienteID = (SELECT PacienteID FROM Paciente WHERE DNI = ?)
              AND OdontologoDNI = ?
              AND FechaProgramada = ?
              AND HoraProgramada = ?
        """;

        try (Connection conn = conexionBD.conectar()) {
            conn.setAutoCommit(false);

            String odontologoNuevoDNI = null;
            String odontologoAntiguoDNI = null;

            // Obtener DNI odontologo nuevo
            try (PreparedStatement psNuevo = conn.prepareStatement(sqlOdontologo)) {
                psNuevo.setString(1, nuevoOdontologo);
                try (ResultSet rs = psNuevo.executeQuery()) {
                    if (rs.next()) {
                        odontologoNuevoDNI = rs.getString("DNI");
                    } else {
                        System.err.println("Odontólogo nuevo no encontrado: " + nuevoOdontologo);
                        return false;
                    }
                }
            }

            // Obtener DNI odontologo antiguo
            try (PreparedStatement psAntiguo = conn.prepareStatement(sqlOdontologo)) {
                psAntiguo.setString(1, odontologoAntiguo);
                try (ResultSet rs = psAntiguo.executeQuery()) {
                    if (rs.next()) {
                        odontologoAntiguoDNI = rs.getString("DNI");
                    } else {
                        System.err.println("Odontólogo antiguo no encontrado: " + odontologoAntiguo);
                        return false;
                    }
                }
            }

            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psUpdate.setString(1, odontologoNuevoDNI);
                psUpdate.setDate(2, nuevaFecha);
                psUpdate.setString(3, nuevaHora);
                psUpdate.setString(4, dniPaciente);
                psUpdate.setString(5, odontologoAntiguoDNI);
                psUpdate.setDate(6, java.sql.Date.valueOf(fechaAntigua));
                psUpdate.setString(7, horaAntigua);

                int filas = psUpdate.executeUpdate();
                if (filas != 1) {
                    conn.rollback();
                    System.err.println("No se pudo actualizar el turno.");
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            System.err.println("Error al actualizar turno: " + e.getMessage());
            return false;
        }
    }

    // ----------- MÉTODO QUE FALTABA: obtenerEspecialidadPorOdontologo -----------

    public List<String> obtenerEspecialidadPorOdontologo(String nombreCompleto) {
        List<String> especialidades = new ArrayList<>();
        String sql = """
            SELECT e.Nombre
            FROM Especialidad e
            JOIN Odontologo o ON e.OdontologoDNI = o.DNI
            JOIN Persona p ON o.DNI = p.DNI
            WHERE CONCAT(p.Nombre, ' ', p.Apellido) = ?
            ORDER BY e.Nombre
        """;

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreCompleto);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                especialidades.add(rs.getString("Nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener especialidad por odontólogo: " + e.getMessage());
        }
        return especialidades;
    }

    // ------------------ MÉTODOS NUEVOS PARA GESTIÓN DE PAGOS -------------------

    /**
     * Obtiene los turnos pendientes de pago para un DNI específico.
     * Retorna lista de arreglos Object[] con los datos relevantes para la tabla de pagos,
     * incluyendo el monto final a pagar con descuento si corresponde.
     */
    public List<Object[]> obtenerTurnosPendientesPago(String dni) {
        List<Object[]> turnos = new ArrayList<>();

        String sql = """
            SELECT 
                persP.DNI,
                CONCAT(persP.Nombre, ' ', persP.Apellido) AS Paciente,
                t.FechaProgramada,
                t.HoraProgramada,
                pac.ObraSocial,
                CONCAT(persO.Nombre, ' ', persO.Apellido) AS Odontologo
            FROM Turno t
            JOIN Paciente pac ON t.PacienteID = pac.PacienteID
            JOIN Persona persP ON pac.DNI = persP.DNI
            JOIN Odontologo o ON t.OdontologoDNI = o.DNI
            JOIN Persona persO ON o.DNI = persO.DNI
            WHERE persP.DNI = ? AND t.PagoID IS NULL
            ORDER BY t.FechaProgramada, t.HoraProgramada;
        """;

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[7]; // Agrego campo monto con descuento
                fila[0] = rs.getString("DNI");
                fila[1] = rs.getString("Paciente");
                fila[2] = rs.getDate("FechaProgramada").toString();
                fila[3] = rs.getTime("HoraProgramada").toString();
                String obraSocial = rs.getString("ObraSocial");
                fila[4] = obraSocial;
                fila[5] = rs.getString("Odontologo");
                fila[6] = calcularMontoConDescuento(obraSocial); // monto con descuento aplicado
                turnos.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener turnos pendientes de pago: " + e.getMessage());
        }

        return turnos;
    }

    /**
     * Registra el pago de un turno específico dado el DNI, fecha, hora y odontólogo.
     * Retorna true si se pudo actualizar correctamente, false si hubo error.
     */
    public boolean registrarPagoTurno(String dni, String fecha, String hora, String odontologoNombreCompleto) {
        String sqlOdontologo = """
            SELECT o.DNI FROM Odontologo o
            JOIN Persona p ON o.DNI = p.DNI
            WHERE CONCAT(p.Nombre, ' ', p.Apellido) = ?
        """;

        String sqlUpdate = """
            UPDATE Turno
            SET PagoID = 1
            WHERE PacienteID = (SELECT PacienteID FROM Paciente WHERE DNI = ?)
              AND FechaProgramada = ?
              AND HoraProgramada = ?
              AND OdontologoDNI = ?
              AND PagoID IS NULL
        """;

        try (Connection conn = conexionBD.conectar()) {
            conn.setAutoCommit(false);

            String odontologoDNI = null;
            try (PreparedStatement psOdo = conn.prepareStatement(sqlOdontologo)) {
                psOdo.setString(1, odontologoNombreCompleto);
                ResultSet rs = psOdo.executeQuery();
                if (rs.next()) {
                    odontologoDNI = rs.getString("DNI");
                } else {
                    System.err.println("Odontólogo no encontrado para registrar pago: " + odontologoNombreCompleto);
                    return false;
                }
            }

            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psUpdate.setString(1, dni);
                psUpdate.setDate(2, java.sql.Date.valueOf(fecha));
                psUpdate.setString(3, hora);
                psUpdate.setString(4, odontologoDNI);

                int filas = psUpdate.executeUpdate();
                if (filas != 1) {
                    conn.rollback();
                    System.err.println("No se pudo registrar el pago, turno no encontrado o ya pagado.");
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar pago turno: " + e.getMessage());
            return false;
        }
    }

    /**
     * Calcula el monto a pagar según la obra social.
     * Si obra social es diferente de null y diferente de "Particular",
     * aplica descuento del 20%, sino el monto es 1000.
     */
    private double calcularMontoConDescuento(String obraSocial) {
        double montoBase = 1000.0;
        if (obraSocial != null && !obraSocial.equalsIgnoreCase("Particular")) {
            return montoBase * 0.8; // 20% descuento
        }
        return montoBase;
    }
} 
