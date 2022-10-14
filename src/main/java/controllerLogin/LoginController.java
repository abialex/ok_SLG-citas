/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerLogin;

import Entidades.Address;
import Entidades.Persona;
import Perspectiva.CitaVerHuamangaController;
import Perspectiva.CitaVerHuantaController;
import Perspectiva.CitaVerObservadorController;
import Util.HttpMethods;
import Util.UtilClass;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controller.CitaVerController;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfNickname;

    @FXML
    private JFXPasswordField jtfcontrasenia;

    @FXML
    private Label lblMensaje;
    private double x = 0;
    private double y = 0;
    UtilClass oUtilClass = new UtilClass(x, y);
    HttpMethods http = new HttpMethods();
    Gson json = new Gson();
    Object oControllerVista;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void validarWithCookie() {
        HttpResponse<String> response = http.loguear(jtfNickname.getText(), jtfcontrasenia.getText(), "loguear");
        if (response != null) {
            Persona opersona = json.fromJson(response.body(), Persona.class);
            if (response.statusCode() == 226) {
                lblMensaje.setText("Ya está logueado");
                ingresar(opersona);

            }
        }
    }

    @FXML
    void validar() {
        if (isCompleto()) {
            HttpResponse<String> response = http.loguear(jtfNickname.getText(), jtfcontrasenia.getText(), "loguear");
            if (response != null) {
                Persona opersona = json.fromJson(response.body(), Persona.class);
                switch (response.statusCode()) {
                    case 200:
                        /* validar que exista el header
                         validar que haya mas de 43 caracteres */
                        if (response.headers().allValues("set-cookie").size() > 1) {
                            http.setCSRFToken(response.headers().allValues("set-cookie").get(0).substring(10, 42));
                            http.setCokkie(response.headers().allValues("set-cookie").get(0).substring(0, 43)
                                    + " " + response.headers().allValues("set-cookie").get(1).substring(0, 42));
                        }

                        lblMensaje.setText("Bienvenido " + opersona.getNombres() + ".");
                        ingresar(opersona);
                        break;

                    case 226:
                        lblMensaje.setText("Ya está logueado");
                        break;
                    case 406:
                        lblMensaje.setText("Credenciales incorrectos");
                        break;
                    default:
                        lblMensaje.setText("Sucedio otro error N° " + response.statusCode());
                        break;

                }
            } else {
                lblMensaje.setText("no hay conexión al servidor");
            }
        } else {
            lblMensaje.setText("llene los campos");
        }
    }

    void ingresar(Persona opersona) {
        Stage stage = new Stage();
        if (opersona.getRol().getRolname().equals("ADMINISTRADOR")) {
            oControllerVista = oUtilClass.mostrarVentana(CitaVerController.class, "CitaVer", stage);
            oUtilClass.ejecutarMetodos_1params(oControllerVista, "setController", opersona);
        } else if (opersona.getRol().getRolname().equals("ASISTENTA")) {
            if (opersona.getLugar().getNombrelugar().equals("HUANTA")) {
                oControllerVista = oUtilClass.mostrarVentana(CitaVerHuantaController.class, "CitaVerHuanta", stage);
                  oUtilClass.ejecutarMetodos_1params(oControllerVista, "setController", opersona);

            } else if (opersona.getLugar().getNombrelugar().equals("HUAMANGA")) {
                oControllerVista = oUtilClass.mostrarVentana(CitaVerHuamangaController.class, "CitaVerHuamanga", stage);
                  oUtilClass.ejecutarMetodos_1params(oControllerVista, "setController", opersona);

            } else if (opersona.getLugar().getNombrelugar().equals("ORTOGNATICA")) {
                //oControllerVista = oUtilClass.mostrarVentana(CitaVerOrtognaticaController.class, "CitaVerOrtognatica", stage);
            }

        } else if (opersona.getRol().getRolname().equals("OBSERVADOR")) {
            oControllerVista = oUtilClass.mostrarVentana(CitaVerObservadorController.class, "CitaVerObservador", stage);
              oUtilClass.ejecutarMetodos_1params(oControllerVista, "setController", opersona);

        }
        cerrar();
    }

    private boolean isCompleto() {
        boolean aux = true;
        if (jtfNickname.getText().isEmpty()) {
            jtfNickname.setStyle("-fx-border-color:red");
            aux = false;
        } else {
            jtfNickname.setStyle("");
        }
        if (jtfcontrasenia.getText().isEmpty()) {
            jtfcontrasenia.setStyle("-fx-border-color:red");
            aux = false;
        } else {
            jtfcontrasenia.setStyle("");
        }
        return aux;
    }

    public void stop() {
        oUtilClass.ejecutarMetodo(oControllerVista, "stop");
    }

    @FXML
    public void cerrar() {
        ((Stage) ap.getScene().getWindow()).close();

    }

}
