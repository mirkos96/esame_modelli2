package org.personalized.dashboard.dao.api;

import org.personalized.dashboard.model.Note;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface NoteDao {

    /**
     * Create a new note for the user
     *
     * @param note
     * @param userId
     * @return
     */
    String create(Note note, String userId);

    /**
     * Get note for the noteId and userId
     *
     * @param noteId
     * @param userId
     * @return
     */
    Note get(String noteId, String userId);

    /**
     * Update the note and return the updated one
     *
     * @param note
     * @param userId
     * @return
     */
    Long update(Note note, String userId);

    /**
     * Delete the note for the given noteId and userId
     *
     * @param noteId
     * @param userId
     * @return
     */
    Long delete(String noteId, String userId);

    /**
     * Count of notes for the userId
     */
    Long count(String userId);

    /**
     * Fetch all notes for the userId with limit and offset
     *
     * @param limit
     * @param offset
     * @param userId
     */
    List<Note> get(int limit, int offset, String userId);

}
