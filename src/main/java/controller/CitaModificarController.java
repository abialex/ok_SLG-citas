/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Cita;
import Entidades.HoraAtencion;
import Util.HttpMethods;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableView;
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
    private JFXTextField jtfFecha;

    @FXML
    private JFXComboBox<HoraAtencion> jcbHora;

    @FXML
    private JFXTextField jtfminuto;

    @FXML
    private JFXTextField jtfPaciente;

    @FXML
    private JFXTextField jtfrazon;

    CitaVerController oCitaVerController;
    Cita oCita;
    TableView<HoraAtencion> table;
    Alert alert = new Alert(Alert.AlertType.WARNING);
    private double x = 0;
    private double y = 0;
    List<HoraAtencion> listHora = new ArrayList<>();
    HttpMethods http = new HttpMethods();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarHora();
        initRestricciones();
    }

    @FXML
    void modificarCita() {
        if (isComplete()) {
            JsonObject citaAtributesJson = new JsonObject();
            citaAtributesJson.addProperty("iddoctor", oCita.getDoctor().getIddoctor());
            citaAtributesJson.addProperty("fechaInicio", oCita.getFechacita()+"");
            citaAtributesJson.addProperty("razon", "OCUPADO");
            citaAtributesJson.addProperty("idhoraatencion", jcbHora.getSelectionModel().getSelectedItem().getIdhoraatencion());
            List<Cita> listCitaOcupada = http.getCitaFilter(Cita.class, "CitaFilter", citaAtributesJson);

            JsonObject citaAtributesJson4 = new JsonObject();
            citaAtributesJson4.addProperty("iddoctor", oCita.getDoctor().getIddoctor());
            citaAtributesJson4.addProperty("fechaInicio", oCita.getFechacita()+"");
            citaAtributesJson4.addProperty("idhoraatencion", jcbHora.getSelectionModel().getSelectedItem().getIdhoraatencion());
            List<Cita> listCita4 = http.getCitaFilter(Cita.class, "CitaFilter", citaAtributesJson4);
            
            if (listCitaOcupada.isEmpty()) {
                if (listCita4.size() < 4 || jcbHora.getSelectionModel().getSelectedItem() == oCita.getHoraatencion()) {
                    oCita.setHoraatencion(jcbHora.getSelectionModel().getSelectedItem());
                    oCita.setMinuto(jtfminuto.getText());
                    oCita.setRazon(jtfrazon.getText());
                    http.UpdateObject(Cita.class, oCita, "UpdateCita");
                    oCitaVerController.actualizarListMesCita();
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
                alert.setContentText("El Dr. está ocupado a las " + listCitaOcupada.get(0).getHoraatencion().getHora() + ":00 " + listCitaOcupada.get(0).getHoraatencion().getAbreviatura());
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
            http.DeleteObject(Cita.class, "DeleteCita", oCita.getIdcita() + "");
            oCitaVerController.actualizarListMesCita();
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

    void cargarHora() {
        listHora = http.getList(HoraAtencion.class, "HoraAtencionAll");
        ObservableList<HoraAtencion> listhora = FXCollections.observableArrayList();
        for (HoraAtencion oHora : listHora) {
            listhora.add(oHora);
        }
        jcbHora.setItems(listhora);
    }

    void setController(CitaVerController odc, TableView<HoraAtencion> table) {
        this.table = table;
        this.oCitaVerController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    void setCita(Cita oCita) {
        this.oCita = oCita;
        jtfDoctor.setText(oCita.getDoctor().getNombredoctor());
        jtfFecha.setText(oCita.getFechacita() + "");
        for (HoraAtencion horaAtencion : listHora) {
            if (horaAtencion.getIdhoraatencion() == oCita.getHoraatencion().getIdhoraatencion()) {
                jcbHora.getSelectionModel().select(horaAtencion);
                break;
            }

        }
        jtfminuto.setText(oCita.getMinuto());
        jtfPaciente.setText(oCita.getNombrepaciente());
        jtfrazon.setText(oCita.getRazon());
    }

    void initRestricciones() {
        jtfminuto.addEventHandler(KeyEvent.KEY_TYPED, event -> SoloNumerosEnteros2(event));
    }

    void SoloNumerosEnteros2(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 2) {
            event.consume();
        }
    }

    @FXML
    void cerrar() {
        oCitaVerController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
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

    public Object mostrarVentana(Class generico, String nameFXML) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(generico.getResource(nameFXML + ".fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(generico.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);//instancia el controlador (!)
        scene.getStylesheets().add(generico.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(((Stage) ap.getScene().getWindow()));
        stage.setScene(scene);
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = event.getX();
                y = event.getY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - x);
                stage.setY(event.getScreenY() - y);
            }
        });
        stage.show();
        return loader.getController();
    }

}
