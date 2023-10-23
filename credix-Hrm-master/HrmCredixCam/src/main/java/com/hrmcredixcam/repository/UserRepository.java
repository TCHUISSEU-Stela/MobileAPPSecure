package com.hrmcredixcam.repository;

import com.hrmcredixcam.model.User;

import com.hrmcredixcam.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    List<User> findByRoles(Role role);

    Optional<User> findByUserNameOrAndEmailOrTelephone(String username, String email, String telephone);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    Boolean existsByTelephone(String telephone);

    List<User> findByIsActive(boolean isActive);
}
