package com.example.todoapp.service;

import java.util.List;

import com.example.todoapp.model.TodoEntity;
import com.example.todoapp.persistence.TodoRepository;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public List<TodoEntity> create(final TodoEntity entity) {
        // Validations
        validate(entity);

        repository.save(entity);

        log.info("Entity Id : {} is saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity) {
        // (1) 저장할 엔티티가 유효한지 확인
        validate(entity);

        // (2) 넘겨받은 엔티티 id 를 이용해 TodoEntity 가져옴.
        // 존재하지 않는 엔티티는 업데이트 할 수 없기 때문
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        if(original.isPresent()) {
            // (3) 반환된 TodoEntity 가 존재하면 값을 새 entity 값으로 덮어쓰기
            final TodoEntity todo = original.get();
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // (4) DB 에 새 값을 저장
            repository.save(todo);
        }

        // Retrieve Todo 에서 만든 메소드를 이용해 사용자의 모든 Todo 리스트 리턴
        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity) {
        // (1) 저장할 엔티티가 유효한지 확인한다.
        validate(entity);

        try {
            // (2) 엔티티 삭제
            repository.delete(entity);
        } catch (Exception e) {
            // (3) exception 발생 시 id 와 exception 로깅
            log.error("error deleting entity", entity.getId(), e);

            // (4) 컨트롤러로 exception 보내기
            // DB 내부 로직을 캡슐화하려면 e를 리턴하지 않고 새 exception 오브젝트 리턴
            throw new RuntimeException("error deleting entity " + entity.getId());
        }

        // (5) 새 Todo 리스트를 가져와 리턴
        return retrieve(entity.getUserId());
    }

    private void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Entity cannot be null.");
        }
    }
    
    public String testService() {
        // TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        // TodoEntity 저장
        repository.save(entity);
        // TodoEntity 검색
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }
}
