package org.personalized.dashboard.dao.api;

import org.personalized.dashboard.model.Bookmark;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface BookmarkDao {

    /**
     * Create a new bookmark for the user
     *
     * @param bookmark
     * @param userId
     */
    String create(Bookmark bookmark, String userId);

    /**
     * Get bookmark for the given bookmarkId and userId
     *
     * @param bookmarkId
     * @param userId
     * @return
     */
    Bookmark get(String bookmarkId, String userId);

    /**
     * Update the bookmark and return the updated one
     *
     * @param bookmark
     * @return
     */
    Long update(Bookmark bookmark, String userId);

    /**
     * Delete the bookmark for the given bookmarkId and userId
     *
     * @param bookmarkId
     * @param userId
     */
    Long delete(String bookmarkId, String userId);

    /**
     * Count of bookmarks for the userId
     */
    Long count(String userId);

    /**
     * Fetch all bookmarks for the userId with limit and offset
     *
     * @param limit
     * @param offset
     * @param userId
     */
    List<Bookmark> get(int limit, int offset, String userId);
}