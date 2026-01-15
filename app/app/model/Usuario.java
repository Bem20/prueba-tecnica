package app.model;

import java.sql.Timestamp;

/**
 * Clase modelo que representa un Usuario en el sistema.
 *
 * TODO: Implementar la clase Usuario con los siguientes atributos:
 * - id (Long)
 * - rut (Integer) - RUT sin dígito verificador
 * - dv (String) - Dígito verificador del RUT
 * - nombre (String)
 * - email (String)
 * - activo (boolean)
 * - fechaCreacion (Timestamp)
 *
 * TODO: Implementar:
 * - Constructor vacío
 * - Constructor con todos los parámetros
 * - Getters y Setters para todos los atributos
 * - Método toString() para mostrar el usuario
 */
public class Usuario {

    private Long id;
    private Integer rut;
    private String dv;
    private String nombre;
    private String email;
    private Boolean activo;
    private Timestamp fechaCreacion;

    public Usuario() {
        // Constructor vacío
    }

    public Usuario(Long id, Integer rut, String dv, String nombre, String email, Boolean activo, Timestamp fechaCreacion) {
        this.id = id;
        this.rut = rut;
        this.dv = dv;
        this.nombre = nombre;
        this.email = email;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRut() {
        return rut;
    }

    public void setRut(Integer rut) {
        this.rut = rut;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        String rutDv = (rut != null ? rut : "") + "-" + (dv != null ? dv : "");
        String estado = Boolean.TRUE.equals(activo) ? "ACTIVO" : "INACTIVO";
        return "Usuario{" +
                "id=" + id +
                ", rut=" + rutDv +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
