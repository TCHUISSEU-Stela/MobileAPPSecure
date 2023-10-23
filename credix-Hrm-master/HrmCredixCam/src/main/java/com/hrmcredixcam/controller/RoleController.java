package com.hrmcredixcam.controller;


import com.hrmcredixcam.authdtos.RoleDTO;
import com.hrmcredixcam.model.Role;
import com.hrmcredixcam.publicdtos.ResponseDTO;
import com.hrmcredixcam.service.RoleService;
import com.hrmcredixcam.utils.EntityResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/roles/")
@RequiredArgsConstructor
@Slf4j
public class RoleController {
    private final RoleService roleService;
    private final EntityResponseUtils entityResponseUtils;

    @CrossOrigin
    @PostMapping("addRole")
    public ResponseEntity<ResponseDTO> addRole(@Valid @RequestBody RoleDTO dto){

        try {

            var role= Role.builder().role("ROLE_"+dto.getRole().toUpperCase()).roleDescription(dto.getDescription()).build();

           var rl= roleService.saveRole(role);

            return new ResponseEntity<>(entityResponseUtils.SuccessFullResponse("Add Role",rl,1), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(entityResponseUtils.ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

}
