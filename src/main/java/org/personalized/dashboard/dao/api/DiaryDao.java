package org.personalized.dashboard.dao.api;

import org.personalized.dashboard.model.Page;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface DiaryDao {

    /**
     * Create a new page for the user
     *
     * @param page
     * @param userId
     * @return
     */
    String create(Page page, String userId);

    /**
     * Get the page for the user
     *
     * @param pageId
     * @param userId
     * @return
     */
    Page get(String pageId, String userId);

    /**
     * Update the page for the user
     *
     * @param page
     * @param userId
     * @return
     */
    Long update(Page page, String userId);

    /**
     * Delete the page for the userId
     *
     * @param pageId
     * @param userId
     * @return
     */
    Long delete(String pageId, String userId);

    /**
     * Count the number of pages for the year
     *
     * @param userId
     * @return
     */
    Long count(String userId);

    /**
     * Get all pages mapped to year for the user
     *
     * @param limit
     * @param offset
     * @param userId
     * @return
     */
    List<Page> getAll(int limit, int offset, String userId);
}
