package it.uniroma2.dicii.bd.model.dao;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.domain.Composta;
import it.uniroma2.dicii.bd.model.domain.PT;
import it.uniroma2.dicii.bd.model.domain.Scheda;
import it.uniroma2.dicii.bd.model.domain.User;
import it.uniroma2.dicii.bd.model.utils.TablePrinter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PtOperationsDao implements GenericProcedureDAO <PT> {

    public String procedure;

    @Override
    public PT execute(Object... params) throws DAOException, SQLException {
        procedure = (String) params[0];
        PT pt = (PT) params[1];
        String cf = pt.getCf();

        switch (procedure) {
            case ("VisualizzaUtenti"):

                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call visualizza_clienti(?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf);
                    if(callableStatement.execute()){
                        ResultSet resultSet = callableStatement.getResultSet();
                        printResult(resultSet, "VisualUtenti");

                    }

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case ("VisualizzaEsercizi"):

                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call visualizza_esercizi()}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    if(callableStatement.execute()){
                        ResultSet resultSet = callableStatement.getResultSet();
                        printResult(resultSet, "VisualEse");

                    }

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case ("VisualizzaSchedaUtente"):
                User user = (User) params[2];
                String cfuser = user.getCf();
                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call pt_visualizzascheda(?,?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf);
                    callableStatement.setString(2, cfuser);
                    if(callableStatement.execute()){
                        ResultSet resultSet = callableStatement.getResultSet();
                        printResult(resultSet, "VisualSched");

                    }

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case ("CreaScheda"):
                User user2 = (User) params[2];
                String cfuser1 = user2.getCf();

                Scheda scheda = (Scheda) params[3];
                java.sql.Date dataEmiss= scheda.getDataEmissione();


                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call crea_scheda(?,?,?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(3, cf);
                    callableStatement.setString(2, cfuser1);
                    callableStatement.setString(1, String.valueOf(dataEmiss));
                    callableStatement.execute();

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case ("ArchiviaScheda"):
                User user3 = (User) params[2];
                String cfuser2 = user3.getCf();
                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call archivia_scheda(?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cfuser2);
                    callableStatement.execute();

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case ("InserisciEsercizioScheda"):

                User user4 = (User) params[2];
                String cfuser3 = user4.getCf();

                Composta composta = (Composta) params[3];
                int numeroserie = composta.getNumeroSerie();
                int numeroripetizioni = composta.getNumeroRipetizioni();
                int indice = composta.getIndice();
                String nomeesercizio = composta.getNomeEsercizio();
                String nomemacchinario = composta.getMacchinario();


                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call aggiungi_scheda_esercizio(?,?,?,?,?,?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setInt(1, numeroserie);
                    callableStatement.setInt(2, numeroripetizioni);
                    callableStatement.setInt(3, indice);
                    callableStatement.setString(4, nomeesercizio);
                    callableStatement.setString(5, nomemacchinario);
                    callableStatement.setString(6, cfuser3);
                    callableStatement.execute();

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case ("GeneraReport"):
                Scheda scheda2 = (Scheda) params[2];

                java.sql.Date dataInizio = scheda2.getDataEmissione();
                java.sql.Date dataFine = scheda2.getDataScad();
                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call generareport(?,?,?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf);
                    callableStatement.setString(2, dataInizio.toString());
                    callableStatement.setString(3, dataFine.toString());
                    if(callableStatement.execute()){
                        ResultSet resultSet = callableStatement.getResultSet();
                        printResult(resultSet, "VisualReport");

                    }

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

        }
        return null;
    }

    private void printResult(ResultSet resultSet, String procedure) throws SQLException {
        resultSet.last();
        int numresult = resultSet.getRow();
        int column = resultSet.getMetaData().getColumnCount();
        resultSet.beforeFirst();
        int i = 1;
        int j = 1;

        switch (procedure) {

            case("VisualUtenti"):
                ArrayList<String> arrayList = new ArrayList<String>();
                TablePrinter tablePrinter = new TablePrinter();
                tablePrinter.setShowVerticalLines(true);
                tablePrinter.setHeaders("Codice Fiscale", "Nome", "Cognome");
                while (column >= j) {
                    while (numresult >= i) {
                        if (resultSet.next()) {
                            arrayList.add(0, resultSet.getString(1));
                            arrayList.add(1, resultSet.getString(2));
                            arrayList.add(2, resultSet.getString(3));
                            i++;
                        }
                        tablePrinter.addRow(arrayList.get(0), arrayList.get(1), arrayList.get(2));
                    }
                    j++;
                }
                System.out.println("");
                System.out.println("");
                System.out.println("");
                tablePrinter.print();
                break;

            case("VisualEse"):
                ArrayList<String> arrayList3 = new ArrayList<String>();
                TablePrinter tablePrinter3 = new TablePrinter();
                tablePrinter3.setShowVerticalLines(true);
                tablePrinter3.setHeaders("Esercizi");
                while (column >= j) {
                    while (numresult >= i) {
                        if (resultSet.next()) {
                            arrayList3.add(0, resultSet.getString(1));
                            i++;
                        }
                        tablePrinter3.addRow(arrayList3.get(0));
                    }
                    j++;
                }
                System.out.println("");
                System.out.println("");
                System.out.println("");
                tablePrinter3.print();
                break;

            case("VisualSched"):
                ArrayList<String> arrayList2 = new ArrayList<String>();
                TablePrinter tablePrinter2 = new TablePrinter();
                tablePrinter2.setShowVerticalLines(true);
                tablePrinter2.setHeaders("Data", "Utente", "Serie", "Ripetizioni", "Indice", "Macchinario", "Nome Esercizio");
                while (column >= j) {
                    while (numresult >= i) {
                        if (resultSet.next()) {
                            arrayList2.add(0, resultSet.getString(1));
                            arrayList2.add(1, resultSet.getString(2));
                            arrayList2.add(2, resultSet.getString(3));
                            arrayList2.add(3, resultSet.getString(4));
                            arrayList2.add(4, resultSet.getString(5));
                            arrayList2.add(5, resultSet.getString(6));
                            arrayList2.add(6, resultSet.getString(7));
                            i++;
                        }
                        tablePrinter2.addRow(arrayList2.get(0), arrayList2.get(1), arrayList2.get(2), arrayList2.get(3), arrayList2.get(4), arrayList2.get(5), arrayList2.get(6));
                    }
                    j++;
                }
            System.out.println("");
            System.out.println("");
            System.out.println("");
            tablePrinter2.print();
            break;

            case("VisualReport"):
                ArrayList<String> arrayList4 = new ArrayList<String>();
                TablePrinter tablePrinter4 = new TablePrinter();
                tablePrinter4.setShowVerticalLines(true);
                tablePrinter4.setHeaders("CF", "Nome", "Cognome", "Data Scheda", "Sessione", "Completamento", "Durata");
                while (column >= j) {
                    while (numresult >= i) {
                        if (resultSet.next()) {
                            arrayList4.add(0, resultSet.getString(1));
                            arrayList4.add(1, resultSet.getString(2));
                            arrayList4.add(2, resultSet.getString(3));
                            arrayList4.add(3, resultSet.getString(4));
                            arrayList4.add(4, resultSet.getString(5));
                            arrayList4.add(5, resultSet.getString(6));
                            arrayList4.add(6, resultSet.getString(7));
                            i++;
                        }
                        tablePrinter4.addRow(arrayList4.get(0), arrayList4.get(1), arrayList4.get(2), arrayList4.get(3), arrayList4.get(4), arrayList4.get(5), arrayList4.get(6));
                    }
                    j++;
                }
                System.out.println("");
                System.out.println("");
                System.out.println("");
                tablePrinter4.print();
                break;

        }
    }
}
