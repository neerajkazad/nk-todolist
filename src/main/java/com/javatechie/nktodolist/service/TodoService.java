package com.javatechie.nktodolist.service;

import com.javatechie.nktodolist.model.Todo;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Service interface for Todo operations
 */
public interface TodoService {
    
    /**
     * Get all todos
     * @return List of all todos
     */
    List<Todo> getAllTodos();
    
    /**
     * Create a new todo
     * @param todo The todo to create
     * @return The created todo
     */
    Todo createTodo(Todo todo);
    
    /**
     * Get a todo by its ID
     * @param id The ID of the todo to retrieve
     * @return ResponseEntity containing the todo if found, or not found status
     */
    ResponseEntity<Todo> getTodoById(Long id);
    
    /**
     * Update an existing todo
     * @param id The ID of the todo to update
     * @param todoDetails The updated todo details
     * @return ResponseEntity containing the updated todo if found, or not found status
     */
    ResponseEntity<Todo> updateTodo(Long id, Todo todoDetails);
    
    /**
     * Delete a todo by its ID
     * @param id The ID of the todo to delete
     * @return ResponseEntity with success status if deleted, or not found status
     */
    ResponseEntity<?> deleteTodo(Long id);
}