package com.hrmcredixcam.service;

import com.hrmcredixcam.exception.DoesNotExistException;
import com.hrmcredixcam.exception.ValueAlreadyTakenException;
import com.hrmcredixcam.model.User;
import com.hrmcredixcam.publicdtos.EmployeeRespDTO;
import com.hrmcredixcam.publicdtos.UpdateEmployeeDTO;

import java.util.List;

public interface UserService {
    boolean verifyIfUserNameExist(String userName) throws ValueAlreadyTakenException;

    boolean verifyIfEmailExist(String email) throws ValueAlreadyTakenException;

    User saveEmployee(User user);

    User findByUserName(String userName) throws DoesNotExistException;

    List<EmployeeRespDTO> getAllEmployees();

    List<EmployeeRespDTO> getActiveEmployees();

    List<EmployeeRespDTO> getArchivedEmployees();

    List<EmployeeRespDTO> getEmployeeByRole(String role);

    EmployeeRespDTO getEmployeeById(String id);

    User getEmployeeByEmail(String email);

    String updateEmployee(String id, UpdateEmployeeDTO updatedEmployee);

    String deleteEmployee(String id);

    String archiveEmployee(String id);

    String activateEmployee(String id);

    String updatePassword(String employeeId, String newPassword);

    String resetPassword(String employeeId, String newPassword, String confirmPassword);

    String changeRole(String employeeId, String newRole);
}
