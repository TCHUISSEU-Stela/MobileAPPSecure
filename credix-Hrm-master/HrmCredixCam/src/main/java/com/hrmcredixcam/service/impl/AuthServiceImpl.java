package com.hrmcredixcam.service.impl;


import com.hrmcredixcam.authdtos.*;
import com.hrmcredixcam.exception.BlogAPIException;
import com.hrmcredixcam.exception.TokenRefreshException;
import com.hrmcredixcam.model.User;
import com.hrmcredixcam.model.RefreshToken;
import com.hrmcredixcam.repository.RoleRepository;
import com.hrmcredixcam.repository.UserRepository;
import com.hrmcredixcam.security.JwtTokenProvider;
import com.hrmcredixcam.service.AuthService;
import com.hrmcredixcam.service.RefreshTokenService;
import com.hrmcredixcam.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository employeeRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;



    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository employeeRepository,
                           RoleService roleService, RoleRepository roleRepository, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    @Override
    public UserInfoResponseDTO login(LoginRequestDTO loginDto) {

        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUserNameOrEmailOrTelephone(), loginDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            // log.info("authentication : {}",authentication);
            String token = jwtTokenProvider.generateTokenFromUsername(authentication);
            String rToken = jwtTokenProvider.generateRefreshTokenFromUsername(authentication);
            var expirationTime= jwtTokenProvider.jwtExpirationTime();

            var refreshToken= RefreshToken.builder()
                    .refreshTokenId(UUID.randomUUID())
                    .token(token)
                    .user(authentication.getName())
                    .refreshToken(rToken)
                    .dateTime(LocalDateTime.now())
                    .expiryDate(jwtTokenProvider.jwtRefreshExpirationTime())
                    .build();

            log.info("refreshToken : {}",refreshToken);

            var employee = employeeRepository.findByUserNameOrAndEmailOrTelephone(loginDto.getUserNameOrEmailOrTelephone(),loginDto.getUserNameOrEmailOrTelephone(),loginDto.getUserNameOrEmailOrTelephone());

            Set<String> roles=new HashSet<>();

            employee.get().getRoles().forEach(role -> roles.add(role.getRole()));

            var savedRefreshToken= refreshTokenService.saveRefreshToken(refreshToken);
            return UserInfoResponseDTO.builder()
                    .token(token)
                    .userName(employee.get().getUserName())
                    .refreshToken(savedRefreshToken.getRefreshTokenId())
                    .email(employee.get().getEmail())
                    .expiration(expirationTime)
                    .roles(roles)
                    .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String registerByAdmin(SignupRequestDTO registerDto) {

        try {
            // add check for username exists in database
            if(employeeRepository.existsByUserName(registerDto.getUserName())){
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
            }

            // add check for email exists in database
            if(employeeRepository.existsByEmail(registerDto.getEmail())){
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
            }

            // add check for telephone exists in database
            if(employeeRepository.existsByTelephone(registerDto.getTelephone())){
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Telephone is already exists!.");
            }

            // Create new user's account
            User employee = User.builder()
                    .userName(registerDto.getUserName())
                    .email(registerDto.getEmail())
                    .telephone(registerDto.getTelephone())
                    .firstName(registerDto.getFirstName())
                    .lastName(registerDto.getLastName())
                    .creationDate(LocalDateTime.now())
                    .password(passwordEncoder.encode(registerDto.getPassword()))
                    .isActive(true)
                    .build();

            Set<String> strRoles = registerDto.getRole();
            var roles=roleService.getListOfRoleFromListOfRoleStr(strRoles);
            employee.setRoles(roles);

            employeeRepository.save(employee);

            return "Employee registered successfully!.";
        } catch (BlogAPIException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public String registerByUser(SignupUserRequestDTO registerDto) {
        try {
            // add check for username exists in database
            if (employeeRepository.existsByUserName(registerDto.getUserName())) {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
            }

            // add check for email exists in database
            if (employeeRepository.existsByEmail(registerDto.getEmail())) {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
            }

            // add check for telephone exists in database
            if (employeeRepository.existsByTelephone(registerDto.getTelephone())) {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Telephone is already exists!.");
            }

            // Create new user's account with the USER role
            User employee = User.builder()
                    .userName(registerDto.getUserName())
                    .email(registerDto.getEmail())
                    .telephone(registerDto.getTelephone())
                    .firstName(registerDto.getFirstName())
                    .lastName(registerDto.getLastName())
                    .creationDate(LocalDateTime.now())
                    .password(passwordEncoder.encode(registerDto.getPassword()))
                    .isActive(true)
                    .roles(Collections.singleton(roleRepository.findByRole(ERole.ROLE_REVENDEUR.toString())
                            .orElseThrow(() -> new RuntimeException("Error: User Role is not found."))))
                    .build();

            employeeRepository.save(employee);

            return "Employee registered successfully!.";
        } catch (BlogAPIException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public UserInfoResponseDTO refreshToken(RefreshTokenDTO refreshTokendto){

        var refreshToken=refreshTokenService.findByRefreshTokenId(refreshTokendto.getToken());

        var valid= jwtTokenProvider.validateJwtExpiringTime(refreshToken);
        if(valid){

            var userNameOrEmailOrTelephone = jwtTokenProvider.getUserNameFromJwtToken(refreshToken.getRefreshToken());
            log.info("UserName Or Email Or Telephone : {}",userNameOrEmailOrTelephone);
            var employee = employeeRepository.findByUserNameOrAndEmailOrTelephone(userNameOrEmailOrTelephone,userNameOrEmailOrTelephone,userNameOrEmailOrTelephone);
            log.info("Employee : {}",employee);

            var roleList=new HashSet<String>();
            employee.get().getRoles().forEach(b-> roleList.add(b.getRole()));

            var jwtTokenFromRefreshToken = jwtTokenProvider.generateTokenFromUsername(employee.get());
            var expirationTime= jwtTokenProvider.jwtExpirationTime();


            return UserInfoResponseDTO.builder()
                    .token(jwtTokenFromRefreshToken)
                    .refreshToken(refreshTokendto.getToken())
                    .email(employee.get().getEmail())
                    .userName(employee.get().getUserName())
                    .expiration(expirationTime)
                    .roles(roleList)
                    .build();
        }else{

            throw new TokenRefreshException(refreshTokendto.getToken()," Expired ");

        }
    }


}
