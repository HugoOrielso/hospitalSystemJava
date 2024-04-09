module hospital.hospitalsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.httpserver;
    requires de.jensd.fx.glyphs.fontawesome;

    opens com.hospitalsystem to javafx.fxml;
    exports com.hospitalsystem;
    exports com.hospitalsystem.Controllers.Admin;
    exports com.hospitalsystem.Controllers.Doctor;
    exports com.hospitalsystem.Controllers.Paciente;
}