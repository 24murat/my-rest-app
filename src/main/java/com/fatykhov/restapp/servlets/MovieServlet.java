package com.fatykhov.restapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatykhov.restapp.dto.ActorMovieDto;
import com.fatykhov.restapp.dto.MovieDto;
import com.fatykhov.restapp.service.impl.MovieServiceImpl;
import com.fatykhov.restapp.util.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MovieServlet", value = "/movies/*")
public class MovieServlet extends HttpServlet {
    private final MovieServiceImpl service = new MovieServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<MovieDto> allMoviesDto = service.getAll();
            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, allMoviesDto);
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            MovieDto movieDto = service.getById(id);

            if (movieDto.getId() == null) {
                ServletUtils.sendNotFound(resp, id, "Movie");
                return;
            }

            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, movieDto);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            ServletUtils.sendBadRequest(resp, "Wrong path. To add new movie path should be empty");
        } else {
            BufferedReader br = req.getReader();
            ActorMovieDto actorMovieFromJson = mapper.readValue(ServletUtils.readJson(br), ActorMovieDto.class);
            MovieDto movieDto = service.save(actorMovieFromJson.getMovieDto(), actorMovieFromJson.getActorsId());

            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, movieDto);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtils.sendBadRequest(resp, "Wrong path. To edit movies info provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            MovieDto movieDtoCheck = service.getById(id);

            if (movieDtoCheck.getId() == null) {
                ServletUtils.sendNotFound(resp, id, "Movie");
                return;
            }

            BufferedReader br = req.getReader();
            MovieDto movieFromJson = mapper.readValue(ServletUtils.readJson(br), MovieDto.class);
            MovieDto movieDto = service.update(id, movieFromJson);

            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, movieDto);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtils.sendBadRequest(resp, "Wrong path. To delete movie provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            MovieDto movieDtoCheck = service.getById(id);

            if (movieDtoCheck.getId() == null) {
                ServletUtils.sendNotFound(resp, id, "Movie");
                return;
            }

            boolean isDeleted = service.remove(id);

            Map<String, Object> messageMap = Map.of(
                    "status", HttpServletResponse.SC_OK,
                    "message", String.format("Movie with id = %d delete status = %b", id, isDeleted)
            );

            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, messageMap);
        }
    }
}
