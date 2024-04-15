package com.hospitalsystem.Controllers.Citas;



import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class CitasData {
    @Getter
    private Integer citaID;
    @Getter
    private String name;
    @Getter
    private String genero;
    @Getter
    private String descripcion;
    private String diagnostico;
    private String tratamiento;
    @Getter
    private String telefono;
    private String direccion;
    @Getter
    private Date fechaCreacion;
    @Getter
    private Date fechaModificacion;
    @Getter
    private Date fechaEliminacion;
    @Getter
    private String estatus;
    private String doctorId;
    @Getter
    private String especialidad;
    private Date calendario;

    public CitasData(Integer citaID, String name, String genero, String telefono, String descripcion, Date fechaCreacion,
                     Date fechaModificacion, Date fechaEliminacion, String estatus){
        this.citaID = citaID;
        this.name = name;
        this.genero = genero;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.fechaEliminacion = fechaEliminacion;
        this.estatus = estatus;
    }

}
