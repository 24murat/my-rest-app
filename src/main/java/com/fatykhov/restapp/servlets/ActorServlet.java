package com.fatykhov.restapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.service.ActorService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ActorServlet", value = "/actors/*")
public class ActorServlet extends HttpServlet {
    private static final String CONNECTION_TYPE = "application/json";
    private final ActorService service = new ActorService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(CONNECTION_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            List<ActorDto> allActorsDto = service.getAllActors();
            String json = mapper.writeValueAsString(allActorsDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } else {
            String stringId = pathInfo.substring(1);
            int id = Integer.parseInt(stringId);
            ActorDto actorDto = service.getActorById(id);

            if (actorDto != null) {
                String json = mapper.writeValueAsString(actorDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(json);
            } else {
                Map<String, Object> errorMap = Map.of(
                        "errorCode", HttpServletResponse.SC_NOT_FOUND,
                        "errorMessage", String.format("Actor with id = %d is not found", id)
                );
                String jsonError = mapper.writeValueAsString(errorMap);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(jsonError);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            BufferedReader br = req.getReader();
            ActorDto actorFromJson = mapper.readValue(readJson(br), ActorDto.class);

            ActorDto actorDto = service.saveActor(actorFromJson);

            String json = mapper.writeValueAsString(actorDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(json);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

            ActorDto actorDtoCheck = service.getActorById(id);

            if (actorDtoCheck != null) {
                BufferedReader br = req.getReader();
                ActorDto actorFromJson = mapper.readValue(readJson(br), ActorDto.class);

                ActorDto actorDto = service.updateActor(id, actorFromJson);

                String json = mapper.writeValueAsString(actorDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(json);
            } else {
                Map<String, Object> errorMap = Map.of(
                        "errorCode", HttpServletResponse.SC_NOT_FOUND,
                        "errorMessage", String.format("Actor with id = %d is not found", id)
                );
                String jsonError = mapper.writeValueAsString(errorMap);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(jsonError);
            }

        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

            ActorDto actorDtoCheck = service.getActorById(id);

            if (actorDtoCheck != null) {
                boolean isDeleted = service.removeActor(id);

                Map<String, Object> messageMap = Map.of(
                        "status", HttpServletResponse.SC_OK,
                        "message", String.format("Actor with id = %d remove status = %b", id, isDeleted)
                );
                String jsonMessage = mapper.writeValueAsString(messageMap);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(jsonMessage);
            } else {
                Map<String, Object> errorMap = Map.of(
                        "errorCode", HttpServletResponse.SC_NOT_FOUND,
                        "errorMessage", String.format("Actor with id = %d is not found", id)
                );
                String jsonError = mapper.writeValueAsString(errorMap);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(jsonError);
            }
        }
    }

    private String readJson(BufferedReader reader) {
        return reader.lines().collect(Collectors.joining());
    }
}
