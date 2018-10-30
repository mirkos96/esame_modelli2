package org.personalized.dashboard.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
@XmlRootElement
public class Tag {

    @Valid
    private Entity entity;
    private List<String> tags = Lists.newArrayList();

    public Tag() {

    }

    public Tag(Entity entity, List<String> tags) {
        this.entity = entity;
        this.tags = tags;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("entity", entity)
                .append("tags", tags)
                .toString();
    }
}
