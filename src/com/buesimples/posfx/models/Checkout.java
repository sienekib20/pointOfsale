package com.buesimples.posfx.models;

public class Checkout {

    private int id;

    private String checkoutNomeArtigo;

    private int checkoutQtdArtigo;

    private String checkoutPrecoArtigo;

    private int idImposto;

    private double taxa;

    private int qtdDisponivel;

    public Checkout() {
    }

    public Checkout(int id, String checkoutNomeArtigo, int checkoutQtdArtigo, String checkoutPrecoArtigo, int idImposto, double taxa, int qtdDisponivel) {
        this.id = id;
        this.checkoutNomeArtigo = checkoutNomeArtigo;
        this.checkoutQtdArtigo = checkoutQtdArtigo;
        this.checkoutPrecoArtigo = checkoutPrecoArtigo;
        this.idImposto = idImposto;
        this.taxa = taxa;
        this.qtdDisponivel = qtdDisponivel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getIdImposto() {
        return idImposto;
    }

    public void setIdImposto(int idImposto) {
        this.idImposto = idImposto;
    }

}
