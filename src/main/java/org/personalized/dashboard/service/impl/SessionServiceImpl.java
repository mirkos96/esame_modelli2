package org.personalized.dashboard.service.impl;

import com.google.inject.Inject;
import org.personalized.dashboard.dao.api.SessionDao;
import org.personalized.dashboard.service.api.SessionService;

/**
 * Created by sudan on 26/7/15.
 */
public class SessionServiceImpl implements SessionService {

    private SessionDao sessionDao;

    @Inject
    public SessionServiceImpl(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public String getUserId(String sessionId) {
        return sessionDao.getUserId(sessionId);
    }
}
