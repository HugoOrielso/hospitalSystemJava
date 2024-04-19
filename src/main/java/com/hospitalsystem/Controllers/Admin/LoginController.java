package com.hospitalsystem.Controllers.Admin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Utils.Complementos;
import com.hospitalsystem.Controllers.Utils.Users;
import com.hospitalsystem.Controllers.Utils.Data;
import com.hospitalsystem.Controllers.Utils.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static com.hospitalsystem.Controllers.Utils.Complementos.*;

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
        login_selectUser.setOnAction(event -> Complementos.switchPages(login_selectUser));
        Complementos.userList(login_selectUser);
    }

    public void toRegister(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Admin/Register.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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

    public void loginAccount(){
        if (login_email.getText().isEmpty() || login_password.getText().isEmpty()){
            alert.errorMessage("Completa todos los campos.");
            return;
        }
        if(!Complementos.isValidEmail(login_email.getText())){
            alert.errorMessage("Ingresa una dirección de correo válida.");
            return;
        }

        String sql = "SELECT * FROM admin WHERE email = ?;";
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
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                alert.errorMessage("El usuario no existe, o correo inválido");
                return;
            }
            String passwordDesencriptada =  decryptPassword(resultSet.getString("password"));
            if(!passwordDesencriptada.equals(login_password.getText())){
                alert.errorMessage("Contraseña incorrecta.");
                return;
            }

            Data.admin_userName = resultSet.getString("name");
            Data.admin_id = resultSet.getInt("id");
            alert.successMessagge("Login exitoso");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Dashboard.fxml"));
            createStage(loader);
            hideStage(login_loginBtn);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
