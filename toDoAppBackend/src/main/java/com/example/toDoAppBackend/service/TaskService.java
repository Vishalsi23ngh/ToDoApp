package com.example.toDoAppBackend.service;

import com.example.toDoAppBackend.model.Task;
import com.example.toDoAppBackend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private  final TaskRepository taskRepository;

    public  Task createTask(Task task){
        return   taskRepository.save(task);

    }

    // getting the task using the id
    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }


    public Task updateTask(Long id, Task newTask) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            taskRepository.save(newTask);
            return optionalTask.get();
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


    //update the task here

}
