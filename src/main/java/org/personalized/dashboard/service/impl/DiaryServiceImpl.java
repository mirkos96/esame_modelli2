package org.personalized.dashboard.service.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.lang3.StringUtils;
import org.personalized.dashboard.auth.SessionManager;
import org.personalized.dashboard.dao.api.ActivityDao;
import org.personalized.dashboard.dao.api.DiaryDao;
import org.personalized.dashboard.model.*;
import org.personalized.dashboard.queue.ESIndexProducer;
import org.personalized.dashboard.service.api.DiaryService;
import org.personalized.dashboard.utils.generator.ActivityGenerator;
import org.personalized.dashboard.utils.htmltidy.DOMParser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
@Repository
public class DiaryServiceImpl implements DiaryService {

    private final DiaryDao diaryDao;
    private final SessionManager sessionManager;
    private final ActivityGenerator activityGenerator;
    private final ActivityDao activityDao;
    private final ESIndexProducer esIndexProducer;
    private final DOMParser domParser;

    @Inject
    public DiaryServiceImpl(DiaryDao diaryDao, SessionManager sessionManager,
                            ActivityGenerator activityGenerator,
                            ActivityDao activityDao,
                            @Named("page") ESIndexProducer esIndexProducer,
                            DOMParser domParser) {
        this.diaryDao = diaryDao;
        this.sessionManager = sessionManager;
        this.activityGenerator = activityGenerator;
        this.activityDao = activityDao;
        this.esIndexProducer = esIndexProducer;
        this.domParser = domParser;
    }

    @Override
    public String createPage(Page page) {
        page.setContent(domParser.removeMalformedTags(page.getContent()));
        page.setSummary(domParser.extractSummary(page.getContent()));
        String pageId = diaryDao.create(page, sessionManager.getUserIdFromSession());
        Activity activity = activityGenerator.generate(ActivityType.CREATED, EntityType.DIARY,
                pageId, page.getTitle());
        activityDao.add(activity, sessionManager.getUserIdFromSession());
        esIndexProducer.enqueue(page, EntityType.DIARY, OperationType.CREATE, pageId,
                sessionManager.getUserIdFromSession());
        return pageId;
    }

    @Override
    public Page getPage(String pageId) {
        return diaryDao.get(pageId, sessionManager.getUserIdFromSession());
    }

    @Override
    public Long updatePage(Page page) {
        page.setContent(domParser.removeMalformedTags(page.getContent()));
        page.setSummary(domParser.extractSummary(page.getContent()));
        Long modifiedCount = diaryDao.update(page, sessionManager.getUserIdFromSession());
        if (modifiedCount > 0) {
            Activity activity = activityGenerator.generate(ActivityType.UPDATED, EntityType.DIARY,
                    page.getPageId(), page.getTitle());
            activityDao.add(activity, sessionManager.getUserIdFromSession());
            esIndexProducer.enqueue(page, EntityType.DIARY, OperationType.UPDATE, page.getPageId(),
                    sessionManager.getUserIdFromSession());
        }
        return modifiedCount;
    }

    @Override
    public Long deletePage(String pageId) {
        Long deletedCount = diaryDao.delete(pageId, sessionManager.getUserIdFromSession());
        if (deletedCount > 0) {
            Activity activity = activityGenerator.generate(ActivityType.DELETED, EntityType.DIARY,
                    pageId, StringUtils.EMPTY);
            activityDao.add(activity, sessionManager.getUserIdFromSession());
            esIndexProducer.enqueue(null, EntityType.DIARY, OperationType.DELETE, pageId,
                    sessionManager.getUserIdFromSession());
        }
        return deletedCount;
    }

    @Override
    public Long countPages() {
        return diaryDao.count(sessionManager.getUserIdFromSession());
    }

    @Override
    public List<Page> getPages(int limit, int offset) {
        return diaryDao.getAll(limit, offset, sessionManager.getUserIdFromSession());
    }
}
