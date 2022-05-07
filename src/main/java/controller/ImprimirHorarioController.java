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
import javafx.scene.control.DatePicker;
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

    @FXML
    private DatePicker dpFecha;
    CitaVerController odc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDoctor();
        cargarSemana();
        //lblHoy.setText("HOY: " + LocalDate.now());
    }

    void cargarSemana() {
        String dia = "MAÑANA";
        if (LocalDate.now().getDayOfWeek().getValue() == 6) {
            dia = "PASADO MAÑANA (LUNES)";
        }
        ObservableList<String> SEMANA = FXCollections.observableArrayList("HOY", dia, "ESTA SEMANA", "PRÓXIMA SEMANA");
        jcbSemana.setItems(SEMANA);
        jcbSemana.getSelectionModel().select("HOY");
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
            String url = "";
            if (jcbSemana.getSelectionModel().getSelectedItem().equals("HOY") || jcbSemana.getSelectionModel().getSelectedItem().equals("MAÑANA") || jcbSemana.getSelectionModel().getSelectedItem().equals("PASADO MAÑANA (LUNES)")) {
                if (jcbSemana.getSelectionModel().getSelectedItem().equals("MAÑANA")) {
                    lc = lc.plusDays(1);
                } else if (jcbSemana.getSelectionModel().getSelectedItem().equals("PASADO MAÑANA (LUNES)")) {
                    lc = lc.plusDays(2);
                }
                url = Citapdf.ImprimirCitaHoy(jcbDoctor.getSelectionModel().getSelectedItem(), lc, jcbSemana.getSelectionModel().getSelectedItem());
                File file = new File(url);
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + url.trim());
                } catch (IOException ex) {
                    Logger.getLogger(ImprimirHorarioController.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                if (!jcbSemana.getSelectionModel().getSelectedItem().equals("ESTA SEMANA")) {
                    lc = lc.plusDays(7);
                }
                url = Citapdf.ImprimirCita(jcbDoctor.getSelectionModel().getSelectedItem(), lc);
                File file = new File(url);
                try {
                    Runtime.getRuntime().exec("explorer.exe /select," + url.trim());
                } catch (IOException ex) {
                    Logger.getLogger(ImprimirHorarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            jcbDoctor.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void imprimirDoctores() {
        if (dpFecha.getValue() != null) {
            String url = Citapdf.ImprimirCitaDoctores(dpFecha.getValue());
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + url.trim());
            } catch (IOException ex) {
                Logger.getLogger(ImprimirHorarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void cerrar() {
        odc.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

}
