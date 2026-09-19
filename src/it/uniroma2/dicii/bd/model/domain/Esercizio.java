package it.uniroma2.dicii.bd.model.domain;

public class Esercizio {

    private String nome;

    public Esercizio() {}

    public Esercizio(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

}
