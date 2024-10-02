package com.buesimples.posfx.models;

/**
 *
 * @author sienekib
 */
public class Ticket {

    private String nomeEmpresa;
    private String nomeDocumento;
    private String codigoDocumento;
    private String dataDoc;
    private String docPages;
    private String nomeEntidade;

    private String nifEntidade;
    private String utilizador;
    private int nItens;
    private double valorRecebido;
    private double valorPagar;
    private double valorTroco;
    private double valorTotal;
    private String tipoPagamento;
    private String telefone;
    private String email;

    private int qtdProduto;
    private String nomeProduto;
    private double precoUnitProduto;
    private double valorTotalProduto;

    // Alterado
    private String nomeEmpresaAtual;
    private String enderecoEmpresaAtual;
    private String nifEmpresaAtual;

    public Ticket() {
    }

    public Ticket(int qtdProduto, String nomeProduto, double precoUnitProduto, double valorTotalProduto) {
        this.qtdProduto = qtdProduto;
        this.nomeProduto = nomeProduto;
        this.precoUnitProduto = precoUnitProduto;
        this.valorTotalProduto = valorTotalProduto;
    }

    public Ticket(String nomeEmpresa, String nomeDocumento, String codigoDocumento, String dataDoc, String docPages,
            String nomeEntidade, String nifEntidade, String utilizador, int nItens, double valorRecebido,
            double valorPagar, double valorTroco, double valorTotal, String tipoPagamento, String telefone,
            String email) {
        this.nomeEmpresa = nomeEmpresa;
        this.nomeDocumento = nomeDocumento;
        this.codigoDocumento = codigoDocumento;
        this.dataDoc = dataDoc;
        this.docPages = docPages;
        this.nomeEntidade = nomeEntidade;
        this.nifEntidade = nifEntidade;
        this.utilizador = utilizador;
        this.nItens = nItens;
        this.valorRecebido = valorRecebido;
        this.valorPagar = valorPagar;
        this.valorTroco = valorTroco;
        this.valorTotal = valorTotal;
        this.tipoPagamento = tipoPagamento;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }

    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
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

    public String getDocPages() {
        return docPages;
    }

    public void setDocPages(String docPages) {
        this.docPages = docPages;
    }

    public String getNomeEntidade() {
        return nomeEntidade;
    }

    public void setNomeEntidade(String nomeEntidade) {
        this.nomeEntidade = nomeEntidade;
    }

    public String getNifEntidade() {
        return nifEntidade;
    }

    public void setNifEntidade(String nifEntidade) {
        this.nifEntidade = nifEntidade;
    }

    public String getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(String utilizador) {
        this.utilizador = utilizador;
    }

    public int getnItens() {
        return nItens;
    }

    public void setnItens(int nItens) {
        this.nItens = nItens;
    }

    public double getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public double getValorPagar() {
        return valorPagar;
    }

    public void setValorPagar(double valorPagar) {
        this.valorPagar = valorPagar;
    }

    public double getValorTroco() {
        return valorTroco;
    }

    public void setValorTroco(double valorTroco) {
        this.valorTroco = valorTroco;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public double getPrecoUnitProduto() {
        return precoUnitProduto;
    }

    public void setPrecoUnitProduto(double precoUnitProduto) {
        this.precoUnitProduto = precoUnitProduto;
    }

    public double getValorTotalProduto() {
        return valorTotalProduto;
    }

    public void setValorTotalProduto(double valorTotalProduto) {
        this.valorTotalProduto = valorTotalProduto;
    }

    public String getNomeEmpresaAtual() {
        return nomeEmpresaAtual;
    }

    public void setNomeEmpresaAtual(String nomeEmpresaAtual) {
        this.nomeEmpresaAtual = nomeEmpresaAtual;
    }

    public String getEnderecoEmpresaAtual() {
        return enderecoEmpresaAtual;
    }

    public void setEnderecoEmpresaAtual(String enderecoEmpresaAtual) {
        this.enderecoEmpresaAtual = enderecoEmpresaAtual;
    }

    public String getNifEmpresaAtual() {
        return nifEmpresaAtual;
    }

    public void setNifEmpresaAtual(String nifEmpresaAtual) {
        this.nifEmpresaAtual = nifEmpresaAtual;
    }

}
