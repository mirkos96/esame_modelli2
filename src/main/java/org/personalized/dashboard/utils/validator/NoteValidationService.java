package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.personalized.dashboard.model.Note;

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
public class NoteValidationService implements ValidationService<Note> {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();
    private final ConstraintValidationService constraintValidationService;

    @Inject
    public NoteValidationService(ConstraintValidationService constraintValidationService) {
        this.constraintValidationService = constraintValidationService;
    }

    @Override
    public List<ErrorEntity> validate(Note note) {
        List<ErrorEntity> errorEntities = Lists.newArrayList();
        validateConstraints(note, errorEntities);
        return errorEntities;
    }

    private void validateConstraints(Note note, List<ErrorEntity> errorEntities) {

        Field fields[] = Note.class.getDeclaredFields();
        for (Field field : fields) {
            Set<ConstraintViolation<Note>> constraintViolations = validator.validateProperty
                    (note, field.getName());
            for (ConstraintViolation<Note> constraintViolation : constraintViolations) {
                constraintValidationService.validateConstraints(field, constraintViolation,
                        errorEntities);
            }
        }

    }
}
