package com.hospitalsystem.Controllers.Paciente;


import java.sql.Date;

public class PacientesData {
    private Integer id;
    private String codigo;
    private String password;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
    private String genero;
    private String imagen;
    private String descripcion;
    private String diagnostico;
    private String tratamiento;
    private String doctor;
    private String especialidad;
    private Date fecha;
    private Date fechaModificacion;
    private Date fechaEliminacion;
    private String estado;

    public String getCodigo() {
        return codigo;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getImagen() {
        return imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public Date getFechaEliminacion() {
        return fechaEliminacion;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getId() {
        return id;
    }

    public String getGenero() {
        return genero;
    }

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
}
