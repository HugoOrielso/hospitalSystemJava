package com.hospitalsystem.Controllers.Paciente;


import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

public class PacientesData {
    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String codigo;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String nombre;
    @Getter @Setter
    private String telefono;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String direccion;
    @Getter @Setter
    private String genero;
    @Getter @Setter
    private String imagen;
    @Getter @Setter
    private String descripcion;
    @Getter @Setter
    private String diagnostico;
    @Getter @Setter
    private String tratamiento;
    @Getter @Setter
    private String doctor;
    @Getter @Setter
    private String especialidad;
    @Getter @Setter
    private Date fecha;
    @Getter @Setter
    private Date fechaModificacion;
    @Getter @Setter
    private Date fechaEliminacion;
    @Getter @Setter
    private String estado;
    @Getter @Setter
    private String doctorName;

    public PacientesData(Integer id, String codigo, String password, String nombre, String telefono, String genero, String email, String direccion,
                         String imagen, String descripcion, String diagnostico, String doctor, String tratamiento,
                         String especialidad, Date fecha, Date fechaEliminacion, Date fechaModificacion, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.password = password;
        this.nombre = nombre;
        this.telefono = telefono;
        this.genero = genero;
        this.email = email;
        this.direccion = direccion;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.diagnostico = diagnostico;
        this.doctor = doctor;
        this.tratamiento = tratamiento;
        this.especialidad = especialidad;
        this.fecha = fecha;
        this.fechaEliminacion = fechaEliminacion;
        this.fechaModificacion = fechaModificacion;
        this.estado = estado;
    }

    public PacientesData(Integer id,String codigo, String nombre, String telefono, String direccion, Date fecha, Date fechaModificacion,
                         Date fechaEliminacion, String genero, String estado){
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fecha = fecha;
        this.fechaEliminacion = fechaEliminacion;
        this.fechaModificacion = fechaModificacion;
        this.genero = genero;
        this.estado = estado;
    }

    public PacientesData(Integer id,String codigo,String nombre,String genero, String telefono, String descripcion, String diagnostico, String tratamiento, Date fecha,String direccion){
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.genero = genero;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.fecha = fecha;
        this.direccion = direccion;
    }

    public PacientesData(String codigo, String nombre, String genero, String diagnostico, String tratamiento, String doctor, Date fecha, String doctorName, String imagen) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.genero = genero;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.doctor = doctor;
        this.fecha = fecha;
        this.doctorName = doctorName;
        this.imagen = imagen;
    }
}
