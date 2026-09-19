package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.dao.ConnectionFactory;
import it.uniroma2.dicii.bd.model.dao.PtHomeScreenDAO;
import it.uniroma2.dicii.bd.model.dao.PtOperationsDao;
import it.uniroma2.dicii.bd.model.dao.SegreteriaOperationsDao;
import it.uniroma2.dicii.bd.model.domain.*;
import it.uniroma2.dicii.bd.view.PTHomeScreenView;
import it.uniroma2.dicii.bd.view.SegreteriaHomeScreenView;

import java.lang.reflect.GenericSignatureFormatError;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class SegreteriaController {

    public void start(Credentials cred) throws DAOException, SQLException, ParseException {
        try{
            ConnectionFactory.changeRole(Role.SEGRETERIA);

        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        chooseOperation();
    }

    private void chooseOperation() {

        while(true){

            int choice;

            choice = SegreteriaHomeScreenView.showHomeScreen();

            switch(choice){

                case 1 -> aggiungiEsercizio();
                case 2 -> aggiungiUtente();
                case 3 -> aggiungiPersonalTrainer();
                case 4 -> exit();

            }
        }


    }

    private void aggiungiEsercizio() {
        String macchinario;
        macchinario = SegreteriaHomeScreenView.prendiNomeMacchinario();
        Esercizio esercizio = new Esercizio(macchinario);

        try{
            new SegreteriaOperationsDao().execute("AggiungiEsercizio", esercizio);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }


    }

    private void aggiungiUtente() {
        String cf;
        String nome;
        String cognome;
        String username;
        String password;

        cf = SegreteriaHomeScreenView.prendicf();
        nome = SegreteriaHomeScreenView.prendiNome();
        cognome= SegreteriaHomeScreenView.prendiCognome();
        username= SegreteriaHomeScreenView.prendiUsername();
        password= SegreteriaHomeScreenView.prendiPassword();

        User user= new User();
        user.setCf(cf);
        user.setNome(nome);
        user.setCognome(cognome);
        user.setUsername(username);

        GeneralUser generalUser = new GeneralUser();
        generalUser.setUsername(username);
        generalUser.setPassword(password);

        try{
            new SegreteriaOperationsDao().execute("AggiungiUtente", user, generalUser);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }

    }

    private void aggiungiPersonalTrainer() {
        String cf;
        String nome;
        String cognome;
        String username;
        String password;

        cf = SegreteriaHomeScreenView.prendicf();
        nome = SegreteriaHomeScreenView.prendiNome();
        cognome= SegreteriaHomeScreenView.prendiCognome();
        username= SegreteriaHomeScreenView.prendiUsername();
        password= SegreteriaHomeScreenView.prendiPassword();

        GeneralUser generalUser = new GeneralUser();
        generalUser.setUsername(username);
        generalUser.setPassword(password);

        PT pt = new PT();
        pt.setCf(cf);
        pt.setNome(nome);
        pt.setCognome(cognome);
        pt.setUsername(username);

        try{
            new SegreteriaOperationsDao().execute("AggiungiPersonalTrainer", pt, generalUser);

        }catch (SQLException | DAOException e){
            throw new RuntimeException(e);
        }


    }

    private void exit() {
        System.out.println("Grazie per aver usato l'applicazione!");
        System.exit(0);
    }

}
