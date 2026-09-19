package it.uniroma2.dicii.bd;

import it.uniroma2.dicii.bd.controller.ApplicationController;
import it.uniroma2.dicii.bd.exception.DAOException;

import java.sql.SQLException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws DAOException, SQLException, ParseException {
        ApplicationController applicationController = new ApplicationController();
        applicationController.start();
    }
}





