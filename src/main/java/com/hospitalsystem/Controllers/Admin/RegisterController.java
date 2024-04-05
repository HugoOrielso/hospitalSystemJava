package com.hospitalsystem.Controllers.Admin;

import com.hospitalsystem.AlertMessage;
import com.hospitalsystem.Database;
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
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public AnchorPane register_form;
    public TextField register_user;
    public PasswordField register_password;
    public TextField register_showPassword;
    public CheckBox register_checkBox;
    public Button register_singupBtn;
    public Hyperlink register_hyperlink;
    public TextField register_email;
    private Stage stage;
    private Scene scene;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private AlertMessage alertMessage = new AlertMessage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        register_singupBtn.setOnAction(event -> registerAccount());
        register_checkBox.setOnAction(event -> registerShowPassword());
    }
    public void toLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Admin/Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Imagenes/logo.jpg"))));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void limpiarFormulario(){
        register_password.clear();
        register_email.clear();
        register_user.clear();
        register_showPassword.clear();
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    public void registerShowPassword(){
        if(register_checkBox.isSelected()){
            register_showPassword.setText(register_password.getText());
            register_showPassword.setVisible(true);
            register_password.setVisible(false);
        }else{
            register_password.setText(register_showPassword.getText());
            register_showPassword.setVisible(false);
            register_password.setVisible(true);
        }
    }
    public void registerAccount(){
        if(register_email.getText().isEmpty() || register_user.getText().isEmpty() || register_password.getText().isEmpty()){
            alertMessage.errorMessage("Por favor llena todos los campos.");
        }
        String checkUserName = "SELECT * FROM admin WHERE name = ?";
        connection = Database.connectionDB();
        try {
            if(!register_showPassword.isVisible()){
                if(!register_showPassword.getText().equals(register_password.getText())){
                    register_showPassword.setText(register_password.getText());
                }
            }else{
                if(!register_showPassword.getText().equals(register_password.getText())){
                    register_password.setText(register_showPassword.getText());
                }
            }
            preparedStatement = connection.prepareStatement(checkUserName);
            preparedStatement.setString(1, register_user.getText());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                alertMessage.errorMessage("El usuario " + register_user.getText() + " ya existe.");
            }
            if(register_password.getText().length() < 8){
                alertMessage.errorMessage("La contraseña debe tener al menos 8 caracteres.");
            }
            if (isValidEmail(register_email.getText())){
                String iserData = "INSERT INTO admin(email,name,password) VALUES (?,?,?);";
                preparedStatement = connection.prepareStatement(iserData);
                preparedStatement.setString(1, register_email.getText());
                preparedStatement.setString(2, register_user.getText());
                preparedStatement.setString(3, register_password.getText());
                preparedStatement.executeUpdate();
                alertMessage.successMessagge("Registro exitoso");
                limpiarFormulario();
            }else{
                alertMessage.errorMessage("Ingresa un email válido.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
