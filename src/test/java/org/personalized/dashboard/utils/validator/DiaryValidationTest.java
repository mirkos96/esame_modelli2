package org.personalized.dashboard.utils.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.model.Note;
import org.personalized.dashboard.model.Page;
import org.personalized.dashboard.utils.Constants;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Created by sudan on 18/7/15.
 */
@ActiveProfiles("test")
public class DiaryValidationTest {


    private ValidationService diaryValidationService;

    @Before
    public void initialize() {
        this.diaryValidationService = new DiaryValidationService(new
                ConstraintValidationService<Note>());
    }

    @Test
    public void testDiaryValidation() {

        Page page = new Page();
        page.setMonth(10);
        page.setDate(20);
        page.setYear(2016);

        List<ErrorEntity> errorEntities = diaryValidationService.validate(page);

        Assert.assertEquals("Error count is 2", 2, errorEntities.size());

        Assert.assertEquals("Error name match", ErrorCodes.EMPTY_FIELD.name(), errorEntities.get(0).getName());
        Assert.assertEquals("Error desc match", "title cannot be empty", errorEntities.get(0).getDescription());
        Assert.assertEquals("Error field match", "title", errorEntities.get(0).getField());

        Assert.assertEquals("Error name match", ErrorCodes.EMPTY_FIELD.name(), errorEntities.get(1).getName());
        Assert.assertEquals("Error desc match", "description cannot be empty", errorEntities.get(1).getDescription());
        Assert.assertEquals("Error field match", "description", errorEntities.get(1).getField());

        StringBuilder invalidTitle = new StringBuilder();
        for (int i = 0; i < Constants.TITLE_MAX_LENGTH + 1; i++) {
            invalidTitle.append("a");
        }

        StringBuilder invalidDescription = new StringBuilder();
        for (int i = 0; i < Constants.TEXT_MAX_LENGTH + 1; i++) {
            invalidDescription.append("a");
        }

        page = new Page();
        page.setTitle(invalidTitle.toString());
        page.setContent(invalidDescription.toString());
        page.setMonth(10);
        page.setDate(21);
        page.setYear(2016);

        errorEntities = diaryValidationService.validate(page);

        Assert.assertEquals("Error count is 2", 2, errorEntities.size());


        Assert.assertEquals("Error name match", ErrorCodes.LENGTH_EXCEEDED.name(), errorEntities.get(0).getName());
        Assert.assertEquals("Error desc match", "title cannot exceed 50 characters", errorEntities.get(0).getDescription());
        Assert.assertEquals("Error field match", "title", errorEntities.get(0).getField());

        Assert.assertEquals("Error name match", ErrorCodes.LENGTH_EXCEEDED.name(), errorEntities.get(1).getName());
        Assert.assertEquals("Error desc match",
                "description cannot exceed 3,000 characters", errorEntities.get(1).getDescription());
        Assert.assertEquals("Error field match", "description", errorEntities.get(1).getField());

        page = new Page();
        page.setTitle("title");
        page.setContent("content");
        page.setDate(31);
        page.setMonth(2);
        page.setYear(2016);

        errorEntities = diaryValidationService.validate(page);

        Assert.assertEquals("Error count is 1", 1, errorEntities.size());

        Assert.assertEquals("Error name match", ErrorCodes.INVALID_VALUE.name(), errorEntities.get(0).getName());
        Assert.assertEquals("Error desc match", "Invalid value", errorEntities.get(0).getDescription());
        Assert.assertEquals("Error field match", "date", errorEntities.get(0).getField());
    }
}
