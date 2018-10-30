package org.personalized.dashboard.utils.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.model.Expense;
import org.personalized.dashboard.utils.Constants;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Created by sudan on 16/7/15.
 */
@ActiveProfiles("test")
public class ExpenseValidationTest {

    private ValidationService expenseValidationService;

    @Before
    public void initialize() {
        this.expenseValidationService = new ExpenseValidationService(
                new ConstraintValidationService<Expense>()
        );
    }

    @Test
    public void testExpenseValidation() {

        Expense expense = new Expense();
        expense.setExpenseId("EXP123456789");
        expense.setTitle("title");
        expense.setDescription("desc");
        expense.setAmount(200);
        expense.setDate(1437066887L);

        List<ErrorEntity> errorEntities = expenseValidationService.validate(expense);
        Assert.assertEquals("Error count is 0", 0, errorEntities.size());

        expense = new Expense();
        expense.setExpenseId("EXP123456789");
        expense.setTitle("");
        expense.setDescription("");

        errorEntities = expenseValidationService.validate(expense);
        Assert.assertEquals("Error count is 4", 4, errorEntities.size());

        Assert.assertEquals("Error 1 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(0).getName());
        Assert.assertEquals("Error 1 description matches", "title cannot be empty", errorEntities
                .get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "title", errorEntities.get(0).getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(1).getName());
        Assert.assertEquals("Error 2 description matches", "description cannot be empty", errorEntities
                .get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "description", errorEntities.get(1).getField());

        Assert.assertEquals("Error 3 name matches", ErrorCodes.INVALID_VALUE.name(), errorEntities
                .get(2).getName());
        Assert.assertEquals("Error 3 description matches", "Invalid value", errorEntities
                .get(2).getDescription());
        Assert.assertEquals("Error 3 field matches", "amount", errorEntities.get(2).getField());

        Assert.assertEquals("Error 4 name matches", ErrorCodes.INVALID_VALUE.name(), errorEntities
                .get(3).getName());
        Assert.assertEquals("Error 4 description matches", "Invalid value", errorEntities
                .get(3).getDescription());
        Assert.assertEquals("Error 4 field matches", "date", errorEntities.get(3).getField());

        expense = new Expense();
        expense.setExpenseId("EXP123456789");
        expense.setTitle("title");
        expense.setDescription("desc");
        expense.setAmount(-1);
        expense.setDate(-1L);

        errorEntities = expenseValidationService.validate(expense);
        Assert.assertEquals("Error count is 2", 2, errorEntities.size());

        Assert.assertEquals("Error 1 name matches", ErrorCodes.INVALID_VALUE.name(), errorEntities
                .get(0).getName());
        Assert.assertEquals("Error 1 description matches", "Invalid value", errorEntities
                .get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "amount", errorEntities.get(0).getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.INVALID_VALUE.name(), errorEntities
                .get(1).getName());
        Assert.assertEquals("Error 2 description matches", "Invalid value", errorEntities
                .get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "date", errorEntities.get(1).getField());

        StringBuilder invalidName = new StringBuilder();
        for (int i = 0; i < Constants.TITLE_MAX_LENGTH + 1; i++) {
            invalidName.append("n");
        }

        StringBuilder invalidDescription = new StringBuilder();
        for (int i = 0; i < Constants.CONTENT_MAX_LENGTH + 1; i++) {
            invalidDescription.append("c");
        }

        expense = new Expense();
        expense.setExpenseId("EXP123456789");
        expense.setTitle(invalidName.toString());
        expense.setDescription(invalidDescription.toString());
        expense.setAmount(200);
        expense.setDate(1437066887L);

        errorEntities = expenseValidationService.validate(expense);
        Assert.assertEquals("Error count is 2", 2, errorEntities.size());


        Assert.assertEquals("Error 1 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(0).getName());
        Assert.assertEquals("Error 1 description matches", "title cannot exceed 50 characters",
                errorEntities.get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "title", errorEntities.get(0).getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(1).getName());
        Assert.assertEquals("Error 2 description matches", "description cannot exceed 1,000 " +
                "characters", errorEntities.get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "description", errorEntities.get(1).getField
                ());

        expense = new Expense();
        expense.setExpenseId("EXP123456789");
        expense.setTitle("title");
        expense.setDate(1468691544000L);
        expense.setDescription("desc");
        expense.setAmount(200);

        errorEntities = expenseValidationService.validate(expense);
        Assert.assertEquals("Error count is 2", 1, errorEntities.size());

        Assert.assertEquals("Error 1 name matches", ErrorCodes.INVALID_VALUE
                        .name(),
                errorEntities.get(0).getName());
        Assert.assertEquals("Error 1 description matches", "Invalid value",
                errorEntities.get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "date", errorEntities.get(0).getField());

    }
}
