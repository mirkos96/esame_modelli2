package org.personalized.dashboard.dao.api;

import org.personalized.dashboard.model.Todo;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface TodoDao {

    /**
     * Create a new todo for the user
     *
     * @param todo
     * @param userId
     * @return
     */
    String create(Todo todo, String userId);

    /**
     * Get the todo for the todoId and userId
     *
     * @param todoId
     * @param userId
     * @return
     */
    Todo get(String todoId, String userId);

    /**
     * Update the todo and return the updated one
     *
     * @param todo
     * @param userId
     * @return
     */
    Long update(Todo todo, String userId);

    /**
     * Delete the todo for the todoId and userId
     *
     * @param todoId
     * @param userId
     */
    Long delete(String todoId, String userId);

    /**
     * Count the number of todos for the userId
     *
     * @param userId
     * @return
     */
    Long count(String userId);

    /**
     * Fetch the todos for the given limit, offset and userId
     *
     * @param limit
     * @param offset
     * @param userId
     * @return
     */
    List<Todo> get(int limit, int offset, String userId);
}
