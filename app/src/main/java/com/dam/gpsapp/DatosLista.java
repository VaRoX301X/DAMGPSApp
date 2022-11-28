package com.dam.gpsapp;

public class DatosLista {
    private String fecha;
    private String latitud;
    private String longitud;

    public DatosLista() {
    }

    public DatosLista(String fecha, String latitud, String longitud) {
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
