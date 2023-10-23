package com.hrmcredixcam.security;

import com.hrmcredixcam.model.User;
import com.hrmcredixcam.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository employeeRepository;

    public CustomUserDetailsService(UserRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmailOrTelephone) throws UsernameNotFoundException {
        User employee = employeeRepository.findByUserNameOrAndEmailOrTelephone(usernameOrEmailOrTelephone, usernameOrEmailOrTelephone,usernameOrEmailOrTelephone)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Employee not found with username or email or telephone: "+ usernameOrEmailOrTelephone));

        Set<GrantedAuthority> authorities = employee
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(employee.getUserName(),
                employee.getPassword(),
                authorities);
    }



}
