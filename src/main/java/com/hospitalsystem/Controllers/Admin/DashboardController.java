package com.hospitalsystem.Controllers.Admin;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
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
    public TableView doctores_tableView;
    public TableColumn doctores_col_doctorID;
    public TableColumn doctores_col_nombre;
    public TableColumn doctores_col_genero;
    public TableColumn doctores_col_telefono;
    public TableColumn doctores_col_email;
    public TableColumn doctores_col_especializacion;
    public TableColumn doctores_col_direccion;
    public TableColumn doctores_col_acciones;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dashboard_btn.setOnAction(event -> swichtScene("dashboard"));
        citas_btn.setOnAction(event -> swichtScene("citas"));
        doctores_btn.setOnAction(event -> swichtScene("doctores"));
        pacientes_btn.setOnAction(event -> swichtScene("pacientes"));
        runTime();
        displayAdminData();
    }

    public void swichtScene(String toScene){
        if (toScene.equals("dashboard")){
            doctores_form.setVisible(false);
            pacientes_form.setVisible(false);
            citas_form.setVisible(false);
            dashboard_form.setVisible(true);
        }else if (toScene.equals("doctores")){
            doctores_form.setVisible(true);
            pacientes_form.setVisible(false);
            citas_form.setVisible(false);
            dashboard_form.setVisible(false);
        }else if (toScene.equals("pacientes")){
            doctores_form.setVisible(false);
            pacientes_form.setVisible(true);
            citas_form.setVisible(false);
            dashboard_form.setVisible(false);
        }else if (toScene.equals("citas")){
            doctores_form.setVisible(false);
            pacientes_form.setVisible(false);
            citas_form.setVisible(true);
            dashboard_form.setVisible(false);
        }
    }
    public void displayAdminData(){
        nav_adminName.setText(Data.admin_userName);
        top_adminName.setText(Data.admin_userName);
        nav_adminID.setText(String.valueOf(Data.admin_id));
    }

    public void runTime(){
        new Thread(){
        public void run(){
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            while(true){
            try {
                    Thread.sleep(1000);
            }catch (Exception e){e.printStackTrace();}
            Platform.runLater(() -> {
                data_time.setText((format.format(new Date())));
            });
        }}
        }.start();
    }

}