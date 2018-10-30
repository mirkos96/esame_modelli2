package org.personalized.dashboard.service.impl;

import com.google.inject.Inject;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.elasticsearch.ElasticsearchClient;
import org.personalized.dashboard.model.SearchContext;
import org.personalized.dashboard.model.SearchDocument;
import org.personalized.dashboard.service.api.SearchService;

import java.util.List;

/**
 * Created by sudan on 6/7/15.
 */
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchClient elasticsearchClient;
    private final SessionManager sessionManager;

    @Inject
    public SearchServiceImpl(ElasticsearchClient elasticsearchClient, SessionManager sessionManager) {
        this.elasticsearchClient = elasticsearchClient;
        this.sessionManager = sessionManager;
    }

    @Override
    public List<SearchDocument> searchEntities(SearchContext searchContext) {
        return elasticsearchClient.search(searchContext, sessionManager.getUserIdFromSession());
    }
}
