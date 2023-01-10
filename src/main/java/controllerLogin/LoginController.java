/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerLogin;

import Entidades.Lugar;
import Entidades.Persona;
import Entidades.Rol;
import Entidades.Usuario;
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
import java.util.List;
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
        HttpResponse<String> response = http.loguear(jtfNickname.getText(), jtfcontrasenia.getText());
        if (response != null) {
            Usuario ousuario = json.fromJson(response.body(), Usuario.class);
            if (response.statusCode() == 226) {
                lblMensaje.setText("Ya está logueado");
                ingresar(ousuario);

            }
        }
    }

    @FXML
    void validar() {
        if (isCompleto()) {
            HttpResponse<String> response = http.loguear(jtfNickname.getText(), jtfcontrasenia.getText());
            if (response != null) {
                switch (response.statusCode()) {
                    case 200:
                        Usuario osuario = json.fromJson(response.body(), Usuario.class);
                        /* validar que exista el header
                         validar que haya mas de 43 caracteres */
                        if (response.headers().allValues("set-cookie").size() > 1) {
                            http.setCSRFToken(response.headers().allValues("set-cookie").get(0).substring(10, 42));
                            http.setCokkie(response.headers().allValues("set-cookie").get(0).substring(0, 43)
                                    + " " + response.headers().allValues("set-cookie").get(1).substring(0, 42));
                        }

                        lblMensaje.setText("Bienvenido " + osuario.getPersona().getNombres() + ".");
                        ingresar(osuario);
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

    Lugar getLugarUsuario(List<Lugar> list_lugar, String nombre_lugar) {

        for (Lugar lugar : list_lugar) {
            if (lugar.getNombrelugar().equals(nombre_lugar)) {
                return lugar;
            }
        }
        return new Lugar();
    }

    void ingresar(Usuario osuario) {
        Stage stage = new Stage();
        List<Rol> list_rol = http.getList(Rol.class, "/RolUserAll");
        List<Lugar> list_lugar = http.getList(Lugar.class, "/LugarUserAll");
        Rol orol = null;
        if (list_rol.size() == 1) {
            orol = list_rol.get(0);
            switch (orol.getRolname()) {
                case "ADMINISTRADOR":
                    oControllerVista = oUtilClass.mostrarVentana(CitaVerController.class, "CitaVer", stage);
                    oUtilClass.ejecutarMetodos_1params(oControllerVista, "setController", osuario, orol, list_lugar);
                    break;
                case "ASISTENTA_HUANTA":
                    oControllerVista = oUtilClass.mostrarVentana(CitaVerHuantaController.class, "CitaVerHuanta", stage);
                    oUtilClass.ejecutarMetodos_1params(oControllerVista, "setController", osuario, orol, list_lugar);
                    break;
                case "ASISTENTA_HUAMANGA":
                    oControllerVista = oUtilClass.mostrarVentana(CitaVerHuamangaController.class, "CitaVerHuamanga", stage);
                    oUtilClass.ejecutarMetodos_1params(oControllerVista, "setController", osuario, orol, list_lugar);
                    break;
                case "DOCTOR":
                    oControllerVista = oUtilClass.mostrarVentana(CitaVerObservadorController.class, "CitaVerObservador", stage);
                    oUtilClass.ejecutarMetodos_1params(oControllerVista, "setController", osuario, orol, list_lugar);
                    break;
                case "OBSERVADOR":
                    oControllerVista = oUtilClass.mostrarVentana(CitaVerObservadorController.class, "CitaVerObservador", stage);
                    oUtilClass.ejecutarMetodos_1params(oControllerVista, "setController", osuario, orol, list_lugar);
                    break;
                default:
                    throw new AssertionError();
            }

        } else if (list_rol.size() >= 1) {
            //abrir con abrirSELECCIONADOR DE LUGAR

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
