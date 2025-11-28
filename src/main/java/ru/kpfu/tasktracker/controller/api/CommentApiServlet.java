package ru.kpfu.tasktracker.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.controller.validator.CommentValidator;
import ru.kpfu.tasktracker.dto.comment.CommentCreateDto;
import ru.kpfu.tasktracker.dto.comment.CommentDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.service.CommentService;

import java.io.IOException;

@WebServlet("/api/v1/comments")
public class CommentApiServlet extends HttpServlet {

    private CommentService commentService;
    private ObjectMapper mapper;
    private CommentValidator commentValidator;

    @Override
    public void init() throws ServletException {
        commentService = (CommentService) getServletContext().getAttribute("commentService");
        mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
        commentValidator = (CommentValidator) getServletContext().getAttribute("commentValidator");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path != null && !path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserProfileDto user = (UserProfileDto) req.getAttribute("user"); ;
        CommentCreateDto commentCreateDto = mapper.readValue(req.getReader(), CommentCreateDto.class);
        commentValidator.validateComment(commentCreateDto);

        CommentDto commentDto = commentService.save(commentCreateDto);
        commentDto.setAuthorName(user.username());
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(commentDto));
    }
}
