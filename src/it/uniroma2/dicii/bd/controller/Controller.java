package it.uniroma2.dicii.bd.controller;

import it.uniroma2.dicii.bd.exception.DAOException;

import java.sql.SQLException;
import java.text.ParseException;

public interface Controller {

    void start() throws DAOException, SQLException, ParseException;
}
