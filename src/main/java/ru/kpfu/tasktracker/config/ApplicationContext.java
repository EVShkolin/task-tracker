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
import ru.kpfu.tasktracker.controller.validator.ProjectMemberValidator;
import ru.kpfu.tasktracker.controller.validator.UserValidator;
import ru.kpfu.tasktracker.mapper.ProjectMapper;
import ru.kpfu.tasktracker.mapper.TaskMapper;
import ru.kpfu.tasktracker.mapper.UserMapper;
import ru.kpfu.tasktracker.repository.ProjectMemberRepository;
import ru.kpfu.tasktracker.repository.ProjectRepository;
import ru.kpfu.tasktracker.repository.TaskRepository;
import ru.kpfu.tasktracker.repository.UserRepository;
import ru.kpfu.tasktracker.security.BCryptPasswordEncoder;
import ru.kpfu.tasktracker.service.ProjectMemberService;
import ru.kpfu.tasktracker.service.ProjectService;
import ru.kpfu.tasktracker.service.TaskService;
import ru.kpfu.tasktracker.service.UserService;

import javax.sql.DataSource;

@Slf4j
@WebListener
public class ApplicationContext implements ServletContextListener {

    private final DataSource dataSource = getDataSource();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        addCommonDependenciesToContext(context);

        ProjectMemberService projectMemberService = new ProjectMemberService(
                new ProjectMemberRepository(dataSource)
        );

        UserService userService = new UserService(
                new UserRepository(dataSource),
                projectMemberService,
                new UserMapper(),
                new BCryptPasswordEncoder(12)
        );

        ProjectService projectService = new ProjectService(
                new ProjectRepository(dataSource),
                projectMemberService,
                new ProjectMapper()
        );

        TaskService taskService = new TaskService(
                new TaskRepository(dataSource),
                projectMemberService,
                new TaskMapper()
        );

        context.setAttribute("userService", userService);
        context.setAttribute("projectService", projectService);
        context.setAttribute("projectMemberService", projectMemberService);
        context.setAttribute("taskService", taskService);

        UserValidator userValidator = new UserValidator();
        context.setAttribute("userValidator", userValidator);
        ProjectMemberValidator projectMemberValidator = new ProjectMemberValidator();
        context.setAttribute("projectMemberValidator", projectMemberValidator);

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
