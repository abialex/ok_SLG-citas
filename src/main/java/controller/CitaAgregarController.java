/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import Entidades.Cita;
import Entidades.Doctor;
import Entidades.Lugar;
import Entidades.Persona;
import Entidades.Usuario;
import Util.HttpMethods;
import Util.UtilClass;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JComboBox;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class CitaAgregarController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfDoctor, jtfFecha, jtfHora, jtfminuto, jtf_dni;

    @FXML
    private JFXTextField jtfrazon, jtfnombrepaciente, jtftelefono;

    @FXML
    private Label lblAMPM;

    @FXML
    private ImageView img_user_doctor, img_calendario, img_user_paciente, img_reloj, img_razon, img_telefono;

    @FXML
    private JFXComboBox<Lugar> jcb_lugar;

    @FXML
    private ImageView img_icon_1, img_icon_2;

    Object oObjetoController;
    Integer horaAtencionpurga;
    Doctor oDoctorpersona;
    LocalDate oFechaCita;
    Usuario oUsuario;
    Persona oPersona;
    List<Lugar> list_lugar;
    TableView<Integer> table;
    HttpMethods http = new HttpMethods();
    UtilClass oUtilClass = new UtilClass();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initRestricciones();
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

    public void setPersona(Integer oHora, Doctor odoctor, LocalDate oFecha, Usuario persona, List<Lugar> lugares) {
        this.horaAtencionpurga = oHora;
        this.oDoctorpersona = odoctor;
        this.oFechaCita = oFecha;
        this.oUsuario = persona;
        this.list_lugar = lugares;
        ObservableList<Lugar> list_lugar_o = FXCollections.observableArrayList();
        for (Lugar olugar : list_lugar) {
            list_lugar_o.add(olugar);
        }
        jcb_lugar.setItems(list_lugar_o);
        jcb_lugar.getSelectionModel().select(list_lugar_o.get(0));
        jtfDoctor.setText(odoctor.getPersona().getNombres() + " " + odoctor.getPersona().getAp_paterno() + " " + odoctor.getPersona().getAp_materno());
        jtfFecha.setText(oFecha.toString());
        jtfHora.setText(oHora + "");
        //lblAMPM.setText(oHora.getAbreviatura());
        jtfminuto.setText("00");
    }

    @FXML
    void guardarCita() {
        if (isComplete()) {
            Cita ocita = new Cita(oDoctorpersona, oPersona, LocalTime.of(horaAtencionpurga, Integer.parseInt(jtfminuto.getText())), oFechaCita, jtfrazon.getText(), jtftelefono.getText(), jcb_lugar.getSelectionModel().getSelectedItem(), oUsuario);
            http.AddObject(Cita.class, ocita, "/AddCita");
            oUtilClass.ejecutarMetodo(oObjetoController, "actualizarListMesCita");
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

    @FXML
    void buscar_persona_by_dni() {
        String dni = jtf_dni.getText();
        Persona opersona = http.ConsultObject(Persona.class, "/GetDni", dni);
        if(opersona != null){
            oPersona = opersona;
            jtfnombrepaciente.setText(opersona.getNombres()+" "+opersona.getAp_paterno()+" "+opersona.getAp_materno());
        }
        else{
            jtfnombrepaciente.setText("no se encontró, registrelo --------->");
        }
    }

}
