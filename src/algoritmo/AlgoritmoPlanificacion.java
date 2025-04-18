package algoritmo;

import modelo.Proceso;
import java.util.List;

public abstract class AlgoritmoPlanificacion {
    public abstract void ejecutar(List<Proceso> lista);
    protected void resetearProcesos(List<Proceso> lista) {
        for (Proceso p : lista) {
            p.rafagasEjecutadas = 0;
            p.tiempoEspera = 0;
            p.tiempoRetorno = 0;
            p.tiempoRespuesta = -1;
            p.iniciado = false;
        }
    }
}
