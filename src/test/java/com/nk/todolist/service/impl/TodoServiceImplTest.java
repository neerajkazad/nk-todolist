package com.nk.todolist.service.impl;

import com.nk.todolist.model.Todo;
import com.nk.todolist.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo todo1;
    private Todo todo2;
    private List<Todo> todoList;

    @BeforeEach
    void setUp() {
        todo1 = new Todo("Buy groceries", false);
        todo1.setId(1L);

        todo2 = new Todo("Finish homework", true);
        todo2.setId(2L);

        todoList = Arrays.asList(todo1, todo2);
    }

    @Test
    void getAllTodos_shouldReturnAllTodos() {
        // Given
        when(todoRepository.findAll()).thenReturn(todoList);

        // When
        List<Todo> result = todoService.getAllTodos();

        // Then
        assertEquals(2, result.size());
        assertEquals(todo1, result.get(0));
        assertEquals(todo2, result.get(1));
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void createTodo_shouldSaveAndReturnTodo() {
        // Given
        Todo newTodo = new Todo("New task", false);
        Todo savedTodo = new Todo("New task", false);
        savedTodo.setId(3L);

        when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

        // When
        Todo result = todoService.createTodo(newTodo);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New task", result.getTitle());
        assertFalse(result.isCompleted());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void createTodo_withExistingId_shouldCreateNewTodo() {
        // Given
        Todo todoWithId = new Todo("Task with ID", false);
        todoWithId.setId(8L); // This is the ID from the error message

        Todo savedTodo = new Todo("Task with ID", false);
        savedTodo.setId(3L); // New ID assigned by the database

        // Verify that a new Todo object is created and saved
        when(todoRepository.save(argThat(todo -> 
            todo.getId() == null && // New Todo should have null ID
            todo.getTitle().equals("Task with ID") && 
            !todo.isCompleted()
        ))).thenReturn(savedTodo);

        // When
        Todo result = todoService.createTodo(todoWithId);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getId()); // Should have the new ID
        assertEquals("Task with ID", result.getTitle());
        assertFalse(result.isCompleted());

        // Verify that save was called with a new Todo object (without ID)
        verify(todoRepository, times(1)).save(argThat(todo -> 
            todo.getId() == null && 
            todo.getTitle().equals("Task with ID") && 
            !todo.isCompleted()
        ));
    }

    @Test
    void getTodoById_whenTodoExists_shouldReturnTodo() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo1));

        // When
        ResponseEntity<Todo> response = todoService.getTodoById(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(todo1, response.getBody());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void getTodoById_whenTodoDoesNotExist_shouldReturnNotFound() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Todo> response = todoService.getTodoById(999L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(todoRepository, times(1)).findById(999L);
    }

    @Test
    void updateTodo_whenTodoExists_shouldUpdateAndReturnTodo() {
        // Given
        Todo todoToUpdate = new Todo("Updated task", true);
        Todo updatedTodo = new Todo("Updated task", true);
        updatedTodo.setId(1L);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo1));
        when(todoRepository.save(any(Todo.class))).thenReturn(updatedTodo);

        // When
        ResponseEntity<Todo> response = todoService.updateTodo(1L, todoToUpdate);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Updated task", response.getBody().getTitle());
        assertTrue(response.getBody().isCompleted());
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void updateTodo_whenTodoDoesNotExist_shouldReturnNotFound() {
        // Given
        Todo todoToUpdate = new Todo("Updated task", true);
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Todo> response = todoService.updateTodo(999L, todoToUpdate);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(todoRepository, times(1)).findById(999L);
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void deleteTodo_whenTodoExists_shouldDeleteAndReturnOk() {
        // Given
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo1));
        doNothing().when(todoRepository).delete(any(Todo.class));

        // When
        ResponseEntity<?> response = todoService.deleteTodo(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(todoRepository, times(1)).findById(1L);
        verify(todoRepository, times(1)).delete(any(Todo.class));
    }

    @Test
    void deleteTodo_whenTodoDoesNotExist_shouldReturnNotFound() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> response = todoService.deleteTodo(999L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(todoRepository, times(1)).findById(999L);
        verify(todoRepository, never()).delete(any(Todo.class));
    }
}
