package it.uniroma2.dicii.bd.model.dao;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.domain.Esercizio;
import it.uniroma2.dicii.bd.model.domain.GeneralUser;
import it.uniroma2.dicii.bd.model.domain.PT;
import it.uniroma2.dicii.bd.model.domain.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SegreteriaOperationsDao implements GenericProcedureDAO{

    public String procedure;

    @Override
    public Object execute(Object... params) throws DAOException, SQLException {

        procedure = (String) params[0];

        switch (procedure) {

            case ("AggiungiEsercizio"):
                Esercizio esercizio = (Esercizio) params[1];

                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call aggiungi_esercizio(?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, esercizio.getNome());
                    callableStatement.execute();

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;


            case ("AggiungiUtente"):
                User user = (User) params[1];
                GeneralUser generalUser = (GeneralUser) params[2];
                String cf = user.getCf();
                String nome = user.getNome();
                String cognome = user.getCognome();
                String username = user.getUsername();
                String pass = generalUser.getPassword();

                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call aggiungi_utente(?,?,?,?,?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf);
                    callableStatement.setString(2, nome);
                    callableStatement.setString(3, cognome);
                    callableStatement.setString(4, username);
                    callableStatement.setString(5, pass);
                    callableStatement.execute();

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case ("AggiungiPersonalTrainer"):
                PT pt = (PT) params[1];
                GeneralUser generalUser2 = (GeneralUser) params[2];
                String cf2 = pt.getCf();
                String nome2 = pt.getNome();
                String cognome2 = pt.getCognome();
                String username2 = pt.getUsername();
                String pass2 = generalUser2.getPassword();

                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call aggiungi_personaltrainer(?,?,?,?,?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf2);
                    callableStatement.setString(2, nome2);
                    callableStatement.setString(3, cognome2);
                    callableStatement.setString(4, username2);
                    callableStatement.setString(5, pass2);
                    callableStatement.execute();

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

        }
        return null;
    }
}
