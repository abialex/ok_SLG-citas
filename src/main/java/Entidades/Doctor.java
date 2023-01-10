/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author alexis
 */

public class Doctor {

    private int iddoctor;
    private String cop;
    private Persona persona;

    public Doctor() {
    }

    public Doctor(String persona) {
        this.cop = persona;
    }

    public int getIddoctor() {
        return iddoctor;
    }

    public void setIddoctor(int iddoctor) {
        this.iddoctor = iddoctor;
    }

    public String getNombredoctor() {
        return cop;
    }

    public void setNombredoctor(String nombredoctor) {
        this.cop = nombredoctor;
    }

    public Doctor getDoctor() {
        return this;
    }

    public String getCop() {
        return cop;
    }

    public void setCop(String cop) {
        this.cop = cop;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    
    

    @Override
    public String toString() {
        return this.persona.toString();
    }

}
