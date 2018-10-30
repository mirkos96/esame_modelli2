package org.personalized.dashboard.service.impl;

import com.google.inject.Inject;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.dao.api.ActivityDao;
import org.personalized.dashboard.model.Activity;
import org.personalized.dashboard.service.api.ActivityService;
import org.personalized.dashboard.utils.Constants;

import java.util.List;

/**
 * Created by sudan on 30/5/15.
 */
public class ActivityServiceImpl implements ActivityService {

    private final ActivityDao activityDao;
    private final SessionManager sessionManager;

    @Inject
    public ActivityServiceImpl(ActivityDao activityDao, SessionManager sessionManager) {
        this.activityDao = activityDao;
        this.sessionManager = sessionManager;
    }

    @Override
    public List<Activity> get() {
        return activityDao.get(Constants.ACTIVITIES_LIMIT, 0, sessionManager.getUserIdFromSession
                ());
    }
}
