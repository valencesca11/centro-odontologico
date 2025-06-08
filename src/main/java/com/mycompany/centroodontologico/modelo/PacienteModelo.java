package com.mycompany.centroodontologico.modelo;

import com.mycompany.centroodontologico.conexion.conexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteModelo {

    private int dni;
    private String nombre;
    private String apellido;
    private Date fechaNacimiento;
    private String obraSocial;
    private String numeroAfiliado;
    private String genero;
    private String telefono;
    private String email;

    public PacienteModelo() {}

    // Getters y Setters
    public int getDni() { return dni; }
    public void setDni(int dni) { this.dni = dni; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setFechaNacimientoStr(String fecha) {
        this.fechaNacimiento = Date.valueOf(fecha); // yyyy-MM-dd
    }
    public String getFechaNacimientoStr() {
        return fechaNacimiento != null ? fechaNacimiento.toString() : "";
    }
    public String getObraSocial() { return obraSocial; }
    public void setObraSocial(String obraSocial) { this.obraSocial = obraSocial; }
    public String getNumeroAfiliado() { return numeroAfiliado; }
    public void setNumeroAfiliado(String numeroAfiliado) { this.numeroAfiliado = numeroAfiliado; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Obtener todos los pacientes
    public static List<PacienteModelo> obtenerTodos() {
        List<PacienteModelo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paciente p JOIN Persona pe ON p.DNI = pe.DNI";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PacienteModelo paciente = new PacienteModelo();
                paciente.setDni(rs.getInt("pe.DNI"));
                paciente.setNombre(rs.getString("pe.Nombre"));
                paciente.setApellido(rs.getString("pe.Apellido"));
                paciente.setFechaNacimiento(rs.getDate("pe.FechaNacimiento"));
                paciente.setGenero(rs.getString("pe.Genero"));
                paciente.setTelefono(rs.getString("pe.Telefono"));
                paciente.setEmail(rs.getString("pe.Email"));
                paciente.setObraSocial(rs.getString("p.ObraSocial"));
                paciente.setNumeroAfiliado(rs.getString("p.NumeroAfiliado"));
                lista.add(paciente);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return lista;
    }

    // Obtener paciente por DNI
    public static PacienteModelo obtenerPorDni(String dni) {
        String sql = "SELECT * FROM Paciente p JOIN Persona pe ON p.DNI = pe.DNI WHERE p.DNI = ?";
        PacienteModelo paciente = null;

        try (Connection con = conexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                paciente = new PacienteModelo();
                paciente.setDni(rs.getInt("pe.DNI"));
                paciente.setNombre(rs.getString("pe.Nombre"));
                paciente.setApellido(rs.getString("pe.Apellido"));
                paciente.setFechaNacimiento(rs.getDate("pe.FechaNacimiento"));
                paciente.setGenero(rs.getString("pe.Genero"));
                paciente.setTelefono(rs.getString("pe.Telefono"));
                paciente.setEmail(rs.getString("pe.Email"));
                paciente.setObraSocial(rs.getString("p.ObraSocial"));
                paciente.setNumeroAfiliado(rs.getString("p.NumeroAfiliado"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return paciente;
    }

    // Guardar (actualizar) paciente
    public boolean guardar() {
        try (Connection con = conexionBD.conectar()) {
            // Actualiza Persona
            String sqlPersona = "UPDATE Persona SET Nombre = ?, Apellido = ?, FechaNacimiento = ?, Genero = ?, Telefono = ?, Email = ? WHERE DNI = ?";
            PreparedStatement psPersona = con.prepareStatement(sqlPersona);
            psPersona.setString(1, nombre);
            psPersona.setString(2, apellido);
            psPersona.setDate(3, fechaNacimiento);
            psPersona.setString(4, genero);
            psPersona.setString(5, telefono);
            psPersona.setString(6, email);
            psPersona.setInt(7, dni);
            psPersona.executeUpdate();

            // Actualiza Paciente
            String sqlPaciente = "UPDATE Paciente SET ObraSocial = ?, NumeroAfiliado = ? WHERE DNI = ?";
            PreparedStatement psPaciente = con.prepareStatement(sqlPaciente);
            psPaciente.setString(1, obraSocial);
            psPaciente.setString(2, numeroAfiliado);
            psPaciente.setInt(3, dni);
            psPaciente.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
