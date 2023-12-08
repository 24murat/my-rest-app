package com.fatykhov.restapp.repository;


import com.fatykhov.restapp.dbConfigAndConnection.DbConnection;
import com.fatykhov.restapp.entity.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private static final String GET_ALL_MOVIES_SQL = "SELECT * FROM Movie";
    private static final String GET_MOVIE_BY_ID_SQL = "SELECT * FROM Movie WHERE id = ?";
    private static final String SAVE_MOVIE_SQL = "INSERT INTO Movie(client_id, title) VALUES (?, ?)";
    private static final String SAVE_ACTOR_MOVIE_SQL = "INSERT INTO Actor_Movie(actor_id, movie_id) VALUES (?, ?)";
    private static final String UPDATE_MOVIE_SQL = "UPDATE Movie SET client_id=?, title=? WHERE id=?";
    private static final String REMOVE_MOVIE_SQL = "DELETE FROM Movie WHERE id=?";

    private final DbConnection dbConnection;

    public MovieRepository() {
        dbConnection = new DbConnection();
    }

    public MovieRepository(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<Movie> findAll() {
        List<Movie> movieList = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_MOVIES_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getInt(1));
                movie.setClientId(resultSet.getInt(2));
                movie.setTitle(resultSet.getString(3));
                movieList.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movieList;
    }

    public Movie findOne(int id) {
        Movie movie = new Movie();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_MOVIE_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                movie.setId(resultSet.getInt(1));
                movie.setClientId(resultSet.getInt(2));
                movie.setTitle(resultSet.getString(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movie;
    }

    public Movie save(Movie movie, List<Integer> actorsId) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatementMovie = connection.prepareStatement(SAVE_MOVIE_SQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatementActorMovie = connection.prepareStatement(SAVE_ACTOR_MOVIE_SQL)) {

            connection.setAutoCommit(true);

            preparedStatementMovie.setInt(1, movie.getClientId());
            preparedStatementMovie.setString(2, movie.getTitle());
            preparedStatementMovie.executeUpdate();

            ResultSet generatedKeys = preparedStatementMovie.getGeneratedKeys();
            if (generatedKeys.next()) {
                int movieId = generatedKeys.getInt(1);
                movie.setId(movieId);

                for (Integer actorId : actorsId) {
                    preparedStatementActorMovie.setInt(1, actorId);
                    preparedStatementActorMovie.setInt(2, movieId);
                    preparedStatementActorMovie.addBatch();
                }
                preparedStatementActorMovie.executeBatch();
            } else {
                throw new SQLException("Error of obtaining the generated key for Movie");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movie;
    }

    public Movie update(int id, Movie updatedMovie) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MOVIE_SQL)) {

            connection.setAutoCommit(true);

            preparedStatement.setInt(1, updatedMovie.getClientId());
            preparedStatement.setString(2, updatedMovie.getTitle());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        updatedMovie.setId(id);
        return updatedMovie;
    }

    public boolean remove(int id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_MOVIE_SQL)) {

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
