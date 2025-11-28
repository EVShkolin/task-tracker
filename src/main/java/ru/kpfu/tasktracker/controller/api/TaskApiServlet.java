package ru.kpfu.tasktracker.controller.api;

import ch.qos.logback.core.encoder.JsonEscapeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.controller.validator.PathValidator;
import ru.kpfu.tasktracker.dto.task.StatusUpdateRequest;
import ru.kpfu.tasktracker.dto.task.TaskDto;
import ru.kpfu.tasktracker.exception.InvalidPathException;
import ru.kpfu.tasktracker.service.TaskService;

import java.io.IOException;

@WebServlet("/api/v1/tasks/*")
public class TaskApiServlet extends HttpServlet {

    private TaskService taskService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.taskService = (TaskService) getServletContext().getAttribute("taskService");
        this.objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        Long taskId = PathValidator.getIdFromPath(path);
        TaskDto taskDto = taskService.findById(taskId);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(taskDto));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            TaskDto taskDto = objectMapper.readValue(req.getReader(), TaskDto.class);
            taskDto = taskService.save(taskDto);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(taskDto));
        } else if (PathValidator.isValidAssignmentPath(path)) {
            String[] pathParts = path.split("/");
            Long taskId = Long.valueOf(pathParts[1]);
            Long memberId = Long.valueOf(pathParts[3]);
            taskService.addAssignee(taskId, memberId);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } else {
            throw new InvalidPathException("Invalid assignment path");
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (PathValidator.isValidStatusUpdatePath(path)) {
            String[] pathParts = path.split("/");
            Long taskId = Long.valueOf(pathParts[1]);
            StatusUpdateRequest request = objectMapper.readValue(req.getReader(), StatusUpdateRequest.class);
            taskService.updateStatus(taskId, request.cardId());
            return;
        }

        Long taskId = PathValidator.getIdFromPath(path);
        TaskDto taskDto = objectMapper.readValue(req.getReader(), TaskDto.class);
        taskDto = taskService.update(taskId, taskDto);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(taskDto));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getPathInfo();

        if (PathValidator.isValidUnassignPath(path)) {
            String[] pathParts = path.split("/");
            Long taskId = Long.valueOf(pathParts[1]);
            Long memberId = Long.valueOf(pathParts[3]);
            taskService.removeAssignee(taskId, memberId);
            return;
        }

        Long taskId = PathValidator.getIdFromPath(path);
        taskService.delete(taskId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
