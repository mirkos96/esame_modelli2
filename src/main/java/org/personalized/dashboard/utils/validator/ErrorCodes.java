package org.personalized.dashboard.utils.validator;

/**
 * Created by sudan on 27/5/15.
 */
public enum ErrorCodes {

    INVALID_URL("Invalid URL format"),
    INVALID_LIMIT("Invalid batch size"),
    INVALID_OFFSET("Invalid offset"),
    LENGTH_EXCEEDED("{0} cannot exceed {1} characters"),
    EMPTY_FIELD("{0} cannot be empty"),
    TASKS_LENGTH_EXCEEDED("Task list exceeded the limit {0}"),
    EMPTY_TASK_LIST("Task list cannot be empty"),
    INVALID_VALUE("Invalid value"),
    INVALID_SEARCH_CONTEXT("Search context cannot be empty"),
    TAG_LENGTH_EXCEEDED("{0} cannot exceed {1} characters"),
    BULK_SUBMIT_NOT_ALLOWED("Bulk submit not allowed");

    private String description;

    ErrorCodes(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription() {
        this.description = description;
    }

}
