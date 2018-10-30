package org.personalized.dashboard.service.api;

import org.personalized.dashboard.model.Bookmark;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface BookmarkService {

    /**
     * Create a bookmark for the user
     *
     * @param bookmark
     * @return
     */
    String createBookmark(Bookmark bookmark);

    /**
     * Get the bookmark for the id
     *
     * @param bookmarkId
     * @return
     */
    Bookmark getBookmark(String bookmarkId);

    /**
     * Update the bookmark
     *
     * @param bookmark
     * @return
     */
    Long updateBookmark(Bookmark bookmark);

    /**
     * Delete the  bookmark for the bookmarkId
     *
     * @param bookmarkId
     */
    void deleteBookmark(String bookmarkId);

    /**
     * Count the bookmarks for the user
     *
     * @return
     */
    Long countBookmarks();

    /**
     * Fetch the bookmarks with given limit and offset
     *
     * @param limit
     * @param offset
     * @return
     */
    List<Bookmark> fetchBookmarks(int limit, int offset);
}
