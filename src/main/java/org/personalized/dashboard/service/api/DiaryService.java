package org.personalized.dashboard.service.api;

import org.personalized.dashboard.model.Page;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface DiaryService {

    /**
     * Create a new page for the user
     *
     * @param page
     * @return
     */
    String createPage(Page page);

    /**
     * Get page for the given pageId
     *
     * @param pageId
     * @return
     */
    Page getPage(String pageId);

    /**
     * Update page for the user
     *
     * @param page
     * @return
     */
    Long updatePage(Page page);

    /**
     * Delete the page given the pageId
     *
     * @param pageId
     * @return
     */
    Long deletePage(String pageId);

    /**
     * Count the pages for the given year
     *
     * @return
     */
    Long countPages();

    /**
     * Get all pages based on limit and offset
     *
     * @param limit
     * @param offset
     * @return
     */
    List<Page> getPages(int limit, int offset);
}
