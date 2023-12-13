package com.fatykhov.restapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.service.ActorService;
import com.fatykhov.restapp.util.ResponseUtils;
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
    private final ActorService service = new ActorService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<ActorDto> allActorsDto = service.getAll();
            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, allActorsDto);
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            ActorDto actorDto = service.getById(id);

            if (actorDto.getId() == null) {
                ResponseUtils.sendNotFound(resp, id, "Actor");
                return;
            }

            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, actorDto);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            ResponseUtils.sendBadRequest(resp, "Wrong path. To add new actor path should be empty");
        } else {
            BufferedReader br = req.getReader();
            ActorDto actorFromJson = mapper.readValue(readJson(br), ActorDto.class);
            ActorDto actorDto = service.save(actorFromJson);

            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, actorDto);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtils.sendBadRequest(resp, "Wrong path. To edit actors info provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            ActorDto actorDtoCheck = service.getById(id);

            if (actorDtoCheck.getId() == null) {
                ResponseUtils.sendNotFound(resp, id, "Actor");
                return;
            }

            BufferedReader br = req.getReader();
            ActorDto actorFromJson = mapper.readValue(readJson(br), ActorDto.class);
            ActorDto actorDto = service.update(id, actorFromJson);

            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, actorDto);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtils.sendBadRequest(resp, "Wrong path. To delete actor provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            ActorDto actorDtoCheck = service.getById(id);

            if (actorDtoCheck.getId() == null) {
                ResponseUtils.sendNotFound(resp, id, "Actor");
                return;
            }

            boolean isDeleted = service.remove(id);

            Map<String, Object> messageMap = Map.of(
                    "status", HttpServletResponse.SC_OK,
                    "message", String.format("Actor with id = %d delete status = %b", id, isDeleted)
            );

            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, messageMap);
        }
    }

    private String readJson(BufferedReader reader) {
        return reader.lines().collect(Collectors.joining());
    }
}
