/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Persona;
import EntidadesAux.PersonaReniec;
import Util.HttpMethods;
import Util.UtilClass;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
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
    
    UtilClass oUtilClass=new UtilClass();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> OCUPACION = FXCollections.observableArrayList("ESTUDIANTE", "UNIVERSITARIO", "TRABAJADOR");
        ObservableList<String> SEXO = FXCollections.observableArrayList("VARÓN", "MUJER");
        jcb_ocupacion.setItems(OCUPACION);
        jcb_sexo.setItems(SEXO);
        initRestricciones();
    }

    public void setController(String dni) {
        jtf_dni.setText(dni);
        consultar(dni);

    }

    void consultar(String dni) {
        if (dni.length() == 8) {
            PersonaReniec opersonareniec = http.consultarDNI(dni);
            if (opersonareniec != null) {
                jtf_nombres.setText(opersonareniec.getNombres());
                jtf_ap_paterno.setText(opersonareniec.getApellidoPaterno());
                jtf_ap_materno.setText(opersonareniec.getApellidoMaterno());

            } else {
                /*
                alertWarning.setHeaderText(null);
                alertWarning.setTitle("Búsqueda");
                alertWarning.setContentText("Sin conexión a internet ");
                alertWarning.showAndWait();*/
            }

        } else {
            /*
            alertWarning.setHeaderText(null);
            alertWarning.setTitle("Búsqueda");
            alertWarning.setContentText("Ingrese un DNI válido");
            alertWarning.showAndWait();*/
        }
    }

    @FXML
    void registrar() {
        if (isCompleto()) {
            LocalDate fechaNacimiento = LocalDate.of(
                    Integer.parseInt(jtf_anio.getText().trim()),
                    Integer.parseInt(jtf_mes.getText().trim()),
                    Integer.parseInt(jtf_dia.getText().trim()));
            Persona opersona = new Persona();
            opersona.setDni(jtf_dni.getText());
            opersona.setNombres(jtf_nombres.getText());
            opersona.setAp_paterno(jtf_ap_paterno.getText());
            opersona.setAp_materno(jtf_ap_materno.getText());
            opersona.setTelefono(jtf_telefono.getText());
            opersona.setSexo(jcb_sexo.getSelectionModel().getSelectedItem());
            opersona.setFecha_cumple(fechaNacimiento);
            opersona.setOcupacion(jcb_ocupacion.getSelectionModel().getSelectedItem());
            opersona.setLugar_de_procedencia(jtf_lugar_procedencia.getText());
            opersona.setDomicilio(jtf_domicilio.getText());
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
        ((Stage) ap.getScene().getWindow()).close();
    }

}
