package com.buesimples.posfx.utils.config.printer;

import com.buesimples.posfx.controllers.IndexController;
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

    public void gerarPDF() {
        try {
            // testing();
            List<Factura> facturaList = new ArrayList<>();
            List<Ticket> ticketList = new ArrayList<>();

            Factura factura = new Factura();
            factura.setValor(100.0);

            Ticket tick = new Ticket();

            List<Map<String, Object>> config = DatabaseHelpers.build().select("configuracao", "GET");
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
                }
            }

            // String nomeDocumento = "Factura Recibo";
            String docPages = "ORIGINAL";
            tick.setDocPages(docPages);

            int IdDocumento = 356;
            List<Map<String, Object>> viewDocumento = DatabaseHelpers.build().querySelect(
                    "viewDocumento",
                    "POST",
                    ""
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
                tick.setnItens((int) Double.parseDouble("2"));
            }

            List<Map<String, Object>> viewModoPagamento = DatabaseHelpers.build().querySelect(
                    "viewartigomodopagamentodocumento",
                    "POST",
                    ""
                            + "{ "
                            + "\"idDocumento\": " + IdDocumento + " "
                            + "}");
            for (Map<String, Object> vd : viewModoPagamento) {
                String nomeTd = JsonUtils.nonNullElse(vd, "nomeTd", 0);
                String nomeE = vd.get("nomeE").toString() != null ? vd.get("nomeE").toString() : "";
                String nifE = vd.get("nifE").toString() != null ? vd.get("nifE").toString() : "";
                String dataDocumento = vd.get("dataDocumento").toString() != null ? vd.get("dataDocumento").toString()
                        : "";
                dataDocumento = dataDocumento.split("T")[0];
                String codigoDocumento = vd.get("codigoDocumento").toString() != null
                        ? vd.get("codigoDocumento").toString()
                        : "";
                String nomeU = vd.get("nomeU").toString() != null ? vd.get("nomeU").toString() : "";
                String nomePp = vd.get("nomePp").toString() != null ? vd.get("nomePp").toString() : "";
                String entregue = vd.get("entregue").toString() != null ? vd.get("entregue").toString() : "0.0";
                String total = vd.get("total").toString() != null ? vd.get("total").toString() : "0.0";
                String troco = vd.get("troco").toString() != null ? vd.get("troco").toString() : "0.0";

                tick.setNomeDocumento(nomeTd);
                tick.setNomeEntidade(nomeE);
                tick.setNomeEmpresaAtual(nomeE);
                tick.setNifEntidade(nifE);
                tick.setNifEmpresaAtual(nifE);
                tick.setDataDoc(dataDocumento);
                tick.setCodigoDocumento(codigoDocumento);
                tick.setUtilizador(nomeU);

                tick.setValorRecebido(Double.parseDouble(entregue));
                tick.setValorPagar(Double.parseDouble(total));
                tick.setValorTroco(Double.parseDouble(troco));
                tick.setTipoPagamento(nomePp);
                tick.setValorTotal(Double.parseDouble(total));
            }

            facturaList.add(factura);
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

            JRBeanCollectionDataSource itemAdicionados = new JRBeanCollectionDataSource(viewDocumento);
            List<TicketProduct> tickProducts = new ArrayList<TicketProduct>();
            tickProducts.add(new TicketProduct(2, "Munitor", 4000.0, 4000.0 * 2));
            tickProducts.add(new TicketProduct(1, "Candieiro", 1500.0, 1500.0 * 1));

            // parameters.put("TABELA_DOCUMENTO", itemAdicionados);

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
            // Gere o relatório PDF
            JasperExportManager.exportReportToPdfFile(jp, reportPath);
            System.out.println("Relatório salvo em: " + reportPath);
            // Imprimir o arquivo gerado, se necessário
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

    // public void gerarPDF(
    // String nomeEpresa,
    // String nomeDocumento,
    // String codigoDocumento,
    // String dataDoc,
    // String docPages,
    // String nomeEntidade,
    // String nifEntidade,
    // String utilizador,
    // int nItens,
    // double valorRecebido,
    // double valorPagar,
    // double valorTroco,
    // double valorTotal,
    // String tipoPagamento,
    // String telefone,
    // String email)
}
