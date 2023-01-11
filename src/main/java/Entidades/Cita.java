/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author alexis
 */
public class Cita {

    private int idcita;
    private String nombrepaciente;
    private LocalDate fechacita;
    private Doctor doctor;
    private Persona persona;
    private LocalTime hora;
    private Lugar lugar;
    private String celular;
    private String razon;
    private Usuario usuario;

    public Cita() {
    }

    public Cita(Doctor doctor, LocalTime horaatencion, LocalDate fechacita, String razon, Lugar lugar, Usuario usuario) {
        this.fechacita = fechacita;
        this.doctor = doctor;
        this.hora = horaatencion;
        this.lugar = lugar;
        this.razon = razon;
        this.usuario = usuario;
    }

    public Cita(Doctor oDoctor, Persona persona_paciente, LocalTime horaAtencion, LocalDate oFechaCita, String razon, Lugar lugar, Usuario personauser) {
        this.doctor = oDoctor;
        this.persona = persona_paciente;
        this.hora = horaAtencion;
        this.fechacita = oFechaCita;
        this.razon = razon;
        this.celular = celular;
        this.lugar = lugar;
        this.usuario = personauser;
    }

    public int getIdcita() {
        return idcita;
    }

    public void setIdcita(int idcita) {
        this.idcita = idcita;
    }

    public String getNombrepaciente() {
        return nombrepaciente;
    }

    public void setNombrepaciente(String nombrepaciente) {
        this.nombrepaciente = nombrepaciente;
    }

    public LocalDate getFechacita() {
        return fechacita;
    }

    public void setFechacita(LocalDate fechacita) {
        this.fechacita = fechacita;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalTime getHora() {
        return hora;
    }
    
    public void setHora(LocalTime horaatencion) {
        this.hora = horaatencion;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

}
