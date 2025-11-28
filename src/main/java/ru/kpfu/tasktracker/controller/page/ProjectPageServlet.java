package ru.kpfu.tasktracker.controller.page;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.dto.project.ProjectDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.service.ProjectMemberService;
import ru.kpfu.tasktracker.service.ProjectService;

import java.io.IOException;

@WebServlet("/projects/*")
public class ProjectPageServlet extends HttpServlet {

    private ProjectService projectService;
    private ProjectMemberService projectMemberService;

    @Override
    public void init() throws ServletException {
        this.projectService = (ProjectService) getServletContext().getAttribute("projectService");
        this.projectMemberService = (ProjectMemberService) getServletContext().getAttribute("projectMemberService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long projectId = Long.valueOf(pathParts[1]);
        String projectMode = null;
        if (pathParts.length == 3) {
            projectMode = pathParts[2];
        }

        ProjectDto project = projectService.findById(projectId);
        UserProfileDto user = (UserProfileDto) req.getAttribute("user");
        Long memberId = projectMemberService.findByUserIdAndProjectId(user.id(), projectId);
        req.setAttribute("memberId", memberId);
        req.setAttribute("project", project);
        if ("kanban".equals(projectMode)) {
            req.getRequestDispatcher("/project-kanban.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/project-main.jsp").forward(req, resp);
        }
    }
}
