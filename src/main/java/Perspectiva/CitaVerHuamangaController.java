/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Perspectiva;

import Entidades.Cita;
import Entidades.Doctor;
import Entidades.Lugar;
import Entidades.Persona;
import Entidades.Rol;
import Entidades.Usuario;
import Util.HttpMethods;
import Util.UtilClass;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controller.CitaAgregarController;
import controller.CitaModificarController;
import controller.ImprimirHorarioController;
import controllerDoctor.DoctorVerController;
import controllerLogin.LoginController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author alexis
 */
public class CitaVerHuamangaController implements Initializable, Runnable {

    @FXML
    private AnchorPane ap;

    @FXML
    private FlowPane fpDias;

    @FXML
    private TableView<LocalTime> tableDoctor1, tableDoctor2, tableDoctor3, tableDoctor4;

    @FXML
    private TableColumn<Integer, Integer> columnHoraAtencion1, columnHoraAtencion2, columnHoraAtencion3, columnHoraAtencion4;

    @FXML
    private TableColumn<Integer, Integer> columnCitas1, columnCitas2, columnCitas3, columnCitas4;

    @FXML
    private TableColumn<Integer, Integer> columnEstado1, columnEstado2, columnEstado3, columnEstado4;

    @FXML
    private JFXComboBox<Doctor> jcbDoctor1, jcbDoctor2, jcbDoctor3, jcbDoctor4;

    @FXML
    private JFXComboBox<String> jcbMes, jcbAnio;

    @FXML
    private Label lblfecha, lblInfoUser;

    @FXML
    private BorderPane bp_citas;

    @FXML
    private ImageView img_adorno;

    ObservableList<LocalTime> listHoraatencion = FXCollections.observableArrayList();
    LocalDate oFecha;
    CitaVerHuamangaController odc = this;
    private double x = 0;
    private double y = 0;
    JFXButton btn;//usado para desmarcar n
    String colorDefault = "-fx-background-color: #ffffff; -fx-border-color: #000000";
    String colorRed = "-fx-background-color: RED; -fx-border-color: #000000";
    String colorPlomo = "-fx-background-color:GRAY; -fx-border-color: #000000";
    String colorBlue = "-fx-background-color:BLUE; -fx-border-color: #000000";
    String colorYellow = "-fx-background-color: #337ab7; -fx-border-color: #000000";
    Doctor personadoctorNinguno;
    List<Cita> listCitaRaiz = new ArrayList<>();
    List<Doctor> listpersonaDoctorG = new ArrayList<>();
    boolean stoperActualizarComboBox = true;
    HttpMethods http = new HttpMethods();
    Thread h1;
    UtilClass oUtilClass = new UtilClass(x, y);
    Usuario oUsuario = new Usuario();
    List<Lugar> list_lugar=new ArrayList<>();
    Lugar oLugar;

    @Override
    public void run() {
        Thread ct = Thread.currentThread();
        while (ct == h1) {
            Platform.runLater(() -> {
                reconsulta();
            });

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateListHoraatencion();
        listpersonaDoctorG = http.getList(Doctor.class, "/DoctorAll");
        cargarDoctor();
        tableDoctor1.setItems(listHoraatencion);
        tableDoctor2.setItems(listHoraatencion);
        tableDoctor3.setItems(listHoraatencion);
        tableDoctor4.setItems(listHoraatencion);

        oFecha = LocalDate.now();
        jcbMes.getSelectionModel().select(getMesNum(LocalDate.now().getMonthValue()));
        cargarMes();
        cargarAnio();
        actualizarListMesCita();
        changueMes();
        h1 = new Thread(this);
        h1.start();
        especial_navidad();

    }

    public void setController(Usuario osuario, Rol orol, ArrayList<Lugar> list_lugar) {
        this.oUsuario = osuario;
        this.list_lugar = list_lugar;
        lblfecha.setText(getNombreDia(oFecha.getDayOfWeek().getValue()) + " " + oFecha.getDayOfMonth() + " DE " + getMesNum(oFecha.getMonthValue()) + " - SEDE: HUAMANGA");
        lblInfoUser.setText( osuario.getPersona().getNombres() + " " + osuario.getPersona().getAp_paterno() + " " + osuario.getPersona().getAp_materno());
        for (Lugar lugar : list_lugar) {
            if(lugar.getNombrelugar().equals("HUAMANGA")){
                oLugar=lugar;
            }
        }
        initTable();
    }

    void reconsulta() {
        actualizarListMesCita();
        initTable();

    }

    @FXML
    void updateListHoraatencion() {
        listHoraatencion.clear();
        listHoraatencion.addAll(
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                LocalTime.of(12, 0),
                LocalTime.of(16, 0),
                LocalTime.of(17, 0),
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                LocalTime.of(20, 0));
    }

    @FXML
    void reiniciar() {
        oFecha = LocalDate.now();
        jcbMes.getSelectionModel().select(getMesNum(LocalDate.now().getMonthValue()));
        jcbAnio.getSelectionModel().select(LocalDate.now().getYear() + "");
        changueMes();
        lblfecha.setText(getNombreDia(oFecha.getDayOfWeek().getValue()) + " " + oFecha.getDayOfMonth() + " DE " + getMesNum(oFecha.getMonthValue()) + " - SEDE: HUAMANGA");
        actualizarListMesCita();
        refreshTable();

    }

    public void UpdatecargarDoctor() {
        listpersonaDoctorG = http.getList(Doctor.class, "/DoctorAll");
        cargarDoctor();
    }

    public void cargarDoctor() {
        stoperActualizarComboBox = false;
        ObservableList<Doctor> listDoctor = FXCollections.observableArrayList();
        Persona oper = new Persona();
        oper.setNombres("NINGUNO");
        oper.setAp_paterno("");
        personadoctorNinguno = new Doctor();
        personadoctorNinguno.setPersona(oper);
        listDoctor.add(personadoctorNinguno);
        for (Doctor odoct : listpersonaDoctorG) {
            listDoctor.add(odoct);
        }
        jcbDoctor1.setItems(listDoctor);
        jcbDoctor2.setItems(listDoctor);
        jcbDoctor3.setItems(listDoctor);
        jcbDoctor4.setItems(listDoctor);
        cargarSettingsDoctor();
        stoperActualizarComboBox = true;
    }

    int extraerIdJCBdoctor(String nombre) {
        String id = oUtilClass.leerTXT(nombre);
        if (id.isEmpty()) {
            return -1;
        } else {
            return Integer.parseInt(id);
        }
    }

    void cargarSettingsDoctor() {
        //1- poniendo a ninguno pordefcto
        //2- si existe el doctor configurado y está activo: aparece en el combo box
        jcbDoctor1.getSelectionModel().select(personadoctorNinguno);
        jcbDoctor2.getSelectionModel().select(personadoctorNinguno);
        jcbDoctor3.getSelectionModel().select(personadoctorNinguno);
        jcbDoctor4.getSelectionModel().select(personadoctorNinguno);

        int idjcbdoctor1 = extraerIdJCBdoctor("jcbdoctor1");
        for (Doctor doctor : listpersonaDoctorG) {
            if (doctor.getPersona().getIdpersona() == idjcbdoctor1) {
                jcbDoctor1.getSelectionModel().select(doctor);
            }
        }
        int idjcbdoctor2 = extraerIdJCBdoctor("jcbdoctor2");
        for (Doctor doctor : listpersonaDoctorG) {
            if (doctor.getPersona().getIdpersona() == idjcbdoctor2) {
                jcbDoctor2.getSelectionModel().select(doctor);
            }
        }
        int idjcbdoctor3 = extraerIdJCBdoctor("jcbdoctor3");
        for (Doctor doctor : listpersonaDoctorG) {
            if (doctor.getPersona().getIdpersona() == idjcbdoctor3) {
                jcbDoctor3.getSelectionModel().select(doctor);
            }
        }
        int idjcbdoctor4 = extraerIdJCBdoctor("jcbdoctor4");
        for (Doctor doctor : listpersonaDoctorG) {
            if (doctor.getPersona().getIdpersona() == idjcbdoctor4) {
                jcbDoctor4.getSelectionModel().select(doctor);
            }
        }

    }

    void cargarMes() {
        ObservableList<String> MES = FXCollections.observableArrayList("ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SETIEMBRE",
                "OCTUBRE", "NOVIEMBRE", "DICIEMBRE");
        jcbMes.setItems(MES);
        jcbMes.getSelectionModel().select(getMesNum(LocalDate.now().getMonthValue()));
    }

    @FXML
    public void actualizarListMesCita() {
        //actualiza las citas que tego cada vez que agrego, elimino o modifico
        listCitaRaiz = http.getList(Cita.class, "/CitaAll");
    }

    void cargarAnio() {
        ObservableList<String> ANIO = FXCollections.observableArrayList("2022", "2023", "2024", "2025");
        jcbAnio.setItems(ANIO);
        jcbAnio.getSelectionModel().select(LocalDate.now().getYear() + "");
    }

    @FXML
    void changueMes() {
        if (LocalDate.now().getMonthValue() == 12) {
            mostrarDias_especial_navidad(numeroDeDiasMes(jcbMes.getSelectionModel().getSelectedItem()));
        } else {
            mostrarDias(numeroDeDiasMes(jcbMes.getSelectionModel().getSelectedItem()));
        }

    }

    void initTable() {

        initTableView1();
        initTableView2();
        initTableView3();
        initTableView4();
    }

    void refreshTable() {
        tableDoctor1.refresh();
        tableDoctor2.refresh();
        tableDoctor3.refresh();
        tableDoctor4.refresh();
    }

    //usado en button
    void setFecha(ActionEvent event) {
        if (btn != null) {
            //evaluando el button seleccionado anteriormente
            //btn.setStyle(colorDefault);
            LocalDate locald = (LocalDate) btn.getUserData();
            if (locald.getDayOfWeek().getValue() == 7) {
                //btn.setStyle(colorRed);
            }
            if (locald.equals(LocalDate.now())) {
                //btn.setStyle(colorPlomo);
            }
        }
        JFXButton buton = (JFXButton) event.getSource();
        btn = buton;
        //buton.setStyle(colorYellow);
        oFecha = (LocalDate) buton.getUserData();
        refreshTable();
        lblfecha.setText(getNombreDia(oFecha.getDayOfWeek().getValue()) + " " + oFecha.getDayOfMonth() + " DE " + getMesNum(oFecha.getMonthValue()) + " - SEDE: HUAMANGA");
    }

    void modificarSettingsDoctor(JFXComboBox jcb) {
        if (stoperActualizarComboBox) {
            Doctor doctor = (Doctor) jcb.getSelectionModel().getSelectedItem();
            int idjcbdoctor = extraerIdJCBdoctor(jcb.getId());
            if (doctor != personadoctorNinguno) {
                if (idjcbdoctor != doctor.getPersona().getIdpersona()) {
                    oUtilClass.updateArchivo(jcb.getId(), doctor.getPersona().getIdpersona() + "");
                }
            } else {
                oUtilClass.updateArchivo(jcb.getId(), "-1");
            }
        }
    }

    //usado en combobox
    @FXML
    void setFechaComboBox1(ActionEvent event) {
        tableDoctor1.refresh();
        modificarSettingsDoctor((JFXComboBox) event.getSource());
    }

    @FXML
    void setFechaComboBox2(ActionEvent event) {
        tableDoctor2.refresh();
        modificarSettingsDoctor((JFXComboBox) event.getSource());
    }

    @FXML
    void setFechaComboBox3(ActionEvent event) {
        tableDoctor3.refresh();
        modificarSettingsDoctor((JFXComboBox) event.getSource());
    }

    @FXML
    void setFechaComboBox4(ActionEvent event) {
        tableDoctor4.refresh();
        modificarSettingsDoctor((JFXComboBox) event.getSource());
    }

    void mostrarDias(int Dias) {
        fpDias.getChildren().clear();
        LocalDate fechaNow = LocalDate.now();
        for (int i = 1; i <= Dias; i++) {

            LocalDate fechaCita = LocalDate.of(Integer.parseInt(jcbAnio.getSelectionModel().getSelectedItem()), getNumMes(jcbMes.getSelectionModel().getSelectedItem()), i);
            JFXButton bt = new JFXButton();
            bt.setUserData(fechaCita);
            bt.addEventHandler(ActionEvent.ACTION, event -> setFecha(event));
            bt.getStyleClass().clear();
            bt.getStyleClass().add("button-forma1");
            int diaSemana = fechaCita.getDayOfWeek().getValue();
            /*if (diaSemana == 7) {
               bt.setStyle(colorRed);
            }*/
            if (fechaCita.equals(fechaNow)) {
                bt.getStyleClass().clear();
                bt.getStyleClass().add("button-forma1-seleccionado");
            }
            /*
            bt.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                    if (newPropertyValue) {
                        bt.setStyle(colorBlue);

                        //el button anterior volviendo a filtrar para cambiar sus colores
                        if (btn != null) {
                            LocalDate locald = (LocalDate) btn.getUserData();
                            if (locald.getDayOfWeek().getValue() == 7) {
                               // btn.setStyle(colorRed);
                            }
                            if (locald.equals(fechaNow)) {
                                btn.setStyle(colorPlomo);
                            }
                            if (locald.equals(oFecha)) {
                               // btn.setStyle(colorYellow);
                            }
                        }

                    } else {
                        bt.setStyle(colorDefault);
                        if (diaSemana == 7) {
                            //bt.setStyle(colorRed);
                        }
                        if (fechaCita.equals(fechaNow)) {
                            //bt.setStyle(colorPlomo);
                        }
                        if (fechaCita.equals(oFecha)) {
                            //bt.setStyle(colorYellow);
                        }
                    }
                }
            });*/
            bt.setText(i < 10 ? "0" + i : "" + i);
            FlowPane.setMargin(bt, new Insets(2, 4, 2, 4));
            if (diaSemana != 7) {
                fpDias.getChildren().add(bt);
            }

        }
    }

    void initTableView1() {
        Label HoraLabel = new Label("Hora");
        HoraLabel.setStyle("-fx-text-fill: white");
        Label CitasLabel = new Label("Citas");
        CitasLabel.setStyle("-fx-text-fill: white");
        columnHoraAtencion1.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));
        columnCitas1.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));
        columnEstado1.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));

        columnHoraAtencion1.setCellFactory(getCellHoraAtencion());
        columnHoraAtencion1.setGraphic(HoraLabel);
        columnCitas1.setCellFactory(getCellCitas(jcbDoctor1));
        columnCitas1.setGraphic(CitasLabel);
        columnCitas1.setText("");
        columnEstado1.setCellFactory(getCellEstado(jcbDoctor1));
    }

    void initTableView2() {
        Label HoraLabel = new Label("Hora");
        HoraLabel.setStyle("-fx-text-fill: white");
        Label CitasLabel = new Label("Citas");
        CitasLabel.setStyle("-fx-text-fill: white");
        columnHoraAtencion2.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));
        columnCitas2.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));
        columnEstado2.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));

        columnHoraAtencion2.setCellFactory(getCellHoraAtencion());
        columnHoraAtencion2.setGraphic(HoraLabel);
        columnCitas2.setCellFactory(getCellCitas(jcbDoctor2));
        columnCitas2.setGraphic(CitasLabel);
        columnCitas2.setText("");
        columnEstado2.setCellFactory(getCellEstado(jcbDoctor2));
    }

    void initTableView3() {
        Label HoraLabel = new Label("Hora");
        HoraLabel.setStyle("-fx-text-fill: white");
        Label CitasLabel = new Label("Citas");
        CitasLabel.setStyle("-fx-text-fill: white");
        columnHoraAtencion3.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));
        columnCitas3.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));
        columnEstado3.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));

        columnHoraAtencion3.setCellFactory(getCellHoraAtencion());
        columnHoraAtencion3.setGraphic(HoraLabel);
        columnCitas3.setCellFactory(getCellCitas(jcbDoctor3));
        columnCitas3.setGraphic(CitasLabel);
        columnCitas3.setText("");
        columnEstado3.setCellFactory(getCellEstado(jcbDoctor3));
    }

    void initTableView4() {
        Label HoraLabel = new Label("Hora");
        HoraLabel.setStyle("-fx-text-fill: white");
        Label CitasLabel = new Label("Citas");
        CitasLabel.setStyle("-fx-text-fill: white");
        columnHoraAtencion4.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));
        columnCitas4.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));
        columnEstado4.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("hour"));

        columnHoraAtencion4.setCellFactory(getCellHoraAtencion());
        columnHoraAtencion4.setGraphic(HoraLabel);
        columnCitas4.setCellFactory(getCellCitas(jcbDoctor4));
        columnCitas4.setGraphic(CitasLabel);
        columnCitas4.setText("");
        columnEstado4.setCellFactory(getCellEstado(jcbDoctor4));
    }

    Callback<TableColumn<Integer, Integer>, TableCell<Integer, Integer>> getCellHoraAtencion() {
        Callback<TableColumn<Integer, Integer>, TableCell<Integer, Integer>> cellHoraAtencion = (TableColumn<Integer, Integer> param) -> {
            // make cell containing buttons
            final TableCell<Integer, Integer> cell = new TableCell<Integer, Integer>() {
                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows                    
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {

                        Label label = new Label();
                        label.setFont(new Font("Times New Roman Bold", 13));
                        LocalTime time = LocalTime.now();
                        if (item == time.getHour()) {
                            setStyle("-fx-background-color:#334ccc");
                        }
                        label.setStyle("-fx-text-fill: white");

                        label.setText(oUtilClass.toformat12horasAMPM(item));
                        setGraphic(label);
                        setText("");

                    }
                }
            };
            return cell;
        };
        return cellHoraAtencion;
    }

    Callback<TableColumn<Integer, Integer>, TableCell<Integer, Integer>> getCellCitas(JFXComboBox<Doctor> jcb) {
        Callback<TableColumn<Integer, Integer>, TableCell<Integer, Integer>> cellHoraAtencion = (TableColumn<Integer, Integer> param) -> {
            // make cell containing buttons
            final TableCell<Integer, Integer> cell = new TableCell<Integer, Integer>() {
                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows                    
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        List<Cita> listCita = new ArrayList<>();
                        for (Cita citaRaiz : listCitaRaiz) {
                            if (citaRaiz.getDoctor().getPersona().getIdpersona() == jcb.getSelectionModel().getSelectedItem().getPersona().getIdpersona()
                                    && citaRaiz.getHora().getHour() == item
                                    && citaRaiz.getFechacita().isEqual(oFecha)) {
                                listCita.add(citaRaiz);
                            }
                        }
                        FlowPane fp = new FlowPane();
                        //fp.setStyle("-fx-background-color: #b2caf7");
                        boolean isOcupado = false;
                        double tam = 48.16;
                        for (Cita cita : listCita) {
                            isOcupado = cita.getNombrepaciente() == null;
                            if (isOcupado) {
                                Label ocupadoLabel = new Label("OCUPADO");
                                ocupadoLabel.setFont(new Font("Times New Roman Bold", 22));
                                ocupadoLabel.setStyle("-fx-text-fill: red");
                                //fp.setStyle("-fx-background-color: #b2caf7");
                                fp.setAlignment(Pos.CENTER);

                                fp.getChildren().add(ocupadoLabel);
                                break;
                            }
                            JFXButton buttonCita = new JFXButton();
                            buttonCita.setUserData(cita);
                            buttonCita.setPrefWidth(110);
                            buttonCita.getStyleClass().add("button-forma2");
                            buttonCita.setMaxHeight(9);
                            buttonCita.setText(oUtilClass.toformat12horas(cita.getHora().getHour()) + ":" + oUtilClass.toformat00(cita.getHora().getMinute()) + " " + cita.getNombrepaciente());
                            if (cita.getLugar().getIdlugar() != oLugar.getIdlugar()) {
                                buttonCita.setText(oUtilClass.toformat12horas(cita.getHora().getHour()) + ":" + oUtilClass.toformat00(cita.getHora().getMinute()) + " " + cita.getLugar().getNombrelugar());

                            } else {
                                Tooltip tooltipCelular = new Tooltip("Celular: " + (cita.getCelular() == null ? "sin número" : cita.getCelular()));
                                buttonCita.addEventHandler(ActionEvent.ACTION, event -> modificarCita(event, getTableView()));
                                tooltipCelular.setShowDelay(Duration.seconds(0.2));
                                buttonCita.setTooltip(tooltipCelular);
                            }
                            FlowPane.setMargin(buttonCita, new Insets(1, 1, 1, 1));
                            fp.getChildren().add(buttonCita);
                        }
                        fp.setMinHeight(tam);
                        setGraphic(fp);
                        setText(null);
                        setStyle("-fx-pref-height: 0px;   -fx-background-color:  linear-gradient(from 41px 39px to 50px 50px, reflect,  #b7cdf7 30%, #bfd5ff  47%);");
                        setStyle("-fx-background-transparent");
                        LocalTime time = LocalTime.now();
                        if (item == time.getHour()) {
                            setStyle("-fx-background-color:#334ccc");
                        }
                    }
                }

                void modificarCita(ActionEvent event, TableView<Integer> table) {
                    JFXButton buton = (JFXButton) event.getSource();
                    Cita oCita = (Cita) buton.getUserData();
                    CitaModificarController oCitaModificarController = (CitaModificarController) oUtilClass.mostrarVentana(CitaModificarController.class, "CitaModificar", ap);
                    oCitaModificarController.setController(odc, table);
                    oCitaModificarController.setCita(oCita);
                    lockedPantalla();
                }
            };
            return cell;
        };
        return cellHoraAtencion;
    }

    Callback<TableColumn<Integer, Integer>, TableCell<Integer, Integer>> getCellEstado(JFXComboBox<Doctor> jcb) {
        Callback<TableColumn<Integer, Integer>, TableCell<Integer, Integer>> cellFoctory = (TableColumn<Integer, Integer> param) -> {
            // make cell containing buttons
            final TableCell<Integer, Integer> cell = new TableCell<Integer, Integer>() {

                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows                    
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        int tamHightImag = 20;
                        int tamWidthImag = 20;
                        List<Cita> listCitaOcupada = new ArrayList<>();
                        for (Cita citaRaiz : listCitaRaiz) {
                            if (citaRaiz.getDoctor().getPersona().getIdpersona() == jcb.getSelectionModel().getSelectedItem().getPersona().getIdpersona() && citaRaiz.getHora().getHour() == item
                                    && citaRaiz.getFechacita().isEqual(oFecha) && citaRaiz.getRazon().equals("OCUPADO")) {
                                listCitaOcupada.add(citaRaiz);
                            }
                        }
                        List<Cita> listCita = new ArrayList<>();
                        for (Cita citaRaiz : listCitaRaiz) {
                            if (citaRaiz.getDoctor().getPersona().getIdpersona() == jcb.getSelectionModel().getSelectedItem().getPersona().getIdpersona() && citaRaiz.getHora().getHour() == item
                                    && citaRaiz.getFechacita().isEqual(oFecha) && !citaRaiz.getRazon().equals("OCUPADO")) {
                                listCita.add(citaRaiz);
                            }
                        }

                        boolean isCitaEnOtroLugar = false;
                        for (Cita horacita : listCita) {
                            if (horacita.getLugar().getIdlugar() != oLugar.getIdlugar()) {
                                isCitaEnOtroLugar = true;
                                break;
                            }
                        }

                        Button addIcon = new Button();
                        addIcon.setText("+");
                        addIcon.setUserData(item);
                        addIcon.getStyleClass().add("button-formacircle-green");
                        addIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mostrarAgregar(event, getTableView()));

                        addIcon.setVisible(listCitaOcupada.isEmpty() && listCita.size() < 4 && !isCitaEnOtroLugar);

                        Button editIcon2 = new Button();
                        editIcon2.setText("x");
                        editIcon2.setUserData(item);
                        editIcon2.getStyleClass().add("button-formacircle-red");
                        editIcon2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> guardarEliminarBloqueo(event, addIcon));

                        editIcon2.setVisible(listCita.isEmpty());

                        VBox managebtn = new VBox(addIcon, editIcon2);
                        managebtn.setStyle("-fx-alignment:center");
                        VBox.setMargin(addIcon, new Insets(4, 0, 4, 0));
                        if (jcb.getSelectionModel().getSelectedItem() != null) {
                            if (jcb.getSelectionModel().getSelectedItem().getPersona().getIdpersona() != -1) {
                                setGraphic(managebtn);
                            }
                        }
                        LocalTime time = LocalTime.now();
                        if (item == time.getHour()) {
                            setStyle("-fx-background-color:#334ccc");
                        }
                        setText(null);

                    }
                }

                void mostrarAgregar(MouseEvent event, TableView<Integer> table) {
                    Button buton = (Button) event.getSource();
                    int oHora = (Integer) buton.getUserData();

                    CitaAgregarController oCitaAgregarController = (CitaAgregarController) oUtilClass.mostrarVentana(CitaAgregarController.class, "CitaAgregar", ap);
                    oCitaAgregarController.setController(odc, table);
                    oCitaAgregarController.setPersona(oHora, jcb.getSelectionModel().getSelectedItem(), oFecha, oUsuario, list_lugar);
                    lockedPantalla();
                }

                void guardarEliminarBloqueo(MouseEvent event, Button addicon) {
                    int oHora = (Integer) addicon.getUserData();
                    LocalTime lt_hora = LocalTime.of(oHora, 00);
                    List<Cita> listCitaOcupada = new ArrayList<>();

                    for (Cita citaRaiz : listCitaRaiz) {
                        if (citaRaiz.getDoctor().getPersona().getIdpersona() == jcb.getSelectionModel().getSelectedItem().getPersona().getIdpersona() && citaRaiz.getHora().getHour() == oHora
                                && citaRaiz.getFechacita().isEqual(oFecha) && citaRaiz.getRazon().equals("OCUPADO")) {
                            listCitaOcupada.add(citaRaiz);
                        }
                    }

                    if (listCitaOcupada.isEmpty()) {
                        Cita ocita = new Cita(jcb.getSelectionModel().getSelectedItem(), lt_hora, oFecha, "OCUPADO", oLugar, oUsuario);
                        http.AddObject(Cita.class, ocita, "/AddCita");
                        actualizarListMesCita();
                        getTableView().refresh();
                    } else {
                        http.DeleteObject(Cita.class, "/DeleteCita", listCitaOcupada.get(0).getIdcita() + "");
                        actualizarListMesCita();
                        getTableView().refresh();
                    }
                }

                private void imagModificarMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/add-1.png").toExternalForm()));
                }

                private void imagModificarFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/add-2.png").toExternalForm()));
                }

                private void imagBlockMoved(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/block-1.png").toExternalForm()));
                }

                private void imagBlockFuera(MouseEvent event) {
                    ImageView imag = (ImageView) event.getSource();
                    imag.setImage(new Image(getClass().getResource("/imagenes/block-2.png").toExternalForm()));
                }
            };
            return cell;
        };
        return cellFoctory;
    }

    @FXML
    void mostrarImprimir() {
        ImprimirHorarioController oImprimirHorarioController = (ImprimirHorarioController) oUtilClass.mostrarVentana(CitaAgregarController.class, "ImprimirHorario", ap);
        oImprimirHorarioController.setController(odc);
        lockedPantalla();
    }

    @FXML
    void mostrarDoctor() {
        DoctorVerController oRegistrarController = (DoctorVerController) oUtilClass.mostrarVentana(DoctorVerController.class, "DoctorVer", ap);
        oRegistrarController.setController(odc);
        lockedPantalla();
    }

    public void lockedPantalla() {
        if (ap.isDisable()) {
            ap.setDisable(false);
        } else {
            ap.setDisable(true);
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

    @FXML
    void imagSettingsMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/settings-2.png").toExternalForm()));
    }

    @FXML
    void imagSettingsFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/settings-1.png").toExternalForm()));
    }

    @FXML
    void imagFeriadoMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/feriado-2.png").toExternalForm()));
    }

    @FXML
    void imagFeriadoFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/feriado-1.png").toExternalForm()));
    }

    @FXML
    void imagHorarioMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/horario-2.png").toExternalForm()));
    }

    @FXML
    void imagHorarioFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/horario-1.png").toExternalForm()));
    }

    @FXML
    void imagDoctorFuera(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/doctor-1.png").toExternalForm()));
    }

    @FXML
    void imagDoctorMoved(MouseEvent event) {
        ImageView imag = (ImageView) event.getSource();
        imag.setImage(new Image(getClass().getResource("/imagenes/doctor-2.png").toExternalForm()));
    }

    public int numeroDeDiasMes(String mes) {
        int numeroDias = -1;
        switch (mes) {
            case "ENERO":
            case "MARZO":
            case "MAYO":
            case "JULIO":
            case "AGOSTO":
            case "OCTUBRE":
            case "DICIEMBRE":
                numeroDias = 31;
                break;
            case "ABRIL":
            case "JUNIO":
            case "SETIEMBRE":
            case "NOVIEMBRE":
                numeroDias = 30;
                break;
            case "FEBRERO":

                Date anioActual = new Date();
                if (esBisiesto(Integer.parseInt(jcbAnio.getSelectionModel().getSelectedItem()))) {
                    numeroDias = 29;
                } else {
                    numeroDias = 28;
                }
                break;
        }
        return numeroDias;
    }

    public boolean esBisiesto(int anio) {
        GregorianCalendar calendar = new GregorianCalendar();
        boolean esBisiesto = false;
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true;
        }
        return esBisiesto;
    }

    public int getNumMes(String mes) {
        int numMes = -1;
        switch (mes) {
            case "ENERO":
                numMes = 1;
                break;
            case "FEBRERO":
                numMes = 2;
                break;
            case "MARZO":
                numMes = 3;
                break;
            case "ABRIL":
                numMes = 4;
                break;
            case "MAYO":
                numMes = 5;
                break;
            case "JUNIO":
                numMes = 6;
                break;
            case "JULIO":
                numMes = 7;
                break;
            case "AGOSTO":
                numMes = 8;
                break;
            case "SETIEMBRE":
                numMes = 9;
                break;
            case "OCTUBRE":
                numMes = 10;
                break;
            case "NOVIEMBRE":
                numMes = 11;
                break;
            case "DICIEMBRE":
                numMes = 12;
                break;
        }
        return numMes;
    }

    public String getMesNum(int mes) {
        String mesNum = "";
        switch (mes) {
            case 1:
                mesNum = "ENERO";
                break;
            case 2:
                mesNum = "FEBRERO";
                break;
            case 3:
                mesNum = "MARZO";
                break;
            case 4:
                mesNum = "ABRIL";
                break;
            case 5:
                mesNum = "MAYO";
                break;
            case 6:
                mesNum = "JUNIO";
                break;
            case 7:
                mesNum = "JULIO";
                break;
            case 8:
                mesNum = "AGOSTO";
                break;
            case 9:
                mesNum = "SETIEMBRE";
                break;
            case 10:
                mesNum = "OCTUBRE";
                break;
            case 11:
                mesNum = "NOVIEMBRE";
                break;
            case 12:
                mesNum = "DICIEMBRE";
                break;
        }
        return mesNum;
    }

    public String getNombreDia(int dia) {
        String nombreDia = "";
        switch (dia) {
            case 1:
                nombreDia = "LUNES";
                break;
            case 2:
                nombreDia = "MARTES";
                break;
            case 3:
                nombreDia = "MIÉRCOLES";
                break;
            case 4:
                nombreDia = "JUEVES";
                break;
            case 5:
                nombreDia = "VIERNES";
                break;
            case 6:
                nombreDia = "SÁBADO";
                break;
            case 7:
                nombreDia = "DOMINGO";
                break;
        }
        return nombreDia;
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
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(generico.getResource("/css/bootstrap3.css").toExternalForm());;
        Stage stage = new Stage();//creando la base vací
        stage.initStyle(StageStyle.TRANSPARENT);
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

    @FXML
    void cerrarSesion() {
        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
        Alert alertOK = new Alert(Alert.AlertType.INFORMATION);
        Alert alertPregunta = new Alert(Alert.AlertType.CONFIRMATION);

        alertPregunta.setHeaderText(null);
        alertPregunta.setTitle("Info");
        alertPregunta.setContentText(oUsuario.getPersona().getNombres() + " ¿desea cerrar sesión?");
        Optional<ButtonType> result = alertPregunta.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int statusCode = http.CerrarSesion().statusCode();
            if (statusCode == 200) {
                alertOK.setHeaderText(null);
                alertOK.setTitle(null);
                alertOK.setContentText("Nos vemos " + oUsuario.getPersona().getNombres());
                alertOK.showAndWait();
                cerrar();
                LoginController oLoginController = (LoginController) oUtilClass.mostrarVentana(LoginController.class, "Login", new Stage());
            } else if (statusCode == 226) {
                alertWarning.setHeaderText(null);
                alertWarning.setTitle(null);
                alertWarning.setContentText("Ya está se cerró la sesión, puede cerrar el programa :) ");
                alertWarning.showAndWait();

            }
        }
    }

    public void stop() {
        h1.stop();
    }

    @FXML
    void cerrar() {
        stop();
        ((Stage) ap.getScene().getWindow()).close();
    }

    void especial_navidad() {
        if (LocalDate.now().getMonthValue() == 12) {
            img_adorno.setVisible(true);
            bp_citas.getStyleClass().add("fondo_navidad");
            tableDoctor1.setStyle("-fx-background-color: transparent");
            tableDoctor2.setStyle("-fx-background-color: transparent");
            tableDoctor3.setStyle("-fx-background-color: transparent");
            tableDoctor4.setStyle("-fx-background-color: transparent");
        }
    }

    void mostrarDias_especial_navidad(int Dias) {
        fpDias.getChildren().clear();
        LocalDate fechaNow = LocalDate.now();
        boolean auxColor = true;
        for (int i = 1; i <= Dias; i++) {

            LocalDate fechaCita = LocalDate.of(Integer.parseInt(jcbAnio.getSelectionModel().getSelectedItem()), getNumMes(jcbMes.getSelectionModel().getSelectedItem()), i);
            JFXButton bt = new JFXButton();
            bt.setUserData(fechaCita);
            bt.addEventHandler(ActionEvent.ACTION, event -> setFecha(event));
            bt.getStyleClass().clear();

            int diaSemana = fechaCita.getDayOfWeek().getValue();
            /*if (diaSemana == 7) {
               bt.setStyle(colorRed);
            }*/

            bt.setText(i < 10 ? "0" + i : "" + i);
            FlowPane.setMargin(bt, new Insets(2, 4, 2, 4));
            if (diaSemana != 7) {
                if (auxColor) {
                    bt.getStyleClass().add("button-forma1_navidad_red");
                    auxColor = false;
                } else {
                    bt.getStyleClass().add("button-forma1_navidad_green");
                    auxColor = true;
                }
                if (fechaCita.equals(fechaNow)) {
                    bt.getStyleClass().clear();
                    bt.getStyleClass().add("button-forma1-seleccionado");
                }
                fpDias.getChildren().add(bt);
            }

        }
    }
}
