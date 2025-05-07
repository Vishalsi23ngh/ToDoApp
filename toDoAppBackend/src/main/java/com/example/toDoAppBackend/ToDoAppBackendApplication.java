package com.example.toDoAppBackend;

import com.example.toDoAppBackend.Analyzer.MyAnalyzer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ToDoAppBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(ToDoAppBackendApplication.class, args);
		Set<String> strings = new HashSet<>();
		strings.add("for");
		MyAnalyzer myAnalyzer = new MyAnalyzer(strings,new HashSet<>());
		String result = myAnalyzer.stem("nikes shoes for mens");
		System.out.println(result);

	}


}
