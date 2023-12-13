package com.fatykhov.restapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.service.ClientService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "ClientServlet", value = "/clients/*")
public class ClientServlet extends HttpServlet {
    private static final String CONNECTION_TYPE = "application/json";
    private final ClientService service = new ClientService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType(CONNECTION_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            List<ClientDto> allClientsDto = service.getAll();
            String json = mapper.writeValueAsString(allClientsDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            ClientDto clientDto = service.getById(id);

            if (clientDto != null) {
                String json = mapper.writeValueAsString(clientDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(json);
            } else {
                Map<String, Object> errorMap = Map.of(
                        "errorCode", HttpServletResponse.SC_NOT_FOUND,
                        "errorMessage", String.format("Client with id = %d is not found", id)
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
            ClientDto clientFromJson = mapper.readValue(readJson(br), ClientDto.class);

            ClientDto clientDto = service.save(clientFromJson);

            String json = mapper.writeValueAsString(clientDto);
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
            long id = Long.parseLong(stringId);

            ClientDto clientDtoCheck = service.getById(id);

            if (clientDtoCheck != null) {
                BufferedReader br = req.getReader();
                ClientDto clientFromJson = mapper.readValue(readJson(br), ClientDto.class);

                ClientDto clientDto = service.update(id, clientFromJson);

                String json = mapper.writeValueAsString(clientDto);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(json);
            } else {
                Map<String, Object> errorMap = Map.of(
                        "errorCode", HttpServletResponse.SC_NOT_FOUND,
                        "errorMessage", String.format("Client with id = %d is not found", id)
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
            long id = Long.parseLong(stringId);

            ClientDto clientDtoCheck = service.getById(id);

            if (clientDtoCheck != null) {
                boolean isDeleted = service.remove(id);

                Map<String, Object> messageMap = Map.of(
                        "status", HttpServletResponse.SC_OK,
                        "message", String.format("Client with id = %d remove status = %b", id, isDeleted)
                );
                String jsonMessage = mapper.writeValueAsString(messageMap);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(jsonMessage);
            } else {
                Map<String, Object> errorMap = Map.of(
                        "errorCode", HttpServletResponse.SC_NOT_FOUND,
                        "errorMessage", String.format("Client with id = %d is not found", id)
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
