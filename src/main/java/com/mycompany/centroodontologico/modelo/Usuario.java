package com.mycompany.centroodontologico.modelo;

import com.mycompany.centroodontologico.conexion.conexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Usuario {
    private String usuario;
    private String contrasena;

    public Usuario(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    // Método para validar contra la base de datos
    public boolean validar() {
        // Consulta para verificar en la tabla 'Secretaria'
        String sqlSecretaria = "SELECT * FROM Secretaria WHERE Usuario = ? AND Contrasenia = ?";
        // Consulta para verificar en la tabla 'Odontologo'
        String sqlOdontologo = "SELECT * FROM Odontologo WHERE Usuario = ? AND Contrasenia = ?";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmtSecretaria = conn.prepareStatement(sqlSecretaria);
             PreparedStatement stmtOdontologo = conn.prepareStatement(sqlOdontologo)) {

            // Validación para la tabla 'Secretaria'
            stmtSecretaria.setString(1, usuario);
            stmtSecretaria.setString(2, contrasena);
            ResultSet rsSecretaria = stmtSecretaria.executeQuery();
            if (rsSecretaria.next()) {
                // Usuario encontrado en la tabla Secretaria
                System.out.println("Acceso como Secretaria permitido.");
                return true;
            }

            // Validación para la tabla 'Odontologo'
            stmtOdontologo.setString(1, usuario);
            stmtOdontologo.setString(2, contrasena);
            ResultSet rsOdontologo = stmtOdontologo.executeQuery();
            if (rsOdontologo.next()) {
                // Usuario encontrado en la tabla Odontologo
                System.out.println("Acceso como Odontologo permitido.");
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
        }
        // Si no se encontró en ninguna de las tablas
        return false;
    }
}
