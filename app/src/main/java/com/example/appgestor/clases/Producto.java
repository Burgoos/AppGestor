package com.example.appgestor.clases;

public class Producto {
    private String nombre;
    private float pCosto, pMayor;
    private int stock, id;

    public Producto(String nombre, float pCosto, float pMayor, int stock, int id) {
        this.nombre = nombre;
        this.pCosto = pCosto;
        this.pMayor = pMayor;
        this.stock = stock;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getpCosto() {
        return pCosto;
    }

    public void setpCosto(float pCosto) {
        this.pCosto = pCosto;
    }

    public float getpMayor() {
        return pMayor;
    }

    public void setpMayor(float pMayor) {
        this.pMayor = pMayor;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
