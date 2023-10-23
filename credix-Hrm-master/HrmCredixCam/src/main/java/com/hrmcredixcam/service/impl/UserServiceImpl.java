package com.hrmcredixcam.service.impl;

import com.hrmcredixcam.authdtos.ERole;
import com.hrmcredixcam.exception.*;
import com.hrmcredixcam.model.User;
import com.hrmcredixcam.model.Role;
import com.hrmcredixcam.publicdtos.EmployeeRespDTO;
import com.hrmcredixcam.publicdtos.UpdateEmployeeDTO;
import com.hrmcredixcam.repository.RoleRepository;
import com.hrmcredixcam.repository.UserRepository;
import com.hrmcredixcam.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JavaMailSender javaMailSender;

    @Override
    public boolean verifyIfUserNameExist(String userName) throws ValueAlreadyTakenException {
        var bool=  employeeRepository.existsByUserName(userName);
        if (bool){
            throw new  ValueAlreadyTakenException("Employee",userName);
        }
        return false;
    }

    @Override
    public boolean verifyIfEmailExist(String email) throws ValueAlreadyTakenException {
        var bool=  employeeRepository.existsByEmail(email);
        if (bool){
            throw new  ValueAlreadyTakenException("Email",email);
        }
        return false;
    }

    @Override
    public User saveEmployee(User user){
        return employeeRepository.save(user);
    }

    @Override
    public User findByUserName(String userName) throws DoesNotExistException {
        return  employeeRepository.findByUserName(userName).orElseThrow(()->new DoesNotExistException("Employee",userName));
    }


    @Override
    public List<EmployeeRespDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(e->{
            Set<String> roles=new HashSet<>();
            e.getRoles().forEach(role -> roles.add(role.getRole()));

            return EmployeeRespDTO.builder()
                    .id(e.getId())
                    .creationDate(e.getCreationDate())
                    .firstName(e.getFirstName())
                    .isActive(e.isActive())
                    .lastName(e.getLastName())
                    .email(e.getEmail())
                    .telephone(e.getTelephone())
                    .userName(e.getUserName())
                    .roles(roles)
                    .build();}).toList();

    }

    @Override
    public List<EmployeeRespDTO> getActiveEmployees() {

        return employeeRepository.findByIsActive(true).stream().map(e->{
            Set<String> roles=new HashSet<>();
            e.getRoles().forEach(role -> roles.add(role.getRole()));

            return EmployeeRespDTO.builder()
                    .id(e.getId())
                    .creationDate(e.getCreationDate())
                    .firstName(e.getFirstName())
                    .isActive(e.isActive())
                    .lastName(e.getLastName())
                    .email(e.getEmail())
                    .telephone(e.getTelephone())
                    .userName(e.getUserName())
                    .roles(roles)


                    .build();}).toList();

    }

    @Override
    public List<EmployeeRespDTO> getArchivedEmployees() {
        return employeeRepository.findByIsActive(false).stream().map(e->{
            Set<String> roles=new HashSet<>();
            e.getRoles().forEach(role -> roles.add(role.getRole()));

            return EmployeeRespDTO.builder()
                    .id(e.getId())
                    .creationDate(e.getCreationDate())
                    .firstName(e.getFirstName())
                    .isActive(e.isActive())
                    .lastName(e.getLastName())
                    .email(e.getEmail())
                    .telephone(e.getTelephone())
                    .userName(e.getUserName())
                    .roles(roles)
                    .build();}).toList();

    }

    @Override
    public List<EmployeeRespDTO> getEmployeeByRole(String strRole) {
        try {
            if(strRole.equalsIgnoreCase("admin")){
                Role userRole = roleRepository.findByRole(ERole.ROLE_RESPONSABLE.toString())
                        .orElseThrow(() -> new BlogAPIException(NOT_FOUND,"Error: Admin Role is not found."));
                return employeeRepository.findByRoles(userRole).stream().map(e->{
                    Set<String> roles=new HashSet<>();
                    e.getRoles().forEach(role -> roles.add(role.getRole()));

                    return EmployeeRespDTO.builder()
                            .id(e.getId())
                            .creationDate(e.getCreationDate())
                            .firstName(e.getFirstName())
                            .isActive(e.isActive())
                            .lastName(e.getLastName())
                            .email(e.getEmail())
                            .telephone(e.getTelephone())
                            .userName(e.getUserName())
                            .roles(roles)
                            .build();}).toList();

            }else if(strRole.equalsIgnoreCase("superadmin")){
                Role userRole = roleRepository.findByRole(ERole.ROLE_SUPERADMIN.toString())
                        .orElseThrow(() -> new BlogAPIException(NOT_FOUND,"Error: SuperAdmin Role is not found."));
                return employeeRepository.findByRoles(userRole).stream().map(e->{
                    Set<String> roles=new HashSet<>();
                    e.getRoles().forEach(role -> roles.add(role.getRole()));

                    return EmployeeRespDTO.builder()
                            .id(e.getId())
                            .creationDate(e.getCreationDate())
                            .firstName(e.getFirstName())
                            .isActive(e.isActive())
                            .lastName(e.getLastName())
                            .email(e.getEmail())
                            .telephone(e.getTelephone())
                            .userName(e.getUserName())
                            .roles(roles)
                            .build();}).toList();
            }else if(strRole.equalsIgnoreCase("user")){
                Role userRole = roleRepository.findByRole(ERole.ROLE_REVENDEUR.toString())
                        .orElseThrow(() -> new BlogAPIException(NOT_FOUND,"Error: Admin Role is not found."));
                return employeeRepository.findByRoles(userRole).stream().map(e->{
                    Set<String> roles=new HashSet<>();
                    e.getRoles().forEach(role -> roles.add(role.getRole()));

                    return EmployeeRespDTO.builder()
                            .id(e.getId())
                            .creationDate(e.getCreationDate())
                            .firstName(e.getFirstName())
                            .isActive(e.isActive())
                            .lastName(e.getLastName())
                            .email(e.getEmail())
                            .telephone(e.getTelephone())
                            .userName(e.getUserName())
                            .roles(roles)
                            .build();}).toList();
            }

            throw new IllegalArgumentException("Error: The role you provided does not exist.");
        }catch (BlogAPIException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public EmployeeRespDTO getEmployeeById(String id) {

        var employee = employeeRepository.findById(id).orElseThrow(() -> new BlogAPIException(NOT_FOUND,"Error: Employee not found whit ID provided"));

        Set<String> roles = new HashSet<>();
        employee.getRoles().forEach(role -> roles.add(role.getRole()));
        return EmployeeRespDTO.builder()
                .id(employee.getId())
                .creationDate(employee.getCreationDate())
                .firstName(employee.getFirstName())
                .isActive(employee.isActive())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .telephone(employee.getTelephone())
                .userName(employee.getUserName())
                .roles(roles)
                .build();

    }

    @Override
    public User getEmployeeByEmail(String email){

        return employeeRepository.findByEmail(email).orElseThrow(() -> new BlogAPIException(NOT_FOUND," Employee not found with email: " + email));

    }

    @Override
    public String updateEmployee(String id, UpdateEmployeeDTO updatedEmployee) {
        User employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BlogAPIException(NOT_FOUND," Employee not found with ID: " + id));
        if(updatedEmployee.getFirstName() != null){
            employee.setFirstName(updatedEmployee.getFirstName());
        }
        if(updatedEmployee.getUserName() != null){
            employee.setUserName(updatedEmployee.getUserName());
        }
        if(updatedEmployee.getEmail() != null){
            employee.setEmail(updatedEmployee.getEmail());
        }
        if(updatedEmployee.getTelephone() != null){
            employee.setTelephone(updatedEmployee.getTelephone());
        }
        if(updatedEmployee.getLastName() != null){
            employee.setLastName(updatedEmployee.getLastName());
        }

        employeeRepository.save(employee);
        return "Employee successfully updated";
    }

    @Override
    public String deleteEmployee(String id){
        User employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BlogAPIException(NOT_FOUND," Employee not found with ID: " + id));
        employeeRepository.delete(employee);
        return "Employee successfully deleted";
    }

    @Override
    public String archiveEmployee(String id){
        User employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BlogAPIException(BAD_REQUEST," Employee not found with ID: " + id));
        employee.setActive(false);
        employeeRepository.save(employee);
        sendResponseEmail(employee.getEmail(), "archived", employee.getFirstName(), employee.getLastName());

        return "Employee successfully archived";
    }

    @Override
    public String activateEmployee(String id){
        User employee = employeeRepository.findById(id)
                .orElseThrow(() -> new BlogAPIException(NOT_FOUND," Employee not found with ID: " + id));
        employee.setActive(true);
        employeeRepository.save(employee);
        sendResponseEmail(employee.getEmail(), "successfully active", employee.getFirstName(), employee.getLastName());
        return "Employee successfully activate";
    }

    @Override
    public String updatePassword(String employeeId, String newPassword){
        if (employeeId == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        Optional<User> optionalEmployee = employeeRepository.findById(employeeId);
        User employee = optionalEmployee.orElseThrow(() -> new BlogAPIException(NOT_FOUND," Employee not found with ID: " + employeeId));

        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
        return "Employee password successfully updated";
    }

    @Override
    public String resetPassword(String employeeId, String newPassword, String confirmPassword){
        if (employeeId == null || newPassword == null || confirmPassword == null) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        if (!newPassword.equals(confirmPassword)){
            throw new InvalidPasswordException("Passwords are not identical");
        }

        Optional<User> optionalEmployee = employeeRepository.findById(employeeId);
        User employee = optionalEmployee.orElseThrow(() -> new BlogAPIException(NOT_FOUND," Employee not found with ID: " + employeeId));

        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);
        return "Employee password successfully reset";
    }

    @Override
    public String changeRole(String employeeId, String newRole) {

        Role roleToUpdate;

        String lowerCaseNewRole = newRole.toLowerCase();

        roleToUpdate = switch (lowerCaseNewRole) {
            case "admin" -> roleRepository.findByRole(ERole.ROLE_RESPONSABLE.toString())
                    .orElseThrow(() -> new InvalidRoleException("Error: Admin Role is not found."));
            case "superadmin" -> roleRepository.findByRole(ERole.ROLE_SUPERADMIN.toString())
                    .orElseThrow(() -> new InvalidRoleException("Error: Superadmin Role is not found."));
            case "user" -> roleRepository.findByRole(ERole.ROLE_REVENDEUR.toString())
                    .orElseThrow(() -> new InvalidRoleException("Error: User Role is not found."));
            default -> throw new BlogAPIException(BAD_REQUEST," Error: Invalid Role");
        };

        User userToUpdate = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new BlogAPIException(NOT_FOUND," Error: Employee not found."));


        userToUpdate.setRoles(Collections.singleton(roleToUpdate));
        employeeRepository.save(userToUpdate);
        return "Employee role successfully changed";
    }

    private void sendResponseEmail(String email, String response,String firstName, String lastName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("teufackandelson123@gmail.com");
        message.setTo(email);
        message.setSubject("response to account management");
        message.setText("Dear "+ firstName.toUpperCase()+ " " + lastName.toLowerCase()
                +", your account has been "+response+" by the administration.");
        javaMailSender.send(message);
        System.out.println("email successfully sent");
    }
}
