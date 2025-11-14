package ru.kpfu.tasktracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.controller.validator.UserValidator;
import ru.kpfu.tasktracker.mapper.UserMapper;
import ru.kpfu.tasktracker.repository.UserRepository;
import ru.kpfu.tasktracker.security.BCryptPasswordEncoder;
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
        addUserDependenciesToContext(context);
        log.info("Application context initialized");
    }

    private void addCommonDependenciesToContext(ServletContext context) {
        ObjectMapper objectMapper = new ObjectMapper();
        context.setAttribute("objectMapper", objectMapper);
    }

    private void addUserDependenciesToContext(ServletContext context) {
        UserRepository userRepository = new UserRepository(dataSource);
        UserMapper userMapper = new UserMapper();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        UserService userService = new UserService(userRepository, userMapper, encoder);
        context.setAttribute("userService", userService);

        UserValidator userValidator = new UserValidator();
        context.setAttribute("userValidator", userValidator);
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
