package com.dubey.dubey_porfolio_java.service.impl;


import com.dubey.dubey_porfolio_java.service.DubeyService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DubeyServiceImpl implements DubeyService {

    @Override
    public String getHello() {
        return "<h1>Hello, World! (GET - Service), my name is Sanjay Dubey! <br/> This hello world gradle and java microservice is deployed on ECS Fargate from ECR using GitHub Actions</h1>";
    }

    @Override
    public String postHello(Map<String, String> body) {
        String name = body.getOrDefault("name", "World");
        return "Hello, " + name + "! (POST - Service)";
    }

    @Override
    public String putHello(Map<String, String> body) {
        String name = body.getOrDefault("name", "World");
        return "Hello, " + name + "! (PUT - Service)";
    }

    @Override
    public String deleteHello() {
        return "Goodbye, World! (DELETE - Service)";
    }

    @Override
    public String patchHello(Map<String, String> body) {
        String name = body.getOrDefault("name", "World");
        return "Hello, " + name + "! (PATCH - Service)";
    }
}
