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
    private String nombres;
    private String ap_paterno;
    private String ap_materno;
    private LocalDate fecha_cumple;
    //private int idusuario;

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

    @Override
    public String toString() {
        return this.nombres + " " + this.ap_paterno;
    }

}
