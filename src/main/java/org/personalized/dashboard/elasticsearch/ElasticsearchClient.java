package org.personalized.dashboard.elasticsearch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.bootstrap.ESBootstrap;
import org.personalized.dashboard.model.EntityType;
import org.personalized.dashboard.model.SearchContext;
import org.personalized.dashboard.model.SearchDocument;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;

import java.util.List;
import java.util.Map;

/**
 * Created by sudan on 5/7/15.
 */
public class ElasticsearchClient {

    private final SessionManager sessionManager;

    @Inject
    public ElasticsearchClient(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public IndexResponse insertOrUpdate(SearchDocument searchDocument, String userId) {
        searchDocument.setCreatedAt(System.currentTimeMillis());
        IndexRequest indexRequest = new IndexRequest(
                ConfigKeys.ES_INDEX, ConfigKeys.ES_TYPE, searchDocument.getDocumentId()
        );
        Map<String, String> payload = Maps.newHashMap();
        payload.put(FieldKeys.ES_ID, searchDocument.getDocumentId());
        payload.put(FieldKeys.ES_TITLE, searchDocument.getTitle());
        if (StringUtils.isNotEmpty(searchDocument.getDescription())) {
            payload.put(FieldKeys.ES_DESCRIPTION, searchDocument.getDescription());
            payload.put(FieldKeys.ES_SUMMARY, searchDocument.getSummary());
        }
        payload.put(FieldKeys.ES_TIMESTAMP, String.valueOf(searchDocument.getCreatedAt()));
        payload.put(FieldKeys.ES_ENTITY_TYPE, searchDocument.getEntityType().name());
        payload.put(FieldKeys.USER_ID, userId);

        indexRequest.source(new Gson().toJson(payload));
        return ESBootstrap.getClient().index(indexRequest).actionGet();
    }

    public DeleteResponse delete(String documentId) {

        return ESBootstrap.getClient().prepareDelete(ConfigKeys.ES_INDEX,
                ConfigKeys.ES_TYPE, documentId).execute().actionGet();
    }

    public List<SearchDocument> search(SearchContext searchContext, String userId) {

        List<SearchDocument> searchDocuments = Lists.newArrayList();
        ESQueryBuilder esQueryBuilder = new ESQueryBuilder();

        SearchResponse searchResponse = ESBootstrap.getClient()
                .prepareSearch(ConfigKeys.ES_INDEX)
                .setTypes(ConfigKeys.ES_TYPE)
                .setQuery(esQueryBuilder.getQueryBuilder(searchContext, userId))
                .setFrom(Constants.ES_OFFSET)
                .setSize(Constants.ES_LIMIT)
                .addSort(SortBuilders.fieldSort(FieldKeys.ES_TIMESTAMP).order(SortOrder.DESC))
                .execute().actionGet();

        if (searchResponse.getHits().getTotalHits() > 0) {
            SearchHit[] searchHits = searchResponse.getHits().getHits();
            for (SearchHit searchHit : searchHits) {
                SearchDocument searchDocument = new SearchDocument();
                searchDocument.setDocumentId(searchHit.getId());
                searchDocument.setTitle(searchHit.getSource().get(FieldKeys.ES_TITLE).toString());

                searchDocument.setEntityType(EntityType.valueOf(
                        searchHit.getSource().get(FieldKeys.ES_ENTITY_TYPE).toString()
                ));
                if (searchHit.getSource().containsKey(FieldKeys.ES_SUMMARY)) {
                    searchDocument.setSummary(searchHit.getSource().get(FieldKeys.ES_SUMMARY).toString());
                }
                searchDocument.setCreatedAt(Long.valueOf(searchHit.getSource().get(FieldKeys.ES_TIMESTAMP).toString()));
                searchDocuments.add(searchDocument);
            }
        }

        return searchDocuments;
    }

    public UpdateResponse addTags(String entityId, String tags) {

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(ConfigKeys.ES_INDEX);
        updateRequest.type(ConfigKeys.ES_TYPE);
        updateRequest.id(entityId);

        IndexRequest indexRequest = new IndexRequest(
                ConfigKeys.ES_INDEX, ConfigKeys.ES_TYPE, entityId
        );
        Map<String, String> payload = Maps.newHashMap();
        payload.put(FieldKeys.ES_ID, entityId);
        payload.put(FieldKeys.ENTITY_TAGS, tags);
        payload.put(FieldKeys.ES_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        updateRequest.doc(indexRequest);

        indexRequest.source(new Gson().toJson(payload));
        return ESBootstrap.getClient().update(updateRequest).actionGet();
    }
}
