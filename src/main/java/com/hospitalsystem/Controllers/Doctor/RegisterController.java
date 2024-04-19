package com.hospitalsystem.Controllers.Doctor;

import com.hospitalsystem.Controllers.Utils.AlertMessage;
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
import static com.hospitalsystem.Controllers.Utils.Complementos.encryptPassword;

public class RegisterController implements Initializable {
    public AnchorPane register_form;
    public TextField register_doctorId;
    public PasswordField register_password;
    public TextField register_showPassword;
    public CheckBox register_checkBox;
    public Button register_singupBtn;
    public Hyperlink register_hyperlink;
    public TextField register_email;
    public TextField register_fullName;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private AlertMessage alertMessage = new AlertMessage();
    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        register_singupBtn.setOnAction(event -> registerDoctor());
        register_checkBox.setOnAction(event -> showPassword());
    }

    public void registerDoctor(){
        if(register_fullName.getText().isEmpty() ||  register_email.getText().isEmpty() || register_password.getText().isEmpty()) {
            alertMessage.errorMessage("Completa todos los campos para poder continuar");
            return;
        }

        if(register_password.getText().length() < 8) {
            alertMessage.errorMessage("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        try {
            String checkDoctorID = "SELECT * FROM doctores WHERE email = ?";
            connection = Database.connectionDB();
            preparedStatement = connection.prepareStatement(checkDoctorID);
            preparedStatement.setString(1, register_email.getText());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                alertMessage.errorMessage("El usuario con existe en la base de datos.");
                return;
            }

            if (register_showPassword.isVisible()){
                if (!register_showPassword.getText().equals(register_password.getText())){
                    register_showPassword.setText(register_password.getText());
                }else if(!register_showPassword.getText().equals(register_password.getText())){
                    register_password.setText(register_showPassword.getText());
                }
            }

            String passwordEncriptada = encryptPassword(register_password.getText());
            String insertData = "INSERT INTO doctores (password,email,nombre_completo,status) VALUES (?,?,?,?);";
            preparedStatement = connection.prepareStatement(insertData);
            preparedStatement.setString(1, passwordEncriptada);
            preparedStatement.setString(2, register_email.getText());
            preparedStatement.setString(3, register_fullName.getText());
            preparedStatement.setString(4, "Confirmado");
            preparedStatement.executeUpdate();
            alertMessage.successMessagge("Registro exitoso.");
            cleanForm();

        }catch (Exception e){ e.printStackTrace(); }
    }

    public void cleanForm(){
        register_fullName.clear();
        register_password.clear();
        register_email.clear();
    }
    public void showPassword(){
        if(register_checkBox.isSelected()){
            register_showPassword.setText(register_password.getText());
            register_showPassword.setVisible(true);
            register_password.setVisible(false);
        }else {
            register_password.setText(register_showPassword.getText());
            register_showPassword.setVisible(false);
            register_password.setVisible(true);
        }
    }
     public void toLoginDoctor(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Doctor/Login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
         stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Imagenes/logo.jpg"))));
        stage.setScene(scene);
        stage.show();
     }
}

