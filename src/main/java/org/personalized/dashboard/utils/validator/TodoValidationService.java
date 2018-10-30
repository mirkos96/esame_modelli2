package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.personalized.dashboard.model.Task;
import org.personalized.dashboard.model.Todo;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * Created by sudan on 31/5/15.
 */
public class TodoValidationService implements ValidationService<Todo> {


    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final ConstraintValidationService constraintValidationService;

    @Inject
    public TodoValidationService(ConstraintValidationService constraintValidationService) {
        this.constraintValidationService = constraintValidationService;
    }

    @Override
    public List<ErrorEntity> validate(Todo todo) {
        List<ErrorEntity> errorEntities = Lists.newArrayList();
        validateConstraints(todo, errorEntities);
        validateTasks(todo, errorEntities);
        return errorEntities;
    }

    private void validateTasks(Todo todo, List<ErrorEntity> errorEntities) {
        if (!CollectionUtils.isEmpty(todo.getTasks()) && todo.getTasks().size() > Constants
                .MAX_TASK_SIZE) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.TASKS_LENGTH_EXCEEDED.name(),
                    MessageFormat.format(ErrorCodes.TASKS_LENGTH_EXCEEDED.getDescription(),
                            Constants.MAX_TASK_SIZE), FieldKeys.TASK_LIST);
            errorEntities.add(errorEntity);
        }
    }


    private void validateConstraints(Todo todo, List<ErrorEntity> errorEntities) {

        Field todoFields[] = Todo.class.getDeclaredFields();
        for (Field todoField : todoFields) {
            Set<ConstraintViolation<Todo>> constraintViolations = validator.validateProperty
                    (todo, todoField.getName());
            for (ConstraintViolation<Todo> constraintViolation : constraintViolations) {
                constraintValidationService.validateConstraints(todoField, constraintViolation,
                        errorEntities);
            }
        }

        for (Task task : todo.getTasks()) {

            if (task.getPercentCompletion() < 0 || task.getPercentCompletion() > 100) {
                ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_VALUE.name(),
                        ErrorCodes.INVALID_VALUE.getDescription(), FieldKeys
                        .TASK_PERCENT_COMPLETION);
                errorEntities.add(errorEntity);
            }

            Field taskFields[] = Task.class.getDeclaredFields();
            for (Field taskField : taskFields) {
                Set<ConstraintViolation<Task>> constraintViolations = validator.validateProperty
                        (task, taskField.getName());
                for (ConstraintViolation<Task> constraintViolation : constraintViolations) {
                    constraintValidationService.validateConstraints(taskField,
                            constraintViolation, errorEntities);
                }
            }
        }
    }

}
