package org.personalized.dashboard.utils.validator;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by sudan on 4/6/15.
 */
public class ConstraintValidationService<T> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    public void validateConstraints(Field field, ConstraintViolation<T> constraintViolation,
                                    List<ErrorEntity> errorEntities) {
        String keyName = field.getAnnotation(FieldName.class).name();
        if (constraintViolation.getConstraintDescriptor().getAnnotation().annotationType() ==
                NotEmpty.class ||
                constraintViolation.getConstraintDescriptor().getAnnotation().annotationType() ==
                        NotNull.class) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.EMPTY_FIELD.name(),
                    MessageFormat.format(ErrorCodes.EMPTY_FIELD.getDescription(), keyName),
                    keyName);
            errorEntities.add(errorEntity);
        } else if (constraintViolation.getConstraintDescriptor().getAnnotation().annotationType()
                == Size.class) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.LENGTH_EXCEEDED.name(),
                    MessageFormat.format(ErrorCodes.LENGTH_EXCEEDED.getDescription(), keyName,
                            constraintViolation.getConstraintDescriptor().getAttributes().get
                                    ("max")), keyName);
            errorEntities.add(errorEntity);
        }
    }
}
