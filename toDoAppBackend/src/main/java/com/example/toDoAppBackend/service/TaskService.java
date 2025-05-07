package com.example.toDoAppBackend.service;

import com.example.toDoAppBackend.Analyzer.MyAnalyzer;
import com.example.toDoAppBackend.model.Task;
import com.example.toDoAppBackend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private  final TaskRepository taskRepository;

    private  final MyAnalyzer myAnalyzer;

    public  Task createTask(Task task){

        Task newTask = new Task();
        String processedMessage = myAnalyzer.stem(task.getTask());
        newTask.setId(task.getId());
        newTask.setOperation(task.getOperation());
        newTask.setTask(task.getTask());
        newTask.setUrgency(task.getUrgency());
        newTask.setDateTime(task.getDateTime());
        newTask.setId(task.getId());
        return  taskRepository.save(newTask);

    }


    // getting the task using the id
    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }


    public Task updateTask(Long id,String operation, String task, String urgency, String datetime ) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            Task existingTask = optionalTask.get();
            existingTask.setOperation(operation);
            existingTask.setUrgency(urgency);
            existingTask.setTask(myAnalyzer.stem(task));
            existingTask.setDateTime(datetime);
            return  taskRepository.save(existingTask);
        }else {
            throw  new RuntimeException("give the valid id");
        }
    }

    public List<Task> getAll() {
        return  taskRepository.findAll();
    }

    public boolean deleteTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            taskRepository.deleteById(id);
            return  true;
        }else{
            return false;
        }
    }

    public List<Task> getTasksByDateRange(Long id, Integer days) {
        List<Task> tasks;
        tasks = taskRepository.findByUserId(id);

        if(days == null || days < 0){
            return  tasks;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(days);
        return  tasks.stream().filter(task -> {
            try {
                LocalDateTime taskData = parseDateTime(task.getDateTime());
                return  taskData != null && !taskData.isBefore(now) && !taskData.isAfter(endDate);
            }catch (Exception exception){
                return  false;
            }
        }).collect(Collectors.toList());
    }

    private LocalDateTime parseDateTime(String dateTime) {
        if(dateTime == null || dateTime.isEmpty()){
            return  null;
        }
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }catch (Exception exception){
            try {
                return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
            }catch (Exception exception1){
                throw  new IllegalArgumentException("Invalid Date time Format");
            }
        }
    }



}
