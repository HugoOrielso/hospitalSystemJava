package com.hospitalsystem.Controllers.Admin;

import com.hospitalsystem.Controllers.Citas.CitasData;
import com.hospitalsystem.Controllers.Doctor.DataDoctor;
import com.hospitalsystem.Controllers.Paciente.PacientesData;
import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Utils.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static com.hospitalsystem.Controllers.Utils.Data.doctor_id;
import static com.hospitalsystem.Controllers.Utils.Data.path;
import static com.hospitalsystem.Controllers.Utils.Database.connectionDB;
import static com.hospitalsystem.Controllers.Utils.Utils.*;

public class DashboardController implements Initializable {
    public Circle top_profile;
    public Label top_adminName;
    public Label data_time;
    public Label nav_adminID;
    public Label nav_adminName;
    public Button dashboard_btn;
    public Button doctores_btn;
    public Button pacientes_btn;
    public Button citas_btn;
    public Button perfil_btn;
    public Button pagos_btn;
    public AnchorPane dashboard_form;
    public AnchorPane configuracion_form_profile;
    public Label dashboard_AD;
    public Label dashboard_TP;
    public Label dashboard_AP;
    public Label dashboard_TC;
    public AreaChart dashboard_chart_DP;
    public BarChart dashboard_chart_DD;

    public TableView<DataDoctor> dashboard_tableViewDataDoctor;
    public TableColumn<DataDoctor, String> dashboard_col_estatus;
    public TableColumn<DataDoctor,String> dashboard_col_doctorID;
    public TableColumn<DataDoctor,String> dashboard_col_nombre;
    public TableColumn<DataDoctor,String> dashboard_col_especializacion;
    public AnchorPane doctores_form;
    public TableView<DataDoctor> doctores_tableView;
    public TableColumn<DataDoctor, String> doctores_col_doctorID;
    public TableColumn<DataDoctor, String> doctores_col_nombre;
    public TableColumn<DataDoctor, String> doctores_col_genero;
    public TableColumn<DataDoctor, String> doctores_col_telefono;
    public TableColumn<DataDoctor, String> doctores_col_email;
    public TableColumn<DataDoctor, String> doctores_col_especializacion;
    public TableColumn<DataDoctor, String> doctores_col_direccion;
    public TableColumn<DataDoctor, String> doctores_col_estatus;
    public TableColumn<DataDoctor, String> doctores_col_acciones;

    public AnchorPane pacientes_form;
    public TableView<PacientesData> pacientes_tableView;
    public TableColumn<PacientesData,String> pacientes_col_id;
    public TableColumn<PacientesData,String> pacientes_col_nombre;
    public TableColumn<PacientesData,String> pacientes_col_genero;
    public TableColumn<PacientesData,String> pacientes_col_telefono;
    public TableColumn<PacientesData,String> pacientes_col_descripcion;
    public TableColumn<PacientesData,String> pacientes_col_diagnostico;
    public TableColumn<PacientesData,String> pacientes_col_tratamiento;
    public TableColumn<PacientesData,String> pacientes_col_fecha;
    public TableColumn<PacientesData,String> pacientes_col_accion;

    public TableView<CitasData> citas_tableView;
    public TableColumn<CitasData, String> citas_col_id;
    public TableColumn<CitasData, String> citas_col_nombre;
    public TableColumn<CitasData, String> citas_col_genero;
    public TableColumn<CitasData, String> citas_col_telefono;
    public TableColumn<CitasData, String> citas_col_descripcion;
    public TableColumn<CitasData, String> citas_col_fecha;
    public TableColumn<CitasData, String> citas_col_fechaModificacion;
    public TableColumn<CitasData, String> citas_col_estatus;

    public AnchorPane citas_form;
    public Button configuracion_btn_importar;
    public Label configuracion_label_nombre;
    public Label configuracion_label_email;
    public Label configuracion_label_fechaCreacion;
    public TextField configuracion_nombre_tf;
    public TextField configuracion_email_tf;
    public Button configuracion_btn_actualizar;
    public ComboBox configuracion_genero_cb;
    public Circle profile_circle;

    public AnchorPane payments_form;
    public Circle circle_img_payment;
    public TableView<PacientesData> pagos_tableView;
    public TableColumn<PacientesData, String> payment_col_id;
    public TableColumn<PacientesData, String> payment_col_nombre;
    public TableColumn<PacientesData, String> payment_col_genero;
    public TableColumn<PacientesData, String> payment_col_diagnostico;
    public TableColumn<PacientesData, String> payment_col_doctor;
    public TableColumn<PacientesData, String> payment_col_fecha;
    public Button btn_verificar_payment;
    public Label label_idPaciente;
    public Label label_nombre;
    public Label label_doctor;
    public Label label_fecha;
    public Button logout_btn;


    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private AlertMessage alertMessage = new AlertMessage();
    private Image image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runTime(data_time);
        displayAdminData();
        actionButtons();
        displayDataPacientes();
        actionButtonsPaciente();
        displayDataCitas();
        setGeneros();
        profileDisplayInfo();
        mostrarImagen();
        PaymentsDisplayData();
        dashboardDA();
        dashboardAP();
        dashboardTP();
        dashboardTC();
        displayDataDoctor();
        dashboardPacientesDataChart();
        dashboardDoctorDataChart();
        configuracion_btn_importar.setOnAction(event -> profileInsertImage());
        pagos_tableView.setOnMouseClicked(event -> paymentSelectItem());
        configuracion_btn_actualizar.setOnAction(event -> actualizarPerfil());
        btn_verificar_payment.setOnAction(event -> toVerificarPago());
        logout_btn.setOnAction(event -> logout());
    }

    public void logout(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/login.fxml"));
        Data.admin_userName = null;
        Data.admin_id = null;
        hideStage(logout_btn);
        createStage(loader);
    }


    public void toVerificarPago(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/CheckOutPaciente.fxml"));
        createStage(loader,600,400);
    }

    public void profileDisplayInfo(){
        String consulta = "SELECT * FROM admin WHERE id = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setInt(1, Data.admin_id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                configuracion_label_nombre.setText(resultSet.getString("name"));
                configuracion_label_email.setText(resultSet.getString("email"));
                configuracion_label_fechaCreacion.setText(resultSet.getString("fecha"));
                configuracion_nombre_tf.setText(resultSet.getString("name"));
                configuracion_email_tf.setText(resultSet.getString("email"));
                if ((resultSet.getString("genero") != null)){
                    configuracion_genero_cb.getSelectionModel().select(resultSet.getString("genero"));
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void actualizarPerfil(){
        if (configuracion_nombre_tf.getText().isEmpty() ||
                configuracion_email_tf.getText().isEmpty() ||
                configuracion_genero_cb.getSelectionModel().getSelectedItem() == null
        ){
            alertMessage.errorMessage("Por favor completa todos los campos");
            return;
        }

        if (Data.path == null || "".equals(Data.path)){
            connection = connectionDB();
            String updateData = "UPDATE admin SET name = ?, email = ?, genero = ? WHERE id = ?;";

            try {
                preparedStatement = connection.prepareStatement(updateData);
                preparedStatement.setString(1,configuracion_nombre_tf.getText());
                preparedStatement.setString(2,configuracion_email_tf.getText());
                preparedStatement.setString(3,configuracion_genero_cb.getSelectionModel().getSelectedItem().toString());
                preparedStatement.setInt(4,Data.admin_id);
                int rowAffected = preparedStatement.executeUpdate();
                if(rowAffected>0){
                    alertMessage.confirmationMessage("Perfil actualizado correctamente");
                    profileDisplayInfo();
                    mostrarImagen();
                }else {
                    alertMessage.errorMessage("No se pudo actualizar el perfil");
                }
            }catch (Exception e){e.printStackTrace();}
            return;
        }
        String updateData = "UPDATE admin SET name = ?, email = ?, genero = ?, imagen = ? WHERE id = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(updateData);
            preparedStatement.setString(1,configuracion_nombre_tf.getText());
            preparedStatement.setString(2,configuracion_email_tf.getText());
            preparedStatement.setString(3,configuracion_genero_cb.getSelectionModel().getSelectedItem().toString());
            path = path.replace("\\", "\\\\");
            Path transfer = Paths.get(path);
            Path copy = Paths.get("C:\\Users\\Usuario\\OneDrive\\Documentos\\Proyectos\\Java\\HospitalSystem\\src\\main\\java\\com\\hospitalsystem\\Directorio\\" + doctor_id + ".jpg");
            try {
                Files.copy(transfer, copy, StandardCopyOption.REPLACE_EXISTING);
            }catch (Exception e) {e.printStackTrace();}
            preparedStatement.setString(4,  copy.toAbsolutePath().toString());
            preparedStatement.setInt(5,Data.admin_id);
            int rowAffected = preparedStatement.executeUpdate();
            if(rowAffected>0){
                alertMessage.confirmationMessage("Perfil actualizado correctamente");
                profileDisplayInfo();
                mostrarImagen();
                return;
            }else{
                alertMessage.errorMessage("No se pudo actualizar el perfil");
            }

        }catch (Exception e){e.printStackTrace();}
    }

    public ObservableList<PacientesData> paymentGetData(){
        ObservableList<PacientesData> listData = FXCollections.observableArrayList();
        String consulta = "SELECT pacientes.codigo, pacientes.nombre, pacientes.genero, pacientes.diagnostico, pacientes.tratamiento, pacientes.doctor, pacientes.fecha, doctores.nombre_completo AS nombreDoctor, pacientes.imagen FROM pacientes INNER JOIN doctores ON pacientes.doctor = doctores.doctor_id WHERE pacientes.fecha_eliminacion IS NULL && estatus_pago IS NULL;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            PacientesData pData;
            while (resultSet.next()){
                // String codigo, String nombre, String genero, String diagnostico, String tratamiento,
                // String doctor, Date fecha, String doctorName
                pData = new PacientesData(resultSet.getString("codigo"),resultSet.getString("nombre"), resultSet.getString("genero"),
                        resultSet.getString("diagnostico"), resultSet.getString("tratamiento"),
                        resultSet.getString("doctor"),resultSet.getDate("fecha"),resultSet.getString("nombreDoctor"), resultSet.getString("imagen"));
                listData.add(pData);
            }
        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    public ObservableList<PacientesData> paymentList;
    public void PaymentsDisplayData(){
        paymentList = paymentGetData();
        payment_col_id.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        payment_col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        payment_col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        payment_col_diagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        payment_col_doctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        payment_col_fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        pagos_tableView.setItems(paymentList);
    }

    public void paymentSelectItem(){
        PacientesData pData = pagos_tableView.getSelectionModel().getSelectedItem();
        int num = pagos_tableView.getSelectionModel().getSelectedIndex();
        if((num - 1) < -1 ){
            return;
        }
        if (pData.getImagen() != null){
            String path = "File:" + pData.getImagen();
            image = new Image(path,147,114,false,true);
            circle_img_payment.setFill(new ImagePattern(image));
            Data.temp_path = pData.getImagen();
        }
        Data.temp_doctorNamePaciente = pData.getDoctorName();
        Data.temp_doctorIdPaciente = pData.getDoctor();
        Data.temp_pacienteId = pData.getCodigo();
        Data.temp_Name = pData.getNombre();
        Data.temp_date = pData.getFecha().toString();
        label_idPaciente.setText(pData.getCodigo().split("-")[0]);
        label_nombre.setText(pData.getNombre());
        label_doctor.setText(pData.getDoctor().split("-")[0]);
        label_fecha.setText(pData.getFecha().toString());
    }

    public ObservableList<DataDoctor> dashboardGetDataDoctor(){
        ObservableList<DataDoctor> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM doctores WHERE fecha_eliminacion IS NULL;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            DataDoctor dData;
            while (resultSet.next()){
                dData = new DataDoctor(resultSet.getInt("id"), resultSet.getString("nombre_completo"), resultSet.getString("especializacion"), resultSet.getString("status"));
                listData.add(dData);
            }
        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    public ObservableList<DataDoctor> dashboardDataDoctor;
    public void displayDataDoctor(){
        dashboardDataDoctor = dashboardGetDataDoctor();
        dashboard_col_doctorID.setCellValueFactory(new PropertyValueFactory<>("idDoctor"));
        dashboard_col_nombre.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        dashboard_col_especializacion.setCellValueFactory(new PropertyValueFactory<>("especializacion"));
        dashboard_col_estatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        dashboard_tableViewDataDoctor.setItems(dashboardDataDoctor);
    }

    public void dashboardPacientesDataChart(){
        dashboard_chart_DP.getData().clear();
        String consulta = "SELECT fecha, COUNT(id) FROM pacientes WHERE fecha_eliminacion IS NULL GROUP BY TIMESTAMP(fecha) ASC LIMIT 8;";
        XYChart.Series chart = new XYChart.Series();
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                chart.getData().add(new XYChart.Data(resultSet.getString(1), resultSet.getInt(2)));
            }

            dashboard_chart_DP.getData().add(chart);
        }catch (Exception e){e.printStackTrace();}
    }

    public void dashboardDoctorDataChart(){
        dashboard_chart_DD.getData().clear();
        String consulta = "SELECT fecha_creacion, COUNT(id) FROM doctores WHERE fecha_eliminacion IS NULL GROUP BY TIMESTAMP(fecha_creacion) ASC LIMIT 8;";
        XYChart.Series chart = new XYChart.Series();
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                chart.getData().add(new XYChart.Data(resultSet.getString(1), resultSet.getInt(2)));
            }

            dashboard_chart_DD.getData().add(chart);
        }catch (Exception e){e.printStackTrace();}
    }

    public void mostrarImagen(){
        String consulta = "SELECT * FROM admin WHERE id = ?;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setInt(1, Data.admin_id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String imagenPath = resultSet.getString("imagen");
                if (imagenPath != null && !imagenPath.trim().isEmpty()) {
                    String fullImagePath = "file:" + imagenPath.trim();
                    image = new Image(fullImagePath, 1002, 20, false, true);
                    top_profile.setFill(new ImagePattern(image));
                    image = new Image(fullImagePath, 140, 125, false, true);
                    profile_circle.setFill(new ImagePattern(image));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dashboardDA(){
        String consulta = "SELECT COUNT(id) FROM doctores WHERE status = ? && fecha_eliminacion IS NULL;";
        try {
            int doctesActivos = 0;
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, Data.estatus[0]);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                doctesActivos = resultSet.getInt("COUNT(id)");
            }
            dashboard_AD.setText(String.valueOf(doctesActivos));
        }catch (Exception e) { e.printStackTrace(); }
    }

    public void dashboardTP(){
        String consulta = "SELECT COUNT(id) FROM pacientes WHERE fecha_eliminacion IS NULL;";
        try {
            int pacientes = 0;
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pacientes = resultSet.getInt("COUNT(id)");
            }
            dashboard_TP.setText(String.valueOf(pacientes));
        }catch (Exception e) { e.printStackTrace(); }
    }

    public void dashboardAP(){
        String consulta = "SELECT COUNT(id) FROM pacientes WHERE fecha_eliminacion IS NULL && estatus = ?;";
        try {
            int pacientes = 0;
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, Data.estatus[2]);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pacientes = resultSet.getInt("COUNT(id)");
            }
            dashboard_AP.setText(String.valueOf(pacientes));
        }catch (Exception e) { e.printStackTrace(); }
    }

    public void dashboardTC(){
        String consulta = "SELECT COUNT(id) FROM citas WHERE fecha_eliminacion IS NULL;";
        try {
            int pacientes = 0;
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pacientes = resultSet.getInt("COUNT(id)");
            }
            dashboard_TC.setText(String.valueOf(pacientes));
        }catch (Exception e) { e.printStackTrace(); }
    }



    public void setGeneros(){
        List<String> listG = new ArrayList<String>();
        for(String data: Data.genero){
            listG.add(data);
        }
        ObservableList listData = FXCollections.observableList(listG);
        configuracion_genero_cb.setItems(listData);
    }

    ObservableList<DataDoctor> doctoresDataList;
    public  void actionButtons(){
        connection = connectionDB();
        doctoresDataList = getDataDoctores();
        Callback<TableColumn<DataDoctor, String>, TableCell<DataDoctor, String>> cellFactory = (TableColumn<DataDoctor, String> param) -> {
            final TableCell<DataDoctor, String> cell = new TableCell<DataDoctor, String>() {
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                        return;
                    }

                    Button editButton = new Button("Editar");
                    Button removeButton = new Button("Eliminar");
                    editButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);\n"
                            + "    -fx-cursor: hand;\n"
                            + "    -fx-text-fill: #fff;\n"
                            + "    -fx-font-size: 14px;\n"
                            + "    -fx-font-family: Arial;");

                    removeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);\n"
                            + "    -fx-cursor: hand;\n"
                            + "    -fx-text-fill: #fff;\n"
                            + "    -fx-font-size: 14px;\n"
                            + "    -fx-font-family: Arial;");
                    editButton.setOnAction(( event) -> {
                        try {
                            DataDoctor dData = doctores_tableView.getSelectionModel().getSelectedItem();
                            int num = doctores_tableView.getSelectionModel().getSelectedIndex();
                            if ((num - 1) < -1) {
                                alertMessage.errorMessage("Por favor selecciona un registro.");
                                return;
                            }
                            Data.temp_doctorCode = dData.getIdDoctor();
                            Data.temp_doctorName = dData.getDoctorName();
                            Data.temp_doctorGenero = dData.getGenero();
                            Data.temp_doctorDireccion = dData.getDireccion();
                            Data.temp_doctorEstatus = dData.getEstatus();
                            Data.temp_doctorTelefono = dData.getTelefono();
                            Data.temp_doctorID = dData.getDoctorId();
                            Data.temp_doctorEmail = dData.getEmail();
                            Data.temp_doctorEspecializacion = dData.getEspecializacion();
                            Data.temp_doctorImagen = dData.getImagen();

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Doctor/EditarDoctor.fxml"));
                            createStage(loader, 620, 417);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    removeButton.setOnAction((event) -> {
                        DataDoctor dData = doctores_tableView.getSelectionModel().getSelectedItem();
                        int num = doctores_tableView.getSelectionModel().getSelectedIndex();
                        if ((num - 1) < -1) {
                            alertMessage.errorMessage("Por favor selecciona un registro.");
                            return;
                        }
                        String deleteData = "UPDATE doctores SET fecha_eliminacion = ?, fecha_modificacion = ? WHERE doctor_id = ?;";
                        try {
                            if (alertMessage.confirmationMessage("Estás seguro de eliminar a " + dData.getDoctorName() + " de la base de datos?")){
                                connection = connectionDB();
                                preparedStatement = connection.prepareStatement(deleteData);
                                Date date = new Date();
                                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                                preparedStatement.setDate(1, sqlDate);
                                preparedStatement.setDate(2, sqlDate);
                                preparedStatement.setString(3, dData.getDoctorId());
                                preparedStatement.executeUpdate();
                                doctorDisplayData();
                                alertMessage.successMessagge("Doctor elimindao correctamente");
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
            doctorDisplayData();
            return cell;
        };
        doctores_col_acciones.setCellFactory(cellFactory);
        doctores_tableView.setItems(doctoresDataList);
    }

    public void swichtScene(ActionEvent actionEvent){
        if (actionEvent.getSource() == dashboard_btn){
            doctores_form.setVisible(false);
            pacientes_form.setVisible(false);
            citas_form.setVisible(false);
            dashboard_form.setVisible(true);
            payments_form.setVisible(false);
            configuracion_form_profile.setVisible(false);
            displayDataDoctor();
        }else if (actionEvent.getSource() == doctores_btn){
            doctores_form.setVisible(true);
            pacientes_form.setVisible(false);
            citas_form.setVisible(false);
            dashboard_form.setVisible(false);
            payments_form.setVisible(false);
            configuracion_form_profile.setVisible(false);
            doctorDisplayData();
            actionButtons();
        }else if (actionEvent.getSource() == pacientes_btn){
            doctores_form.setVisible(false);
            pacientes_form.setVisible(true);
            payments_form.setVisible(false);
            citas_form.setVisible(false);
            dashboard_form.setVisible(false);
            configuracion_form_profile.setVisible(false);
            displayDataPacientes();
        }else if (actionEvent.getSource() == citas_btn){
            doctores_form.setVisible(false);
            payments_form.setVisible(false);
            pacientes_form.setVisible(false);
            citas_form.setVisible(true);
            dashboard_form.setVisible(false);
            configuracion_form_profile.setVisible(false);
        }else if (actionEvent.getSource() == perfil_btn){
            doctores_form.setVisible(false);
            payments_form.setVisible(false);
            pacientes_form.setVisible(false);
            citas_form.setVisible(false);
            dashboard_form.setVisible(false);
            configuracion_form_profile.setVisible(true);
        } else if (actionEvent.getSource() == pagos_btn) {
            doctores_form.setVisible(false);
            payments_form.setVisible(true);
            pacientes_form.setVisible(false);
            citas_form.setVisible(false);
            dashboard_form.setVisible(false);
            configuracion_form_profile.setVisible(false);
            PaymentsDisplayData();
        }
    }


    ObservableList<PacientesData> pacientesDataList;
    public  void actionButtonsPaciente(){
        connection = connectionDB();
        pacientesDataList = getDataPacientes();
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
                    editButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);\n"
                            + "    -fx-cursor: hand;\n"
                            + "    -fx-text-fill: #fff;\n"
                            + "    -fx-font-size: 14px;\n"
                            + "    -fx-font-family: Arial;");

                    removeButton.setStyle("-fx-background-color: linear-gradient(to bottom right, #188ba7, #306090);\n"
                            + "    -fx-cursor: hand;\n"
                            + "    -fx-text-fill: #fff;\n"
                            + "    -fx-font-size: 14px;\n"
                            + "    -fx-font-family: Arial;");
                    editButton.setOnAction(( event) -> {
                        try {
                            PacientesData pData = pacientes_tableView.getSelectionModel().getSelectedItem();
                            int num = pacientes_tableView.getSelectionModel().getSelectedIndex();
                            if ((num - 1) < -1) {
                                alertMessage.errorMessage("Por favor selecciona un registro.");
                                return;
                            }
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Paciente/EditarPaciente.fxml"));
                            Data.temp_Code = pData.getId();
                            Data.temp_CodeUUID = pData.getCodigo();
                            Data.temp_Name = pData.getNombre();
                            Data.temp_Genero = pData.getGenero();
                            Data.temp_Telefono = pData.getTelefono();
                            Data.temp_Diagnostico = pData.getDiagnostico();
                            Data.temp_Descripcion = pData.getDescripcion();
                            Data.temp_Tratamiento = pData.getTratamiento();
                            Data.temp_Direccion = pData.getDireccion();
                            Data.temp_Fecha = pData.getFecha();

                            createStage(loader,620,417);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    removeButton.setOnAction(( event) -> {
                        PacientesData pData = pacientes_tableView.getSelectionModel().getSelectedItem();
                        int num = pacientes_tableView.getSelectionModel().getSelectedIndex();
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
        pacientes_col_accion.setCellFactory(cellFactory);
        pacientes_tableView.setItems(pacientesDataList);
    }

    public void profileInsertImage(){
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image", "*jpg", "*jpeg", "*png"));
        File file = open.showOpenDialog(configuracion_btn_importar.getScene().getWindow());
        if (file != null) {
            Data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 140, 125, false, true);
            profile_circle.setFill(new ImagePattern(image));
        }
    }

    public ObservableList<CitasData> getDataCitas(){
        ObservableList<CitasData> dataCitas = FXCollections.observableArrayList();
        String citasQuery = "SELECT * FROM citas WHERE fecha_eliminacion IS NULL;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(citasQuery);
            resultSet = preparedStatement.executeQuery();
            CitasData citasData;
            while (resultSet.next()) {
                citasData = new CitasData(resultSet.getString("cita_id"),resultSet.getString("name"),resultSet.getString("genero"),resultSet.getString("telefono"),resultSet.getString("descripcion"),resultSet.getString("fecha_creacion"),resultSet.getDate("fecha_modificacion"),resultSet.getDate("fecha_eliminacion"),resultSet.getString("estatus"),resultSet.getString("diagnostico"),resultSet.getString("tratamiento"),resultSet.getString("direccion"),resultSet.getDate("calendario"));
                dataCitas.add(citasData);
            }
        }catch (Exception e) {e.printStackTrace();}
        return dataCitas;
    }

    public ObservableList<CitasData> citasData;
    public void displayDataCitas(){
        citasData = getDataCitas();
        citas_col_id.setCellValueFactory(new PropertyValueFactory<>("citaID"));
        citas_col_nombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        citas_col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        citas_col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        citas_col_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        citas_col_fecha.setCellValueFactory(new PropertyValueFactory<>("calendario"));
        citas_col_fechaModificacion.setCellValueFactory(new PropertyValueFactory<>("fechaModificacion"));
        citas_col_estatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        citas_tableView.setItems(citasData);
    }

    public ObservableList<DataDoctor> getDataDoctores(){
        ObservableList<DataDoctor> listData = FXCollections.observableArrayList();
        String consulta = "SELECT * FROM doctores WHERE fecha_eliminacion IS NULL;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            DataDoctor dataD;
            while (resultSet.next()){
                // Integer idDoctor, String uuid, String doctorName, String genero, String numero, String email, String especializacion, String direccion, String estatus
                dataD = new DataDoctor(resultSet.getInt("id"), resultSet.getString("doctor_id"), resultSet.getString("nombre_completo"), resultSet.getString("sexo"), resultSet.getString("telefono"),resultSet.getString("email"),resultSet.getString("especializacion"),resultSet.getString("direccion"),resultSet.getString("status"), resultSet.getString("imagen"));
                listData.add(dataD);
            }
        }catch (Exception e) {e.printStackTrace();}
        return listData;
    }


    public ObservableList<PacientesData> getDataPacientes(){
        ObservableList<PacientesData> listData = FXCollections.observableArrayList();
        String consulta = "SELECT * FROM pacientes WHERE fecha_eliminacion IS NULL;";
        try {
            connection = connectionDB();
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            PacientesData pData;
            while (resultSet.next()){
                //Integer id,String codigo,String nombre,String genero, String telefono, String descripcion, String diagnostico, String tratamiento, Date fecha
                pData = new PacientesData(resultSet.getInt("id"),resultSet.getString("codigo"),resultSet.getString("nombre"), resultSet.getString("genero"),resultSet.getString("numero"),resultSet.getString("descripcion"),resultSet.getString("diagnostico"),resultSet.getString("tratamiento"),resultSet.getDate("fecha"),resultSet.getString("direccion"));
                listData.add(pData);
            }
            return listData;
        }catch (Exception e) {e.printStackTrace();}
        return listData;
    }

    public ObservableList<PacientesData> pacientesListData;
    public void displayDataPacientes(){
        pacientesListData = getDataPacientes();
        pacientes_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        pacientes_col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        pacientes_col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        pacientes_col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        pacientes_col_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        pacientes_col_diagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        pacientes_col_tratamiento.setCellValueFactory(new PropertyValueFactory<>("tratamiento"));
        pacientes_col_fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        pacientes_tableView.setItems(pacientesListData);
    }

    public ObservableList<DataDoctor> doctorListData;
    public void doctorDisplayData(){
        doctorListData = getDataDoctores();
        doctores_col_doctorID.setCellValueFactory(new PropertyValueFactory<>("idDoctor"));
        doctores_col_nombre.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        doctores_col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        doctores_col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        doctores_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        doctores_col_especializacion.setCellValueFactory(new PropertyValueFactory<>("especializacion"));
        doctores_col_direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        doctores_col_estatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        doctores_tableView.setItems(doctorListData);
    }

    public void displayAdminData(){
        nav_adminName.setText(Data.admin_userName);
        top_adminName.setText(Data.admin_userName);
        nav_adminID.setText(String.valueOf(Data.admin_id));
        doctorDisplayData();
    }
}