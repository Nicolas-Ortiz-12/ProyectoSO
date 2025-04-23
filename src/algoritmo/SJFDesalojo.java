package algoritmo;

import modelo.Proceso;
import java.util.*;

public class SJFDesalojo extends AlgoritmoPlanificacion {
    public List<String> ejecucion = new ArrayList<>();

    @Override
    public void ejecutar(List<Proceso> lista) {
        resetearProcesos(lista);
        int tiempo = 0;
        List<Proceso> pendientes = new ArrayList<>(lista);
        Proceso actual = null;

        while (!pendientes.isEmpty() || actual != null) {

            List<Proceso> disponibles = new ArrayList<>();
            for (Proceso p : pendientes) {
                if (p.llegada <= tiempo && !p.estaTerminado()) {
                    disponibles.add(p);
                }
            }

            if (actual != null && !actual.estaTerminado()) {
                disponibles.add(actual);
            }

            if (disponibles.isEmpty()) {
                ejecucion.add("-");
                tiempo++;
                actual = null;
                continue;
            }

            actual = disponibles.stream()
                    .min(Comparator.comparingInt(p -> p.rafagasTotales - p.rafagasEjecutadas))
                    .get();

            if (!actual.iniciado) {
                actual.tiempoRespuesta = tiempo - actual.llegada;
                actual.iniciado = true;
            }

            actual.rafagasEjecutadas++;
            ejecucion.add(actual.nombre);
            tiempo++;

            if (actual.estaTerminado()) {
                actual.tiempoRetorno = tiempo - actual.llegada;
                actual.tiempoEspera = actual.tiempoRetorno - actual.rafagasTotales;
                pendientes.remove(actual);
                actual = null;
            }
        }
    }

    public List<String> getEjecucion() {
        return ejecucion;
    }
}
