package it.uniroma2.dicii.bd.model.dao;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.domain.PT;
import it.uniroma2.dicii.bd.model.domain.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PtHomeScreenDAO implements GenericProcedureDAO<PT>{

    private static PtHomeScreenDAO instance = null;

    private PtHomeScreenDAO(){}

    public static PtHomeScreenDAO getInstance() {

        if(instance == null){
            instance = new PtHomeScreenDAO();
        }
        return instance;
    }

    @Override
    public PT execute(Object... params) throws DAOException, SQLException {
        String username = (String) params[0];
        PT pt = null;

        try{

            Connection connection = ConnectionFactory.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call cf_pt(?)}");
            callableStatement.setString(1, username);

            if(callableStatement.execute()){

                ResultSet resultSet = callableStatement.getResultSet();

                if(resultSet.next()){

                    pt = new PT();
                    pt.setCf(resultSet.getString(1));
                }


            }

        }catch(SQLException e){

            throw new DAOException("Errore obtaining user: " + e.getMessage());

        }

        return pt;
    }
}
