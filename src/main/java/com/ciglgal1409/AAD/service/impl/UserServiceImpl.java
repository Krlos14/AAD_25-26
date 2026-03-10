package com.ciglgal1409.AAD.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ciglgal1409.AAD.model.User;
import com.ciglgal1409.AAD.repository.UserRepository;
import com.ciglgal1409.AAD.service.UserService;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public Boolean saveUser(User user) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
