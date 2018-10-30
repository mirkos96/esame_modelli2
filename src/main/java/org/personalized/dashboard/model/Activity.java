package org.personalized.dashboard.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sudan on 30/5/15.
 */
@XmlRootElement
public class Activity {

    private String activityId;
    private ActivityType activityType;
    private Entity entity;
    private String description;
    private Long createdOn;

    public Activity() {

    }

    public Activity(String activityId, ActivityType activityType, Entity entity, String
            description) {
        this.activityId = activityId;
        this.activityType = activityType;
        this.entity = entity;
        this.description = description;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("activityId", activityId)
                .append("activityType", activityType)
                .append("description", description)
                .append("entity", entity)
                .append("createdOn", createdOn)
                .toString();
    }
}