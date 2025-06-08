package com.mycompany.centroodontologico.conexion;

import com.mycompany.centroodontologico.conexion.conexionBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class testBD {
    public static void main(String[] args) {
        Connection conn = conexionBD.conectar();

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                String sql = "SELECT * FROM pacientes"; // Cambia el nombre de la tabla si es otra
                ResultSet rs = stmt.executeQuery(sql);

                System.out.println("👀 Resultados de la tabla 'pacientes':");
                while (rs.next()) {
                    String nombre = rs.getString("nombre"); // cambia el nombre de columna si es diferente
                    String apellido = rs.getString("apellido"); // opcional
                    System.out.println("- " + nombre + " " + apellido);
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                System.out.println("❌ Error durante la consulta: " + e.getMessage());
            }
        }
    }
}
