package com.fatykhov.restapi.repository;

import com.fatykhov.restapp.dbConfigAndConnection.DbConnection;
import com.fatykhov.restapp.entity.Actor;
import com.fatykhov.restapp.repository.ActorRepository;
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
class ActorRepositoryTest {

    @Mock
    private DbConnection dbConnection;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private ActorRepository actorRepository;
    @Spy
    private ActorRepository spyRepository;

    private Actor actorExpected;

    @BeforeEach
    void setup() {
        actorRepository = new ActorRepository(dbConnection);
        spyRepository = spy(actorRepository);
        actorExpected = new Actor();
        actorExpected.setId(1);
        actorExpected.setName("TestActor");
    }

    @Test
    void findAllTest() {
        try {
            Field sqlField = ActorRepository.class.getDeclaredField("GET_ALL_ACTORS_SQL");
            sqlField.setAccessible(true);
            String getAllActorsSql = (String) sqlField.get(actorRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            List<Actor> actorList = actorRepository.findAll();

            assertNotNull(actorList);
            verify(connection).prepareStatement(eq(getAllActorsSql));
            verify(preparedStatement).executeQuery();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findOneTest() {
        try {
            Field sqlField = ActorRepository.class.getDeclaredField("GET_ACTOR_BY_ID_SQL");
            sqlField.setAccessible(true);
            String getActorByIdSql = (String) sqlField.get(actorRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(spyRepository.findOne(1)).thenReturn(actorExpected);

            Actor actor = spyRepository.findOne(1);

            assertNotNull(actor);
            assertEquals(1, actor.getId());
            assertEquals("TestActor", actor.getName());
            verify(connection).prepareStatement(eq(getActorByIdSql));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveTest(){
        try {
            Field sqlField = ActorRepository.class.getDeclaredField("SAVE_ACTOR_SQL");
            sqlField.setAccessible(true);
            String saveActorSql = (String) sqlField.get(actorRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(preparedStatement);
            when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
            when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);

            Actor savedActor = actorRepository.save(actorExpected);

            assertNotNull(savedActor);
            verify(connection).prepareStatement(eq(saveActorSql), eq(Statement.RETURN_GENERATED_KEYS));
            verify(preparedStatement).setString(1, actorExpected.getName());
            verify(preparedStatement).executeUpdate();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTest(){
        try {
            Field sqlField = ActorRepository.class.getDeclaredField("UPDATE_ACTOR_SQL");
            sqlField.setAccessible(true);
            String updateActorSql = (String) sqlField.get(actorRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            Actor updatedActor = new Actor();
            updatedActor.setId(1);
            updatedActor.setName("UpdatedTestActor");

            Actor result = actorRepository.update(1, updatedActor);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals("UpdatedTestActor", result.getName());
            verify(connection).prepareStatement(eq(updateActorSql));
            verify(preparedStatement).executeUpdate();
            verify(preparedStatement).setString(eq(1), eq("UpdatedTestActor"));
            verify(preparedStatement).setLong(eq(2), eq(1L));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeTest(){
        try {
            Field sqlField = ActorRepository.class.getDeclaredField("REMOVE_ACTOR_SQL");
            sqlField.setAccessible(true);
            String removeActorSql = (String) sqlField.get(actorRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            boolean result = actorRepository.remove(1);

            assertTrue(result);
            verify(connection).prepareStatement(eq(removeActorSql));
            verify(preparedStatement).setLong(eq(1), eq(1L));
            verify(preparedStatement).executeUpdate();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
