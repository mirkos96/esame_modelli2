package org.personalized.dashboard.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.bootstrap.ConfigManager;
import org.personalized.dashboard.bootstrap.MongoBootstrap;
import org.personalized.dashboard.dao.api.NoteDao;
import org.personalized.dashboard.dao.impl.NoteDaoImpl;
import org.personalized.dashboard.model.Note;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.generator.IdGenerator;
import org.personalized.dashboard.utils.htmltidy.DOMParser;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;

/**
 * Created by sudan on 31/5/15.
 */
@ActiveProfiles("test")
public class NoteDaoTest {


    private NoteDao noteDao;
    private DOMParser domParser;

    @Before
    public void initialize() throws IOException {
        ConfigManager.init();
        this.domParser = new DOMParser();
        this.noteDao = new NoteDaoImpl(new IdGenerator());
    }

    @Test
    public void testNoteDao() {

        Boolean isDebugMode = Boolean.valueOf(ConfigKeys.MONGO_DEBUG_FLAG);

        /*
            To run these test cases enable isDebugMode in config.properties
         */
        if (isDebugMode) {
            MongoBootstrap.init();
            MongoBootstrap.getMongoDatabase().getCollection(Constants.NOTES).drop();

            Note note1 = new Note();
            note1.setTitle("title1");
            note1.setNote(domParser.removeMalformedTags("note1"));
            note1.setSummary(domParser.extractSummary("note1"));

            Note note2 = new Note();
            note2.setTitle("title2");
            note2.setNote(domParser.removeMalformedTags("note2"));
            note2.setSummary(domParser.extractSummary("note2"));

            // Create two notes
            String noteid1 = noteDao.create(note1, "1");
            String noteid2 = noteDao.create(note2, "1");

            Note noteRead1 = noteDao.get(noteid1, "1");
            Note noteRead2 = noteDao.get(noteid2, "1");

            // Verify the created values
            Assert.assertEquals("NoteIds match", noteid1, noteRead1.getNoteId());
            Assert.assertEquals("title match", "title1", noteRead1.getTitle());
            Assert.assertEquals("note match", "note1", noteRead1.getNote());
            Assert.assertNotNull("Createdon is not null", noteRead1.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", noteRead2.getModifiedAt());

            Assert.assertEquals("NoteIds match", noteid2, noteRead2.getNoteId());
            Assert.assertEquals("title match", "title2", noteRead2.getTitle());
            Assert.assertEquals("note match", "note2", noteRead2.getNote());
            Assert.assertNotNull("Createdon is not null", noteRead2.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", noteRead2.getModifiedAt());

            // Update and verify the updated values

            noteRead2.setTitle("google_advanced");
            noteRead2.setNote(domParser.removeMalformedTags("desc_advanced"));
            noteRead2.setSummary(domParser.extractSummary("desc_advanced"));
            noteDao.update(noteRead2, "1");

            noteRead2 = noteDao.get(noteid2, "1");

            Assert.assertEquals("NoteIds match", noteid2, noteRead2.getNoteId());
            Assert.assertEquals("title match", "google_advanced", noteRead2.getTitle());
            Assert.assertEquals("note match", "desc_advanced", noteRead2.getNote());
            Assert.assertNotNull("Createdon is not null", noteRead2.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", noteRead2.getModifiedAt());

            // verify the count
            long count = noteDao.count("1");
            Assert.assertEquals("Count is 2", 2, count);


            // verify the paginated api
            List<Note> notes = noteDao.get(10, 0, "1");
            Assert.assertEquals("Count is 2", 2, notes.size());

            Assert.assertEquals("NoteIds match", noteid2, notes.get(0).getNoteId());
            Assert.assertEquals("title match", "google_advanced", notes.get(0).getTitle());
            Assert.assertEquals("note match", "desc_advanced", notes.get(0).getNote());
            Assert.assertEquals("summary match", "desc_advanced", notes.get(0).getSummary());
            Assert.assertNotNull("Createdon is not null", notes.get(0).getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", notes.get(0).getModifiedAt());

            Assert.assertEquals("NoteIds match", noteid1, notes.get(1).getNoteId());
            Assert.assertEquals("title match", "title1", notes.get(1).getTitle());
            Assert.assertEquals("note match", "note1", notes.get(1).getNote());
            Assert.assertEquals("summary match", "note1", notes.get(1).getSummary());
            Assert.assertNotNull("Createdon is not null", notes.get(1).getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", notes.get(1).getModifiedAt());

            // verify delete followed by data
            noteDao.delete(noteid2, "1");
            count = noteDao.count("1");
            Assert.assertNull("Note2 is deleted", noteDao.get(noteid2, "1"));
            Assert.assertNotNull("Note1 is not deleted", noteDao.get(noteid1, "1"));
            Assert.assertEquals("Count now is 1", 1, count);

            notes = noteDao.get(10, 0, "1");
            Assert.assertEquals("Count is 1", 1, notes.size());

            Assert.assertEquals("NoteIds match", noteid1, notes.get(0).getNoteId());
            Assert.assertEquals("title match", "title1", notes.get(0).getTitle());
            Assert.assertEquals("note match", "note1", notes.get(0).getNote());
            Assert.assertEquals("summary match", "note1", notes.get(0).getSummary());
            Assert.assertNotNull("Createdon is not null", notes.get(0).getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", notes.get(0).getModifiedAt());

            Note note3 = new Note();
            note3.setTitle("title3");
            String text = "<html><body><head><script type='text/javascript' src='hello.js'></script>" +
                    "<div>hello<ul><li>one</li><li>2</li></ul></div>" +
                    "<p><img src='https://github.com'/></p></body></html>";

            note3.setNote(domParser.removeMalformedTags(text));
            note3.setSummary(domParser.extractSummary(text));
            String noteId3 = noteDao.create(note3, "1");

            Note noteRead3 = noteDao.get(noteId3, "1");

            Assert.assertEquals("NoteIds match", noteId3, noteRead3.getNoteId());
            Assert.assertEquals("title match", "title3", noteRead3.getTitle());
            Assert.assertEquals("note match", "<div>hello<ul><li>one</li><li>2</li></ul></div>" +
                    "<p><img src=\"https://github.com\" /></p>", noteRead3.getNote());
            Assert.assertEquals("summary match", "hello one 2", noteRead3.getSummary());
            Assert.assertNotNull("Createdon is not null", noteRead3.getCreatedOn());
            Assert.assertNotNull("modifiedAt is not null", noteRead3.getModifiedAt());

            MongoBootstrap.getMongoDatabase().getCollection(Constants.NOTES).drop();

        }
    }
}
