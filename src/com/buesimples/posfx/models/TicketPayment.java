package com.buesimples.posfx.models;

public class TicketPayment {

   private String modoPagamento;

   private Double valorPago;

   public TicketPayment() {
   }

   public TicketPayment(String modoPagamento, Double valorPago) {
      this.modoPagamento = modoPagamento;
      this.valorPago = valorPago;
   }

   public String getModoPagamento() {
      return modoPagamento;
   }

   public void setModoPagamento(String modoPagamento) {
      this.modoPagamento = modoPagamento;
   }

   public Double getValorPago() {
      return valorPago;
   }

   public void setValorPago(Double valorPago) {
      this.valorPago = valorPago;
   }

}
