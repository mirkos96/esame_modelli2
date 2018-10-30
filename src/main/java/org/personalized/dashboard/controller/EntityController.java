package org.personalized.dashboard.controller;

import com.google.inject.Inject;
import org.personalized.dashboard.model.EntityCount;
import org.personalized.dashboard.model.ExpenseFilter;
import org.personalized.dashboard.service.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

/**
 * Created by sudan on 2/8/15.
 */
@Controller
@Scope("request")
@Path("/entities")
public class EntityController {

    private final Logger LOGGER = LoggerFactory.getLogger(EntityController.class);

    private final BookmarkService bookmarkService;
    private final NoteService noteService;
    private final PinService pinService;
    private final TodoService todoService;
    private final DiaryService diaryService;
    private final ExpenseService expenseService;

    @Inject
    public EntityController(BookmarkService bookmarkService, NoteService noteService,
                            PinService pinService, TodoService todoService,
                            DiaryService diaryService, ExpenseService expenseService) {
        this.bookmarkService = bookmarkService;
        this.noteService = noteService;
        this.pinService = pinService;
        this.todoService = todoService;
        this.diaryService = diaryService;
        this.expenseService = expenseService;
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntitiesCount(@Context HttpHeaders httpHeaders) {

        try {

            Long bookmarksCount = bookmarkService.countBookmarks();
            Long notesCount = noteService.countNotes();
            Long pinsCount = pinService.countPins();
            Long todosCount = todoService.countTodos();
            Long diariesCount = diaryService.countPages();
            Long expensesCount = expenseService.countExpense(new ExpenseFilter());

            EntityCount entityCount = new EntityCount();
            entityCount.setBookmarks(bookmarksCount);
            entityCount.setNotes(notesCount);
            entityCount.setPins(pinsCount);
            entityCount.setTodos(todosCount);
            entityCount.setDiaries(diariesCount);
            entityCount.setExpenses(expensesCount);
            GenericEntity<EntityCount> countMapObj = new GenericEntity<EntityCount>
                    (entityCount) {
            };
            return Response.status(Response.Status.OK).entity(countMapObj).build();

        } catch (Exception e) {
            LOGGER.error("EntityController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
