/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Cita;
import Entidades.Doctor;
import Entidades.HoraAtencion;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class CitaAgregarController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfDoctor;

    @FXML
    private JFXTextField jtfFecha;

    @FXML
    private JFXTextField jtfHora, jtfminuto;

    @FXML
    private JFXTextField jtfrazon, jtfnombrepaciente;

    CitaVerController citaControol;
    HoraAtencion horaAtencion;
    Doctor oDoctor;
    LocalDate oFechaCita;
    TableView<HoraAtencion> table;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    void setController(CitaVerController odc, TableView<HoraAtencion> table) {
        this.table = table;
        this.citaControol = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    void setPersona(HoraAtencion oHora, Doctor doc, LocalDate oFecha) {
        this.horaAtencion = oHora;
        this.oDoctor = doc;
        this.oFechaCita = oFecha;
        jtfDoctor.setText(doc.getNombredoctor());
        jtfFecha.setText(oFecha.toString());
        jtfHora.setText(oHora.getHora());
    }

    @FXML
    void guardarCita() {
        if (isComplete()) {
            Cita ocita = new Cita(oDoctor, jtfnombrepaciente.getText(), horaAtencion, oFechaCita, jtfrazon.getText(), jtfminuto.getText());
            App.jpa.getTransaction().begin();
            App.jpa.persist(ocita);
            App.jpa.getTransaction().commit();
            citaControol.actualizarListMesCita();
            table.refresh();
            cerrar();
        }
    }

    boolean isComplete() {
        boolean aux = true;
        if (jtfminuto.getText().trim().length() == 0) {
            jtfminuto.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfminuto.setStyle("");
        }

        if (jtfrazon.getText().trim().length() == 0) {
            jtfrazon.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfrazon.setStyle("");
        }

        if (jtfnombrepaciente.getText().trim().length() == 0) {
            jtfnombrepaciente.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtfnombrepaciente.setStyle("");
        }

        return aux;
    }

    @FXML
    void cerrar() {
        citaControol.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

}
