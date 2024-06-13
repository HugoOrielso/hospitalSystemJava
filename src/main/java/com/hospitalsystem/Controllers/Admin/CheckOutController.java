package com.hospitalsystem.Controllers.Admin;

import com.hospitalsystem.Controllers.Utils.AlertMessage;
import com.hospitalsystem.Controllers.Utils.Data;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import static com.hospitalsystem.Controllers.Utils.Database.connectionDB;

public class CheckOutController implements Initializable {
    public Label pagos_lb_id;
    public Label pagos_lb_nombre;
    public Label pagos_lb_doctor;
    public Label pagos_lb_fecha;
    public DatePicker pagos_datePicker;
    public Label pagos_lb_precioTotal;
    public Button pagar_btn;
    public ImageView pagos_imageView;
    public Label indicadorImagen;
    public Label pagos_lb_totalDias;
    public Button btn_contar;
    public DatePicker pagos_fechaEntradaPicker;
    private AlertMessage alert = new AlertMessage();
    private Image image;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayFields();
        btn_contar.setOnAction(e -> countBtn());
        pagar_btn.setOnAction(e -> paymentBtn());
    }

    public void displayFields(){
        pagos_lb_id.setText(Data.temp_pacienteId.split("-")[0]);
        pagos_lb_nombre.setText(Data.temp_Name);
        pagos_lb_doctor.setText(Data.temp_doctorNamePaciente);
        pagos_fechaEntradaPicker.setValue(LocalDate.parse(Data.temp_date));
        pagos_fechaEntradaPicker.setEditable(false);
        pagos_fechaEntradaPicker.setDisable(true);
        if (Data.path == null) {
            indicadorImagen.setVisible(true);
            indicadorImagen.setText("Sin imágen");
            return;
        }
        image = new Image("File:" + Data.temp_path, 336, 41, false, true);
        pagos_imageView.setImage(image);
        indicadorImagen.setVisible(false);
    }

    public void countBtn (){
        long countDays = 0;
        if (pagos_datePicker.getValue() != null && pagos_fechaEntradaPicker.getValue() != null){
            countDays = ChronoUnit.DAYS.between(pagos_fechaEntradaPicker.getValue(),pagos_datePicker.getValue());

            // precio por día en pesos colombianos
            double price = 100000;
            double totalPrice = (price *  countDays);
            pagos_lb_precioTotal.setText(Double.toString(totalPrice));
            pagos_lb_totalDias.setText(String.valueOf(countDays));
            return;
        }
        alert.errorMessage("Por favor selecciona una fecha ");
    }

    public void paymentBtn(){
        if (       pagos_fechaEntradaPicker.getValue() == null
                || pagos_datePicker.getValue() == null
                || pagos_lb_id.getText().isEmpty()
                || pagos_lb_nombre.getText().isEmpty()
                || pagos_lb_doctor.getText().isEmpty()
                || pagos_lb_precioTotal.getText().isEmpty()
                || pagos_lb_totalDias.getText().isEmpty()
        ){
            alert.errorMessage("Por favor completa todos los campos");
            return;
        }
        if (Integer.parseInt(pagos_lb_totalDias.getText()) < 0 ||  Double.parseDouble(pagos_lb_precioTotal.getText()) < 0){
            alert.errorMessage("Por favor selecciona un formato de días o precios correcto.");
            return;
        }
        String consulta = "INSERT INTO pagos (paciente_id,doctor_id,total_dias,precio_total,fecha_entrada,fecha_verificacion,fecha_pago) VALUES (?,?,?,?,?,?,?)";
        Date fechaPago = new Date(new java.util.Date().getTime());

        try {
            connection = connectionDB();
            if (alert.confirmationMessage("Estás seguro de realizar el cobro")){
                preparedStatement = connection.prepareStatement(consulta);
                preparedStatement.setString(1, Data.temp_pacienteId);
                preparedStatement.setString(2, Data.temp_doctorIdPaciente);
                preparedStatement.setInt(3,Integer.parseInt(pagos_lb_totalDias.getText()));
                preparedStatement.setDouble(4,Double.parseDouble(String.valueOf(pagos_lb_precioTotal.getText())));
                preparedStatement.setDate(5, Date.valueOf(pagos_fechaEntradaPicker.getValue()));
                preparedStatement.setDate(6, Date.valueOf(pagos_datePicker.getValue()));
                preparedStatement.setDate(7, fechaPago);
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    String updatePay = "UPDATE pacientes SET estatus_pago = ? WHERE codigo = ? && doctor = ?;";
                    preparedStatement = connection.prepareStatement(updatePay);
                    preparedStatement.setString(1, Data.pagosEstatus[3]);
                    preparedStatement.setString(2, Data.temp_pacienteId);
                    preparedStatement.setString(3, Data.temp_doctorIdPaciente);
                    int rowsAffected = preparedStatement.executeUpdate();

                    alert.confirmationMessage("Pago exitoso");
                    return;
                }
            }

        }catch (Exception e){e.printStackTrace();}
    }
}
