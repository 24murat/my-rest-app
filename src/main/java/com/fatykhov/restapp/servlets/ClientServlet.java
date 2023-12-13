package com.fatykhov.restapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.service.ClientService;
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

@WebServlet(name = "ClientServlet", value = "/clients/*")
public class ClientServlet extends HttpServlet {
    private final ClientService service = new ClientService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<ClientDto> allClientsDto = service.getAll();
            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, allClientsDto);
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            ClientDto clientDto = service.getById(id);

            if (clientDto.getId() == null) {
                ResponseUtils.sendNotFound(resp, id, "Client");
                return;
            }

            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, clientDto);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            ResponseUtils.sendBadRequest(resp, "Wrong path. To add new client path should be empty");
        } else {
            BufferedReader br = req.getReader();
            ClientDto clientFromJson = mapper.readValue(readJson(br), ClientDto.class);
            ClientDto clientDto = service.save(clientFromJson);

            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_CREATED, clientDto);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtils.sendBadRequest(resp, "Wrong path. To edit clients info provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);

            ClientDto clientDtoCheck = service.getById(id);

            if (clientDtoCheck.getId() == null) {
                ResponseUtils.sendNotFound(resp, id, "Client");
                return;
            }

            BufferedReader br = req.getReader();
            ClientDto clientFromJson = mapper.readValue(readJson(br), ClientDto.class);
            ClientDto clientDto = service.update(id, clientFromJson);

            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, clientDto);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ResponseUtils.sendBadRequest(resp, "Wrong path. To delete client provide id");
        } else {
            String stringId = pathInfo.substring(1);
            long id = Long.parseLong(stringId);
            ClientDto clientDtoCheck = service.getById(id);

            if (clientDtoCheck.getId() == null) {
                ResponseUtils.sendNotFound(resp, id, "Client");
                return;
            }

            boolean isDeleted = service.remove(id);

            Map<String, Object> messageMap = Map.of(
                    "status", HttpServletResponse.SC_OK,
                    "message", String.format("Client with id = %d delete status = %b", id, isDeleted)
            );

            ResponseUtils.sendJsonResponse(resp, HttpServletResponse.SC_OK, messageMap);
        }
    }

    private String readJson(BufferedReader reader) {
        return reader.lines().collect(Collectors.joining());
    }
}
