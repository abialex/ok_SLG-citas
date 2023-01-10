/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Cita;
import Util.HttpMethods;
import Util.UtilClass;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class CitaModificarController implements Initializable {

    @FXML
    private AnchorPane ap;
    @FXML
    private JFXTextField jtfDoctor;

    @FXML
    private JFXTextField jtfFecha, jtfminuto, jtfPaciente, jtfrazon, jtftelefono;

    @FXML
    private JFXComboBox<Integer> jcbHora;

    @FXML
    private Label lblAMPM;

    @FXML
    private ImageView img_user_doctor, img_calendario, img_user_paciente, img_reloj, img_razon, img_telefono;
    
    @FXML
    private ImageView img_icon_1, img_icon_2;

    Object oObjetoController;
    Cita oCita;
    TableView<Integer> table;
    Alert alert = new Alert(Alert.AlertType.WARNING);
    ObservableList<Integer> listHora = FXCollections.observableArrayList();
    HttpMethods http = new HttpMethods();
    UtilClass oUtilClass = new UtilClass();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         cargarHora();
        initRestricciones();
        
        especial_navidad();
    }

    void initRestricciones() {
        jtfminuto.addEventHandler(KeyEvent.KEY_TYPED, event -> oUtilClass.SoloNumerosEnteros2(event));
        jtftelefono.addEventHandler(KeyEvent.KEY_TYPED, event -> oUtilClass.SoloNumerosEnteros9(event));
    }

    public void setController(Object odc, TableView<Integer> table) {
        this.table = table;
        this.oObjetoController = odc;
       
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    void cargarHora() {
        listHora.addAll(9,10,11,12,16,17,18,19,20);
        jcbHora.setItems(listHora);
    }

    @FXML
    void changueHora() {
        //lblAMPM.setText(jcbHora.getSelectionModel().getSelectedItem().getAbreviatura());
    }

    @FXML
    void modificarCita() {
        if (isComplete()) {
            JsonObject citaAtributesJson = new JsonObject();
            citaAtributesJson.addProperty("iddoctor", oCita.getDoctor().getPersona().getIdpersona());
            citaAtributesJson.addProperty("fechaInicio", oCita.getFechacita() + "");
            citaAtributesJson.addProperty("razon", "OCUPADO");
            citaAtributesJson.addProperty("hora", jcbHora.getSelectionModel().getSelectedItem());
            List<Cita> listCitaOcupada = http.getCitaFilter(Cita.class, "/CitaFilter", citaAtributesJson);

            JsonObject citaAtributesJson4 = new JsonObject();
            citaAtributesJson4.addProperty("iddoctor", oCita.getDoctor().getPersona().getIdpersona());
            citaAtributesJson4.addProperty("fechaInicio", oCita.getFechacita() + "");
            citaAtributesJson4.addProperty("hora", jcbHora.getSelectionModel().getSelectedItem());
            List<Cita> listCita4 = http.getCitaFilter(Cita.class, "/CitaFilter", citaAtributesJson4);

            if (listCitaOcupada.isEmpty()) {
                if (listCita4.size() < 4 || jcbHora.getSelectionModel().getSelectedItem() == oCita.getHora().getHour()) {
                    oCita.setHora(LocalTime.of(jcbHora.getSelectionModel().getSelectedItem(), Integer.parseInt(jtfminuto.getText())));
                    oCita.setRazon(jtfrazon.getText());
                    oCita.setCelular(jtftelefono.getText());
                    oCita.setHora(LocalTime.of(jcbHora.getSelectionModel().getSelectedItem(), Integer.parseInt(jtfminuto.getText())));
                    http.UpdateObject(Cita.class, oCita, "/UpdateCita");
                    oUtilClass.ejecutarMetodo(oObjetoController, "actualizarListMesCita");
                    table.refresh();
                    cerrar();
                } else {
                    alert.setHeaderText(null);
                    alert.setTitle(null);
                    alert.setContentText("Esa hora está al máximo de atenciones");
                    alert.showAndWait();
                }
            } else {
                alert.setHeaderText(null);
                alert.setTitle(null);
                alert.setContentText("El Dr. está ocupado a las " + listCitaOcupada.get(0).getHora().getHour() + ":00 " /*+ listCitaOcupada.get(0).getHora().getAbreviatura()*/);
                alert.showAndWait();
            }
        }
    }

    @FXML
    void eliminarCita() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText("¿Desea eliminar al paciente: " + oCita.getNombrepaciente() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            http.DeleteObject(Cita.class, "/DeleteCita", oCita.getIdcita() + "");
            oUtilClass.ejecutarMetodo(oObjetoController, "actualizarListMesCita");
            table.refresh();
            cerrar();
        }
    }

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
    }

    public void setCita(Cita oCita) {
        
        this.oCita = oCita;
        jtfDoctor.setText(oCita.getDoctor().getPersona().getNombres() + " " + oCita.getDoctor().getPersona().getAp_paterno());
        jtfFecha.setText(oCita.getFechacita() + "");
        for (Integer hora : listHora) {
            if (hora == oCita.getHora().getHour()) {
                jcbHora.getSelectionModel().select(hora);
                //lblAMPM.setText(oCita.getHora().getAbreviatura());
                break;
            }

        }
        jtfminuto.setText(oCita.getHora().getMinute()+"");
        jtfPaciente.setText(oCita.getNombrepaciente());
        jtfrazon.setText(oCita.getRazon());
        jtftelefono.setText(oCita.getCelular() == null ? "" : oCita.getCelular());
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

        return aux;
    }

    @FXML
    void cerrar() {
        oUtilClass.ejecutarMetodo(oObjetoController, "lockedPantalla");
        ((Stage) ap.getScene().getWindow()).close();
    }

    void especial_navidad() {
        //img_user_doctor.setImage(new Image(getClass().getResource("/imagenes/icons_navidad/icon_doctor_navidad.png").toExternalForm()));
        img_calendario.setImage(new Image(getClass().getResource("/imagenes/icons_navidad/icon_calendario_navidad.png").toExternalForm()));
        //img_user_paciente.setImage(new Image(getClass().getResource("/imagenes/icons_navidad/icon_paciente_navidad.png").toExternalForm()));
        img_reloj.setImage(new Image(getClass().getResource("/imagenes/icons_navidad/icon_reloj_navidad.png").toExternalForm()));
        img_razon.setImage(new Image(getClass().getResource("/imagenes/icons_navidad/icon_motivo_navidad.png").toExternalForm()));
        img_telefono.setImage(new Image(getClass().getResource("/imagenes/icons_navidad/icon_telefono_navidad.png").toExternalForm()));
        img_icon_1.setVisible(true);
        img_icon_2.setVisible(true);

    }
}
