package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.personalized.dashboard.model.Page;
import org.personalized.dashboard.utils.FieldKeys;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

/**
 * Created by sudan on 17/7/15.
 */
public class DiaryValidationService implements ValidationService<Page> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final ConstraintValidationService constraintValidationService;

    @Inject
    public DiaryValidationService(ConstraintValidationService constraintValidationService) {
        this.constraintValidationService = constraintValidationService;
    }

    @Override
    public List<ErrorEntity> validate(Page page) {
        List<ErrorEntity> errorEntities = Lists.newArrayList();
        validateConstraints(page, errorEntities);
        validateDate(page, errorEntities);
        return errorEntities;
    }


    private void validateConstraints(Page page, List<ErrorEntity> errorEntities) {

        Field fields[] = Page.class.getDeclaredFields();
        for (Field field : fields) {
            Set<ConstraintViolation<Page>> constraintViolations = validator.validateProperty
                    (page, field.getName());
            for (ConstraintViolation<Page> constraintViolation : constraintViolations) {
                constraintValidationService.validateConstraints(field, constraintViolation,
                        errorEntities);
            }
        }
    }

    private void validateDate(Page page, List<ErrorEntity> errorEntities) {

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);

            String date = page.getYear() + "-" + page.getMonth() + "-" + page.getDate();
            dateFormat.parse(date);
        } catch (ParseException e) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_VALUE.name(),
                    ErrorCodes.INVALID_VALUE.getDescription(), FieldKeys.PAGE_DATE);
            errorEntities.add(errorEntity);
        }
    }
}
