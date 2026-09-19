package it.uniroma2.dicii.bd.model.dao;

import it.uniroma2.dicii.bd.exception.DAOException;
import it.uniroma2.dicii.bd.model.domain.User;

import java.sql.*;

public class UtenteHomeScreenDAO implements GenericProcedureDAO <User> {

    private static UtenteHomeScreenDAO instance = null;

    private UtenteHomeScreenDAO(){}

    public static UtenteHomeScreenDAO getInstance() {

            if(instance == null){
                instance = new UtenteHomeScreenDAO();
            }
            return instance;
    }

    @Override
    public User execute(Object... params) throws DAOException, SQLException {
        String username = (String) params[0];
        User user = null;

        try{

            Connection connection = ConnectionFactory.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{call cf_user(?)}");
            callableStatement.setString(1, username);

            if(callableStatement.execute()){

                ResultSet resultSet = callableStatement.getResultSet();

                if(resultSet.next()){
                    user = new User();
                    user.setCf(resultSet.getString(1));
                }


            }

        }catch(SQLException e){

            throw new DAOException("Errore obtaining user: " + e.getMessage());

        }

        return user;

    }
}
