package sied;

public class Patrocinador {
    
    private int nif;
    private String nombre;
    private double retorno;
    private Sección sección;

    public Patrocinador(int nif, String nombre, double retorno) {
        this.nif = nif;
        this.nombre = nombre;
        this.retorno = retorno;
    }
    public Patrocinador(int nif, String nombre, double retorno, Sección sección) {
        this(nif, nombre, retorno);
        this.sección=sección;
    }

    public int getNif() {
        return nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getRetorno() {
        return retorno;
    }

    public void setRetorno(double retorno) {
        this.retorno = retorno;
    }

    public Sección getSección() {
        return sección;
    }

    public void setSección(Sección sección) {
        this.sección = sección;
    }
    
}
