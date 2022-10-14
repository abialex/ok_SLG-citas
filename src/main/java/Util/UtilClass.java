/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Util;

import com.jfoenix.controls.JFXTextField;
import controller.CitaModificarController;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author alexis
 */
public class UtilClass {

    double x = 0, y = 0;

    public UtilClass(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public UtilClass() {
    }

    public Object mostrarVentana(Class generico, String nameFXML, AnchorPane ap) {
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
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

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

    public Object mostrarVentana(Class generico, String nameFXML, Stage st) {
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
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        stage.initOwner(st);
        stage.getIcons().add(new Image(getClass().getResource("/imagenes/logo.png").toExternalForm()));
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

    public void lockedPantalla(AnchorPane ap) {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
    }

    public void SoloNumerosEnteros2(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 2) {
            event.consume();
        }
    }

    public void SoloNumerosEnteros9(KeyEvent event) {
        JFXTextField o = (JFXTextField) event.getSource();
        char key = event.getCharacter().charAt(0);
        if (!Character.isDigit(key)) {
            event.consume();
        }
        if (o.getText().length() >= 9) {
            event.consume();
        }
    }

    public String leerTXT(String direccion) {
        File file = new File(direccion);
        String temp = "";
        if (!file.exists()) {
            System.out.println("no existe " + direccion);
            return "";
        }
        try {
            BufferedReader bf;
            bf = new BufferedReader(new FileReader(direccion));

            String bfRead;
            while ((bfRead = bf.readLine()) != null) {
                temp = temp + bfRead;
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(UtilClass.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {
            Logger.getLogger(UtilClass.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return temp;

    }

    //actualiza archivo
    //si no encuentra el archivo lo crea y le agrega el contenido
    public String updateArchivo(String direccion, String contenido) {
        File file = new File(direccion);
        String contenidoFile = leerTXT(direccion);
        if (file.exists()) {

            if (contenidoFile.equals(contenido)) {
                return contenidoFile;
            } else {
                file.delete();
                try {
                    FileWriter fichero = new FileWriter(direccion);
                    fichero.write(contenido);
                    fichero.close();
                } catch (IOException ex) {
                    Logger.getLogger(UtilClass.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }

                return contenidoFile;
            }
        } else {
            try {
                FileWriter fichero = new FileWriter(direccion);
                fichero.write(contenido);
                fichero.close();
            } catch (IOException ex) {
                Logger.getLogger(UtilClass.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
            return contenidoFile;

        }
    }

    public void abrirURL(String direccion) {
        URL url = null;
        try {
            url = new URL(direccion);
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
    }

    public void ejecutarMetodo(Object obj, String metodo) {
        try {
            Class[] parametro = null;
            Object[] parametro2 = null;
            obj.getClass().getDeclaredMethod(metodo).invoke(obj);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UtilClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public <T> void ejecutarMetodos_1params(Object objClass, String metodo, T param1) {
        try {
            objClass.getClass().getDeclaredMethod(metodo, param1.getClass()).invoke(objClass, param1);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UtilClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
