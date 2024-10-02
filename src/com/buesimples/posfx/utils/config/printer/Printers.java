package com.buesimples.posfx.utils.config.printer;

import com.buesimples.posfx.controllers.IndexController;
import com.buesimples.posfx.controllers.cart.CheckoutItemController;
import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.models.Factura;
import com.buesimples.posfx.models.Ticket;
import com.buesimples.posfx.models.TicketProduct;
import com.buesimples.posfx.utils.constants.Constants;
import com.buesimples.posfx.utils.json.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.buesimples.posfx.utils.config.Config;
import com.jfoenix.controls.JFXTextArea;
import java.awt.Desktop;

import javax.print.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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

    /*
     * public void printDocument(String selectedPrinter) {
     * // Busca as impressoras disponíveis
     * PrintService printer =
     * Arrays.stream(PrintServiceLookup.lookupPrintServices(null, null))
     * .filter(service -> service.getName().equals(selectedPrinter))
     * .findFirst()
     * .orElse(null);
     * 
     * if (printer == null) {
     * System.out.println("Impressora não encontrada: " + selectedPrinter);
     * return;
     * }
     * 
     * // Exibe os DocFlavors suportados
     * System.out.println("DocFlavors suportados pela impressora " + selectedPrinter
     * + ":");
     * for (DocFlavor flavor : printer.getSupportedDocFlavors()) {
     * System.out.println(flavor);
     * }
     * 
     * // Tenta imprimir
     * try {
     * DocPrintJob printJob = printer.createPrintJob();
     * String textToPrint = "Olá, esta é uma impressão de teste!";
     * // DocFlavor flavor = DocFlavor.STRING.TEXT_PLAIN; // Use TEXT_PLAIN para
     * texto simples
     * DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE; // Use TEXT_PLAIN para
     * texto simples
     * 
     * // Verifica se o flavor é suportado
     * if (!printer.isDocFlavorSupported(flavor)) {
     * System.out.println("Flavor " + flavor + " não é suportado pela impressora.");
     * return;
     * }
     * 
     * Doc doc = new SimpleDoc(textToPrint.getBytes(), flavor, null);
     * printJob.print(doc, null);
     * System.out.println("Imprimindo na impressora: " + selectedPrinter);
     * } catch (PrintException e) {
     * System.out.println("Erro ao imprimir: " + e.getMessage());
     * }
     * }
     */
    public void printDocument(String selectedPrinter) {
        ObservableSet<Printer> printers = Printer.getAllPrinters();

        for (Printer printer : printers) {
            System.out.println("GETTING PRINNTERS: " + printer.getName());
            if (selectedPrinter.equals(printer.getName())) {
                // print();
                Stage stage = (IndexController.getInstance().getMainStage());
                pageSetup(stage);
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

    public void pageSetup(Stage owner) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            return;
        }

        // boolean proceder = job.showPageSetupDialog(owner);
        boolean proceder = job.showPrintDialog(owner);

        if (proceder) {
            print(job);
        }
    }

    public void printPDF(String pdfPath, String printerName) {

    }

    private void print(PrinterJob job) {
        // Define the Job Status Message
        System.out.println("Creating a printer job...");

        // Create a printer job for the default printer
        // PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            // Show the printer job status
            System.out.println(job.jobStatusProperty().asString());

            JFXTextArea a = new JFXTextArea("Testando");

            // Print the node
            boolean printed = job.printPage(a);

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

    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
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

            // Gerar o relatório PDF
            JasperExportManager.exportReportToPdfFile(jp, reportPath);
            System.out.println("Relatório salvo em: " + reportPath);

            // printReport(reportPath);

            System.out.println("Relatório gerado com sucesso!");
        } catch (JRException ex) {
            System.out.println("ERRO: " + ex);
        }
    }

    private void printReport(String filePath) {
        try {
            File file = new File(filePath);

            // Verificar se a impressão é suportada
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.PRINT)) {
                Desktop.getDesktop().print(file);
                System.out.println("Arquivo enviado para impressão: " + filePath);
            } else {
                System.out.println("Impressão não é suportada neste sistema.");
            }

        } catch (IOException ex) {
            System.out.println("Erro ao tentar imprimir o arquivo: " + ex.getMessage());
        }
    }
}
