package org.personalized.dashboard.dao.impl;

import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.UserDao;
import org.personalized.dashboard.model.User;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.generator.IdGenerator;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Created by sudan on 26/7/15.
 */
public class UserDaoImpl implements UserDao {

    private final IdGenerator idGenerator;

    @Inject
    public UserDaoImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public String upsert(User user) {

        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.USERS);

        Document document = collection.find(eq(FieldKeys.EMAIL, user.getEmail())).first();
        if (document == null) {
            user.setUserId(idGenerator.generateId(Constants.USER_PREFIX, Constants.ID_LENGTH, true));

            collection.insertOne(
                    new Document()
                            .append(FieldKeys.PRIMARY_KEY, user.getUserId())
                            .append(FieldKeys.USERNAME, user.getUsername())
                            .append(FieldKeys.EMAIL, user.getEmail())
                            .append(FieldKeys.PROFILE_PIC, user.getProfilePicURL())
                            .append(FieldKeys.CREATED_ON, System.currentTimeMillis())
                            .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis())
            );
            return user.getUserId();
        } else {
            Document updatedProfileDocument = new Document()
                    .append(FieldKeys.USERNAME, user.getUsername())
                    .append(FieldKeys.PROFILE_PIC, user.getProfilePicURL())
                    .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());

            collection.updateOne(and(
                            eq(FieldKeys.EMAIL, user.getEmail()),
                            eq(FieldKeys.PRIMARY_KEY, document.getString(FieldKeys.PRIMARY_KEY))
                    ),
                    new Document(Constants.SET_OPERATION, updatedProfileDocument)
            );
            return document.getString(FieldKeys.PRIMARY_KEY);
        }
    }

    @Override
    public User get(String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection(Constants.USERS);

        Document document = collection.find(eq(FieldKeys.PRIMARY_KEY, userId)).first();

        if (document != null) {
            User user = new User();
            user.setUserId(document.getString(FieldKeys.PRIMARY_KEY));
            user.setUsername(document.getString(FieldKeys.USERNAME));
            user.setEmail(document.getString(FieldKeys.EMAIL));
            user.setProfilePicURL(document.getString(FieldKeys.PROFILE_PIC));

            if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                user.setTags(tags);
            }

            if (document.containsKey(FieldKeys.EXPENSE_CATEGORIES)) {
                List<String> categories = (List<String>) document.get(FieldKeys.EXPENSE_CATEGORIES);
                user.setExpenseCategories(categories);
            }
            return user;
        }
        return null;
    }
}
