package org.personalized.dashboard.dao;

import com.google.api.client.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.bootstrap.ConfigManager;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.*;
import org.personalized.dashboard.dao.impl.*;
import org.personalized.dashboard.model.*;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sudan on 26/7/15.
 */
@ActiveProfiles("test")
public class UserDaoTest {

    private UserDao userDao;
    private BookmarkDao bookmarkDao;
    private NoteDao noteDao;
    private ExpenseDao expenseDao;
    private TagDao tagDao;

    @Before
    public void initialize() throws IOException {
        ConfigManager.init();
        this.userDao = new UserDaoImpl(new IdGenerator());
        this.bookmarkDao = new BookmarkDaoImpl(new IdGenerator());
        this.noteDao = new NoteDaoImpl(new IdGenerator());
        this.expenseDao = new ExpenseDaoImpl(new IdGenerator());
        this.tagDao = new TagDaoImpl();
    }

    @Test
    public void testUserDao() throws ParseException {

        Boolean isDebugMode = Boolean.valueOf(ConfigKeys.MONGO_DEBUG_FLAG);

        /*
            To run these test cases enable isDebugMode in config.properties
         */
        if (isDebugMode) {
            MongoBootstrap.init();
            MongoBootstrap.getMongoDatabase().getCollection(Constants.USERS).drop();

            User user = new User();
            user.setUsername("sudan");
            user.setEmail("ssudan16@gmail.com");
            user.setProfilePicURL("http://www.google.com");
            String userId = userDao.upsert(user);

            User newUser = userDao.get(userId);

            Assert.assertEquals("UserId validation", userId, newUser.getUserId());
            Assert.assertEquals("Username validation", "sudan", newUser.getUsername());
            Assert.assertEquals("email validation", "ssudan16@gmail.com", newUser.getEmail());
            Assert.assertEquals("Image url validation", "http://www.google.com", newUser.getProfilePicURL());

            user = new User();
            user.setUserId("noeffect");
            user.setUsername("sudan1");
            user.setEmail("ssudan16@gmail.com");
            user.setProfilePicURL("http://www.yahoo.com");
            String updatedUserId = userDao.upsert(user);

            newUser = userDao.get(updatedUserId);

            Assert.assertEquals("UserId validation", updatedUserId, newUser.getUserId());
            Assert.assertEquals("Username validation", "sudan1", newUser.getUsername());
            Assert.assertEquals("email validation", "ssudan16@gmail.com", newUser.getEmail());
            Assert.assertEquals("Image url validation", "http://www.yahoo.com", newUser.getProfilePicURL());

            Bookmark bookmark = new Bookmark();
            bookmark.setName("name");
            bookmark.setDescription("desc");
            bookmark.setUrl("http://www.google.com");
            String bookmarkId = bookmarkDao.create(bookmark, userId);

            Note note = new Note();
            note.setTitle("title");
            note.setNote("note");
            String noteId = noteDao.create(note, userId);

            Entity entity1 = new Entity(EntityType.BOOKMARK, bookmarkId, "name");
            Entity entity2 = new Entity(EntityType.NOTE, noteId, "title");

            List<String> tag1 = Lists.newArrayList();
            tag1.add("name");
            tag1.add("desc");

            List<String> tag2 = Lists.newArrayList();
            tag2.add("title");
            tag2.add("desc");

            tagDao.update(tag1, entity1, userId);
            tagDao.update(tag2, entity2, userId);

            newUser = userDao.get(updatedUserId);

            Assert.assertEquals("UserId validation", updatedUserId, newUser.getUserId());
            Assert.assertEquals("Username validation", "sudan1", newUser.getUsername());
            Assert.assertEquals("email validation", "ssudan16@gmail.com", newUser.getEmail());
            Assert.assertEquals("Image url validation", "http://www.yahoo.com", newUser.getProfilePicURL());

            Assert.assertEquals("Tag count is 3", 3, newUser.getTags().size());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Expense expense = new Expense();
            expense.setTitle("title2");
            expense.setDescription("desc2");
            expense.setAmount(400);
            expense.setDate(dateFormat.parse("2015-06-01").getTime());
            List<String> categories = com.google.common.collect.Lists.newArrayList();
            categories.add("cat1");
            categories.add("cat2");
            categories.add("cat3");
            categories.add("cat1");
            categories.add("cat4");
            categories.add("cat2");
            expense.setCategories(categories);
            expenseDao.create(expense, userId);

            newUser = userDao.get(updatedUserId);

            Assert.assertEquals("UserId validation", updatedUserId, newUser.getUserId());
            Assert.assertEquals("Username validation", "sudan1", newUser.getUsername());
            Assert.assertEquals("email validation", "ssudan16@gmail.com", newUser.getEmail());
            Assert.assertEquals("Image url validation", "http://www.yahoo.com", newUser.getProfilePicURL());

            Assert.assertEquals("Tag count is 3", 3, newUser.getTags().size());
            Assert.assertEquals("Category count is 4", 4, newUser.getExpenseCategories().size());
        }
    }
}
