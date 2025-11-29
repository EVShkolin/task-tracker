package ru.kpfu.tasktracker.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.controller.validator.InvitationValidator;
import ru.kpfu.tasktracker.controller.validator.PathValidator;
import ru.kpfu.tasktracker.dto.invitation.InvitationDto;
import ru.kpfu.tasktracker.service.InvitationService;

import java.io.IOException;

@WebServlet("/api/v1/invitations/*")
public class InvitationApiServlet extends HttpServlet {

    private InvitationService invitationService;
    private ObjectMapper mapper;
    private InvitationValidator validator;

    @Override
    public void init() throws ServletException {
        this.invitationService = (InvitationService) getServletContext().getAttribute("invitationService");
        this.mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        this.validator = (InvitationValidator) getServletContext().getAttribute("invitationValidator");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path != null && !path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        InvitationDto invitation = mapper.readValue(req.getReader(), InvitationDto.class);
        validator.validate(invitation);

        invitationService.save(invitation);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(invitation));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        Long invitationId = PathValidator.getIdFromPath(path);

        InvitationDto invitation = mapper.readValue(req.getReader(), InvitationDto.class);
        validator.validate(invitation);

        invitation = invitationService.updateStatus(invitationId, invitation);
        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(invitation));
    }
}
