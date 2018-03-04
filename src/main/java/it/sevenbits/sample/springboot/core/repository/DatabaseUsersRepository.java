package it.sevenbits.sample.springboot.core.repository;

import it.sevenbits.sample.springboot.core.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository to list all users.
 */
public class DatabaseUsersRepository implements UsersRepository {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseUsersRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findByUserName(String username) {
        return null;    // TODO
    }

    public List<User> findAll() {
        HashMap<String, User> users = new HashMap<>();

        for (Map<String, Object> row : jdbcTemplate.queryForList(
                "SELECT username, authority FROM authorities a" +
                        " WHERE EXISTS" +
                        " (SELECT * FROM users u WHERE" +
                        " u.username = a.username AND u.enabled = true)")) {

            String username = String.valueOf(row.get("username"));
            String newRole = String.valueOf(row.get("authority"));
            User user = users.computeIfAbsent(username, name -> new User(name, new ArrayList<>()));
            List<String> roles = user.getAuthorities();
            roles.add(newRole);

        }

        List<User> result = new ArrayList<>();
        result.addAll(users.values());
        return result;
    }

}
