package com.example.toDoAppBackend.controller;

import com.example.toDoAppBackend.model.EmailRequest;
import com.example.toDoAppBackend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:5500")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest){
        try {
            if(emailRequest.getTo().isEmpty() || emailRequest.getMessage().isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            emailService.sendTaskNotification(emailRequest.getTo(),emailRequest.getMessage(),emailRequest.getFrom());
            return  ResponseEntity.ok().body("Email sent successfully");
        }catch (Exception e){
            return  ResponseEntity.badRequest().body("Failed to send email =" + e.getMessage());
        }
    }
}
