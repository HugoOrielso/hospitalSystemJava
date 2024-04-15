package com.hospitalsystem.Controllers.Doctor;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

import static com.hospitalsystem.Complementos.reduceUUID;

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
    public TableView dashboard_tableView;
    public TableColumn dashboard_col_doctorID;
    public TableColumn dashboard_col_nombre;
    public TableColumn dashboard_col_especializacion;
    public AnchorPane pacientes_form;
    public AnchorPane citas_form;
    public TableColumn citas_col_id;
    public TableColumn citas_col_nombre;
    public TableColumn citas_col_genero;
    public TableColumn citas_col_telefono;
    public TableColumn citas_col_descripcion;
    public TableColumn citas_col_fecha;
    public TableColumn citas_col_fechaModificacion;
    public TableColumn citas_col_fechaEliminar;
    public TableColumn citas_col_accion;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showData();
    }

    private void showData(){
        nav_doctorName.setText(DataDoctor.doctor_userName);
        String idReducido = reduceUUID(DataDoctor.doctor_id);
        nav_doctorId.setText(idReducido);
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
