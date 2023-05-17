package com.pagupa.notiapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orden implements Serializable {
    private int orden_id;
    private String orden_numero;
    private String orden_serie;
    private int orden_secuencial;

    private Date orden_fecha_inicio;
    private Date orden_fecha_finalizacion;
    private int orden_prioridad;
    private String orden_lugar;
    private String orden_descripcion;
    private String orden_asignacion;
    private String orden_logistica;
    private String orden_observacion;
    private int orden_resultado;
    private int orden_estado;
    private int orden_estatus;

    private String orden_informe;
    private String orden_recibido_por;

    private String cliente_nombre;
    private String cliente_cedula;
    private String cliente_direccion;
    private String cliente_telefono;

    private int tipo_id;
    private int cliente_id;
    private int user_id;
    private int sucursal_id;

    private List<Responsable> responsables=new ArrayList<>();
    private List<Detalle> detalles=new ArrayList<>();
    private List<DetalleOrden> detallesOrden=new ArrayList<>();

    public Orden() {
    }

    public Orden(int orden_id, Date orden_fecha_inicio, int orden_prioridad, String orden_lugar, int orden_estado, int tipo_id, int cliente_id, int user_id, int sucursal_id) {
        this.orden_id = orden_id;
        this.orden_fecha_inicio = orden_fecha_inicio;
        this.orden_prioridad = orden_prioridad;
        this.orden_lugar = orden_lugar;
        this.orden_estado = orden_estado;
        this.tipo_id = tipo_id;
        this.cliente_id = cliente_id;
        this.user_id = user_id;
        this.sucursal_id = sucursal_id;
    }

    public void addResponsable(Responsable r){
        responsables.add(r);
    }
    public void deleteResponsable(Responsable r){
        responsables.remove(r);
    }
    public List<Responsable> getResponsables(){
        return this.responsables;
    }


    public void addDetalle(Detalle d){
        detalles.add(d);
    }
    public void deleteDetalle(Detalle d){
        detalles.remove(d);
    }
    public List<Detalle> getDetalles(){
        return this.detalles;
    }

    public void addDetalleOrden(DetalleOrden d){
        detallesOrden.add(d);
    }
    public void deleteDetalle(DetalleOrden d){
        detallesOrden.remove(d);
    }
    public List<DetalleOrden> getDetallesOrden(){
        return this.detallesOrden;
    }


    public String getCliente_nombre() {
        return cliente_nombre;
    }
    public void setCliente_nombre(String cliente_nombre) {
        this.cliente_nombre = cliente_nombre;
    }

    public String getCliente_cedula() {
        return cliente_cedula;
    }
    public void setCliente_cedula(String cliente_cedula) {
        this.cliente_cedula = cliente_cedula;
    }

    public String getCliente_direccion() {
        return cliente_direccion;
    }
    public void setCliente_direccion(String cliente_direccion) {
        this.cliente_direccion = cliente_direccion;
    }

    public String getCliente_telefono() {
        return cliente_telefono;
    }
    public void setCliente_telefono(String cliente_telefono) {
        this.cliente_telefono = cliente_telefono;
    }





    public int getOrden_id() {
        return orden_id;
    }
    public void setOrden_id(int orden_id) {
        this.orden_id = orden_id;
    }

    public String getOrden_numero() {
        return orden_numero;
    }
    public void setOrden_numero(String orden_numero) {
        this.orden_numero = orden_numero;
    }

    public String getOrden_serie() {
        return orden_serie;
    }
    public void setOrden_serie(String orden_serie) {
        this.orden_serie = orden_serie;
    }

    public int getOrden_secuencial() {
        return orden_secuencial;
    }
    public void setOrden_secuencial(int orden_secuencial) {
        this.orden_secuencial = orden_secuencial;
    }

    public Date getOrden_fecha_inicio() {
        return orden_fecha_inicio;
    }
    public void setOrden_fecha_inicio(Date orden_fecha_inicio) {
        this.orden_fecha_inicio = orden_fecha_inicio;
    }

    public Date getOrden_fecha_finalizacion() {
        return orden_fecha_finalizacion;
    }
    public void setOrden_fecha_finalizacion(Date orden_fecha_finalizacion) {
        this.orden_fecha_finalizacion = orden_fecha_finalizacion;
    }

    public int getOrden_prioridad() {
        return orden_prioridad;
    }
    public void setOrden_prioridad(int orden_prioridad) {
        this.orden_prioridad = orden_prioridad;
    }

    public String getOrden_lugar() {
        return orden_lugar;
    }
    public void setOrden_lugar(String orden_lugar) {
        this.orden_lugar = orden_lugar;
    }

    public String getOrden_descripcion() {
        return orden_descripcion;
    }
    public void setOrden_descripcion(String orden_descripcion) {
        this.orden_descripcion = orden_descripcion;
    }

    public String getOrden_asignacion() {
        return orden_asignacion;
    }
    public void setOrden_asignacion(String orden_asignacion) {
        this.orden_asignacion = orden_asignacion;
    }

    public String getOrden_logistica() {
        return orden_logistica;
    }
    public void setOrden_logistica(String orden_logistica) {
        this.orden_logistica = orden_logistica;
    }

    public String getOrden_observacion() {
        return orden_observacion;
    }
    public void setOrden_observacion(String orden_observacion) {
        this.orden_observacion = orden_observacion;
    }

    public int getOrden_resultado() {
        return orden_resultado;
    }
    public void setOrden_resultado(int orden_resultado) {
        this.orden_resultado = orden_resultado;
    }

    public String getOrden_informe() {
        return orden_informe;
    }
    public void setOrden_informe(String orden_informe) {
        this.orden_informe = orden_informe;
    }

    public String getOrden_recibido_por() {
        return orden_recibido_por;
    }
    public void setOrden_recibido_por(String orden_recibido_por) {
        this.orden_recibido_por = orden_recibido_por;
    }

    public int getOrden_estado() {
        return orden_estado;
    }
    public void setOrden_estado(int orden_estado) {
        this.orden_estado = orden_estado;
    }

    public int getOrden_estatus() {
        return orden_estatus;
    }
    public void setOrden_estatus(int orden_estatus) {
        this.orden_estatus = orden_estatus;
    }

    public int getTipo_id() {
        return tipo_id;
    }
    public void setTipo_id(int tipo_id) {
        this.tipo_id = tipo_id;
    }

    public int getCliente_id() {
        return cliente_id;
    }
    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSucursal_id() {
        return sucursal_id;
    }
    public void setSucursal_id(int sucursal_id) {
        this.sucursal_id = sucursal_id;
    }
}
