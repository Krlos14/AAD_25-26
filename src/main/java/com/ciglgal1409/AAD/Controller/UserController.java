package com.ciglgal1409.AAD.Controller;

import com.ciglgal1409.AAD.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ciglgal1409.AAD.model.User;

@RestController
@RequestMapping(path="/user")

public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value="/saveUser",method= RequestMethod.POST)
    public Boolean saveUser(@RequestBody User u){
        return userService.saveUser(u);
    }
}
