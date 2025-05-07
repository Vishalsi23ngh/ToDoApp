package com.example.toDoAppBackend.controller;

import com.example.toDoAppBackend.model.Task;
import com.example.toDoAppBackend.model.User;
import com.example.toDoAppBackend.repository.UserRepository;
import com.example.toDoAppBackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {
   private  final  TaskService taskService;

   private  final UserRepository userRepository;
    //start  create task here
    @PostMapping("/add/task")
    public ResponseEntity<Task> createTask(@RequestBody  Task task ){
       Task createTask = taskService.createTask(task);
//       if(createTask != null && task.getId() != null){
//           Optional<User> user = userRepository.findById(task.getId());
//           user.ifPresent(userData ->);
//       }
        return ResponseEntity.ok(createTask);
    }

//    private  static  boolean isABoolean(Task task){
//        return  (task.getTask().isEmpty() || task.getTask() == null);
//    }

    public ResponseEntity<List<Task>> getTaskByDateRange(@RequestParam(required = true) Long id, @RequestParam(required = false) Integer days){
        List<Task>  tasks = taskService.getTasksByDateRange(id, days);
        return ResponseEntity.ok(tasks);
    }
    // create task ending here
    @GetMapping("/task/get/{id}")
    public  ResponseEntity<Task> getTasks(@PathVariable Long id){

        Optional<Task> optionalTask = taskService.getTask(id );
        return  ResponseEntity.of(optionalTask);
    }


    @PutMapping("/task/update")
    public  ResponseEntity<Task> updateTask(@RequestParam Long id, @RequestBody Task task){
        Task updatedTask = taskService.updateTask(id,task.getOperation(),task.getTask(),task.getUrgency(),task.getDateTime());
        return  ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/task/getAll")
    public  ResponseEntity<List<Task>> getAllTask(){
        List<Task> tasks = taskService.getAll();
        return  ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/task/delete/{id}")
    public  void  deleteTask(@PathVariable Long id){
        boolean deleteTask = taskService.deleteTask(id);
        if(deleteTask){
            System.out.println("task deleted successfully");
        }else{
            System.out.println("invalid task id");
        }
    }
}
