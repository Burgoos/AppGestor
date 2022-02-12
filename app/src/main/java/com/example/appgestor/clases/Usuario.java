package com.example.appgestor.clases;

public class Usuario {
    private String nombre, correo, foto;
    private int id;

    public Usuario(String nombre, String correo, String foto, int id) {
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
