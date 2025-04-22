package com.example.toDoAppBackend.controller;


import com.example.toDoAppBackend.config.JwtConfig;
import com.example.toDoAppBackend.model.User;
import com.example.toDoAppBackend.repository.UserRepository;
import com.example.toDoAppBackend.service.JwtService;
import com.example.toDoAppBackend.service.UserService;
import com.example.toDoAppBackend.util.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5500")
@RequiredArgsConstructor
public class AuthController {

    private  final JwtConfig jwtConfig;

    private  final UserRepository userRepository;

    private  final  JwtService jwtService;
    private  final  UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String token){
        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            if(jwtConfig.isTokenExpired(token)){
                String email = jwtConfig.getUserEmail(token);
                String password = jwtConfig.getUserPassword(token);

                return  userRepository.findByEmail(email).filter(user -> user.getPassword().equals(password))
                        .map(user -> {
                            Map<String, Object> response = new HashMap<>();
                            response.put("id", user.getId());
                            response.put("email", user.getEmail());
                            return  ResponseEntity.ok(response);
                        })
                        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                }
            }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }


    @PostMapping("/signup")
    public  ResponseEntity<?> signup(@RequestHeader("Authorization") String token){
        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            if(jwtService.validateToken(token)){
                String email = jwtService.getEmailFromClaims(token);
                String password = jwtService.getPassword(token);
                User user =userService.saveToDb(email,password);
                if(user == null){
                    return  ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message","email already exist"));
                }
                Map<String, Object> response = new HashMap<>();
                response.put("Id",user.getId());
                response.put("email:",user.getEmail());
            }
        }
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
