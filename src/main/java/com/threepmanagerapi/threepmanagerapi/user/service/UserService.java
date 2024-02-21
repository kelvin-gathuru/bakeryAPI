package com.threepmanagerapi.threepmanagerapi.user.service;

import com.threepmanagerapi.threepmanagerapi.settings.service.JwtService;
import com.threepmanagerapi.threepmanagerapi.settings.utility.RandomCodeGenerator;
import com.threepmanagerapi.threepmanagerapi.settings.utility.ResponseService;
import com.threepmanagerapi.threepmanagerapi.user.dto.*;
import com.threepmanagerapi.threepmanagerapi.user.model.User;
import com.threepmanagerapi.threepmanagerapi.user.model.UserType;
import com.threepmanagerapi.threepmanagerapi.user.repository.UserRepository;
import com.threepmanagerapi.threepmanagerapi.settings.config.JwtUser;
import com.threepmanagerapi.threepmanagerapi.settings.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ResponseService responseService;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16, new SecureRandom());


    public ResponseEntity createUser(CreateUserDto createUserDto){
        try{
            Optional<User> existingUser = userRepository.findByEmail(createUserDto.getEmail());
            if(existingUser.isPresent()){
                return responseService.formulateResponse(
                        null,
                        "Owner Exists",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            }

            User user = new User();

            user.setName(createUserDto.getName());
            user.setPhone(createUserDto.getPhone());
            user.setEmail(createUserDto.getEmail());
            user.setUserType(UserType.valueOf(createUserDto.getUserType()));
            String generatedPassword = RandomCodeGenerator.generateRandomCode();
            String encodedPassword = encoder.encode(generatedPassword);
            user.setPassword(encodedPassword);
            user.setRegistrationDate(LocalDateTime.now());

            userRepository.save(user);
            emailService.sendCredentials(createUserDto.getEmail(), generatedPassword);
            return responseService.formulateResponse(
                    null,
                    "User added successfully ",
                    HttpStatus.OK,
                    null,
                    true
            );
        } catch (Exception exception) {
            log.error("Encountered Exception {}", exception.getMessage());
            return responseService.formulateResponse(
                    null,
                    "Exception creating user ",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }
    }
    public ResponseEntity authenticate(AuthenticationDto authenticationDto){
        try {
            Optional<User> user = userRepository.findByEmail(authenticationDto.getEmail());
            if (user.isPresent()) {
                    if (encoder.matches(authenticationDto.getPassword(), user.get().getPassword())) {
                        JwtUser jwtUser = new JwtUser();
                        jwtUser.setUserID(user.get().getUserID());
                        jwtUser.setName(user.get().getName());
                        jwtUser.setEmail(user.get().getEmail());
                        String token = jwtService.generateToken(jwtUser);
                        Map<String, Object> response = new HashMap<>();
                        response.put("token", token);
                        response.put("name",user.get().getName());
                        response.put("userType",user.get().getUserType());
                        return responseService.formulateResponse(
                                response,
                                "Login successful",
                                HttpStatus.OK,
                                null,
                                true
                        );
                    }
                    return responseService.formulateResponse(
                            null,
                            "Incorrect password",
                            HttpStatus.UNAUTHORIZED,
                            null,
                            false
                    );

                }

            return responseService.formulateResponse(
                    null,
                    "User not found",
                    HttpStatus.BAD_REQUEST,
                    null,
                    false
            );
        }catch (Exception e) {
            log.error("Encountered Exception {}", e.getMessage());
            return responseService.formulateResponse(
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    false
            );
        }
    }

    public ResponseEntity forgotPassword(EmailDto emailDto) {
        try {
            Optional<User> user = userRepository.findByEmail(emailDto.getEmail());
            if (user.isEmpty()) {
                return responseService.formulateResponse(
                        null,
                        "Email not sent",
                        HttpStatus.NOT_FOUND,
                        null,
                        false
                );
            }
            emailService.sendResetEmail(emailDto.getEmail());
            return responseService.formulateResponse(
                    null,
                    "Email to reset sent",
                    HttpStatus.OK,
                    null,
                    true
            );
        }catch (Exception e) {
            log.error("Encountered Exception {}", e.getMessage());
            return responseService.formulateResponse(
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    false
            );
        }
    }

    public ResponseEntity resetPassword(ResetPasswordDto resetPasswordDto) {
        try {
            Optional<User> user = userRepository.findByEmail(resetPasswordDto.getEmail());
            if (user.isEmpty()) {
                return responseService.formulateResponse(
                        null,
                        "Password not reset",
                        HttpStatus.BAD_REQUEST,
                        null,
                        false
                );
            } else {

                String encodedPassword = encoder.encode(resetPasswordDto.getPassword());
                user.get().setPassword(encodedPassword);
                userRepository.save(user.get());
                return responseService.formulateResponse(
                        null,
                        "Password reset successfully",
                        HttpStatus.OK,
                        null,
                        true
                );
            }
        }catch (Exception e) {
            log.error("Encountered Exception {}", e.getMessage());
            return responseService.formulateResponse(
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    false
            );
        }
    }

    public ResponseEntity changePassword(String token, ChangePasswordDto changePasswordDto) {
        try {
            Long userID = jwtService.extractuserID(token);
            Optional<User> user = userRepository.findByUserID(userID);
            if (user.isEmpty()) {
                return responseService.formulateResponse(
                        null,
                        "User not found",
                        HttpStatus.NOT_FOUND,
                        null,
                        false
                );
            }

            if (encoder.matches(changePasswordDto.getCurrentPassword(), user.get().getPassword())) {
                String encodedPassword = encoder.encode(changePasswordDto.getNewPassword());
                user.get().setPassword(encodedPassword);
                userRepository.save(user.get());
                return responseService.formulateResponse(
                        null,
                        "Password changed successfully",
                        HttpStatus.OK,
                        null,
                        true
                );
            }
            return responseService.formulateResponse(
                    null,
                    "Incorrect Password",
                    HttpStatus.UNAUTHORIZED,
                    null,
                    false
            );

        } catch (Exception e) {
            log.error("Encountered Exception {}", e.getMessage());
            return responseService.formulateResponse(
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    false
            );
        }
    }
}
