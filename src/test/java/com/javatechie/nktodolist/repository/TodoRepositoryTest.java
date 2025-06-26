package com.javatechie.nktodolist.repository;

import com.javatechie.nktodolist.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    private Todo todo1;
    private Todo todo2;

    @BeforeEach
    void setUp() {
        // Create and persist test todos
        todo1 = new Todo("Buy groceries", false);
        todo2 = new Todo("Finish homework", true);
        
        // Use TestEntityManager to persist the test data
        entityManager.persist(todo1);
        entityManager.persist(todo2);
        entityManager.flush();
    }

    @Test
    void findAll_shouldReturnAllTodos() {
        // When
        List<Todo> todos = todoRepository.findAll();
        
        // Then
        assertEquals(2, todos.size());
        assertTrue(todos.stream().anyMatch(t -> t.getTitle().equals("Buy groceries") && !t.isCompleted()));
        assertTrue(todos.stream().anyMatch(t -> t.getTitle().equals("Finish homework") && t.isCompleted()));
    }

    @Test
    void findById_whenTodoExists_shouldReturnTodo() {
        // When
        Optional<Todo> foundTodo = todoRepository.findById(todo1.getId());
        
        // Then
        assertTrue(foundTodo.isPresent());
        assertEquals("Buy groceries", foundTodo.get().getTitle());
        assertFalse(foundTodo.get().isCompleted());
    }

    @Test
    void findById_whenTodoDoesNotExist_shouldReturnEmpty() {
        // When
        Optional<Todo> foundTodo = todoRepository.findById(999L);
        
        // Then
        assertFalse(foundTodo.isPresent());
    }

    @Test
    void save_shouldPersistTodo() {
        // Given
        Todo newTodo = new Todo("Learn Spring Boot", false);
        
        // When
        Todo savedTodo = todoRepository.save(newTodo);
        
        // Then
        assertNotNull(savedTodo.getId());
        
        // Verify it was actually persisted
        Todo persistedTodo = entityManager.find(Todo.class, savedTodo.getId());
        assertNotNull(persistedTodo);
        assertEquals("Learn Spring Boot", persistedTodo.getTitle());
        assertFalse(persistedTodo.isCompleted());
    }

    @Test
    void save_shouldUpdateExistingTodo() {
        // Given
        Todo todoToUpdate = todoRepository.findById(todo1.getId()).orElseThrow();
        todoToUpdate.setTitle("Buy groceries updated");
        todoToUpdate.setCompleted(true);
        
        // When
        Todo updatedTodo = todoRepository.save(todoToUpdate);
        
        // Then
        assertEquals(todo1.getId(), updatedTodo.getId());
        assertEquals("Buy groceries updated", updatedTodo.getTitle());
        assertTrue(updatedTodo.isCompleted());
        
        // Verify it was actually updated in the database
        Todo persistedTodo = entityManager.find(Todo.class, todo1.getId());
        assertEquals("Buy groceries updated", persistedTodo.getTitle());
        assertTrue(persistedTodo.isCompleted());
    }

    @Test
    void delete_shouldRemoveTodo() {
        // Given
        Todo todoToDelete = todoRepository.findById(todo1.getId()).orElseThrow();
        
        // When
        todoRepository.delete(todoToDelete);
        
        // Then
        // Verify it was actually removed from the database
        Todo deletedTodo = entityManager.find(Todo.class, todo1.getId());
        assertNull(deletedTodo);
        
        // Verify the other todo still exists
        Todo remainingTodo = entityManager.find(Todo.class, todo2.getId());
        assertNotNull(remainingTodo);
    }
}