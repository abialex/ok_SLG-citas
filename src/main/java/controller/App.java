package controller;

import Util.JPAUtil;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import javax.persistence.EntityManager;

/**
 * JavaFX App
 */
public class App extends Application {

   private static Scene scene;
    public static EntityManager jpa= JPAUtil.getEntityManagerFactory().createEntityManager();
    private double x = 0;
    private double y = 0;
    CitaVerController oCitaVerController;
    public static Stage stagePrincpal;

    @Override
    public void start(Stage stage) throws IOException {
        CrearArchivos();
        stagePrincpal = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("VerPaciente.fxml"));
        fxmlLoader.setLocation(App.class.getResource("CitaVer.fxml"));
        Parent root = fxmlLoader.load();
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
    static void CrearArchivos(){
        File carpetaImages = new File("Archivos paciente");
        if (!carpetaImages.exists()) {
            carpetaImages.mkdirs();
        }
        File carpetaPdf = new File("Pdf");
        if (!carpetaPdf.exists()) {
            carpetaPdf.mkdirs();
        }
    }

}
