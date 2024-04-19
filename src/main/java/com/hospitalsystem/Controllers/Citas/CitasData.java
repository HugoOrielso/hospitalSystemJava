package com.hospitalsystem.Controllers.Citas;



import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class CitasData {
    @Getter
    private String citaID;
    @Getter
    private String name;
    @Getter
    private String genero;
    @Getter
    private String descripcion;
    @Getter
    private String diagnostico;
    @Getter
    private String tratamiento;
    @Getter
    private String telefono;
    @Getter
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
    private Date calendario;

    public CitasData(String citaID, String name, String genero, String telefono, String descripcion, Date fechaCreacion,
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

}
