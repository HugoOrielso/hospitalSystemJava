package com.hospitalsystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Admin/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setMinHeight(550);
        stage.setMinWidth(330);
        stage.setTitle("Hospital Taliznay");
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Imagenes/logo.jpg"))));
        stage.show();
    }

    public static void main(String[] args){
        launch();
    }
}
