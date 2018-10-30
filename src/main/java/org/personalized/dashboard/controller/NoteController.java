package org.personalized.dashboard.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.model.BatchSize;
import org.personalized.dashboard.model.Note;
import org.personalized.dashboard.service.api.NoteService;
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
@Path("/notes")
public class NoteController {

    private final Logger LOGGER = LoggerFactory.getLogger(NoteController.class);

    private final NoteService noteService;
    private final ValidationService batchSizeValidationService;
    private final ValidationService noteValidationService;

    @Inject
    public NoteController(NoteService noteService,
                          @Named("note") ValidationService noteValidationService,
                          @Named("batchSize") ValidationService batchSizeValidationService) {
        this.noteService = noteService;
        this.noteValidationService = noteValidationService;
        this.batchSizeValidationService = batchSizeValidationService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNote(@Context HttpHeaders httpHeaders, Note note) {

        try {
            List<ErrorEntity> errorEntities = noteValidationService.validate(note);
            if (CollectionUtils.isEmpty(errorEntities)) {
                String noteId = noteService.createNote(note);
                return Response.status(Response.Status.CREATED).entity(noteId).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("NoteController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("{noteId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNote(@Context HttpHeaders httpHeaders, @PathParam("noteId") String noteId) {

        try {
            if (StringUtils.isEmpty(noteId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Note note = noteService.getNote(noteId);
                if (note == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                } else {
                    return Response.status(Response.Status.OK).entity(note).build();
                }
            }
        } catch (Exception e) {
            LOGGER.error("NoteController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{noteId}")
    public Response updateNote(@Context HttpHeaders httpHeaders,
                               @PathParam("noteId") String noteId,
                               Note note) {

        try {
            List<ErrorEntity> errorEntities = noteValidationService.validate(note);
            if (CollectionUtils.isEmpty(errorEntities)) {
                Long modifiedCount = noteService.updateNote(note);
                if (modifiedCount > 0) {
                    return Response.status(Response.Status.OK).entity(note).build();
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
            LOGGER.error("NoteController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("{noteId}")
    public Response deleteNote(@Context HttpHeaders httpHeaders, @PathParam("noteId") String noteId) {

        try {
            if (StringUtils.isEmpty(noteId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                noteService.deleteNote(noteId);
                return Response.status(Response.Status.OK).build();
            }
        } catch (Exception e) {
            LOGGER.error("NoteController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countNotes(@Context HttpHeaders httpHeaders) {

        try {
            Long count = noteService.countNotes();
            return Response.status(Response.Status.OK).entity(String.valueOf(count)).build();
        } catch (Exception e) {
            LOGGER.error("NoteController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchNotes(@Context HttpHeaders httpHeaders,
                               @QueryParam("limit") int limit, @QueryParam("offset") int offset) {

        try {
            BatchSize batchSize = new BatchSize(limit, offset);
            List<ErrorEntity> errorEntities = batchSizeValidationService.validate(batchSize);
            if (CollectionUtils.isEmpty(errorEntities)) {
                List<Note> notes = noteService.fetchNotes(batchSize.getLimit(), batchSize.getOffset());
                GenericEntity<List<Note>> noteListObj = new GenericEntity<List<Note>>(notes) {
                };
                return Response.status(Response.Status.OK).entity(noteListObj).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("NoteController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
