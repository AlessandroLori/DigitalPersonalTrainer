package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.dao.*;
import it.uniroma2.dicii.bd.model.domain.*;
import it.uniroma2.dicii.bd.view.PTHomeScreenView;
import it.uniroma2.dicii.bd.view.UtenteHomeScreenView;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.System.exit;

public class PTController {

    private PT pt;

    public void start(Credentials cred) throws DAOException, SQLException, ParseException {
        try{
            ConnectionFactory.changeRole(Role.PT);
            PtHomeScreenDAO ptHomeScreenDAO = PtHomeScreenDAO.getInstance();
            pt = ptHomeScreenDAO.execute(cred.getUsername());

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

        chooseOperation();
    }

    private void chooseOperation() throws ParseException {
        while(true){

            int choice;

            choice = PTHomeScreenView.showHomeScreen();

            switch(choice){

                case 1 -> visualizzaUtenti();
                case 2 -> visualizzaEsercizi();
                case 3 -> archiviaScheda();
                case 4 -> creaScheda();
                case 5 -> inserisciEseScheda();
                case 6 -> visualizzaSchedaUtente();
                case 7 -> generaReport();
                case 8 -> exit();

            }
        }
    }


    private void visualizzaUtenti() {

        try{
            new PtOperationsDao().execute("VisualizzaUtenti", pt);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void visualizzaEsercizi() {

        try{
            new PtOperationsDao().execute("VisualizzaEsercizi", pt);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void archiviaScheda() {
        String cfuser;
        cfuser = PTHomeScreenView.prendiCfUser();
        User user = new User();
        user.setCf(cfuser);

        try{
            new PtOperationsDao().execute("ArchiviaScheda", pt, user);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void creaScheda() throws ParseException {

        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        Date sqlDate = Date.valueOf(formattedDate);
        Scheda scheda = new Scheda();
        scheda.setDataEmissione(sqlDate);

        String cfuser;
        cfuser = PTHomeScreenView.prendiCfUser();
        User user = new User();
        user.setCf(cfuser);

        try{
            new PtOperationsDao().execute("CreaScheda", pt, user, scheda);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void inserisciEseScheda() {

        String cfuser;
        int numeroserie, numeroripetizioni, indice;
        String nomeesercizio, nomemacchinario;

        cfuser = PTHomeScreenView.prendiCfUser();
        User user = new User();
        user.setCf(cfuser);

        numeroserie = PTHomeScreenView.prendiNumeroSerie();
        numeroripetizioni = PTHomeScreenView.prendiNumeroRipetizioni();
        indice = PTHomeScreenView.prendiPosizioneIndice();
        nomeesercizio = PTHomeScreenView.prendiNomeEsercizio();
        nomemacchinario = PTHomeScreenView.prendiNomeMacchinario();

        Composta composta = new Composta();
        composta.setNumeroSerie(numeroserie);
        composta.setNumeroRipetizioni(numeroripetizioni);
        composta.setIndice(indice);
        composta.setNomeEsercizio(nomeesercizio);
        composta.setMacchinario(nomemacchinario);

        try{
            new PtOperationsDao().execute("InserisciEsercizioScheda", pt, user, composta);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void visualizzaSchedaUtente() {

        String cfuser;
        cfuser = PTHomeScreenView.prendiCfUser();
        User user = new User();
        user.setCf(cfuser);

        try{
            new PtOperationsDao().execute("VisualizzaSchedaUtente", pt, user);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void generaReport() throws ParseException {

        java.sql.Date dataInizio, dataFine;

        dataInizio = PTHomeScreenView.prendiDataInizio();
        dataFine = PTHomeScreenView.prendiDataFine();
        Scheda scheda = new Scheda();
        scheda.setDataEmissione(dataInizio);
        scheda.setDataScad(dataFine);

        try{
            new PtOperationsDao().execute("GeneraReport", pt, scheda);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void exit() {

        System.out.println("Grazie per aver usato l'applicazione!");
        System.exit(0);
    }

}
