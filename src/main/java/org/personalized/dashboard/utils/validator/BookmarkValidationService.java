package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.apache.commons.validator.routines.UrlValidator;
import org.personalized.dashboard.model.Bookmark;
import org.personalized.dashboard.utils.FieldKeys;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * Created by sudan on 26/5/15.
 */
public class BookmarkValidationService implements ValidationService<Bookmark> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final ConstraintValidationService constraintValidationService;

    @Inject
    public BookmarkValidationService(ConstraintValidationService constraintValidationService) {
        this.constraintValidationService = constraintValidationService;
    }

    @Override
    public List<ErrorEntity> validate(Bookmark bookmark) {
        List<ErrorEntity> errorEntities = Lists.newArrayList();
        validateConstraints(bookmark, errorEntities);
        validateUrl(bookmark, errorEntities);


        return errorEntities;
    }

    private void validateUrl(Bookmark bookmark, List<ErrorEntity> errorEntities) {
        UrlValidator urlValidator = new UrlValidator();
        if (!urlValidator.isValid(bookmark.getUrl())) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_URL.name(), ErrorCodes
                    .INVALID_URL.getDescription(), FieldKeys.BOOKMARK_URL);
            errorEntities.add(errorEntity);
        }
    }

    private void validateConstraints(Bookmark bookmark, List<ErrorEntity> errorEntities) {

        Field fields[] = Bookmark.class.getDeclaredFields();
        for (Field field : fields) {
            Set<ConstraintViolation<Bookmark>> constraintViolations = validator.validateProperty
                    (bookmark, field.getName());
            for (ConstraintViolation<Bookmark> constraintViolation : constraintViolations) {
                constraintValidationService.validateConstraints(field, constraintViolation,
                        errorEntities);
            }
        }
    }
}
