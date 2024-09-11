package com.buesimples.posfx.models;

public class Checkout {

   private int id;

   private String checkoutNomeArtigo;

   private int checkoutQtdArtigo;

   private String checkoutPrecoArtigo;

   private double taxa;

   private int qtdDisponivel;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public Checkout() {
   }

   public Checkout(int id, String checkoutNomeArtigo, int checkoutQtdArtigo, String checkoutPrecoArtigo, double taxa, int qtdDisponivel) {
      this.id = id;
      this.checkoutNomeArtigo = checkoutNomeArtigo;
      this.checkoutQtdArtigo = checkoutQtdArtigo;
      this.checkoutPrecoArtigo = checkoutPrecoArtigo;
      this.taxa = taxa;
      this.qtdDisponivel = qtdDisponivel;
   }

   public String getCheckoutNomeArtigo() {
      return checkoutNomeArtigo;
   }

   public void setCheckoutNomeArtigo(String checkoutNomeArtigo) {
      this.checkoutNomeArtigo = checkoutNomeArtigo;
   }

   public int getCheckoutQtdArtigo() {
      return checkoutQtdArtigo;
   }

   public void setCheckoutQtdArtigo(int checkoutQtdArtigo) {
      this.checkoutQtdArtigo = checkoutQtdArtigo;
   }

   public String getCheckoutPrecoArtigo() {
      return checkoutPrecoArtigo;
   }

   public void setCheckoutPrecoArtigo(String checkoutPrecoArtigo) {
      this.checkoutPrecoArtigo = checkoutPrecoArtigo;
   }

   public double getTaxa() {
      return taxa;
   }

   public void setTaxa(double taxa) {
      this.taxa = taxa;
   }

   public int qtdDisponivel() {
      return qtdDisponivel;
   }

   public void setQtdDisponivel(int qtdDisponivel) {
      this.qtdDisponivel = qtdDisponivel;
   }
}
