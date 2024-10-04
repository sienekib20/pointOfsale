package com.buesimples.posfx;

import java.util.List;
import java.util.Map;

import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.utils.loader.NodeLoader;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

   @Override
   public void start(Stage primaryStage) throws Exception {
      NodeLoader loader = NodeLoader.getInstance();

      if (DatabaseHelpers.build().checkConnection() == false) {
         loader.showWindow(primaryStage, "alerts.ConnectionError");
         return;
      }
      this.pretifyFont();
      loader.showWindow(primaryStage, "auth.Login");
      generatehash();
   }

   public static void main(String[] args) {
      launch(args);
   }

   private void pretifyFont() {
      System.setProperty("prism.lcdtext", "true");
      System.setProperty("prism.text", "t2k");
      System.setProperty("prism.subpixeltext", "true");
      System.setProperty("prism.allowhidpi", "true");
   }

   private void generatehash() {
      List<Map<String, Object>> mapDoc = DatabaseHelpers.build().querySelect(
            "documento",
            "POST", "{\r\n" + //
                  "    \"idDocumento\": 356\r\n" + //
                  "}");
      for (Map<String, Object> map : mapDoc) {
         String dataDoc = map.get("dataDoc") != null ? map.get("dataDoc").toString() : "";
         String[] dataParts = dataDoc.contains("T") ? dataDoc.split("T") : dataDoc.split(" ");
         System.out.println("Data: " + dataDoc);
         System.out.println("Data Parts: " + dataParts[0]);
      }
   }

   // private void testLayoutTicket(Stage primaryStage) {
   // // Cabeçalho
   // Text companyName = new Text("MAYONGI ANGOLA\n");
   // companyName.setFont(Font.font("Arial", 16));
   // companyName.setTextAlignment(TextAlignment.CENTER);

   // Text docType = new Text("Factura Recibo\n");
   // docType.setFont(Font.font("Arial", 14));
   // docType.setTextAlignment(TextAlignment.CENTER);

   // Text original = new Text("ORIGINAL\n\n");
   // original.setFont(Font.font("Arial", 12));
   // original.setTextAlignment(TextAlignment.CENTER);

   // TextFlow header = new TextFlow(companyName, docType, original);
   // // Centraliza o cabeçalho
   // VBox headerBox = new VBox(header);
   // headerBox.setAlignment(Pos.CENTER);

   // // Detalhes do documento
   // Text docInfo = new Text("Documento: FR 2024/27\nData de emissão:
   // 2024-10-02\n\n");
   // Text clientInfo = new Text("Cliente: Consumidor Final\nNIF: 999999999\n\n");

   // TextFlow documentDetails = new TextFlow(docInfo, clientInfo);

   // // Itens da compra
   // Text itemsHeader = new Text("Qtd. Produto P/Unit. Total\n");
   // itemsHeader.setFont(Font.font("Arial", 12));

   // Text item1 = new Text("1 Chavenas 1000.0 1000.0\n");
   // Text item2 = new Text("1 Coluna JBL 20000.0 20000.0\n");
   // Text item3 = new Text("2 Instalação 100000.0 200000.0\n");

   // TextFlow items = new TextFlow(itemsHeader, item1, item2, item3);

   // // Resumo financeiro
   // Text summary = new Text(
   // "Utilizador: Conta Mayongi\n" +
   // "Nº de Itens: 2\n" +
   // "Quantia recebida: 23940.0\n" +
   // "Quantia a pagar: 23940.0\n" +
   // "Troco: 0.0\n" +
   // "Total: 23940.0\n" +
   // "Tipo de Pagamento: Pronto Pagamento\n\n"
   // );

   // TextFlow financialSummary = new TextFlow(summary);

   // // Rodapé
   // Text footer = new Text(
   // "Consumidor Final\n" +
   // "Luanda - Angola\n" +
   // "568293353655\n\n" +
   // "Contactos:\n" +
   // "Telefone: 930407548\n" +
   // "Email: email@gmail.com\n\n" +
   // "*Obrigado e volte sempre!*\n\n" +
   // "Iva: Regime de Não Sujeição\n\n" +
   // "Os bens/serviços foram colocados à disposição\n" +
   // "do adquirente na data e local do documento\n"
   // );

   // TextFlow footerText = new TextFlow(footer);

   // // Layout geral
   // VBox layout = new VBox(10, headerBox, documentDetails, items,
   // financialSummary, footerText);
   // layout.setAlignment(Pos.TOP_CENTER); // Centraliza todo o conteúdo

   // Scene scene = new Scene(layout, 300, 500);
   // primaryStage.setScene(scene);
   // primaryStage.setTitle("Ticket Example");
   // primaryStage.show();
   // }

}
