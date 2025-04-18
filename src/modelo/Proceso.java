package modelo;

public class Proceso {
    public String nombre;
    public int llegada;
    public int rafagasTotales;
    public int rafagasEjecutadas;
    public int prioridad;

    public int tiempoEspera;
    public int tiempoRetorno;
    public int tiempoRespuesta;
    public boolean iniciado;

    public Proceso(String nombre, int llegada, int rafagasTotales, int prioridad) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.rafagasTotales = rafagasTotales;
        this.rafagasEjecutadas = 0;
        this.prioridad = prioridad;
        this.tiempoEspera = 0;
        this.tiempoRetorno = 0;
        this.tiempoRespuesta = -1;
        this.iniciado = false;
    }

    public boolean estaTerminado() {
        return rafagasEjecutadas >= rafagasTotales;
    }

    @Override
    public String toString() {
        return "Proceso{" +
                "nombre='" + nombre + '\'' +
                ", llegada=" + llegada +
                ", ráfagas=" + rafagasTotales +
                ", prioridad=" + prioridad +
                '}';
    }
}
