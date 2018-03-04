package it.sevenbits.sample.springboot.core.repository;

import it.sevenbits.sample.springboot.core.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleUsersRepository implements UsersRepository {

    private final Map<String, User> users = new HashMap<>();

    public SampleUsersRepository() {
        users.put("admin",
                new User("admin", "$2a$10$UeMJWRNWF.lRU/MN6xCYMeAMl5L3L0t4Nm.MXUeltdNfV/fz3B83u",
                Arrays.asList("admin")));
        users.put("guest",
                new User("guest", "$2a$10$rZ14r48ay82okhyZKZNKpeKmXv2OSqyV4LIN5U/G2G7PrMLCFfSDW",
                Arrays.asList("guest")));
    }

    @Override
    public User findByUserName(final String username) {
        return users.get(username);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

}
