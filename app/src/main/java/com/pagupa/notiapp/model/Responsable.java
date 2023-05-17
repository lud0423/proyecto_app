package com.pagupa.notiapp.model;

import java.io.Serializable;

public class Responsable  implements Serializable {
    private int responsable_id;
    private int empleado_id;
    private String responsable_nombre_empleado;
    private String responsable_cedula_empleado;
    private String responsable_telefono_empleado;

    public Responsable() {
    }

    public int getResponsable_id() {
        return responsable_id;
    }

    public void setResponsable_id(int responsable_id) {
        this.responsable_id = responsable_id;
    }

    public int getEmpleado_id() {
        return empleado_id;
    }

    public void setEmpleado_id(int empleado_id) {
        this.empleado_id = empleado_id;
    }

    public String getResponsable_nombre_empleado() {
        return responsable_nombre_empleado;
    }

    public void setResponsable_nombre_empleado(String responsable_nombre_empleado) {
        this.responsable_nombre_empleado = responsable_nombre_empleado;
    }

    public String getResponsable_cedula_empleado() {
        return responsable_cedula_empleado;
    }

    public void setResponsable_cedula_empleado(String responsable_cedula_empleado) {
        this.responsable_cedula_empleado = responsable_cedula_empleado;
    }

    public String getResponsable_telefono_empleado() {
        return responsable_telefono_empleado;
    }

    public void setResponsable_telefono_empleado(String responsable_telefono_empleado) {
        this.responsable_telefono_empleado = responsable_telefono_empleado;
    }
}
