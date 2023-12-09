package com.fatykhov.restapi.repository;

import com.fatykhov.restapp.dbConfigAndConnection.DbConnection;
import com.fatykhov.restapp.entity.Movie;
import com.fatykhov.restapp.repository.MovieRepository;
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
class MovieRepositoryTest {

    @Mock
    private DbConnection dbConnection;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatementMovie;
    @Mock
    private PreparedStatement preparedStatementActorMovie;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private MovieRepository movieRepository;
    @Spy
    private MovieRepository spyRepository;

    private Movie movieExpected;

    @BeforeEach
    void setup() {
        movieRepository = new MovieRepository(dbConnection);
        spyRepository = spy(movieRepository);
        movieExpected = new Movie();
        movieExpected.setId(1);
        movieExpected.setClientId(1);
        movieExpected.setTitle("TestMovie");
    }

    @Test
    void findAllTest() {
        try {
            Field sqlField = MovieRepository.class.getDeclaredField("GET_ALL_MOVIES_SQL");
            sqlField.setAccessible(true);
            String getAllMoviesSql = (String) sqlField.get(movieRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatementMovie);
            when(preparedStatementMovie.executeQuery()).thenReturn(resultSet);

            List<Movie> movieList = movieRepository.findAll();

            assertNotNull(movieList);
            verify(connection).prepareStatement(eq(getAllMoviesSql));
            verify(preparedStatementMovie).executeQuery();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findOneTest() {
        try {
            Field sqlField = MovieRepository.class.getDeclaredField("GET_MOVIE_BY_ID_SQL");
            sqlField.setAccessible(true);
            String getMovieByIdSql = (String) sqlField.get(movieRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatementMovie);
            when(preparedStatementMovie.executeQuery()).thenReturn(resultSet);
            when(spyRepository.findOne(1)).thenReturn(movieExpected);

            Movie movie = spyRepository.findOne(1);

            assertNotNull(movie);
            assertEquals(1, movie.getId());
            assertEquals(1, movie.getClientId());
            assertEquals("TestMovie", movie.getTitle());
            verify(connection).prepareStatement(eq(getMovieByIdSql));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void saveTest() {
        try {
            Field sqlFieldMovie = MovieRepository.class.getDeclaredField("SAVE_MOVIE_SQL");
            sqlFieldMovie.setAccessible(true);
            String saveMovieSql = (String) sqlFieldMovie.get(movieRepository);

            Field sqlFieldActorMovie = MovieRepository.class.getDeclaredField("SAVE_ACTOR_MOVIE_SQL");
            sqlFieldActorMovie.setAccessible(true);
            String saveActorMovieSql = (String) sqlFieldActorMovie.get(movieRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(eq(saveMovieSql), eq(Statement.RETURN_GENERATED_KEYS)))
                    .thenReturn(preparedStatementMovie);
            when(preparedStatementMovie.getGeneratedKeys()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);

            when(connection.prepareStatement(eq(saveActorMovieSql))).thenReturn(preparedStatementActorMovie);

            List<Integer> actorsId = List.of(1);

            Movie savedMovie = movieRepository.save(movieExpected, actorsId);

            assertNotNull(savedMovie);
            verify(connection).prepareStatement(eq(saveMovieSql), eq(Statement.RETURN_GENERATED_KEYS));
            verify(preparedStatementMovie).setInt(1, movieExpected.getClientId());
            verify(preparedStatementMovie).setString(2, movieExpected.getTitle());
            verify(preparedStatementMovie).executeUpdate();

            verify(connection).prepareStatement(eq(saveActorMovieSql));
            for (Integer actorId : actorsId) {
                verify(preparedStatementActorMovie).setInt(1, actorId);
                verify(preparedStatementActorMovie).setInt(2, savedMovie.getId());
                verify(preparedStatementActorMovie).addBatch();
            }
            verify(preparedStatementActorMovie).executeBatch();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTest() {
        try {
            Field sqlField = MovieRepository.class.getDeclaredField("UPDATE_MOVIE_SQL");
            sqlField.setAccessible(true);
            String updateMovieSql = (String) sqlField.get(movieRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatementMovie);

            Movie updatedMovie = new Movie();
            updatedMovie.setId(1);
            updatedMovie.setClientId(2);
            updatedMovie.setTitle("UpdatedTestMovie");

            Movie result = movieRepository.update(1, updatedMovie);

            assertNotNull(result);
            assertEquals(1, result.getId());
            assertEquals(2, result.getClientId());
            assertEquals("UpdatedTestMovie", result.getTitle());
            verify(connection).prepareStatement(eq(updateMovieSql));
            verify(preparedStatementMovie).executeUpdate();
            verify(preparedStatementMovie).setInt(eq(1), eq(2));
            verify(preparedStatementMovie).setString(eq(2), eq("UpdatedTestMovie"));
            verify(preparedStatementMovie).setInt(eq(3), eq(1));
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeTest() {
        try {
            Field sqlField = MovieRepository.class.getDeclaredField("REMOVE_MOVIE_SQL");
            sqlField.setAccessible(true);
            String removeMovieSql = (String) sqlField.get(movieRepository);

            when(dbConnection.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatementMovie);
            when(preparedStatementMovie.executeUpdate()).thenReturn(1);

            boolean result = movieRepository.remove(1);

            assertTrue(result);
            verify(connection).prepareStatement(eq(removeMovieSql));
            verify(preparedStatementMovie).setInt(eq(1), eq(1));
            verify(preparedStatementMovie).executeUpdate();
        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
