package org.personalized.dashboard.controller;

import com.google.inject.Inject;
import org.personalized.dashboard.model.Activity;
import org.personalized.dashboard.service.api.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * Created by sudan on 31/5/15.
 */
@Component
@Scope("request")
@Path("activities")
public class ActivityController {

    private final Logger LOGGER = LoggerFactory.getLogger(ActivityController.class);

    private final ActivityService activityService;

    @Inject
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivities(@Context HttpHeaders httpHeaders) {

        try {
            List<Activity> activities = activityService.get();
            GenericEntity<List<Activity>> activityListObj = new GenericEntity<List<Activity>>
                    (activities) {
            };
            return Response.status(Response.Status.OK).entity(activityListObj).build();
        } catch (Exception e) {
            LOGGER.error("ActivityController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

