package com.hospitalsystem.Controllers.Doctor;

import com.hospitalsystem.AlertMessage;
import com.hospitalsystem.Controllers.Citas.CitasData;
import com.hospitalsystem.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import static com.hospitalsystem.Complementos.reduceUUID;
import static com.hospitalsystem.Complementos.runTime;
import static com.hospitalsystem.Database.connectionDB;

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
    public TextField tf_pacienteId;
    public TextField tf_pacienteNombre;
    public ComboBox tf_pacienteGenero;
    public TextField tf_pacienteTelefono;
    public TextField tf_pacientePassword;
    public TextField tf_pacienteDireccion;
    public Button paciente_confirmarBtn;
    public Label cp_lb_pacienteId;
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
                citasData = new CitasData(resultSet.getInt("cita_id"), resultSet.getString("name"),
                        resultSet.getString("genero"), resultSet.getString("telefono"),
                        resultSet.getString("descripcion"), resultSet.getDate("fecha_creacion"),resultSet.getDate("fecha_modificacion"),
                        resultSet.getDate("fecha_eliminacion"),resultSet.getString("estatus"));
                listData.add(citasData);
            }
        }catch (Exception e) { e.printStackTrace(); }
        return listData;
    }


    public ObservableList<CitasData> citasListaData;

    public void citasMostrarData(){
        citasListaData = citasObtenerData();
        citas_col_id.setCellValueFactory(new PropertyValueFactory<>("citaID"));
        citas_col_nombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        citas_col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        citas_col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        citas_col_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        citas_col_fecha.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
        citas_col_fechaModificacion.setCellValueFactory(new PropertyValueFactory<>("fechaModificacion"));
        citas_col_fechaEliminar.setCellValueFactory(new PropertyValueFactory<>("fechaEliminacion"));
        citas_col_estatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        citas_tableView.setItems(citasListaData);
    }

    public void listaDeGeneros(){
        List<String> listaGeneros = new ArrayList<>();
        for (String data : Data.genero) {
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
        String idReducido = reduceUUID(Data.doctor_id);
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
}
