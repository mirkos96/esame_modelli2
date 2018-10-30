package org.personalized.dashboard.service.api;

import org.personalized.dashboard.model.Activity;

import java.util.List;

/**
 * Created by sudan on 30/5/15.
 */
public interface ActivityService {

    /**
     * Get the latest activities for the user
     *
     * @return
     */
    List<Activity> get();
}
