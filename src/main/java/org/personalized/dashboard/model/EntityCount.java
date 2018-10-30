package org.personalized.dashboard.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by sudan on 2/8/15.
 */
@XmlRootElement
public class EntityCount {

    private Long bookmarks;
    private Long notes;
    private Long pins;
    private Long todos;
    private Long diaries;
    private Long expenses;

    public EntityCount() {

    }

    public Long getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(Long bookmarks) {
        this.bookmarks = bookmarks;
    }


    public Long getNotes() {
        return notes;
    }

    public void setNotes(Long notes) {
        this.notes = notes;
    }

    public Long getPins() {
        return pins;
    }

    public void setPins(Long pins) {
        this.pins = pins;
    }

    public Long getTodos() {
        return todos;
    }

    public void setTodos(Long todos) {
        this.todos = todos;
    }

    public Long getDiaries() {
        return diaries;
    }

    public void setDiaries(Long diaries) {
        this.diaries = diaries;
    }

    public Long getExpenses() {
        return expenses;
    }

    public void setExpenses(Long expenses) {
        this.expenses = expenses;
    }

    @Override

    public String toString() {
        return new ToStringBuilder(this)
                .append("bookmarks", bookmarks)
                .append("notes", notes)
                .append("pins", pins)
                .append("todos", todos)
                .append("diaries", diaries)
                .append("expenses", expenses)
                .toString();
    }
}
