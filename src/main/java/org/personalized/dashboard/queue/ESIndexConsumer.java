package org.personalized.dashboard.queue;

import com.google.gson.Gson;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.bootstrap.QueueBootstrap;
import org.personalized.dashboard.elasticsearch.ElasticsearchClient;
import org.personalized.dashboard.model.EntityType;
import org.personalized.dashboard.model.OperationType;
import org.personalized.dashboard.model.SearchDocument;
import org.personalized.dashboard.utils.FieldKeys;
import org.personalized.dashboard.utils.htmltidy.DOMParser;
import org.personalized.dashboard.utils.stopwords.StopwordsRemover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Map;

/**
 * Created by sudan on 4/7/15.
 */
public class ESIndexConsumer implements MessageListener {

    private final Logger LOGGER = LoggerFactory.getLogger(ESIndexConsumer.class);

    // TODO need to fix this ugly initialization
    private ElasticsearchClient elasticsearchClient = new ElasticsearchClient(new SessionManager());

    public ESIndexConsumer() throws JMSException {
        QueueBootstrap.getInstance().getMessageConsumer().setMessageListener(this);
    }

    @Override
    public void onMessage(Message message) {
        if (message != null) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String payload = textMessage.getText();
                Map<String, String> payloadMap = new Gson().fromJson(payload, Map.class);

                if (OperationType.valueOf(payloadMap.get(FieldKeys.ES_OP_TYPE)) == OperationType.DELETE) {
                    elasticsearchClient.delete(payloadMap.get(FieldKeys.ES_ID));
                } else if (OperationType.valueOf(payloadMap.get(FieldKeys.ES_OP_TYPE)) == OperationType.PATCH) {
                    elasticsearchClient.addTags(payloadMap.get(FieldKeys.ES_ID), payloadMap.get(FieldKeys.ENTITY_TAGS));
                } else {
                    SearchDocument searchDocument = new SearchDocument();
                    searchDocument.setDocumentId(payloadMap.get(FieldKeys.ES_ID));
                    searchDocument.setTitle(payloadMap.get(FieldKeys.ES_TITLE));
                    DOMParser domParser = new DOMParser();
                    String summary = domParser.extractSummary(payloadMap.get(FieldKeys.ES_DESCRIPTION));
                    searchDocument.setSummary(summary.substring(0, Math.min(summary.length(), 250)));
                    searchDocument.setDescription(
                            domParser.removeHtmlTags(
                                    StopwordsRemover.removeStopWords(payloadMap.get(FieldKeys.ES_DESCRIPTION))
                            )
                    );
                    searchDocument.setEntityType(EntityType.valueOf(payloadMap.get(FieldKeys.ES_ENTITY_TYPE)));
                    elasticsearchClient.insertOrUpdate(searchDocument, payloadMap.get(FieldKeys.USER_ID));
                }

            } catch (JMSException e) {
                LOGGER.error("Error in consuming message ", textMessage);
            }
        }
    }
}
