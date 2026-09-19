package it.uniroma2.dicii.bd.model.domain;

public enum Role {
    SEGRETERIA(3),
    UTENTE(2),
    PT(1);
    //GENERALUSER(4);



    private final int id;

    private Role(int id) {
        this.id = id;
    }

    public static Role fromInt(int id) {
        for (Role type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
