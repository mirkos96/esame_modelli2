package org.personalized.dashboard.controller;

import com.google.inject.Inject;
import org.personalized.dashboard.model.User;
import org.personalized.dashboard.service.api.UserService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sudan on 26/7/15.
 */
@Controller
@Scope("request")
@Path("/users")
public class UserController {

    private final UserService userService;

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@Context HttpHeaders httpHeaders) {
        User user = userService.getUser();
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.status(Response.Status.OK).entity(user).build();
        }
    }
}
