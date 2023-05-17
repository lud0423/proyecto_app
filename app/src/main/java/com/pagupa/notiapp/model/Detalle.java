package com.pagupa.notiapp.model;

import java.io.Serializable;

public class Detalle implements Serializable {
    private int detalle_id;
    private String descripcion;
    private int estado;

    public Detalle() {
    }

    public int getDetalle_id() {
        return detalle_id;
    }
    public void setDetalle_id(int detalle_id) {
        this.detalle_id = detalle_id;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }
    public void setEstado(int estado) {
        this.estado = estado;
    }
}
