package it.uniroma2.dicii.bd.model.domain;

import java.util.Date;

public class Scheda {

    private java.sql.Date dataEmissione;
    private java.sql.Date dataScad;
    private enum tipo{scaduta, attiva}
    private String cf;

    public Scheda(){}

    public Scheda(java.sql.Date dataEmissione, java.sql.Date dataScad){
        this.dataEmissione = dataEmissione;
        this.dataScad = dataScad;
    }

    public Scheda(java.sql.Date dataEmissione){
        this.dataEmissione = dataEmissione;
    }

    public java.sql.Date getDataEmissione() {
        return dataEmissione;
    }
    public void setDataEmissione(java.sql.Date dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public java.sql.Date getDataScad() {
        return dataScad;
    }
    public void setDataScad(java.sql.Date dataScad) {
        this.dataScad = dataScad;
    }


}
