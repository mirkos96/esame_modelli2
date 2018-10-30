package org.personalized.dashboard.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.validator.FieldName;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sudan on 9/4/15.
 */
@XmlRootElement
public class Task {

    private String taskId;
    private Priority priority = Priority.MEDIUM;

    @NotEmpty
    @Size(max = Constants.TITLE_MAX_LENGTH)
    @FieldName(name = FieldKeys.TASK_NAME)
    private String name;

    @NotEmpty
    @Size(max = Constants.CONTENT_MAX_LENGTH)
    @FieldName(name = FieldKeys.TASK_DESCRIPTION)
    private String task;

    @FieldName(name = FieldKeys.TASK_PERCENT_COMPLETION)
    private int percentCompletion = 0;

    public Task() {

    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getPercentCompletion() {
        return percentCompletion;
    }

    public void setPercentCompletion(int percentCompletion) {
        this.percentCompletion = percentCompletion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("taskId", taskId)
                .append("priority", priority)
                .append("name", name)
                .append("task", task)
                .append("percentCompletion", percentCompletion)
                .toString();
    }

}
