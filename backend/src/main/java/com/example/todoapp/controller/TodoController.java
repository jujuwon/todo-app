package com.example.todoapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.todoapp.dto.ResponseDTO;
import com.example.todoapp.dto.TodoDTO;
import com.example.todoapp.model.TodoEntity;
import com.example.todoapp.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;
    
    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.badRequest().body(response);
    }
    
    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";

            // (1) TodoEntity 로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);
            
            // (2) id 를 null 로 초기화
            entity.setId(null);

            // (3) 임시 사용자 아이디 설정
            // todo 인증 부분은 후에 추가
            entity.setUserId(temporaryUserId);

            // (4) 서비스를 이용해 Todo 엔티티 생성
            List<TodoEntity> entities = service.create(entity);

            // (5) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // (6) 변환된 TodoDTO 리스트를 이용해 ResponseDTO 를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // (7) ResponseDTO 를 리턴
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList() {
        String temporaryUserId = "temporary-user";

        // (1) 서비스 메소드의 retrieve() 를 사용해 Todo 리스트 가져오기
        List<TodoEntity> entities = service.retrieve(temporaryUserId);

        // (2) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // (3) 변환된 TodoDTO 리스트를 이용해 ResponseDTO 를 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        // (4) ResponseDTO 를 리턴
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
        String temporaryUserId = "temporary-user";

        // (1) dto 를 entity 로 변환
        TodoEntity entity = TodoDTO.toEntity(dto);

        // (2) id 를 temporaryUserId 로 초기화
        // 이 부분은 후에 인증 추가
        entity.setUserId(temporaryUserId);

        // (3) 서비스를 이용해 entity 를 업데이트
        List<TodoEntity> entities = service.update(entity);

        // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO 를 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        //(6) ResponseDTO 리턴
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
        try {
            String temporaryUserId = "temporary-user";

            // (1) TodoEntity 로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // (2) 임시 사용자 아이디 설정
            // 후에 인증 부분 추가
            entity.setUserId(temporaryUserId);

            // (3) 서비스를 이용해 entity 삭제
            List<TodoEntity> entities = service.delete(entity);

            // (4) 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // (5) 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            // (6) ResponseDTO 리턴
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // (7) 예외 있는 경우 dto 대신 error 에 메시지 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // @GetMapping
    // public ResponseEntity<?> todoControllerResponseEntity() {
    //     List<String> list = new ArrayList<>();
    //     list.add("Success");
    //     ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
    //     return ResponseEntity.ok().body(response);
    // }
}
