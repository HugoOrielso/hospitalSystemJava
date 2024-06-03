package com.hospitalsystem.Controllers.Doctor;

import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Utils.Utils;
import com.hospitalsystem.Controllers.Utils.Data;
import com.hospitalsystem.Controllers.Utils.Database;
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
import static com.hospitalsystem.Controllers.Utils.Utils.*;

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
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private AlertMessage alertMessage = new AlertMessage();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login_selectUser.setOnAction(event -> Utils.switchPages(login_selectUser));
        chechBox_password.setOnAction(event -> showPassword());
        Utils.userList(login_selectUser);
        login_loginBtnDoctor.setOnAction(event -> loginAccount());
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
        if(login_doctoremail.getText().isEmpty() || login_passwordDoctor.getText().isEmpty()) {
            alertMessage.errorMessage("Completa todos los campos para continuar.");
            return;
        }
        String sql = "SELECT * FROM doctores WHERE email = ?;";
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
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()){
                alertMessage.errorMessage("El ususario no existe, por favor regístrate.");
                return;
            }

            String passwordDesencriptada = decryptPassword(resultSet.getString("password"));

            if(!passwordDesencriptada.equals(login_passwordDoctor.getText())){
                alertMessage.errorMessage("Contraseña incorrecta.");
                return;
            }

            Data.doctor_id = (resultSet.getString("doctor_id"));
            Data.doctor_userName = resultSet.getString("nombre_completo");
            alertMessage.successMessagge("Login exitoso");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Doctor/DoctorMainForm.fxml"));
            createStage(loader);
            hideStage(login_loginBtnDoctor);

        }catch (Exception e){ e.printStackTrace(); }
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
