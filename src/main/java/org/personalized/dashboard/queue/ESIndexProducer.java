package org.personalized.dashboard.queue;

import org.personalized.dashboard.model.EntityType;
import org.personalized.dashboard.model.OperationType;

/**
 * Created by sudan on 4/7/15.
 */
public interface ESIndexProducer<T> {

    void enqueue(T obj, EntityType entityType, OperationType operationType, String entityId, String userId);
}
