package com.mycompany.centroodontologico.modelo;

import com.mycompany.centroodontologico.conexion.conexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Odontologo {

    private int dni;
    private String matricula;
    private String usuario;
    private String contrasenia;
    private String nombre;
    private String apellido;
    private String especialidad;
    private String dia;
    private String horario;

    public Odontologo() {}

    // Getters y Setters
    public int getDni() { return dni; }
    public void setDni(int dni) { this.dni = dni; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    // Métodos de utilidad
    public static List<String> obtenerEspecialidadesDisponibles() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT Nombre FROM Especialidad ORDER BY Nombre";
        try (Connection con = conexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getString("Nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static OdontologoDetalleModelo obtenerDetalle(int dni) {
        return new Odontologo().obtenerDetalleOdontologo(dni);
    }

    public OdontologoDetalleModelo obtenerDetalleOdontologo(int dniBuscado) {
        OdontologoDetalleModelo detalle = new OdontologoDetalleModelo();

        String query = """
            SELECT 
                p.Nombre,
                p.Apellido,
                p.DNI,
                p.FechaNacimiento,
                p.Telefono,
                p.Genero,
                p.Email,
                o.Matricula,
                o.Usuario,
                o.Contrasenia,
                e.Nombre AS Especialidad,
                h.Dia,
                h.HoraInicio,
                h.HoraFin
            FROM Persona p
            JOIN Odontologo o ON p.DNI = o.DNI
            LEFT JOIN Especialidad e ON o.DNI = e.OdontologoDNI
            LEFT JOIN HorarioOdontologo h ON o.DNI = h.OdontologoDNI
            WHERE o.DNI = ?
        """;

        try (Connection con = conexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, dniBuscado);

            try (ResultSet rs = ps.executeQuery()) {
                List<String> especialidades = new ArrayList<>();
                List<String> horarios = new ArrayList<>();
                boolean tieneDatos = false;

                while (rs.next()) {
                    if (!tieneDatos) {
                        detalle.setNombre(rs.getString("Nombre"));
                        detalle.setApellido(rs.getString("Apellido"));
                        detalle.setDni(rs.getInt("DNI"));
                        detalle.setFechaNacimiento(rs.getDate("FechaNacimiento"));
                        detalle.setTelefono(rs.getString("Telefono"));
                        detalle.setGenero(rs.getString("Genero"));
                        detalle.setEmail(rs.getString("Email"));
                        detalle.setMatricula(rs.getString("Matricula"));
                        detalle.setUsuario(rs.getString("Usuario"));
                        detalle.setContrasenia(rs.getString("Contrasenia"));
                        tieneDatos = true;
                    }

                    String esp = rs.getString("Especialidad");
                    if (esp != null && !especialidades.contains(esp)) {
                        especialidades.add(esp);
                    }

                    String dia = rs.getString("Dia");
                    Time inicio = rs.getTime("HoraInicio");
                    Time fin = rs.getTime("HoraFin");
                    if (dia != null && inicio != null && fin != null) {
                        horarios.add(dia + ";" + inicio.toString() + " - " + fin.toString());
                    }
                }

                detalle.setEspecialidades(especialidades);
                detalle.setHorarios(horarios);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return detalle;
    }

    public static boolean eliminarPorDni(int dni) {
        try (Connection conn = conexionBD.conectar()) {
            conn.setAutoCommit(false);

            String[] queries = {
                "DELETE FROM Especialidad WHERE OdontologoDNI = ?",
                "DELETE FROM HorarioOdontologo WHERE OdontologoDNI = ?",
                "DELETE FROM Odontologo WHERE DNI = ?",
                "DELETE FROM Persona WHERE DNI = ?"
            };

            for (String sql : queries) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, dni);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean actualizarOdontologo(OdontologoDetalleModelo detalle) {
        if (detalle == null) return false;

        try (Connection conn = conexionBD.conectar()) {
            conn.setAutoCommit(false);

            // Actualizar Persona
            String sqlPersona = """
                UPDATE Persona SET 
                    Nombre = ?, 
                    Apellido = ?, 
                    FechaNacimiento = ?, 
                    Telefono = ?, 
                    Genero = ?, 
                    Email = ?
                WHERE DNI = ?
            """;
            try (PreparedStatement ps = conn.prepareStatement(sqlPersona)) {
                ps.setString(1, detalle.getNombre());
                ps.setString(2, detalle.getApellido());
                ps.setDate(3, new java.sql.Date(detalle.getFechaNacimiento().getTime()));
                ps.setString(4, detalle.getTelefono());
                ps.setString(5, detalle.getGenero());
                ps.setString(6, detalle.getEmail());
                ps.setInt(7, detalle.getDni());
                ps.executeUpdate();
            }

            // Actualizar Odontologo
            String sqlOdontologo = "UPDATE Odontologo SET Matricula = ?, Usuario = ?, Contrasenia = ? WHERE DNI = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlOdontologo)) {
                ps.setString(1, detalle.getMatricula());
                ps.setString(2, detalle.getUsuario());
                ps.setString(3, detalle.getContrasenia());
                ps.setInt(4, detalle.getDni());
                ps.executeUpdate();
            }

            // Actualizar Especialidades
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Especialidad WHERE OdontologoDNI = ?")) {
                ps.setInt(1, detalle.getDni());
                ps.executeUpdate();
            }
            guardarEspecialidades(detalle.getDni(), detalle.getEspecialidades(), conn);

            // Actualizar Horarios
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM HorarioOdontologo WHERE OdontologoDNI = ?")) {
                ps.setInt(1, detalle.getDni());
                ps.executeUpdate();
            }
            guardarHorarios(detalle.getDni(), parsearHorariosDesdeStrings(detalle.getHorarios()), conn);

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertarOdontologo(OdontologoDetalleModelo odontologo) {
        if (odontologo == null || !validarFormatoEmail(odontologo.getEmail())) {
            return false;
        }

        if (emailExiste(odontologo.getEmail())) {
            return false;
        }

        try (Connection conn = conexionBD.conectar()) {
            conn.setAutoCommit(false);

            // Insertar en Persona
            String sqlPersona = "INSERT INTO Persona (DNI, Nombre, Apellido, FechaNacimiento, Telefono, Genero, Email) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlPersona)) {
                ps.setInt(1, odontologo.getDni());
                ps.setString(2, odontologo.getNombre());
                ps.setString(3, odontologo.getApellido());
                ps.setDate(4, new java.sql.Date(odontologo.getFechaNacimiento().getTime()));
                ps.setString(5, odontologo.getTelefono());
                ps.setString(6, odontologo.getGenero());
                ps.setString(7, odontologo.getEmail());
                ps.executeUpdate();
            }

            // Insertar en Odontologo
            String sqlOdontologo = "INSERT INTO Odontologo (DNI, Matricula, Usuario, Contrasenia) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlOdontologo)) {
                ps.setInt(1, odontologo.getDni());
                ps.setString(2, odontologo.getMatricula());
                ps.setString(3, odontologo.getUsuario());
                ps.setString(4, odontologo.getContrasenia());
                ps.executeUpdate();
            }

            guardarEspecialidades(odontologo.getDni(), odontologo.getEspecialidades(), conn);
            guardarHorarios(odontologo.getDni(), parsearHorariosDesdeStrings(odontologo.getHorarios()), conn);

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos auxiliares
    private static List<HorarioModelo> parsearHorariosDesdeStrings(List<String> horariosStr) {
        List<HorarioModelo> horarios = new ArrayList<>();

        for (String h : horariosStr) {
            try {
                String[] partes = h.split(";");
                if (partes.length != 2) continue;

                String dia = partes[0].trim();
                String[] horas = partes[1].split("-");
                if (horas.length != 2) continue;

                Time inicio = Time.valueOf(horas[0].trim());
                Time fin = Time.valueOf(horas[1].trim());

                HorarioModelo horario = new HorarioModelo();
                horario.setDia(dia);
                horario.setHoraInicio(inicio);
                horario.setHoraFin(fin);

                horarios.add(horario);
            } catch (Exception e) {
                System.err.println("⚠️ Error al parsear horario: " + h);
            }
        }

        return horarios;
    }

    private static void guardarHorarios(int odontologoDni, List<HorarioModelo> horarios, Connection conn) throws SQLException {
        String sql = "INSERT INTO HorarioOdontologo (OdontologoDNI, Dia, HoraInicio, HoraFin) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (HorarioModelo h : horarios) {
                ps.setInt(1, odontologoDni);
                ps.setString(2, h.getDia());
                ps.setTime(3, h.getHoraInicio());
                ps.setTime(4, h.getHoraFin());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static void guardarEspecialidades(int odontologoDni, List<String> especialidades, Connection conn) throws SQLException {
        String sql = "INSERT INTO Especialidad (OdontologoDNI, Nombre) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String esp : especialidades) {
                ps.setInt(1, odontologoDni);
                ps.setString(2, esp);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private static boolean validarFormatoEmail(String email) {
        return email != null && Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", email);
    }

    private static boolean emailExiste(String email) {
        String query = "SELECT 1 FROM Persona WHERE Email = ?";
        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Evita duplicados si hay error
        }
    }
}
