package org.personalized.dashboard.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.validator.FieldName;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
@XmlRootElement
public class Bookmark {

    private String bookmarkId;

    @NotEmpty
    @Size(max = Constants.TITLE_MAX_LENGTH)
    @FieldName(name = FieldKeys.BOOKMARK_NAME)
    private String name;

    @NotEmpty
    @Size(max = Constants.CONTENT_MAX_LENGTH)
    @FieldName(name = FieldKeys.BOOKMARK_DESCRIPTION)
    private String description;

    @NotEmpty
    @Size(max = Constants.URL_MAX_LENGTH)
    @FieldName(name = FieldKeys.BOOKMARK_URL)
    private String url;

    private List<String> tags = Lists.newArrayList();

    private Long createdOn;
    private Long modifiedAt;

    public Bookmark() {

    }

    public String getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(String bookmarkId) {
        this.bookmarkId = bookmarkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("bookmarkId", bookmarkId)
                .append("name", name)
                .append("description", description)
                .append("url", url)
                .append("createdOn", createdOn)
                .append("modifiedAt", modifiedAt)
                .append("tags", tags)
                .toString();
    }
}
