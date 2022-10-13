package controller;

import Entidades.Address;
import Perspectiva.CitaVerObservadorController;
import Util.HttpMethods;
import Util.JPAUtil;
import Util.UtilClass;
import controllerLogin.LoginController;
import controllerVistaEtc.CargandoVistaController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javax.persistence.EntityManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private double x = 0;
    private double y = 0;
    CargandoVistaController oCargandoVistaController;
    Stage stage;
    UtilClass oUtilClass = new UtilClass(x, y);
    HttpMethods http = new HttpMethods();
    LoginController oLoginController; 

    public class Proceso extends Thread {

        @Override
        public void run() {
            Platform.runLater(() -> {
                procesoMostrar();
            });
        }
    }

    @Override
    public void start(Stage st) throws IOException {
        this.stage = st;
        oCargandoVistaController = (CargandoVistaController) mostrarVentana(CargandoVistaController.class, "CargandoVista");
        CrearArchivos();
        new Proceso().start();
    }

    void procesoMostrar() {
        
        /*
        Address oaddress = http.getAddress();
        if (oaddress.getRol().getRolname().equals("ADMINISTRADOR")) {
            oControllerVista = oUtilClass.mostrarVentana(CitaVerController.class, "CitaVer", stage);

        } else if (oaddress.getRol().getRolname().equals("ASISTENTA")) {
            if (oaddress.getLugar().getNombrelugar().equals("HUANTA")) {
                oControllerVista = oUtilClass.mostrarVentana(CitaVerObservadorController.class, "CitaVerHuanta", stage);

            } else if (oaddress.getLugar().getNombrelugar().equals("HUAMANGA")) {
                oControllerVista = oUtilClass.mostrarVentana(CitaVerObservadorController.class, "CitaVerHuamanga", stage);

            } else if (oaddress.getLugar().getNombrelugar().equals("ORTOGNATICA")) {
                //oControllerVista = oUtilClass.mostrarVentana(CitaVerOrtognaticaController.class, "CitaVerOrtognatica", stage);
            }

        } else if (oaddress.getRol().getRolname().equals("OBSERVADOR")) {
            oControllerVista = oUtilClass.mostrarVentana(CitaVerObservadorController.class, "CitaVerObservador", stage);

        }*/
        oLoginController = (LoginController) oUtilClass.mostrarVentana(LoginController.class, "Login", stage);
        oLoginController.validarWithCookie();
        oCargandoVistaController.cerrar();
    }

    @Override
    public void stop() {
        oLoginController.stop();
    }

    static void CrearArchivos() {
        File carpetaImages = new File("Archivos paciente");
        if (!carpetaImages.exists()) {
            carpetaImages.mkdirs();
        }
        File carpetaPdf = new File("Pdf");
        if (!carpetaPdf.exists()) {
            carpetaPdf.mkdirs();
        }
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
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);

        stage.getIcons().add(new Image(getClass().getResource("/imagenes/logo.png").toExternalForm()));
        stage.setScene(scene);
        stage.show();
        return loader.getController();
    }

    public static void main(String[] args) {
        launch();
    }
}
