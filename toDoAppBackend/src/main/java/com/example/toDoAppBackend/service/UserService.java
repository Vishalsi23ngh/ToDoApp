package com.example.toDoAppBackend.service;

import com.example.toDoAppBackend.Analyzer.MyAnalyzer;
import com.example.toDoAppBackend.model.User;
import com.example.toDoAppBackend.repository.UserRepository;
import com.example.toDoAppBackend.util.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    MyAnalyzer myAnalyzer;

    @Autowired
    UserHelper userHelper;

   private  final UserRepository userRepository;

   public User findUserByEmail(String email){
       String processedEmail = myAnalyzer.stem(email);
       if(userRepository.existsByEmail(processedEmail)){
          Optional<User> user = userRepository.findByEmail(email);
          return  user.orElse(null);
      }
      return  null;
   }

   public  User saveToDb(String email, String password){
       String processedEmail = myAnalyzer.stem(email);
       User user = User.builder().email(processedEmail).password(password).build();
       if(userRepository.existsByEmail(processedEmail)){
           return  null;
       }else {
           return userRepository.save(user);
       }
   }
}
