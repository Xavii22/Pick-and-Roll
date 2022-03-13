package com.pickandroll.erp.controller;

import com.pickandroll.erp.model.Role;
import com.pickandroll.erp.model.User;
import com.pickandroll.erp.service.RoleService;
import com.pickandroll.erp.service.UserService;
import com.pickandroll.erp.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class RegisterController {

    @Autowired
    private UserService userService;    
    
    @Autowired
    private RoleService roleService;

    @GetMapping("/register")
    public String registerForm(User user) {
        return "register";
    }

    @PostMapping("/registerUser")
    public String registerUser(@Valid User user, Errors errors) {    
        // Volver si hay errores en el formulario
        if (errors.hasErrors()) {
            return "register";
        }
        
        Utils u = new Utils();
        // Email no valido
        if (!u.checkDni(user.getDni())) {
            return "register";
        }
        
        // Si la contraseñas no coinciden
        if (!user.getPassword().equals(user.getPasswordCheck())) {
            return "register";
        }

        // Si el usuario ya existe
        if (checkIfUserExist(user.getEmail())) {
            return "register";
        }
        
        // Encriptamos la contraseña antes de guardarla        
        user.setPassword(u.encrypPasswd(user.getPassword()));
        
        // Le colocamos el rol por defecto con la ID del nuevo usuario
        Role defaultRole = new Role();
        defaultRole.setName("customer");
        List<Role> newRole = new ArrayList<>();
        newRole.add(defaultRole);
        
        user.setRoles(newRole);
        
        roleService.addRole(defaultRole);
        // Lo guardamos en la BBDD
        userService.addUser(user);
        

        return "redirect:/";
    }

    // Método para comprobar si el email ya existe en la DDBB
    private boolean checkIfUserExist(String email) {
        List<User> userList = userService.listUsers();
        for (User u : userList) {
            if (u.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}