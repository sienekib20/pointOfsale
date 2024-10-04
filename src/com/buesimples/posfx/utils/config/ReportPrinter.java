package com.buesimples.posfx.utils.config;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleGraphics2DExporterOutput;
import javafx.print.PrinterJob;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import java.awt.print.Printable;
import java.awt.Graphics;
import java.awt.print.PrinterException;
import java.awt.print.PageFormat;
import java.util.HashMap;
import java.util.Map;

public class ReportPrinter {

    private static ReportPrinter instance = null;

    public static ReportPrinter getInstance() {
        if (instance == null) {
            instance = new ReportPrinter();
        }
        return instance;
    }

    public void printReport(Stage owner) {
        try {
            // Caminho para o arquivo JRXML
            String jrxmlFilePath = "path/to/your/report.jrxml";

            // Parâmetros do relatório, se houver
            Map<String, Object> parameters = new HashMap<>();

            // Gerar o relatório
            JasperPrint jasperPrint = generateReport(jrxmlFilePath, parameters);

            // Imprimir o relatório
            printJasperReport(jasperPrint, owner);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public JasperPrint generateReport(String jrxmlFilePath, Map<String, Object> parameters) throws JRException {
        // Compilar o arquivo JRXML
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFilePath);
        // Preencher o relatório com dados
        return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
    }

    public void printJasperReport(JasperPrint jasperPrint, Stage owner) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            System.out.println("Could not create a printer job.");
            return;
        }

        // Show the print dialog
        boolean proceed = job.showPrintDialog(owner);
        if (proceed) {
            // Cria um Canvas e um GraphicsContext
            Canvas canvas = new Canvas(600, 800); // Defina o tamanho do canvas de acordo com seu relatório
            GraphicsContext gc = canvas.getGraphicsContext2D();

            try {
                // Renderizar o JasperPrint no Canvas
                JRGraphics2DExporter exporter = new JRGraphics2DExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                // Utilize um ImageWriter para renderizar no GraphicsContext
                WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Limpa o canvas

                exporter.setExporterOutput(new SimpleGraphics2DExporterOutput());

                // Exportar o relatório
                exporter.exportReport();

                // Agora, você pode imprimir o canvas
                boolean printed = job.printPage(canvas);
                if (printed) {
                    job.endJob();
                } else {
                    System.out.println("Printing failed.");
                }
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }

}
