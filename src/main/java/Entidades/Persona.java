/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;

/**
 *
 * @author alexis
 */
public class Persona {

    private int idpersona;
    private String dni;
    private String nombres;
    private String ap_paterno;
    private String ap_materno;
    private String telefono;
    private String sexo;
    private LocalDate fecha_cumple;
    private String ocupacion;
    private String lugar_de_procedencia;
    private String domicilio;

    public Persona() {
    }

    public Persona(String nombress, String ap_paterno) {
        this.nombres = nombress;
        this.ap_paterno = ap_paterno;
    }

    public int getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(int idpersona) {
        this.idpersona = idpersona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getAp_paterno() {
        return ap_paterno;
    }

    public void setAp_paterno(String ap_paterno) {
        this.ap_paterno = ap_paterno;
    }

    public String getAp_materno() {
        return ap_materno;
    }

    public void setAp_materno(String ap_materno) {
        this.ap_materno = ap_materno;
    }

    public LocalDate getFecha_cumple() {
        return fecha_cumple;
    }

    public void setFecha_cumple(LocalDate fecha_cumple) {
        this.fecha_cumple = fecha_cumple;
    }

    public Persona getPersona() {
        return this;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getLugar_de_procedencia() {
        return lugar_de_procedencia;
    }

    public void setLugar_de_procedencia(String lugar_de_procedencia) {
        this.lugar_de_procedencia = lugar_de_procedencia;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    
    @Override
    public String toString() {
        return this.nombres + " " + this.ap_paterno;
    }

}
