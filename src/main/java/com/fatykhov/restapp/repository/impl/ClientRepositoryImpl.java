package com.fatykhov.restapp.repository.impl;

import com.fatykhov.restapp.dbConfigAndConnection.DbConnection;
import com.fatykhov.restapp.entity.Client;
import com.fatykhov.restapp.repository.ClientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientRepositoryImpl implements ClientRepository {
    private static final String GET_ALL_CLIENTS_SQL = "SELECT * FROM Client";
    private static final String GET_CLIENT_BY_ID_SQL = "SELECT * FROM Client WHERE id = ?";
    private static final String SAVE_CLIENT_SQL = "INSERT INTO Client(name) VALUES (?)";
    private static final String UPDATE_CLIENT_SQL = "UPDATE Client SET name=? WHERE id=?";
    private static final String REMOVE_CLIENT_SQL = "DELETE FROM Client WHERE id=?";

    private final DbConnection dbConnection;

    public ClientRepositoryImpl() {
        dbConnection = new DbConnection();
    }

    public ClientRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clientList = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_CLIENTS_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client();
                client.setId(resultSet.getLong(1));
                client.setName(resultSet.getString(2));
                clientList.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientList;
    }

    @Override
    public Client findOne(Long id) {
        Client client = new Client();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CLIENT_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setId(resultSet.getLong(1));
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

            preparedStatement.setString(1, client.getName());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                Long clientId = generatedKeys.getLong(1);
                client.setId(clientId);
            } else {
                throw new SQLException("Error of obtaining the generated key for Client");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return client;
    }

    @Override
    public Client update(Long id, Client updatedClient) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT_SQL)) {

            preparedStatement.setString(1, updatedClient.getName());
            preparedStatement.setLong(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        updatedClient.setId(id);
        return updatedClient;
    }

    @Override
    public boolean remove(Long id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_CLIENT_SQL)) {

            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();

            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
