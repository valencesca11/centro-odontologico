package com.mycompany.centroodontologico;
import com.mycompany.centroodontologico.vista.LoginFrame;
import javax.swing.SwingUtilities;

/**
 * Clase principal del sistema odontológico.
 * Desde aquí se lanza la interfaz de inicio de sesión.
 */
public class CentroOdontologico {

    public static void main(String[] args) {
        // Ejecuta la interfaz de usuario en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
