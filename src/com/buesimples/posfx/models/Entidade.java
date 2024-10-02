package com.buesimples.posfx.models;

/**
 *
 * @author sienekib
 */
public class Entidade {

    private int refEntidade;

    private String nome;

    private String nomeComercial;

    private String BI;

    private String NIF;

    private String localidade;

    private String dataNasc;

    private String morada;

    private String tipoEntidade;

    private String retencao;

    private String estado;

    public Entidade() {
    }

    public Entidade(int refEntidade, String nome, String NIF) {
        this.refEntidade = refEntidade;
        this.nome = nome;
        this.NIF = NIF;
    }

    public Entidade(int refEntidade, String nome, String NIF, String morada) {
        this.refEntidade = refEntidade;
        this.nome = nome;
        this.NIF = NIF;
        this.morada = morada;
    }

    public Entidade(int refEntidade, String nome, String nomeComercial, String BI, String NIF, String localidade, String dataNasc, String morada, String tipoEntidade, String retencao, String estado) {
        this.refEntidade = refEntidade;
        this.nome = nome;
        this.nomeComercial = nomeComercial;
        this.BI = BI;
        this.NIF = NIF;
        this.localidade = localidade;
        this.dataNasc = dataNasc;
        this.morada = morada;
        this.tipoEntidade = tipoEntidade;
        this.retencao = retencao;
        this.estado = estado;
    }

    public int getRefEntidade() {
        return refEntidade;
    }

    public void setRefEntidade(int refEntidade) {
        this.refEntidade = refEntidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeComercial() {
        return nomeComercial;
    }

    public void setNomeComercial(String nomeComercial) {
        this.nomeComercial = nomeComercial;
    }

    public String getBI() {
        return BI;
    }

    public void setBI(String BI) {
        this.BI = BI;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getTipoEntidade() {
        return tipoEntidade;
    }

    public void setTipoEntidade(String tipoEntidade) {
        this.tipoEntidade = tipoEntidade;
    }

    public String getRetencao() {
        return retencao;
    }

    public void setRetencao(String retencao) {
        this.retencao = retencao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
