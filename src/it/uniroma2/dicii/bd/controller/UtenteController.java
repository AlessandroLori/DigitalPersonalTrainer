package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.dao.AllenamentoDao;
import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.dao.SchedaDao;
import it.uniroma2.dicii.bd.model.dao.UtenteHomeScreenDAO;
import it.uniroma2.dicii.bd.model.domain.*;
import it.uniroma2.dicii.bd.view.UtenteHomeScreenView;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

public class UtenteController {

    private User user;

        public void start(Credentials cred) throws DAOException, SQLException, ParseException {
            try{
                ConnectionFactory.changeRole(Role.UTENTE);
                UtenteHomeScreenDAO utenteHomeScreenDAO = UtenteHomeScreenDAO.getInstance();
                user = utenteHomeScreenDAO.execute(cred.getUsername());

            }catch (SQLException | DAOException e){
                throw new RuntimeException(e);
            }

            chooseOperation();
        }

        public void chooseOperation() throws DAOException, SQLException, ParseException {

            while(true){

                int choice;

                choice = UtenteHomeScreenView.showHomeScreen();

                switch(choice){

                    case 1 -> iniziaAllenamento();
                    case 2 -> visualizzaSchedaAttiva();
                    case 3 -> visualizzaSchedeArchiviate();
                    case 4 -> visualizzaSingolaSchedaArchiviata();
                    case 5 -> exit();

                }

            }

        }



    private void iniziaAllenamento() throws DAOException, SQLException {

        try{
            new AllenamentoDao().execute("InizioAllenamento", user);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

        while(true) {

            int choice;

            choice = UtenteHomeScreenView.showAllenamentoScreen();

            switch (choice) {

                case 1 -> eseguiEsercizio();
                case 2 -> eseguiSerie();
                case 3 -> visualizzaEserciziMancanti();
                case 4 -> visualizzaSerieMancanti();
                case 5 -> terminaAllenamento();
            }
        }
    }


    private void eseguiEsercizio() {
        String esename;

        esename = UtenteHomeScreenView.esercizioEseguito();
        Esercizio esercizio = new Esercizio();
        esercizio.setNome(esename);

        try{
            new AllenamentoDao().execute("EseguiEsercizio", user, esercizio);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void eseguiSerie() {
        String esename;

        esename = UtenteHomeScreenView.esercizioEseguito();
        Esercizio esercizio = new Esercizio();
        esercizio.setNome(esename);

        try{
            new AllenamentoDao().execute("EseguiSerie", user, esercizio);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }
    }

    private void visualizzaEserciziMancanti() {

        try{
            new AllenamentoDao().execute("VediEsercizi", user);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void visualizzaSerieMancanti() {

        String esename = UtenteHomeScreenView.esercizioVediSerie();
        Esercizio esercizio = new Esercizio();
        esercizio.setNome(esename);
        try{
             new AllenamentoDao().execute("VediSerie", user, esercizio);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }
    }

    private void terminaAllenamento() {

        try{
            new AllenamentoDao().execute("FineAllenamento", user);
            chooseOperation();

        }catch (SQLException | DAOException | ParseException e){
            throw new RuntimeException(e);
        }
    }

    private void visualizzaSchedaAttiva() {

        try{
            new SchedaDao().execute("VisualizzaAttiva", user);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }
    }

    private void visualizzaSchedeArchiviate() {
        try{
            new SchedaDao().execute("VisualizzaArchiviate", user);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }
    }

    private void visualizzaSingolaSchedaArchiviata() throws ParseException {
        java.sql.Date dataEmissione = UtenteHomeScreenView.schedaVediArchiviata();
        Scheda scheda = new Scheda();
        scheda.setDataEmissione(dataEmissione);

        try{
            new SchedaDao().execute("VisualizzaSingolaArchiviata", user, scheda);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }
    }



    private void exit(){
        System.out.println("Grazie per aver usato l'applicazione!");
        System.exit(0);
    }
}
