package com.dept.spring.security.demo.features.todo;

import static java.lang.String.format;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import javax.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {

    private final TodoRepository todoRepo;

    public TodoController(TodoRepository todoRepo) {
        this.todoRepo = todoRepo;
    }

    @GetMapping("/todos")
    public ResponseEntity<Collection<Todo>> getAll() {
        return ResponseEntity.ok(todoRepo.findAll());
    }

    @PostMapping("/todos")
    public ResponseEntity<Void> create(@RequestBody TodoRequest todoRequest) throws URISyntaxException {
        Todo newTodo = todoRepo.create(todoRequest);
        return ResponseEntity.created(generateTodoLocation(newTodo)).build();
    }

    private URI generateTodoLocation(Todo newTodo) throws URISyntaxException {
        return new URI(format("/todos/%s", newTodo.uuid));
    }

}
