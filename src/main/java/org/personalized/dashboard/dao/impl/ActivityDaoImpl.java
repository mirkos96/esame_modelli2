package org.personalized.dashboard.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.ActivityDao;
import org.personalized.dashboard.model.Activity;
import org.personalized.dashboard.model.ActivityType;
import org.personalized.dashboard.model.Entity;
import org.personalized.dashboard.model.EntityType;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.generator.IdGenerator;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Created by sudan on 30/5/15.
 */
public class ActivityDaoImpl implements ActivityDao {

    private IdGenerator idGenerator;

    @Inject
    public ActivityDaoImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public void add(Activity activity, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.ACTIVITIES);

        String activityId = idGenerator.generateId(Constants.ACTIVITIES_PREFIX, Constants
                .ID_LENGTH, true);
        Document document = new Document()
                .append(FieldKeys.PRIMARY_KEY, activityId)
                .append(FieldKeys.ACTIVITY_TYPE, activity.getActivityType().name())
                .append(FieldKeys.ENTITY_ID, activity.getEntity().getEntityId())
                .append(FieldKeys.ENTITY_TYPE, activity.getEntity().getEntityType().name())
                .append(FieldKeys.ACTIVITY_DESCRIPTION, activity.getDescription())
                .append(FieldKeys.USER_ID, userId)
                .append(FieldKeys.CREATED_ON, System.currentTimeMillis());
        collection.insertOne(document);

    }

    @Override
    public List<Activity> get(int limit, int offset, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.ACTIVITIES);

        FindIterable<Document> iterator = collection.find(and
                        (
                                eq(FieldKeys.USER_ID, userId)
                        )
        ).skip(offset).limit(limit).sort(
                new Document(FieldKeys.CREATED_ON, -1)
        );

        final List<Activity> activities = Lists.newArrayList();
        iterator.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Activity activity = new Activity();
                Entity entity = new Entity();
                entity.setEntityId(document.getString(FieldKeys.ENTITY_ID));
                entity.setEntityType(EntityType.valueOf(document.getString(FieldKeys.ENTITY_TYPE)));
                activity.setEntity(entity);
                activity.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
                activity.setDescription(document.getString(FieldKeys.ACTIVITY_DESCRIPTION));
                activity.setActivityType(ActivityType.valueOf(document.getString(FieldKeys
                        .ACTIVITY_TYPE)));
                activities.add(activity);
            }
        });
        return activities;

    }
}
