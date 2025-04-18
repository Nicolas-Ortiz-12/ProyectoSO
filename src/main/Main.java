package main;

import algoritmo.FCFS;
import algoritmo.SJF;
import modelo.Proceso;
import util.GanttChart;
import util.LectorCSV;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Proceso> lista = LectorCSV.leerProcesos("procesos.csv");

        SJF sjf = new SJF();
        sjf.ejecutar(lista);

        GanttChart.mostrar(lista);
    }
}
