package com.fatykhov.restapp.repository;

import com.fatykhov.restapp.dbConfigAndConnection.DbConnection;
import com.fatykhov.restapp.entity.Movie;
import com.fatykhov.restapp.repository.interfaces.MovieRepositoryInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository implements MovieRepositoryInterface {
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

    @Override
    public List<Movie> findAll() {
        List<Movie> movieList = new ArrayList<>();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_MOVIES_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Movie movie = new Movie();
                movie.setId(resultSet.getLong(1));
                movie.setClientId(resultSet.getLong(2));
                movie.setTitle(resultSet.getString(3));
                movieList.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movieList;
    }

    @Override
    public Movie findOne(long id) {
        Movie movie = new Movie();
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_MOVIE_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                movie.setId(resultSet.getLong(1));
                movie.setClientId(resultSet.getLong(2));
                movie.setTitle(resultSet.getString(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return movie;
    }

    public Movie save(Movie movie, List<Long> actorsId) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatementMovie = connection.prepareStatement(SAVE_MOVIE_SQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement preparedStatementActorMovie = connection.prepareStatement(SAVE_ACTOR_MOVIE_SQL)) {

            preparedStatementMovie.setLong(1, movie.getClientId());
            preparedStatementMovie.setString(2, movie.getTitle());
            preparedStatementMovie.executeUpdate();

            ResultSet generatedKeys = preparedStatementMovie.getGeneratedKeys();
            if (generatedKeys.next()) {
                long movieId = generatedKeys.getLong(1);
                movie.setId(movieId);

                for (Long actorId : actorsId) {
                    preparedStatementActorMovie.setLong(1, actorId);
                    preparedStatementActorMovie.setLong(2, movieId);
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

    @Override
    public Movie update(long id, Movie updatedMovie) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_MOVIE_SQL)) {

            preparedStatement.setLong(1, updatedMovie.getClientId());
            preparedStatement.setString(2, updatedMovie.getTitle());
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        updatedMovie.setId(id);
        return updatedMovie;
    }

    @Override
    public boolean remove(long id) {
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_MOVIE_SQL)) {

            preparedStatement.setLong(1, id);
            int result = preparedStatement.executeUpdate();

            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
