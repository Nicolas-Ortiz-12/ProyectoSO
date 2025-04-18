package util;

import modelo.Proceso;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV {
    public static List<Proceso> leerProcesos(String nombreArchivo) {
        List<Proceso> procesos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            br.readLine(); // Saltar encabezado

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 4) {
                    try {
                        String nombre = partes[0].trim();
                        int rafagas = Integer.parseInt(partes[1].trim());
                        int llegada = Integer.parseInt(partes[2].trim());
                        int prioridad = Integer.parseInt(partes[3].trim());
                        procesos.add(new Proceso(nombre, llegada, rafagas, prioridad));
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico en línea: " + linea);
                    }
                } else {
                    System.err.println("Línea incompleta: " + linea);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return procesos;
    }
}
