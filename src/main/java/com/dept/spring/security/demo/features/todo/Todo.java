package com.dept.spring.security.demo.features.todo;

import java.time.Instant;
import java.util.UUID;

public class Todo {
    public UUID uuid;
    public String title;
    public String description;
    public Instant dueTime;
    public Boolean completed;
}
