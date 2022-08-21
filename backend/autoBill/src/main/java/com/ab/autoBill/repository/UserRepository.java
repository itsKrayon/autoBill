package com.ab.autoBill.repository;

import com.ab.autoBill.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @author Keyan
 */
@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    User findByUsername(String username);

    User findUserById(int userId);

   // List<User> findUsersByValid(boolean isValid);

}
