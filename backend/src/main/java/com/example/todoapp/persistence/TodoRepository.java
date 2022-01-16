package com.example.todoapp.persistence;
import java.util.List;

import com.example.todoapp.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
    // @Query("SELECT * FROM Todo t where t.userId = ?1")
    List<TodoEntity> findByUserId(String userId);
}
