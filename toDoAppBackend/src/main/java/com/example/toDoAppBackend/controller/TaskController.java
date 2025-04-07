package com.example.toDoAppBackend.controller;

import com.example.toDoAppBackend.model.Task;
import com.example.toDoAppBackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {
   private  final  TaskService taskService;
    //start  create task here
    @PostMapping("/add/task")
    public ResponseEntity<Task> createTask(@RequestBody  Task task){

        System.out.println("Got Task");
        Task taskResponse = taskService.createTask(task);
        return  ResponseEntity.ok(taskResponse);
    }

//    private  static  boolean isABoolean(Task task){
//        return  (task.getTask().isEmpty() || task.getTask() == null);
//    }

    // create task ending here
    @GetMapping("/task/get/{id}")
    public  ResponseEntity<Task> getTaskById(@PathVariable Long id){
        System.out.println("returning Task");
        Optional<Task> optionalTask = taskService.getTask(id);
        return  ResponseEntity.of(optionalTask);
    }


    @PutMapping("/task/update")
    public  ResponseEntity<Task> updateTask(@RequestParam Long id, @RequestBody Task task){
        Task updatedTask = taskService.updateTask(id, task);
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
