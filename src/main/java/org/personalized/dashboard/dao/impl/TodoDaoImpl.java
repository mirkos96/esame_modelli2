package org.personalized.dashboard.dao.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.TodoDao;
import org.personalized.dashboard.model.Priority;
import org.personalized.dashboard.model.Task;
import org.personalized.dashboard.model.Todo;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.generator.IdGenerator;

import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by sudan on 3/4/15.
 */
public class TodoDaoImpl implements TodoDao {

    private IdGenerator idGenerator;

    @Inject
    public TodoDaoImpl(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public String create(Todo todo, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.TODOS);
        String todoId = idGenerator.generateId(Constants.TODO_PREFIX, Constants.ID_LENGTH, true);

        List<Document> tasks = Lists.newArrayList();
        for (Task task : todo.getTasks()) {
            String taskId = idGenerator.generateId(Constants.TASK_PREFIX, Constants.ID_LENGTH, true);
            Document document = new Document()
                    .append(FieldKeys.PRIMARY_KEY, taskId)
                    .append(FieldKeys.TASK_NAME, task.getName())
                    .append(FieldKeys.TASK_DESCRIPTION, task.getTask())
                    .append(FieldKeys.TASK_PERCENT_COMPLETION, task.getPercentCompletion())
                    .append(FieldKeys.TASK_PRIORITY, task.getPriority().name());
            tasks.add(document);
        }
        Document document = new Document()
                .append(FieldKeys.PRIMARY_KEY, todoId)
                .append(FieldKeys.TODO_TITLE, todo.getTitle())
                .append(FieldKeys.TASKS, tasks)
                .append(FieldKeys.USER_ID, userId)
                .append(FieldKeys.CREATED_ON, System.currentTimeMillis())
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());
        collection.insertOne(document);
        return todoId;
    }

    @Override
    public Todo get(String todoId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.TODOS);
        Document document = collection.find(and
                        (
                                eq(FieldKeys.PRIMARY_KEY, todoId),
                                eq(FieldKeys.USER_ID, userId),
                                ne(FieldKeys.IS_DELETED, true)
                        )
        ).first();

        if (document != null) {
            Todo todo = new Todo();
            todo.setTodoId(document.getString(FieldKeys.PRIMARY_KEY));
            todo.setTitle(document.getString(FieldKeys.TODO_TITLE));
            todo.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
            todo.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));
            List<Document> tasksDocuments = (List<Document>) document.get(FieldKeys.TASKS);
            List<Task> tasks = Lists.newArrayList();
            for (Document taskDocument : tasksDocuments) {
                Task task = new Task();
                task.setTaskId(taskDocument.getString(FieldKeys.PRIMARY_KEY));
                task.setName(taskDocument.getString(FieldKeys.TASK_NAME));
                task.setTask(taskDocument.getString(FieldKeys.TASK_DESCRIPTION));
                task.setPriority(Priority.valueOf(taskDocument.getString(FieldKeys.TASK_PRIORITY)));
                task.setPercentCompletion(taskDocument.getInteger(FieldKeys
                        .TASK_PERCENT_COMPLETION));
                tasks.add(task);
            }
            todo.setTasks(tasks);
            if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                todo.setTags(tags);
            }
            return todo;

        }
        return null;


    }

    @Override
    public Long update(Todo todo, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.TODOS);

        List<Document> tasks = Lists.newArrayList();
        for (Task task : todo.getTasks()) {
            String taskId = idGenerator.generateId(Constants.TASK_PREFIX, Constants.ID_LENGTH, true);
            Document document = new Document()
                    .append(FieldKeys.PRIMARY_KEY, taskId)
                    .append(FieldKeys.TASK_NAME, task.getName())
                    .append(FieldKeys.TASK_DESCRIPTION, task.getTask())
                    .append(FieldKeys.TASK_PERCENT_COMPLETION, task.getPercentCompletion())
                    .append(FieldKeys.TASK_PRIORITY, task.getPriority().name());
            tasks.add(document);
        }
        Document document = new Document()
                .append(FieldKeys.TODO_TITLE, todo.getTitle())
                .append(FieldKeys.TASKS, tasks)
                .append(FieldKeys.MODIFIED_AT, System.currentTimeMillis());

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, todo.getTodoId()),
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                ),
                new Document(Constants.SET_OPERATION, document)
        );
        return updateResult.getModifiedCount();
    }

    @Override
    public Long delete(String todoId, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.TODOS);

        Document document = new Document()
                .append(FieldKeys.IS_DELETED, true);

        UpdateResult updateResult = collection.updateOne(
                and(
                        eq(FieldKeys.PRIMARY_KEY, todoId),
                        eq(FieldKeys.USER_ID, userId)
                ),
                new Document(Constants.SET_OPERATION, document)
        );
        return updateResult.getModifiedCount();
    }

    @Override
    public Long count(String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.TODOS);

        return collection.count(
                and(
                        eq(FieldKeys.USER_ID, userId),
                        ne(FieldKeys.IS_DELETED, true)
                )
        );

    }

    @Override
    public List<Todo> get(int limit, int offset, String userId) {
        MongoCollection<Document> collection = MongoBootstrap.getMongoDatabase().getCollection
                (Constants.TODOS);

        FindIterable<Document> iterator = collection.find(and
                        (
                                eq(FieldKeys.USER_ID, userId),
                                ne(FieldKeys.IS_DELETED, true)
                        )
        ).skip(offset).limit(limit).sort(
                new Document(FieldKeys.MODIFIED_AT, -1)
        );

        final List<Todo> todos = Lists.newArrayList();
        iterator.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Todo todo = new Todo();
                todo.setTodoId(document.getString(FieldKeys.PRIMARY_KEY));
                todo.setTitle(document.getString(FieldKeys.TODO_TITLE));
                todo.setCreatedOn(document.getLong(FieldKeys.CREATED_ON));
                todo.setModifiedAt(document.getLong(FieldKeys.MODIFIED_AT));
                List<Document> tasksDocuments = (List<Document>) document.get(FieldKeys.TASKS);
                List<Task> tasks = Lists.newArrayList();
                for (Document taskDocument : tasksDocuments) {
                    Task task = new Task();
                    task.setTaskId(taskDocument.getString(FieldKeys.PRIMARY_KEY));
                    task.setName(taskDocument.getString(FieldKeys.TASK_NAME));
                    task.setTask(taskDocument.getString(FieldKeys.TASK_DESCRIPTION));
                    task.setPriority(Priority.valueOf(taskDocument.getString(FieldKeys
                            .TASK_PRIORITY)));
                    task.setPercentCompletion(taskDocument.getInteger(FieldKeys
                            .TASK_PERCENT_COMPLETION));
                    tasks.add(task);
                }
                todo.setTasks(tasks);
                if (document.containsKey(FieldKeys.ENTITY_TAGS)) {
                    List<String> tags = (List<String>) document.get(FieldKeys.ENTITY_TAGS);
                    todo.setTags(tags);
                }
                todos.add(todo);
            }
        });
        return todos;

    }
}
