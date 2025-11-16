package ru.kpfu.tasktracker.controller.page;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.dto.project.ProjectDto;
import ru.kpfu.tasktracker.service.ProjectService;

import java.io.IOException;

@WebServlet("/project/*")
public class ProjectPageServlet extends HttpServlet {

    private ProjectService projectService;

    @Override
    public void init() throws ServletException {
        this.projectService = (ProjectService) getServletContext().getAttribute("projectService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String[] parts = pathInfo.split("/");
        if (parts.length != 2) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Long projectId = Long.parseLong(parts[1]);
        ProjectDto project = projectService.findById(projectId);
        req.setAttribute("project", project);
        req.getRequestDispatcher("/project-page.jsp").forward(req, resp);
    }
}
