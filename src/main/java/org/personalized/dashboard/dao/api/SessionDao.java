package org.personalized.dashboard.dao.api;

/**
 * Created by sudan on 26/7/15.
 */
public interface SessionDao {

    /**
     * Create a new session for the user
     *
     * @param sessionId
     * @param userId
     */
    void create(String sessionId, String userId);

    /**
     * Delete the session
     *
     * @param sessionId
     */
    void delete(String sessionId);

    /**
     * Get userId from sessionId
     *
     * @param sessionId
     * @return
     */
    String getUserId(String sessionId);
}
