package org.personalized.dashboard.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.model.BatchSize;
import org.personalized.dashboard.model.Expense;
import org.personalized.dashboard.model.ExpenseFilter;
import org.personalized.dashboard.service.api.ExpenseService;
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
 * Created by sudan on 17/5/15.
 */
@Controller
@Scope("request")
@Path("/expenses")
public class ExpenseController {

    private final Logger LOGGER = LoggerFactory.getLogger(ExpenseController.class);

    private final ExpenseService expenseService;
    private final ValidationService expenseValidationService;
    private final ValidationService batchSizeValidationService;

    @Inject
    public ExpenseController(ExpenseService expenseService,
                             @Named("expense") ValidationService expenseValidationService,
                             @Named("batchSize") ValidationService batchSizeValidationService) {
        this.expenseService = expenseService;
        this.expenseValidationService = expenseValidationService;
        this.batchSizeValidationService = batchSizeValidationService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createExpense(@Context HttpHeaders httpHeaders, Expense expense) {
        try {
            List<ErrorEntity> errorEntities = expenseValidationService.validate(expense);
            if (CollectionUtils.isEmpty(errorEntities)) {
                String expenseId = expenseService.createExpense(expense);
                return Response.status(Response.Status.CREATED).entity(expenseId).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("ExpenseController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("{expenseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExpense(@Context HttpHeaders httpHeaders, @PathParam("expenseId") String expenseId) {

        try {
            if (StringUtils.isEmpty(expenseId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                Expense expense = expenseService.getExpense(expenseId);
                if (expense == null) {
                    return Response.status(Response.Status.NOT_FOUND).build();
                } else {
                    return Response.status(Response.Status.OK).entity(expense).build();
                }
            }
        } catch (Exception e) {
            LOGGER.error("ExpenseController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{expenseId}")
    public Response updateExpense(@Context HttpHeaders httpHeaders,
                                  @PathParam("expenseId") String expenseId,
                                  Expense expense) {

        try {
            List<ErrorEntity> errorEntities = expenseValidationService.validate(expense);
            if (CollectionUtils.isEmpty(errorEntities)) {
                Long modifiedCount = expenseService.updateExpense(expense);
                if (modifiedCount > 0) {
                    return Response.status(Response.Status.OK).entity(expense).build();
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
            LOGGER.error("ExpenseController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("{expenseId}")
    public Response deleteExpense(@Context HttpHeaders httpHeaders,
                                  @PathParam("expenseId") String expenseId) {

        try {
            if (StringUtils.isEmpty(expenseId)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                expenseService.deleteExpense(expenseId);
                return Response.status(Response.Status.OK).build();
            }
        } catch (Exception e) {
            LOGGER.error("ExpenseController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countExpenses(@Context HttpHeaders httpHeaders, ExpenseFilter expenseFilter) {

        try {
            Long count = expenseService.countExpense(expenseFilter);
            return Response.status(Response.Status.OK).entity(String.valueOf(count)).build();
        } catch (Exception e) {
            LOGGER.error("ExpenseController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchExpenses(@Context HttpHeaders httpHeaders,
                                  @QueryParam("limit") int limit, @QueryParam("offset")
    int offset, ExpenseFilter expenseFilter) {

        try {
            BatchSize batchSize = new BatchSize(limit, offset);
            List<ErrorEntity> errorEntities = batchSizeValidationService.validate(batchSize);
            if (CollectionUtils.isEmpty(errorEntities)) {
                List<Expense> expenses = expenseService.fetchExpenses(expenseFilter,
                        batchSize.getLimit(), batchSize.getOffset());
                GenericEntity<List<Expense>> expenseListObj = new GenericEntity<List<Expense>>
                        (expenses) {
                };
                return Response.status(Response.Status.OK).entity(expenseListObj).build();
            } else {
                GenericEntity<List<ErrorEntity>> errorObj = new GenericEntity<List<ErrorEntity>>
                        (errorEntities) {
                };
                return Response.status(Response.Status.BAD_REQUEST).entity(errorObj).build();
            }
        } catch (Exception e) {
            LOGGER.error("ExpenseController encountered an error", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
