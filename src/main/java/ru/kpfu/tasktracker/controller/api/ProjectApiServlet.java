package ru.kpfu.tasktracker.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.dto.project.ProjectCreateDto;
import ru.kpfu.tasktracker.dto.project.ProjectDto;
import ru.kpfu.tasktracker.service.ProjectService;

import java.io.IOException;

@WebServlet("/api/v1/projects/*")
public class ProjectApiServlet extends HttpServlet {

    private ProjectService projectService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        this.projectService = (ProjectService) getServletContext().getAttribute("projectService");
        this.objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProjectCreateDto projectDto = objectMapper.readValue(req.getReader(), ProjectCreateDto.class);
        ProjectDto project = projectService.save(projectDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(project));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] parts = path.split("/");
        if (parts.length != 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long projectId = Long.valueOf(parts[1]);
        ProjectCreateDto projectDto = objectMapper.readValue(req.getReader(), ProjectCreateDto.class);
        projectService.update(projectId, projectDto);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String[] parts = path.split("/");
        if (parts.length != 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long projectId = Long.valueOf(parts[1]);
        projectService.delete(projectId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
