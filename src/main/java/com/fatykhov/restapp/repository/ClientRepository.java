package com.fatykhov.restapp.repository;

import com.fatykhov.restapp.dbConfigAndConnection.DbConnection;
import com.fatykhov.restapp.entity.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {
    private static final String GET_ALL_CLIENTS_SQL = "SELECT * FROM Client";
    private static final String GET_CLIENT_BY_ID_SQL = "SELECT * FROM Client WHERE id = ?";
    private static final String SAVE_CLIENT_SQL = "INSERT INTO Client(name) VALUES (?)";
    private static final String UPDATE_CLIENT_SQL = "UPDATE Client SET name=? WHERE id=?";
    private static final String REMOVE_CLIENT_SQL = "DELETE FROM Client WHERE id=?";

    private final DbConnection dbConnection;

    public ClientRepository() {
        dbConnection = new DbConnection();
    }

    public ClientRepository(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Client> findAll() {
        List<Client> clientList = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CLIENTS_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getInt(1));
                client.setName(resultSet.getString(2));
                clientList.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientList;
    }

    public Client findOne(int id) {
        Client client = new Client();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENT_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                client.setId(resultSet.getInt(1));
                client.setName(resultSet.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    public Client save(Client client) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_CLIENT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            connection.setAutoCommit(true);

            preparedStatement.setString(1, client.getName());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int clientId = generatedKeys.getInt(1);
                client.setId(clientId); // Обновление id у объекта client
            } else {
                throw new SQLException("Error of obtaining the generated key for Client");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    public Client update(int id, Client updatedClient) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT_SQL)) {

            connection.setAutoCommit(true);

            preparedStatement.setString(1, updatedClient.getName());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        updatedClient.setId(id);
        return updatedClient;
    }

    public boolean remove(int id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_CLIENT_SQL)) {

            connection.setAutoCommit(true);

            preparedStatement.setInt(1, id);
            // executeUpdate() запишет в result количество измененных строк после SQL запроса
            int result = preparedStatement.executeUpdate();

            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
