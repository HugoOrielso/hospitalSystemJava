module hospital.hospitalsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.httpserver;
    requires de.jensd.fx.glyphs.fontawesome;
    requires static lombok;

    opens com.hospitalsystem to javafx.fxml;
    exports com.hospitalsystem;
    exports com.hospitalsystem.Controllers.Admin;
    exports com.hospitalsystem.Controllers.Doctor;
    exports com.hospitalsystem.Controllers.Paciente;
    exports com.hospitalsystem.Controllers.Citas;
    exports com.hospitalsystem.Controllers.Utils;
    exports com.hospitalsystem.Controllers.Grabar;
    opens com.hospitalsystem.Controllers.Utils to javafx.fxml;
}