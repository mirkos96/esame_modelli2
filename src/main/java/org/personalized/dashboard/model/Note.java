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
public class Note {

    private String noteId;

    @NotEmpty
    @Size(max = Constants.TITLE_MAX_LENGTH)
    @FieldName(name = FieldKeys.NOTE_TITLE)
    private String title;

    @NotEmpty
    @Size(max = Constants.CONTENT_MAX_LENGTH)
    @FieldName(name = FieldKeys.NOTE_DESCRIPTION)
    private String note;

    private String summary;

    private List<String> tags = Lists.newArrayList();

    private Long createdOn;
    private Long modifiedAt;

    public Note() {

    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
                .append("noteId", noteId)
                .append("title", title)
                .append("summary", summary)
                .append("note", note)
                .append("createdOn", createdOn)
                .append("modifiedAt", modifiedAt)
                .append("tags", tags)
                .toString();
    }
}
