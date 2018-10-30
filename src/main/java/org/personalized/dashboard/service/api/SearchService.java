package org.personalized.dashboard.service.api;

import org.personalized.dashboard.model.SearchContext;
import org.personalized.dashboard.model.SearchDocument;

import java.util.List;

/**
 * Created by sudan on 6/7/15.
 */
public interface SearchService {

    /**
     * Returns list of documents
     *
     * @param searchContext
     * @return
     */
    List<SearchDocument> searchEntities(SearchContext searchContext);
}
