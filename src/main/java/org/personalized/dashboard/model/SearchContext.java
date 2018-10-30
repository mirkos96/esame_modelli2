package org.personalized.dashboard.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by sudan on 6/7/15.
 */
@XmlRootElement
public class SearchContext {

    private List<EntityType> entityTypes;
    private List<String> titles;
    private List<String> tags;
    private List<String> keywords;

    public SearchContext() {

    }

    public SearchContext(List<EntityType> entityTypes, List<String> titles, List<String> tags, List<String> keywords) {
        this.entityTypes = entityTypes;
        this.titles = titles;
        this.tags = tags;
        this.keywords = keywords;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("entityTypes", entityTypes)
                .append("titles", titles)
                .append("tags", tags)
                .append("keywords", keywords)
                .toString();
    }
}
