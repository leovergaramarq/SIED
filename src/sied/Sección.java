package sied;

public class Sección {
    
    private int código;
    private String nombre;
    private double presupuesto;
    private DirectorTécnico dt;
    private Patrocinador patr;

    public Sección(int código, String nombre, double presupuesto) {
        this.código = código;
        this.nombre = nombre;
        this.presupuesto = presupuesto;
    }

    public Sección(int código, String nombre, double presupuesto, DirectorTécnico dt, Patrocinador patr) {
        this(código, nombre, presupuesto);
        this.dt=dt;
        this.patr=patr;
    }

    public int getCódigo() {
        return código;
    }

    public void setCódigo(int código) {
        this.código = código;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public DirectorTécnico getDt() {
        return dt;
    }

    public void setDt(DirectorTécnico dt) {
        this.dt = dt;
    }

    public Patrocinador getPatr() {
        return patr;
    }

    public void setPatr(Patrocinador patr) {
        this.patr = patr;
    }

}
