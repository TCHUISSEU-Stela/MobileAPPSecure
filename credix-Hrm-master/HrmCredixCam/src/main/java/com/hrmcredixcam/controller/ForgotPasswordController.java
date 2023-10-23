package com.hrmcredixcam.controller;

import com.hrmcredixcam.exception.InvalidPasswordException;
import com.hrmcredixcam.model.User;
import com.hrmcredixcam.model.PasswordResetToken;
import com.hrmcredixcam.publicdtos.Response;
import com.hrmcredixcam.service.UserService;
import com.hrmcredixcam.service.PasswordResetTokenService;
import com.hrmcredixcam.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserService employeeService;
    private final PasswordResetTokenService tokenService;
    private final JavaMailSender javaMailSender;

    // API 1 : Envoyer l'OTP par e-mail
    @CrossOrigin
    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable String email) {

        User employee = employeeService.getEmployeeByEmail(email);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("this employee whit email "+ email+" does not exist.");
        }

        String otp = generateOTP();
        sendOTPEmail(email, otp);

        Date expirationDate = calculateExpirationDate();
        tokenService.createToken(employee, otp, expirationDate);
        return ResponseEntity.ok("OTP sent by email: "+ email +" check your email, if your email is missing, please check your spam");
    }

    // API 2 : Vérifier l'OTP
    @CrossOrigin
    @PostMapping("/verify-otp/{otp}")
    public ResponseEntity<String> verifyOTP(@PathVariable String otp) {

        PasswordResetToken token = tokenService.findByToken(otp);
        if (token == null || token.isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP.");
        }

        User employee = token.getEmployee();
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token not linked to an employee.");
        }

        // Réinitialisation du mot de passe
        String newPassword = generateNewPassword();
        employeeService.updatePassword(employee.getId(), newPassword);

        tokenService.deleteToken(token);

        return ResponseEntity.ok("Password reset successfully, your new password = [ " + newPassword +
                " ]. please modify it after logging in.");
    }


    // API 3 : Réinitialiser le mot de passe
    @PreAuthorize("hasAnyRole('ROLE_SUPERADMIN', 'ROLE_ADMIN', 'ROLE_USER')")
    @CrossOrigin
    @PostMapping("/{employeeId}/reset-password/{newPassword}/{confirmPassword}")
    public ResponseEntity<Response> resetPassword(@PathVariable String employeeId,
                                                @PathVariable String newPassword,
                                                @PathVariable String confirmPassword) {

        try {
            employeeService.resetPassword(employeeId, newPassword, confirmPassword);
            return ResponseEntity.ok(
                    Response.builder()
                            .timeStamp(now())
                            .message("Employee Password successfully reset.")
                            .status(OK)
                            .statusCode(OK.value())
                            .build()
            );
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
            return UserUtils.getResponseEntityG("Passwords are not identical.", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return UserUtils.getResponseEntityG("Employee not found with ID: " + employeeId, NOT_FOUND);
        }

    }

    private String generateNewPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        int length = 8;
        StringBuilder newPassword = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            newPassword.append(characters.charAt(index));
        }

        return newPassword.toString();
    }

    private String generateOTP() {
        // Générer un nombre aléatoire entre 100000 et 999999 (inclus)
        int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(otp);
    }

    private void sendOTPEmail(String email, String otp) {
        // Envoyez l'OTP par e-mail en utilisant JavaMailSender
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("teufackandelson123@gmail.com");
        message.setTo(email);
        message.setSubject("Password reset");
        message.setText("Your OTP code to reset the password is :" + otp);
        javaMailSender.send(message);
    }

    private Date calculateExpirationDate() {
        // Calculez la date d'expiration (par exemple, 5 minutes à partir de maintenant)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 5);
        return calendar.getTime();
    }
}

