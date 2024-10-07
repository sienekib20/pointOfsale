package com.buesimples.posfx.models;

public class TicketProductModel {

   private int qtdProduto;

   private String nomeProduto;

   private Double valorTotalProduto;

   public TicketProductModel() {
   }

   public TicketProductModel(int qtdProduto, String nomeProduto, Double valorTotalProduto) {
      this.qtdProduto = qtdProduto;
      this.nomeProduto = nomeProduto;
      this.valorTotalProduto = valorTotalProduto;
   }

   public int getQtdProduto() {
      return qtdProduto;
   }

   public void setQtdProduto(int qtdProduto) {
      this.qtdProduto = qtdProduto;
   }

   public String getNomeProduto() {
      return nomeProduto;
   }

   public void setNomeProduto(String nomeProduto) {
      this.nomeProduto = nomeProduto;
   }

   public Double getValorTotalProduto() {
      return valorTotalProduto;
   }

   public void setValorTotalProduto(Double valorTotalProduto) {
      this.valorTotalProduto = valorTotalProduto;
   }

}
