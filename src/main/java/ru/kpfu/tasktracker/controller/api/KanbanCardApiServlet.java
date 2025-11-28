package ru.kpfu.tasktracker.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.controller.validator.PathValidator;
import ru.kpfu.tasktracker.dto.task.KanbanCardDto;
import ru.kpfu.tasktracker.service.KanbanCardService;

import java.io.IOException;

@WebServlet("/api/v1/kanban-cards/*")
public class KanbanCardApiServlet extends HttpServlet {

    private KanbanCardService kanbanCardService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.kanbanCardService = (KanbanCardService) getServletContext().getAttribute("kanbanCardService");
        this.objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path != null && !path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        KanbanCardDto kanbanCardDto = objectMapper.readValue(req.getInputStream(), KanbanCardDto.class);
        kanbanCardDto = kanbanCardService.create(kanbanCardDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(kanbanCardDto));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        Long id = PathValidator.getIdFromPath(path);
        KanbanCardDto kanbanCardDto = objectMapper.readValue(req.getInputStream(), KanbanCardDto.class);
        kanbanCardDto = kanbanCardService.update(id, kanbanCardDto);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(kanbanCardDto));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        Long id = PathValidator.getIdFromPath(path);
        kanbanCardService.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
