package com.pickandroll.erp.service;

import com.pickandroll.erp.model.User;
import java.util.List;

public interface UserServiceInterface {

    public List<User> listUsers();

    public void addUser(User user);

    public void deleteUser(User user);

    public User findByEmail(String email);
    
    public void updateResetPasswordToken(String token, String email);

    public User findByResetPasswordToken(String token);
    
    public void updatePassword(User user, String newPassword);

}
