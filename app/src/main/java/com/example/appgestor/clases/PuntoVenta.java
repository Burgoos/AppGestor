package com.example.appgestor.clases;

import android.graphics.Bitmap;

import java.io.Serializable;

public class PuntoVenta implements Serializable {
    private String nombre, codigo, dirección;
    Bitmap foto;
    private Double latitud, longitud;

    public PuntoVenta(String codigo, String nombre, String dirección, Double latitud, Double longitud, Bitmap foto) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.dirección = dirección;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDirección() {
        return dirección;
    }

    public void setDirección(String dirección) {
        this.dirección = dirección;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
