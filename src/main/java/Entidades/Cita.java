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
    private LocalDate fechacita;
    private Doctor doctor;
    private Persona persona;
    private LocalTime hora;
    private Lugar lugar;
    private String razon;
    private User user;

    public Cita() {
    }

    public Cita(Doctor doctor, LocalTime horaatencion, LocalDate fechacita, String razon, Lugar lugar, User usuario) {
        this.fechacita = fechacita;
        this.doctor = doctor;
        this.hora = horaatencion;
        this.lugar = lugar;
        this.razon = razon;
        this.user = usuario;
    }

    public Cita(Doctor oDoctor, Persona persona_paciente, LocalTime horaAtencion, LocalDate oFechaCita, String razon, Lugar lugar, User personauser) {
        this.doctor = oDoctor;
        this.persona = persona_paciente;
        this.hora = horaAtencion;
        this.fechacita = oFechaCita;
        this.razon = razon;
        this.lugar = lugar;
        this.user = personauser;
    }

    public int getIdcita() {
        return idcita;
    }

    public void setIdcita(int idcita) {
        this.idcita = idcita;
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

    public User getUser() {
        return user;
    }

    public void setUser(User usuario) {
        this.user = usuario;
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

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    
    

}
