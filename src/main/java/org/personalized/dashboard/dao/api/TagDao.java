package org.personalized.dashboard.dao.api;

import org.personalized.dashboard.model.Entity;

import java.util.List;

/**
 * Created by sudan on 11/7/15.
 */
public interface TagDao {

    /**
     * update tags to a given entity and userId
     *
     * @param tags
     * @param entity
     * @param userId
     */
    Long update(List<String> tags, Entity entity, String userId);

}
