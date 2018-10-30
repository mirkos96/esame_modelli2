package org.personalized.dashboard.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.model.BatchSize;
import org.personalized.dashboard.model.Todo;
import org.personalized.dashboard.service.api.TodoService;
import org.personalized.dashboard.utils.validator.ErrorEntity;
import org.personalized.dashboard.utils.validator.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */

@Controller
@Scope("request")
@Path("/todos")
public class TodoController {

    private final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    private final TodoService todoService;
    private final ValidationService batchSizeValidationService;
    private final ValidationService todoValidationService;

    @Inject
    public TodoController(TodoService todoService,
                          @Named("batchSize") ValidationService batchSizeValidationService,
                          @Named("todo") ValidationService todoValidationService) {
        this.todoService = todoService;
        this.batchSizeValidationService = batchSizeValidationService;
        this.todoValidationService = todoValidationService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTodo(@Context HttpHeaders httpHeaders, Todo todo) {

        try {
            List<ErrorEntity> errorEntities = todoValidationService.validate(todo);
            if (CollectionUtils.isEmpty(errorEntities)) {
                String todoId = todoService.createTodo(todo);
                return Response.status(Response.Status.CREATED).entity(todoId).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("TodoController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("{todoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTodo(@Context HttpHeaders httpHeaders, @PathParam("todoId") String todoId) {

        try {
            if (StringUtils.isEmpty(todoId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Todo todo = todoService.getTodo(todoId);
                if (todo == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                } else {
                    return Response.status(Response.Status.OK).entity(todo).build();
                }
            }
        } catch (Exception e) {
            LOGGER.error("TodoController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{todoId}")
    public Response updateTodo(@Context HttpHeaders httpHeaders,
                               @PathParam("todoId") String todoId, 
                               Todo todo) {

        try {
            List<ErrorEntity> errorEntities = todoValidationService.validate(todo);
            if (CollectionUtils.isEmpty(errorEntities)) {
                Long modifiedCount = todoService.updateTodo(todo);
                if (modifiedCount > 0) {
                    return Response.status(Response.Status.OK).entity(todo).build();
                } else {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("TodoController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("{todoId}")
    public Response deleteTodo(@Context HttpHeaders httpHeaders, @PathParam("todoId") String todoId) {

        try {
            if (StringUtils.isEmpty(todoId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                todoService.deleteTodo(todoId);
                return Response.status(Response.Status.OK).build();
            }
        } catch (Exception e) {
            LOGGER.error("TodoController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countTodos(@Context HttpHeaders httpHeaders) {

        try {
            Long count = todoService.countTodos();
            return Response.status(Response.Status.OK).entity(String.valueOf(count)).build();
        } catch (Exception e) {
            LOGGER.error("TodoController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchTodos(@Context HttpHeaders httpHeaders,
                               @QueryParam("limit") int limit, @QueryParam("offset") int offset) {

        try {
            BatchSize batchSize = new BatchSize(limit, offset);
            List<ErrorEntity> errorEntities = batchSizeValidationService.validate(batchSize);
            if (CollectionUtils.isEmpty(errorEntities)) {
                List<Todo> todos = todoService.fetchTodos(batchSize.getLimit(), batchSize.getOffset());
                GenericEntity<List<Todo>> todoListObj = new GenericEntity<List<Todo>>(todos) {
                };
                return Response.status(Response.Status.OK).entity(todoListObj).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("TodoController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
