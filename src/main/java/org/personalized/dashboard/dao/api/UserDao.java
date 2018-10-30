package org.personalized.dashboard.dao.api;

import org.personalized.dashboard.model.User;

/**
 * Created by sudan on 26/7/15.
 */
public interface UserDao {

    /**
     * Create or update user based on email
     *
     * @param user
     * @return
     */
    String upsert(User user);

    /**
     * Get the user object
     *
     * @param userId
     * @return
     */
    User get(String userId);
}
