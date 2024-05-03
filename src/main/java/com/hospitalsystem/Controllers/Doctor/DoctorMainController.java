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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.hospitalsystem.Controllers.Utils.Complementos.*;
import static com.hospitalsystem.Controllers.Utils.Data.doctor_id;
import static com.hospitalsystem.Controllers.Utils.Data.genero;
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
    public AreaChart dashboard_chart_DP;
    public BarChart dashboard_chart_DD;
    public TableView<CitasData> dashboard_tableView;
    public TableColumn dashboard_col_doctorID;
    public TableColumn dashboard_col_nombre;
    public TableColumn dashboard_col_especializacion;
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
    public TextField citas_tf_citaId;
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
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private AlertMessage alertMessage = new AlertMessage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showData();
        runTime(data_time);
        citasMostrarData();
        listaDeGeneros();
        listaEstatus();
        pacienteGeneroLista();
        citas_btn_actualizar.setOnAction(event -> actualizarCita());
        citas_btn_insertar.setOnAction(event -> citaBotonInsertar());
        citas_btn_limpiar.setOnAction(event -> limpiarFormularioCitas());
        citas_btn_eliminar.setOnAction(event -> eliminarRegistro());
        paciente_confirmarBtn.setOnAction(event -> confirmBtn());
        ip_lb_addBtn.setOnAction(event -> addBtnPaciente());
        ip_lb_registroBtn.setOnAction(event -> pacienteGrabarBtn());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Grabar/GrabarPaginaForm.fxml"));
            createStage(loader);
            hideStage(ip_lb_registroBtn);
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
                        resultSet.getString("descripcion"), resultSet.getDate("fecha_creacion"),resultSet.getDate("fecha_modificacion"),
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
        }else if (actionEvent.getSource() == pacientes_btn){
            dashboard_form.setVisible(false);
            pacientes_form.setVisible(true);
            citas_form.setVisible(false);
        }else if (actionEvent.getSource() == citas_btn){
            dashboard_form.setVisible(false);
            pacientes_form.setVisible(false);
            citas_form.setVisible(true);
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
        citas_label_citaId.setText("" + cData.getCitaID());
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
