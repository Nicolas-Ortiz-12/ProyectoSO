package algoritmo;

import modelo.Proceso;
import java.util.*;

public class Prioridad extends AlgoritmoPlanificacion {
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

            Proceso actual = disponibles.stream()
                    .min(Comparator.comparingInt(p -> p.prioridad))
                    .get();

            actual.tiempoEspera = tiempo - actual.llegada;
            tiempo += actual.rafagasTotales;
            actual.rafagasEjecutadas = actual.rafagasTotales;
            actual.tiempoRetorno = tiempo - actual.llegada;
            pendientes.remove(actual);
        }
    }
}
