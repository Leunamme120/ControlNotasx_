import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeParseException;

public class ControlNotasGUI extends JFrame {
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtId;
    private JTextField txtGrado;
    private JTextField txtFechaNacimiento;
    private JTextField txtNota; // Campo para ingresar la nota
    private JComboBox<String> comboBimestre; // Selector de bimestre
    private JComboBox<String> comboEstudiantes; // ComboBox para seleccionar estudiantes
    private JTextArea txtAreaInformacion;
    private ControlNotas controlNotas;

    public ControlNotasGUI() {
        controlNotas = new ControlNotas();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("PENEL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelInputs = new JPanel(new GridLayout(5, 100));
        panelInputs.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelInputs.add(txtNombre);

        panelInputs.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panelInputs.add(txtApellido);

        panelInputs.add(new JLabel("ID:"));
        txtId = new JTextField();
        panelInputs.add(txtId);

        panelInputs.add(new JLabel("Grado:"));
        txtGrado = new JTextField();
        panelInputs.add(txtGrado);

        panelInputs.add(new JLabel("Fecha de Nacimiento (dd/MM/yyyy):"));
        txtFechaNacimiento = new JTextField();
        panelInputs.add(txtFechaNacimiento);

        panelInputs.add(new JLabel("Nota:"));
        txtNota = new JTextField();
        panelInputs.add(txtNota);

        panelInputs.add(new JLabel("Bimestre:"));
        comboBimestre = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        panelInputs.add(comboBimestre);

        panelInputs.add(new JLabel("Seleccionar Estudiante:"));
        comboEstudiantes = new JComboBox<>();
        actualizarListaEstudiantes();
        panelInputs.add(comboEstudiantes);

        JPanel panelBotones = new JPanel();
        JButton btnAgregarEstudiante = new JButton("Agregar Estudiante");
        JButton btnAgregarNota = new JButton("Agregar Nota");
        JButton btnMostrar = new JButton("Mostrar Información");
        JButton btnGuardar = new JButton("Guardar en Archivo");
        JButton btnCargar = new JButton("Cargar desde Archivo");

        panelBotones.add(btnAgregarEstudiante);
        panelBotones.add(btnAgregarNota);
        panelBotones.add(btnMostrar);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCargar);


        txtAreaInformacion = new JTextArea(10, 75);
        JScrollPane scrollPane = new JScrollPane(txtAreaInformacion);

        add(panelInputs, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        btnAgregarEstudiante.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarEstudiante();
            }
        });

        btnAgregarNota.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarNota();
            }
        });

        btnMostrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInformacion();
            }
        });

        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlNotas.guardarEstudiantesEnArchivo("estudiantes.dat");
            }
        });

        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controlNotas.cargarEstudiantesDesdeArchivo("estudiantes.dat");
                mostrarInformacion();
                actualizarListaEstudiantes();
            }
        });

        pack();
        setLocationRelativeTo(null); // Centrar ventana
    }

    private void agregarEstudiante() {
        try {
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();
            String id = txtId.getText();
            String grado = txtGrado.getText();
            String fechaNacimientoStr = txtFechaNacimiento.getText();

            if (!id.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "El ID debe tener exactamente 4 dígitos.");
            return;
            }
            
            if (nombre.isEmpty() || apellido.isEmpty() || id.isEmpty() || grado.isEmpty() || fechaNacimientoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            Estudiante estudiante = new Estudiante(nombre, apellido, id, grado, fechaNacimientoStr);
            controlNotas.agregarEstudiante(estudiante);
            JOptionPane.showMessageDialog(this, "Estudiante agregado correctamente.");
            limpiarCampos();
            actualizarListaEstudiantes(); // Actualizar ComboBox después de agregar un estudiante
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Error en el formato de la fecha. Utilice el formato yyyy-MM-dd.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar estudiante: " + ex.getMessage());
        }
    }

    private void agregarNota() {
        try {
            String id = (String) comboEstudiantes.getSelectedItem(); // Obtener ID del estudiante seleccionado
            double nota = Double.parseDouble(txtNota.getText());
            int bimestre = comboBimestre.getSelectedIndex() + 1;

            Estudiante estudiante = buscarEstudiantePorId(id);
            if (estudiante != null) {
                estudiante.agregarNota(bimestre, nota);
                JOptionPane.showMessageDialog(this, "Nota agregada correctamente.");
                txtNota.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Estudiante no encontrado.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, introduzca un número válido para la nota.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar nota: " + ex.getMessage());
        }
    }

    private Estudiante buscarEstudiantePorId(String id) {
        for (Estudiante estudiante : controlNotas.getEstudiantes()) {
            if (estudiante.getId().equals(id)) {
                return estudiante;
            }
        }
        return null;
    }

    private void mostrarInformacion() {
        StringBuilder informacion = new StringBuilder();
        for (Estudiante estudiante : controlNotas.getEstudiantes()) {
            informacion.append(estudiante.toString())
                    .append(", Promedio Anual: ").append(estudiante.calcularPromedioAnual())
                    .append("\n");
        }
        txtAreaInformacion.setText(informacion.toString());
    }

    private void actualizarListaEstudiantes() {
        comboEstudiantes.removeAllItems();
        for (Estudiante estudiante : controlNotas.getEstudiantes()) {
            comboEstudiantes.addItem(estudiante.getId());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtId.setText("");
        txtGrado.setText("");
        txtFechaNacimiento.setText("");
        txtNota.setText("");
        comboBimestre.setSelectedIndex(0);
        comboEstudiantes.setSelectedIndex(-1);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ControlNotasGUI().setVisible(true);
            }
        });
    }
}
