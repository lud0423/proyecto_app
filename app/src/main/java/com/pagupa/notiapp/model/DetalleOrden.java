package com.pagupa.notiapp.model;

import java.io.Serializable;

public class DetalleOrden implements Serializable {
    private int detalle_orden_cantidad;
    private int producto_id;
    private String producto_nombre;
    private int detalle_orden_stock;
    private int detalle_orden_estado;

    public DetalleOrden() {
    }

    public int getDetalle_orden_cantidad() {
        return detalle_orden_cantidad;
    }
    public void setDetalle_orden_cantidad(int detalle_orden_cantidad) {
        this.detalle_orden_cantidad = detalle_orden_cantidad;
    }

    public int getProducto_id() {
        return producto_id;
    }
    public void setProducto_id(int producto_id) {
        this.producto_id = producto_id;
    }

    public String getProducto_nombre() {
        return producto_nombre;
    }
    public void setProducto_nombre(String producto_nombre) {
        this.producto_nombre = producto_nombre;
    }

    public int getDetalle_orden_stock() {
        return detalle_orden_stock;
    }
    public void setDetalle_orden_stock(int detalle_orden_stock) {
        this.detalle_orden_stock = detalle_orden_stock;
    }

    public int getDetalle_orden_estado() {
        return detalle_orden_estado;
    }
    public void setDetalle_orden_estado(int detalle_orden_estado) {
        this.detalle_orden_estado = detalle_orden_estado;
    }
}
