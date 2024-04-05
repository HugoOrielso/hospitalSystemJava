package com.hospitalsystem.Controllers.Doctor;

import com.hospitalsystem.AlertMessage;
import com.hospitalsystem.Controllers.Users;
import com.hospitalsystem.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public AnchorPane login_form;
    public TextField login_showPassword;
    public CheckBox chechBox_password;
    public ComboBox login_selectUser;
    public Hyperlink login_hyperlink;
    public Button login_loginBtnDoctor;
    public PasswordField login_passwordDoctor;
    public TextField login_doctoremail;
    private Connection connection;
    private PreparedStatement  preparedStatement;
    private ResultSet resultSet;

    private AlertMessage alertMessage = new AlertMessage();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login_selectUser.setOnAction(event -> switchPages());
        chechBox_password.setOnAction(event -> showPassword());
        userList();
        login_loginBtnDoctor.setOnAction(event -> loginAccount());
    }
    public void userList(){
        List<String> ListU = new ArrayList<>();
        for (String data : Users.users){
            ListU.add(data);
        }
        ObservableList listData = FXCollections.observableList(ListU);
        login_selectUser.setItems(listData);
    }
    public void switchPages(){
        if (login_selectUser.getSelectionModel().getSelectedItem() == "Administrador"){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Admin/Login.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Hospital Taliznay");
                stage.setMinWidth(340);
                stage.setMinHeight(580);
                stage.setScene(new Scene(root));
                stage.show();
            }catch (Exception e){ e.printStackTrace(); }
        }
        if (login_selectUser.getSelectionModel().getSelectedItem() == "Doctor"){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Doctor/Login.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Hospital Taliznay");
                stage.setMinWidth(340);
                stage.setMinHeight(580);
                stage.setScene(new Scene(root));
                stage.show();
            }catch (Exception e){ e.printStackTrace(); }
        }
        if (login_selectUser.getSelectionModel().getSelectedItem() == "Paciente"){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Paciente/Login.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Hospital Taliznay");
                stage.setMinWidth(340);
                stage.setMinHeight(580);
                stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Imagenes/logo.jpg"))));
                stage.setScene(new Scene(root));
                stage.show();
            }catch (Exception e){ e.printStackTrace(); }
        }
        login_selectUser.getScene().getWindow().hide();
    }

    public void showPassword(){
        if(chechBox_password.isSelected()){
            login_showPassword.setText(login_passwordDoctor.getText());
            login_showPassword.setVisible(true);
            login_passwordDoctor.setVisible(false);
        }else{
            login_passwordDoctor.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_passwordDoctor.setVisible(true);
        }
    }

    public void loginAccount(){
        if(login_doctoremail.getText().isEmpty() || login_passwordDoctor.getText().isEmpty()){
            alertMessage.errorMessage("Completa todos los campos para continuar.");
        }else {
            String sql = "SELECT * FROM doctores WHERE email = ? AND password =?;";
            connection = Database.connectionDB();
            if (login_showPassword.isVisible()){
                if (!login_showPassword.getText().equals(login_passwordDoctor)){
                    login_showPassword.setText(login_passwordDoctor.getText());
                }
                login_passwordDoctor.setText(login_showPassword.getText());
            }

            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, login_doctoremail.getText());
                preparedStatement.setString(2, login_passwordDoctor.getText());
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    alertMessage.successMessagge("Login exitoso");
                }
            }catch (Exception e){ e.printStackTrace(); }
        }
    }
    public void toRegisterDoctor(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Doctor/Register.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Imagenes/logo.jpg"))));
        stage.setScene(scene);
        stage.show();
    }

}
