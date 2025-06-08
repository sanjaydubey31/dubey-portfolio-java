package com.dubey.dubey_porfolio_java.controller;

import com.dubey.dubey_porfolio_java.dto.User;
import com.dubey.dubey_porfolio_java.service.DubeyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping("/api")
public class DubeyController {
    @Autowired
    private  DubeyService dubeyService;

    
    // Handle POST request
    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        // Here you can process the data or save it to a database
        System.out.println("Received User: " + user);
        String message = "User " + user.getFirstName() + " " + user.getLastName() + " created successfully!";
        // Return a simple response
        return new User(message, user.getFirstName(), user.getLastName(), user.getEmail());
    }

    @GetMapping("/hello")
    public String getHello() {

        //if(1 == 1) throw new ResourceNotFoundException("Test exception User not found with ID: " );

        return dubeyService.getHello();
    }

    @PostMapping
    public String postHello(@RequestBody Map<String, String> body) {
        return dubeyService.postHello(body);
    }

    @PutMapping
    public String putHello(@RequestBody Map<String, String> body) {
        return dubeyService.putHello(body);
    }

    @DeleteMapping
    public String deleteHello() {
        return dubeyService.deleteHello();
    }

    @PatchMapping
    public String patchHello(@RequestBody Map<String, String> body) {
        return dubeyService.patchHello(body);
    }
}
