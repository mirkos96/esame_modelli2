package org.personalized.dashboard.utils.validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.model.Note;
import org.personalized.dashboard.utils.Constants;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Created by sudan on 30/5/15.
 */
@ActiveProfiles("test")
public class NoteValidationTest {

    private ValidationService noteValidationService;

    @Before
    public void initialize() {
        this.noteValidationService = new NoteValidationService(new
                ConstraintValidationService<Note>());
    }

    @Test
    public void testNoteValidation() {
        Note note = new Note();
        note.setNoteId("NOT123456789");
        note.setTitle("");
        note.setNote("");
        List<ErrorEntity> errorEntities = noteValidationService.validate(note);
        Assert.assertEquals("Error count is 2", 2, errorEntities.size());

        Assert.assertEquals("Error 1 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(0).getName());
        Assert.assertEquals("Error 1 description matches", "title cannot be empty", errorEntities
                .get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "title", errorEntities.get(0).getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.EMPTY_FIELD.name(), errorEntities
                .get(1).getName());
        Assert.assertEquals("Error 2 description matches", "note cannot be empty", errorEntities
                .get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "note", errorEntities.get(1).getField());


        note = new Note();
        note.setNoteId("NOT123456789");
        note.setTitle("title");
        note.setNote("desc");
        errorEntities = noteValidationService.validate(note);
        Assert.assertEquals("Error count is 0", 0, errorEntities.size());


        StringBuilder invalidTitle = new StringBuilder();
        for (int i = 0; i < Constants.TITLE_MAX_LENGTH + 1; i++) {
            invalidTitle.append("n");
        }

        StringBuilder invalidNote = new StringBuilder();
        for (int i = 0; i < Constants.CONTENT_MAX_LENGTH + 1; i++) {
            invalidNote.append("c");
        }

        note = new Note();
        note.setNoteId("NOT123456789");
        note.setTitle(invalidTitle.toString());
        note.setNote(invalidNote.toString());
        errorEntities = noteValidationService.validate(note);
        Assert.assertEquals("Error count is 2", 2, errorEntities.size());


        Assert.assertEquals("Error 1 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(0).getName());
        Assert.assertEquals("Error 1 description matches", "title cannot exceed 50 characters",
                errorEntities.get(0).getDescription());
        Assert.assertEquals("Error 1 field matches", "title", errorEntities.get(0).getField());

        Assert.assertEquals("Error 2 name matches", ErrorCodes.LENGTH_EXCEEDED.name(),
                errorEntities.get(1).getName());
        Assert.assertEquals("Error 2 description matches", "note cannot exceed 1,000 characters",
                errorEntities.get(1).getDescription());
        Assert.assertEquals("Error 2 field matches", "note", errorEntities.get(1).getField());
    }
}
