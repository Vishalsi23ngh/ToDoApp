package com.example.toDoAppBackend.util;

import com.example.toDoAppBackend.Analyzer.MyAnalyzer;
import com.example.toDoAppBackend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserHelper {
    private  final MyAnalyzer myAnalyzer;

    public boolean isValidUser(String email, String password, User user){
        String processedEmail =myAnalyzer.stem(email);
        if(user.getEmail().toLowerCase().trim().equals(email) && user.getPassword().equals(password)){
            return  true;
        }
        return  false;
    }
}
