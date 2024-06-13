package com.hospitalsystem.Controllers.Admin;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class AdminData {

    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String nombre;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String imagen;
    @Getter @Setter
    private String genero;
    @Getter @Setter
    private Date fecha;

    public AdminData(Integer id, String nombre, String email, String password, String imagen, String genero, Date fecha) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.imagen = imagen;
        this.genero = genero;
        this.fecha = fecha;
    }
}
