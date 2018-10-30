package org.personalized.dashboard.elasticsearch;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.personalized.dashboard.model.EntityType;
import org.personalized.dashboard.model.SearchContext;
import org.personalized.dashboard.utils.FieldKeys;
import org.springframework.util.CollectionUtils;

/**
 * Created by sudan on 11/7/15.
 */
public class ESQueryBuilder {

    public BoolQueryBuilder getQueryBuilder(SearchContext searchContext, String userId) {

        BoolQueryBuilder searchQueryBuilder = new BoolQueryBuilder();

        BoolQueryBuilder userIdQueryBuilder = new BoolQueryBuilder();
        userIdQueryBuilder.must(QueryBuilders.termQuery(FieldKeys.USER_ID, userId));
        searchQueryBuilder.must(userIdQueryBuilder);

        if (!CollectionUtils.isEmpty(searchContext.getEntityTypes())) {
            BoolQueryBuilder entityTypeQueryBuilder = new BoolQueryBuilder();
            for (EntityType entityType : searchContext.getEntityTypes()) {
                entityTypeQueryBuilder.should(QueryBuilders.matchQuery(FieldKeys.ES_ENTITY_TYPE, entityType.name()));
            }
            searchQueryBuilder.must(entityTypeQueryBuilder);
        }

        if (!CollectionUtils.isEmpty(searchContext.getTitles())) {
            BoolQueryBuilder titleQueryBuilder = new BoolQueryBuilder();
            for (String title : searchContext.getTitles()) {
                titleQueryBuilder.should(QueryBuilders.fuzzyQuery(FieldKeys.ES_TITLE, title));
            }
            searchQueryBuilder.must(titleQueryBuilder);
        }

        if (!CollectionUtils.isEmpty(searchContext.getKeywords())) {
            BoolQueryBuilder keywordQueryBuilder = new BoolQueryBuilder();
            for (String keyword : searchContext.getKeywords()) {
                keywordQueryBuilder.should(QueryBuilders.fuzzyQuery(FieldKeys.ES_DESCRIPTION, keyword));
            }
            searchQueryBuilder.must(keywordQueryBuilder);
        }

        if (!CollectionUtils.isEmpty(searchContext.getTags())) {
            BoolQueryBuilder tagQueryBuilder = new BoolQueryBuilder();
            for (String tag : searchContext.getTags()) {
                tagQueryBuilder.should(QueryBuilders.fuzzyQuery(FieldKeys.ENTITY_TAGS, tag));
            }
            searchQueryBuilder.must(tagQueryBuilder);
        }

        return searchQueryBuilder;
    }

}
