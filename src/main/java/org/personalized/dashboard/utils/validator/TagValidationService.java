package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.personalized.dashboard.model.Tag;
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
 * Created by sudan on 11/7/15.
 */
public class TagValidationService implements ValidationService<Tag> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final ConstraintValidationService constraintValidationService;

    @Inject
    public TagValidationService(ConstraintValidationService constraintValidationService) {
        this.constraintValidationService = constraintValidationService;
    }

    @Override
    public List<ErrorEntity> validate(Tag tag) {
        List<ErrorEntity> errorEntities = Lists.newArrayList();
        validateConstraints(tag, errorEntities);
        validateTags(tag, errorEntities);
        return errorEntities;
    }

    public void validateTags(Tag tag, List<ErrorEntity> errorEntities) {

        if (CollectionUtils.isEmpty(tag.getTags()))
            return;

        if (tag.getTags().size() > Constants.MAX_TAGS_LENGTH) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.TAG_LENGTH_EXCEEDED.name(),
                    MessageFormat.format(ErrorCodes.TAG_LENGTH_EXCEEDED.getDescription(),
                            Constants.MAX_TAGS_LENGTH), FieldKeys.ENTITY_TAGS);
            errorEntities.add(errorEntity);
        }

        for (String tagName : tag.getTags()) {
            if (tagName.length() > Constants.TAG_MAX_LENGTH) {
                ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.LENGTH_EXCEEDED.name(),
                        MessageFormat.format(ErrorCodes.LENGTH_EXCEEDED.getDescription(),
                                Constants.TAG_MAX_LENGTH), FieldKeys.ENTITY_TAGS);
                errorEntities.add(errorEntity);
            }
        }
    }

    public void validateConstraints(Tag tag, List<ErrorEntity> errorEntities) {
        Field fields[] = Tag.class.getDeclaredFields();
        for (Field field : fields) {
            Set<ConstraintViolation<Tag>> constraintViolations = validator.validateProperty
                    (tag, field.getName());
            for (ConstraintViolation<Tag> constraintViolation : constraintViolations) {
                constraintValidationService.validateConstraints(field, constraintViolation,
                        errorEntities);
            }
        }
    }
}
