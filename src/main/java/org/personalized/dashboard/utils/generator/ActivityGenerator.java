package org.personalized.dashboard.utils.generator;

import org.personalized.dashboard.model.Activity;
import org.personalized.dashboard.model.ActivityType;
import org.personalized.dashboard.model.Entity;
import org.personalized.dashboard.model.EntityType;

/**
 * Created by sudan on 30/5/15.
 */
public class ActivityGenerator<T> {

    public Activity generate(ActivityType activityType, EntityType entityType, String entityId,
                             String entityName) {

        StringBuilder description = new StringBuilder();
        if (activityType == ActivityType.TAG_ADDED) {
            description.append("tags has been updated for ")
                    .append(entityType.name());
        } else {
            description.append(entityName)
                    .append(" has been ").append(activityType.name());
        }
        Activity activity = new Activity();
        activity.setDescription(description.toString());
        activity.setActivityType(activityType);
        activity.setEntity(new Entity(entityType, entityId));
        return activity;
    }
}
