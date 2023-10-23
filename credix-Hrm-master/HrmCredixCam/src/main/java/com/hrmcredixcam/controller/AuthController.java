package com.hrmcredixcam.controller;

import com.hrmcredixcam.authdtos.LoginRequestDTO;
import com.hrmcredixcam.authdtos.RefreshTokenDTO;
import com.hrmcredixcam.authdtos.SignupRequestDTO;
import com.hrmcredixcam.authdtos.SignupUserRequestDTO;
import com.hrmcredixcam.model.User;
import com.hrmcredixcam.publicdtos.ResponseDTO;
import com.hrmcredixcam.service.AuthService;
import com.hrmcredixcam.service.UserService;
import com.hrmcredixcam.service.RoleService;
import com.hrmcredixcam.utils.EntityResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthController {


    private final EntityResponseUtils entityResponseUtils;
    private final AuthService authService;
    private final PasswordEncoder encoder;
    private final UserService employeeService;
    private final RoleService roleService;
    private final JavaMailSender javaMailSender;

    @CrossOrigin
    @PostMapping("signIn")
    public ResponseEntity<ResponseDTO> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        try {

            var employee=authService.login(loginRequestDTO);
            log.info("Successful login : {}",employee);
            return new ResponseEntity<>(entityResponseUtils.SuccessFullResponse("Authenticating employee",employee,1), OK);
        } catch (Exception e) {
            log.info("Error login : {}",e.getMessage());
            return new ResponseEntity<>(entityResponseUtils.ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @PostMapping("registerByAdmin")
    public ResponseEntity<ResponseDTO> registerUserByAdmin(@Valid @RequestBody SignupRequestDTO signUpRequestDTO) {

        try {
            var employee=authService.registerByAdmin(signUpRequestDTO);
            log.info("Success full creating employee : {}",employee);
            sendEmail(signUpRequestDTO.getEmail(), signUpRequestDTO.getFirstName(), signUpRequestDTO.getLastName());
            return new ResponseEntity<>(entityResponseUtils.SuccessFullResponse("Employee registered successfully!",employee,0), OK);
        } catch (Exception e) {
            log.error("Exception creating employee : {}",e.getMessage());
            return new ResponseEntity<>(entityResponseUtils.ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }


    @CrossOrigin
    @PostMapping("registerByUser")
    public ResponseEntity<ResponseDTO> registerUserByUSer(@Valid @RequestBody SignupUserRequestDTO signUpRequestDTO) {

        try {
            var employee=authService.registerByUser(signUpRequestDTO);
            log.info("Success full creating employee : {}",employee);
            sendEmail(signUpRequestDTO.getEmail(), signUpRequestDTO.getFirstName(), signUpRequestDTO.getLastName());
            return new ResponseEntity<>(entityResponseUtils.SuccessFullResponse("Employee registered successfully!",employee,0), OK);
        } catch (Exception e) {
            log.error("Exception creating employee : {}",e.getMessage());
            return new ResponseEntity<>(entityResponseUtils.ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    @CrossOrigin
    @PostMapping("refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokendto) {

        try {
            var employee=authService.refreshToken(refreshTokendto);
            log.info("Successfull refreshing token for employee : {}",employee);
            return new ResponseEntity<>(entityResponseUtils.SuccessFullResponse("Refreshing  Token ",employee,1), OK);


        } catch (Exception e) {
            log.info("Exception refreshing token for employee : {}",e.getMessage());
            return new ResponseEntity<>(entityResponseUtils.ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    @CrossOrigin
    @PostMapping("add/responsable")
    public ResponseEntity<ResponseDTO> addAdmin(@Valid @RequestBody SignupRequestDTO signUpRequestDTO) {

        try {
            var employee = User.builder()
                    .userName(signUpRequestDTO.getUserName())
                    .lastName(signUpRequestDTO.getLastName())
                    .firstName(signUpRequestDTO.getFirstName())
                    .email(signUpRequestDTO.getEmail())
                    .telephone(signUpRequestDTO.getTelephone())
                    .isActive(true)
                    .password(encoder.encode(signUpRequestDTO.getPassword()))
                    .creationDate(LocalDateTime.now()).build();


            Set<String> strRoles = signUpRequestDTO.getRole();
            var roles = roleService.getListOfRoleFromListOfRoleStr(strRoles);
            employee.setRoles(roles);

            if (!employeeService.verifyIfUserNameExist(signUpRequestDTO.getUserName())
            && !employeeService.verifyIfEmailExist(signUpRequestDTO.getEmail())){

                var usr=employeeService.saveEmployee(employee);

                return new ResponseEntity<>(entityResponseUtils.SuccessFullResponse("Admin registered successfully!",usr,1), OK);

            }else{
                return new ResponseEntity<>(entityResponseUtils.ErrorResponse("Admin not registered!"), OK);
            }

        } catch (Exception e) {

            return new ResponseEntity<>(entityResponseUtils.ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    private void sendEmail(String email, String firstName, String lastName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("teufackandelson123@gmail.com");
        message.setTo(email);
        message.setSubject("Successfully account creation");
        message.setText("Dear "+ firstName.toUpperCase()+ " " + lastName +" your account has been successfully created, " +
                "you can now go to the application to log in.");
        javaMailSender.send(message);
        System.out.println("email successfully sent");
    }


}
