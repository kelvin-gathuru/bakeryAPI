package com.threepmanagerapi.threepmanagerapi.user.controller;

import com.threepmanagerapi.threepmanagerapi.user.dto.*;
import com.threepmanagerapi.threepmanagerapi.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }
    @PostMapping("/authenticate")
    public ResponseEntity login(@RequestBody AuthenticationDto authenticationDto) {
        return userService.authenticate(authenticationDto);
    }
    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestBody EmailDto emailDto) {
        return userService.forgotPassword(emailDto);
    }
    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return userService.resetPassword(resetPasswordDto);
    }
    @PostMapping("/changePassword")
    public ResponseEntity changePassword(@RequestHeader("Authorization") String token,
                                                     @RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(token, changePasswordDto);
    }
}
