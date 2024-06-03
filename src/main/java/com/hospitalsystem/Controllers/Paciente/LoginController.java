package com.hospitalsystem.Controllers.Paciente;

import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Utils.Utils;
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

import static com.hospitalsystem.Controllers.Utils.Utils.decryptPassword;
import static com.hospitalsystem.Controllers.Utils.Utils.isValidEmail;

public class LoginController implements Initializable {

    public AnchorPane main_form;
    public AnchorPane login_form;
    public TextField login_emailPaciente;
    public PasswordField login_passwordPaciente;
    public TextField login_showPassword;
    public CheckBox chechBox_password;
    public Button login_loginBtn;
    public ComboBox login_selectUserPaciente;
    public Hyperlink login_hyperlink;
    private Stage stage;
    private Scene scene;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private AlertMessage alert = new AlertMessage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login_selectUserPaciente.setOnAction(event -> Utils.switchPages(login_selectUserPaciente));
        login_loginBtn.setOnAction(event -> loginAccount());
        chechBox_password.setOnAction(event -> showPassword());
        Utils.userList(login_selectUserPaciente);
    }

    public void toRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Paciente/Register.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void showPassword(){
        if(chechBox_password.isSelected()){
            login_showPassword.setText(login_passwordPaciente.getText());
            login_showPassword.setVisible(true);
            login_passwordPaciente.setVisible(false);
        }else{
            login_passwordPaciente.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_passwordPaciente.setVisible(true);
        }
    }

    public void loginAccount(){
        if (login_emailPaciente.getText().isEmpty() || login_passwordPaciente.getText().isEmpty()) {
            alert.errorMessage("Completa todos los campos.");
            return;
        }
        if(!isValidEmail(login_emailPaciente.getText()) ){
            alert.errorMessage("Estructura de email incorrecta, ingresa un email válido");
            return;
        }
        try {
            String sql = "SELECT * FROM pacientes WHERE email = ?;";
            connection = Database.connectionDB();
            if(!login_showPassword.isVisible()){
                if (!login_showPassword.getText().equals(login_passwordPaciente.getText())){
                    login_showPassword.setText(login_passwordPaciente.getText());
                }
            }else{
                if (!login_showPassword.getText().equals(login_passwordPaciente.getText())){
                    login_passwordPaciente.setText(login_showPassword.getText());
                }
            }
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,login_emailPaciente.getText());
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                alert.errorMessage("El usuario no existe en la base de datos.");
                return;
            }
            String passwordDesencriptada = decryptPassword(resultSet.getString("password"));
            if (!passwordDesencriptada.equals(login_showPassword.getText())){
                alert.errorMessage("Contraseña incorrecta");
                return;
            }
            alert.successMessagge("Login exitoso");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
