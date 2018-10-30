package org.personalized.dashboard.service.api;

import org.personalized.dashboard.model.Note;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface NoteService {

    /**
     * Create a note for the user
     *
     * @param note
     * @return
     */
    String createNote(Note note);

    /**
     * Get the note for the id
     *
     * @param noteId
     * @return
     */
    Note getNote(String noteId);

    /**
     * Update the note
     *
     * @param note
     * @return
     */
    Long updateNote(Note note);

    /**
     * Delete the  note for the noteId
     *
     * @param noteId
     */
    void deleteNote(String noteId);

    /**
     * Count the note for the user
     *
     * @return
     */
    Long countNotes();

    /**
     * Fetch the notes with given limit and offset
     *
     * @param limit
     * @param offset
     * @return
     */
    List<Note> fetchNotes(int limit, int offset);

}
