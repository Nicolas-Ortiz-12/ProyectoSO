package algoritmo;

import modelo.Proceso;
import java.util.*;

public class SJF extends AlgoritmoPlanificacion {
    @Override
    public void ejecutar(List<Proceso> lista) {
        resetearProcesos(lista);
        List<Proceso> pendientes = new ArrayList<>(lista);
        List<Proceso> ejecutados = new ArrayList<>();
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

            Proceso siguiente = disponibles.stream()
                    .min(Comparator.comparingInt(p -> p.rafagasTotales))
                    .get();

            siguiente.tiempoEspera = tiempo - siguiente.llegada;
            tiempo += siguiente.rafagasTotales;
            siguiente.rafagasEjecutadas = siguiente.rafagasTotales;
            siguiente.tiempoRetorno = tiempo - siguiente.llegada;
            pendientes.remove(siguiente);
            ejecutados.add(siguiente);
        }
    }
}
