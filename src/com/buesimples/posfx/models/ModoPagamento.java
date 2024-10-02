package com.buesimples.posfx.models;

/**
 *
 * @author sienekib
 */
public class ModoPagamento {

    private int idDocumento;

    private int idModoPagamento;

    private double valor;

    public ModoPagamento(int idDocumento, int idModoPagamento, double valor) {
        this.idDocumento = idDocumento;
        this.idModoPagamento = idModoPagamento;
        this.valor = valor;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdModoPagamento() {
        return idModoPagamento;
    }

    public void setIdModoPagamento(int idModoPagamento) {
        this.idModoPagamento = idModoPagamento;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

}
