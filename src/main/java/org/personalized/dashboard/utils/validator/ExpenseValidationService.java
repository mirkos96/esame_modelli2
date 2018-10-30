package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.personalized.dashboard.model.Expense;
import org.personalized.dashboard.utils.FieldKeys;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by sudan on 12/7/15.
 */
public class ExpenseValidationService implements ValidationService<Expense> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final ConstraintValidationService constraintValidationService;

    @Inject
    public ExpenseValidationService(ConstraintValidationService constraintValidationService) {
        this.constraintValidationService = constraintValidationService;
    }

    @Override
    public List<ErrorEntity> validate(Expense expense) {
        List<ErrorEntity> errorEntities = Lists.newArrayList();
        validateConstraints(expense, errorEntities);
        validateAmount(expense, errorEntities);
        validateDate(expense, errorEntities);
        return errorEntities;
    }

    public void validateConstraints(Expense expense, List<ErrorEntity> errorEntities) {
        Field fields[] = Expense.class.getDeclaredFields();
        for (Field field : fields) {
            Set<ConstraintViolation<Expense>> constraintViolations = validator.validateProperty
                    (expense, field.getName());
            for (ConstraintViolation<Expense> constraintViolation : constraintViolations) {
                constraintValidationService.validateConstraints(field, constraintViolation,
                        errorEntities);
            }
        }
    }

    private void validateAmount(Expense expense, List<ErrorEntity> errorEntities) {

        if (expense.getAmount() <= 0) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_VALUE.name(),
                    ErrorCodes.INVALID_VALUE.getDescription(), FieldKeys.EXPENSE_AMOUNT);
            errorEntities.add(errorEntity);
        }
    }

    private void validateDate(Expense expense, List<ErrorEntity> errorEntities) {

        try {
            new Date(expense.getDate());

            if (expense.getDate() > System.currentTimeMillis() || expense.getDate() <= 0) {
                ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_VALUE.name(),
                        ErrorCodes.INVALID_VALUE.getDescription(), FieldKeys.EXPENSE_DATE);
                errorEntities.add(errorEntity);
            }
        } catch (Exception e) {

            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_VALUE.name(),
                    ErrorCodes.INVALID_VALUE.getDescription(), FieldKeys.EXPENSE_DATE);
            errorEntities.add(errorEntity);
        }
    }
}
