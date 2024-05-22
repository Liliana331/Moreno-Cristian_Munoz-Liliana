package dh.backend.dao.impl;

import dh.backend.dao.IDao;
import dh.backend.db.H2Connection;
import dh.backend.model.Odontologo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OdontologoDaoH2 implements IDao<Odontologo> {

    private static Logger LOGGER = Logger.getLogger(OdontologoDaoH2.class);
    private static String SQL_INSERT = "INSERT INTO ODONTOLOGOS VALUES(DEFAULT, ?, ?, ?)";
    private static String SQL_SELECT_ALL = "SELECT * FROM ODONTOLOGOS";

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        Connection connection = null;
        Odontologo odontologoADevolver = null;
        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, odontologo.getNumeroMatricula());
            preparedStatement.setString(2, odontologo.getNombre());
            preparedStatement.setString(3, odontologo.getApellido());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                Integer numeroMatricula = odontologo.getNumeroMatricula();
                String nombre = odontologo.getNombre();
                String apellido = odontologo.getApellido();

                odontologoADevolver = new Odontologo(id, numeroMatricula, nombre, apellido);
            }

            connection.commit();
            connection.setAutoCommit(true);
            LOGGER.info("Se registro el odontologo: " + odontologoADevolver);

        }catch (Exception e){
            if(connection != null){
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error("Error al realizar rollback" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            LOGGER.error("Error al registrar Odontologo" + e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.info(e.getMessage());
                e.printStackTrace();
            }
        }
        return odontologoADevolver;
    }

    @Override
    public List<Odontologo> buscarTodos() {
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList<>();
        try{
            connection = H2Connection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);

            while (resultSet.next()){
                Integer id = resultSet.getInt(1);
                Integer numeroMatricula = resultSet.getInt(2);
                String nombre = resultSet.getString(3);
                String apellido = resultSet.getString(4);

                Odontologo nuevoOdontologo = new Odontologo(id, numeroMatricula, nombre, apellido);

                LOGGER.info("Odontologo listado: " + nuevoOdontologo);
                odontologos.add(nuevoOdontologo);
            }


        }catch (Exception e){
            LOGGER.error("Error al listar Odontologos" + e.getMessage());
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.info(e.getMessage());
                e.printStackTrace();
            }
        }
        return odontologos;
    }
}
