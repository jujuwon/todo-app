package com.example.todoapp.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import com.example.todoapp.dto.ResponseDTO;
import com.example.todoapp.dto.TestRequestBodyDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping
    public String testController() {
        return "Hello World!";
    }
    @GetMapping("/testGetMapping")
    public String testControllerPath() {
        return "Hello World! testGetMapping";
    }
    @GetMapping("/{id}")
    public String testControllerPathVariables(@PathVariable(required = false) int id) {
        return "Hello World! ID " + id;
    }
    @GetMapping("/testRequestParam")
    public String testControllerRequestParam(@RequestParam(required = false) int id) {
        return "Hello World! ID " + id;
    }
    @GetMapping("/testRequestBody")
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
        return "Hello World! ID " + testRequestBodyDTO.getId() + " Message : " + testRequestBodyDTO.getMessage();
    }
    @GetMapping("/testResponseBody")
    public ResponseDTO<String> testControllerResponseBody() {
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm ResponseDTO");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return response;
    }
    @GetMapping("/testResponseEntity")
    public ResponseEntity<?> testControllerResponseEntity() {
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm ResponseEntity. And you get 400!");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        // http status 를 400 으로 설정
        return ResponseEntity.ok().body(response);
    }
}
