package it.uniroma2.dicii.bd.model.dao;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.domain.Scheda;
import it.uniroma2.dicii.bd.model.domain.User;
import it.uniroma2.dicii.bd.model.utils.TablePrinter;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SchedaDao implements GenericProcedureDAO<Scheda>{

    public String procedure;

    @Override
    public Scheda execute(Object... params) throws DAOException, SQLException {
        procedure = (String) params[0];
        User user = (User) params[1];
        String cf = user.getCf();

        switch (procedure) {

            case ("VisualizzaAttiva"):

                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call visualizza_schedaattiva(?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf);

                    if(callableStatement.execute()){
                        ResultSet resultSet = callableStatement.getResultSet();
                        printSched(resultSet, "SingolaArchiv");
                    }

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case ("VisualizzaArchiviate"):

                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call visualizza_schedearchiviate(?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(1, cf);

                    if(callableStatement.execute()){
                        ResultSet resultSet = callableStatement.getResultSet();
                        printSched(resultSet, "ListaSchedeArchiv");
                    }

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }
                break;

            case("VisualizzaSingolaArchiviata"):
                Scheda scheda= (Scheda) params[2];
                java.sql.Date dataEmiss = scheda.getDataEmissione();

                try {

                    Connection connection = ConnectionFactory.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{call visualizza_singolaarchiviata(?,?)}",
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    callableStatement.setString(2, cf);
                    callableStatement.setString(1, String.valueOf(dataEmiss));

                    if(callableStatement.execute()){
                        ResultSet resultSet = callableStatement.getResultSet();
                        printSched(resultSet, "SingolaArchiv");
                    }

                } catch (SQLException e) {
                    throw new DAOException("Errore: " + e.getMessage());
                }

                break;

        }
        return null;
    }

    private void printSched(ResultSet resultSet, String procedure) throws SQLException {
        resultSet.last();
        int numresult = resultSet.getRow();
        int column = resultSet.getMetaData().getColumnCount();
        resultSet.beforeFirst();
        int i = 1;
        int j = 1;

        switch (procedure) {

            case("ListaSchedeArchiv"):
                ArrayList<String> arrayList = new ArrayList<String>();
                TablePrinter tablePrinter = new TablePrinter();
                tablePrinter.setShowVerticalLines(true);
                tablePrinter.setHeaders("Data Inizio", "Data Fine");
                while (column >= j) {
                    while (numresult >= i) {
                        if (resultSet.next()) {
                            arrayList.add(0, resultSet.getString(1));
                            arrayList.add(1, resultSet.getString(2));
                            i++;
                        }
                        tablePrinter.addRow(arrayList.get(0), arrayList.get(1));
                    }
                    j++;
                }
                System.out.println("");
                System.out.println("");
                System.out.println("");
                tablePrinter.print();
                break;

            case("SingolaArchiv"):
                ArrayList<String> arrayList2 = new ArrayList<String>();
                TablePrinter tablePrinter2 = new TablePrinter();
                tablePrinter2.setShowVerticalLines(true);
                tablePrinter2.setHeaders("Data", "Serie", "Ripetizioni", "Indice", "Macchinario", "Nome Esercizio");
                while (column >= j) {
                    while (numresult >= i) {
                        if (resultSet.next()) {
                            arrayList2.add(0, resultSet.getString(1));
                            arrayList2.add(1, resultSet.getString(2));
                            arrayList2.add(2, resultSet.getString(3));
                            arrayList2.add(3, resultSet.getString(4));
                            arrayList2.add(4, resultSet.getString(5));
                            arrayList2.add(5, resultSet.getString(6));
                            i++;
                        }
                        tablePrinter2.addRow(arrayList2.get(0), arrayList2.get(1), arrayList2.get(2), arrayList2.get(3), arrayList2.get(4), arrayList2.get(5));
                    }
                    j++;
                }
                System.out.println("");
                System.out.println("");
                System.out.println("");
                tablePrinter2.print();
                break;


        }
    }
}
