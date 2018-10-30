package org.personalized.dashboard.service.impl;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.dao.api.ActivityDao;
import org.personalized.dashboard.dao.api.BookmarkDao;
import org.personalized.dashboard.model.*;
import org.personalized.dashboard.queue.ESIndexProducer;
import org.personalized.dashboard.service.api.BookmarkService;
import org.personalized.dashboard.utils.generator.ActivityGenerator;
import org.springframework.stereotype.Repository;

import javax.inject.Named;
import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
@Repository
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkDao bookmarkDao;
    private final SessionManager sessionManager;
    private final ActivityGenerator activityGenerator;
    private final ActivityDao activityDao;
    private final ESIndexProducer esIndexProducer;

    @Inject
    public BookmarkServiceImpl(BookmarkDao bookmarkDao, SessionManager sessionManager,
                               ActivityGenerator activityGenerator, ActivityDao activityDao,
                               @Named("bookmark") ESIndexProducer esIndexProducer) {
        this.bookmarkDao = bookmarkDao;
        this.sessionManager = sessionManager;
        this.activityGenerator = activityGenerator;
        this.activityDao = activityDao;
        this.esIndexProducer = esIndexProducer;
    }

    @Override
    public String createBookmark(Bookmark bookmark) {
        String bookmarkId = bookmarkDao.create(bookmark, sessionManager.getUserIdFromSession());
        Activity activity = activityGenerator.generate(ActivityType.CREATED, EntityType.BOOKMARK,
                bookmarkId, bookmark.getName());
        activityDao.add(activity, sessionManager.getUserIdFromSession());
        esIndexProducer.enqueue(bookmark, EntityType.BOOKMARK, OperationType.CREATE, bookmarkId,
                sessionManager.getUserIdFromSession());
        return bookmarkId;
    }

    @Override
    public Bookmark getBookmark(String bookmarkId) {
        return bookmarkDao.get(bookmarkId, sessionManager.getUserIdFromSession());
    }

    @Override
    public Long updateBookmark(Bookmark bookmark) {
        Long modifiedCount = bookmarkDao.update(bookmark, sessionManager.getUserIdFromSession());
        if (modifiedCount > 0) {
            Activity activity = activityGenerator.generate(ActivityType.UPDATED, EntityType
                    .BOOKMARK, bookmark.getBookmarkId(), bookmark.getName());
            activityDao.add(activity, sessionManager.getUserIdFromSession());
            esIndexProducer.enqueue(bookmark, EntityType.BOOKMARK, OperationType.UPDATE, bookmark.getBookmarkId(),
                    sessionManager.getUserIdFromSession());
        }
        return modifiedCount;
    }

    @Override
    public void deleteBookmark(String bookmarkId) {
        Long deletedCount = bookmarkDao.delete(bookmarkId, sessionManager.getUserIdFromSession());
        if (deletedCount > 0) {
            Activity activity = activityGenerator.generate(ActivityType.DELETED, EntityType
                    .BOOKMARK, bookmarkId, StringUtils.EMPTY);
            activityDao.add(activity, sessionManager.getUserIdFromSession());
            esIndexProducer.enqueue(null, EntityType.BOOKMARK, OperationType.DELETE, bookmarkId,
                    sessionManager.getUserIdFromSession());

        }
    }

    @Override
    public Long countBookmarks() {
        return bookmarkDao.count(sessionManager.getUserIdFromSession());
    }

    public List<Bookmark> fetchBookmarks(int limit, int offset) {
        return bookmarkDao.get(limit, offset, sessionManager.getUserIdFromSession());
    }
}
