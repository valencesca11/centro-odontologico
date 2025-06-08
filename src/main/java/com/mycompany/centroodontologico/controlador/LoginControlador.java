package com.mycompany.centroodontologico.controlador;

import com.mycompany.centroodontologico.modelo.Usuario;
import javax.swing.JOptionPane;

public class LoginControlador {

    public void validarLogin(String usuario, String contrasena) {
        Usuario u = new Usuario(usuario, contrasena);

        if (u.validar()) {
            JOptionPane.showMessageDialog(null, "✅ Bienvenida/o al sistema, " + usuario);
            new MenuAdministracionControlador(); // ← este es el cambio correcto
        } else {
            JOptionPane.showMessageDialog(null, "❌ Usuario o contraseña incorrectos.");
        }
    }
}