package com.hospitalsystem.Controllers.Citas;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

public class CitasData {
    @Getter @Setter
    private String citaID;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String genero;
    @Getter @Setter
    private String descripcion;
    @Getter @Setter
    private String diagnostico;
    @Getter @Setter
    private String tratamiento;
    @Getter @Setter
    private String telefono;
    @Getter @Setter
    private String direccion;
    @Getter @Setter
    private String fechaCreacion;
    @Getter @Setter
    private Date fechaModificacion;
    @Getter @Setter
    private Date fechaEliminacion;
    @Getter @Setter
    private String estatus;
    @Getter @Setter
    private String doctorId;
    @Getter @Setter
    private Date calendario;

    public CitasData(String citaID, String name, String genero, String telefono, String descripcion, String fechaCreacion,
                     Date fechaModificacion, Date fechaEliminacion, String estatus, String diagnostico, String tratamiento,
                     String direccion,Date calendario){
        this.citaID = citaID;
        this.name = name;
        this.genero = genero;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.fechaEliminacion = fechaEliminacion;
        this.estatus = estatus;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.direccion = direccion;
        this.calendario = calendario;
    }

    public CitasData(){}

    public CitasData(String citaID, String name, String descripcion, String fechaCreacion, String estatus){
        this.citaID = citaID;
        this.name = name;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estatus = estatus;
    }
}
