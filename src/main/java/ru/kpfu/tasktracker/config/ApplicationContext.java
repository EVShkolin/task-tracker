package ru.kpfu.tasktracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.controller.validator.CommentValidator;
import ru.kpfu.tasktracker.controller.validator.ProjectMemberValidator;
import ru.kpfu.tasktracker.controller.validator.UserValidator;
import ru.kpfu.tasktracker.mapper.*;
import ru.kpfu.tasktracker.repository.*;
import ru.kpfu.tasktracker.security.BCryptPasswordEncoder;
import ru.kpfu.tasktracker.service.*;

import javax.sql.DataSource;

@Slf4j
@WebListener
public class ApplicationContext implements ServletContextListener {

    private final DataSource dataSource = getDataSource();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        addCommonDependenciesToContext(context);

        CommentRepository commentRepository = new CommentRepository(dataSource);
        CommentService commentService = new CommentService(commentRepository, new CommentMapper());

        ProjectMemberService projectMemberService = new ProjectMemberService(
                new ProjectMemberRepository(dataSource),
                new ProjectMemberMapper()
        );

        UserService userService = new UserService(
                new UserRepository(dataSource),
                projectMemberService,
                new UserMapper(),
                new BCryptPasswordEncoder(12)
        );

        TaskRepository taskRepository = new TaskRepository(dataSource);
        TaskService taskService = new TaskService(
                taskRepository,
                projectMemberService,
                commentService,
                new TaskMapper()
        );

        KanbanCardService kanbanCardService = new KanbanCardService(
                new KanbanCardRepository(dataSource, taskRepository),
                new KanbanCardMapper()
        );

        ProjectService projectService = new ProjectService(
                new ProjectRepository(dataSource),
                projectMemberService,
                kanbanCardService,
                new ProjectMapper()
        );



        context.setAttribute("userService", userService);
        context.setAttribute("projectService", projectService);
        context.setAttribute("projectMemberService", projectMemberService);
        context.setAttribute("taskService", taskService);
        context.setAttribute("kanbanCardService", kanbanCardService);
        context.setAttribute("commentService", commentService);

        UserValidator userValidator = new UserValidator();
        context.setAttribute("userValidator", userValidator);
        ProjectMemberValidator projectMemberValidator = new ProjectMemberValidator();
        context.setAttribute("projectMemberValidator", projectMemberValidator);
        CommentValidator commentValidator = new CommentValidator();
        context.setAttribute("commentValidator", commentValidator);

        log.info("Application context initialized");
    }

    private void addCommonDependenciesToContext(ServletContext context) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        context.setAttribute("objectMapper", objectMapper);
    }

    private DataSource getDataSource() {
        String username = System.getenv("POSTGRES_USERNAME");
        String password = System.getenv("POSTGRES_PASSWORD");

        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setJdbcUrl("jdbc:postgresql://host.docker.internal:5432/task_tracker");
        ds.setUsername(username);
        ds.setPassword(password);

        return ds;
    }
}
