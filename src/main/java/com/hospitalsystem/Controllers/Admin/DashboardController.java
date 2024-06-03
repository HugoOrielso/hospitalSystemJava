package com.hospitalsystem.Controllers.Admin;

import com.hospitalsystem.Controllers.Doctor.DataDoctor;
import com.hospitalsystem.Controllers.Utils.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import static com.hospitalsystem.Controllers.Utils.Utils.runTime;
import static com.hospitalsystem.Controllers.Utils.Database.connectionDB;

public class DashboardController implements Initializable {
    public Circle top_profile;
    public Label top_adminName;
    public Label data_time;
    public Label nav_adminID;
    public Label nav_adminName;
    public Button dashboard_btn;
    public Button doctores_btn;
    public Button pacientes_btn;
    public Button citas_btn;
    public Button perfil_btn;
    public Button pagos_btn;
    public AnchorPane dashboard_form;
    public Label dashboard_AD;
    public Label dashboard_TP;
    public Label dashboard_AP;
    public Label dashboard_TC;
    public AreaChart dashboard_chart_DP;
    public BarChart dashboard_chart_DD;
    public TableView dashboard_tableView;
    public TableColumn dashboard_col_doctorID;
    public TableColumn dashboard_col_nombre;
    public TableColumn dashboard_col_especializacion;
    public AnchorPane doctores_form;
    public TableView<DataDoctor> doctores_tableView;
    public TableColumn<DataDoctor, String> doctores_col_doctorID;
    public TableColumn<DataDoctor, String> doctores_col_nombre;
    public TableColumn<DataDoctor, String> doctores_col_genero;
    public TableColumn<DataDoctor, String> doctores_col_telefono;
    public TableColumn<DataDoctor, String> doctores_col_email;
    public TableColumn<DataDoctor, String> doctores_col_especializacion;
    public TableColumn<DataDoctor, String> doctores_col_direccion;
    public TableColumn<DataDoctor, String> doctores_col_estatus;
    public TableColumn<DataDoctor, String> doctores_col_acciones;

    public AnchorPane pacientes_form;
    public TableView pacientes_tableView;
    public TableColumn pacientes_col_id;
    public TableColumn pacientes_col_nombre;
    public TableColumn pacientes_col_genero;
    public TableColumn pacientes_col_telefono;
    public TableColumn pacientes_col_descripcion;
    public TableColumn pacientes_col_diagnostico;
    public TableColumn pacientes_col_tratamiento;
    public TableColumn pacientes_col_fecha;
    public TableColumn pacientes_col_accion;

    public TableColumn citas_col_id;
    public TableColumn citas_col_nombre;
    public TableColumn citas_col_genero;
    public TableColumn citas_col_telefono;
    public TableColumn citas_col_descripcion;
    public TableColumn citas_col_fecha;
    public TableColumn citas_col_fechaModificacion;
    public TableColumn citas_col_fechaEliminar;
    public TableColumn citas_col_accion;
    public AnchorPane citas_form;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runTime(data_time);
        displayAdminData();
    }

    public void swichtScene(ActionEvent actionEvent){
        if (actionEvent.getSource() == dashboard_btn){
            doctores_form.setVisible(false);
            pacientes_form.setVisible(false);
            citas_form.setVisible(false);
            dashboard_form.setVisible(true);
        }else if (actionEvent.getSource() == doctores_btn){
            doctores_form.setVisible(true);
            pacientes_form.setVisible(false);
            citas_form.setVisible(false);
            dashboard_form.setVisible(false);
        }else if (actionEvent.getSource() == pacientes_btn){
            doctores_form.setVisible(false);
            pacientes_form.setVisible(true);
            citas_form.setVisible(false);
            dashboard_form.setVisible(false);
        }else if (actionEvent.getSource() == citas_btn){
            doctores_form.setVisible(false);
            pacientes_form.setVisible(false);
            citas_form.setVisible(true);
            dashboard_form.setVisible(false);
        }
    }

    public ObservableList<DataDoctor> getDataDoctores(){
        ObservableList<DataDoctor> listData = FXCollections.observableArrayList();
        String consulta = "SELECT * FROM doctores;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            DataDoctor dataD;
            while (resultSet.next()){
                // Integer idDoctor, String doctorName, String genero, String numero, String email, String especializacion, String direccion, String estatus
                dataD = new DataDoctor(resultSet.getInt("id"), resultSet.getString("nombre_completo"), resultSet.getString("sexo"), resultSet.getString("telefono"),resultSet.getString("email"),resultSet.getString("especializacion"),resultSet.getString("direccion"),resultSet.getString("status"));
                listData.add(dataD);
            }

        }catch (Exception e) {e.printStackTrace();}
        return listData;
    }

    public ObservableList<DataDoctor> doctorListData;

    public void doctorDisplayData(){
        doctorListData = getDataDoctores();
        doctores_col_doctorID.setCellValueFactory(new PropertyValueFactory<>("idDoctor"));
        doctores_col_nombre.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        doctores_col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        doctores_col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        doctores_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        doctores_col_especializacion.setCellValueFactory(new PropertyValueFactory<>("especializacion"));
        doctores_col_direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        doctores_col_estatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        doctores_tableView.setItems(doctorListData);
    }

    public void displayAdminData(){
        nav_adminName.setText(Data.admin_userName);
        top_adminName.setText(Data.admin_userName);
        nav_adminID.setText(String.valueOf(Data.admin_id));
        doctorDisplayData();
    }



}