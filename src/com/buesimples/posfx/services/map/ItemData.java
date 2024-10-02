package com.buesimples.posfx.services.map;

/**
 *
 * @author sienekib
 */
public class ItemData {

    private int idArtigo;
    private String descricao;
    private int quantidade;
    private double precoInicial;
    private int idImposto;
    private double valorImposto;
    private double valorTotal;

    public ItemData(int idArtigo, String descricao, int quantidade, double precoInicial, int idImposto, double valorImposto, double valorTotal) {
        this.idArtigo = idArtigo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.precoInicial = precoInicial;
        this.idImposto = idImposto;
        this.valorImposto = valorImposto;
        this.valorTotal = valorTotal;
    }

    // Getters e Setters
    public int getIdArtigo() {
        return idArtigo;
    }

    public void setIdArtigo(int idArtigo) {
        this.idArtigo = idArtigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoInicial() {
        return precoInicial;
    }

    public void setPrecoInicial(double precoInicial) {
        this.precoInicial = precoInicial;
    }

    public int getIdImposto() {
        return idImposto;
    }

    public void setIdImposto(int idImposto) {
        this.idImposto = idImposto;
    }

    public double getValorImposto() {
        return valorImposto;
    }

    public void setValorImposto(double valorImposto) {
        this.valorImposto = valorImposto;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Atualizar quantidade e recalcular valor total
    public void atualizarQuantidade(int novaQuantidade) {
        this.quantidade = novaQuantidade;
        this.valorTotal = this.quantidade * this.precoInicial;
    }
}
