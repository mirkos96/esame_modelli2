package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.model.SearchContext;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by sudan on 6/7/15.
 */
public class SearchValidationService implements ValidationService<SearchContext> {

    @Override
    public List<ErrorEntity> validate(SearchContext searchContext) {

        List<ErrorEntity> errorEntities = Lists.newArrayList();
        if (CollectionUtils.isEmpty(searchContext.getEntityTypes()) &&
                CollectionUtils.isEmpty(searchContext.getTitles()) &&
                CollectionUtils.isEmpty(searchContext.getKeywords()) &&
                CollectionUtils.isEmpty(searchContext.getTags())) {
            ErrorEntity errorEntity = new ErrorEntity(ErrorCodes.INVALID_SEARCH_CONTEXT.name(),
                    ErrorCodes.INVALID_SEARCH_CONTEXT.getDescription(),
                    StringUtils.EMPTY);
            errorEntities.add(errorEntity);
        }
        return errorEntities;
    }
}