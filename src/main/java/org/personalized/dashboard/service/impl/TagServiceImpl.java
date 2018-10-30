package org.personalized.dashboard.service.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.dao.api.ActivityDao;
import org.personalized.dashboard.dao.api.TagDao;
import org.personalized.dashboard.model.*;
import org.personalized.dashboard.queue.ESIndexProducer;
import org.personalized.dashboard.service.api.TagService;
import org.personalized.dashboard.utils.generator.ActivityGenerator;

/**
 * Created by sudan on 11/7/15.
 */
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final SessionManager sessionManager;
    private final ActivityGenerator activityGenerator;
    private final ActivityDao activityDao;
    private final ESIndexProducer esIndexProducer;

    @Inject
    public TagServiceImpl(TagDao tagDao, SessionManager sessionManager,
                          ActivityGenerator activityGenerator,
                          ActivityDao activityDao,
                          @Named("tag") ESIndexProducer esIndexProducer) {
        this.tagDao = tagDao;
        this.sessionManager = sessionManager;
        this.activityGenerator = activityGenerator;
        this.activityDao = activityDao;
        this.esIndexProducer = esIndexProducer;
    }

    @Override
    public Long updateTags(Tag tag) {
        Entity entity = tag.getEntity();
        Long modifiedCount = tagDao.update(tag.getTags(), entity, sessionManager.getUserIdFromSession());
        if (modifiedCount > 0) {
            Activity activity = activityGenerator.generate(ActivityType.TAG_ADDED, entity.getEntityType(),
                    entity.getEntityId(), entity.getTitle());
            activityDao.add(activity, sessionManager.getUserIdFromSession());
            esIndexProducer.enqueue(tag, entity.getEntityType(), OperationType.PATCH, entity.getEntityId(),
                    sessionManager.getUserIdFromSession());
        }
        return modifiedCount;
    }
}
