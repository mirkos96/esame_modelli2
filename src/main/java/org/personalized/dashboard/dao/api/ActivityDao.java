package org.personalized.dashboard.dao.api;

import org.personalized.dashboard.model.Activity;

import java.util.List;

/**
 * Created by sudan on 30/5/15.
 */
public interface ActivityDao {

    /**
     * Add an entry of activity for the user
     *
     * @param activity
     */
    void add(Activity activity, String userId);

    /**
     * Get the latest activities for the user
     *
     * @param limit
     * @param offset
     * @param userId
     */
    List<Activity> get(int limit, int offset, String userId);
}
