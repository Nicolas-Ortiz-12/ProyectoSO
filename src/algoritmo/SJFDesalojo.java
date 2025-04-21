package algoritmo;

import modelo.Proceso;
import java.util.*;

public class SJFDesalojo extends AlgoritmoPlanificacion {
    @Override
    public void ejecutar(List<Proceso> lista) {
        resetearProcesos(lista);
        int tiempo = 0;
        List<Proceso> pendientes = new ArrayList<>(lista);

        while (!pendientes.isEmpty()) {
            List<Proceso> disponibles = new ArrayList<>();
            for (Proceso p : pendientes) {
                if (p.llegada <= tiempo && !p.estaTerminado()) {
                    disponibles.add(p);
                }
            }

            if (disponibles.isEmpty()) {
                tiempo++;
                continue;
            }

            Proceso actual = disponibles.stream()
                    .min(Comparator.comparingInt(p -> (p.rafagasTotales - p.rafagasEjecutadas)))
                    .get();

            if (!actual.iniciado) {
                actual.tiempoRespuesta = tiempo - actual.llegada;
                actual.iniciado = true;
            }

            actual.rafagasEjecutadas++;
            tiempo++;

            if (actual.estaTerminado()) {
                actual.tiempoRetorno = tiempo - actual.llegada;
                actual.tiempoEspera = actual.tiempoRetorno - actual.rafagasTotales;
            }
        }
    }
}
