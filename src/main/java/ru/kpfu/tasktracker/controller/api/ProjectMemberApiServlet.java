package ru.kpfu.tasktracker.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.controller.validator.PathValidator;
import ru.kpfu.tasktracker.controller.validator.ProjectMemberValidator;
import ru.kpfu.tasktracker.dto.projectmember.MemberCreateDto;
import ru.kpfu.tasktracker.dto.projectmember.RoleUpdateRequest;
import ru.kpfu.tasktracker.service.ProjectMemberService;

import java.io.IOException;

@WebServlet("/api/v1/members/*")
public class ProjectMemberApiServlet extends HttpServlet {

    private ProjectMemberService projectMemberService;
    private ProjectMemberValidator projectMemberValidator;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        this.projectMemberService = (ProjectMemberService) getServletContext().getAttribute("projectMemberService");
        this.projectMemberValidator = (ProjectMemberValidator) getServletContext().getAttribute("projectMemberValidator");
        this.mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path != null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        MemberCreateDto member = mapper.readValue(req.getInputStream(), MemberCreateDto.class);
        projectMemberService.save(member);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        Long id = PathValidator.getIdFromPath(path);
        RoleUpdateRequest request = mapper.readValue(req.getInputStream(), RoleUpdateRequest.class);
        projectMemberValidator.validateRoleUpdateRequest(request);
        projectMemberService.updateRole(id, request);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        Long id = PathValidator.getIdFromPath(path);
        projectMemberService.delete(id);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
