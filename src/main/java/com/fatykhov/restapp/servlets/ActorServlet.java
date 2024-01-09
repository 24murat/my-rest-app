package com.fatykhov.restapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.service.impl.ActorServiceImpl;
import com.fatykhov.restapp.util.ServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ActorServlet", value = "/actors/*")
public class ActorServlet extends HttpServlet {
    private final ActorServiceImpl service = new ActorServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<ActorDto> allActorsDto = service.getAll();
            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, allActorsDto);
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            ActorDto actorDto = service.getById(id);

            if (actorDto.getId() == null) {
                ServletUtils.sendNotFound(resp, id, "Actor");
                return;
            }

            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, actorDto);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            ServletUtils.sendBadRequest(resp, "Wrong path. To add new actor path should be empty");
        } else {
            BufferedReader br = req.getReader();
            ActorDto actorFromJson = mapper.readValue(ServletUtils.readJson(br), ActorDto.class);
            ActorDto actorDto = service.save(actorFromJson);

            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, actorDto);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtils.sendBadRequest(resp, "Wrong path. To edit actors info provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            ActorDto actorDtoCheck = service.getById(id);

            if (actorDtoCheck.getId() == null) {
                ServletUtils.sendNotFound(resp, id, "Actor");
                return;
            }

            BufferedReader br = req.getReader();
            ActorDto actorFromJson = mapper.readValue(ServletUtils.readJson(br), ActorDto.class);
            ActorDto actorDto = service.update(id, actorFromJson);

            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, actorDto);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ServletUtils.sendBadRequest(resp, "Wrong path. To delete actor provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            ActorDto actorDtoCheck = service.getById(id);

            if (actorDtoCheck.getId() == null) {
                ServletUtils.sendNotFound(resp, id, "Actor");
                return;
            }

            boolean isDeleted = service.remove(id);

            Map<String, Object> messageMap = Map.of(
                    "status", HttpServletResponse.SC_OK,
                    "message", String.format("Actor with id = %d delete status = %b", id, isDeleted)
            );

            ServletUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, messageMap);
        }
    }
}
