package it.uniroma2.dicii.bd.model.domain;

public class Composta {

    private java.sql.Date dataEmissione;
    private int numeroSerie;
    private int numeroRipetizioni;
    private int indice;
    private String macchinario;
    private String cf;
    private String nomeEsercizio;

    public Composta(){}

    public Composta( /*java.sql.Date dataEmissione,*/ int numeroSerie, int numeroRipetizioni, int indice, String macchinario, String cf, String nomeEsercizio ) {
        //this.dataEmissione = dataEmissione;
        this.numeroSerie = numeroSerie;
        this.numeroRipetizioni = numeroRipetizioni;
        this.indice = indice;
        this.macchinario = macchinario;
        this.cf = cf;
        this.nomeEsercizio = nomeEsercizio;
    }

    public java.sql.Date getDataEmissione() {
        return dataEmissione;
    }
    public void setDataEmissione(java.sql.Date dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public int getNumeroSerie() {
        return numeroSerie;
    }
    public void setNumeroSerie(int numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public int getNumeroRipetizioni() {
        return numeroRipetizioni;
    }
    public void setNumeroRipetizioni(int numeroRipetizioni){
        this.numeroRipetizioni = numeroRipetizioni;
    }

    public int getIndice() {
        return indice;
    }
    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getMacchinario() {
        return macchinario;
    }
    public void setMacchinario(String macchinario) {
        this.macchinario = macchinario;
    }

    public String getCf() {
        return cf;
    }
    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getNomeEsercizio() {
        return nomeEsercizio;
    }
    public void setNomeEsercizio(String nomeEsercizio) {
        this.nomeEsercizio = nomeEsercizio;
    }


}
