package algoritmo;

import modelo.Proceso;
import java.util.*;

public class FCFS extends AlgoritmoPlanificacion {
    @Override
    public void ejecutar(List<Proceso> lista) {
        //resetea los procesos
        resetearProcesos(lista);
        //ordena los procesos por llegada
        lista.sort(Comparator.comparingInt(p -> p.llegada));
        //calcula los tiempos
        int tiempo = 0;
        for (Proceso p : lista) {
            // si el proceso no ha llegado el sistema espera hasta que llegue
            if (tiempo < p.llegada) tiempo = p.llegada;
            p.tiempoEspera = tiempo - p.llegada;
            tiempo += p.rafagasTotales;
            p.rafagasEjecutadas = p.rafagasTotales;
            p.tiempoRetorno = tiempo - p.llegada;
        }
    }
}
