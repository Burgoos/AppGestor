package com.example.appgestor.clases;

import android.graphics.Bitmap;

public class Usuario {
    private String nombre, correo;
    Bitmap foto;
    private int id;

    public Usuario(int id, String nombre, String correo, Bitmap foto) {
        this.nombre = nombre;
        this.correo = correo;
        this.foto = foto;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
