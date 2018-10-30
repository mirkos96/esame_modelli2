package org.personalized.dashboard.service.api;

import org.personalized.dashboard.model.User;

/**
 * Created by sudan on 26/7/15.
 */
public interface UserService {

    /**
     * Get user object
     *
     * @return
     */
    User getUser();

    /**
     * Create or update a user
     *
     * @param user
     * @return
     */
    String upsert(User user);
}
