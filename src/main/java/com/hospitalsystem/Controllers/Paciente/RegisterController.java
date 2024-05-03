package com.hospitalsystem.Controllers.Paciente;

import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Utils.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static com.hospitalsystem.Controllers.Utils.Complementos.encryptPassword;
import static com.hospitalsystem.Controllers.Utils.Complementos.isValidEmail;

public class RegisterController implements Initializable {
    public AnchorPane register_form;
    public TextField register_userPaciente;
    public PasswordField register_passwordPaciente;
    public TextField register_showPassword;
    public CheckBox register_checkBox;
    public Button register_singupBtn;
    public Hyperlink register_hyperlink;
    public TextField register_emailPaciente;
    private Stage stage;
    private Scene scene;
    AlertMessage alertMessage = new AlertMessage();
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        register_checkBox.setOnAction(event -> registerShowPassword());
        register_singupBtn.setOnAction(event -> registerAccount());
    }

    public void toLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Paciente/Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void registerShowPassword(){
        if(register_checkBox.isSelected()){
            register_showPassword.setText(register_passwordPaciente.getText());
            register_showPassword.setVisible(true);
            register_passwordPaciente.setVisible(false);
        }else{
            register_passwordPaciente.setText(register_showPassword.getText());
            register_showPassword.setVisible(false);
            register_passwordPaciente.setVisible(true);
        }
    }

    public void registerAccount(){
        if(register_emailPaciente.getText().isEmpty() || register_userPaciente.getText().isEmpty() || register_passwordPaciente.getText().isEmpty()){
            alertMessage.errorMessage("Por favor llena todos los campos.");
            return;
        }
        String checkUserName = "SELECT * FROM pacientes WHERE email = ?";
        connection = Database.connectionDB();
        try {
            if(!register_showPassword.isVisible()){
                if(!register_showPassword.getText().equals(register_passwordPaciente.getText())){
                    register_showPassword.setText(register_passwordPaciente.getText());
                }
            }else{
                if(!register_showPassword.getText().equals(register_passwordPaciente.getText())){
                    register_passwordPaciente.setText(register_showPassword.getText());
                }
            }
            preparedStatement = connection.prepareStatement(checkUserName);
            preparedStatement.setString(1, register_emailPaciente.getText());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                alertMessage.errorMessage("El usuario " + register_userPaciente.getText() + " ya existe.");
                return;
            }
            if(register_passwordPaciente.getText().length() < 8){
                alertMessage.errorMessage("La contraseña debe tener al menos 8 caracteres.");
                return;
            }
            if (!isValidEmail(register_emailPaciente.getText())){
                alertMessage.errorMessage("Ingresa un email válido.");
                return;
            }
            String passwordEncriptada = encryptPassword(register_passwordPaciente.getText());
            String iserData = "INSERT INTO pacientes(email,nombre,password) VALUES (?,?,?);";
            preparedStatement = connection.prepareStatement(iserData);
            preparedStatement.setString(1, register_emailPaciente.getText());
            preparedStatement.setString(2, register_userPaciente.getText());
            preparedStatement.setString(3, passwordEncriptada);
            preparedStatement.executeUpdate();
            alertMessage.successMessagge("Registro exitoso");
            limpiarFormulario();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void limpiarFormulario(){
        register_passwordPaciente.clear();
        register_emailPaciente.clear();
        register_userPaciente.clear();
        register_showPassword.clear();
    }
}
