package com.hospitalsystem.Controllers.Utils;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.FileNotFoundException;
import java.util.Date;

public class GenerarPDF {

    private static AlertMessage alertMessage = new AlertMessage();

    public static void generatePDF(String doctor, String paciente, String diagnostico, String fecha, String telefono, String uuid) {
        String pdfPath = "./src/main/java/com/hospitalsystem/Directorio/PDFS/cita-" + uuid  + ".pdf";
        String imagePath = "./src/main/resources/Imagenes/logo.jpg";

        try {

            PdfWriter writer = new PdfWriter(pdfPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.LETTER); // Tamaño carta
            PdfPage page = pdf.addNewPage(PageSize.LETTER);
            Date fechaHoy = new Date(new Date().getTime() + 3600 * 24 * 1000 );
            int widthImagen = 50;
            int heightImagen = 50;

            ImageData logoHospital = ImageDataFactory.create(imagePath);
            Image imagen = new Image(logoHospital);
            document.add(new Paragraph("Nueva cita").setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER).setBold().setFontSize(15));

            imagen.setWidth(widthImagen);
            imagen.setHeight(heightImagen);

            float x = PageSize.LETTER.getWidth() - 90;
            float y = PageSize.LETTER.getHeight() - 70;

            imagen.setFixedPosition(1, x, y);
            Paragraph diagnosticoParrafo = new Paragraph("Diagnóstico: " + diagnostico + ".");
            Paragraph telefonoParrafo = new Paragraph("Telefono de contacto: " + telefono + ".");
            Paragraph pacienteParrafo = new Paragraph("Paciente: ").add(paciente);

            LineSeparator ls = new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine());

            Paragraph paragraph3 = new Paragraph("Mediante el presente documento el doctor " + doctor
                    + " hace constancia que el paciente " + paciente + " con diagnóstico de " + diagnostico
                    + " tiene cita programada para el día " + fecha)
                    .setTextAlignment(TextAlignment.LEFT);

            document.add(new Paragraph("\n\n"));
            document.add(ls);
            document.add(paragraph3);
            document.add(new Paragraph("\n\n"));

            document.add(new Paragraph("Datos específicos").setBold().setFontSize(15).setTextAlignment(TextAlignment.CENTER));
            document.add(pacienteParrafo);
            document.add(diagnosticoParrafo);
            document.add(telefonoParrafo);
            document.add(imagen);
            document.add(ls);

            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Observaciones: ").setBold().setFontSize(15));
            document.add(new Paragraph("Tenga en cuenta estar 20 minutos antes, traer el documento de identificación y exámenes si son requeridos."));

            PdfPage lastPage = pdf.getLastPage();
            float bottomMargin = 50;
            float rightMargin = 50;
            float textWidth = 200;
            float leftMargin = 50;

            Paragraph fechaHoyString = new Paragraph(fechaHoy.toString())
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFixedPosition(lastPage.getPageSize().getWidth() - rightMargin - textWidth, bottomMargin, textWidth);
            document.add(fechaHoyString);

            Paragraph firmaDoctor = new Paragraph("Firma doctor")
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFixedPosition(leftMargin, bottomMargin, textWidth);
            document.add(firmaDoctor);

            document.close();
            alertMessage.confirmationMessage("Documento generado correctamente, consulta tus documentos.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
