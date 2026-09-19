package it.uniroma2.dicii.bd.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UtenteHomeScreenView {


    public static int showHomeScreen() {
        int choice;
        Scanner input = new Scanner(System.in);

        while(true){
            System.out.println("");
            System.out.println("Cosa vuoi fare?");
            System.out.println("1) Inizia Allenamento");
            System.out.println("2) Visualizza la tua scheda attiva");
            System.out.println("3) Visualizza tutte le tue schede archiviate");
            System.out.println("4) Visualizza una scheda archiviata");
            System.out.println("5) Esci");
            System.out.println("");
            System.out.println("Inserisci il codice");
            choice = input.nextInt();

            if(choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 5){
                break;
            }
            System.out.println("Operazione invalida!");
        }
        return choice;
    }

    public static int showAllenamentoScreen(){
        int choice;
        Scanner input = new Scanner(System.in);
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("Cova vuoi fare?");
            System.out.println("1) Esegui Esercizio");
            System.out.println("2) Esegui Serie");
            System.out.println("3) Visualizza Esercizi Mancanti");
            System.out.println("4) Visualizza Serie Mancanti");
            System.out.println("5) Termina Allenamento");
            System.out.println("");
            System.out.println("Inserisci il codice: ");
            choice = input.nextInt();
            if (choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 5){
                break;
            }
            System.out.println("Operazione invalida!");
        }

        return choice;
    }

    public static String esercizioEseguito(){
        Scanner input = new Scanner(System.in);
        String nomese;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Quale esercizio stai eseguendo?");
            System.out.println("");
            System.out.println("Inserisci il nome: ");
            nomese = input.next();
            if (nomese != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }

        return nomese;
    }

    public static String esercizioVediSerie(){
        Scanner input = new Scanner(System.in);
        String nomese;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Di quale esercizio vuoi vedere le serie mancanti?");
            System.out.println("");
            System.out.println("Inserisci il nome: ");
            nomese = input.next();
            if (nomese != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }

        return nomese;
    }

    public static java.sql.Date schedaVediArchiviata() throws ParseException {
        Scanner input = new Scanner(System.in);
        java.util.Date dataEmissione;
        String str;

        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Quale scheda archiviata vuoi visionare?");
            System.out.println("");
            System.out.println("Inserisci la data di emissione: yy-mm-gg");
            str = input.next();

            if (str != null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                dataEmissione = dateFormat.parse(str);

                break;
            }
            System.out.println("Operazione invalida!");
        }

        return new java.sql.Date(dataEmissione.getTime());
    }

}
