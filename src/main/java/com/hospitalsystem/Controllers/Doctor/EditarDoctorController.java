package com.hospitalsystem.Controllers.Doctor;

import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Utils.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.hospitalsystem.Controllers.Utils.Database.connectionDB;

public class EditarDoctorController implements Initializable {

    public AnchorPane container_edit;
    public TextField edit_doctorNombre;
    public ComboBox edit_doctorGenero;
    public TextField edit_doctorTelefono;
    public TextField edit_doctorDireccion;
    public Button btn_edit_actualizar;
    public ComboBox edit_doctorEstatus;
    public Label editar_doctorId;
    public Label editar_label_id;
    public TextField edit_doctorEmail;
    public ComboBox edit_doctorEspecializacion;
    public ImageView imageViewer;
    public Label indicarImagen;
    private AlertMessage alert = new AlertMessage();
    public Image image;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaEstatus();
        listaGeneros();
        listaEspecializacion();
        setField();
        btn_edit_actualizar.setOnAction(event -> actualizarPerfil());
    }


    public void setField(){
        editar_label_id.setText(Data.temp_doctorID);
        edit_doctorNombre.setText(Data.temp_doctorName);
        edit_doctorTelefono.setText(Data.temp_doctorTelefono);
        edit_doctorEmail.setText(Data.temp_doctorEmail);
        edit_doctorDireccion.setText(Data.temp_doctorDireccion);
        String imagenPath = Data.temp_doctorImagen;
        if (imagenPath != null && !imagenPath.trim().isEmpty()) {
            String fullImagePath = "file:" + imagenPath.trim();
            image = new Image(fullImagePath, 200, 180, false, true);
            imageViewer.setImage(image);
            indicarImagen.setVisible(false);
        }else{
            indicarImagen.setVisible(true);
            indicarImagen.setText("No hay imagen disponible");
        }
        edit_doctorGenero.getSelectionModel().select(Data.temp_doctorGenero);
        edit_doctorEstatus.getSelectionModel().select(Data.temp_doctorEstatus);
        edit_doctorEspecializacion.getSelectionModel().select(Data.temp_doctorEspecializacion);
    }

    public void listaGeneros(){
        List<String> generoL = new ArrayList<String>();
        for (String data: Data.genero){
            generoL.add(data);
        }

        ObservableList listData = FXCollections.observableList(generoL);
        edit_doctorGenero.setItems(listData);
    }

    public void listaEstatus(){
        List<String> estatusL = new ArrayList<String>();
        for (String data: Data.estatus){
            estatusL.add(data);
        }
        ObservableList listData = FXCollections.observableList(estatusL);
        edit_doctorEstatus.setItems(listData);
    }

    public void displayNewDataDoctor(){
        String consulta = "SELECT * FROM doctores WHERE doctor_id = ?;";

        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, editar_label_id.getText());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                edit_doctorNombre.setText(resultSet.getString("nombre_completo"));
                edit_doctorTelefono.setText(resultSet.getString("telefono"));
                edit_doctorDireccion.setText(resultSet.getString("direccion"));
                edit_doctorEmail.setText(resultSet.getString("email"));
                edit_doctorGenero.getSelectionModel().select(resultSet.getString("sexo"));
                edit_doctorEstatus.getSelectionModel().select(resultSet.getString("status"));
                edit_doctorEspecializacion.getSelectionModel().select(resultSet.getString("especializacion"));
            }
        }catch (Exception e){ e.printStackTrace();}
    }

    public void listaEspecializacion(){
        List<String> especializacionL = new ArrayList<String>();

        for (String data : Data.especializaciones){
            especializacionL.add(data);
        }
        ObservableList listData = FXCollections.observableList(especializacionL);
        edit_doctorEspecializacion.setItems(listData);

    }

    public void actualizarPerfil(){
        if(edit_doctorNombre.getText() == null
                || edit_doctorTelefono.getText() == null
                || editar_label_id.getText() == null
                || edit_doctorEmail.getText() == null
                || edit_doctorEspecializacion.getSelectionModel().getSelectedItem() == null
                || edit_doctorEstatus.getSelectionModel().getSelectedItem() == null
                || edit_doctorGenero.getSelectionModel().getSelectedItem() == null
                || edit_doctorDireccion.getText() == null
        ){
            alert.errorMessage("Por favor completa todos los campos");
            return;
        }

        try {
            if (alert.confirmationMessage("Estás seguro de actualizar el doctor " + Data.temp_doctorName)) {
                String consulta = "UPDATE doctores SET email = ?, nombre_completo = ?, status = ?, especializacion = ?, direccion = ?, sexo = ?, telefono = ?, fecha_modificacion = ? WHERE doctor_id = ?;";
                Date fechaHoy = new Date(new java.util.Date().getTime());
                connection = connectionDB();
                preparedStatement = connection.prepareStatement(consulta);
                preparedStatement.setString(1, edit_doctorEmail.getText());
                preparedStatement.setString(2, edit_doctorNombre.getText());
                preparedStatement.setString(3, edit_doctorEstatus.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(4, edit_doctorEspecializacion.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(5, edit_doctorDireccion.getText());
                preparedStatement.setString(6, edit_doctorGenero.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setString(7, edit_doctorTelefono.getText());
                preparedStatement.setDate(8, fechaHoy);
                preparedStatement.setString(9, editar_label_id.getText());
                int affectedRow = preparedStatement.executeUpdate();
                if (affectedRow > 0) {
                    alert.successMessagge("Registro actualizado exitosamente.");
                    displayNewDataDoctor();
                    return;
                }

                alert.errorMessage("No se actualizó el registro, intenta más tarde.");
                return;
            }else{
                alert.errorMessage("Operación cancelada.");
            }


        }catch (Exception e){e.printStackTrace();}
    }
}

