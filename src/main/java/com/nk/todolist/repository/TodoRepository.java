package com.nk.todolist.repository;

import com.nk.todolist.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // Spring Data JPA will automatically implement basic CRUD operations
    // We can add custom query methods here if needed
}