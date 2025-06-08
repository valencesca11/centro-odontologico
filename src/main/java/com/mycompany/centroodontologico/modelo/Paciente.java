package com.mycompany.centroodontologico.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.centroodontologico.conexion.conexionBD;

public class Paciente {

    public void registrarPaciente(PacienteModelo modelo) {
        String sqlPersona = "INSERT INTO Persona (DNI, nombre, apellido, FechaNacimiento, telefono, email, genero) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlPaciente = "INSERT INTO Paciente (DNI, obraSocial, numeroAfiliado) VALUES (?, ?, ?)";

        try (Connection conn = conexionBD.conectar()) {
            // Insertar en Persona
            PreparedStatement psPersona = conn.prepareStatement(sqlPersona);
            psPersona.setInt(1, modelo.getDni());
            psPersona.setString(2, modelo.getNombre());
            psPersona.setString(3, modelo.getApellido());
            psPersona.setDate(4, modelo.getFechaNacimiento()); //
            psPersona.setString(5, modelo.getTelefono());
            psPersona.setString(6, modelo.getEmail());
            psPersona.setString(7, modelo.getGenero());
            psPersona.executeUpdate();

            // Insertar en Paciente
            PreparedStatement psPaciente = conn.prepareStatement(sqlPaciente);
            psPaciente.setInt(1, modelo.getDni());
            psPaciente.setString(2, modelo.getObraSocial());
            psPaciente.setString(3, modelo.getNumeroAfiliado());
            psPaciente.executeUpdate();

            System.out.println("Paciente registrado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al registrar paciente: " + e.getMessage());
        }
    }

    public List<PacienteModelo> obtenerPacientes() {
        List<PacienteModelo> lista = new ArrayList<>();

        String sql = """
            SELECT p.DNI, p.nombre, p.apellido, p.FechaNacimiento, 
                   p.telefono, p.email, p.genero, 
                   pac.obraSocial, pac.numeroAfiliado
            FROM Persona p
            JOIN Paciente pac ON p.DNI = pac.DNI
        """;

        try (Connection conn = conexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PacienteModelo modelo = new PacienteModelo();
                modelo.setDni(rs.getInt("DNI"));
                modelo.setNombre(rs.getString("nombre"));
                modelo.setApellido(rs.getString("apellido"));
                modelo.setFechaNacimiento(rs.getDate("FechaNacimiento"));
                modelo.setTelefono(rs.getString("telefono"));
                modelo.setEmail(rs.getString("email"));
                modelo.setGenero(rs.getString("genero"));
                modelo.setObraSocial(rs.getString("obraSocial"));
                modelo.setNumeroAfiliado(rs.getString("numeroAfiliado"));

                lista.add(modelo);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener pacientes: " + e.getMessage());
        }

        return lista;
    }
}
