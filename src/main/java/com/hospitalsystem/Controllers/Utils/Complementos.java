package com.hospitalsystem.Controllers.Utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hospitalsystem.Controllers.Utils.Database.connectionDB;

public class Complementos {

    public static Connection connection;
    public static PreparedStatement preparedStatement;
    public static ResultSet resultSet;
    public static String reduceUUID(String uuid){
        String[] ids = uuid.split("-");
        return ids[0];
    }

    public static void createStage(FXMLLoader loader, int minWidth, int minHeight) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(Complementos.class.getResource("/Imagenes/logo.jpg"))));
        stage.setResizable(false);
        stage.setMinHeight(minHeight);
        stage.setMinWidth(minWidth);
        stage.setTitle("Hospital Taliznay");
        stage.show();
    }

    public static void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(Complementos.class.getResource("/Imagenes/logo.jpg"))));
        stage.setResizable(true);
        stage.setTitle("Hospital Taliznay");
        stage.show();
    }

    public static void switchPages(ComboBox login_selectUser){
        FXMLLoader loader = null;
        if (login_selectUser.getSelectionModel().getSelectedItem() == "Administrador"){
            loader = new FXMLLoader(Complementos.class.getResource("/Fxml/Admin/Login.fxml"));
        }
        if (login_selectUser.getSelectionModel().getSelectedItem() == "Doctor"){
            loader = new FXMLLoader(Complementos.class.getResource("/Fxml/Doctor/Login.fxml"));
        }
        if (login_selectUser.getSelectionModel().getSelectedItem() == "Paciente"){
            loader = new FXMLLoader(Complementos.class.getResource("/Fxml/Paciente/Login.fxml"));
        }
        login_selectUser.getScene().getWindow().hide();
        createStage(loader,340,580);
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean verificarCorrero(String correo){
        if(correo.isEmpty()){
            return false;
        }
        connection = connectionDB();
        try {
            String consulta = "SELECT email FROM pacientes WHERE email = ?";

            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, correo);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }

        }catch (Exception e) {e.printStackTrace();}

        return false;
    }

    public static void hideStage(Button btn){
        btn.getScene().getWindow().hide();
    }

    public static String encryptPassword(String password) {
        try {
            byte[] encrypt = Base64.getEncoder().encode(password.getBytes());
            return new String(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Or handle the exception appropriately
        }
    }

    public static String decryptPassword(String password) {
        try {
            byte[] decrypt = Base64.getDecoder().decode(password);
            return new String(decrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Or handle the exception appropriately
        }
    }

    public static void runTime(Label labelTime){
        new Thread(){
            public void run(){
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                while(true){
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){e.printStackTrace();}
                    Platform.runLater(() -> {
                        labelTime.setText((format.format(new Date())));
                    });
                }
            }
        }.start();
    }

    public static void userList(ComboBox userList){
        List<String> ListU = new ArrayList<>();
        Collections.addAll(ListU, Users.users);
        ObservableList<String> listData = FXCollections.observableList(ListU);
        userList.setItems(listData);
    }

}

