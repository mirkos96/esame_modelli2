package org.personalized.dashboard.controller;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.model.BatchSize;
import org.personalized.dashboard.model.Page;
import org.personalized.dashboard.service.api.DiaryService;
import org.personalized.dashboard.utils.validator.ErrorEntity;
import org.personalized.dashboard.utils.validator.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by sudan on 3/4/15
 */

@Controller
@Scope("request")
@Path("/diaries")
public class DiaryController {

    private final Logger LOGGER = LoggerFactory.getLogger(DiaryController.class);

    private final DiaryService diaryService;
    private final ValidationService diaryValidationService;
    private final ValidationService batchSizeValidationService;


    @Inject
    public DiaryController(DiaryService diaryService,
                           @Named("diary") ValidationService diaryValidationService,
                           @Named("batchSize") ValidationService batchSizeValidationService) {

        this.diaryService = diaryService;
        this.diaryValidationService = diaryValidationService;
        this.batchSizeValidationService = batchSizeValidationService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPage(@Context HttpHeaders httpHeaders, Page page) {

        try {
            List<ErrorEntity> errorEntities = diaryValidationService.validate(page);
            if (CollectionUtils.isEmpty(errorEntities)) {
                String pageId = diaryService.createPage(page);
                return Response.status(Response.Status.CREATED).entity(pageId).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("DiaryController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("{pageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPage(@Context HttpHeaders httpHeaders,
                            @PathParam("pageId") String pageId) {

        try {
            if (StringUtils.isEmpty(pageId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Page page = diaryService.getPage(pageId);
                if (page == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                } else {
                    return Response.status(Response.Status.OK).entity(page).build();
                }
            }
        } catch (Exception e) {
            LOGGER.error("PageController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{pageId}")
    public Response updatePage(@Context HttpHeaders httpHeaders,
                              @PathParam("pageId") String pageId,
                              Page page) {

        try {
            List<ErrorEntity> errorEntities = diaryValidationService.validate(page);
            if (CollectionUtils.isEmpty(errorEntities)) {
                Long modifiedCount = diaryService.updatePage(page);
                if (modifiedCount > 0) {
                    return Response.status(Response.Status.OK).entity(page).build();
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
            LOGGER.error("DiaryController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("{pageId}")
    public Response deletePage(@Context HttpHeaders httpHeaders,
                               @PathParam("pageId") String pageId) {

        try {
            if (StringUtils.isEmpty(pageId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                diaryService.deletePage(pageId);
                return Response.status(Response.Status.OK).build();
            }
        } catch (Exception e) {
            LOGGER.error("DiaryController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countPages(@Context HttpHeaders httpHeaders) {

        try {
            Long count = diaryService.countPages();
            return Response.status(Response.Status.OK).entity(String.valueOf(count)).build();
        } catch (Exception e) {
            LOGGER.error("DiaryController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchPages(@Context HttpHeaders httpHeaders,
                               @QueryParam("limit") int limit, @QueryParam("offset") int
            offset) {

        try {
            BatchSize batchSize = new BatchSize(limit, offset);
            List<ErrorEntity> errorEntities = batchSizeValidationService.validate(batchSize);
            if (CollectionUtils.isEmpty(errorEntities)) {
                List<Page> pages = diaryService.getPages(limit, offset);
                GenericEntity<List<Page>> pageListObj = new GenericEntity<List<Page>>
                        (pages) {
                };
                return Response.status(Response.Status.OK).entity(pageListObj).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("DiaryController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
