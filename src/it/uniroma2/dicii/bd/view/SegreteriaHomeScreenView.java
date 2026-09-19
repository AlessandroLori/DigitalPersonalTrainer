package it.uniroma2.dicii.bd.view;

import java.util.Scanner;

public class SegreteriaHomeScreenView {

    public static int showHomeScreen() {
        int choice;
        Scanner input = new Scanner(System.in);

        while(true){
            System.out.println("");
            System.out.println("Cosa vuoi fare?");
            System.out.println("1) Aggiungi nuovo macchinario in palestra");
            System.out.println("2) Registra un nuovo utente");
            System.out.println("3) Registra un nuovo personal trainer");
            System.out.println("4) Esci");
            System.out.println("");
            System.out.println("Inserisci il codice");
            choice = input.nextInt();

            if(choice == 1 || choice == 2 || choice == 3 || choice == 4){
                break;
            }
            System.out.println("Operazione invalida!");
        }
        return choice;
    }

    public static String prendiNomeMacchinario() {
        Scanner input = new Scanner(System.in);
        String esercizio;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Quale macchinario vuoi aggiungere?");
            System.out.println("");
            System.out.println("Inserisci il nome: ");
            esercizio = input.next();
            if (esercizio != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }
        return esercizio;
    }

    public static String prendicf() {
        Scanner input = new Scanner(System.in);
        String cf;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci il codice fiscale: ");
            cf = input.next();
            if (cf != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }
        return cf;
    }


    public static String prendiNome() {
        Scanner input = new Scanner(System.in);
        String nome;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci il nome: ");
            nome = input.next();
            if (nome != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }
        return nome;
    }

    public static String prendiCognome() {
        Scanner input = new Scanner(System.in);
        String cognome;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci il cognome: ");
            cognome = input.next();
            if (cognome != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }
        return cognome;
    }

    public static String prendiUsername() {
        Scanner input = new Scanner(System.in);
        String username;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci l'username: ");
            username = input.next();
            if (username != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }
        return username;
    }

    public static String prendiPassword() {
        Scanner input = new Scanner(System.in);
        String password;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci la password: ");
            password = input.next();
            if (password != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }
        return password;
    }
}
