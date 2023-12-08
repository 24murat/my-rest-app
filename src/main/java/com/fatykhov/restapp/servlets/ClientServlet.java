package com.fatykhov.restapp.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.service.ClientService;
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

// Эта аннотация указывает контейнеру сервлетов, что класс ClientServlet является сервлетом,
// и он должен обрабатывать запросы по пути /clients/*.
// MarketServlet расширяет базовый класс HttpServlet, что позволяет ему обрабатывать HTTP-запросы.
@WebServlet(name = "ClientServlet", value = "/clients/*")
public class ClientServlet extends HttpServlet {
    private final ClientService service = new ClientService();
    // Объект Jackson ObjectMapper для преобразования объектов в JSON и обратно.
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String CONNECTION_TYPE = "application/json";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONNECTION_TYPE);

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            // Получаем список всех клиентов с использованием сервиса
            List<ClientDto> allClientsDto = service.getAllClients();
            // Преобразуем этот список в строку JSON.
            String json = mapper.writeValueAsString(allClientsDto);
            // Устанавливаем статус ответа на 200 OK.
            resp.setStatus(HttpServletResponse.SC_OK);
            // Записываем строку JSON в тело ответа, которая будет возвращена клиенту.
            resp.getWriter().write(json);
        } else {
            String stringId = pathInfo.substring(1);
            int id = Integer.parseInt(stringId);
            ClientDto clientDto = service.getClientById(id);

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
            // Преобразуем JSON в объект ClientDto
            ClientDto clientFromJson = mapper.readValue(readJson(br), ClientDto.class);

            ClientDto clientDto = service.saveClient(clientFromJson);

            String json = mapper.writeValueAsString(clientDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(json);
        }
    }

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

            ClientDto clientDtoCheck = service.getClientById(id);

            if (clientDtoCheck != null) {
                // Читаем тело POST запроса
                BufferedReader br = req.getReader();
                // Преобразуем JSON в объект ClientDto
                ClientDto clientFromJson = mapper.readValue(readJson(br), ClientDto.class);

                ClientDto clientDto = service.updateClient(id, clientFromJson);

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

            ClientDto clientDtoCheck = service.getClientById(id);

            if (clientDtoCheck != null) {
                boolean isDeleted = service.removeClient(id);

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

    private String readJson(BufferedReader reader) throws IOException {
        return reader.lines().collect(Collectors.joining());
    }
}
