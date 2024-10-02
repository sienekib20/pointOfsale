package com.buesimples.posfx.models;

/**
 *
 * @author sienekib
 */
public class TicketProduct {

    private int qtdProduto;

    private String nomeProduto;

    private Double precoUnitProduto;

    private Double valorTotalProduto;

    public TicketProduct() {
    }

    public TicketProduct(int qtdProduto, String nomeProduto, Double precoUnitProduto, Double valorTotalProduto) {
        this.qtdProduto = qtdProduto;
        this.nomeProduto = nomeProduto;
        this.precoUnitProduto = precoUnitProduto;
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

    public Double getPrecoUnitProduto() {
        return precoUnitProduto;
    }

    public void setPrecoUnitProduto(Double precoUnitProduto) {
        this.precoUnitProduto = precoUnitProduto;
    }

    public Double getValorTotalProduto() {
        return valorTotalProduto;
    }

    public void setValorTotalProduto(Double valorTotalProduto) {
        this.valorTotalProduto = valorTotalProduto;
    }
}
