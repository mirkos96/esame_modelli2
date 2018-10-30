package org.personalized.dashboard.dao;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.bootstrap.ConfigManager;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.TodoDao;
import org.personalized.dashboard.dao.impl.TodoDaoImpl;
import org.personalized.dashboard.model.Priority;
import org.personalized.dashboard.model.Task;
import org.personalized.dashboard.model.Todo;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

/**
 * Created by sudan on 5/6/15.
 */
@ActiveProfiles("test")
public class TodoDaoTest {

    private TodoDao todoDao;

    @Before
    public void initialize() throws IOException {
        ConfigManager.init();
        this.todoDao = new TodoDaoImpl(new IdGenerator());
    }

    @Test
    public void testTodoDao() {
        Boolean isDebugMode = Boolean.valueOf(ConfigKeys.MONGO_DEBUG_FLAG);

        /*
            To run these test cases enable isDebugMode in config.properties
         */
        if (isDebugMode) {
            MongoBootstrap.init();
            MongoBootstrap.getMongoDatabase().getCollection(Constants.TODOS).drop();

            Task task1 = new Task();
            task1.setName("name1");
            task1.setTask("task1");
            task1.setPriority(Priority.HIGH);
            task1.setPercentCompletion(10);

            Task task2 = new Task();
            task2.setName("name2");
            task2.setTask("task2");
            task2.setPriority(Priority.MEDIUM);
            task2.setPercentCompletion(20);

            List<Task> tasks1 = Lists.newArrayList();
            tasks1.add(task1);
            tasks1.add(task2);

            List<Task> tasks2 = Lists.newArrayList();
            tasks2.add(task1);

            Todo todo1 = new Todo();
            todo1.setTitle("todo1");
            todo1.setTasks(tasks1);

            Todo todo2 = new Todo();
            todo2.setTitle("todo2");
            todo2.setTasks(tasks2);

            String todoId1 = todoDao.create(todo1, "1");
            String todoId2 = todoDao.create(todo2, "1");

            Todo todoRead1 = todoDao.get(todoId1, "1");
            Todo todoRead2 = todoDao.get(todoId2, "1");

            Assert.assertEquals("Count is 2", new Long(2), todoDao.count("1"));

            Assert.assertEquals("todo1 name is todo1", "todo1", todoRead1.getTitle());
            Assert.assertEquals("todo1 task1 name is name1", "name1", todoRead1.getTasks().get(0)
                    .getName());
            Assert.assertEquals("todo1 task2 name is name2", "name2", todoRead1.getTasks().get(1)
                    .getName());

            Assert.assertEquals("todo1 task1 task is task1", "task1", todoRead1.getTasks().get(0)
                    .getTask());
            Assert.assertEquals("todo1 task2 task is task2", "task2", todoRead1.getTasks().get(1)
                    .getTask());

            Assert.assertEquals("todo1 task1 priority is HIGH", "HIGH", todoRead1.getTasks().get
                    (0).getPriority().name());
            Assert.assertEquals("todo1 task2 priority is MEDIUM", "MEDIUM", todoRead1.getTasks()
                    .get(1).getPriority().name());

            Assert.assertEquals("todo1 task1 percent is 10", 10, todoRead1.getTasks().get(0)
                    .getPercentCompletion());
            Assert.assertEquals("todo1 task2 percent is 20", 20, todoRead1.getTasks().get(1)
                    .getPercentCompletion());

            Assert.assertEquals("todo2 name is todo2", "todo2", todoRead2.getTitle());
            Assert.assertEquals("todo2 task1 name is name1", "name1", todoRead2.getTasks().get(0)
                    .getName());
            Assert.assertEquals("todo2 task1 task is task1", "task1", todoRead2.getTasks().get(0)
                    .getTask());
            Assert.assertEquals("todo2 task1 priority is HIGH", "HIGH", todoRead2.getTasks().get
                    (0).getPriority().name());
            Assert.assertEquals("todo2 task1 percent is 10", 10, todoRead2.getTasks().get(0)
                    .getPercentCompletion());

            todoRead2.setTitle("newtitle");
            todoRead2.setTasks(tasks2);
            todoDao.update(todoRead2, "1");

            Todo todoUpdate1 = todoDao.get(todoId2, "1");

            Assert.assertEquals("todo2 name is todo1", "newtitle", todoUpdate1.getTitle());
            Assert.assertEquals("todo2 task1 name is name1", "name1", todoUpdate1.getTasks().get
                    (0).getName());
            Assert.assertEquals("todo2 task1 task is task1", "task1", todoUpdate1.getTasks().get
                    (0).getTask());
            Assert.assertEquals("todo2 task1 priority is HIGH", "HIGH", todoUpdate1.getTasks()
                    .get(0).getPriority().name());
            Assert.assertEquals("todo2 task1 percent is 10", 10, todoUpdate1.getTasks().get(0)
                    .getPercentCompletion());

            List<Todo> todos = todoDao.get(2, 0, "1");

            Assert.assertEquals("todo1 name is todo1", "newtitle", todos.get(0).getTitle());
            Assert.assertEquals("todo1 task1 name is name1", "name1", todos.get(0).getTasks().get
                    (0).getName());
            Assert.assertEquals("todo1 task1 task is task1", "task1", todos.get(0).getTasks().get
                    (0).getTask());
            Assert.assertEquals("todo1 task1 priority is HIGH", "HIGH", todos.get(0).getTasks()
                    .get(0).getPriority().name());
            Assert.assertEquals("todo1 task1 percent is 10", 10, todos.get(0).getTasks().get(0)
                    .getPercentCompletion());

            Assert.assertEquals("todo2 name is todo1", "todo1", todos.get(1).getTitle());
            Assert.assertEquals("todo2 task1 name is name1", "name1", todos.get(1).getTasks().get
                    (0).getName());
            Assert.assertEquals("todo2 task2 name is name2", "name2", todos.get(1).getTasks().get
                    (1).getName());
            Assert.assertEquals("todo2 task1 task is task1", "task1", todos.get(1).getTasks().get
                    (0).getTask());
            Assert.assertEquals("todo2 task2 task is task2", "task2", todos.get(1).getTasks().get
                    (1).getTask());
            Assert.assertEquals("todo2 task1 priority is HIGH", "HIGH", todos.get(1).getTasks()
                    .get(0).getPriority().name());
            Assert.assertEquals("todo2 task2 priority is MEDIUM", "MEDIUM", todos.get(1).getTasks
                    ().get(1).getPriority().name());
            Assert.assertEquals("todo2 task1 percent is 10", 10, todos.get(1).getTasks().get(0)
                    .getPercentCompletion());
            Assert.assertEquals("todo2 task2 percent is 20", 20, todos.get(1).getTasks().get(1)
                    .getPercentCompletion());

            todoDao.delete(todoId1, "1");
            Todo todo = todoDao.get(todoId1, "1");
            Assert.assertNull("todo is deleted", todo);
            Assert.assertNotNull("todo is not deleted", todoDao.get(todoId2, "1"));
            Assert.assertEquals("Count is 1", new Long(1), todoDao.count("1"));
        }
    }
}