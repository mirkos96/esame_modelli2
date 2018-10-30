package org.personalized.dashboard.dao.impl;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.SessionDao;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by sudan on 26/7/15.
 */
public class SessionDaoImpl implements SessionDao {

    @Override
    public void create(String sessionId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.SESSIONS);

        Document document = new Document()
                .append(FieldKeys.PRIMARY_KEY, sessionId)
                .append(FieldKeys.USER_ID, userId);
        collection.insertOne(document);
    }

    @Override
    public void delete(String sessionId) {

        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.SESSIONS);

        collection.deleteOne(eq(FieldKeys.PRIMARY_KEY, sessionId));
    }

    @Override
    public String getUserId(String sessionId) {

        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.SESSIONS);

        Document document = collection.find(eq(FieldKeys.PRIMARY_KEY, sessionId)).first();

        if (document != null) {
            return document.getString(FieldKeys.USER_ID);
        }
        return null;
    }
}
