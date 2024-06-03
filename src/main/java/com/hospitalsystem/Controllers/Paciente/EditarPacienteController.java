package com.hospitalsystem.Controllers.Paciente;

import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Utils.Data;
import com.hospitalsystem.Controllers.Utils.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static com.hospitalsystem.Controllers.Utils.Database.connectionDB;

public class EditarPacienteController implements Initializable {

    public TextField edit_pacienteId;
    public TextField edit_pacienteNombre;
    public ComboBox<String> edit_pacienteGenero;
    public TextField edit_telefono;
    public TextField edit_direccion;
    public Button btn_edit_actualizar;
    public ComboBox<String> edit_estatus;
    public AnchorPane container_edit;
    private AlertMessage alertMessage = new AlertMessage();
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    public void setField(){
        edit_pacienteId.setText(String.valueOf(Data.temp_Code));
        edit_pacienteNombre.setText(Data.temp_Name);
        edit_pacienteGenero.getSelectionModel().select(Data.temp_Genero);
        edit_direccion.setText(Data.temp_Direccion);
        edit_telefono.setText(Data.temp_Telefono);
        edit_estatus.getSelectionModel().select(Data.temp_Estatus);
    }

    public void listaGeneros(){
        List<String> generoL = new ArrayList<String>();
        for (String data: Data.genero){
            generoL.add(data);
        }

        ObservableList listData = FXCollections.observableList(generoL);
        edit_pacienteGenero.setItems(listData);
    }

    public void listaEstatus(){
        List<String> estatusL = new ArrayList<String>();
        for (String data: Data.estatus){
            estatusL.add(data);
        }
         ObservableList listData = FXCollections.observableList(estatusL);
        edit_estatus.setItems(listData);
    }

    public void actualizarPaciente(){
        if(edit_pacienteId.getText().isEmpty() || edit_pacienteNombre.getText().isEmpty() || edit_pacienteGenero.getSelectionModel().getSelectedItem() == null ||
            edit_direccion.getText().isEmpty() || edit_telefono.getText().isEmpty() || edit_estatus.getSelectionModel().getSelectedItem() == null
        ){
            alertMessage.errorMessage("Por favor completa todos los campos.");
            return;
        }

        if(edit_telefono.getText().length() > 15){
            alertMessage.errorMessage("Longitud del teléfono debe ser menor a 15.");
            return;
        }
        if(edit_telefono.getText().length() < 10){
            alertMessage.errorMessage("Longitud del telefono debe ser mayor a 10.");
            return;
        }
        String actualizar = "UPDATE pacientes SET nombre = ?, numero = ?, direccion = ?, genero = ?, estatus = ?, fecha_modificacion = ? WHERE id = ?;";
        try {
            if(alertMessage.confirmationMessage("¿Estás seguro que quieres actualizar este registro?")){
                Date fecha = new Date();
                java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());
                connection = connectionDB();
                preparedStatement = connection.prepareStatement(actualizar);
                preparedStatement.setString(1, edit_pacienteNombre.getText());
                preparedStatement.setString(2, edit_telefono.getText());
                preparedStatement.setString(3, edit_direccion.getText());
                preparedStatement.setString(4,edit_pacienteGenero.getSelectionModel().getSelectedItem());
                preparedStatement.setString(5, edit_estatus.getSelectionModel().getSelectedItem());
                preparedStatement.setDate(6, sqlDate);
                preparedStatement.setString(7, edit_pacienteId.getText());
                preparedStatement.executeUpdate();
                alertMessage.successMessagge("Registro actualizado exitosamente.");
                return;
            }

            alertMessage.errorMessage("Actualización cancelada");

        }catch (Exception e){ e.printStackTrace(); }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setField();
        listaEstatus();
        listaGeneros();
        btn_edit_actualizar.setOnAction(event ->  actualizarPaciente());
    }
}
