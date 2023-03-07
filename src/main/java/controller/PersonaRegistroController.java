/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Doctor;
import Entidades.Persona;
import EntidadesAux.PersonaReniec;
import Util.HttpMethods;
import Util.UtilClass;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class PersonaRegistroController implements Initializable {

    HttpMethods http = new HttpMethods();

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXComboBox<String> jcb_sexo;

    @FXML
    private JFXTextField jtf_dia, jtf_mes, jtf_anio;

    @FXML
    private JFXComboBox<String> jcb_ocupacion;

    @FXML
    private JFXTextField jtf_lugar_procedencia, jtf_domicilio;

    @FXML
    private JFXTextField jtf_nombres, jtf_ap_paterno, jtf_ap_materno;

    @FXML
    private JFXTextField jtf_dni;

    @FXML
    private JFXTextField jtf_telefono;



    UtilClass oUtilClass = new UtilClass();
    Doctor oDoctor = new Doctor();
    CitaAgregarController oCitaAgregarController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> OCUPACION = FXCollections.observableArrayList("ESTUDIANTE", "UNIVERSITARIO", "TRABAJADOR");
        ObservableList<String> SEXO = FXCollections.observableArrayList("VARÓN", "MUJER");
        jcb_ocupacion.setItems(OCUPACION);
        jcb_sexo.setItems(SEXO);
        initRestricciones();
    }

    public void setController(CitaAgregarController ocitaAgregarController, String dni, Doctor odoctor) {
        this.oDoctor=odoctor;
        this.oCitaAgregarController = ocitaAgregarController;
        jtf_dni.setText(dni);
        jtf_lugar_procedencia.setText("Huanta");
        consultar(dni);

    }

    void consultar(String dni) {
        if (dni.length() == 8) {
            PersonaReniec opersonareniec = http.consultarDNI(dni);
            if (opersonareniec.getNombres() != null) {
                jtf_nombres.setText(opersonareniec.getNombres());
                jtf_ap_paterno.setText(opersonareniec.getApellidoPaterno());
                jtf_ap_materno.setText(opersonareniec.getApellidoMaterno());

            } else {

            }

        } else {

        }
    }

    @FXML
    void registrar() {
        if (isCompleto()) {
            JsonObject responseJSON = new JsonObject();
            JsonObject opersonaJSON = new JsonObject();
            opersonaJSON.addProperty("dni", jtf_dni.getText());
            opersonaJSON.addProperty("nombres", jtf_nombres.getText());
            opersonaJSON.addProperty("ap_paterno", jtf_ap_paterno.getText());
            opersonaJSON.addProperty("ap_materno", jtf_ap_materno.getText());
            opersonaJSON.addProperty("telefono", jtf_telefono.getText());
            opersonaJSON.addProperty("sexo", jcb_sexo.getSelectionModel().getSelectedItem());
            opersonaJSON.addProperty("fecha_cumple_formato", LocalDate.of(Integer.parseInt(jtf_anio.getText()), Integer.parseInt(jtf_mes.getText()), Integer.parseInt(jtf_dia.getText())).toString());
            opersonaJSON.addProperty("ocupacion", jcb_ocupacion.getSelectionModel().getSelectedItem());
            opersonaJSON.addProperty("lugar_de_procedencia", jtf_lugar_procedencia.getText());
            opersonaJSON.addProperty("domicilio", jtf_domicilio.getText());
            responseJSON.add("persona", opersonaJSON);

            JsonObject ohistoria_clinicaJSON = new JsonObject();
            ohistoria_clinicaJSON.addProperty("doctor_id", oDoctor.getIddoctor() );
            ohistoria_clinicaJSON.addProperty("motivo_consulta", "");
            ohistoria_clinicaJSON.addProperty("enfermedad_actual", "");
            ohistoria_clinicaJSON.addProperty("examen_intraoral", "");
            ohistoria_clinicaJSON.addProperty("examen_radiografico", "");
            ohistoria_clinicaJSON.addProperty("antecedentes", "");
            ohistoria_clinicaJSON.addProperty("diagnostico", "");
            responseJSON.add("historia_clinica", ohistoria_clinicaJSON);



            HttpResponse<String> response = http.AddObjects(responseJSON, "historia_clinica/RegistrarHistoriaClinica");
            if (response.statusCode() == 200) {
                oUtilClass.mostrar_alerta_success("Exitoso", "Registrado ");
                Persona opersona=new Persona();
                opersona.setIdpersona(Integer.parseInt(response.body()));
                opersona.setNombres(jtf_nombres.getText());
                opersona.setAp_paterno(jtf_ap_paterno.getText());
                opersona.setAp_materno(jtf_ap_materno.getText());
                opersona.setTelefono(jtf_telefono.getText());
                opersona.setDni(jtf_dni.getText());
                oCitaAgregarController.actualizar_dni_datospersona_after_register(opersona);
            } else {
                oUtilClass.mostrar_alerta_warning("Código error: " + response.statusCode(), "No se guardó");
            }
            cerrar();
        }

    }

    boolean isCompleto() {
        boolean aux = true;
        if (jtf_nombres.getText().trim().length() == 0) {
            jtf_nombres.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtf_nombres.setStyle("");
        }

        if (jtf_ap_paterno.getText().trim().length() == 0) {
            jtf_ap_paterno.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtf_ap_paterno.setStyle("");
        }

        if (jtf_ap_materno.getText().trim().length() == 0) {
            jtf_ap_materno.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtf_ap_materno.setStyle("");
        }

        if (jtf_dni.getText().trim().length() == 0) {
            jtf_dni.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtf_dni.setStyle("");
        }

        if (jtf_telefono.getText().trim().length() == 0) {
            jtf_telefono.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtf_telefono.setStyle("");
        }

        if (jcb_sexo.getSelectionModel().getSelectedItem() == null) {
            jcb_sexo.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcb_sexo.setStyle("");
        }

        if (jcb_ocupacion.getSelectionModel().getSelectedItem() == null) {
            jcb_ocupacion.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jcb_ocupacion.setStyle("");
        }

        if (jtf_lugar_procedencia.getText().trim().length() == 0) {
            jtf_lugar_procedencia.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtf_lugar_procedencia.setStyle("");
        }

        if (jtf_domicilio.getText().trim().length() == 0) {
            jtf_domicilio.setStyle("-fx-border-color: #ff052b");
            aux = false;
        } else {
            jtf_domicilio.setStyle("");
        }

        boolean auxfecha = true;
        if (jtf_dia.getText().trim().length() == 0) {
            jtf_dia.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtf_dia.setStyle("");
        }

        if (jtf_mes.getText().trim().length() == 0) {
            jtf_mes.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtf_mes.setStyle("");
        }

        if (jtf_anio.getText().trim().length() == 0) {
            jtf_anio.setStyle("-fx-border-color: #ff052b");
            auxfecha = false;
        } else {
            jtf_anio.setStyle("");
        }

        boolean auxfechaCorrect = isfechavalid(auxfecha);
        if (!aux || !auxfecha) {
            oUtilClass.mostrar_alerta_warning("Incompleto", "LLene los cuadros encerrados en rojo");
        }
        return aux && auxfecha && auxfechaCorrect;
    }

    boolean isfechavalid(boolean aux) {
        try {
            if (aux) {
                LocalDate.of(Integer.parseInt(jtf_anio.getText().trim()), Integer.parseInt(jtf_mes.getText().trim()), Integer.parseInt(jtf_dia.getText().trim()));
            }
        } catch (Exception e) {
            aux = false;
            oUtilClass.mostrar_alerta_warning("Fecha nacimiento", "Ingrese una fecha válida");
        }
        return aux;
    }

    void initRestricciones() {
        jtf_nombres.addEventHandler(KeyEvent.KEY_TYPED, event -> oUtilClass.SoloLetras(event));
        jtf_telefono.addEventHandler(KeyEvent.KEY_TYPED, event -> oUtilClass.SoloNumerosEnteros9(event));
        jtf_dni.addEventHandler(KeyEvent.KEY_TYPED, event -> oUtilClass.SoloNumerosEnteros8(event));
        jtf_dia.addEventHandler(KeyEvent.KEY_TYPED, event -> oUtilClass.SoloNumerosEnteros2(event));
        jtf_mes.addEventHandler(KeyEvent.KEY_TYPED, event -> oUtilClass.SoloNumerosEnteros2(event));
        jtf_anio.addEventHandler(KeyEvent.KEY_TYPED, event -> oUtilClass.SoloNumerosEnteros4(event));

    }

    @FXML
    void cerrar() {
        oCitaAgregarController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

}
