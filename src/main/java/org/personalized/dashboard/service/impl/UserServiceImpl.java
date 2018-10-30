package org.personalized.dashboard.service.impl;

import com.google.inject.Inject;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.dao.api.UserDao;
import org.personalized.dashboard.model.User;
import org.personalized.dashboard.service.api.UserService;

/**
 * Created by sudan on 26/7/15.
 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final SessionManager sessionManager;

    @Inject
    public UserServiceImpl(UserDao userDao, SessionManager sessionManager) {
        this.userDao = userDao;
        this.sessionManager = sessionManager;
    }

    @Override
    public User getUser() {
        return userDao.get(sessionManager.getUserIdFromSession());
    }

    @Override
    public String upsert(User user) {
        return userDao.upsert(user);
    }
}
