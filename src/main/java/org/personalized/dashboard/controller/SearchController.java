package org.personalized.dashboard.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.personalized.dashboard.model.SearchContext;
import org.personalized.dashboard.model.SearchDocument;
import org.personalized.dashboard.service.api.SearchService;
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
 * Created by sudan on 6/7/15.
 */
@Component
@Scope("request")
@Path("/search")
public class SearchController {

    private final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    private final ValidationService searchValidationService;
    private final SearchService searchService;

    @Inject
    public SearchController(@Named("search") ValidationService searchValidationService,
                            SearchService searchService) {
        this.searchValidationService = searchValidationService;
        this.searchService = searchService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEntities(@Context HttpHeaders httpHeaders, SearchContext searchContext) {

        try {
            List<ErrorEntity> errorEntities = searchValidationService.validate(searchContext);
            if (CollectionUtils.isEmpty(errorEntities)) {
                List<SearchDocument> searchDocuments = searchService.searchEntities(searchContext);
                GenericEntity<List<SearchDocument>> searchDocumentsObj = new GenericEntity<List<SearchDocument>>
                        (searchDocuments) {
                };
                return Response.status(Response.Status.OK).entity(searchDocumentsObj).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("SearchController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
