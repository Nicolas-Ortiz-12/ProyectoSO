package interfaz;

import algoritmo.*;
import modelo.Proceso;
import util.GanttChart;
import util.LectorCSV;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class VentanaPlanificador extends JFrame {

    private List<Proceso> listaProcesos;
    private List<JCheckBox> checkboxesAlgoritmos = new ArrayList<>();
    private JTextArea areaResultado;
    private JPanel panelChecks;
    private JButton botonCargarCSV;

    public VentanaPlanificador() {
        setTitle("Planificador de Procesos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializar con lista vacía
        listaProcesos = new ArrayList<>();

        inicializarComponentes();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void inicializarComponentes() {
        panelChecks = new JPanel();
        panelChecks.setLayout(new BoxLayout(panelChecks, BoxLayout.Y_AXIS));

        String[] algoritmos = {"FCFS", "SJF", "SJF Desalojo", "Prioridad", "Round Robin", "HRRN", "Cola Multinivel"};

        for (String nombre : algoritmos) {
            JCheckBox check = new JCheckBox(nombre);
            checkboxesAlgoritmos.add(check);
            panelChecks.add(check);
        }

        // Botón para cargar CSV
        botonCargarCSV = new JButton("Cargar CSV");
        botonCargarCSV.addActionListener(this::cargarCSV);

        JButton botonEjecutar = new JButton("Ejecutar");
        botonEjecutar.addActionListener(this::ejecutarAlgoritmo);

        areaResultado = new JTextArea(25, 80);
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(areaResultado);

        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(botonCargarCSV);
        panelBotones.add(botonEjecutar);

        add(panelChecks, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarCSV(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            String rutaArchivo = fileChooser.getSelectedFile().getPath();
            listaProcesos = LectorCSV.leerProcesos(rutaArchivo);

            if (listaProcesos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se pudieron cargar procesos del archivo",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Procesos cargados correctamente: " + listaProcesos.size(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                mostrarProcesosCargados();
            }
        }
    }

    private void mostrarProcesosCargados() {
        StringBuilder sb = new StringBuilder("Procesos cargados:\n");
        sb.append(String.format("%-10s %-10s %-10s %-10s\n",
                "Nombre", "Ráfagas", "Llegada", "Prioridad"));

        for (Proceso p : listaProcesos) {
            sb.append(String.format("%-10s %-10d %-10d %-10d\n",
                    p.nombre, p.rafagasTotales, p.llegada, p.prioridad));
        }

        areaResultado.setText(sb.toString());
    }

    private void ejecutarAlgoritmo(ActionEvent e) {
        if (listaProcesos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay procesos cargados. Por favor, cargue un archivo CSV primero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder resultados = new StringBuilder();
        String[] algoritmosDisponibles = {"FCFS", "SJF", "SJF Desalojo", "Prioridad", "Round Robin", "HRRN", "Cola Multinivel"};

        for (int i = 0; i < algoritmosDisponibles.length; i++) {
            JCheckBox check = checkboxesAlgoritmos.get(i);
            String algNombre = algoritmosDisponibles[i];

            if (check.isSelected()) {
                // Copia de la lista original
                List<Proceso> copia = new ArrayList<>();
                for (Proceso p : listaProcesos) {
                    copia.add(new Proceso(p.nombre, p.llegada, p.rafagasTotales, p.prioridad));
                }

                AlgoritmoPlanificacion algoritmo = switch (algNombre) {
                    case "FCFS" -> new FCFS();
                    case "SJF" -> new SJF();
                    case "SJF Desalojo" -> new SJFDesalojo();
                    case "Prioridad" -> new Prioridad();
                    case "Round Robin" -> new RoundRobin(2); // puedes modificar el quantum
                    case "HRRN" -> new HRRN();
                    case "Cola Multinivel" -> new ColaMultinivel(List.of(new FCFS(), new RoundRobin(2)));
                    default -> null;
                };

                if (algoritmo != null) {
                    algoritmo.ejecutar(copia);
                    resultados.append("\nAlgoritmo: ").append(algNombre).append("\n");
                    resultados.append(GanttChart.generarTexto(copia)).append("\n");
                }
            }
        }

        if (resultados.length() == 0) {
            areaResultado.setText("No se seleccionó ningún algoritmo para ejecutar.");
        } else {
            areaResultado.setText(resultados.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPlanificador::new);
    }
}