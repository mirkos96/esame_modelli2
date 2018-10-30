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
 * Created by sudan on 27/4/15.
 */
@XmlRootElement
public class Page {

    private String pageId;

    @NotEmpty
    @Size(max = Constants.TITLE_MAX_LENGTH)
    @FieldName(name = FieldKeys.PAGE_TITLE)
    private String title;

    @NotEmpty
    @Size(max = Constants.TEXT_MAX_LENGTH)
    @FieldName(name = FieldKeys.PAGE_DESCRIPTION)
    private String content;

    private String summary;

    @FieldName(name = FieldKeys.PAGE_YEAR)
    private int year;

    @FieldName(name = FieldKeys.PAGE_MONTH)
    private int month;

    @FieldName(name = FieldKeys.PAGE_DATE)
    private int date;

    private Long createdOn;
    private Long modifiedAt;

    private List<String> tags = Lists.newArrayList();

    public Page() {

    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override

    public String toString() {
        return new ToStringBuilder(this)
                .append("pageId", pageId)
                .append("title", title)
                .append("summary", summary)
                .append("content", content)
                .append("year", year)
                .append("month", month)

                .append("date", date)
                .append("tags", tags)
                .append("createdOn", createdOn)
                .append("modifiedAt", modifiedAt)
                .toString();
    }
}
