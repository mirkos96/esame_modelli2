package org.personalized.dashboard.dao.api;

import org.personalized.dashboard.model.Pin;

import java.util.List;

/**
 * Created by sudan on 3/4/15.
 */
public interface PinDao {


    /**
     * Create a new pin for the user
     *
     * @param pin
     * @param userId
     * @return
     */
    String create(Pin pin, String userId);

    /**
     * Get pin for the pinId and userId
     *
     * @param pinId
     * @param userId
     * @return
     */
    Pin get(String pinId, String userId);

    /**
     * Update the pin and return the updated one
     *
     * @param pin
     * @param userId
     * @return
     */
    Long update(Pin pin, String userId);

    /**
     * Delete the pin for the given pinId and userId
     *
     * @param pinId
     * @param userId
     * @return
     */
    Long delete(String pinId, String userId);

    /**
     * Count of pins for the userId
     */
    Long count(String userId);

    /**
     * Fetch all pins for the userId with limit and offset
     *
     * @param limit
     * @param offset
     * @param userId
     */
    List<Pin> get(int limit, int offset, String userId);

}
