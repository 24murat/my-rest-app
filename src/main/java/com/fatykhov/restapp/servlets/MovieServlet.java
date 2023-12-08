package com.fatykhov.restapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatykhov.restapp.dto.ActorMovieDto;
import com.fatykhov.restapp.dto.MovieDto;
import com.fatykhov.restapp.service.MovieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Эта аннотация указывает контейнеру сервлетов, что класс MovieServlet является сервлетом,
// и он должен обрабатывать запросы по пути /movies/*.
// MarketServlet расширяет базовый класс HttpServlet, что позволяет ему обрабатывать HTTP-запросы.
@WebServlet(name = "MovieServlet", value = "/movies/*")
public class MovieServlet extends HttpServlet {
    private final MovieService service = new MovieService();
    // Объект Jackson ObjectMapper для преобразования объектов в JSON и обратно.
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String CONNECTION_TYPE = "application/json";

    // Получает фильм по id либо получает все фильмы
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONNECTION_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            // Получаем список всех клиентов с использованием сервиса
            List<MovieDto> allMoviesDto = service.getAllMovies();
            // Преобразуем этот список в строку JSON.
            String json = mapper.writeValueAsString(allMoviesDto);
            // Устанавливаем статус ответа на 200 OK.
            resp.setStatus(HttpServletResponse.SC_OK);
            // Записываем строку JSON в тело ответа, которая будет возвращена клиенту.
            resp.getWriter().write(json);
        } else {
            String stringId = pathInfo.substring(1);
            int id = Integer.parseInt(stringId);
            MovieDto movieDto = service.getMovieById(id);

            if (movieDto != null) {
                String json = mapper.writeValueAsString(movieDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(json);
            } else {
                Map<String, Object> errorMap = Map.of(
                        "errorCode", HttpServletResponse.SC_NOT_FOUND,
                        "errorMessage", String.format("Movie with id = %d is not found", id)
                );
                String jsonError = mapper.writeValueAsString(errorMap);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(jsonError);
            }
        }
    }

    // Добавляет новый фильм
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONNECTION_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            Map<String, Object> errorMap = Map.of(
                    "errorCode", HttpServletResponse.SC_BAD_REQUEST,
                    "errorMessage", "Wrong path"
            );
            String jsonError = mapper.writeValueAsString(errorMap);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(jsonError);
        } else {
            // Читаем тело POST запроса
            BufferedReader br = req.getReader();
            // Преобразуем JSON в объект MovieDto
            ActorMovieDto actorMovieFromJson = mapper.readValue(readJson(br), ActorMovieDto.class);

            MovieDto movieDto = service.saveMovie(actorMovieFromJson.getMovieDto(), actorMovieFromJson.getActorsId());

            String json = mapper.writeValueAsString(movieDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(json);
        }
    }

    // Изменяет существующий фильм с переданным id
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONNECTION_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            Map<String, Object> errorMap = Map.of(
                    "errorCode", HttpServletResponse.SC_BAD_REQUEST,
                    "errorMessage", "Wrong path"
            );
            String jsonError = mapper.writeValueAsString(errorMap);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(jsonError);
        } else {
            String stringId = pathInfo.substring(1);
            int id = Integer.parseInt(stringId);

            MovieDto movieDtoCheck = service.getMovieById(id);

            if (movieDtoCheck != null) {
                // Читаем тело POST запроса
                BufferedReader br = req.getReader();
                // Преобразуем JSON в объект MovieDto
                MovieDto movieFromJson = mapper.readValue(readJson(br), MovieDto.class);

                MovieDto movieDto = service.updateMovie(id, movieFromJson);

                String json = mapper.writeValueAsString(movieDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(json);
            } else {
                Map<String, Object> errorMap = Map.of(
                        "errorCode", HttpServletResponse.SC_NOT_FOUND,
                        "errorMessage", String.format("Movie with id = %d is not found", id)
                );
                String jsonError = mapper.writeValueAsString(errorMap);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(jsonError);
            }
        }
    }

    // Удаляет фильм по id
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONNECTION_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            Map<String, Object> errorMap = Map.of(
                    "errorCode", HttpServletResponse.SC_BAD_REQUEST,
                    "errorMessage", "Wrong path"
            );
            String jsonError = mapper.writeValueAsString(errorMap);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(jsonError);
        } else {
            String stringId = pathInfo.substring(1);
            int id = Integer.parseInt(stringId);

            MovieDto movieDtoCheck = service.getMovieById(id);

            if (movieDtoCheck != null) {
                boolean isDeleted = service.removeMovie(id);

                Map<String, Object> messageMap = Map.of(
                        "status", HttpServletResponse.SC_OK,
                        "message", String.format("Movie with id = %d remove status = %b", id, isDeleted)
                );
                String jsonMessage = mapper.writeValueAsString(messageMap);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(jsonMessage);
            } else {
                Map<String, Object> errorMap = Map.of(
                        "errorCode", HttpServletResponse.SC_NOT_FOUND,
                        "errorMessage", String.format("Movie with id = %d is not found", id)
                );
                String jsonError = mapper.writeValueAsString(errorMap);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(jsonError);
            }
        }
    }

    private String readJson(BufferedReader reader) throws IOException {
        return reader.lines().collect(Collectors.joining());
    }
}
