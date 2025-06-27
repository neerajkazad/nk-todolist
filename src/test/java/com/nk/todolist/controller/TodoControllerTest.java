package com.nk.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nk.todolist.model.Todo;
import com.nk.todolist.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testGetAllTodos() throws Exception {
        Mockito.when(todoService.getAllTodos()).thenReturn(Arrays.asList(todo1));
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Buy groceries"));
    }

    @Test
    void testGetTodoById() throws Exception {
        Mockito.when(todoService.getTodoById(1L)).thenReturn(ResponseEntity.of(Optional.of(todo1)));
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Buy groceries"));
    }

    @Test
    void shouldGetAllTodos() throws Exception {
        when(todoService.getAllTodos()).thenReturn(todoList);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Buy groceries")))
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Finish homework")))
                .andExpect(jsonPath("$[1].completed", is(true)));

        verify(todoService, times(1)).getAllTodos();
    }

    @Test
    void shouldCreateTodo() throws Exception {
        Todo newTodo = new Todo("New task", false);
        Todo savedTodo = new Todo("New task", false);
        savedTodo.setId(3L);

        when(todoService.createTodo(any(Todo.class))).thenReturn(savedTodo);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("New task")))
                .andExpect(jsonPath("$.completed", is(false)));

        verify(todoService, times(1)).createTodo(any(Todo.class));
    }

    @Test
    void shouldGetTodoById() throws Exception {
        when(todoService.getTodoById(1L)).thenReturn(ResponseEntity.ok(todo1));

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Buy groceries")))
                .andExpect(jsonPath("$.completed", is(false)));

        verify(todoService, times(1)).getTodoById(1L);
    }

    @Test
    void shouldReturn404WhenGetTodoByIdNotFound() throws Exception {
        when(todoService.getTodoById(999L)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/todos/999"))
                .andExpect(status().isNotFound());

        verify(todoService, times(1)).getTodoById(999L);
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        Todo updatedTodo = new Todo("Buy groceries updated", true);
        updatedTodo.setId(1L);

        when(todoService.updateTodo(eq(1L), any(Todo.class))).thenReturn(ResponseEntity.ok(updatedTodo));

        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Buy groceries updated")))
                .andExpect(jsonPath("$.completed", is(true)));

        verify(todoService, times(1)).updateTodo(eq(1L), any(Todo.class));
    }

    @Test
    void shouldReturn404WhenUpdateTodoNotFound() throws Exception {
        Todo updatedTodo = new Todo("Non-existent todo", true);

        when(todoService.updateTodo(eq(999L), any(Todo.class))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(put("/api/todos/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isNotFound());

        verify(todoService, times(1)).updateTodo(eq(999L), any(Todo.class));
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        when(todoService.deleteTodo(1L)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isOk());

        verify(todoService, times(1)).deleteTodo(1L);
    }

    @Test
    void shouldReturn404WhenDeleteTodoNotFound() throws Exception {
        when(todoService.deleteTodo(999L)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/api/todos/999"))
                .andExpect(status().isNotFound());

        verify(todoService, times(1)).deleteTodo(999L);
    }
}
