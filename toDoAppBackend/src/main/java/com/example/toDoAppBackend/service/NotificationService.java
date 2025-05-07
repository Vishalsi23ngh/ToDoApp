package com.example.toDoAppBackend.service;

import com.example.toDoAppBackend.model.Task;
import com.example.toDoAppBackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    EmailService emailService ;

    public  void  sendTaskNotification(Task task, User user, Boolean isUpdated){
        if(user!= null){
            String action = isUpdated ? "updated": "created";
            String emailMessage = String.format("Task %s: %s\n Urgency %s\n Due: %s",action,task.getTask(),task.getUrgency(),task.getDateTime());
            String subject = "Task" + action.substring(1,2).toUpperCase() + action.substring(2).toUpperCase();

        emailService.sendTaskNotification(user.getEmail(),subject, emailMessage);
        }
    }
}
