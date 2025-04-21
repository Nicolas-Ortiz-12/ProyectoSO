package algoritmo;

import modelo.Proceso;
import java.util.*;

public class ColaMultinivel extends AlgoritmoPlanificacion {
    private final AlgoritmoPlanificacion[] colas = new AlgoritmoPlanificacion[3];

    public ColaMultinivel(AlgoritmoPlanificacion cola1, AlgoritmoPlanificacion cola2, AlgoritmoPlanificacion cola3) {
        colas[0] = cola1;
        colas[1] = cola2;
        colas[2] = cola3;
    }

    @Override
    public void ejecutar(List<Proceso> lista) {
        resetearProcesos(lista);
        List<Proceso> cola1 = new ArrayList<>();
        List<Proceso> cola2 = new ArrayList<>();
        List<Proceso> cola3 = new ArrayList<>();

        for (Proceso p : lista) {
            if (p.prioridad == 1) cola1.add(p);
            else if (p.prioridad == 2) cola2.add(p);
            else cola3.add(p);
        }

        colas[0].ejecutar(cola1);
        colas[1].ejecutar(cola2);
        colas[2].ejecutar(cola3);

        // Combinar resultados
        lista.clear();
        lista.addAll(cola1);
        lista.addAll(cola2);
        lista.addAll(cola3);
    }
}
