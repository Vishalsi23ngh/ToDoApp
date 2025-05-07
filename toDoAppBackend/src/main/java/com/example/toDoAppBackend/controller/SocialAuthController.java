package com.example.toDoAppBackend.controller;

import com.example.toDoAppBackend.model.SocialLoginRequest;
import com.example.toDoAppBackend.model.User;
import com.example.toDoAppBackend.repository.UserRepository;
import com.example.toDoAppBackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/social-login")
@CrossOrigin(origins = "http://localhost:5500")
public class SocialAuthController {



    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> socialLogin(@RequestBody SocialLoginRequest request){
        try {
            String email = request.getUserData().getEmail();

            Optional<User> existingUser  = userRepository.findByEmail(email);

            if(existingUser.isPresent()){
                User user = existingUser.get();
                Map<String, Object> response = new HashMap<>();
                response.put("user",Map.of(
                        "id",user.getId(),
                        "email",user.getEmail()
                ));
                return  ResponseEntity.ok(response);
            }else{
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setPassword("social_"+System.currentTimeMillis());
                User savedUser = userRepository.save(newUser);
                Map<String,Object> response = new HashMap<>();
                response.put("user",Map.of(
                        "id",savedUser.getId(),
                        "email",savedUser.getEmail()
                ));

                return  ResponseEntity.ok(response);
            }
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message :", "failed to process social login"));
        }
    }
}
