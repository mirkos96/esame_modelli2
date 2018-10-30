package org.personalized.dashboard.service.api;

/**
 * Created by sudan on 26/7/15.
 */
public interface SessionService {

    /**
     * Get the userId from sessionId
     *
     * @param sessionId
     * @return
     */
    String getUserId(String sessionId);
}
