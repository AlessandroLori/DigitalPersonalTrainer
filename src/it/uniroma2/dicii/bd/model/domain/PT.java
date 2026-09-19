package it.uniroma2.dicii.bd.model.domain;

public class PT {

    private String cf;
    private String nome;
    private String cognome;
    private String username;

    public PT(){}

    public PT(String cf){
        this.cf=cf;
    }

    public PT(String cf, String nome, String cognome, String username) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
    }

    public String getCf() {return cf;}
    public void setCf(String cf) {this.cf = cf;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getCognome() {return cognome;}
    public void setCognome(String cognome) {this.cognome = cognome;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}



}
