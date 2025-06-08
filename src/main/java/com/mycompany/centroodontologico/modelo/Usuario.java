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
        String sql = "SELECT * FROM Secretaria WHERE Usuario = ? AND Contrasenia = ?";

        try (Connection conn = conexionBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si encontró un usuario válido

        } catch (Exception e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}
