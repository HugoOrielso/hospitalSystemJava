package com.hospitalsystem.Controllers.Utils;

import com.hospitalsystem.Controllers.Paciente.PacientesData;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class GenerarExcel {


    public static void generarExcel(ObservableList<PacientesData> listaPacientes){
        String excelFilePath = "./src/main/java/com/hospitalsystem/Directorio/Excels/reporte-" + UUID.randomUUID() + ".xlsx";

        try (Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Datos de Pacientes");
            String[] headers = {"Nombre", "Género", "Teléfono", "Dirección", "Fecha creación", "Fecha modificación"};

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (PacientesData paciente : listaPacientes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(paciente.getNombre());
                row.createCell(1).setCellValue(paciente.getGenero());
                row.createCell(2).setCellValue(paciente.getTelefono());
                row.createCell(3).setCellValue(paciente.getDireccion());
                row.createCell(4).setCellValue(paciente.getFecha());
                row.createCell(5).setCellValue(paciente.getFechaModificacion());
            }

            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                workbook.write(outputStream);
            }
        }catch (IOException e){e.printStackTrace();}


    }
}
