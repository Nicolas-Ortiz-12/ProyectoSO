package util;

import modelo.Proceso;
import java.util.*;

public class GanttChart {

    public static String generarTexto(List<Proceso> lista) {
        StringBuilder sb = new StringBuilder();
        List<String> timeline = new ArrayList<>();

        for (Proceso p : lista) {
            for (int i = 0; i < p.rafagasEjecutadas; i++) {
                timeline.add(p.nombre);
            }
        }

        sb.append("Diagrama de Gantt:\n ");
        for (String nombre : timeline) sb.append("───");
        sb.append("\n|");
        for (String nombre : timeline) sb.append(" " + nombre + " |");
        sb.append("\n ");
        for (String nombre : timeline) sb.append("───");
        sb.append("\n0");
        for (int i = 1; i <= timeline.size(); i++) sb.append(String.format("%4d", i));
        sb.append("\n\nResumen de tiempos:\n");
        sb.append(String.format("%-10s %-10s %-10s %-10s\n", "Proceso", "Llegada", "Espera", "Retorno"));
        for (Proceso p : lista) {
            sb.append(String.format("%-10s %-10d %-10d %-10d\n", p.nombre, p.llegada, p.tiempoEspera, p.tiempoRetorno));
        }

        double totalEspera = 0, totalRetorno = 0;
        for (Proceso p : lista) {
            totalEspera += p.tiempoEspera;
            totalRetorno += p.tiempoRetorno;
        }
        sb.append(String.format("\nTiempo de Espera Promedio: %.2f\n", totalEspera / lista.size()));
        sb.append(String.format("Tiempo de Retorno Promedio: %.2f\n", totalRetorno / lista.size()));

        return sb.toString();
    }

}
