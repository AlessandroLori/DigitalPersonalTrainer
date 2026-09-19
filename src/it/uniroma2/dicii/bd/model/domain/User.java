package it.uniroma2.dicii.bd.model.domain;

public class User {

    private String cf;
    private String nome;
    private String cognome;
    private String pt;
    private String username;

    public User(){}

    public User(String cf){
        this.cf = cf;
    }

    public User(String cf, String nome, String cognome, String pt, String username) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.pt = pt;
        this.username = username;
    }

    public String getCf() {return cf;}
    public void setCf(String cf) {this.cf = cf;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getCognome() {return cognome;}
    public void setCognome(String cognome) {this.cognome = cognome;}

    public String getPt() {return pt;}
    public void setPt(String pt) {this.pt = pt;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

}
