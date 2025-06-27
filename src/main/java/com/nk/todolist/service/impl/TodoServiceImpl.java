package com.nk.todolist.service.impl;

import com.nk.todolist.model.Todo;
import com.nk.todolist.repository.TodoRepository;
import com.nk.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the TodoService interface
 */
@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public Todo createTodo(Todo todo) {
        // Create a new Todo object to ensure we're not updating an existing record
        Todo newTodo = new Todo(todo.getTitle(), todo.isCompleted());
        return todoRepository.save(newTodo);
    }

    @Override
    public ResponseEntity<Todo> getTodoById(Long id) {
        return todoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Todo> updateTodo(Long id, Todo todoDetails) {
        return todoRepository.findById(id)
                .map(existingTodo -> {
                    // Update the todo fields
                    existingTodo.setTitle(todoDetails.getTitle());
                    existingTodo.setCompleted(todoDetails.isCompleted());

                    // Save the updated todo
                    Todo updatedTodo = todoRepository.save(existingTodo);
                    return ResponseEntity.ok(updatedTodo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> deleteTodo(Long id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todoRepository.delete(todo);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
