/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerDoctor;

import Entidades.Persona;
import Util.HttpMethods;
import com.jfoenix.controls.JFXTextField;
import controller.CitaVerController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class DoctorModificarController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfNombreDoctor;

    DoctorVerController oDoctorVerController;
    Persona oDoctor;
    HttpMethods http = new HttpMethods();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    void modificar() {
        oDoctor.setNombres(jtfNombreDoctor.getText());
        http.UpdateObject(Persona.class, oDoctor, "/UpdateDoctor");
        oDoctorVerController.UpdatecargarDoctor();
        oDoctorVerController.updateListDoctor();
        cerrar();
    }

    public void setController(DoctorVerController odc, Persona odoctor) {
        this.oDoctorVerController = odc;
        this.oDoctor = odoctor;
        jtfNombreDoctor.setText(odoctor.getNombres());
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void cerrar() {
        oDoctorVerController.lockedPantalla();
        ((Stage) ap.getScene().getWindow()).close();
    }

}
