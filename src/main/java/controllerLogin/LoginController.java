/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerLogin;

import Entidades.Lugar;
import Entidades.Persona;
import Entidades.Rol;
import Entidades.User;
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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    private JFXPasswordField jtf_password;

    @FXML
    private Label lblMensaje, lbl_password_show;
    private double x = 0;
    private double y = 0;
    UtilClass oUtilClass = new UtilClass(x, y);
    HttpMethods http = new HttpMethods();
    Gson json = new Gson();
    Object oControllerVista;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbl_password_show.setVisible(false);

    }

    public void validarWithCookie() {
        HttpResponse<String> response = http.loguear(jtfNickname.getText(), jtf_password.getText());

        if (response != null) {
            User ousuario = json.fromJson(response.body(), User.class);
            if (response.statusCode() == 226) {
                lblMensaje.setText("Ya está logueado");
                ingresar(ousuario);
            }
        }
        else {
            lblMensaje.setText("no hay conexión al servidor");
        }
    }

   /* @FXML
    public void togglevisiblePassword(ActionEvent event) {
        if (pass_toggle.isSelected()) {
            jtf_password.setVisible(false);
            return;
        }
        jtf_password.setVisible(true);

    }*/

    @FXML
    void validar() {
        if (isCompleto()) {
            HttpResponse<String> response = http.loguear(jtfNickname.getText(), jtf_password.getText());
            if (response != null) {
                User osuario = json.fromJson(response.body(), User.class);
                switch (response.statusCode()) {
                    case 200 :
                        /* validar que exista el header
                         validar que haya mas de 43 caracteres */
                        if (response.headers().allValues("set-cookie").size() > 1) {
                            http.setCSRFToken(response.headers().allValues("set-cookie").get(0).substring(10, 42));
                            http.setCokkie(response.headers().allValues("set-cookie").get(0).substring(0, 43)
                                    + " " + response.headers().allValues("set-cookie").get(1).substring(0, 42));
                        }
                        //actualizando csfr y cookie
                        http = new HttpMethods();
                        lblMensaje.setText("Bienvenido " + osuario.getPersona().getNombres() + ".");
                        ingresar(osuario);
                        break;

                    case 226:
                        lblMensaje.setText("Bienvenido");
                        ingresar(osuario);
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

    void ingresar(User osuario) {
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
        if (jtf_password.getText().isEmpty()) {
            jtf_password.setStyle("-fx-border-color:red");
            aux = false;
        } else {
            jtf_password.setStyle("");
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

    boolean visible = false;

    @FXML
    void passwordFieldTyped(KeyEvent event) {
        lbl_password_show.setText(jtf_password.getText());

    }
    @FXML
    void changue_visible_password(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        if(visible) {
            imag.setImage(new Image(getClass().getResource("/imagenes/visible-1.png").toExternalForm()));
            lbl_password_show.setVisible(false);
            visible = false;
        }

        else {
            imag.setImage(new Image(getClass().getResource("/imagenes/visible-2.png").toExternalForm()));
            lbl_password_show.setVisible(true);
            visible = true;
        }
    }

    @FXML
    void imagAddpacienteFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/visible-1.png").toExternalForm()));
    }

}
