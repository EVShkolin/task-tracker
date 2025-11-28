package ru.kpfu.tasktracker.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.dto.ExceptionDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;

@WebFilter("/*")
public class GlobalExceptionHandler extends HttpFilter {

    private ObjectMapper mapper;

    @Override
    public void init() {
        this.mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (IdenticalPasswordException |
                 UsernameAlreadyExistsException |
                 InvalidParameterException |
                 TaskAssignmentException |
                 TaskStatusUpdateException e) {
            ExceptionDto exceptionDto = new ExceptionDto(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage(),
                    null
            );
            handleException(exceptionDto, res);
        } catch (ObjectNotFoundException e) {
            ExceptionDto exceptionDto = new ExceptionDto(
                    HttpServletResponse.SC_NOT_FOUND,
                    e.getMessage(),
                    null
            );
            handleException(exceptionDto, res);
        } catch (ValidationException e) {
            ExceptionDto exceptionDto = new ExceptionDto(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage(),
                    e.getErrors()
            );
            handleException(exceptionDto, res);
        }

    }


    private void handleException(ExceptionDto exceptionDto, HttpServletResponse res) throws IOException {
        res.setStatus(exceptionDto.getStatusCode());
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String exceptionJson = mapper.writeValueAsString(exceptionDto);
        PrintWriter writer = res.getWriter();
        writer.print(exceptionJson);
        writer.flush();
    }
}
