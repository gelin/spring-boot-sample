package it.sevenbits.sample.springboot.core.repository;

import it.sevenbits.sample.springboot.core.model.User;

import java.util.List;

public interface UsersRepository {

    User findByUserName(String username);

    List<User> findAll();

}
