package org.personalized.dashboard.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.personalized.dashboard.model.Tag;
import org.personalized.dashboard.service.api.TagService;
import org.personalized.dashboard.utils.validator.ErrorEntity;
import org.personalized.dashboard.utils.validator.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by sudan on 11/7/15.
 */
@Component
@Scope("request")
@Path("/tags")
public class TagController {

    private final Logger LOGGER = LoggerFactory.getLogger(TagController.class);

    private final TagService tagService;
    private final ValidationService tagValidationService;

    @Inject
    public TagController(TagService tagService,
                         @Named("tag") ValidationService tagValidationService) {
        this.tagService = tagService;
        this.tagValidationService = tagValidationService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTags(@Context HttpHeaders httpHeaders, Tag tag) {

        try {
            List<ErrorEntity> errorEntities = tagValidationService.validate(tag);
            if (CollectionUtils.isEmpty(errorEntities)) {
                Long modifiedCount = tagService.updateTags(tag);
                if (modifiedCount > 0) {
                    return Response.status(Response.Status.CREATED).entity(tag).build();
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
            LOGGER.error("TagController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}