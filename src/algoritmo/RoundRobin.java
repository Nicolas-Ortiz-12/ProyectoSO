package algoritmo;

import modelo.Proceso;
import java.util.*;

public class RoundRobin extends AlgoritmoPlanificacion {
    private final int quantum;

    public RoundRobin(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public void ejecutar(List<Proceso> lista) {
        resetearProcesos(lista);
        Queue<Proceso> cola = new LinkedList<>();
        int tiempo = 0;
        List<Proceso> pendientes = new ArrayList<>(lista);

        while (!pendientes.isEmpty() || !cola.isEmpty()) {
            // Agregar procesos disponibles a la cola
            for (Proceso p : pendientes) {
                if (p.llegada <= tiempo && !cola.contains(p)) {
                    cola.add(p);
                }
            }

            // Eliminar los ya agregados
            Iterator<Proceso> it = pendientes.iterator();
            while (it.hasNext()) {
                Proceso p = it.next();
                if (p.llegada <= tiempo) {
                    it.remove();
                }
            }

            if (cola.isEmpty()) {
                tiempo++;
                continue;
            }

            Proceso actual = cola.poll();
            if (!actual.iniciado) {
                actual.tiempoRespuesta = tiempo - actual.llegada;
                actual.iniciado = true;
            }

            int rafagasRestantes = actual.rafagasTotales - actual.rafagasEjecutadas;
            int tiempoEjecutado = Math.min(quantum, rafagasRestantes);
            actual.rafagasEjecutadas += tiempoEjecutado;
            tiempo += tiempoEjecutado;

            if (actual.estaTerminado()) {
                actual.tiempoRetorno = tiempo - actual.llegada;
                actual.tiempoEspera = actual.tiempoRetorno - actual.rafagasTotales;
            } else {
                cola.add(actual); // vuelve al final
            }
        }
    }
}
