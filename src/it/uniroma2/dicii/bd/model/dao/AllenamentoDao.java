package it.uniroma2.dicii.bd.model.dao;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.domain.Esercizio;
import it.uniroma2.dicii.bd.model.domain.User;
import it.uniroma2.dicii.bd.model.utils.TablePrinter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AllenamentoDao implements GenericProcedureDAO <User> {

    public String procedure;

    @Override
    public User execute(Object... params) throws DAOException, SQLException {
        procedure = (String) params[0];
        User user = (User) params[1];
        String cf = user.getCf();

        switch (procedure){
            case("InizioAllenamento"):

                try{

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call inizio_allenamento(?)}");
                    callableStatement.setString(1, cf);
                    callableStatement.execute();

                }catch(SQLException e){
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case("EseguiEsercizio"):
                Esercizio esercizio = (Esercizio) params[2];
                String nomese = esercizio.getNome();
                try{

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call esegui_esercizio(?,?)}");
                    callableStatement.setString(1, nomese);
                    callableStatement.setString(2, cf);
                    callableStatement.execute();

                }catch(SQLException e){
                    throw new DAOException("Errore: " + e.getMessage());
                }

                break;

            case("EseguiSerie"):
                Esercizio esercizio2 = (Esercizio) params[2];
                String nomese2 = esercizio2.getNome();
                try{

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call esegui_serieeserc(?,?)}");
                    callableStatement.setString(2, nomese2);
                    callableStatement.setString(1, cf);
                    callableStatement.execute();

                }catch(SQLException e){
                    throw new DAOException("Errore: " + e.getMessage());
                }

                break;

            case("VediEsercizi"):

                try{

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call visualizza_esercizimancanti(?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf);
                    callableStatement.execute();

                    if (callableStatement.execute()) {
                        ResultSet resultSet = callableStatement.getResultSet();
                        printEser(resultSet, "VediEser");
                    }

                }catch(SQLException e){
                    throw new DAOException("Errore: " + e.getMessage());
                }

                break;

            case("VediSerie"):
                Esercizio esercizio3 = (Esercizio) params[2];
                String nomese3 = esercizio3.getNome();
                try{

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call visualizza_seriemancanti(?,?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf);
                    callableStatement.setString(2, nomese3);
                    callableStatement.execute();

                    if (callableStatement.execute()) {
                        ResultSet resultSet = callableStatement.getResultSet();
                        printEser(resultSet, "VediSer");

                    }

                }catch(SQLException e){
                    throw new DAOException("Errore: " + e.getMessage());
                }

                break;

            case("FineAllenamento"):

                try{

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call fine_allenamento(?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf);
                    callableStatement.execute();

                }catch(SQLException e){
                    throw new DAOException("Errore: " + e.getMessage());
                }

                break;

        }

        return null;
    }

    private void printEser(ResultSet resultSet, String procedure) throws SQLException {
        resultSet.last();
        int numresult = resultSet.getRow();
        resultSet.beforeFirst();
        int column = resultSet.getMetaData().getColumnCount();
        int i = 1;
        int j = 1;

        switch (procedure) {
            case ("VediEser"):
                ArrayList<String> arrayList2 = new ArrayList<String>();
                TablePrinter tablePrinter2 = new TablePrinter();
                tablePrinter2.setShowVerticalLines(true);
                tablePrinter2.setHeaders("Macchinario", "Serie", "Ripetizioni", "Indice");
                while (column >= j) {
                    while (numresult >= i) {
                        if (resultSet.next()) {
                            arrayList2.add(0, resultSet.getString(1));
                            arrayList2.add(1, resultSet.getString(2));
                            arrayList2.add(2, resultSet.getString(3));
                            arrayList2.add(3, resultSet.getString(4));
                            i++;
                        }
                        tablePrinter2.addRow(arrayList2.get(0), arrayList2.get(1), arrayList2.get(2), arrayList2.get(3));
                    }
                    j++;
                }
                System.out.println("");
                tablePrinter2.print();
                break;

            case ("VediSer"):
                ArrayList<String> arrayList = new ArrayList<String>();
                TablePrinter tablePrinter = new TablePrinter();
                tablePrinter.setShowVerticalLines(true);
                tablePrinter.setHeaders("Serie Rimanenti");
                while (column >= j) {
                    while (numresult >= i) {
                        if (resultSet.next()) {
                            arrayList.add(0, resultSet.getString(1));

                            i++;
                        }
                        tablePrinter.addRow(arrayList.get(0));
                    }
                    j++;
                }
                System.out.println("");
                tablePrinter.print();
                break;
        }
    }
}
