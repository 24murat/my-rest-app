package com.fatykhov.restapp.repository.impl;

import com.fatykhov.restapp.dbConfig.ConnectionPool;
import com.fatykhov.restapp.entity.Actor;
import com.fatykhov.restapp.repository.ActorRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ActorRepositoryImpl implements ActorRepository {
    private static final String GET_ALL_ACTORS_SQL = "SELECT * FROM Actor";
    private static final String GET_ACTOR_BY_ID_SQL = "SELECT * FROM Actor WHERE id = ?";
    private static final String SAVE_ACTOR_SQL = "INSERT INTO Actor(name) VALUES (?)";
    private static final String UPDATE_ACTOR_SQL = "UPDATE Actor SET name=? WHERE id=?";
    private static final String REMOVE_ACTOR_SQL = "DELETE FROM Actor WHERE id=?";

    private final ConnectionPool connectionPool;

    public ActorRepositoryImpl() {
        connectionPool = new ConnectionPool();
    }

    public ActorRepositoryImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public List<Actor> findAll() {
        List<Actor> actorList = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ACTORS_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Actor actor = new Actor();
                actor.setId(resultSet.getLong(1));
                actor.setName(resultSet.getString(2));
                actorList.add(actor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actorList;
    }

    @Override
    public Actor findOne(Long id) {
        Actor actor = new Actor();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACTOR_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                actor.setId(resultSet.getLong(1));
                actor.setName(resultSet.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actor;
    }

    public Actor save(Actor actor) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_ACTOR_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, actor.getName());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                Long actorId = generatedKeys.getLong(1);
                actor.setId(actorId);
            } else {
                throw new SQLException("Error of obtaining the generated key for Actor");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return actor;
    }

    @Override
    public Actor update(Long id, Actor updatedActor) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACTOR_SQL)) {

            preparedStatement.setString(1, updatedActor.getName());
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        updatedActor.setId(id);
        return updatedActor;
    }

    @Override
    public boolean remove(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_ACTOR_SQL)) {

            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
