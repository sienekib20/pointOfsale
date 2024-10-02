package com.buesimples.posfx.models;

public class Artigos {

    private Integer id;
    private String article;
    private String price;
    private Integer priceLine;
    private String taxa;
    private Integer qtdStock;
    private Integer idImposto;

    public Artigos() {
    }

    public Artigos(Integer id, String article, String price, String taxa, Integer qtdStock, Integer idImposto) {
        this.id = id;
        this.article = article;
        this.price = price;
        this.taxa = taxa;
        this.qtdStock = qtdStock;
        this.idImposto = idImposto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getPriceLine() {
        return priceLine;
    }

    public void setPriceLine(Integer priceLine) {
        this.priceLine = priceLine;
    }

    public String getTaxa() {
        return taxa;
    }

    public void setTaxa(String taxa) {
        this.taxa = taxa;
    }

    public Integer getQtdStock() {
        return this.qtdStock;
    }

    public void setQtdStock(Integer qtd) {
        this.qtdStock = qtd;
    }

    public Integer getIdImposto() {
        return idImposto;
    }

    public void setIdImposto(Integer idImposto) {
        this.idImposto = idImposto;
    }

}
