package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.domain.Credentials;
import it.uniroma2.dicii.bd.view.ApplicationView;

import java.sql.SQLException;
import java.text.ParseException;

public class ApplicationController implements Controller {
    Credentials cred;

    @Override
    public void start() throws DAOException, SQLException, ParseException {
        while(true){

            int choise;
            choise = ApplicationView.showApplicationView();
            switch (choise){

                case 1 -> registration();
                case 2 -> login();
                default -> throw new RuntimeException("Scelta non valida!");
            }
        }
    }

    public void registration(){

        System.out.println("Rivolgiti alla segreteria per registrarti e poter iniziare ad allenarti ");

    }

    public void login() throws DAOException, SQLException, ParseException {

        LoginController loginController = new LoginController();
        loginController.start();
        cred = loginController.getCred();

        if(cred.getRole() == null){
            throw new RuntimeException("Invalid Credentials");
        }

        switch (cred.getRole()){

            case UTENTE -> new UtenteController().start(cred);
            case SEGRETERIA -> new SegreteriaController().start(cred);
            case PT -> new PTController().start(cred);
            default -> throw new RuntimeException("Invalid Credentials");
        }

    }

}
