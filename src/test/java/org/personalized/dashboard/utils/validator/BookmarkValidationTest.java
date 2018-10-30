package org.personalized.dashboard.utils.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.model.Bookmark;
import org.personalized.dashboard.utils.Constants;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Created by sudan on 30/5/15.
 */
@ActiveProfiles("test")
public class BookmarkValidationTest {

    private ValidationService bookmarkValidationService;

    @Before
    public void initialize() {
        this.bookmarkValidationService = new BookmarkValidationService(new
                ConstraintValidationService<Bookmark>());
    }

    @Test
    public void testBookmarkValidation() {
        Bookmark bookmark = new Bookmark();
        bookmark.setBookmarkId("BOK123456789");
        bookmark.setName("");
        bookmark.setDescription("");
        bookmark.setUrl("");
        List<ErrorEntity> errorEntities = bookmarkValidationService.validate(bookmark);
        Assert.assertEquals("Error count is 4", 4, errorEntities.size());

        Assert.assertEquals("Error 1 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(0).getName());
        Assert.assertEquals("Error 1 description matches", "name cannot be empty", errorEntities
                .get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "name", errorEntities.get(0).getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(1).getName());
        Assert.assertEquals("Error 2 description matches", "description cannot be empty",
                errorEntities.get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "description", errorEntities.get(1).getField
                ());

        Assert.assertEquals("Error 3 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(2).getName());
        Assert.assertEquals("Error 3 description matches", "url cannot be empty", errorEntities
                .get(2).getDescription());
        Assert.assertEquals("Error 3 field matches", "url", errorEntities.get(2).getField());

        Assert.assertEquals("Error 4 name matches", ErrorCodes.INVALID_URL.name(), errorEntities
                .get(3).getName());
        Assert.assertEquals("Error 4 description matches", "Invalid URL format", errorEntities
                .get(3).getDescription());
        Assert.assertEquals("Error 4 field matches", "url", errorEntities.get(3).getField());

        bookmark = new Bookmark();
        bookmark.setBookmarkId("BOK123456789");
        bookmark.setName("sudan");
        bookmark.setDescription("desc");
        bookmark.setUrl("http://www.google.com");
        errorEntities = bookmarkValidationService.validate(bookmark);
        Assert.assertEquals("Error count is 0", 0, errorEntities.size());

        StringBuilder invalidUrl = new StringBuilder();
        for (int i = 0; i < Constants.URL_MAX_LENGTH + 1; i++) {
            invalidUrl.append("a");
        }

        StringBuilder invalidName = new StringBuilder();
        for (int i = 0; i < Constants.TITLE_MAX_LENGTH + 1; i++) {
            invalidName.append("n");
        }

        StringBuilder invalidDescription = new StringBuilder();
        for (int i = 0; i < Constants.CONTENT_MAX_LENGTH + 1; i++) {
            invalidDescription.append("c");
        }

        bookmark = new Bookmark();
        bookmark.setBookmarkId("BOK123456789");
        bookmark.setName(invalidName.toString());
        bookmark.setDescription(invalidDescription.toString());
        bookmark.setUrl(invalidUrl.toString());
        errorEntities = bookmarkValidationService.validate(bookmark);
        Assert.assertEquals("Error count is 4", 4, errorEntities.size());


        Assert.assertEquals("Error 1 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(0).getName());
        Assert.assertEquals("Error 1 description matches", "name cannot exceed 50 characters",
                errorEntities.get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "name", errorEntities.get(0).getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(1).getName());
        Assert.assertEquals("Error 2 description matches", "description cannot exceed 1,000 " +
                "characters", errorEntities.get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "description", errorEntities.get(1).getField
                ());

        Assert.assertEquals("Error 3 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(2).getName());
        Assert.assertEquals("Error 3 description matches", "url cannot exceed 300 characters",
                errorEntities.get(2).getDescription());
        Assert.assertEquals("Error 3 field matches", "url", errorEntities.get(2).getField());

        Assert.assertEquals("Error 4 name matches", ErrorCodes.INVALID_URL.name(), errorEntities
                .get(3).getName());
        Assert.assertEquals("Error 4 description matches", "Invalid URL format", errorEntities
                .get(3).getDescription());
        Assert.assertEquals("Error 4 field matches", "url", errorEntities.get(3).getField());

    }
}
