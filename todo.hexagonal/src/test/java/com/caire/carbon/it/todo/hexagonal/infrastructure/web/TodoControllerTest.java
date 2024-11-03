package com.caire.carbon.it.todo.hexagonal.infrastructure.web;

import com.caire.carbon.it.todo.hexagonal.domain.exception.DuplicateTitleException;
import com.caire.carbon.it.todo.hexagonal.domain.model.Todo;
import com.caire.carbon.it.todo.hexagonal.domain.service.ITodoService;
import com.caire.carbon.it.todo.hexagonal.infrastructure.web.dto.TodoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("getAllTodos() testing : should return 200 status code")
    void shouldReturnAllTodosInAscendingOrderOfId() throws Exception {
        // Arrange
        Todo todo1 = new Todo(1L, "Task 1", false);
        Todo todo2 = new Todo(2L, "Task 2", true);
        List<Todo> todos = Arrays.asList(todo1, todo2);

        when(todoService.getAllTodos()).thenReturn(todos);

        // Act & Assert
        mockMvc.perform(get("/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].order", is(1)))
                .andExpect(jsonPath("$[1].order", is(2)));
    }

    @Test
    @DisplayName("createTodo() testing : should Return 201 status code")
    public void createTodo_ShouldReturn201_WhenTodoIsCreated() throws Exception {
        Todo todo = new Todo(1L, "New Todo", false);
        when(todoService.createTodo(any(Todo.class))).thenReturn(todo);
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"New Todo\", \"completed\": false}"))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/todos/1"))
                .andExpect(jsonPath("$.order").value(1))
                .andExpect(jsonPath("$.title").value("New Todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    @DisplayName("createTodo() testing : should Return 409 status code")
    void createTodo_withDuplicateTitle_returns409() throws Exception {
        Todo todo = new Todo(1L, "Existing Todo", false);

        when(todoService.createTodo(any(Todo.class)))
                .thenThrow(new DuplicateTitleException("Un todo avec le titre 'Existing Todo' existe déjà."));

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Un todo avec le titre 'Existing Todo' existe déjà."));
    }

    @Test
    @DisplayName("createTodo() testing : should Return 400 status code")
    public void createTodo_WithInvalidData_ShouldReturn400() throws Exception {
        // Given
        Todo todo = new Todo(1L, "", false);
        // When
        when(todoService.createTodo(any(Todo.class))).thenThrow(new IllegalArgumentException("Invalid todo data"));

        //Then
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("deleteAllTodos() testing : should Return 204 status code")
    public void testDeleteAllTodos() throws Exception {
        // Arrange
        doNothing().when(todoService).deleteTodos();
        // Act & Assert
        mockMvc.perform(delete("/todos"))
                .andExpect(status().isNoContent());
        // Verify
        verify(todoService, times(1)).deleteTodos();
    }

    @ParameterizedTest
    @DisplayName("getTodoById() status testing")
    @CsvSource({
            "1, true, 200",
            "2, false, 404"
    })
    void gettingTodoById_shouldReturn_the_ExpectedStatus(long id, boolean isFound, int expectedStatus) throws Exception {
        Todo todo = new Todo(1L, "New Todo", false);

        when(todoService.findById(id)).thenReturn(
                isFound ? Optional.of(todo) : Optional.empty()
        );

        mockMvc.perform(get("/todos/" + id))
                .andExpect(status().is(expectedStatus));

        verify(todoService, times(1)).findById(id);
    }

    @Test
    @DisplayName("updateTodo() : testing : should Return 400 status code")
    void updateTodo_WithInvalidData_ShouldReturn400() throws Exception {
        Todo todo = new Todo(1L, "", false);
        when(todoService.update(any(Todo.class))).thenThrow(new IllegalArgumentException("Invalid todo data, title is Empty"));

        mockMvc.perform(put("/todos/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("updateTodo() testing : should Return 404 status code")
    void updateTodo_WithInvalidData_ShouldReturn404() throws Exception {
        Todo updatedTodo = new Todo(1L, "LOL", false);
        when(todoService.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(put("/todos/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("updateTodo() testing : should Return 409 status code")
    void updateTodo_When_AnOther_TodoWithSameTitleExist_ShouldReturn409() throws Exception {
        Todo existingTodo = new Todo(1L, "Existing Title", false);
        Todo updatedTodo = new Todo(1L, "New Title", false);

        when(todoService.findById(anyLong())).thenReturn(Optional.of(existingTodo));
        when(todoService.existsByTitle(updatedTodo.title())).thenReturn(true);

        mockMvc.perform(put("/todos/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("updateTodo() testing : should Return 200 status code")
    void updateTodo_WhenTodoExist_WithANewTitle_ShouldReturn200() throws Exception {
        Todo existingTodo = new Todo(1L, "Existing Title", false);
        Todo updatedTodo = new Todo(1L, "New Title", false);

        when(todoService.findById(anyLong())).thenReturn(Optional.of(existingTodo));
        when(todoService.existsByTitle(anyString())).thenReturn(false);
        when(todoService.update(any(Todo.class))).thenReturn(updatedTodo);

        mockMvc.perform(put("/todos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingTodo)))
                        .andExpect(status().isOk())
                        .andExpect(result -> assertEquals(updatedTodo.title(),
                        objectMapper.readValue(result.getResponse().getContentAsString(), TodoDTO.class).getTitle()));
    }

    @Test
    @DisplayName("deleteTodoById() testing : should Return 204 status code")
    void testDeleteTodoById() throws Exception {
        doNothing().when(todoService).deleteTodoById(1L);
        // Act & Assert
        mockMvc.perform(delete("/todos/{id}", 1L))
                .andExpect(status().isNoContent());
        // Verify
        verify(todoService, times(1)).deleteTodoById(1L);
    }
}