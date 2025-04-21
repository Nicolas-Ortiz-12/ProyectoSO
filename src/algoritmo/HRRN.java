package algoritmo;

import modelo.Proceso;
import java.util.*;

public class HRRN extends AlgoritmoPlanificacion {
    @Override
    public void ejecutar(List<Proceso> lista) {
        resetearProcesos(lista);
        List<Proceso> pendientes = new ArrayList<>(lista);
        int tiempo = 0;

        while (!pendientes.isEmpty()) {
            List<Proceso> disponibles = new ArrayList<>();
            for (Proceso p : pendientes) {
                if (p.llegada <= tiempo) {
                    disponibles.add(p);
                }
            }

            if (disponibles.isEmpty()) {
                tiempo++;
                continue;
            }

            // Calcular el índice de respuesta para cada disponible y elegir el mayor
            Proceso siguiente = null;
            double mayorIndice = -1;

            for (Proceso p : disponibles) {
                double espera = tiempo - p.llegada;
                double respuesta = (espera + p.rafagasTotales) / (double) p.rafagasTotales;
                if (respuesta > mayorIndice) {
                    mayorIndice = respuesta;
                    siguiente = p;
                }
            }

            // Ejecutar el proceso seleccionado
            if (siguiente != null) {
                siguiente.tiempoEspera = tiempo - siguiente.llegada;
                tiempo += siguiente.rafagasTotales;
                siguiente.rafagasEjecutadas = siguiente.rafagasTotales;
                siguiente.tiempoRetorno = tiempo - siguiente.llegada;
                pendientes.remove(siguiente);
            }
        }
    }
}
