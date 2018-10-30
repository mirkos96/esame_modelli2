package org.personalized.dashboard.elasticsearch;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.bootstrap.ESBootstrap;
import org.personalized.dashboard.model.EntityType;
import org.personalized.dashboard.model.SearchContext;
import org.personalized.dashboard.model.SearchDocument;
import org.personalized.dashboard.utils.ConfigKeys;
import org.personalized.dashboard.utils.htmltidy.DOMParser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * Created by sudan on 11/7/15.
 */
@ActiveProfiles("test")
public class ElasticSearchClientTest {

    private ElasticsearchClient elasticsearchClient = new ElasticsearchClient(new SessionManager());

    @Test
    public void testElasticSearchClient() throws Exception {

        Boolean isDebugMode = Boolean.valueOf(ConfigKeys.ES_DEBUG_FLAG);

        /**
         * Use a different fresh type in debugMode by changing  elasticsearch.type in config.properties
         */
        if (isDebugMode) {

            ESBootstrap.init();
            insertSampleData();

            /**
             * This is to allow elasticsearch to index
             */
            Thread.sleep(1000L);

            SearchContext searchContext = new SearchContext();
            List<EntityType> entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.BOOKMARK);
            searchContext.setEntityTypes(entityTypes);
            List<SearchDocument> searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total bookmark count is 2", 2, searchDocuments.size());

            searchContext = new SearchContext();
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.NOTE);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total note count is 2", 2, searchDocuments.size());

            searchContext = new SearchContext();
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.TODO);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total todo count is 2", 2, searchDocuments.size());

            searchContext = new SearchContext();
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.PIN);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total pin count is 1", 1, searchDocuments.size());

            searchContext = new SearchContext();
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.BOOKMARK);
            searchContext.setEntityTypes(entityTypes);
            List<String> keywords = Lists.newArrayList();
            keywords.add("Microsoft");
            searchContext.setKeywords(keywords);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search result count is 1", 1, searchDocuments.size());

            searchContext = new SearchContext();
            keywords = Lists.newArrayList();
            keywords.add("language");
            keywords.add("engine");
            searchContext.setKeywords(keywords);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 4", 4, searchDocuments.size());

            searchContext = new SearchContext();
            keywords = Lists.newArrayList();
            keywords.add("technology");
            searchContext.setKeywords(keywords);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 1", 1, searchDocuments.size());

            searchContext = new SearchContext();
            List<String> titles = Lists.newArrayList();
            titles.add("bing");
            titles.add("python");
            searchContext.setTitles(titles);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 2", 2, searchDocuments.size());

            searchContext = new SearchContext();
            keywords = Lists.newArrayList();
            keywords.add("language");
            searchContext.setKeywords(keywords);
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.PIN);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 0", 0, searchDocuments.size());

            searchContext = new SearchContext();
            keywords = Lists.newArrayList();
            keywords.add("language");
            searchContext.setKeywords(keywords);
            titles = Lists.newArrayList();
            titles.add("python");
            searchContext.setTitles(titles);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 1", 1, searchDocuments.size());

            searchContext = new SearchContext();
            List<String> tags = Lists.newArrayList();
            tags.add("language");
            tags.add("java");
            searchContext.setTags(tags);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 3", 3, searchDocuments.size());

            searchContext = new SearchContext();
            tags = Lists.newArrayList();
            tags.add("microsoft");
            searchContext.setTags(tags);
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.DIARY);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 0", 0, searchDocuments.size());

            searchContext = new SearchContext();
            tags = Lists.newArrayList();
            tags.add("bookmark");
            tags.add("note");
            searchContext.setTags(tags);
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.NOTE);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 2", 2, searchDocuments.size());

            searchContext = new SearchContext();
            tags = Lists.newArrayList();
            tags.add("python");
            searchContext.setTags(tags);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 2", 2, searchDocuments.size());

            searchContext = new SearchContext();
            tags = Lists.newArrayList();
            tags.add("python");
            searchContext.setTags(tags);
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.EXPENSE);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 1", 1, searchDocuments.size());

            searchContext = new SearchContext();
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.DIARY);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 2", 2, searchDocuments.size());

            searchContext = new SearchContext();
            tags = Lists.newArrayList();
            tags.add("cleopatra");
            searchContext.setTags(tags);
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.DIARY);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 1", 1, searchDocuments.size());


            searchContext = new SearchContext();
            tags = Lists.newArrayList();
            tags.add("sanjee");
            searchContext.setTags(tags);
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.DIARY);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total search count is 0", 0, searchDocuments.size());


            elasticsearchClient.delete("BOK123456789");
            Thread.sleep(1000L);

            searchContext = new SearchContext();
            entityTypes = Lists.newArrayList();
            entityTypes.add(EntityType.BOOKMARK);
            searchContext.setEntityTypes(entityTypes);
            searchDocuments = elasticsearchClient.search(searchContext, "1");
            Assert.assertEquals("Total bookmark count is 1", 1, searchDocuments.size());
        }

    }

    public void insertSampleData() {

        SearchDocument searchDocument = new SearchDocument();
        searchDocument.setDocumentId("BOK123456789");
        searchDocument.setTitle("BingSearch");
        searchDocument.setEntityType(EntityType.BOOKMARK);
        searchDocument.setDescription("Bing is Microsoft product. It is a very good search engine");
        elasticsearchClient.insertOrUpdate(searchDocument, "1");

        searchDocument = new SearchDocument();
        searchDocument.setDocumentId("BOK234567890");
        searchDocument.setTitle("YahooSearch");
        searchDocument.setEntityType(EntityType.BOOKMARK);
        searchDocument.setDescription("Yahoo search engine ");
        elasticsearchClient.insertOrUpdate(searchDocument, "1");

        searchDocument = new SearchDocument();
        searchDocument.setDocumentId("TOD234567890");
        searchDocument.setTitle("TodoList");
        searchDocument.setEntityType(EntityType.TODO);
        searchDocument.setDescription("My Todo List contains learning technologies");
        elasticsearchClient.insertOrUpdate(searchDocument, "1");

        searchDocument = new SearchDocument();
        searchDocument.setDocumentId("TOD134567890");
        searchDocument.setTitle("SecondtodoList");
        searchDocument.setEntityType(EntityType.TODO);
        searchDocument.setDescription("Core Java is in backlog");
        elasticsearchClient.insertOrUpdate(searchDocument, "1");

        searchDocument = new SearchDocument();
        searchDocument.setDocumentId("NOT234567890");
        searchDocument.setTitle("Java");
        searchDocument.setEntityType(EntityType.NOTE);
        searchDocument.setDescription("Java Virtual Machine. Java is a programming Language");
        elasticsearchClient.insertOrUpdate(searchDocument, "1");

        searchDocument = new SearchDocument();
        searchDocument.setDocumentId("NOT234567891");
        searchDocument.setTitle("Python");
        searchDocument.setEntityType(EntityType.NOTE);
        searchDocument.setDescription("Python is an interpreted language");
        elasticsearchClient.insertOrUpdate(searchDocument, "1");


        searchDocument = new SearchDocument();
        searchDocument.setDocumentId("PIN234567890");
        searchDocument.setTitle("Sample pin");
        searchDocument.setEntityType(EntityType.PIN);
        searchDocument.setDescription("Pin for testing users");
        elasticsearchClient.insertOrUpdate(searchDocument, "1");

        searchDocument = new SearchDocument();
        searchDocument.setDocumentId("EXP123456789");
        searchDocument.setTitle("sample expense");
        searchDocument.setDescription("marriage bought ##python");
        searchDocument.setEntityType(EntityType.EXPENSE);
        elasticsearchClient.insertOrUpdate(searchDocument, "1");

        String description = "<html><body><li>sudan</li><li>personal diary</li></body></html>";
        searchDocument = new SearchDocument();
        searchDocument.setDocumentId("PAG123456789");
        searchDocument.setTitle("sample page");
        DOMParser domParser = new DOMParser();
        searchDocument.setDescription(domParser.removeHtmlTags(description));
        searchDocument.setEntityType(EntityType.DIARY);
        elasticsearchClient.insertOrUpdate(searchDocument, "1");

        description = "<html><body><li>sudan</li><li>cleopatra diary</li></body></html>";
        searchDocument = new SearchDocument();
        searchDocument.setDocumentId("PAG123456749");
        searchDocument.setTitle("sample page");
        domParser = new DOMParser();
        searchDocument.setDescription(domParser.removeHtmlTags(description));
        searchDocument.setEntityType(EntityType.DIARY);
        elasticsearchClient.insertOrUpdate(searchDocument, "1");

        String tags = "bookmark microsoft product";
        elasticsearchClient.addTags("BOK123456789", tags);

        tags = "bookmark yahoo";
        elasticsearchClient.addTags("BOK234567890", tags);

        tags = "todo java";
        elasticsearchClient.addTags("TOD134567890", tags);

        tags = "note java language";
        elasticsearchClient.addTags("NOT234567890", tags);

        tags = "note python language";
        elasticsearchClient.addTags("NOT234567891", tags);

        tags = "python marriage";
        elasticsearchClient.addTags("EXP123456789", tags);

        tags = "diary personal";
        elasticsearchClient.addTags("PAG123456789", tags);

        tags = "cleopatra";
        elasticsearchClient.addTags("PAG123456749", tags);
    }
}
