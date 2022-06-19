package sied;

public class DirectorTécnico {
    
    private int dni;
    private String nombre;
    private double salario;
    private Sección sección;

    public DirectorTécnico(int dni, String nombre, double salario) {
        this.dni = dni;
        this.nombre = nombre;
        this.salario = salario;
    }

    public DirectorTécnico(int dni, String nombre, double salario, Sección sección) {
        this(dni, nombre, salario);
        this.sección=sección;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Sección getSección() {
        return sección;
    }

    public void setSección(Sección sección) {
        this.sección = sección;
    }
    
}
