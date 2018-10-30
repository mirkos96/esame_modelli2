package org.personalized.dashboard.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.validator.FieldName;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sudan on 6/4/15.
 */
@XmlRootElement
public class Entity {

    @NotEmpty
    @FieldName(name = FieldKeys.ENTITY_TYPE)
    private EntityType entityType;

    @NotEmpty
    @FieldName(name = FieldKeys.ENTITY_ID)
    private String entityId;

    @NotEmpty
    @FieldName(name = FieldKeys.ENTITY_NAME)
    private String title;

    public Entity() {

    }

    public Entity(EntityType entityType, String entityId) {
        this.entityType = entityType;
        this.entityId = entityId;

    }

    public Entity(EntityType entityType, String entityId, String title) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.title = title;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("entityType", entityType)
                .append("entityId", entityId)
                .append("title", title)
                .toString();
    }

}
