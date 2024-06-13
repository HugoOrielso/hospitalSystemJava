package com.hospitalsystem.Controllers.Doctor;

import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Citas.CitasData;
import com.hospitalsystem.Controllers.Utils.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import javafx.scene.image.Image;
import java.io.File;
import java.net.URL;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.hospitalsystem.Controllers.Utils.GenerarPDF.generatePDF;
import static com.hospitalsystem.Controllers.Utils.Utils.*;
import static com.hospitalsystem.Controllers.Utils.Data.*;
import static com.hospitalsystem.Controllers.Utils.Database.connectionDB;

public class DoctorMainController implements Initializable {
    public Circle top_profile;
    public Label top_Name;
    public Label data_time;
    public Label nav_doctorId;
    public Label nav_doctorName;
    public Button dashboard_btn;
    public Button pacientes_btn;
    public Button perfil_btn;
    public Button citas_btn;
    public AnchorPane dashboard_form;
    public Label dashboard_AD;
    public Label dashboard_TP;
    public Label dashboard_AP;
    public Label dashboard_TC;
    public BarChart dashboard_chart_DD;
    public TableColumn dashboard_col_doctorID;
    public TableColumn dashboard_col_nombre;
    public AnchorPane pacientes_form;
    public AnchorPane citas_form;
    public TableColumn<CitasData,String> citas_col_id;
    public TableColumn<CitasData,String> citas_col_nombre;
    public TableColumn<CitasData,String> citas_col_genero;
    public TableColumn<CitasData,String> citas_col_telefono;
    public TableColumn<CitasData,String> citas_col_descripcion;
    public TableColumn<CitasData,String> citas_col_fecha;
    public TableColumn<CitasData,String> citas_col_fechaModificacion;
    public TableColumn<CitasData,String> citas_col_fechaEliminar;
    public TableColumn<CitasData,String> citas_col_estatus;
    public TextField tf_pacienteNombre;
    public ComboBox <String> pacienteGenero;
    public TextField tf_pacienteTelefono;
    public TextField tf_pacientePassword;
    public TextField tf_pacienteDireccion;
    public Button paciente_confirmarBtn;
    public Label cp_lb_password;
    public Label cp_lb_fechaCreacion;
    public Label ip_lb_nombre;
    public Label ip_lb_genero;
    public Label ip_lb_telefono;
    public Label ip_lb_direccion;
    public Button ip_lb_addBtn;
    public Button ip_lb_registroBtn;
    public TableView citas_tableView;
    public TextField citas_tf_nombre;
    public ComboBox citas_cb_genero;
    public TextField citas_tf_descripcion;
    public TextField citas_tf_diagnostico;
    public TextField citas_tf_tratamiento;
    public TextField citas_tf_telefono;
    public TextField citas_tf_direccion;
    public ComboBox citas_cb_estatus;
    public Button citas_btn_actualizar;
    public Button citas_btn_insertar;
    public Button citas_btn_eliminar;
    public Button citas_btn_limpiar;
    public DatePicker citas_tf_horario;
    public Label citas_label_citaId;
    public TextField tf_email;
    public Label cp_lb_email;
    public TextField profile_tf_nombre;
    public TextField profile_tf_email;
    public ComboBox profile_tf_genero;
    public TextField profile_tf_numero;
    public TextField profile_tf_direccion;
    public  ComboBox profile_tf_especializacion;
    public Button profile_btn_actualizar;
    public ComboBox profile_tf_estatus;
    public Label profile_label_creacion;
    public Label profile_label_email;
    public Label profile_label_nombre;
    public Button profile_import_btn;
    public Circle profile_circle_image;
    public AnchorPane configuracion_page_perfil;
    public Label profile_label_id;
    public Label label_especializacion;
    public Label label_genero;
    public Label dashboard_IP;
    public TableColumn<CitasData,String> dashboard_col_citaId;
    public TableColumn<CitasData,String> dashboard_col_nombreCita;
    public TableColumn<CitasData,String> dashboard_col_descripcionCita;
    public TableColumn<CitasData,String> dashboard_col_fechaCita;
    public TableColumn<CitasData,String> dashboard_col_estatusCita;
    public TableView<CitasData> dashboard_tableViewCitas;
    public AreaChart dashboard_chart_NDP;
    public BarChart dashboard_chart_NDC;
    public Button logout_btn;
    public Button citas_btn_generarPdf;
    public Label uuid_citas;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private AlertMessage alertMessage = new AlertMessage();
    private Image imagen;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showData();
        runTime(data_time);
        citasMostrarData();
        listaDeGeneros();
        listaEstatus();
        pacienteGeneroLista();
        profileLabels();
        profileFields();
        especializacionesList();
        mostrarImagen();
        displayInfoDashboardTC();
        displayInfoDashboardIP();
        displayInfoDashboardTP();
        displayInfoDashboardAP();
        mostrarDataCitasTable();
        dashboardNP();
        dashboardNC();
        citas_btn_actualizar.setOnAction(event -> actualizarCita());
        citas_btn_insertar.setOnAction(event -> citaBotonInsertar());
        citas_btn_limpiar.setOnAction(event -> limpiarFormularioCitas());
        citas_btn_eliminar.setOnAction(event -> eliminarRegistro());
        paciente_confirmarBtn.setOnAction(event -> confirmBtn());
        ip_lb_addBtn.setOnAction(event -> addBtnPaciente());
        ip_lb_registroBtn.setOnAction(event -> pacienteGrabarBtn());
        profile_btn_actualizar.setOnAction(event -> profileUpdateBtn());
        profile_import_btn.setOnAction(event -> changeProfile());
        logout_btn.setOnAction(event -> cerrarSesion());
        citas_btn_generarPdf.setOnAction(event -> seleccionarCitaToPDF());
    }

    public void seleccionarCitaToPDF(){





        if (doctor_userName.isEmpty() || citas_tf_nombre.getText().isEmpty() || citas_tf_diagnostico.getText().isEmpty()
            || citas_tf_horario.getValue() == null || citas_tf_telefono.getText().isEmpty()
        ){
            alertMessage.errorMessage("Para generar el documento completa todos los campos.");
            return;
        }
        System.out.println(citas_tf_nombre.getText());
        try {
            String consulta = "SELECT * FROM citas WHERE cita_id = ?;";
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, citas_label_citaId.getText());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                generatePDF(doctor_userName, citas_tf_nombre.getText(), citas_tf_diagnostico.getText(), citas_tf_horario.getValue().toString(), citas_tf_telefono.getText(), citas_label_citaId.getText());
                return;
            }
            alertMessage.errorMessage("Selecciona una cita válida");
        }catch (Exception e){e.printStackTrace();}
    }

    public void cerrarSesion(){
        doctor_id = "";
        doctor_userName = "";
        hideStage(logout_btn);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Doctor/Login.fxml"));
        createStage(loader);
    }

    public void displayInfoDashboardIP(){
        String consulta = "SELECT COUNT(id) FROM pacientes WHERE estatus = ? && doctor = ?;";

        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, estatus[1]);
            preparedStatement.setString(2, doctor_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                dashboard_IP.setText(String.valueOf(resultSet.getInt("COUNT(id)")));
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void displayInfoDashboardTP(){
        String consulta = "SELECT COUNT(id) FROM pacientes WHERE doctor = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                dashboard_TP.setText(String.valueOf(resultSet.getInt("COUNT(id)")));
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void displayInfoDashboardAP(){
        String consulta = "SELECT COUNT(id) FROM pacientes WHERE estatus = ? && doctor = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, estatus[0]);
            preparedStatement.setString(2, doctor_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                dashboard_AP.setText(String.valueOf(resultSet.getInt("COUNT(id)")));
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void displayInfoDashboardTC(){
        String consulta = "SELECT COUNT(id) FROM citas WHERE estatus = ? && doctor_id = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, estatus[0]);
            preparedStatement.setString(2, doctor_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                dashboard_TC.setText(String.valueOf(resultSet.getInt("COUNT(id)")));
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void especializacionesList(){
        List<String> ListEspecializaciones = new ArrayList<>();
        for (String data: Data.especializaciones){
            ListEspecializaciones.add(data);
        }
        ObservableList<String> listData = FXCollections.observableList(ListEspecializaciones);
        profile_tf_especializacion.setItems(listData);
    }

    public ObservableList<CitasData> dashboardTablaCitas(){
        ObservableList<CitasData> listData = FXCollections.observableArrayList();
        String consulta = "SELECT * FROM citas WHERE doctor_id = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            CitasData cData;
            while (resultSet.next()){
                cData = new CitasData(resultSet.getString("cita_id"),resultSet.getString("name"),resultSet.getString("descripcion"),resultSet.getString("fecha_creacion"), resultSet.getString("estatus"));
                listData.add(cData);
            }
            return listData;
        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    ObservableList<CitasData> dasboardGetData;
    private void mostrarDataCitasTable(){
         dasboardGetData = dashboardTablaCitas();
        dashboard_col_citaId.setCellValueFactory(new PropertyValueFactory<>("citaID"));
        dashboard_col_nombreCita.setCellValueFactory(new PropertyValueFactory<>("name"));
        dashboard_col_descripcionCita.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        dashboard_col_fechaCita.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        dashboard_col_estatusCita.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        dashboard_tableViewCitas.setItems(dasboardGetData);
    }

    public void dashboardNP(){
        dashboard_chart_NDP.getData().clear();
        String consulta = "SELECT fecha, COUNT(id) FROM pacientes WHERE estatus = ? && doctor = ? GROUP BY TIMESTAMP(fecha) ASC LIMIT 8;";
        try {
            XYChart.Series chart = new XYChart.Series();
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, estatus[2]);
            preparedStatement.setString(2, doctor_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                chart.getData().add(new XYChart.Data(resultSet.getString(1), resultSet.getInt(2)));
            }
            dashboard_chart_NDP.getData().add(chart);
        }catch (Exception e){e.printStackTrace();}
    }

    public void dashboardNC(){
        dashboard_chart_NDC.getData().clear();

        String consulta = "SELECT fecha_creacion, COUNT(id) FROM citas WHERE doctor_id = ? GROUP BY TIMESTAMP(fecha_creacion) ASC LIMIT 7;";
        try {
            XYChart.Series chart = new XYChart.Series();
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                chart.getData().add(new XYChart.Data(resultSet.getString(1), resultSet.getInt(2)));
            }
            dashboard_chart_NDC.getData().add(chart);
        }catch (Exception e){e.printStackTrace();}
    }

    private void confirmBtn(){
        if (
                tf_pacienteNombre.getText().isEmpty() ||
                pacienteGenero.getSelectionModel().getSelectedItem() == null ||
                tf_pacienteTelefono.getText().isEmpty() ||
                tf_pacientePassword.getText().isEmpty() ||
                tf_pacienteDireccion.getText().isEmpty() || tf_email.getText().isEmpty()
        ){
            alertMessage.errorMessage("Por favor completa todos los campos.");
            return;
        }
        Date fecha = new Date(new java.util.Date().getTime());
        cp_lb_password.setText(tf_pacientePassword.getText());
        cp_lb_fechaCreacion.setText(String.valueOf(fecha));
        ip_lb_nombre.setText(tf_pacienteNombre.getText());
        ip_lb_genero.setText(String.valueOf(pacienteGenero.getSelectionModel().getSelectedItem()));
        ip_lb_telefono.setText(tf_pacienteTelefono.getText());
        ip_lb_direccion.setText(tf_pacienteDireccion.getText());
        cp_lb_email.setText(tf_email.getText());
    }

    public void addBtnPaciente(){
        if (cp_lb_password.getText().isEmpty() ||
                cp_lb_email.getText().isEmpty() ||
                cp_lb_fechaCreacion.getText().isEmpty() ||
                ip_lb_nombre.getText().isEmpty() ||
                ip_lb_genero.getText().isEmpty() ||
                ip_lb_telefono.getText().isEmpty() ||
                ip_lb_direccion.getText().isEmpty()
        ) {
            alertMessage.errorMessage("Por favor completa todos los campos.");
            return;
        }
        try{
            String insertarData = "INSERT INTO pacientes (nombre, email, password, numero, direccion, doctor, estatus, genero) VALUES (?,?,?,?,?,?,?,?);";
            if(verificarCorrero(tf_email.getText())){
                alertMessage.errorMessage("El paciente ya existe, por favor intenta con otro");
                return;
            }
            if(!verificarDoctor(doctor_id)){
                alertMessage.errorMessage("El doctor no existe");
                return;
            }
            connection = connectionDB();
            String passwordEncriptada = encryptPassword(tf_pacientePassword.getText());
            if (passwordEncriptada.isEmpty()){
                alertMessage.errorMessage("No se pudo guardar el registro intenta más tarde");
                return;
            }
            preparedStatement = connection.prepareStatement(insertarData);
            preparedStatement.setString(1, tf_pacienteNombre.getText());
            preparedStatement.setString(2, tf_email.getText());
            preparedStatement.setString(3, passwordEncriptada);
            preparedStatement.setString(4, tf_pacienteTelefono.getText());
            preparedStatement.setString(5, tf_pacienteDireccion.getText());
            preparedStatement.setString(6, doctor_id);
            preparedStatement.setString(7, "confirmado");
            preparedStatement.setString(8, pacienteGenero.getSelectionModel().getSelectedItem());
            preparedStatement.executeUpdate();
            alertMessage.confirmationMessage("Agregado correctamente");
            limpiarFormularioAddPaciente();
        }catch (Exception e) {e.printStackTrace();}
    }

    public boolean verificarDoctor(String id){
        if (id.isEmpty()){
            return false;
        }
        try {
            String consulta = "SELECT * FROM doctores WHERE doctor_id = ?;";
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }else {
                return false;
            }
        }catch (Exception e) { e.printStackTrace();}
        return true;
    }

    public void pacienteGrabarBtn(){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Registros/RegistrosPaginaForm.fxml"));
            createStage(loader);
    }

    public void limpiarFormularioAddPaciente(){
        tf_pacienteNombre.clear();
        pacienteGenero.getSelectionModel().clearSelection();
        tf_pacienteTelefono.clear();
        tf_pacientePassword.clear();
        tf_pacienteDireccion.clear();
        tf_email.clear();
        cp_lb_password.setText("");
        cp_lb_fechaCreacion.setText("");
        ip_lb_nombre.setText("");
        ip_lb_genero.setText("");
        ip_lb_telefono.setText("");
        ip_lb_direccion.setText("");
        cp_lb_email.setText("");
    }

    private void pacienteGeneroLista(){
        List<String> listG = new ArrayList<>();

        for (String data : genero){
            listG.add(data);
        }
        ObservableList lista = FXCollections.observableList(listG);
        pacienteGenero.setItems(lista);
    }

    public ObservableList<CitasData> citasObtenerData(){
        ObservableList<CitasData> listData = FXCollections.observableArrayList();
        String consulta = "SELECT * FROM citas WHERE fecha_eliminacion IS NULL";
        connection = connectionDB();
        CitasData citasData;
        try {
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                citasData = new CitasData(resultSet.getString("cita_id"), resultSet.getString("name"),
                        resultSet.getString("genero"), resultSet.getString("telefono"),
                        resultSet.getString("descripcion"), resultSet.getString("fecha_creacion"),resultSet.getDate("fecha_modificacion"),
                        resultSet.getDate("fecha_eliminacion"),resultSet.getString("estatus"), resultSet.getString("diagnostico"),
                        resultSet.getString("tratamiento"), resultSet.getString("direccion"),resultSet.getDate("calendario"));
                listData.add(citasData);
            }
        }catch (Exception e) { e.printStackTrace(); }
        return listData;
    }

    public ObservableList<CitasData> citasListaData;
    public void citaBotonInsertar(){
        if (!validarFormulario()) return;
        if (!existenciaDoctor(doctor_id)){
            alertMessage.errorMessage("El id del doctor es incorrecto o no exite en la base de datos, por favor regístrate");
            return;
        }
        connection = connectionDB();
        try {
            String crearCita  = "INSERT INTO citas ( name, genero,descripcion,diagnostico,tratamiento,telefono," +
                    "direccion,estatus,doctor_id,calendario) VALUES (?,?,?,?,?,?,?,?,?,?);";
            preparedStatement =connection.prepareStatement(crearCita);
            preparedStatement.setString(1, (citas_tf_nombre.getText()));
            preparedStatement.setString(2, (String)citas_cb_genero.getSelectionModel().getSelectedItem());
            preparedStatement.setString(3, (citas_tf_descripcion.getText()));
            preparedStatement.setString(4, (citas_tf_diagnostico.getText()));
            preparedStatement.setString(5, (citas_tf_tratamiento.getText()));
            preparedStatement.setString(6, (citas_tf_telefono.getText()));
            preparedStatement.setString(7, (citas_tf_direccion.getText()));
            preparedStatement.setString(8, (String)citas_cb_estatus.getSelectionModel().getSelectedItem());
            preparedStatement.setString(9, (doctor_id));
            preparedStatement.setString(10, ("" + citas_tf_horario.getValue()));
            preparedStatement.executeUpdate();
            limpiarFormularioCitas();
            citasMostrarData();
            alertMessage.successMessagge("Cita creada exitosamente");
        }catch (Exception e) { e.printStackTrace(); }
    }

    public boolean existenciaDoctor(String id){
        String consulta = "SELECT * FROM doctores WHERE doctor_id = ?;";
        connection = connectionDB();
        try {
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void citasMostrarData(){
        citasListaData = citasObtenerData();
        citas_col_id.setCellValueFactory(new PropertyValueFactory<>("citaID"));
        citas_col_nombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        citas_col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        citas_col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        citas_col_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        citas_col_fecha.setCellValueFactory(new PropertyValueFactory<>("calendario"));
        citas_col_fechaModificacion.setCellValueFactory(new PropertyValueFactory<>("fechaModificacion"));
        citas_col_fechaEliminar.setCellValueFactory(new PropertyValueFactory<>("fechaEliminacion"));
        citas_col_estatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        citas_tableView.setItems(citasListaData);
    }

    public void listaDeGeneros(){
        List<String> listaGeneros = new ArrayList<>();
        for (String data : genero) {
            listaGeneros.add(data);
        }
        ObservableList generos = FXCollections.observableList(listaGeneros);
        citas_cb_genero.setItems(generos);
    }

    public void listaDeGenerosDoctor(){
        List<String> listaGeneros = new ArrayList<>();
        for (String data : genero) {
            listaGeneros.add(data);
        }
        ObservableList generos = FXCollections.observableList(listaGeneros);
        profile_tf_genero.setItems(generos);
    }

    public void profileUpdateBtn(){
        if(profile_tf_nombre.getText().isEmpty()
                || profile_tf_email.getText().isEmpty()
                || profile_tf_numero.getText().isEmpty()
                || profile_tf_direccion.getText().isEmpty()
        ){
            alertMessage.errorMessage("Completa todos los campos");
            return;
        }

        if(path.isEmpty() || "".equals(path) || path == null ){
            String consulta = "UPDATE doctores SET email = ?, nombre_completo = ?, especializacion = ?, status = ?, direccion = ?, sexo = ?, telefono = ?, fecha_modificacion = ? WHERE doctor_id = ?;";
            try {
                java.util.Date date = new java.util.Date();
                Date sqlDate = new java.sql.Date(date.getTime());
                connection = connectionDB();
                preparedStatement = connection.prepareStatement(consulta);
                preparedStatement.setString(1, profile_tf_email.getText());
                preparedStatement.setString(2, profile_tf_nombre.getText());
                preparedStatement.setString(3, profile_tf_especializacion.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(4, profile_tf_estatus.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(5, profile_tf_direccion.getText());
                preparedStatement.setString(6, profile_tf_genero.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(7, profile_tf_numero.getText());
                preparedStatement.setDate(8, sqlDate);
                preparedStatement.setString(9, doctor_id);

                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    alertMessage.successMessagge("Perfil actualizado correctamente.");
                    return;
                }
                alertMessage.errorMessage("No se pudo actializar el perfil, intenta más tarde.");

                return;
            }catch (Exception e) { e.printStackTrace(); }
        }

        String consulta = "UPDATE doctores SET email = ?, nombre_completo = ?, especializacion = ?,  direccion = ?, sexo = ?, telefono = ?, fecha_modificacion = ?, imagen = ? WHERE doctor_id = ?;";
        try {
            java.util.Date date = new java.util.Date();
            Date sqlDate = new java.sql.Date(date.getTime());
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, profile_tf_email.getText());
            preparedStatement.setString(2, profile_tf_nombre.getText());
            if(profile_tf_especializacion.isVisible()){
                preparedStatement.setString(3, profile_tf_especializacion.getSelectionModel().getSelectedItem().toString());
            }
            if(label_genero.isVisible()){
                preparedStatement.setString(3,label_genero.getText());
            }
            preparedStatement.setString(4, profile_tf_direccion.getText());
            if (profile_tf_genero.isVisible()){
                preparedStatement.setString(5, profile_tf_genero.getSelectionModel().getSelectedItem().toString());
            }
            if(label_genero.isVisible()){
                preparedStatement.setString(5,label_genero.getText());
            }
            preparedStatement.setString(6, profile_tf_numero.getText());

            preparedStatement.setDate(7, sqlDate);
            path = path.replace("\\", "\\\\");
            Path transfer = Paths.get(path);


            Path copy = Paths.get("C:\\Users\\Usuario\\OneDrive\\Documentos\\Proyectos\\Java\\HospitalSystem\\src\\main\\java\\com\\hospitalsystem\\Directorio\\" + doctor_id + ".jpg");
            try {
                Files.copy(transfer, copy, StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e) {e.printStackTrace();}
            preparedStatement.setString(8,  copy.toAbsolutePath().toString());

            preparedStatement.setString(9, doctor_id);

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                alertMessage.successMessagge("Perfil actualizado correctamente.");
                return;
            }
            alertMessage.errorMessage("No se pudo actializar el perfil, intenta más tarde.");
            return;
        }catch (Exception e) { e.printStackTrace(); }
    }

    public void changeProfile(){
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Abrir imagen", "*.png", "*.jpg", "*.jpeg", "*.bmp"));
        File file = open.showOpenDialog(profile_import_btn.getScene().getWindow());
        if (file != null) {
            Data.path = file.getAbsolutePath();
            imagen = new Image(file.toURI().toString(),139,101,false,true);
            profile_circle_image.setFill(new ImagePattern(imagen));
        }
    }

    public void profileLabels(){
        String consulta = "SELECT * FROM doctores WHERE doctor_id = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                profile_label_id.setText(resultSet.getString("doctor_id").split("-")[0]);
                profile_label_nombre.setText(resultSet.getString("nombre_completo"));
                profile_label_email.setText(resultSet.getString("email"));
                profile_label_creacion.setText(resultSet.getString("fecha_creacion"));
            }
        }catch (Exception e) { e.printStackTrace(); }
    }

    public void profileFields(){
        String consulta = "SELECT * FROM doctores WHERE doctor_id = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                profile_tf_nombre.setText(resultSet.getString("nombre_completo"));
                profile_tf_email.setText(resultSet.getString("email"));

                if(resultSet.getString("telefono") == null){
                    profile_tf_numero.setPromptText("Aún sin teléfono registrado");
                }else{
                    profile_tf_numero.setText(resultSet.getString("telefono"));
                }

                if(resultSet.getString("sexo") == null){
                    label_genero.setVisible(false);
                    profile_tf_genero.setVisible(true);
                    listaDeGenerosDoctor();
                }else{
                    label_genero.setVisible(true);
                    profile_tf_genero.setVisible(false);
                    label_genero.setText(resultSet.getString("sexo"));
                }

                if (resultSet.getString("direccion") == null){
                    profile_tf_direccion.setPromptText("Aún sin dirección registrada");
                }else {
                    profile_tf_direccion.setText(resultSet.getString("direccion"));
                }

                if (resultSet.getString("especializacion") == null){
                    label_especializacion.setVisible(false);
                    profile_tf_especializacion.setVisible(true);
                    listaEstatus();
                }else{
                    label_especializacion.setVisible(true);
                    profile_tf_especializacion.setVisible(false);
                    label_especializacion.setText(resultSet.getString("especializacion"));
                }
            }
        }catch (Exception e) { e.printStackTrace(); }
    }

    public void mostrarImagen(){
        String consulta = "SELECT * FROM doctores WHERE doctor_id = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String imagenPath = resultSet.getString("imagen");
                if (imagenPath != null && !imagenPath.trim().isEmpty()) {
                    String fullImagePath = "file:" + imagenPath.trim();

                    // Configuración de la imagen para top_profile
                    imagen = new Image(fullImagePath, 1002, 20, false, true);
                    top_profile.setFill(new ImagePattern(imagen));

                    // Configuración de la imagen para profile_circle_image
                    imagen = new Image(fullImagePath, 139, 101, false, true);
                    profile_circle_image.setFill(new ImagePattern(imagen));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listaEstatus(){
        List<String> listaEstatus = new ArrayList<>();
        for (String data : Data.estatus) {
            listaEstatus.add(data);
        }
        ObservableList estatus = FXCollections.observableList(listaEstatus);
        citas_cb_estatus.setItems(estatus);
    }

    private void showData(){
        nav_doctorName.setText(Data.doctor_userName);
        String idReducido = reduceUUID(doctor_id);
        nav_doctorId.setText(idReducido);
        top_Name.setText(Data.doctor_userName);
    }

    public void swithcViews(ActionEvent actionEvent){
        if (actionEvent.getSource() == dashboard_btn){
            dashboard_form.setVisible(true);
            pacientes_form.setVisible(false);
            citas_form.setVisible(false);
            configuracion_page_perfil.setVisible(false);
        }else if (actionEvent.getSource() == pacientes_btn){
            dashboard_form.setVisible(false);
            pacientes_form.setVisible(true);
            configuracion_page_perfil.setVisible(false);
            citas_form.setVisible(false);
        }else if (actionEvent.getSource() == citas_btn){
            dashboard_form.setVisible(false);
            configuracion_page_perfil.setVisible(false);
            pacientes_form.setVisible(false);
            citas_form.setVisible(true);
        } else if (actionEvent.getSource() == perfil_btn) {
            configuracion_page_perfil.setVisible(true);
            citas_form.setVisible(false);
            pacientes_form.setVisible(false);
            dashboard_form.setVisible(false);
        }
    }

    public void limpiarFormularioCitas(){
        citas_label_citaId.setText("");
        citas_tf_descripcion.clear();
        citas_tf_diagnostico.clear();
        citas_tf_tratamiento.clear();
        citas_tf_telefono.clear();
        citas_tf_direccion.clear();
        citas_cb_genero.getSelectionModel().clearSelection();
        citas_cb_estatus.getSelectionModel().clearSelection();
        citas_tf_horario.setValue(null);
    }

    public void actualizarCita(){
        if (!existenciaDoctor(doctor_id)){
            alertMessage.errorMessage("No puedes realizar esta acción");
            return;
        }
        if (!validarFormulario()) return;
        String id = uuidSeparado(citas_label_citaId.getText());
        System.out.println(citas_label_citaId.getText());
        Date sqlDate = new Date(new java.util.Date().getTime());
        connection = connectionDB();
            String consulta = "UPDATE citas SET name = ? , genero = ?, descripcion = ?, diagnostico = ?, tratamiento = ?," +
                    "telefono = ?, direccion = ?, estatus = ? , fecha_modificacion = ?, calendario = ? WHERE cita_id = ?;";
        try {
            if (alertMessage.confirmationMessage("¿Estás seguro que quieres actualizar los datos de la cita: " + id)){
                preparedStatement = connection.prepareStatement(consulta);
                preparedStatement.setString(1, citas_tf_nombre.getText());
                preparedStatement.setString(2, citas_cb_genero.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(3, citas_tf_descripcion.getText());
                preparedStatement.setString(4, citas_tf_diagnostico.getText());
                preparedStatement.setString(5, citas_tf_tratamiento.getText());
                preparedStatement.setString(6, citas_tf_telefono.getText());
                preparedStatement.setString(7, citas_tf_direccion.getText());
                preparedStatement.setString(8, citas_cb_estatus.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setDate(9, sqlDate);
                preparedStatement.setString(10, String.valueOf(citas_tf_horario.getValue()));
                preparedStatement.setString(11, citas_label_citaId.getText());
                preparedStatement.executeUpdate();
                citasMostrarData();
                limpiarFormularioCitas();
                alertMessage.confirmationMessage("Los datos se han actualizado correctamente");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public String uuidSeparado(String uuid){
        String[] idSeparado = uuid.split("-");
        return idSeparado[0];
    }

    private boolean validarFormulario() {
        if (
                citas_tf_nombre.getText().isEmpty() ||
                citas_cb_genero.getSelectionModel().getSelectedItem() == null ||
                citas_tf_descripcion.getText().isEmpty() ||
                citas_tf_diagnostico.getText().isEmpty() ||
                citas_tf_tratamiento.getText().isEmpty() ||
                citas_tf_telefono.getText().isEmpty() ||
                citas_tf_direccion.getText().isEmpty() ||
                citas_cb_estatus.getSelectionModel().getSelectedItem() == null ||
                citas_tf_horario.getValue() == null
        )
        {
            alertMessage.errorMessage("Por favor completa todos los campos.");
            return false;
        }
        return true;
    }


    public void dataSeleccionada(){
        CitasData cData = (CitasData) citas_tableView.getSelectionModel().getSelectedItem();
        if(cData == null){
            return;
        }
        int num = citas_tableView.getSelectionModel().getSelectedIndex();
        if ((num - 1) <  0){
            return;
        }
        if(cData.getFechaCreacion() != null){
            citas_tf_horario.setValue(LocalDate.parse(cData.getFechaCreacion()));
        }
        citas_label_citaId.setText(cData.getCitaID());
        citas_tf_nombre.setText(cData.getName());
        citas_cb_genero.getSelectionModel().select(cData.getGenero());
        citas_tf_descripcion.setText(cData.getDescripcion());
        citas_tf_diagnostico.setText(cData.getDiagnostico());
        citas_tf_tratamiento.setText(cData.getTratamiento());
        citas_tf_telefono.setText(cData.getTelefono());
        citas_tf_direccion.setText(cData.getDireccion());
        citas_cb_estatus.getSelectionModel().select(cData.getEstatus());
    }

    public boolean validarCita(String uuid){
        String checkCita = "SELECT * FROM citas WHERE cita_id = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(checkCita);
            preparedStatement.setString(1, uuid);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }
        }catch (Exception e){ e.printStackTrace(); }
        return false;
    }

    public void eliminarRegistro(){
        if (citas_label_citaId.getText().isEmpty()) {
            alertMessage.errorMessage("Por favor selecciona un item.");
            return;
        }
        if (!validarCita(citas_label_citaId.getText())){
            alertMessage.errorMessage("Por favor selecciona una cita existente");
            return;
        }
        try {
            java.sql.Date fechaEliminacion = new Date(new java.util.Date().getTime());
            String consulta = "UPDATE citas SET fecha_eliminacion = ? WHERE cita_id = ?;";
            connection = connectionDB();
            if (alertMessage.confirmationMessage("Estás seguro que quieres eliminar la cita de " + citas_tf_nombre.getText())) {
                preparedStatement = connection.prepareStatement(consulta);
                preparedStatement.setDate(1, fechaEliminacion);
                preparedStatement.setString(2, citas_label_citaId.getText());
                preparedStatement.executeUpdate();
                citasMostrarData();
                limpiarFormularioCitas();
                alertMessage.confirmationMessage("La cita ha sido eliminada correctamente");
            }else{
                alertMessage.errorMessage("Acción cancelada.");
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
