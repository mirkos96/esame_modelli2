package org.personalized.dashboard.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.BookmarkDao;
import org.personalized.dashboard.model.Bookmark;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.generator.IdGenerator;

import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by sudan on 3/4/15.
 */
public class BookmarkDaoImpl implements BookmarkDao {

    private IdGenerator idGenerator;

    @Inject
    public BookmarkDaoImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public String create(Bookmark bookmark, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.BOOKMARKS);

        String bookmarkId = idGenerator.generateId(Constants.BOOKMARK_PREFIX, Constants.ID_LENGTH, true);
        Document document = new Document()
                .append(FieldKeys.PRIMARY_KEY, bookmarkId)
                .append(FieldKeys.BOOKMARK_NAME, bookmark.getName())
                .append(FieldKeys.BOOKMARK_DESCRIPTION, bookmark.getDescription())
                .append(FieldKeys.BOOKMARK_URL, bookmark.getUrl())
                .append(FieldKeys.USER_ID, userId)
                .append(FieldKeys.CREATED_ON, System.currentTimeMillis())
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());
        collection.insertOne(document);

        return bookmarkId;
    }

    @Override
    public Bookmark get(String bookmarkId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.BOOKMARKS);
        Document document = collection.find(and
                        (
                                eq(FieldKeys.PRIMARY_KEY, bookmarkId),
                                eq(FieldKeys.USER_ID, userId),
                                ne(FieldKeys.IS_DELETED, true)
                        )
        ).first();
        if (document != null) {
            Bookmark bookmark = new Bookmark();
            bookmark.setBookmarkId(document.getString(FieldKeys.PRIMARY_KEY));
            bookmark.setName(document.getString(FieldKeys.BOOKMARK_NAME));
            bookmark.setDescription(document.getString(FieldKeys.BOOKMARK_DESCRIPTION));
            bookmark.setUrl(document.getString(FieldKeys.BOOKMARK_URL));
            bookmark.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
            bookmark.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));

            if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                bookmark.setTags(tags);
            }
            return bookmark;
        }
        return null;
    }

    @Override
    public Long update(Bookmark bookmark, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.BOOKMARKS);
        Document document = new Document()
                .append(FieldKeys.BOOKMARK_NAME, bookmark.getName())
                .append(FieldKeys.BOOKMARK_DESCRIPTION, bookmark.getDescription())
                .append(FieldKeys.BOOKMARK_URL, bookmark.getUrl())
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, bookmark.getBookmarkId()),
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                ),
                new Document(Constants.SET_OPERATION, document)
        );
        return updateResult.getModifiedCount();
    }

    @Override
    public Long delete(String bookmarkId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.BOOKMARKS);

        Document document = new Document()
                .append(FieldKeys.IS_DELETED, true);

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, bookmarkId),
                        eq(FieldKeys.USER_ID, userId)
                ),
                new Document(Constants.SET_OPERATION, document)
        );
        return updateResult.getModifiedCount();
    }

    @Override
    public Long count(String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.BOOKMARKS);

        return collection.count(
                and(
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                )
        );
    }

    @Override
    public List<Bookmark> get(int limit, int offset, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.BOOKMARKS);

        FindIterable<Document> iterator = collection.find(and
                        (
                                eq(FieldKeys.USER_ID, userId),
                                ne(FieldKeys.IS_DELETED, true)
                        )
        ).skip(offset).limit(limit).sort(
                new Document(FieldKeys.MODIFIED_AT, -1)
        );

        final List<Bookmark> bookmarks = Lists.newArrayList();
        iterator.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Bookmark bookmark = new Bookmark();
                bookmark.setBookmarkId(document.getString(FieldKeys.PRIMARY_KEY));
                bookmark.setName(document.getString(FieldKeys.BOOKMARK_NAME));
                bookmark.setDescription(document.getString(FieldKeys.BOOKMARK_DESCRIPTION));
                bookmark.setUrl(document.getString(FieldKeys.BOOKMARK_URL));
                bookmark.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
                bookmark.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));

                if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                    List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                    bookmark.setTags(tags);
                }
                bookmarks.add(bookmark);
            }
        });
        return bookmarks;
    }
}
