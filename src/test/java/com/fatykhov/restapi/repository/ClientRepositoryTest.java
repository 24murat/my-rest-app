package com.fatykhov.restapi.repository;

import com.fatykhov.restapp.dbConfigAndConnection.DbConnection;
import com.fatykhov.restapp.entity.Client;
import com.fatykhov.restapp.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientRepositoryTest {

    @Mock
    private DbConnection dbConnection;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private ClientRepository clientRepository;
    @Spy
    private ClientRepository spyRepository;

    private Client clientExpected;

    @BeforeEach
    void setup() {
        clientRepository = new ClientRepository(dbConnection);
        spyRepository = spy(clientRepository);
        clientExpected = new Client();
        clientExpected.setId(1);
        clientExpected.setName("TestClient");
    }

    @Test
    void findAllTest() {
        try {
            Field sqlField = ClientRepository.class.getDeclaredField("GET_ALL_CLIENTS_SQL");
            sqlField.setAccessible(true);
            String getAllClientsSql = (String) sqlField.get(clientRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            List<Client> clientList = spyRepository.findAll();

            assertNotNull(clientList);
            verify(connection).prepareStatement(eq(getAllClientsSql));
            verify(preparedStatement).executeQuery();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findOneTest() {
        try {
            Field sqlField = ClientRepository.class.getDeclaredField("GET_CLIENT_BY_ID_SQL");
            sqlField.setAccessible(true);
            String getClientByIdSql = (String) sqlField.get(clientRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.findOne(1)).thenReturn(clientExpected);

            Client client = spyRepository.findOne(1);

            assertNotNull(client);
            assertEquals(1, client.getId());
            assertEquals("TestClient", client.getName());
            verify(connection).prepareStatement(eq(getClientByIdSql));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveTest(){
        try {
            Field sqlField = ClientRepository.class.getDeclaredField("SAVE_CLIENT_SQL");
            sqlField.setAccessible(true);
            String saveClientSql = (String) sqlField.get(clientRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(preparedStatement);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);

            Client savedClient = clientRepository.save(clientExpected);

            assertNotNull(savedClient);
            verify(connection).prepareStatement(eq(saveClientSql), eq(Statement.RETURN_GENERATED_KEYS));
            verify(preparedStatement).setString(1, clientExpected.getName());
            verify(preparedStatement).executeUpdate();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTest(){
        try {
            Field sqlField = ClientRepository.class.getDeclaredField("UPDATE_CLIENT_SQL");
            sqlField.setAccessible(true);
            String updateClientSql = (String) sqlField.get(clientRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            Client updatedClient = new Client();
            updatedClient.setId(1);
            updatedClient.setName("UpdatedTestClient");

            Client result = clientRepository.update(1, updatedClient);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("UpdatedTestClient", result.getName());
            verify(connection).prepareStatement(eq(updateClientSql));
            verify(preparedStatement).executeUpdate();
            verify(preparedStatement).setString(eq(1), eq("UpdatedTestClient"));
            verify(preparedStatement).setLong(eq(2), eq(1L));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeTest(){
        try {
            Field sqlField = ClientRepository.class.getDeclaredField("REMOVE_CLIENT_SQL");
            sqlField.setAccessible(true);
            String removeClientSql = (String) sqlField.get(clientRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = clientRepository.remove(1);

            assertTrue(result);
            verify(connection).prepareStatement(eq(removeClientSql));
            verify(preparedStatement).setLong(eq(1), eq(1L));
            verify(preparedStatement).executeUpdate();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

