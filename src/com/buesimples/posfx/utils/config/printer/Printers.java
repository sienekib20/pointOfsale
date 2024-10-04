package com.buesimples.posfx.utils.config.printer;

import com.buesimples.posfx.controllers.IndexController;
import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.models.Ticket;
import com.buesimples.posfx.models.TicketProduct;
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

    public void gerarPDF(int IdDocumento, int nItem) {
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
         PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

         if (printService == null) {
            System.out.println("No printer found.");
            return;
         }

         PDFPageable pageable = new PDFPageable(document);
         DocPrintJob printJob = printService.createPrintJob();

         // Create the PrintRequestAttributeSet as required
         PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();

         // Adjusting the DocFlavor according to PDFPageable
         DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE; // Adjust this if needed

         // Make sure to use the correct SimpleDoc type
         SimpleDoc doc = new SimpleDoc(pageable, flavor, null);
         printJob.print(doc, attr);

         document.close();
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

    private StackPane gggggg() {
        // // Definir fonte menor para o recibo
        // Font smallFont = Font.font("Courier New", 10);

        // // Criar VBox para organizar os textos de forma vertical
        // VBox vbox = new VBox(5); // Espaçamento de 5px entre os elementos
        // vbox.setPadding(new Insets(10, 10, 10, 10)); // Definir margens
        // vbox.setAlignment(Pos.TOP_CENTER); // Alinhar no topo e centralizar o conteúdo

        // // Cabeçalho centralizado
        // Text header1 = new Text("The Lone Pine");
        // header1.setFont(Font.font("Courier New", 14));
        // vbox.getChildren().add(header1);

        // Text header2 = new Text("43 Manchester Road\n12480 Brisbane\nAustralia\n617-3236-6207\n");
        // header2.setFont(smallFont);
        // vbox.getChildren().add(header2);

        // // Informações da fatura
        // Text invoiceInfo = new Text("Invoice: 08000008          09/04/08\nTable: 25                 12:45\n");
        // invoiceInfo.setFont(smallFont);
        // vbox.getChildren().add(invoiceInfo);

        // // Linha separadora
        // Text separator = new Text("----------------------------------------");
        // separator.setFont(smallFont);
        // vbox.getChildren().add(separator);

        // // Itens do recibo
        // vbox.getChildren().add(createItemLine("2 Carlsberg Bottle", "16.00", smallFont));
        // vbox.getChildren().add(createItemLine("3 Heineken Draft Standard", "24.60", smallFont));
        // vbox.getChildren().add(createItemLine("1 Heineken Draft Half Liter", "15.20", smallFont));
        // vbox.getChildren().add(createItemLine("2 Carlsberg Bucket (5 bottles)", "80.00", smallFont));
        // vbox.getChildren().add(createItemLine("4 Grilled Chicken Breast", "74.00", smallFont));
        // vbox.getChildren().add(createItemLine("3 Sirloin Steak", "96.00", smallFont));
        // vbox.getChildren().add(createItemLine("1 Coke", "3.50", smallFont));
        // vbox.getChildren().add(createItemLine("5 Ice Cream", "18.00", smallFont));

        // // Subtotal, imposto e taxa de serviço
        // vbox.getChildren().add(createItemLine("Subtotal", "327.30", smallFont));
        // vbox.getChildren().add(createItemLine("Sales/Gov Tax - 5%", "16.36", smallFont));
        // vbox.getChildren().add(createItemLine("Service Charge - 10%", "32.73", smallFont));

        // // Linha separadora
        // Text separator2 = new Text("----------------------------------------");
        // separator2.setFont(smallFont);
        // vbox.getChildren().add(separator2);

        // // Total geral
        // Text grandTotal = new Text("GRAND TOTAL: 376.40");
        // grandTotal.setFont(smallFont);
        // vbox.getChildren().add(grandTotal);

        // // Mensagem final
        // Text footer = new Text("Thank you and see you again!\nCash: 400.00  Change: 23.60\n");
        // footer.setFont(smallFont);
        // vbox.getChildren().add(footer);

        // // Configurar layout da cena
        // StackPane pane = new StackPane(vbox);
        // pane.setAlignment(Pos.TOP_LEFT);
        // Font menor para o recibo
        Font smallFont = Font.font("Courier New", 8);

        // Criar um GridPane para organizar o layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10)); // Ajustar margens
        grid.setVgap(5); // Espaçamento vertical entre linhas
        grid.setHgap(10); // Espaçamento horizontal entre colunas

        // Adicionar cabeçalho ao GridPane
        Text header1 = new Text("The Lone Pine");
        header1.setFont(Font.font("Courier New", 8));
        GridPane.setColumnSpan(header1, 2); // Mesclar colunas para centralizar
        grid.add(header1, 0, 0, 2, 1); // Adicionar na coluna 0, linha 0, ocupando 2 colunas

        Text header2 = new Text("43 Manchester Road\n12480 Brisbane\nAustralia\n617-3236-6207\n");
        header2.setFont(smallFont);
        grid.add(header2, 0, 1, 2, 1); // Adicionar em duas colunas

        // Informações da fatura
        Text invoiceInfo = new Text("Invoice: 08000008          09/04/08\nTable: 25                 12:45\n");
        invoiceInfo.setFont(smallFont);
        grid.add(invoiceInfo, 0, 2, 2, 1);

        // Adicionar linha separadora
        Text separator = new Text("----------------------------------------");
        separator.setFont(smallFont);
        grid.add(separator, 0, 3, 2, 1);

        // Adicionar itens ao recibo
        grid.add(createItemLine("2 Carlsberg Bottle", "16.00", smallFont), 0, 4, 2, 1);
        grid.add(createItemLine("3 Heineken Draft Standard", "24.60", smallFont), 0, 5, 2, 1);
        grid.add(createItemLine("1 Heineken Draft Half Liter", "15.20", smallFont), 0, 6, 2, 1);
        grid.add(createItemLine("2 Carlsberg Bucket (5 bottles)", "80.00", smallFont), 0, 7, 2, 1);
        grid.add(createItemLine("4 Grilled Chicken Breast", "74.00", smallFont), 0, 8, 2, 1);
        grid.add(createItemLine("3 Sirloin Steak", "96.00", smallFont), 0, 9, 2, 1);
        grid.add(createItemLine("1 Coke", "3.50", smallFont), 0, 10, 2, 1);
        grid.add(createItemLine("5 Ice Cream", "18.00", smallFont), 0, 11, 2, 1);

        // Adicionar subtotal, impostos e taxas
        grid.add(createItemLine("Subtotal", "327.30", smallFont), 0, 12, 2, 1);
        grid.add(createItemLine("Sales/Gov Tax - 5%", "16.36", smallFont), 0, 13, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);
        grid.add(createItemLine("Service Charge - 10%", "32.73", smallFont), 0, 14, 2, 1);

        // Adicionar linha separadora
        grid.add(new Text("----------------------------------------"), 0, 15, 2, 1);

        // Total geral
        Text grandTotal = new Text("GRAND TOTAL: 376.40");
        grandTotal.setFont(smallFont);
        grid.add(grandTotal, 0, 16, 2, 1);

        // Adicionar mensagem final
        Text footer = new Text("Thank you and see you again!\nCash: 400.00  Change: 23.60\n");
        footer.setFont(smallFont);
        grid.add(footer, 0, 17, 2, 1);

        // Configurar layout da cena
        StackPane pane = new StackPane(grid);
        pane.setAlignment(Pos.TOP_LEFT);
        return pane;
    }

    private GridPane createItemLine(String item, String price, Font font) {
         GridPane line = new GridPane();
        line.setHgap(10);
        Text itemName = new Text(item);
        itemName.setFont(font);
        Text itemPrice = new Text(price);
        itemPrice.setFont(font);
        GridPane.setColumnSpan(itemName, 1);
        GridPane.setColumnSpan(itemPrice, 1);
        line.add(itemName, 0, 0);
        line.add(itemPrice, 1, 0);
        return line;
    }

}
