package com.hospitalsystem.Controllers.Admin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

public class LoginController implements Initializable {
    public AnchorPane main_form;
    public AnchorPane login_form;
    public PasswordField login_password;
    public Button login_loginBtn;
    public ComboBox login_selectUser;
    public Hyperlink login_hyperlink;
    public TextField login_email;
    public CheckBox chechBox_password;
    public TextField login_showPassword;
    private Stage stage;
    private Scene scene;
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private AlertMessage alert = new AlertMessage();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        login_loginBtn.setOnAction(event -> loginAccount());
        chechBox_password.setOnAction(event -> showLoginPassword());
        login_selectUser.setOnAction(event -> switchPages());
        userList();
    }
    public void toRegister(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Admin/Register.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
                stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Imagenes/logo.jpg"))));
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
    public void showLoginPassword(){
        if(chechBox_password.isSelected()){
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);
        }else{
            login_password.setText(login_showPassword.getText());
            login_password.setVisible(true);
            login_showPassword.setVisible(false);
        }
    }
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    public void loginAccount(){
        if (login_email.getText().isEmpty() || login_password.getText().isEmpty()){
            alert.errorMessage("Completa todos los campos.");
        }else{
            if(isValidEmail(login_email.getText()) ){
                String sql = "SELECT * FROM admin WHERE email = ? && password = ?;";
                connection = Database.connectionDB();
                try {

                    if(!login_showPassword.isVisible()){
                        if (!login_showPassword.getText().equals(login_password.getText())){
                            login_showPassword.setText(login_password.getText());
                        }
                    }else{
                        if (!login_showPassword.getText().equals(login_password.getText())){
                            login_password.setText(login_showPassword.getText());
                        }
                    }
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1,login_email.getText());
                    preparedStatement.setString(2,login_password.getText());
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()){
                        alert.successMessagge("Login exitoso");
                        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Admin/Dashboard.fxml"));
                        Stage stage = new Stage();
                        stage.setTitle("Hospital Taliznay || Portal de aministración");
                        stage.setScene(new Scene(root));
                        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Imagenes/logo.jpg"))));
                        stage.show();
                        login_loginBtn.getScene().getWindow().hide();
                    }else{
                        alert.errorMessage("Correo o contraseña incorrectos");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                alert.errorMessage("Ingresa una dirección de correo válida.");
            }
        }
    }
}
