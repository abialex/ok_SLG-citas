package controller;

import Util.JPAUtil;
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
import javafx.stage.StageStyle;
import javax.persistence.EntityManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private double x = 0;
    private double y = 0;
    CitaVerController oCitaVerController;
    CargandoVistaController oCargandoVistaController;
    Stage stage;

    public class Proceso extends Thread {

        @Override
        public void run() {
            Platform.runLater(() -> {
                procesoMostrar();
            });
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        oCargandoVistaController = (CargandoVistaController) mostrarVentana(CargandoVistaController.class, "CargandoVista");
        CrearArchivos();
        new Proceso().start();
    }

    void procesoMostrar() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(App.class.getResource("CitaVer.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            File file =new File("error.txt");
            String content=ex.toString();
               try {
            if(!file.exists()){
             
                    file.createNewFile();
                
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            } catch (IOException ex1) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex1);
                }
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/bootstrap3.css").toExternalForm());
        oCitaVerController = (CitaVerController) fxmlLoader.getController(); //esto depende de (1)
        //oCitaVerController.setStagePrincipall(stage);
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
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("/imagenes/logo.png").toExternalForm()));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(event -> {
        });
        stage.show();
        oCargandoVistaController.cerrar();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
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

}
