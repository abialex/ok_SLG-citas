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
    private Persona doctor;
    private LocalTime hora;
    private Lugar lugar;
    private String celular;
    private String razon;
    private String minuto;
    private Persona persona;

    public Cita() {
    }

    public Cita(Persona doctor, LocalTime horaatencion, LocalDate fechacita, String razon, Lugar lugar, Persona persona) {
        this.fechacita = fechacita;
        this.doctor = doctor;
        this.hora = horaatencion;
        this.lugar = lugar;
        this.razon = razon;
        this.persona = persona;
    }

    public Cita(Persona oDoctor, String nombrepaciente, LocalTime horaAtencion, LocalDate oFechaCita, String razon, String minuto, String celular, Lugar lugar, Persona personauser) {
        this.doctor = oDoctor;
        this.nombrepaciente = nombrepaciente;
        this.hora = horaAtencion;
        this.fechacita = oFechaCita;
        this.razon = razon;
        this.minuto = minuto;
        this.celular = celular;
        this.lugar = lugar;
        this.persona = personauser;
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

    public Persona getDoctor() {
        return doctor;
    }

    public void setDoctor(Persona doctor) {
        this.doctor = doctor;
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

    public String getMinuto() {
        return minuto;
    }

    public void setMinuto(String minuto) {
        this.minuto = minuto;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
