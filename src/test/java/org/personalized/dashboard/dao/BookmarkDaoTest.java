package org.personalized.dashboard.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.bootstrap.ConfigManager;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.BookmarkDao;
import org.personalized.dashboard.dao.impl.BookmarkDaoImpl;
import org.personalized.dashboard.model.Bookmark;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

/**
 * Created by sudan on 31/5/15.
 */
@ActiveProfiles
public class BookmarkDaoTest {

    private BookmarkDao bookmarkDao;

    @Before
    public void initialize() throws IOException {
        ConfigManager.init();
        this.bookmarkDao = new BookmarkDaoImpl(new IdGenerator());
    }

    @Test
    public void testBookmarkDao() {

        Boolean isDebugMode = Boolean.valueOf(ConfigKeys.MONGO_DEBUG_FLAG);

        /*
            To run these test cases enable isDebugMode in config.properties

         */
        if (isDebugMode) {
            MongoBootstrap.init();
            MongoBootstrap.getMongoDatabase().getCollection(Constants.BOOKMARKS).drop();

            Bookmark bookmark1 = new Bookmark();
            bookmark1.setName("bookmark1");
            bookmark1.setDescription("desc1");
            bookmark1.setUrl("http://www.google.com");

            Bookmark bookmark2 = new Bookmark();
            bookmark2.setName("bookmark2");
            bookmark2.setDescription("desc2");
            bookmark2.setUrl("http://www.yahoo.com");

            // Create two bookmarks
            String bookmarkid1 = bookmarkDao.create(bookmark1, "1");
            String bookmarkid2 = bookmarkDao.create(bookmark2, "1");

            Bookmark bookmarkRead1 = bookmarkDao.get(bookmarkid1, "1");
            Bookmark bookmarkRead2 = bookmarkDao.get(bookmarkid2, "1");

            // Verify the created values
            Assert.assertEquals("BookmarkIds match", bookmarkid1, bookmarkRead1.getBookmarkId());
            Assert.assertEquals("name match", "bookmark1", bookmarkRead1.getName());
            Assert.assertEquals("description match", "desc1", bookmarkRead1.getDescription());
            Assert.assertEquals("url match", "http://www.google.com", bookmarkRead1.getUrl());
            Assert.assertNotNull("Createdon is not null", bookmarkRead1.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", bookmarkRead1.getModifiedAt());

            Assert.assertEquals("BookmarkIds match", bookmarkid2, bookmarkRead2.getBookmarkId());
            Assert.assertEquals("name match", "bookmark2", bookmarkRead2.getName());
            Assert.assertEquals("description match", "desc2", bookmarkRead2.getDescription());
            Assert.assertEquals("url match", "http://www.yahoo.com", bookmarkRead2.getUrl());
            Assert.assertNotNull("Createdon is not null", bookmarkRead2.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", bookmarkRead2.getModifiedAt());

            // Update and verify the updated values
            bookmarkRead2.setName("google_advanced");
            bookmarkRead2.setDescription("desc_advanced");
            bookmarkRead2.setUrl("http://www.inmobi.com");
            bookmarkDao.update(bookmarkRead2, "1");

            bookmarkRead2 = bookmarkDao.get(bookmarkid2, "1");

            Assert.assertEquals("BookmarkIds match", bookmarkid2, bookmarkRead2.getBookmarkId());
            Assert.assertEquals("name match", "google_advanced", bookmarkRead2.getName());
            Assert.assertEquals("description match", "desc_advanced", bookmarkRead2
                    .getDescription());
            Assert.assertEquals("url match", "http://www.inmobi.com", bookmarkRead2.getUrl());
            Assert.assertNotNull("Createdon is not null", bookmarkRead2.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", bookmarkRead2.getModifiedAt());

            // verify the count
            long count = bookmarkDao.count("1");
            Assert.assertEquals("Count is 2", 2, count);

            // verify the paginated api
            List<Bookmark> bookmarks = bookmarkDao.get(10, 0, "1");
            Assert.assertEquals("Count is 2", 2, bookmarks.size());

            Assert.assertEquals("BookmarkIds match", bookmarkid2, bookmarks.get(0).getBookmarkId());
            Assert.assertEquals("name match", "google_advanced", bookmarks.get(0).getName());
            Assert.assertEquals("description match", "desc_advanced", bookmarks.get(0)
                    .getDescription());
            Assert.assertEquals("url match", "http://www.inmobi.com", bookmarks.get(0).getUrl());
            Assert.assertNotNull("Createdon is not null", bookmarks.get(0).getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", bookmarks.get(0).getModifiedAt());

            Assert.assertEquals("BookmarkIds match", bookmarkid1, bookmarks.get(1).getBookmarkId());
            Assert.assertEquals("name match", "bookmark1", bookmarks.get(1).getName());
            Assert.assertEquals("description match", "desc1", bookmarks.get(1).getDescription());
            Assert.assertEquals("url match", "http://www.google.com", bookmarks.get(1).getUrl());
            Assert.assertNotNull("Createdon is not null", bookmarks.get(1).getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", bookmarks.get(1).getModifiedAt());

            // verify delete followed by data
            bookmarkDao.delete(bookmarkid2, "1");
            count = bookmarkDao.count("1");
            Assert.assertNull("Bookmark2 is deleted", bookmarkDao.get(bookmarkid2, "1"));
            Assert.assertNotNull("Bookmark1 is not deleted", bookmarkDao.get(bookmarkid1, "1"));
            Assert.assertEquals("Count now is 1", 1, count);

            bookmarks = bookmarkDao.get(10, 0, "1");
            Assert.assertEquals("Count is 1", 1, bookmarks.size());

            Assert.assertEquals("BookmarkIds match", bookmarkid1, bookmarks.get(0).getBookmarkId());
            Assert.assertEquals("name match", "bookmark1", bookmarks.get(0).getName());
            Assert.assertEquals("description match", "desc1", bookmarks.get(0).getDescription());
            Assert.assertEquals("url match", "http://www.google.com", bookmarks.get(0).getUrl());
            Assert.assertNotNull("Createdon is not null", bookmarks.get(0).getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", bookmarks.get(0).getModifiedAt());

            MongoBootstrap.getMongoDatabase().getCollection(Constants.BOOKMARKS).drop();
        }
    }
}
