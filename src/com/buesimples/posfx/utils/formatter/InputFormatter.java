package com.buesimples.posfx.utils.formatter;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class InputFormatter {

   private static final DecimalFormat FORMATTER = new DecimalFormat("#,##0.00",
         new DecimalFormatSymbols(Locale.getDefault()));
   
   
   public static double valorMonetarioToDouble(String valor) {
       return Double.parseDouble(valor.replace(" ", "").replace(',','.'));
   }

   public static void onlyNumbers(JFXTextField textField) {
      textField.setTextFormatter(new TextFormatter<>(change -> {
         String newText = change.getControlNewText();
         if (newText.matches("\\d*")) {
            return change;
         }
         return null;
      }));
   }

   public static void moneyFormatter(TextField textField) {
      StringConverter<Number> numberConverter = new NumberStringConverter() {
         @Override
         public String toString(Number object) {
            return object == null ? "" : FORMATTER.format(object);
         }

         @Override
         public Number fromString(String string) {
            try {
               String cleanedString = string.replaceAll("[^\\d,]", "");
               cleanedString = cleanedString.replace(',', '.');
               return FORMATTER.parse(cleanedString);
            } catch (ParseException e) {
               return null;
            }
         }
      };

      TextFormatter<Number> textFormatter = new TextFormatter<>(numberConverter, 0, change -> {
         String newText = change.getControlNewText().replaceAll("[^\\d,]", "");
         if (newText.matches("\\d*([\\.,]\\d*)?")) {
            return change;
         }
         return null;
      });

      textField.setTextFormatter(textFormatter);

      // Apply formatting on key release
      textField.setOnKeyReleased(event -> {
         String text = textField.getText();
         textField.setText(""); // Clear the text field temporarily
         textField.setText(text); // Reapply the text to trigger the formatter
      });
   }

}
