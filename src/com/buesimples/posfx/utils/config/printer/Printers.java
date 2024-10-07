package com.buesimples.posfx.utils.config.printer;

import com.buesimples.posfx.controllers.IndexController;
import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.models.ModoPagamento;
import com.buesimples.posfx.models.Ticket;
import com.buesimples.posfx.models.TicketModel;
import com.buesimples.posfx.models.TicketPayment;
import com.buesimples.posfx.models.TicketProduct;
import com.buesimples.posfx.models.TicketProductModel;
import com.buesimples.posfx.utils.constants.Constants;
import com.buesimples.posfx.utils.json.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.buesimples.posfx.utils.config.Config;
import com.jfoenix.controls.JFXTextArea;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.jfree.util.UnitType;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Printers {

    private static Printers instance = null;
    private static String configFile;
    private static Gson gson;

    private static class PrinterConfig {

        String selectedPrinter;
    }

    public Printers() {
        instance = this;
    }

    public static Printers getInstance() {
        if (instance == null) {
            new PrinterConfig();
        }
        initConfigurations();
        return instance;
    }

    public static Printers initConfigurations() {
        configFile = Constants.getJsonFile("config");
        gson = new GsonBuilder().setPrettyPrinting().create();

        return new Printers();
    }

    public static PrintService[] loadPrint() {
        return PrintServiceLookup.lookupPrintServices(null, null);
    }

    public static String loadPrinterConfig() {
        try (Reader reader = new FileReader(configFile)) {
            PrinterConfig config = gson.fromJson(reader, PrinterConfig.class);
            return config.selectedPrinter;
        } catch (IOException e) {
            System.out.println("Erro ao carregar configuração: " + e.getMessage());
        }
        return null;
    }

    public int savePrinterConfig(String printerName) {
        /*
         * PrinterConfig config = new PrinterConfig();
         * config.selectedPrinter = printerName;
         */

        return Config.setConfig("impressora", printerName);
    }

    public void printDocument(String selectedPrinter) {
        ObservableSet<Printer> printers = Printer.getAllPrinters();

        for (Printer printer : printers) {
            System.out.println("GETTING PRINNTERS: " + printer.getName());
            if (selectedPrinter.equals(printer.getName())) {
                // print();
                Stage stage = (IndexController.getInstance().getMainStage());
                // pageSetup(stage);
            }
        }
    }

    public void getDefaultPrinter() {
        Printer defaultprinter = Printer.getDefaultPrinter();

        if (defaultprinter != null) {
            String name = defaultprinter.getName();
            System.out.println("Default printer name: " + name);
        } else {
            System.out.println("No printers installed.");
        }
    }

    public void pageSetup(Stage owner, VBox node) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            return;
        }

        // boolean proceder = job.showPageSetupDialog(owner);
        boolean proceder = job.showPrintDialog(owner);

        if (proceder) {
            print(job, node);
        }

    }

    private void print(PrinterJob job, VBox node) {
        // PrinterJob job = PrinterJob.createPrinterJob();
        // Define the Job Status Message
        System.out.println("Creating a printer job...");

        // Create a printer job for the default printer
        // PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            // Show the printer job status
            System.out.println(job.jobStatusProperty().asString());
            double widthMm = 80; // 80mm de largura
            double heightMm = 200; // 200mm de altura

            // Conversão de milímetros para pontos
            double widthPoints = (widthMm / 25.4) * 72;
            double HeightPoints = (heightMm / 25.4) * 72;
            // JFXTextArea a = new JFXTextArea("Testando");
            // Criar um layout de página customizado (80mm de largura por 200mm de altura)
            // PageLayout pageLayout = job.getPrinter().createPageLayout(new Paper("Ticket",
            // widthPoints, HeightPoints, UnitType.RELATIVE),
            // PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

            PageLayout pageLayout = job.getPrinter().createPageLayout(Paper.LEGAL,
                    PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);

            // Print the node
            boolean printed = job.printPage(pageLayout, node);

            if (printed) {
                // End the printer job
                job.endJob();
            } else {
                // Write Error Message
                // System.out.println.textProperty().unbind();
                System.out.println("Printing failed.");
            }
        } else {
            // Write Error Message
            System.out.println("Could not create a printer job.");
        }
    }

    public void gerar2PDF(int IdDocumento, int nItem, String agtCode) {
        List<TicketModel> ticketList = new ArrayList<>();
        TicketModel tick = new TicketModel();

        List<Map<String, Object>> config = DatabaseHelpers.build().select(
                "configuracao",
                "GET");

        for (Map<String, Object> empresa : config) {
            switch (empresa.get("campo").toString()) {
                case "titulo":
                    tick.setNomeEmpresa(empresa.get("valor").toString());
                    break;
                case "endereco":
                    tick.setEnderecoEmpresa(empresa.get("valor").toString());
                    break;
                case "telefone":
                    tick.setTelefoneEmpresa(empresa.get("valor").toString());
                    break;
                case "nif":
                    tick.setNifEmpresa(empresa.get("valor").toString());
                    break;
            }
        }


        System.out.println("TICKET: " + tick.getNomeEmpresa());
        System.out.println("TICKET: " + tick.getEnderecoEmpresa());
        System.out.println("TICKET: " + tick.getTelefoneEmpresa());
        System.out.println("TICKET: " + tick.getNifEmpresa());

        

        List<TicketProductModel> tickProducts = new ArrayList<TicketProductModel>();
        TicketProductModel tickModel = new TicketProductModel();

        List<Map<String, Object>> viewDocumento = DatabaseHelpers.build().querySelect(
                "viewDocumento",
                "POST", ""
                        + "{ "
                        + "\"idDocumento\": " + IdDocumento + " "
                        + "}");
        for (Map<String, Object> vd : viewDocumento) {
            tickModel.setNomeProduto(
                    Objects.requireNonNull(vd.get("descricaoAd").toString(), ""));

            tickModel.setQtdProduto(
                    (int) Double.parseDouble(Objects.requireNonNull(
                            vd.get("qtdAd").toString(), "0")));
            tickModel.setValorTotalProduto(
                    Double.parseDouble(Objects.requireNonNull(
                            vd.get("totalAd").toString(), "0.0")));

            // Add in TickProductsList
            tickProducts.add(new TicketProductModel(
                    tickModel.getQtdProduto(),
                    tickModel.getNomeProduto(),
                    tickModel.getValorTotalProduto()));

        }
        tick.setnItems(nItem);

        List<Map<String, Object>> viewModoPagamento = DatabaseHelpers.build().querySelect(
                "viewartigomodopagamentodocumento",
                "POST", ""
                        + "{ "
                        + "\"idDocumento\": " + IdDocumento + " "
                        + "}");
        Double valorTotalEntregue = 0.0;
        Double valorTotalAPagar = 0.0;
        Double valorTotalTroco = 0.0;

        for (Map<String, Object> vd : viewModoPagamento) {
            // String nomeTd = JsonUtils.nonNullElse(vd, "nomeTd", 0);
            String nomeE = JsonUtils.nonNullElse(vd, "nomeE", 0);
            String moradaE = JsonUtils.nonNullElse(vd, "moradaE", 0);

            String nifE = JsonUtils.nonNullElse(vd, "nifE", 0);
            String dataDocumento = JsonUtils.nonNullElse(vd, "dataDocumento", 0);
            String horaDocumento = "";
            if (dataDocumento.contains("T")) {
                String[] partes = dataDocumento.split("T");
                dataDocumento = partes[0];
                if (partes.length > 1) {
                    horaDocumento = partes[1];
                    if (horaDocumento.contains("+")) {
                        horaDocumento = horaDocumento.split("\\+")[0];
                    } else if (horaDocumento.contains(" ")) {
                        horaDocumento = horaDocumento.split(" ")[0];
                    }
                }
            }

            String codigoDocumento = JsonUtils.nonNullElse(vd, "codigoDocumento", 0);
            String nomeU = JsonUtils.nonNullElse(vd, "nomeU", 0);
            // String nomePp = JsonUtils.nonNullElse(vd, "nomePp", 0);
            String entregue = JsonUtils.nonNullElse(vd, "entregue", 1);
            String total = JsonUtils.nonNullElse(vd, "total", 1);
            String troco = JsonUtils.nonNullElse(vd, "troco", 1);

            // tick.setCodigoDocumento(nomeTd);
            tick.setCodigoDocumento(codigoDocumento);
            tick.setNomeCliente(nomeE);
            tick.setMoradaCliente(moradaE);
            tick.setTelefoneCliente("");
            tick.setnContribuinte(nifE);
            // tick.setNifEmpresaAtual(nifE);
            tick.setDataDoc(dataDocumento);
            tick.setHoraDoc(horaDocumento);
            tick.setUtilizador(nomeU);

            valorTotalEntregue += Double.parseDouble(entregue);
            valorTotalAPagar += Double.parseDouble(total);
            valorTotalTroco += Double.parseDouble(troco);

            tick.setValorTroco(valorTotalTroco);
            tick.setValorTotal(valorTotalAPagar);
        }

        ticketList.add(tick);
        tick.setNumeroProcesso(agtCode);

        Map<String, Object> parameters = new HashMap<>();
        // parameters.put("valorTotal", factura.getValor());
        parameters.put("nomeEmpresa", tick.getNomeEmpresa());
        parameters.put("enderecoEmpresa", tick.getEnderecoEmpresa());
        parameters.put("nifEmpresa", tick.getNifEmpresa());
        parameters.put("telefoneEmpresa", tick.getTelefoneEmpresa());

        parameters.put("codigoDocumento", tick.getCodigoDocumento());
        parameters.put("dataDoc", tick.getDataDoc());
        parameters.put("horaDoc", tick.getHoraDoc());

        parameters.put("nItems", tick.getnItems());
        parameters.put("valorTotal", tick.getValorTotal());
        parameters.put("valorTroco", tick.getValorTroco());

        parameters.put("nomeCliente", tick.getNomeCliente());
        parameters.put("moradaCliente", tick.getMoradaCliente());
        parameters.put("nContribuinte", tick.getnContribuinte());
        parameters.put("telefoneCliente", tick.getTelefoneCliente());

        parameters.put("numeroProcesso", tick.getNumeroProcesso());
        parameters.put("utilizador", tick.getUtilizador());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ticketList);
        JRBeanCollectionDataSource itemAdicionados = new JRBeanCollectionDataSource(tickProducts);

        parameters.put("TABELA_DOCUMENTO", itemAdicionados);

        List<Map<String, Object>> modoPagamento = DatabaseHelpers.build().querySelectData(
                "POST",
                "{\r\n" + //
                        "    \"tabela\": \"modopagamentodocumento\",\r\n" + //
                        "    \"join\": [\r\n" + //
                        "        {\r\n" + //
                        "            \"tipo\": \"inner\",\r\n" + //
                        "            \"tabela\": \"modopagamento\",\r\n" + //
                        "            \"juncao\": \"modopagamento.idModoPagamento = modopagamentodocumento.idModoPagamento\"\r\n"
                        + //
                        "        }\r\n" + //
                        "    ],\r\n" + //
                        "    \"coluna\": [\r\n" + //
                        "        {\r\n" + //
                        "            \"modoPagamento\": \"modopagamento.nome\",\r\n" + //
                        "            \"valor\": \"modopagamentodocumento.valor\"\r\n" + //
                        "        }\r\n" + //
                        "    ],\r\n" + //
                        "    \"where\": [\r\n" + //
                        "        {\r\n" + //
                        "            \"tipo\": \"AND\",\r\n" + //
                        "            \"parametro\": " + IdDocumento + ",\r\n" + //
                        "            \"valor\": \"356\"\r\n" + //
                        "        }\r\n" + //
                        "    ]\r\n" + //
                        "}");

        List<TicketPayment> tickPayment = new ArrayList<TicketPayment>();
        TicketPayment tickP = new TicketPayment();

        for (Map<String, Object> modo : modoPagamento) {
            String modoPayment = modo.get("modoPagamento") != null ? modo.get("modoPagamento").toString() : "";
            String valor = modo.get("valor") != null ? modo.get("valor").toString() : "0.0";
            Double valorPayment = Double.parseDouble(valor);

            tickP.setModoPagamento(modoPayment);
            tickP.setValorPago(valorPayment);

            tickPayment.add(tickP);
        }
        JRBeanCollectionDataSource tickModoPayment = new JRBeanCollectionDataSource(tickPayment);

        parameters.put("TABELA_MODO_PAGAMENTO", tickModoPayment);

        //

        // Gere o relatório

        InputStream resourceStream = getClass().getResourceAsStream("/resources/reports/Modelo_Ticket_Descontao.jrxml");
        try {
            JasperReport jr = JasperCompileManager.compileReport(resourceStream);

            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, dataSource);

            String userHome = System.getenv("USERPROFILE");
            String reportDirPath = userHome + "\\documents\\buesimples\\reports";

            // Verificar se o diretório existe, caso contrário, criar
            File reportDir = new File(reportDirPath);
            if (!reportDir.exists()) {
                if (reportDir.mkdirs()) {
                    System.out.println("Diretório criado: " + reportDirPath);
                } else {
                    System.out.println("Erro ao criar diretório: " + reportDirPath);
                    // return;
                }
            }

            // Obter data e hora atual formatada
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            String timestamp = now.format(formatter);

            // Gerar o nome do arquivo com o timestamp
            String reportPath = reportDirPath + "\\" + timestamp + "_create_ticket.pdf";
            JasperExportManager.exportReportToPdfFile(jp, reportPath);

            // Gerar o relatório PDF
            printPDF(reportPath);

        } catch (JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void gerarPDF(int IdDocumento, int nItem, String agtCode) {
        try {

            List<Ticket> ticketList = new ArrayList<>();
            Ticket tick = new Ticket();

            List<Map<String, Object>> config = DatabaseHelpers.build().select(
                    "configuracao",
                    "GET");
            // String logo, nomeEmpresa, telefoneDoc, emailDoc, nifDoc, websiteDoc;
            for (Map<String, Object> empresa : config) {
                switch (empresa.get("campo").toString()) {
                    case "titulo":
                        tick.setNomeEmpresa(empresa.get("valor").toString());
                        break;
                    case "email":
                        tick.setEmail(empresa.get("valor").toString());
                        break;
                    case "cidade":
                        tick.setEnderecoEmpresaAtual(empresa.get("valor").toString());
                        break;
                    case "telefone":
                        tick.setTelefone(empresa.get("valor").toString());
                        break;
                    case "nif":
                        tick.setNifEmpresaAtual(empresa.get("valor").toString());
                        break;

                }
            }

            // String nomeDocumento = "Factura Recibo";
            String docPages = "ORIGINAL";
            tick.setDocPages(docPages);

            List<TicketProduct> tickProducts = new ArrayList<TicketProduct>();

            List<Map<String, Object>> viewDocumento = DatabaseHelpers.build().querySelect(
                    "viewDocumento",
                    "POST", ""
                            + "{ "
                            + "\"idDocumento\": " + IdDocumento + " "
                            + "}");
            for (Map<String, Object> vd : viewDocumento) {
                tick.setNomeProduto(
                        Objects.requireNonNull(vd.get("descricaoAd").toString(), ""));
                tick.setPrecoUnitProduto(
                        Double.parseDouble(Objects.requireNonNull(
                                vd.get("precoAd").toString(), "0.0")));
                tick.setQtdProduto(
                        (int) Double.parseDouble(Objects.requireNonNull(
                                vd.get("qtdAd").toString(), "0")));
                tick.setValorTotalProduto(
                        Double.parseDouble(Objects.requireNonNull(
                                vd.get("totalAd").toString(), "0.0")));

                // Add in TickProductsList
                tickProducts.add(new TicketProduct(
                        tick.getQtdProduto(),
                        tick.getNomeProduto(),
                        tick.getPrecoUnitProduto(),
                        tick.getValorTotalProduto()));

            }
            tick.setnItens(nItem);

            List<Map<String, Object>> viewModoPagamento = DatabaseHelpers.build().querySelect(
                    "viewartigomodopagamentodocumento",
                    "POST", ""
                            + "{ "
                            + "\"idDocumento\": " + IdDocumento + " "
                            + "}");
            Double valorTotalEntregue = 0.0;
            Double valorTotalAPagar = 0.0;
            Double valorTotalTroco = 0.0;

            for (Map<String, Object> vd : viewModoPagamento) {
                String nomeTd = JsonUtils.nonNullElse(vd, "nomeTd", 0);
                String nomeE = JsonUtils.nonNullElse(vd, "nomeE", 0);
                String nifE = JsonUtils.nonNullElse(vd, "nifE", 0);
                String dataDocumento = JsonUtils.nonNullElse(vd, "dataDocumento", 0);
                dataDocumento = dataDocumento.split("T")[0];
                String codigoDocumento = JsonUtils.nonNullElse(vd, "codigoDocumento", 0);
                String nomeU = JsonUtils.nonNullElse(vd, "nomeU", 0);
                String nomePp = JsonUtils.nonNullElse(vd, "nomePp", 0);
                String entregue = JsonUtils.nonNullElse(vd, "entregue", 1);
                String total = JsonUtils.nonNullElse(vd, "total", 1);
                String troco = JsonUtils.nonNullElse(vd, "troco", 1);

                tick.setNomeDocumento(nomeTd);
                tick.setNomeEntidade(nomeE);
                tick.setNomeEmpresaAtual(nomeE);
                tick.setNifEntidade(nifE);
                // tick.setNifEmpresaAtual(nifE);
                tick.setDataDoc(dataDocumento);
                tick.setCodigoDocumento(codigoDocumento);
                tick.setUtilizador(nomeU);

                tick.setTipoPagamento(nomePp);

                valorTotalEntregue += Double.parseDouble(entregue);
                valorTotalAPagar += Double.parseDouble(total);
                valorTotalTroco += Double.parseDouble(troco);

                tick.setValorRecebido(valorTotalEntregue);
                tick.setValorPagar(valorTotalAPagar);
                tick.setValorTroco(valorTotalTroco);
                tick.setValorTotal(valorTotalAPagar);
            }
            ticketList.add(tick);

            Map<String, Object> parameters = new HashMap<>();
            // parameters.put("valorTotal", factura.getValor());
            parameters.put("nomeEmpresa", tick.getNomeEmpresa());
            parameters.put("nomeDocumento", tick.getNomeDocumento());
            parameters.put("docPages", tick.getDocPages());

            parameters.put("codigoDocumento", tick.getCodigoDocumento());
            parameters.put("dataDoc", tick.getDataDoc());

            parameters.put("nomeEntidade", tick.getNomeEntidade());
            parameters.put("nifEntidade", tick.getNifEntidade());

            parameters.put("telefone", tick.getTelefone());
            parameters.put("email", tick.getEmail());
            parameters.put("nifEmpresaAtual", tick.getNifEmpresaAtual());
            parameters.put("enderecoEmpresaAtual", tick.getEnderecoEmpresaAtual());
            // parameters.put("nomeEmpresaAtual", tick.getNomeEntidade());
            parameters.put("nomeEmpresaAtual", tick.getNomeEmpresaAtual());

            parameters.put("utilizador", tick.getUtilizador());
            parameters.put("valorRecebido", tick.getValorRecebido());
            parameters.put("valorPagar", tick.getValorPagar());
            parameters.put("valorTroco", tick.getValorTroco());
            parameters.put("valorTotal", tick.getValorTotal());
            parameters.put("tipoPagamento", tick.getTipoPagamento());
            parameters.put("nItens", tick.getnItens());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ticketList);
            JRBeanCollectionDataSource itemAdicionados = new JRBeanCollectionDataSource(tickProducts);

            parameters.put("TABELA_DOCUMENTO", itemAdicionados);

            // Gere o relatório

            InputStream resourceStream = getClass().getResourceAsStream("/resources/reports/ModeloTicket.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(resourceStream);

            /*
             * int largura = jr.getPageWidth();
             * int altura = jr.getPageHeight();
             *
             * double larguraMm = (largura / 72.0) * 25.4;
             * double alturaMm = (altura / 72.0) * 25.4;
             * ticketAttributes.add(new MediaPrintableArea(0, 0, (float) larguraMm,
             * (float)
             * alturaMm, MediaPrintableArea.MM));
             */

            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, dataSource);
            // JasperViewer.viewReport(jp);
            // print(node);

            pageSetup(IndexController.getInstance().getMainStage(), this.generateReportLayout(tickProducts, tick));

            String userHome = System.getenv("USERPROFILE");
            String reportDirPath = userHome + "\\documents\\buesimples\\reports";

            // Verificar se o diretório existe, caso contrário, criar
            File reportDir = new File(reportDirPath);
            if (!reportDir.exists()) {
                if (reportDir.mkdirs()) {
                    System.out.println("Diretório criado: " + reportDirPath);
                } else {
                    System.out.println("Erro ao criar diretório: " + reportDirPath);
                    return;
                }
            }

            // Obter data e hora atual formatada
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            String timestamp = now.format(formatter);

            // Gerar o nome do arquivo com o timestamp
            String reportPath = reportDirPath + "\\" + timestamp + "_create_ticket.pdf";
            JasperExportManager.exportReportToPdfFile(jp, reportPath);

            // Gerar o relatório PDF
            printPDF(reportPath);
        } catch (JRException ex) {
            System.out.println("ERRO: " + ex);
        }
    }

    public void printPDF(String filePath) {
        try {
            PDDocument document = PDDocument.load(new File(filePath));

            // Obtém a lista de impressoras e deixa o usuário escolher uma
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

            if (printServices.length == 0) {
                System.out.println("Nenhuma impressora encontrada.");
                return;
            }

            PrintService printService = null;

            String defaultPrinterName = Config.getConfig("impressora");

            for (int i = 0; i < printServices.length; i++) {
                if (printServices[i].getName().equals(defaultPrinterName)) {
                    printService = printServices[i];
                }
            }

            if (printService != null) {
                PDFPageable pageable = new PDFPageable(document);
                DocPrintJob printJob = printService.createPrintJob();

                // Cria o conjunto de atributos de solicitação de impressão conforme necessário
                PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();

                // Ajusta o DocFlavor de acordo com o PDFPageable
                DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;

                // Usa o tipo correto de SimpleDoc
                SimpleDoc doc = new SimpleDoc(pageable, flavor, null);
                printJob.print(doc, attr);

                document.close();

            } else {
                System.out.println("Erro de Impressora. Conecte Uma");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox generateReportLayout(List<TicketProduct> ticketList, Ticket ticket) {

        VBox reportLayout = new VBox();
        reportLayout.setPrefSize(226.0, 510.0);
        reportLayout.setMinSize(226.0, 510.0);
        reportLayout.setStyle("-fx-background-color: white");

        reportLayout.getChildren().add(this.reportHeaderSection(ticket));
        reportLayout.getChildren().add(this.getHr());
        reportLayout.getChildren().add(this.typeDocSection(ticket));
        reportLayout.getChildren().add(this.getHr());
        reportLayout.getChildren().add(this.productSection(ticketList));
        reportLayout.getChildren().add(this.getHr());
        reportLayout.getChildren().add(this.detailsProductSection(ticket));
        reportLayout.getChildren().add(this.getHr());
        Label txt = new Label("*Obrigado e volte sempre!*");
        txt.setPrefSize(226.0, 12.0);
        txt.setStyle("-fx-font-size: 8");
        txt.setAlignment(Pos.CENTER);
        reportLayout.getChildren().add(txt);

        return reportLayout;
    }

    private Label getHr() {
        Label hr = new Label();
        hr.setPrefSize(220.0, 1.0);
        hr.setMinSize(220.0, 1.0);
        hr.setStyle("-fx-background-color: #000");
        return hr;
    }

    private VBox generateVbox() {
        VBox vbx = new VBox();
        vbx.setPrefWidth(226.0);
        vbx.setAlignment(Pos.CENTER);
        vbx.setPadding(new Insets(0, 5, 0, 5));
        return vbx;
    }

    private VBox reportHeaderSection(Ticket ticket) {
        // Section Header
        VBox reportHeader = this.generateVbox();

        // ImageView logoEmpresa = new ImageView(new Image(""));
        // logoEmpresa.setFitWidth(20.0);
        // logoEmpresa.setFitHeight(20.0);

        // reportHeader.getChildren().add(logoEmpresa);

        Label nomeEmpresa = new Label(ticket.getNomeEmpresa());
        nomeEmpresa.setPrefSize(216.0, 10.0);
        nomeEmpresa.setMinSize(216.0, 10.0);
        nomeEmpresa.setStyle("-fx-font-size: 8; -fx-font-weight: bold");
        nomeEmpresa.setAlignment(Pos.CENTER);

        Label tipoDocumento = new Label(ticket.getTipoPagamento());
        tipoDocumento.setPrefSize(216.0, 10.0);
        tipoDocumento.setMinSize(216.0, 10.0);
        tipoDocumento.setStyle("-fx-font-size: 7;");
        tipoDocumento.setAlignment(Pos.CENTER);

        Label docPages = new Label("ORIGINAL");
        docPages.setPrefSize(216.0, 10.0);
        docPages.setMinSize(216.0, 10.0);
        docPages.setStyle("-fx-font-size: 7;");
        docPages.setAlignment(Pos.CENTER);

        // reportHeader.getChildren().add(logoEmpresa);
        reportHeader.getChildren().add(nomeEmpresa);
        reportHeader.getChildren().add(tipoDocumento);
        reportHeader.getChildren().add(docPages);

        return reportHeader;
    }

    private VBox typeDocSection(Ticket ticket) {

        VBox vbox = this.generateVbox();

        HBox layoutHbox = new HBox();
        layoutHbox.setPrefSize(216.0, 15.0);

        Label title = new Label("Documento:");
        title.setPrefSize(108.0, 12.0);
        title.setMinSize(108.0, 12.0);
        title.setStyle("-fx-font-size: 7; ");

        Label documento = new Label(ticket.getCodigoDocumento());
        documento.setPrefSize(108.0, 12.0);
        documento.setMinSize(108.0, 12.0);
        documento.setStyle("-fx-font-size: 7; ");
        documento.setAlignment(Pos.CENTER_RIGHT);

        layoutHbox.getChildren().add(title);
        layoutHbox.getChildren().add(documento);

        HBox layoutHbox1 = new HBox();
        layoutHbox1.setPrefSize(216.0, 15.0);

        Label title1 = new Label("Data de Emissão:");
        title1.setPrefSize(108.0, 12.0);
        title1.setMinSize(108.0, 12.0);
        title1.setStyle("-fx-font-size: 7; ");

        Label documento1 = new Label(ticket.getDataDoc());
        documento1.setPrefSize(108.0, 12.0);
        documento1.setMinSize(108.0, 12.0);
        documento1.setStyle("-fx-font-size: 7; ");
        documento1.setAlignment(Pos.CENTER_RIGHT);

        layoutHbox1.getChildren().add(title1);
        layoutHbox1.getChildren().add(documento1);

        vbox.getChildren().add(layoutHbox);
        vbox.getChildren().add(layoutHbox1);

        return vbox;
    }

    private VBox productSection(List<TicketProduct> ticketList) {
        VBox vbx = this.generateVbox();

        HBox hbx = new HBox();
        hbx.setPrefSize(216.0, 12);
        hbx.setAlignment(Pos.CENTER_LEFT);

        Label qtd = new Label("Qtd.");
        qtd.setPrefSize(25.0, 12.0);
        qtd.setMinSize(25.0, 12.0);
        qtd.setStyle("-fx-font-size: 8;");

        Label produto = new Label("Produto");
        produto.setPrefSize(90.0, 12.0);
        produto.setMinSize(90.0, 12.0);
        produto.setStyle("-fx-font-size: 8;");

        Label punit = new Label("P/Unit.");
        punit.setPrefSize(49.0, 12.0);
        punit.setMinSize(49.0, 12.0);
        punit.setStyle("-fx-font-size: 8;");

        Label total = new Label("Total");
        total.setPrefSize(52.0, 12.0);
        total.setMinSize(52.0, 12.0);
        total.setStyle("-fx-font-size: 8;");

        hbx.getChildren().add(qtd);
        hbx.getChildren().add(produto);
        hbx.getChildren().add(punit);
        hbx.getChildren().add(total);

        vbx.getChildren().add(hbx);

        for (TicketProduct tp : ticketList) {
            HBox box = new HBox();
            hbx.setPrefSize(216.0, 12);
            hbx.setAlignment(Pos.CENTER_LEFT);

            Label qtd1 = new Label(String.valueOf(tp.getQtdProduto()));
            qtd1.setPrefSize(25.0, 12.0);
            qtd1.setMinSize(25.0, 12.0);
            qtd1.setStyle("-fx-font-size: 7;");

            Label produto1 = new Label(tp.getNomeProduto());
            produto1.setPrefSize(90.0, 12.0);
            produto1.setMinSize(90.0, 12.0);
            produto1.setStyle("-fx-font-size: 7;");

            Label punit1 = new Label(String.valueOf(tp.getPrecoUnitProduto()));
            punit1.setPrefSize(49.0, 12.0);
            punit1.setMinSize(49.0, 12.0);
            punit1.setStyle("-fx-font-size: 7;");

            Label total1 = new Label(String.valueOf(tp.getValorTotalProduto()));
            total1.setPrefSize(52.0, 12.0);
            total1.setMinSize(52.0, 12.0);
            total1.setStyle("-fx-font-size: 7;");
            box.getChildren().add(qtd1);
            box.getChildren().add(produto1);
            box.getChildren().add(punit1);
            box.getChildren().add(total1);
            vbx.getChildren().add(box);
        }

        return vbx;
    }

    private VBox detailsProductSection(Ticket ticket) {
        VBox vbx = this.generateVbox();

        HBox hbx = new HBox();
        hbx.setPrefSize(216.0, 12);
        hbx.setAlignment(Pos.CENTER_LEFT);

        Label utilizador = new Label("Utilizador:");
        utilizador.setPrefSize(80.0, 12.0);
        utilizador.setMinSize(80.0, 12.0);
        utilizador.setStyle("-fx-font-size: 7;");

        Label utilizadorVal = new Label(ticket.getUtilizador());
        utilizadorVal.setPrefSize(136.0, 12.0);
        utilizadorVal.setMinSize(136.0, 12.0);
        utilizadorVal.setStyle("-fx-font-size: 7;");
        utilizadorVal.setAlignment(Pos.CENTER_RIGHT);

        hbx.getChildren().add(utilizador);
        hbx.getChildren().add(utilizadorVal);
        vbx.getChildren().add(hbx);

        HBox hbx0 = new HBox();
        hbx0.setPrefSize(216.0, 12);
        hbx0.setAlignment(Pos.CENTER_LEFT);

        Label nIten = new Label("Nº de Itens:");
        nIten.setPrefSize(80.0, 12.0);
        nIten.setMinSize(80.0, 12.0);
        nIten.setStyle("-fx-font-size: 7;");

        Label nItenVal = new Label(String.valueOf(ticket.getnItens()));
        nItenVal.setPrefSize(136.0, 12.0);
        nItenVal.setMinSize(136.0, 12.0);
        nItenVal.setStyle("-fx-font-size: 7;");
        nItenVal.setAlignment(Pos.CENTER_RIGHT);

        hbx0.getChildren().add(nIten);
        hbx0.getChildren().add(nItenVal);
        vbx.getChildren().add(hbx0);

        HBox hbx2 = new HBox();
        hbx2.setPrefSize(216.0, 12);
        hbx2.setAlignment(Pos.CENTER_LEFT);

        Label valorRecebido = new Label("Quantia Recebida:");
        valorRecebido.setPrefSize(80.0, 12.0);
        valorRecebido.setMinSize(80.0, 12.0);
        valorRecebido.setStyle("-fx-font-size: 7;");

        Label valorRecebidoVal = new Label(String.valueOf(ticket.getValorRecebido()));
        valorRecebidoVal.setPrefSize(136.0, 12.0);
        valorRecebidoVal.setMinSize(136.0, 12.0);
        valorRecebidoVal.setStyle("-fx-font-size: 7;");
        valorRecebidoVal.setAlignment(Pos.CENTER_RIGHT);

        hbx2.getChildren().add(valorRecebido);
        hbx2.getChildren().add(valorRecebidoVal);
        vbx.getChildren().add(hbx2);

        HBox hbx3 = new HBox();
        hbx3.setPrefSize(216.0, 12);
        hbx3.setAlignment(Pos.CENTER_LEFT);

        Label valorPagar = new Label("Quantia a Pagar:");
        valorPagar.setPrefSize(80.0, 12.0);
        valorPagar.setMinSize(80.0, 12.0);
        valorPagar.setStyle("-fx-font-size: 7;");

        Label valorPagarVal = new Label(String.valueOf(ticket.getValorPagar()));
        valorPagarVal.setPrefSize(136.0, 12.0);
        valorPagarVal.setMinSize(136.0, 12.0);
        valorPagarVal.setStyle("-fx-font-size: 7;");
        valorPagarVal.setAlignment(Pos.CENTER_RIGHT);

        hbx3.getChildren().add(valorPagar);
        hbx3.getChildren().add(valorPagarVal);
        vbx.getChildren().add(hbx3);

        HBox hbx4 = new HBox();
        hbx4.setPrefSize(216.0, 12);
        hbx4.setAlignment(Pos.CENTER_LEFT);

        Label troco = new Label("Troco:");
        troco.setPrefSize(80.0, 12.0);
        troco.setMinSize(80.0, 12.0);
        troco.setStyle("-fx-font-size: 7;");

        Label trocoVal = new Label(String.valueOf(ticket.getValorTroco()));
        trocoVal.setPrefSize(136.0, 12.0);
        trocoVal.setMinSize(136.0, 12.0);
        trocoVal.setStyle("-fx-font-size: 7;");
        trocoVal.setAlignment(Pos.CENTER_RIGHT);

        hbx4.getChildren().add(troco);
        hbx4.getChildren().add(trocoVal);
        vbx.getChildren().add(hbx4);

        HBox hbx5 = new HBox();
        hbx5.setPrefSize(216.0, 12);
        hbx5.setAlignment(Pos.CENTER_LEFT);

        Label total = new Label("Total:");
        total.setPrefSize(80.0, 12.0);
        total.setMinSize(80.0, 12.0);
        total.setStyle("-fx-font-size: 7;");

        Label totalVal = new Label(String.valueOf(ticket.getValorTotal()));
        totalVal.setPrefSize(136.0, 12.0);
        totalVal.setMinSize(136.0, 12.0);
        totalVal.setStyle("-fx-font-size: 7;");
        totalVal.setAlignment(Pos.CENTER_RIGHT);

        hbx5.getChildren().add(total);
        hbx5.getChildren().add(totalVal);
        vbx.getChildren().add(hbx5);

        HBox hbx6 = new HBox();
        hbx6.setPrefSize(216.0, 12);
        hbx6.setAlignment(Pos.CENTER_LEFT);

        Label tipoDoc = new Label("Tipo de Pagamento:");
        tipoDoc.setPrefSize(80.0, 12.0);
        tipoDoc.setMinSize(80.0, 12.0);
        tipoDoc.setStyle("-fx-font-size: 7;");

        Label tipoDocVal = new Label(ticket.getTipoPagamento());
        tipoDocVal.setPrefSize(136.0, 12.0);
        tipoDocVal.setMinSize(136.0, 12.0);
        tipoDocVal.setStyle("-fx-font-size: 7;");

        hbx6.getChildren().add(tipoDoc);
        hbx6.getChildren().add(tipoDocVal);

        vbx.getChildren().add(hbx6);

        return vbx;
    }
    // private GridPane createItemLine(String item, String price, Font font) {
    // GridPane line = new GridPane();
    // line.setHgap(10);
    // Text itemName = new Text(item);
    // itemName.setFont(font);
    // Text itemPrice = new Text(price);
    // itemPrice.setFont(font);
    // GridPane.setColumnSpan(itemName, 1);
    // GridPane.setColumnSpan(itemPrice, 1);
    // line.add(itemName, 0, 0);
    // line.add(itemPrice, 1, 0);
    // return line;
    // }

}
