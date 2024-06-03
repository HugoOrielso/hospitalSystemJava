package com.hospitalsystem.Controllers.Registros;

import com.hospitalsystem.Controllers.Paciente.PacientesData;
import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Utils.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import java.net.URL;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;
import static com.hospitalsystem.Controllers.Utils.Utils.createStage;
import static com.hospitalsystem.Controllers.Utils.Data.doctor_id;
import static com.hospitalsystem.Controllers.Utils.Database.connectionDB;

public class RegistrosController implements Initializable {
    public AnchorPane grabarMainForm;
    public TextField search;
    public TableColumn<PacientesData, String> col_id;
    public TableColumn<PacientesData, String> col_nombre;
    public TableColumn<PacientesData, String> col_genero;
    public TableColumn<PacientesData, String> col_telefono;
    public TableColumn<PacientesData, String> col_direccion;
    public TableColumn<PacientesData, String> col_f_creacion;
    public TableColumn<PacientesData, String> col_f_modificacion;
    public TableColumn<PacientesData, String> col_f_eliminacion;
    public TableColumn<PacientesData, String> col_accion;
    public TableView<PacientesData> tableViewGrabar;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Statement statement;
    private AlertMessage alertMessage = new AlertMessage();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayDataPacientes();
        actionButtons();
    }

    public ObservableList<PacientesData> obtenerDataPacienteDT(){
        ObservableList<PacientesData> listData = FXCollections.observableArrayList();
        String consulta = "SELECT * FROM pacientes WHERE fecha_eliminacion IS NULL && doctor = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, doctor_id);
            resultSet = preparedStatement.executeQuery();
            PacientesData pData;
            while (resultSet.next()){
                pData = new PacientesData(resultSet.getInt("id"), resultSet.getString("codigo"),resultSet.getString("nombre"),
                        resultSet.getString("numero"), resultSet.getString("direccion"), resultSet.getDate("fecha"),
                        resultSet.getDate("fecha_modificacion"), resultSet.getDate("fecha_eliminacion"), resultSet.getString("genero"), resultSet.getString("estatus"));
                listData.add(pData);
            }
        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    private ObservableList<PacientesData> pacientesGrabarData;
    public void displayDataPacientes(){
        pacientesGrabarData = obtenerDataPacienteDT();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        col_direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        col_f_creacion.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        col_f_modificacion.setCellValueFactory(new PropertyValueFactory<>("fechaModificacion"));
        col_f_eliminacion.setCellValueFactory(new PropertyValueFactory<>("fechaEliminacion"));
        tableViewGrabar.setItems(pacientesGrabarData);
    }


    public  void actionButtons(){
        connection = connectionDB();
        pacientesGrabarData = obtenerDataPacienteDT();
        Callback<TableColumn<PacientesData, String>, TableCell<PacientesData, String>> cellFactory = (TableColumn<PacientesData, String> param) -> {
            final TableCell<PacientesData, String> cell = new TableCell<PacientesData, String>() {
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                        return;
                    }

                    Button editButton = new Button("Editar");
                    Button removeButton = new Button("Eliminar");
                    editButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #a413a1, #64308e);\n"
                            + "    -fx-cursor: hand;\n"
                            + "    -fx-text-fill: #fff;\n"
                            + "    -fx-font-size: 14px;\n"
                            + "    -fx-font-family: Arial;");

                    removeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #a413a1, #64308e);\n"
                            + "    -fx-cursor: hand;\n"
                            + "    -fx-text-fill: #fff;\n"
                            + "    -fx-font-size: 14px;\n"
                            + "    -fx-font-family: Arial;");
                    editButton.setOnAction(( event) -> {
                        try {
                            PacientesData pData = tableViewGrabar.getSelectionModel().getSelectedItem();
                            int num = tableViewGrabar.getSelectionModel().getSelectedIndex();
                            if ((num - 1) < -1) {
                                alertMessage.errorMessage("Por favor selecciona un registro.");
                                return;
                            }
                            Data.temp_Code = pData.getId();
                            Data.temp_Name = pData.getNombre();
                            Data.temp_Genero = pData.getGenero();
                            Data.temp_Direccion = pData.getDireccion();
                            Data.temp_Estatus = pData.getEstado();
                            Data.temp_Telefono = pData.getTelefono();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Paciente/EditarPaciente.fxml"));
                            createStage(loader);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    removeButton.setOnAction(( event) -> {
                        PacientesData pData = tableViewGrabar.getSelectionModel().getSelectedItem();
                        int num = tableViewGrabar.getSelectionModel().getSelectedIndex();
                        if ((num - 1) < -1) {
                            alertMessage.errorMessage("Por favor selecciona un registro.");
                            return;
                        }
                        String deleteData = "UPDATE pacientes SET fecha_eliminacion = ?, fecha_modificacion = ? WHERE codigo = ?;";
                        try {
                            if (alertMessage.confirmationMessage("Estás seguro de eliminar a " + pData.getNombre() + " de la base de datos?")){
                                connection = connectionDB();
                                preparedStatement = connection.prepareStatement(deleteData);
                                Date date = new Date();
                                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                                preparedStatement.setDate(1, sqlDate);
                                preparedStatement.setDate(2, sqlDate);
                                preparedStatement.setString(3, pData.getCodigo());
                                preparedStatement.executeUpdate();
                                alertMessage.successMessagge("Paciente elimindao correctamente");
                                displayDataPacientes();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    HBox manageBtn = new HBox(editButton, removeButton);
                    manageBtn.setAlignment(Pos.CENTER);
                    manageBtn.setSpacing(5);
                    setGraphic(manageBtn);
                    setText(null);
                };
            };
            return cell;
        };
        col_accion.setCellFactory(cellFactory);
        tableViewGrabar.setItems(pacientesGrabarData);
    }
}
