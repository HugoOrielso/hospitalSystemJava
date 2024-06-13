package com.hospitalsystem.Controllers.Doctor;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class DataDoctor {
    @Getter @Setter
    private Integer idDoctor;
    @Getter @Setter
    private String doctorId;
    @Getter @Setter
    private String doctorName;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String genero;
    @Getter @Setter
    private String telefono;
    @Getter @Setter
    private String direccion;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String estatus;
    @Getter @Setter
    private String imagen;
    @Getter @Setter
    private String especializacion;
    @Getter @Setter
    private Date fecha;
    @Getter @Setter
    private Date fechaModificacion;
    @Getter @Setter
    private Date fechaEliminacion;

    public DataDoctor(Integer idDoctor, String doctorId, String doctorName, String email, String genero, String telefono, String direccion, String password, String estatus, String imagen, String especializacion, Date fecha, Date fechaModificacion, Date fechaEliminacion) {
        this.idDoctor = idDoctor;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.email = email;
        this.genero = genero;
        this.telefono = telefono;
        this.direccion = direccion;
        this.password = password;
        this.estatus = estatus;
        this.imagen = imagen;
        this.especializacion = especializacion;
        this.fecha = fecha;
        this.fechaModificacion = fechaModificacion;
        this.fechaEliminacion = fechaEliminacion;
    }

    public DataDoctor(Integer idDoctor, String uuid, String doctorName, String genero, String telefono, String email, String especializacion, String direccion, String estatus, String imagen) {
        this.idDoctor = idDoctor;
        this.doctorId = uuid;
        this.doctorName = doctorName;
        this.genero = genero;
        this.telefono = telefono;
        this.email = email;
        this.especializacion = especializacion;
        this.direccion = direccion;
        this.estatus = estatus;
        this.imagen = imagen;
    }

    public DataDoctor(Integer idDoctor, String doctorName, String especializacion, String estatus) {
        this.idDoctor = idDoctor;
        this.doctorName = doctorName;
        this.especializacion = especializacion;
        this.estatus = estatus;
    }
}
