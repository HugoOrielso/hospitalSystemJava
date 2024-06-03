package com.hospitalsystem.Controllers.Utils;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;

public class Database {
    public static Connection connectionDB(){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_system","root", "");
            return connection;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
