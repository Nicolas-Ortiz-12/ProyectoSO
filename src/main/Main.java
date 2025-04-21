package main;
import interfaz.VentanaPlanificador;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new VentanaPlanificador().setVisible(true);
        });
    }
}
