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
    private JPanel panelMultinivel;
    private List<JComboBox<String>> combosNiveles;
    private List<JTextField> camposQuantumNiveles;


    public VentanaPlanificador() {
        setTitle("Planificador de Procesos");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout());

        comboAlgoritmo = new JComboBox<>(new String[]{
                "FCFS", "SJF", "SJF Desalojo", "Prioridad", "Round Robin", "HRRN", "Cola Multinivel"
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

        JPanel panelCentral = new JPanel(new BorderLayout());


        // Tabla
        tablaProcesos = new JTable();
        JScrollPane scrollTabla = new JScrollPane(tablaProcesos);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);


        panelMultinivel = new JPanel();
        panelMultinivel.setLayout(new FlowLayout());
        panelMultinivel.setBorder(BorderFactory.createTitledBorder("Configuración Cola Multinivel"));
        panelMultinivel.setVisible(false);

        combosNiveles = new ArrayList<>();
        camposQuantumNiveles = new ArrayList<>();

        JButton btnAgregarNivel = new JButton("Agregar Nivel");
        btnAgregarNivel.addActionListener(e -> agregarNivelMultinivel());

        panelMultinivel.add(btnAgregarNivel);
        panelCentral.add(panelMultinivel, BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);



        // Área de resultado
        areaResultado = new JTextArea(10, 70);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollArea = new JScrollPane(areaResultado);
        add(scrollArea, BorderLayout.SOUTH);



        botonCargar.addActionListener(e -> cargarCSV());
        botonEjecutar.addActionListener(e -> ejecutarAlgoritmo());
        comboAlgoritmo.addActionListener(e -> {
            String seleccion = (String) comboAlgoritmo.getSelectedItem();
            panelMultinivel.setVisible("Cola Multinivel".equals(seleccion));
            campoQuantum.setVisible(!"Cola Multinivel".equals(seleccion));
            pack();
        });

        pack();

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

    private void agregarNivelMultinivel() {
        JPanel nivelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JComboBox<String> comboNivel = new JComboBox<>(new String[]{
                "FCFS", "SJF", "SJF Desalojo", "Prioridad", "Round Robin", "HRRN"
        });

        JTextField campoQ = new JTextField(3);
        campoQ.setText("4");

        combosNiveles.add(comboNivel);
        camposQuantumNiveles.add(campoQ);

        nivelPanel.add(new JLabel("Nivel " + combosNiveles.size() + ":"));
        nivelPanel.add(comboNivel);
        nivelPanel.add(new JLabel("Q:"));
        nivelPanel.add(campoQ);

        // Botón para eliminar nivel
        JButton btnEliminar = new JButton("X");
        btnEliminar.addActionListener(e -> {
            panelMultinivel.remove(nivelPanel);
            combosNiveles.remove(comboNivel);
            camposQuantumNiveles.remove(campoQ);
            panelMultinivel.revalidate();
            panelMultinivel.repaint();
            pack();
        });
        nivelPanel.add(btnEliminar);

        panelMultinivel.add(nivelPanel);
        panelMultinivel.revalidate();
        panelMultinivel.repaint();
        pack();
    }



    private void ejecutarAlgoritmo() {
        if (listaProcesos == null) return;

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
            case "Cola Multinivel" -> {
                List<AlgoritmoPlanificacion> algoritmos = new ArrayList<>();
                for (int i = 0; i < combosNiveles.size(); i++) {
                    String alg = (String) combosNiveles.get(i).getSelectedItem();
                    AlgoritmoPlanificacion nivel = switch (alg) {
                        case "FCFS" -> new FCFS();
                        case "SJF" -> new SJF();
                        case "SJF Desalojo" -> new SJFDesalojo();
                        case "Prioridad" -> new Prioridad();
                        case "Round Robin" -> new RoundRobin(
                                Integer.parseInt(camposQuantumNiveles.get(i).getText()));
                        case "HRRN" -> new HRRN();
                        default -> null;
                    };
                    if (nivel != null) algoritmos.add(nivel);
                }
                yield new ColaMultinivel(algoritmos);
            }
            default -> null;
        };

        if (algoritmo != null) {
            algoritmo.ejecutar(copia);
            String resultado = GanttChart.generarTexto(copia);
            areaResultado.setText(resultado);
        }
    }
}

