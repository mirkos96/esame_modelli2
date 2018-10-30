package org.personalized.dashboard.service.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.dao.api.ActivityDao;
import org.personalized.dashboard.dao.api.TodoDao;
import org.personalized.dashboard.model.*;
import org.personalized.dashboard.queue.ESIndexProducer;
import org.personalized.dashboard.service.api.TodoService;
import org.personalized.dashboard.utils.generator.ActivityGenerator;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
@Repository
public class TodoServiceImpl implements TodoService {

    private final TodoDao todoDao;
    private final SessionManager sessionManager;
    private final ActivityGenerator activityGenerator;
    private final ActivityDao activityDao;
    private final ESIndexProducer esIndexProducer;

    @Inject
    public TodoServiceImpl(TodoDao todoDao, SessionManager sessionManager,
                           ActivityGenerator activityGenerator,
                           ActivityDao activityDao, @Named("todo") ESIndexProducer esIndexProducer) {
        this.todoDao = todoDao;
        this.sessionManager = sessionManager;
        this.activityGenerator = activityGenerator;
        this.activityDao = activityDao;
        this.esIndexProducer = esIndexProducer;
    }

    @Override
    public String createTodo(Todo todo) {
        String todoId = todoDao.create(todo, sessionManager.getUserIdFromSession());
        Activity activity = activityGenerator.generate(ActivityType.CREATED, EntityType.TODO,
                todoId, todo.getTitle());
        activityDao.add(activity, sessionManager.getUserIdFromSession());
        esIndexProducer.enqueue(todo, EntityType.TODO, OperationType.CREATE, todoId,
                sessionManager.getUserIdFromSession());
        return todoId;
    }

    @Override
    public Todo getTodo(String todoId) {
        return todoDao.get(todoId, sessionManager.getUserIdFromSession());
    }

    @Override
    public Long updateTodo(Todo todo) {
        Long modifiedCount = todoDao.update(todo, sessionManager.getUserIdFromSession());
        if (modifiedCount > 0) {
            Activity activity = activityGenerator.generate(ActivityType.UPDATED, EntityType.TODO,
                    todo.getTodoId(), todo.getTitle());
            activityDao.add(activity, sessionManager.getUserIdFromSession());
            esIndexProducer.enqueue(todo, EntityType.TODO, OperationType.UPDATE, todo.getTodoId(),
                    sessionManager.getUserIdFromSession());

        }
        return modifiedCount;
    }

    @Override
    public void deleteTodo(String todoId) {
        Long deletedCount = todoDao.delete(todoId, sessionManager.getUserIdFromSession());
        if (deletedCount > 0) {
            Activity activity = activityGenerator.generate(ActivityType.DELETED, EntityType.TODO,
                    todoId, StringUtils.EMPTY);
            activityDao.add(activity, sessionManager.getUserIdFromSession());
            esIndexProducer.enqueue(null, EntityType.TODO, OperationType.DELETE, todoId,
                    sessionManager.getUserIdFromSession());
        }
    }

    @Override
    public Long countTodos() {
        return todoDao.count(sessionManager.getUserIdFromSession());
    }

    @Override
    public List<Todo> fetchTodos(int limit, int offset) {
        return todoDao.get(limit, offset, sessionManager.getUserIdFromSession());
    }
}
