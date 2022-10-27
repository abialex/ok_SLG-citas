/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Persona;
import Pdf.Citapdf;
import Util.HttpMethods;
import Util.UtilClass;
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
    private JFXComboBox<Persona> jcbDoctor;

    @FXML
    private Label lblHoy;

    @FXML
    private DatePicker dpFecha;
    Object oObjetoController;
    HttpMethods http = new HttpMethods();
    UtilClass oUtilClass = new UtilClass();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDoctor();
        //lblHoy.setText("HOY: " + LocalDate.now());
    }

    public void cargarDoctor() {
        List<Persona> listDoctorG = http.getList(Persona.class, "/DoctorAll");
        ObservableList<Persona> listDoctor = FXCollections.observableArrayList();
        Persona doctorNinguno;
        doctorNinguno = new Persona();
        doctorNinguno.setNombres("NINGUNO");
        doctorNinguno.setAp_paterno("");
        doctorNinguno.setAp_materno("");
        listDoctor.add(doctorNinguno);
        for (Persona odoct : listDoctorG) {
            listDoctor.add(odoct);
        }
        jcbDoctor.setItems(listDoctor);
        jcbDoctor.getSelectionModel().select(doctorNinguno);
    }

    public void setController(Object odc) {
        this.oObjetoController = odc;
        this.dpFecha.setValue(LocalDate.now().plusDays(1));
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void imprimirHoy() {
        if (!jcbDoctor.getSelectionModel().getSelectedItem().getNombres().equals("NINGUNO")) {
            jcbDoctor.setStyle("");
            LocalDate lc = LocalDate.now();
            abrirArchivo(Citapdf.ImprimirCitaHoy(jcbDoctor.getSelectionModel().getSelectedItem(), lc, "HOY"));
        } else {
            jcbDoctor.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void imprimirManiana() {
        if (!jcbDoctor.getSelectionModel().getSelectedItem().getNombres().equals("NINGUNO")) {
            jcbDoctor.setStyle("");
            LocalDate lc = LocalDate.now();
            String url = "";
            if (LocalDate.now().getDayOfWeek().getValue() == 6) {
                lc = lc.plusDays(2);
                abrirArchivo(Citapdf.ImprimirCitaHoy(jcbDoctor.getSelectionModel().getSelectedItem(), lc, "PASADO MAÑANA"));
            } else {
                lc = lc.plusDays(1);
                abrirArchivo(Citapdf.ImprimirCitaHoy(jcbDoctor.getSelectionModel().getSelectedItem(), lc, "MAÑANA"));
            }

        } else {
            jcbDoctor.setStyle("-fx-border-color: red");
        }
    }

    @FXML
    void imprimirEstaSemana() {
        if (!jcbDoctor.getSelectionModel().getSelectedItem().getNombres().equals("NINGUNO")) {
            jcbDoctor.setStyle("");
            LocalDate lc = LocalDate.now();
            abrirArchivo(Citapdf.ImprimirCita(jcbDoctor.getSelectionModel().getSelectedItem(), lc));

        } else {
            jcbDoctor.setStyle("-fx-border-color: red");
        }

    }

    @FXML
    void imprimirProximaSemana() {
        if (!jcbDoctor.getSelectionModel().getSelectedItem().getNombres().equals("NINGUNO")) {
            jcbDoctor.setStyle("");
            LocalDate lc = LocalDate.now();
            lc = lc.plusDays(7);
            abrirArchivo(Citapdf.ImprimirCita(jcbDoctor.getSelectionModel().getSelectedItem(), lc));

        } else {
            jcbDoctor.setStyle("-fx-border-color: red");
        }

    }

    void abrirArchivo(String url) {
        File file = new File(url);
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(ImprimirHorarioController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void imprimirDoctores() {
        if (dpFecha.getValue() != null) {
            File file = new File(Citapdf.ImprimirCitaDoctores(dpFecha.getValue()));
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                Logger.getLogger(ImprimirHorarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void cerrar() {
        oUtilClass.ejecutarMetodo(oObjetoController, "lockedPantalla");
        ((Stage) ap.getScene().getWindow()).close();
    }

}
