package it.uniroma2.dicii.bd.view;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class PTHomeScreenView {

    public static int showHomeScreen() {
        int choice;
        Scanner input = new Scanner(System.in);

        while(true){
            System.out.println("");
            System.out.println("Cosa vuoi fare?");
            System.out.println("1) Visualizza i tuoi utenti");
            System.out.println( "2) Visualizza esercizi disponibili in palestra");
            System.out.println("3) Archivia Scheda di un utente");
            System.out.println("4) Crea Scheda per utente");
            System.out.println("5) Inserisci esercizio nella scheda");
            System.out.println("6) Visualizza scheda attiva di un utente");
            System.out.println("7) Ottieni report sull'andamento di un utente");
            System.out.println("8) Esci");
            System.out.println("");
            System.out.println("Inserisci il codice");
            choice = input.nextInt();

            if(choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 5 || choice == 6 || choice == 7 || choice == 8){
                break;
            }
            System.out.println("Operazione invalida!");
        }
        return choice;
    }

    public static String prendiCfUser() {

        Scanner input = new Scanner(System.in);
        String cfuser;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Quale utente vuoi selezionare?");
            System.out.println("");
            System.out.println("Inserisci il codice fiscale: ");
            cfuser = input.next();
            if (cfuser != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }

        return cfuser;

    }

    public static int prendiNumeroSerie() {
        Scanner input = new Scanner(System.in);
        int numeroSerie;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci il numero di serie per l'esercizio?");
            System.out.println("");
            System.out.println("Inserisci il numero: ");
            numeroSerie = input.nextInt();
            if (numeroSerie != 0 ){
                break;
            }
            System.out.println("Operazione invalida!");
        }

        return numeroSerie;
    }

    public static int prendiNumeroRipetizioni() {
        Scanner input = new Scanner(System.in);
        int numeroRipetizioni;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci il numero di ripetizioni per serie per l'esercizio?");
            System.out.println("");
            System.out.println("Inserisci il numero: ");
            numeroRipetizioni = input.nextInt();
            if (numeroRipetizioni != 0 ){
                break;
            }
            System.out.println("Operazione invalida!");
        }

        return numeroRipetizioni;

    }

    public static int prendiPosizioneIndice() {
        Scanner input = new Scanner(System.in);
        int posizioneIndice;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci la posizione dell'esercizio nella scheda?");
            System.out.println("");
            System.out.println("Inserisci il numero: ");
            posizioneIndice = input.nextInt();
            if (posizioneIndice != 0 ){
                break;
            }
            System.out.println("Operazione invalida!");
        }

        return posizioneIndice;

    }

    public static String prendiNomeEsercizio() {
        Scanner input = new Scanner(System.in);
        String nomeEsercizio;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Qual'è il nome dell'esercizio?");
            System.out.println("");
            System.out.println("Inserisci il nome: ");
            nomeEsercizio = input.next();
            if (nomeEsercizio != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }

        return nomeEsercizio;

    }

    public static String prendiNomeMacchinario() {
        Scanner input = new Scanner(System.in);
        String nomeEsercizio;
        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Quale macchinario deve usare per questo esercizio?");
            System.out.println("");
            System.out.println("Inserisci il nome: ");
            nomeEsercizio = input.next();
            if (nomeEsercizio != null){
                break;
            }
            System.out.println("Operazione invalida!");
        }

        return nomeEsercizio;

    }

    public static java.sql.Date prendiDataInizio() throws ParseException {

        Scanner input = new Scanner(System.in);
        String str;
        java.util.Date dataInizio;

        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci la data di partenza per generare il report");
            System.out.println("");
            System.out.println("Inserisci una data: ");
            str = input.next();
            if (str != null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dataInizio = dateFormat.parse(str);

                break;
            }
            System.out.println("Operazione invalida!");
        }

        return new java.sql.Date(dataInizio.getTime());

    }

    public static java.sql.Date prendiDataFine() throws ParseException {
        Scanner input = new Scanner(System.in);
        String str;
        java.util.Date dataFine;

        while(true){
            System.out.println("");
            System.out.println("");
            System.out.println("Inserisci la data di conclusione per generare il report");
            System.out.println("");
            System.out.println("Inserisci una data: ");
            str = input.next();
            if (str != null){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dataFine = dateFormat.parse(str);

                break;
            }
            System.out.println("Operazione invalida!");
        }

        return new java.sql.Date(dataFine.getTime());

    }
}
