package org.personalized.dashboard.utils.validator;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.personalized.dashboard.model.EntityType;
import org.personalized.dashboard.model.SearchContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Created by sudan on 6/7/15.
 */

@ActiveProfiles("test")
public class SearchVallidationTest {

    private ValidationService searchValidationService;

    @Before
    public void initialize() {
        this.searchValidationService = new SearchValidationService();
    }

    @Test
    public void testSearchValidation() {

        SearchContext searchContext = new SearchContext();
        List<ErrorEntity> errorEntities = searchValidationService.validate(searchContext);
        Assert.assertEquals("Error count is 1", 1, errorEntities.size());
        Assert.assertEquals("Error name matches", ErrorCodes.INVALID_SEARCH_CONTEXT.name(), errorEntities
                .get(0).getName());
        Assert.assertEquals("Error description matches", "Search context cannot be empty",
                errorEntities.get(0).getDescription());

        searchContext = new SearchContext();
        List<EntityType> entityTypes = Lists.newArrayList();
        entityTypes.add(EntityType.BOOKMARK);
        searchContext.setEntityTypes(entityTypes);
        errorEntities = searchValidationService.validate(searchContext);
        Assert.assertEquals("Error count is 0", 0, errorEntities.size());

        searchContext = new SearchContext();
        List<String> titles = Lists.newArrayList();
        titles.add("title");
        searchContext.setTitles(titles);
        errorEntities = searchValidationService.validate(searchContext);
        Assert.assertEquals("Error count is 0", 0, errorEntities.size());

        searchContext = new SearchContext();
        List<String> tags = Lists.newArrayList();
        tags.add("one");
        searchContext.setTags(tags);
        errorEntities = searchValidationService.validate(searchContext);
        Assert.assertEquals("Error count is 0", 0, errorEntities.size());

        searchContext = new SearchContext();
        List<String> keywords = Lists.newArrayList();
        keywords.add("key");
        searchContext.setKeywords(keywords);
        errorEntities = searchValidationService.validate(searchContext);
        Assert.assertEquals("Error count is 0", 0, errorEntities.size());
    }

}
