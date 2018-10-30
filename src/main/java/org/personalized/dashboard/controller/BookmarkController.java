package org.personalized.dashboard.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.model.BatchSize;
import org.personalized.dashboard.model.Bookmark;
import org.personalized.dashboard.service.api.BookmarkService;
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
@Path("/bookmarks")
public class BookmarkController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookmarkController.class);

    private final BookmarkService bookmarkService;
    private final ValidationService bookmarkValidationService;
    private final ValidationService batchSizeValidationService;

    @Inject
    public BookmarkController(BookmarkService bookmarkService,
                              @Named("bookmark") ValidationService bookmarkValidationService,
                              @Named("batchSize") ValidationService batchSizeValidationService) {
        this.bookmarkService = bookmarkService;
        this.bookmarkValidationService = bookmarkValidationService;
        this.batchSizeValidationService = batchSizeValidationService;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBookmark(@Context HttpHeaders httpHeaders, Bookmark bookmark) {

        try {
            List<ErrorEntity> errorEntities = bookmarkValidationService.validate(bookmark);
            if (CollectionUtils.isEmpty(errorEntities)) {
                String bookmarkId = bookmarkService.createBookmark(bookmark);
                return Response.status(Response.Status.CREATED).entity(bookmarkId).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("BookmarkController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("{bookmarkId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookmark(@Context HttpHeaders httpHeaders,
                                @PathParam("bookmarkId") String bookmarkId) {

        try {
            if (StringUtils.isEmpty(bookmarkId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Bookmark bookmark = bookmarkService.getBookmark(bookmarkId);
                if (bookmark == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                } else {
                    return Response.status(Response.Status.OK).entity(bookmark).build();
                }
            }
        } catch (Exception e) {
            LOGGER.error("BookmarkController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{bookmarkId}")
    public Response updateBookmark(@Context HttpHeaders httpHeaders,
                                   @PathParam("bookmarkId") String bookmarkId,
                                   Bookmark bookmark) {

        try {
            List<ErrorEntity> errorEntities = bookmarkValidationService.validate(bookmark);
            if (CollectionUtils.isEmpty(errorEntities)) {
                Long modifiedCount = bookmarkService.updateBookmark(bookmark);
                if (modifiedCount > 0) {
                    return Response.status(Response.Status.OK).entity(bookmark).build();
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
            LOGGER.error("BookmarkController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("{bookmarkId}")
    public Response deleteBookmark(@Context HttpHeaders httpHeaders,
                                   @PathParam("bookmarkId") String bookmarkId) {

        try {
            if (StringUtils.isEmpty(bookmarkId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                bookmarkService.deleteBookmark(bookmarkId);
                return Response.status(Response.Status.OK).build();
            }
        } catch (Exception e) {
            LOGGER.error("BookmarkController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countBookmarks(@Context HttpHeaders httpHeaders) {

        try {
            Long count = bookmarkService.countBookmarks();
            return Response.status(Response.Status.OK).entity(String.valueOf(count)).build();
        } catch (Exception e) {
            LOGGER.error("BookmarkController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchBookmarks(@Context HttpHeaders httpHeaders,
                                   @QueryParam("limit") int limit, @QueryParam("offset") int
            offset) {

        try {
            BatchSize batchSize = new BatchSize(limit, offset);
            List<ErrorEntity> errorEntities = batchSizeValidationService.validate(batchSize);
            if (CollectionUtils.isEmpty(errorEntities)) {
                List<Bookmark> bookmarks = bookmarkService.fetchBookmarks(batchSize.getLimit(),
                        batchSize.getOffset());
                GenericEntity<List<Bookmark>> bookmarkListObj = new GenericEntity<List<Bookmark>>
                        (bookmarks) {
                };
                return Response.status(Response.Status.OK).entity(bookmarkListObj).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("BookmarkController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
