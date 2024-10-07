package com.buesimples.posfx.models;

public class TicketModel {

   private String nomeEmpresa;
   private String enderecoEmpresa;
   private String nifEmpresa;
   private String telefoneEmpresa;

   private String codigoDocumento;
   private String dataDoc;
   private String horaDoc;

   private int nItems;
   private Double valorTroco;
   private Double valorTotal;

   private String nomeCliente;
   private String moradaCliente;
   private String nContribuinte;
   private String telefoneCliente;

   private String numeroProcesso;
   private String utilizador;

   public TicketModel() {
   }

   public TicketModel(String nomeEmpresa, String enderecoEmpresa, String nifEmpresa, String telefoneEmpresa,
         String codigoDocumento, String dataDoc, String horaDoc, int nItems, Double valorTroco, Double valorTotal,
         String nomeCliente, String moradaCliente, String nContribuinte, String telefoneCliente, String numeroProcesso,
         String utilizador) {
      this.nomeEmpresa = nomeEmpresa;
      this.enderecoEmpresa = enderecoEmpresa;
      this.nifEmpresa = nifEmpresa;
      this.telefoneEmpresa = telefoneEmpresa;
      this.codigoDocumento = codigoDocumento;
      this.dataDoc = dataDoc;
      this.horaDoc = horaDoc;
      this.nItems = nItems;
      this.valorTroco = valorTroco;
      this.valorTotal = valorTotal;
      this.nomeCliente = nomeCliente;
      this.moradaCliente = moradaCliente;
      this.nContribuinte = nContribuinte;
      this.telefoneCliente = telefoneCliente;
      this.numeroProcesso = numeroProcesso;
      this.utilizador = utilizador;
   }

   public String getNomeEmpresa() {
      return nomeEmpresa;
   }

   public void setNomeEmpresa(String nomeEmpresa) {
      this.nomeEmpresa = nomeEmpresa;
   }

   public String getEnderecoEmpresa() {
      return enderecoEmpresa;
   }

   public void setEnderecoEmpresa(String enderecoEmpresa) {
      this.enderecoEmpresa = enderecoEmpresa;
   }

   public String getNifEmpresa() {
      return nifEmpresa;
   }

   public void setNifEmpresa(String nifEmpresa) {
      this.nifEmpresa = nifEmpresa;
   }

   public String getTelefoneEmpresa() {
      return telefoneEmpresa;
   }

   public void setTelefoneEmpresa(String telefoneEmpresa) {
      this.telefoneEmpresa = telefoneEmpresa;
   }

   public String getCodigoDocumento() {
      return codigoDocumento;
   }

   public void setCodigoDocumento(String codigoDocumento) {
      this.codigoDocumento = codigoDocumento;
   }

   public String getDataDoc() {
      return dataDoc;
   }

   public void setDataDoc(String dataDoc) {
      this.dataDoc = dataDoc;
   }

   public String getHoraDoc() {
      return horaDoc;
   }

   public void setHoraDoc(String horaDoc) {
      this.horaDoc = horaDoc;
   }

   public int getnItems() {
      return nItems;
   }

   public void setnItems(int nItems) {
      this.nItems = nItems;
   }

   public Double getValorTroco() {
      return valorTroco;
   }

   public void setValorTroco(Double valorTroco) {
      this.valorTroco = valorTroco;
   }

   public Double getValorTotal() {
      return valorTotal;
   }

   public void setValorTotal(Double valorTotal) {
      this.valorTotal = valorTotal;
   }

   public String getNomeCliente() {
      return nomeCliente;
   }

   public void setNomeCliente(String nomeCliente) {
      this.nomeCliente = nomeCliente;
   }

   public String getMoradaCliente() {
      return moradaCliente;
   }

   public void setMoradaCliente(String moradaCliente) {
      this.moradaCliente = moradaCliente;
   }

   public String getnContribuinte() {
      return nContribuinte;
   }

   public void setnContribuinte(String nContribuinte) {
      this.nContribuinte = nContribuinte;
   }

   public String getTelefoneCliente() {
      return telefoneCliente;
   }

   public void setTelefoneCliente(String telefoneCliente) {
      this.telefoneCliente = telefoneCliente;
   }

   public String getNumeroProcesso() {
      return numeroProcesso;
   }

   public void setNumeroProcesso(String numeroProcesso) {
      this.numeroProcesso = numeroProcesso;
   }

   public String getUtilizador() {
      return utilizador;
   }

   public void setUtilizador(String utilizador) {
      this.utilizador = utilizador;
   }

}
