package org.personalized.dashboard.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.bootstrap.ESBootstrap;
import org.personalized.dashboard.bootstrap.QueueBootstrap;
import org.personalized.dashboard.dao.api.*;
import org.personalized.dashboard.dao.impl.*;
import org.personalized.dashboard.model.*;
import org.personalized.dashboard.queue.*;
import org.personalized.dashboard.service.api.*;
import org.personalized.dashboard.service.impl.*;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.generator.ActivityGenerator;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.personalized.dashboard.utils.htmltidy.DOMParser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Created by sudan on 5/6/15.
 */
@ActiveProfiles("test")
public class ActivityServiceTest {

    private BookmarkService bookmarkService;
    private NoteService noteService;
    private PinService pinService;
    private TodoService todoService;
    private ExpenseService expenseService;
    private DiaryService diaryService;
    private ActivityService activityService;

    @Before
    public void initialize() {

        DOMParser domParser = new DOMParser();
        IdGenerator idGenerator = new IdGenerator();
        BookmarkDao bookmarkDao = new BookmarkDaoImpl(idGenerator);
        NoteDao noteDao = new NoteDaoImpl(idGenerator);
        PinDao pinDao = new PinDaoImpl(idGenerator);
        TodoDao todoDao = new TodoDaoImpl(idGenerator);
        ExpenseDao expenseDao = new ExpenseDaoImpl(idGenerator);
        DiaryDao diaryDao = new DiaryDaoImpl(idGenerator);
        ActivityDao activityDao = new ActivityDaoImpl(idGenerator);
        SessionManager sessionManager = new SessionManager();
        ActivityGenerator activityGenerator = new ActivityGenerator();
        ESIndexProducer bookmarkESIndexProducer = new BookmarkESIndexProducer();
        ESIndexProducer noteESIndexProducer = new NoteESIndexProducer();
        ESIndexProducer pinESIndexProducer = new PinESIndexProducer();
        ESIndexProducer todoESIndexProducer = new TodoESIndexProducer();
        ESIndexProducer expenseESIndexProducer = new ExpenseESIndexProducer();
        ESIndexProducer pageESIndexProducer = new PageESIndexProducer();

        this.bookmarkService = new BookmarkServiceImpl(bookmarkDao, sessionManager,
                activityGenerator, activityDao, bookmarkESIndexProducer);
        this.noteService = new NoteServiceImpl(noteDao, sessionManager, activityGenerator,
                activityDao, domParser, noteESIndexProducer);
        this.pinService = new PinServiceImpl(pinDao, sessionManager, activityGenerator,
                activityDao, pinESIndexProducer);
        this.todoService = new TodoServiceImpl(todoDao, sessionManager, activityGenerator,
                activityDao, todoESIndexProducer);
        this.expenseService = new ExpenseServiceImpl(expenseDao, sessionManager, activityGenerator,
                activityDao, expenseESIndexProducer);
        this.diaryService = new DiaryServiceImpl(diaryDao, sessionManager, activityGenerator,
                activityDao, pageESIndexProducer, new DOMParser());
        this.activityService = new ActivityServiceImpl(activityDao, sessionManager);
    }

    @Test
    public void testActivityService() throws Exception {

        Boolean isDebugMode = Boolean.valueOf(ConfigKeys.MONGO_DEBUG_FLAG);

        /*
            To run these test cases enable isDebugMode in config.properties
         */

        if (isDebugMode) {

            QueueBootstrap.init();
            ESBootstrap.init();

            Bookmark bookmark = new Bookmark();
            bookmark.setName("name");
            bookmark.setDescription("desc");
            bookmark.setUrl("http://www.google.com");
            String bookmarkId = bookmarkService.createBookmark(bookmark);

            bookmark.setDescription("desc1");
            bookmark.setBookmarkId(bookmarkId);
            bookmarkService.updateBookmark(bookmark);

            bookmarkService.deleteBookmark(bookmarkId);

            Note note = new Note();
            note.setNote("note");
            note.setTitle("title");
            String noteId = noteService.createNote(note);

            note.setNoteId(noteId);
            note.setTitle("title1");
            noteService.updateNote(note);
            noteService.deleteNote(noteId);

            Pin pin = new Pin();
            pin.setName("name");
            pin.setDescription("desc");
            pin.setImageUrl("http://www.google.com");
            String pinId = pinService.createPin(pin);

            pin.setPinId(pinId);
            pin.setName("name1");
            pinService.updatePin(pin);
            pinService.deletePin(pinId);

            Task task = new Task();
            task.setName("name");
            task.setTask("task");
            task.setPriority(Priority.MEDIUM);
            task.setPercentCompletion(10);

            Todo todo = new Todo();
            todo.setTitle("title");
            todo.getTasks().add(task);
            String todoId = todoService.createTodo(todo);

            todo.setTodoId(todoId);
            todo.setTitle("sss");
            todoService.updateTodo(todo);
            todoService.deleteTodo(todoId);

            Expense expense = new Expense();
            expense.setTitle("title");
            expense.setDescription("desc");
            expense.setAmount(300);
            expense.setDate(System.currentTimeMillis());
            String expenseId = expenseService.createExpense(expense);

            expense.setExpenseId(expenseId);
            expense.setAmount(400);
            expenseService.updateExpense(expense);
            expenseService.deleteExpense(expenseId);

            Page page = new Page();
            page.setTitle("title");
            page.setContent("content");
            page.setMonth(10);
            page.setDate(22);
            String pageId = diaryService.createPage(page);

            page.setPageId(pageId);
            page.setContent("con");
            diaryService.updatePage(page);
            diaryService.deletePage(pageId);

            List<Activity> activities = activityService.get();

            Assert.assertEquals("Activity type match", ActivityType.DELETED.name(), activities
                    .get(0).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.DIARY.name(), activities.get(0)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", pageId, activities.get(0).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.UPDATED.name(), activities
                    .get(1).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.DIARY.name(), activities.get(1)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", pageId, activities.get(1).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.CREATED.name(), activities
                    .get(2).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.DIARY.name(), activities.get(2)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", pageId, activities.get(2).getEntity().getEntityId());


            Assert.assertEquals("Activity type match", ActivityType.DELETED.name(), activities
                    .get(3).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.EXPENSE.name(), activities.get(3)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", expenseId, activities.get(3).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.UPDATED.name(), activities
                    .get(4).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.EXPENSE.name(), activities.get(4)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", expenseId, activities.get(4).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.CREATED.name(), activities
                    .get(5).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.EXPENSE.name(), activities.get(5)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", expenseId, activities.get(5).getEntity().getEntityId());


            Assert.assertEquals("Activity type match", ActivityType.DELETED.name(), activities
                    .get(6).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.TODO.name(), activities.get(6)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", todoId, activities.get(6).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.UPDATED.name(), activities
                    .get(7).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.TODO.name(), activities.get(7)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", todoId, activities.get(7).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.CREATED.name(), activities
                    .get(8).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.TODO.name(), activities.get(8)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", todoId, activities.get(8).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.DELETED.name(), activities
                    .get(9).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.PIN.name(), activities.get(9)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", pinId, activities.get(9).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.UPDATED.name(), activities
                    .get(10).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.PIN.name(), activities.get(10)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", pinId, activities.get(10).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.CREATED.name(), activities
                    .get(11).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.PIN.name(), activities.get(11)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", pinId, activities.get(11).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.DELETED.name(), activities
                    .get(12).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.NOTE.name(), activities.get(12)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", noteId, activities.get(12).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.UPDATED.name(), activities
                    .get(13).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.NOTE.name(), activities.get(13)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", noteId, activities.get(13).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.CREATED.name(), activities
                    .get(14).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.NOTE.name(), activities.get(14)
                    .getEntity().getEntityType().name());
            Assert.assertEquals("Id match", noteId, activities.get(14).getEntity().getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.DELETED.name(), activities
                    .get(15).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.BOOKMARK.name(), activities.get
                    (15).getEntity().getEntityType().name());
            Assert.assertEquals("Id match", bookmarkId, activities.get(15).getEntity().getEntityId
                    ());

            Assert.assertEquals("Activity type match", ActivityType.UPDATED.name(), activities
                    .get(16).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.BOOKMARK.name(), activities.get
                    (16).getEntity().getEntityType().name());
            Assert.assertEquals("Id match", bookmarkId, activities.get(16).getEntity()
                    .getEntityId());

            Assert.assertEquals("Activity type match", ActivityType.CREATED.name(), activities
                    .get(17).getActivityType().name());
            Assert.assertEquals("Entity type match", EntityType.BOOKMARK.name(), activities.get
                    (17).getEntity().getEntityType().name());
            Assert.assertEquals("Id match", bookmarkId, activities.get(17).getEntity()
                    .getEntityId());
        }
    }
}
