package sied;

import java.time.LocalDate;

public class EntidadDeportiva {
    
    private final String nombre;
    private final LocalDate fundación;
    private final String ubicación;
    private double capitalGeneral, deudaGeneral;
    private float proyecciónIngresos, gastosInfrServicios, gastosSecciones, gastosRecHum, gastosDTsRecHum, ingresosEntradas,
            ingresosPatrocinios, ingresosVentas;

    public EntidadDeportiva(String nombre, LocalDate fundación, String ubicación, double capitalGeneral, 
            double deudaGeneral, float gastosInfrServicios, float gastosSecciones, float gastosRecHum, float gastosDTsRecHum, 
            float proyecciónIngresos, float ingresosEntradas,float ingresosPatrocinios, float ingresosVentas) {
        this.nombre = nombre;
        this.fundación = fundación;
        this.ubicación = ubicación;
        this.capitalGeneral = capitalGeneral;
        this.deudaGeneral = deudaGeneral;
        this.gastosInfrServicios = gastosInfrServicios;
        this.gastosSecciones=gastosSecciones;
        this.gastosRecHum = gastosRecHum;
        this.gastosDTsRecHum=gastosDTsRecHum;
        this.proyecciónIngresos = proyecciónIngresos;
        this.ingresosEntradas=ingresosEntradas;
        this.ingresosPatrocinios=ingresosPatrocinios;
        this.ingresosVentas=ingresosVentas;
    }

    /*
    public EntidadDeportiva(){
        this.nombre="Reus United";
        this.fundación=LocalDate.of(1899, 11, 22);
        this.ubicación="Reus, España";
        this.capitalGeneral=20000000;
        this.proyecciónIngresos=(float) 0.9;
        this.deudaGeneral=11000000;
        dts=new PointedList();
        secciones=new PointedList();
        patrocinadores=new PointedList();
    }
    */
    public double getCapitalGeneral() {
        return capitalGeneral;
    }

    public void setCapitalGeneral(double capitalGeneral) {
        this.capitalGeneral = capitalGeneral;
    }

    public float getProyecciónIngresos() {
        return proyecciónIngresos;
    }

    public void setProyecciónIngresos(float proyecciónIngresos) {
        this.proyecciónIngresos = proyecciónIngresos;
    }

    public double getDeudaGeneral() {
        return deudaGeneral;
    }

    public void setDeudaGeneral(double deudaGeneral) {
        this.deudaGeneral = deudaGeneral;
    }

    public float getGastosInfrServicios() {
        return gastosInfrServicios;
    }

    public void setGastosInfrServicios(float gastosInfrServicios) {
        this.gastosInfrServicios = gastosInfrServicios;
    }

    public float getGastosRecHum() {
        return gastosRecHum;
    }

    public void setGastosRecHum(float gastosRecHum) {
        this.gastosRecHum = gastosRecHum;
    }

    public LocalDate getFundación() {
        return fundación;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUbicación() {
        return ubicación;
    }

    public float getGastosSecciones() {
        return gastosSecciones;
    }

    public void setGastosSecciones(float gastosSecciones) {
        this.gastosSecciones = gastosSecciones;
    }

    public float getGastosDTsRecHum() {
        return gastosDTsRecHum;
    }

    public void setGastosDTsRecHum(float gastosDTsRecHum) {
        this.gastosDTsRecHum = gastosDTsRecHum;
    }

    public float getIngresosEntradas() {
        return ingresosEntradas;
    }

    public void setIngresosEntradas(float ingresosEntradas) {
        this.ingresosEntradas = ingresosEntradas;
    }

    public float getIngresosPatrocinios() {
        return ingresosPatrocinios;
    }

    public void setIngresosPatrocinios(float ingresosPatrocinios) {
        this.ingresosPatrocinios = ingresosPatrocinios;
    }

    public float getIngresosVentas() {
        return ingresosVentas;
    }

    public void setIngresosVentas(float ingresosVentas) {
        this.ingresosVentas = ingresosVentas;
    }
    
}