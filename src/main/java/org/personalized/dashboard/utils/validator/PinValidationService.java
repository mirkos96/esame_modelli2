package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.commons.validator.routines.UrlValidator;
import org.personalized.dashboard.model.Pin;
import org.personalized.dashboard.utils.FieldKeys;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * Created by sudan on 30/5/15.
 */
public class PinValidationService implements ValidationService<Pin> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final ConstraintValidationService constraintValidationService;

    @Inject
    public PinValidationService(ConstraintValidationService constraintValidationService) {
        this.constraintValidationService = constraintValidationService;
    }

    @Override
    public List<ErrorEntity> validate(Pin pin) {
        List<ErrorEntity> errorEntities = Lists.newArrayList();
        validateConstraints(pin, errorEntities);
        validateImageUrl(pin, errorEntities);
        return errorEntities;
    }

    private void validateImageUrl(Pin pin, List<ErrorEntity> errorEntities) {
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(pin.getImageUrl())) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_URL.name(), ErrorCodes
                    .INVALID_URL.getDescription(), FieldKeys.PIN_IMAGE_URL);
            errorEntities.add(errorEntity);
        }
    }


    private void validateConstraints(Pin pin, List<ErrorEntity> errorEntities) {


        Field fields[] = Pin.class.getDeclaredFields();
        for (Field field : fields) {
            Set<ConstraintViolation<Pin>> constraintViolations = validator.validateProperty(pin,
                    field.getName());
            for (ConstraintViolation<Pin> constraintViolation : constraintViolations) {
                constraintValidationService.validateConstraints(field, constraintViolation,
                        errorEntities);
            }
        }


    }
}
