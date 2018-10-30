package org.personalized.dashboard.queue;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.personalized.dashboard.bootstrap.QueueBootstrap;
import org.personalized.dashboard.model.EntityType;
import org.personalized.dashboard.model.Expense;
import org.personalized.dashboard.model.OperationType;
import org.personalized.dashboard.utils.Constants;
import org.personalized.dashboard.utils.FieldKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.util.Map;

/**
 * Created by sudan on 12/7/15.
 */
public class ExpenseESIndexProducer implements ESIndexProducer<Expense> {

    private final Logger LOGGER = LoggerFactory.getLogger(ExpenseESIndexProducer.class);

    @Override
    public void enqueue(Expense obj, EntityType entityType, OperationType operationType, String entityId,
                        String userId) {

        Gson gson = new GsonBuilder().create();
        Map<String, String> payload = Maps.newHashMap();
        payload.put(FieldKeys.ES_ID, entityId);
        payload.put(FieldKeys.ES_ENTITY_TYPE, entityType.name());
        payload.put(FieldKeys.ES_OP_TYPE, operationType.name());
        payload.put(FieldKeys.USER_ID, userId);
        if (operationType != OperationType.DELETE) {
            payload.put(FieldKeys.ES_TITLE, obj.getTitle());
            StringBuilder description = new StringBuilder();
            description.append(obj.getAmount());
            description.append(Constants.SECONDARY_SEPARATOR);
            description.append(obj.getDescription());

            if (!CollectionUtils.isEmpty(obj.getCategories())) {
                description.append(Constants.SECONDARY_SEPARATOR);
                for (String category : obj.getCategories()) {
                    description.append(category);
                    description.append(Constants.SEPARATOR);
                }
            }
            payload.put(FieldKeys.ES_DESCRIPTION, description.toString());
        }
        String text = gson.toJson(payload);
        MessageProducer producer = QueueBootstrap.getInstance().getMessageProducer();
        Session session = QueueBootstrap.getInstance().getSession();

        try {
            producer.send(session.createTextMessage(text));
        } catch (JMSException e) {
            LOGGER.error("Exception in enqueueing ", obj, "with ", entityId, " for ", operationType, " for es indexing");
        }
    }
}
