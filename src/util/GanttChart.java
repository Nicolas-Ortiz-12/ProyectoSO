package util;

import modelo.Proceso;
import java.util.*;

public class GanttChart {

    public static void mostrar(List<Proceso> lista) {
        // Paso 1: Crear la línea de ejecución
        List<String> timeline = new ArrayList<>();
        List<String> procesosOrdenados = new ArrayList<>();

        // Repetimos cada proceso según sus ráfagas ejecutadas
        for (Proceso p : lista) {
            for (int i = 0; i < p.rafagasEjecutadas; i++) {
                timeline.add(p.nombre);
            }
        }

        // Paso 2: Línea superior
        System.out.println("Diagrama de Gantt:");
        System.out.print(" ");
        for (String nombre : timeline) {
            System.out.print("───");
        }
        System.out.println();

        // Paso 3: Nombres de procesos
        System.out.print("|");
        for (String nombre : timeline) {
            System.out.print(" " + nombre + " |");
        }
        System.out.println();

        // Paso 4: Línea inferior
        System.out.print(" ");
        for (String nombre : timeline) {
            System.out.print("───");
        }
        System.out.println();

        // Paso 5: Línea de tiempo
        System.out.print("0");
        for (int i = 1; i <= timeline.size(); i++) {
            System.out.print(String.format("%4d", i));
        }
        System.out.println();

        // Paso 6: Mostrar tabla de tiempos
        System.out.println("\nResumen de tiempos:");
        System.out.printf("%-10s %-10s %-10s %-10s\n", "Proceso", "Llegada", "Espera", "Retorno");
        for (Proceso p : lista) {
            System.out.printf("%-10s %-10d %-10d %-10d\n", p.nombre, p.llegada, p.tiempoEspera, p.tiempoRetorno);
        }

        // Paso 7: Calcular promedios
        double totalEspera = 0;
        double totalRetorno = 0;
        for (Proceso p : lista) {
            totalEspera += p.tiempoEspera;
            totalRetorno += p.tiempoRetorno;
        }

        System.out.printf("\nTiempo de Espera Promedio: %.2f\n", totalEspera / lista.size());
        System.out.printf("Tiempo de Retorno Promedio: %.2f\n", totalRetorno / lista.size());
    }
}
