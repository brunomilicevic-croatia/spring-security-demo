package com.dept.spring.security.demo.features.todo;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TodoRepository {

    private final Logger log = LoggerFactory.getLogger(TodoRepository.class);
    private final Map<UUID, Todo> todoStore = new HashMap<>();

    @PostConstruct
    public void afterBeanCreated() {
        var todo = new Todo();
        todo.uuid = UUID.randomUUID();
        todo.dueTime = Instant.now();
        todo.title = "Seeded two";
        todoStore.put(todo.uuid, todo);
    }

    public Collection<Todo> findAll() {
        return todoStore.values();
    }

    public Todo create(TodoRequest todoRequest) {
        var todo = new Todo();
        todo.uuid = UUID.randomUUID();
        todo.completed = false;
        todo.description = todoRequest.description;
        todo.title = todoRequest.title;
        todo.dueTime = todoRequest.dueTime;
        todoStore.put(UUID.randomUUID(), todo);
        return todo;
    }
}
