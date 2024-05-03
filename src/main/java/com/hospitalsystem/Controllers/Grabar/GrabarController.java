package com.hospitalsystem.Controllers.Grabar;

import com.hospitalsystem.Controllers.Paciente.PacientesData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import static com.hospitalsystem.Controllers.Utils.Data.doctor_id;
import static com.hospitalsystem.Controllers.Utils.Database.connectionDB;

public class GrabarController implements Initializable {
    public AnchorPane grabarMainForm;
    public TextField search;
    public TableColumn<PacientesData, String> col_id;
    public TableColumn<PacientesData, String> col_nombre;
    public TableColumn<PacientesData, String> col_genero;
    public TableColumn<PacientesData, String> col_telefono;
    public TableColumn<PacientesData, String> col_direccion;
    public TableColumn<PacientesData, String> col_f_creacion;
    public TableColumn<PacientesData, String> col_f_modificacion;
    public TableColumn<PacientesData, String> col_f_eliminacion;
    public TableColumn<PacientesData, String> col_accion;
    public TableView<PacientesData> tableViewGrabar;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Statement statement;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayDataPacientes();
    }

    public ObservableList<PacientesData> obtenerDataPacienteDT(){
        ObservableList<PacientesData> listData = FXCollections.observableArrayList();

        String consulta = "SELECT * FROM pacientes WHERE fecha_eliminacion IS NULL && doctor = ?;";
        // Integer id,String codigo, String nombre, String telefono, String direccion, Date fecha, Date fechaModificacion,
        //                         Date fechaEliminacion, String genero)
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            PacientesData pData;
            while (resultSet.next()){
                pData = new PacientesData(resultSet.getInt("id"), resultSet.getString("codigo"),resultSet.getString("nombre"),
                        resultSet.getString("numero"), resultSet.getString("direccion"), resultSet.getDate("fecha"),
                        resultSet.getDate("fecha_modificacion"), resultSet.getDate("fecha_eliminacion"), resultSet.getString("genero"));

                listData.add(pData);
            }
        }catch (Exception e){e.printStackTrace();}
        return listData;
    }
    private ObservableList<PacientesData> pacientesGrabarData;
    public void displayDataPacientes(){
        pacientesGrabarData = obtenerDataPacienteDT();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        col_direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        col_f_creacion.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        col_f_modificacion.setCellValueFactory(new PropertyValueFactory<>("fechaModificacion"));
        col_f_eliminacion.setCellValueFactory(new PropertyValueFactory<>("fechaEliminacion"));
        tableViewGrabar.setItems(pacientesGrabarData);
    }
}
