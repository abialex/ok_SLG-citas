/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllerDoctor;

import Entidades.Doctor;
import Util.HttpMethods;
import Util.UtilClass;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import controller.App;
import controller.CitaVerController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class DoctorVerController implements Initializable {

    @FXML
    private AnchorPane ap;

    @FXML
    private JFXTextField jtfNombres;

    @FXML
    private TableView<Doctor> tableDoctor;

    @FXML
    private TableColumn<Doctor, Doctor> columnNombres;

    @FXML
    private TableColumn<Doctor, Doctor> columnEstado;

    ObservableList<Doctor> listDoctor = FXCollections.observableArrayList();
    private double x = 0;
    private double y = 0;
    DoctorVerController oDoctorVerController = this;
    Doctor oDoctorEliminar;
    int indexEliminar;
    Object oObjetoController;
    HttpMethods http = new HttpMethods();
    UtilClass oUtilClass = new UtilClass(x, y);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateListDoctor();
        initTableView();
        tableDoctor.setItems(listDoctor);
    }

    @FXML
    void updateListDoctor() {
        List<Doctor> olistDoc = http.getList(Doctor.class, "DoctorAll");
        listDoctor.clear();
        for (Doctor oDoc : olistDoc) {
            listDoctor.add(oDoc);
        }
    }

    @FXML
    void guardarDoctor() {
        if (jtfNombres.getText().length() != 0) {
            Doctor odoctor = new Doctor();
            odoctor.setNombredoctor(jtfNombres.getText());
            odoctor.setActivo(true);
            http.AddObject(Doctor.class, odoctor, "AddDoctor");
            updateListDoctor();
            oUtilClass.ejecutarMetodo(oObjetoController, "UpdatecargarDoctor");
            jtfNombres.setText("");
        }
    }

    void initTableView() {
        columnNombres.setCellValueFactory(new PropertyValueFactory<Doctor, Doctor>("doctor"));
        columnEstado.setCellValueFactory(new PropertyValueFactory<Doctor, Doctor>("doctor"));

        columnNombres.setCellFactory(column -> {
            TableCell<Doctor, Doctor> cell = new TableCell<Doctor, Doctor>() {
                @Override
                protected void updateItem(Doctor item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText("");
                    } else {
                        Label olabel = new Label();
                        olabel.setStyle("-fx-text-fill: white");
                        olabel.setUserData(item);
                        olabel.setText(item.getNombredoctor());
                        //olabel.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> changueActivo(event));
                        //olabel.addEventHandler(KeyEvent.KEY_RELEASED, event -> modificar(event));
                        olabel.focusedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                                if (newPropertyValue) {
                                    olabel.setStyle("-fx-border-color:WHITE;");
                                } else {
                                    olabel.setStyle("");
                                }
                            }
                        });
                        setGraphic(olabel);
                        setText(null);
                    }
                }

                void changueActivo(MouseEvent event) {
                    JFXTextField check = (JFXTextField) event.getSource();
                    check.setStyle("-fx-border-color:BLACK;");

                    check.setEditable(true);

                }

                void modificar(KeyEvent event) {
                    JFXTextField check = (JFXTextField) event.getSource();
                    Doctor doc = (Doctor) check.getUserData();
                    if (event.getCode() == (KeyCode.ENTER)) {
                        if (check.getText().length() != 0) {
                            doc.setNombredoctor(check.getText());
                            http.UpdateObject(Doctor.class, doc, "UpdateDoctor");
                            updateListDoctor();
                            oUtilClass.ejecutarMetodo(oObjetoController, "UpdatecargarDoctor");
                        }
                    }
                    if (event.getCode() == (KeyCode.ESCAPE)) {
                        updateListDoctor();
                    }
                }
            };
            return cell;
        });

        Callback<TableColumn<Doctor, Doctor>, TableCell<Doctor, Doctor>> cellFoctory = (TableColumn<Doctor, Doctor> param) -> {
            // make cell containing buttons
            final TableCell<Doctor, Doctor> cell = new TableCell<Doctor, Doctor>() {

                @Override
                public void updateItem(Doctor item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows                    
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        int tamHightImag = 23;
                        int tamWidthImag = 23;

                        ImageView editIcon = newImage("delete-1.png", tamHightImag, tamWidthImag, item);
                        editIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarEliminar(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagEliminarMoved(event));
                        editIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagEliminarFuera(event));

                        ImageView configIcon = newImage("modify-1.png", tamHightImag, tamWidthImag, item);
                        configIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarModificar(event));
                        configIcon.addEventHandler(MouseEvent.MOUSE_MOVED, event -> imagModificarMoved(event));
                        configIcon.addEventHandler(MouseEvent.MOUSE_EXITED, event -> imagModificarFuera(event));

                        HBox managebtn = new HBox(configIcon, editIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(editIcon, new Insets(0, 2.75, 0, 2.75));
                        HBox.setMargin(configIcon, new Insets(0, 2.75, 0, 2.75));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }

                ImageView newImage(String nombreImagen, int hight, int width, Object item) {
                    ImageView imag = new ImageView(new Image(getClass().getResource("/imagenes/" + nombreImagen).toExternalForm()));
                    imag.setFitHeight(hight);
                    imag.setFitWidth(width);
                    imag.setUserData(item);
                    imag.setStyle(
                            " -fx-cursor: hand;"
                    );
                    return imag;
                }

                void mostrarEliminar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    oDoctorEliminar = (Doctor) imag.getUserData();
                    indexEliminar = listDoctor.indexOf(oDoctorEliminar);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Info");
                    alert.setContentText("¿Desea eliminar al Dr(a): " + oDoctorEliminar.getNombredoctor() + "?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        eliminar();
                        updateListDoctor();
                    }

                }

                void mostrarModificar(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    Doctor odoctor = (Doctor) imag.getUserData();
                    DoctorModificarController oDoctorModificarController = (DoctorModificarController) oUtilClass.mostrarVentana(DoctorModificarController.class, "DoctorModificar", ap);
                    oDoctorModificarController.setController(oDoctorVerController, odoctor);
                    lockedPantalla();

                }

                private void imagEliminarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-2.png").toExternalForm()));
                }

                private void imagEliminarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/delete-1.png").toExternalForm()));
                }

                private void imagModificarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/modify-2.png").toExternalForm()));
                }

                private void imagModificarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/modify-1.png").toExternalForm()));
                }

            };
            return cell;
        };
        columnEstado.setCellFactory(cellFoctory);
    }

    public void eliminar() {
        if (indexEliminar != -1) {
            oDoctorEliminar.setFlag(true);
            http.UpdateObject(Doctor.class, oDoctorEliminar, "UpdateDoctor");
            updateListDoctor();
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
        scene.getStylesheets().add(generico.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner((Stage) ap.getScene().getWindow());
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

    public void UpdatecargarDoctor() {
        oUtilClass.ejecutarMetodo(oObjetoController, "UpdatecargarDoctor");
    }

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
        }
    }

    public void setController(Object odc) {
        this.oObjetoController = odc;
        ap.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> cerrar());
    }

    @FXML
    void cerrar() {
        oUtilClass.ejecutarMetodo(oObjetoController, "lockedPantalla");
        ((Stage) ap.getScene().getWindow()).close();
    }

}
