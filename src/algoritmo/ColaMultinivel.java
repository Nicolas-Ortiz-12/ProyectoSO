package algoritmo;

import modelo.Proceso;
import java.util.*;
public class ColaMultinivel extends AlgoritmoPlanificacion {
    private final List<AlgoritmoPlanificacion> niveles;
    private final List<List<Proceso>> colasPorNivel;

    public ColaMultinivel(List<AlgoritmoPlanificacion> algoritmos) {
        this.niveles = new ArrayList<>(algoritmos);
        this.colasPorNivel = new ArrayList<>();
        for (int i = 0; i < algoritmos.size(); i++) {
            colasPorNivel.add(new ArrayList<>());
        }
    }

    @Override
    public void ejecutar(List<Proceso> lista) {
        resetearProcesos(lista);

        // Distribuir procesos según prioridad
        for (Proceso p : lista) {
            int nivel = Math.min(p.prioridad, niveles.size() - 1);
            colasPorNivel.get(nivel).add(p);
        }

        // Ejecutar cada nivel
        int tiempoTotal = 0;
        for (int i = 0; i < niveles.size(); i++) {
            List<Proceso> procesosNivel = colasPorNivel.get(i);
            if (!procesosNivel.isEmpty()) {
                niveles.get(i).ejecutar(procesosNivel);

                // Actualizar tiempo total
                for (Proceso p : procesosNivel) {
                    tiempoTotal = Math.max(tiempoTotal, p.llegada + p.tiempoRetorno);
                }
            }
        }
    }
}
