/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Doctor;
import Pdf.Citapdf;
import com.jfoenix.controls.JFXComboBox;
import controller.App;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class ImprimirHorarioController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXComboBox<String> jcbSemana;

    @FXML
    private JFXComboBox<Doctor> jcbDoctor;
    
    @FXML
    private Label lblHoy;

    CitaVerController odc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDoctor();
        cargarSemana();
        lblHoy.setText("HOY: "+LocalDate.now());
    }

    void cargarSemana() {
        ObservableList<String> SEMANA = FXCollections.observableArrayList("ESTA SEMANA", "PRÓXIMA SEMANA");
        jcbSemana.setItems(SEMANA);
        jcbSemana.getSelectionModel().select("ESTA SEMANA");
    }

    public void cargarDoctor() {
        List<Doctor> listDoctorG = App.jpa.createQuery("select p from Doctor p where flag = false and activo = true").getResultList();
        ObservableList<Doctor> listDoctor = FXCollections.observableArrayList();
        Doctor doctorNinguno;
        doctorNinguno = new Doctor();
        doctorNinguno.setNombredoctor("NINGUNO");
        listDoctor.add(doctorNinguno);
        for (Doctor odoct : listDoctorG) {
            listDoctor.add(odoct);
        }
        jcbDoctor.setItems(listDoctor);
        jcbDoctor.getSelectionModel().select(doctorNinguno);
    }

    void setController(CitaVerController odc) {
        this.odc = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void imprimir() {
        if (!jcbDoctor.getSelectionModel().getSelectedItem().getNombredoctor().equals("NINGUNO")) {
            jcbDoctor.setStyle("");
            LocalDate lc = LocalDate.now();
            if (!jcbSemana.getSelectionModel().getSelectedItem().equals("ESTA SEMANA")) {
                lc = lc.plusDays(7);
            }
            String url = Citapdf.ImprimirCita(jcbDoctor.getSelectionModel().getSelectedItem(), lc);
            File file = new File(url);
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                Logger.getLogger(ImprimirHorarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            jcbDoctor.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void cerrar() {
        odc.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

}
