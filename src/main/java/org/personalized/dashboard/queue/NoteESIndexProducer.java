package org.personalized.dashboard.queue;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.personalized.dashboard.bootstrap.QueueBootstrap;
import org.personalized.dashboard.model.EntityType;
import org.personalized.dashboard.model.Note;
import org.personalized.dashboard.model.OperationType;
import org.personalized.dashboard.utils.FieldKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.util.Map;

/**
 * Created by sudan on 4/7/15.
 */
public class NoteESIndexProducer implements ESIndexProducer<Note> {

    private final Logger LOGGER = LoggerFactory.getLogger(NoteESIndexProducer.class);

    @Override
    public void enqueue(Note obj, EntityType entityType, OperationType operationType, String entityId,
                        String userId) {


        Gson gson = new GsonBuilder().create();
        Map<String, String> payload = Maps.newHashMap();
        payload.put(FieldKeys.ES_ID, entityId);
        payload.put(FieldKeys.ES_ENTITY_TYPE, entityType.name());
        payload.put(FieldKeys.ES_OP_TYPE, operationType.name());
        payload.put(FieldKeys.USER_ID, userId);
        if (operationType != OperationType.DELETE) {
            payload.put(FieldKeys.ES_TITLE, obj.getTitle());
            payload.put(FieldKeys.ES_DESCRIPTION, obj.getNote());
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
