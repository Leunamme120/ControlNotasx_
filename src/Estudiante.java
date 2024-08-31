import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class Estudiante implements Serializable {
    private String nombre;
    private String apellido;
    private String id;
    private String grado;
    private LocalDate fechaNacimiento;
    private Map<Integer, Double> notas;

    public Estudiante(String nombre, String apellido, String id, String grado, String fechaNacimientoStr) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.grado = grado;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            this.fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha de nacimiento no válida. Utilice el formato dd/MM/yyyy.");
        }
        this.notas = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void agregarNota(int bimestre, double nota) {
        notas.put(bimestre, nota);
    }

    public double calcularPromedioAnual() {
        double suma = 0;
        for (double nota : notas.values()) {
            suma += nota;
        }
        return notas.size() > 0 ? suma / notas.size() : 0;
    }

    public boolean estaAprobado() {
        return calcularPromedioAnual() >= 60;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(nombre).append(" ").append(apellido)
          .append(", ID: ").append(id)
          .append(", Grado: ").append(grado)
          .append(", Fecha de Nacimiento: ").append(fechaNacimiento)
          .append("\nNotas:\n");
        
        for (int bimestre = 1; bimestre <= 4; bimestre++) {
            sb.append("Bimestre ").append(bimestre).append(": ")
              .append(notas.containsKey(bimestre) ? notas.get(bimestre) : "No ingresada").append("\n");
        }
        
        sb.append("Promedio Anual: ").append(calcularPromedioAnual())
          .append(" - ").append(estaAprobado() ? "Aprobado" : "No Aprobado")
          .append("\n");
        return sb.toString();
    }
}
