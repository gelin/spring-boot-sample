package it.sevenbits.sample.springboot.core.services;

import it.sevenbits.sample.springboot.core.model.User;
import it.sevenbits.sample.springboot.core.repository.UsersRepository;
import it.sevenbits.sample.springboot.web.models.Login;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UsersRepository users;

    public LoginService(UsersRepository users) {
        this.users = users;
    }

    public User login(Login login) {
        User user = users.findByUserName(login.getLogin());
        if (user == null) {
            throw new LoginFailedException("User '" + login.getLogin() + "' not found");
        }
        if (!BCrypt.checkpw(login.getPassword(), user.getPassword())) {
            throw new LoginFailedException("Wrong password");
            // Different exceptions are thrown only for demonstration
            // Actually, both cases should look the same outside
        }
        return new User(user.getUsername(), user.getAuthorities());
    }

}
