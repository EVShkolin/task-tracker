package ru.kpfu.tasktracker.controller.page;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.dto.invitation.InvitationDto;
import ru.kpfu.tasktracker.dto.user.UserDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.service.InvitationService;
import ru.kpfu.tasktracker.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfilePageServlet extends HttpServlet {

    private UserService userService;
    private InvitationService invitationService;

    @Override
    public void init() throws ServletException {
        this.userService = (UserService) getServletContext().getAttribute("userService");
        this.invitationService = (InvitationService) getServletContext().getAttribute("invitationService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProfileDto user = (UserProfileDto) req.getAttribute("user");
        List<InvitationDto> invitations = invitationService.findAllByUserId(user.id());

        req.setAttribute("invitations", invitations);
        req.getRequestDispatcher("/profile.jsp").forward(req, resp);
    }

}
