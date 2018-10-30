package org.personalized.dashboard.model;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.validator.FieldName;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
@XmlRootElement
public class Todo {

    private String todoId;

    @NotEmpty
    @Size(max = Constants.TITLE_MAX_LENGTH)
    @FieldName(name = FieldKeys.TODO_TITLE)
    private String title;

    @Valid
    @NotEmpty
    @FieldName(name = FieldKeys.TASK_LIST)
    private List<Task> tasks = Lists.newArrayList();

    private List<String> tags = Lists.newArrayList();

    private Long createdOn;
    private Long modifiedAt;

    public Todo() {

    }

    public String getTodoId() {
        return todoId;
    }

    public void setTodoId(String todoId) {
        this.todoId = todoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
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
                .append("todoId", todoId)
                .append("title", title)
                .append("tasks", tasks)
                .append("tags", tags)
                .append("createdOn", createdOn)
                .append("modifiedAt", modifiedAt)
                .toString();
    }
}
