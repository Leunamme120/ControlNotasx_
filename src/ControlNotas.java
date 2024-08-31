import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ControlNotas {
    private List<Estudiante> estudiantes;

    public ControlNotas() {
        this.estudiantes = new ArrayList<>();
    }

    public void agregarEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public void guardarEstudiantesEnArchivo(String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(estudiantes);
            System.out.println("Estudiantes guardados en el archivo: " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar estudiantes: " + e.getMessage());
        }
    }

    public void cargarEstudiantesDesdeArchivo(String nombreArchivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            estudiantes = (List<Estudiante>) ois.readObject();
            System.out.println("Estudiantes cargados desde el archivo: " + nombreArchivo);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar estudiantes: " + e.getMessage());
        }
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    String mostrarInformacion() {
        throw new UnsupportedOperationException("NO SOPORTADO");
    }
}
