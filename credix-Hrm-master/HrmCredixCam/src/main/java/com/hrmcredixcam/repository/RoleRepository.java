package com.hrmcredixcam.repository;

import com.hrmcredixcam.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role,String> {
    Optional<Role>  findByRole(String role);

     boolean existsByRole(String role);
}
