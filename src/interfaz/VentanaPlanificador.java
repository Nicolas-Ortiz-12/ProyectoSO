package interfaz;

import algoritmo.*;
import modelo.Proceso;
import util.GanttChart;
import util.LectorCSV;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class VentanaPlanificador extends JFrame {

    private JTable tablaProcesos;
    private JTextArea areaResultado;
    private JComboBox<String> comboAlgoritmo;
    private JTextField campoQuantum;
    private JButton botonCargar, botonEjecutar;
    private List<Proceso> listaProcesos;

    public VentanaPlanificador() {
        setTitle("Planificador de Procesos");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout());

        comboAlgoritmo = new JComboBox<>(new String[]{
                "FCFS", "SJF", "SJF Desalojo", "Prioridad", "Round Robin", "HRRN"
        });

        campoQuantum = new JTextField(3);
        campoQuantum.setText("4");

        botonCargar = new JButton("Cargar CSV");
        botonEjecutar = new JButton("Ejecutar");

        panelSuperior.add(new JLabel("Algoritmo:"));
        panelSuperior.add(comboAlgoritmo);
        panelSuperior.add(new JLabel("Q:"));
        panelSuperior.add(campoQuantum);
        panelSuperior.add(botonCargar);
        panelSuperior.add(botonEjecutar);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla
        tablaProcesos = new JTable();
        JScrollPane scrollTabla = new JScrollPane(tablaProcesos);
        add(scrollTabla, BorderLayout.CENTER);

        // Área de resultado
        areaResultado = new JTextArea(10, 70);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollArea = new JScrollPane(areaResultado);
        add(scrollArea, BorderLayout.SOUTH);

        // Eventos
        botonCargar.addActionListener(e -> cargarCSV());
        botonEjecutar.addActionListener(e -> ejecutarAlgoritmo());
    }

    private void cargarCSV() {
        JFileChooser chooser = new JFileChooser();
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            String ruta = chooser.getSelectedFile().getAbsolutePath();
            listaProcesos = LectorCSV.leerProcesos(ruta);
            mostrarTabla(listaProcesos);
        }
    }

    private void mostrarTabla(List<Proceso> lista) {
        String[] columnas = {"Nombre", "Llegada", "Ráfagas", "Prioridad"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Proceso p : lista) {
            modelo.addRow(new Object[]{p.nombre, p.llegada, p.rafagasTotales, p.prioridad});
        }

        tablaProcesos.setModel(modelo);
    }

    private void ejecutarAlgoritmo() {
        if (listaProcesos == null) return;

        // Clonar lista original
        List<Proceso> copia = new ArrayList<>();
        for (Proceso p : listaProcesos) {
            copia.add(new Proceso(p.nombre, p.llegada, p.rafagasTotales, p.prioridad));
        }

        String seleccion = (String) comboAlgoritmo.getSelectedItem();
        AlgoritmoPlanificacion algoritmo = switch (seleccion) {
            case "FCFS" -> new FCFS();
            case "SJF" -> new SJF();
            case "SJF Desalojo" -> new SJFDesalojo();
            case "Prioridad" -> new Prioridad();
            case "Round Robin" -> {
                int q = Integer.parseInt(campoQuantum.getText());
                yield new RoundRobin(q);
            }
            case "HRRN" -> new HRRN();
            default -> null;
        };

        if (algoritmo != null) {
            algoritmo.ejecutar(copia);
            String resultado = GanttChart.generarTexto(copia); // versión que devuelve String
            areaResultado.setText(resultado);
        }
    }
}
