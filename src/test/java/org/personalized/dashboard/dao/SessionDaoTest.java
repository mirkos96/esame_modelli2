package org.personalized.dashboard.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.bootstrap.ConfigManager;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.SessionDao;
import org.personalized.dashboard.dao.impl.SessionDaoImpl;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.Constants;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

/**
 * Created by sudan on 26/7/15.
 */
@ActiveProfiles("test")
public class SessionDaoTest {

    private SessionDao sessionDao;

    @Before
    public void initialize() throws IOException {
        ConfigManager.init();
        this.sessionDao = new SessionDaoImpl();
    }

    @Test
    public void testSessionDao() {

        Boolean isDebugMode = Boolean.valueOf(ConfigKeys.MONGO_DEBUG_FLAG);

        /*
            To run these test cases enable isDebugMode in config.properties
         */
        if (isDebugMode) {
            MongoBootstrap.init();
            MongoBootstrap.getMongoDatabase().getCollection(Constants.SESSIONS).drop();

            sessionDao.create("session1", "user1");
            sessionDao.create("session2", "user2");

            String userId1 = sessionDao.getUserId("session1");
            String userId2 = sessionDao.getUserId("session2");
            String userId3 = sessionDao.getUserId("session3");

            Assert.assertEquals("userId is user1", "user1", userId1);
            Assert.assertEquals("userId is user2", "user2", userId2);
            Assert.assertNull("userId is null", userId3);

            sessionDao.delete("session1");

            userId1 = sessionDao.getUserId("session1");
            userId2 = sessionDao.getUserId("session2");

            Assert.assertNull("userId is null", userId1);
            Assert.assertEquals("userId is user2", "user2", userId2);


        }
    }
}
