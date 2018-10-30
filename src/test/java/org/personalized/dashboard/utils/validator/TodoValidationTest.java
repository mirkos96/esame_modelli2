package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.model.Priority;
import org.personalized.dashboard.model.Task;
import org.personalized.dashboard.model.Todo;
import org.personalized.dashboard.utils.Constants;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sudan on 4/6/15.
 */
@ActiveProfiles("test")
public class TodoValidationTest {

    private ValidationService todoValidationService;

    @Before
    public void initialize() {
        this.todoValidationService = new TodoValidationService(new
                ConstraintValidationService<Todo>());
    }

    @Test
    public void testTodoValidation() {

        Todo todo = new Todo();
        todo.setTodoId("TOD123456789");
        todo.setTitle("");
        todo.setTasks(new ArrayList<Task>());
        List<ErrorEntity> errorEntities = todoValidationService.validate(todo);

        Assert.assertEquals("Error count is 2", 2, errorEntities.size());

        Assert.assertEquals("Error 1 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(0).getName());
        Assert.assertEquals("Error 1 description matches", "title cannot be empty", errorEntities
                .get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "title", errorEntities.get(0).getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(1).getName());
        Assert.assertEquals("Error 2 description matches", "tasks cannot be empty", errorEntities
                .get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "tasks", errorEntities.get(1).getField());

        Task task = new Task();
        task.setTaskId("TAS123456789");
        task.setPriority(Priority.MEDIUM);
        task.setName("name");
        task.setTask("task");
        task.setPercentCompletion(2);
        List<Task> tasks = Lists.newArrayList();
        tasks.add(task);
        todo = new Todo();
        todo.setTodoId("TOD123456789");
        todo.setTitle("title");
        todo.setTasks(tasks);
        errorEntities = todoValidationService.validate(todo);

        Assert.assertEquals("Error count is 0", 0, errorEntities.size());

        Task task1 = new Task();
        task1.setTaskId("TAS123456789");
        task1.setPriority(Priority.MEDIUM);
        task1.setName("");
        task1.setTask("");
        task1.setPercentCompletion(101);
        Task task2 = new Task();
        task2.setTaskId("TAS123456789");
        task2.setPriority(Priority.HIGH);
        task2.setName("name");
        task2.setTask("Desc");
        task2.setPercentCompletion(-1);
        tasks = Lists.newArrayList();
        tasks.add(task1);
        tasks.add(task2);

        todo = new Todo();
        todo.setTodoId("TOD123456789");
        todo.setTitle("ss");
        todo.setTasks(tasks);
        errorEntities = todoValidationService.validate(todo);

        System.out.println(errorEntities);
        Assert.assertEquals("Error count is 4", 4, errorEntities.size());

        Assert.assertEquals("Error 1 name matches", ErrorCodes.INVALID_VALUE.name(),
                errorEntities.get(0).getName());
        Assert.assertEquals("Error 1 description matches", "Invalid value", errorEntities.get(0)
                .getDescription());
        Assert.assertEquals("Error 1 field matches", "percent_completed", errorEntities.get(0)
                .getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(1).getName());
        Assert.assertEquals("Error 2 description matches", "name cannot be empty", errorEntities
                .get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "name", errorEntities.get(1).getField());

        Assert.assertEquals("Error 3 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(2).getName());
        Assert.assertEquals("Error 3 description matches", "task cannot be empty", errorEntities
                .get(2).getDescription());
        Assert.assertEquals("Error 3 field matches", "task", errorEntities.get(2).getField());


        Assert.assertEquals("Error 4 name matches", ErrorCodes.INVALID_VALUE.name(),
                errorEntities.get(3).getName());
        Assert.assertEquals("Error 4 description matches", "Invalid value", errorEntities.get(3)
                .getDescription());
        Assert.assertEquals("Error 4 field matches", "percent_completed", errorEntities.get(3)
                .getField());

        StringBuilder invalidName = new StringBuilder();
        for (int i = 0; i < Constants.TITLE_MAX_LENGTH + 1; i++) {
            invalidName.append("n");
        }

        StringBuilder invalidDescription = new StringBuilder();
        for (int i = 0; i < Constants.CONTENT_MAX_LENGTH + 1; i++) {
            invalidDescription.append("c");
        }

        task = new Task();
        task.setTaskId("TAS123456789");
        task.setPriority(Priority.HIGH);
        task.setName(invalidName.toString());
        task.setTask(invalidDescription.toString());
        task.setPercentCompletion(10);
        tasks = Lists.newArrayList();
        tasks.add(task);

        todo = new Todo();
        todo.setTodoId("TOD123456789");
        todo.setTitle(invalidName.toString());
        todo.setTasks(tasks);
        errorEntities = todoValidationService.validate(todo);

        Assert.assertEquals("Error count is 3", 3, errorEntities.size());

        Assert.assertEquals("Error 1 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(0).getName());
        Assert.assertEquals("Error 1 description matches", "title cannot exceed 50 characters",
                errorEntities.get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "title", errorEntities.get(0).getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(1).getName());
        Assert.assertEquals("Error 2 description matches", "name cannot exceed 50 characters",
                errorEntities.get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "name", errorEntities.get(1).getField());

        Assert.assertEquals("Error 3 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(2).getName());
        Assert.assertEquals("Error 3 description matches", "task cannot exceed 1,000 characters",
                errorEntities.get(2).getDescription());
        Assert.assertEquals("Error 3 field matches", "task", errorEntities.get(2).getField());

        task = new Task();
        task.setTaskId("TAS123456789");
        task.setPriority(Priority.HIGH);
        task.setName("name");
        task.setTask("Desc");
        task.setPercentCompletion(10);
        tasks = Lists.newArrayList();
        for (int i = 0; i < 12; i++)
            tasks.add(task);

        todo = new Todo();
        todo.setTodoId("TOD123456789");
        todo.setTitle("name");
        todo.setTasks(tasks);
        errorEntities = todoValidationService.validate(todo);
        Assert.assertEquals("Error count is 1", 1, errorEntities.size());

        Assert.assertEquals("Error 1 name matches", ErrorCodes.TASKS_LENGTH_EXCEEDED.name(),
                errorEntities.get(0).getName());
        Assert.assertEquals("Error 1 description matches", "Task list exceeded the limit 10",
                errorEntities.get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "tasks", errorEntities.get(0).getField());

    }
}
