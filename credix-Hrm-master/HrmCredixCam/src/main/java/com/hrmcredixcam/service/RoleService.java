package com.hrmcredixcam.service;

import com.hrmcredixcam.exception.AlreadyExistException;
import com.hrmcredixcam.model.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getListOfRoleFromListOfRoleStr(Set<String> strRoles);

    Role saveRole(Role roleData) throws AlreadyExistException;


}
