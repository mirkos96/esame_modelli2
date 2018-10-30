package org.personalized.dashboard.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.DiaryDao;
import org.personalized.dashboard.model.Page;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.generator.IdGenerator;

import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by sudan on 3/4/15.
 */
public class DiaryDaoImpl implements DiaryDao {

    private IdGenerator idGenerator;

    @Inject
    public DiaryDaoImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public String create(Page page, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.DIARIES);

        String pageId = idGenerator.generateId(Constants.PAGE_PREFIX, Constants.ID_LENGTH, true);
        Document document = new Document()
                .append(FieldKeys.PRIMARY_KEY, pageId)
                .append(FieldKeys.PAGE_TITLE, page.getTitle())
                .append(FieldKeys.PAGE_SUMMARY, page.getSummary())
                .append(FieldKeys.PAGE_DESCRIPTION, page.getContent())
                .append(FieldKeys.PAGE_YEAR, page.getYear())
                .append(FieldKeys.PAGE_MONTH, page.getMonth())
                .append(FieldKeys.PAGE_DATE, page.getDate())
                .append(FieldKeys.USER_ID, userId)
                .append(FieldKeys.CREATED_ON, System.currentTimeMillis())
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());

        collection.insertOne(document);
        return pageId;
    }

    @Override
    public Page get(String pageId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.DIARIES);

        Document document = collection.find(and(
                        eq(FieldKeys.PRIMARY_KEY, pageId),
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                )

        ).first();

        if (document != null) {
            Page page = new Page();
            page.setPageId(pageId);
            page.setTitle(document.getString(FieldKeys.PAGE_TITLE));
            page.setSummary(document.getString(FieldKeys.PAGE_SUMMARY));
            page.setContent(document.getString(FieldKeys.PAGE_DESCRIPTION));
            page.setDate(document.getInteger(FieldKeys.PAGE_DATE));
            page.setMonth(document.getInteger(FieldKeys.PAGE_MONTH));
            page.setYear(document.getInteger(FieldKeys.PAGE_YEAR));
            page.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
            page.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));

            if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                page.setTags(tags);
            }
            return page;
        }
        return null;
    }

    @Override
    public Long update(Page page, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.DIARIES);
        Document document = new Document()
                .append(FieldKeys.PAGE_TITLE, page.getTitle())
                .append(FieldKeys.PAGE_SUMMARY, page.getSummary())
                .append(FieldKeys.PAGE_DESCRIPTION, page.getContent())
                .append(FieldKeys.PAGE_YEAR, page.getYear())
                .append(FieldKeys.PAGE_MONTH, page.getMonth())
                .append(FieldKeys.PAGE_DATE, page.getDate())
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, page.getPageId()),
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                ),
                new Document(Constants.SET_OPERATION, document)
        );
        return updateResult.getModifiedCount();
    }

    @Override
    public Long delete(String pageId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.DIARIES);

        Document document = new Document()
                .append(FieldKeys.IS_DELETED, true);

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, pageId),
                        eq(FieldKeys.USER_ID, userId)
                ),
                new Document(Constants.SET_OPERATION, document)
        );
        return updateResult.getModifiedCount();
    }

    @Override
    public Long count(String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.DIARIES);
        return collection.count(
                and(
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                )
        );
    }

    @Override
    public List<Page> getAll(int limit, int offset, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.DIARIES);

        FindIterable<Document> iterator = collection.find(and
                        (
                                eq(FieldKeys.USER_ID, userId),
                                ne(FieldKeys.IS_DELETED, true)
                        )
        ).skip(offset).limit(limit).sort(
                new Document(FieldKeys.MODIFIED_AT, -1)
        );

        final List<Page> pages = Lists.newArrayList();
        iterator.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {

                Page page = new Page();
                page.setPageId(document.getString(FieldKeys.PRIMARY_KEY));
                page.setTitle(document.getString(FieldKeys.PAGE_TITLE));
                page.setSummary(document.getString(FieldKeys.PAGE_SUMMARY));
                page.setContent(document.getString(FieldKeys.PAGE_DESCRIPTION));
                page.setDate(document.getInteger(FieldKeys.PAGE_DATE));
                page.setYear(document.getInteger(FieldKeys.PAGE_YEAR));
                page.setMonth(document.getInteger(FieldKeys.PAGE_MONTH));
                page.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
                page.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));

                if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                    List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                    page.setTags(tags);
                }
                pages.add(page);
            }
        });

        return pages;
    }
}
