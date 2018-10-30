package org.personalized.dashboard.service.api;

import org.personalized.dashboard.model.Todo;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface TodoService {

    /**
     * Create a new todo for the user
     *
     * @param todo
     * @return
     */
    String createTodo(Todo todo);

    /**
     * Get the todo for the todoId
     *
     * @param todoId
     * @return
     */
    Todo getTodo(String todoId);

    /**
     * Updates the todo
     *
     * @param todo
     * @return
     */
    Long updateTodo(Todo todo);

    /**
     * Delete the todo
     *
     * @param todoId
     * @return
     */
    void deleteTodo(String todoId);


    /**
     * Count the todos for the user
     *
     * @return
     */
    Long countTodos();


    /**
     * Fetch the todos with given limit and offset
     *
     * @param limit
     * @param offset
     * @return
     */
    List<Todo> fetchTodos(int limit, int offset);

}
