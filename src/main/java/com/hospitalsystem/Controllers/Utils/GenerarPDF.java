package com.hospitalsystem.Controllers.Utils;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.util.Date;

public class GenerarPDF {

    private static AlertMessage alertMessage = new AlertMessage();

    public static void generatePDF(String doctor, String paciente, String diagnostico, String fecha, String telefono, String uuid) {
        // Carpeta de salida
        String outputFolder = "src/main/java/com/hospitalsystem/Directorio/PDFS/";
        String pdfPath = outputFolder + "cita-" + uuid + ".pdf";

        // Ruta del logo (ajustada para desarrollo local)
        String imagePath = "./src/main/resources/Imagenes/logo.jpg";

        try {
            // Crear carpeta si no existe
            File folder = new File(outputFolder);
            if (!folder.exists()) folder.mkdirs();

            // Verificar imagen antes de intentar usarla
            File logoFile = new File(imagePath);
            if (!logoFile.exists()) {
                alertMessage.errorMessage("Logo no encontrado en: " + logoFile.getAbsolutePath());
                return;
            }

            // Crear el PDF
            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.LETTER);

            // Fecha +1 día
            Date fechaHoy = new Date(new Date().getTime() + 3600 * 24 * 1000);

            // Logo
            ImageData logoHospital = ImageDataFactory.create(imagePath);
            Image imagen = new Image(logoHospital)
                    .setWidth(50)
                    .setHeight(50)
                    .setFixedPosition(1, PageSize.LETTER.getWidth() - 90, PageSize.LETTER.getHeight() - 70);

            // Título
            document.add(new Paragraph("Nueva cita")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setBold()
                    .setFontSize(15));

            // Separador
            LineSeparator ls = new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine());
            document.add(new Paragraph("\n\n"));
            document.add(ls);

            // Cuerpo
            document.add(new Paragraph("Mediante el presente documento el doctor " + doctor
                    + " hace constancia que el paciente " + paciente + " con diagnóstico de " + diagnostico
                    + " tiene cita programada para el día " + fecha));
            document.add(new Paragraph("\n\n"));

            // Datos específicos
            document.add(new Paragraph("Datos específicos").setBold().setFontSize(15).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("Paciente: ").add(paciente));
            document.add(new Paragraph("Diagnóstico: " + diagnostico));
            document.add(new Paragraph("Teléfono de contacto: " + telefono));
            document.add(imagen);
            document.add(ls);

            // Observaciones
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Observaciones: ").setBold().setFontSize(15));
            document.add(new Paragraph("Tenga en cuenta estar 20 minutos antes, traer el documento de identificación y exámenes si son requeridos."));

            // Footer (firma y fecha)
            document.add(new Paragraph(fechaHoy.toString())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFixedPosition(pdf.getLastPage().getPageSize().getWidth() - 250, 50, 200));
            document.add(new Paragraph("Firma doctor")
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFixedPosition(50, 50, 200));

            document.close();

            System.out.println("✅ PDF generado correctamente en: " + new File(pdfPath).getAbsolutePath());
            alertMessage.confirmationMessage("Documento generado correctamente, consulta la carpeta PDFS.");

        } catch (Exception e) {
            e.printStackTrace();
            alertMessage.errorMessage("Error generando PDF: " + e.getMessage());
        }
    }
}
